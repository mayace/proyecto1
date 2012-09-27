package gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

public class CeProjectManager extends JPanel {

    private boolean already_exists(File file) {
        if(tree_map.containsKey(getKey(file))){
            return true;
        }
        else{
            return false;
        }
    }

    private void addNodo(File file, DefaultMutableTreeNode parent) {
        String key = getKey(file);
        DefaultMutableTreeNode child = analize_folder(file);
        parent.add(child);
        tree_map.put(key, child);
        
        //fire event
        fire_CeNodeAdded(child);
    }

    public JTree getTree() {
        return tree;
    }
    
    //<editor-fold defaultstate="collapsed" desc="CLASS CeNodoItem">
    public class CeNode{
        File    file;
        boolean root;

        public CeNode(File file, boolean root) {
            this.file = file;
            this.root = root;
        }
        
        public CeNode(File file) {
            this.file = file;
            this.root = false;
        }
        
        public File getFile() {
            return file;
        }

        public boolean isRoot() {
            return root;
        }
        
        
        @Override
        public String toString() {
            return file.getName();
        }
        
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="CLASS CeRender">
    public class CeRender extends DefaultTreeCellRenderer{
        
        Icon    root_icon;
        Icon    file_icon;
        Icon    folder_icon;
        Icon    error_icon; // the file does not exists

        public CeRender(Icon root_icon, Icon file_icon, Icon folder_icon, Icon error_icon) {
            this.root_icon = root_icon;
            this.file_icon = file_icon;
            this.folder_icon = folder_icon;
            this.error_icon = error_icon;
        }
        
        
       
        
        
        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
            
            final CeNode ce_obj = (CeNode)((DefaultMutableTreeNode)value).getUserObject();
            
            final File ce_obj_file = ce_obj.getFile();
            
            if(ce_obj.isRoot()){
                setIcon(root_icon);
            }
            else if(!ce_obj_file.exists()){
                setIcon(error_icon);
            }
            else if(ce_obj_file.isDirectory()){
                setIcon(folder_icon);
            }
            else{
                setIcon(file_icon);
            }
            
            return this;
        }
        
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="EVENT CeNodeClickListener">
    
    //<editor-fold defaultstate="collapsed" desc="CLASS CeNodeClickedEvent">
    public class CeNodeClickedEvent extends java.util.EventObject{
        DefaultMutableTreeNode  node;
        MouseEvent              me;
        
        public CeNodeClickedEvent(DefaultMutableTreeNode node, MouseEvent me, Object source) {
            super(source);
            this.node = node;
            this.me = me;
        }
        
        public DefaultMutableTreeNode getNode() {
            return node;
        }
        
        public MouseEvent getMe() {
            return me;
        }
        
    }
    //</editor-fold>
    
    public interface CeNodeClickListener{
        public void CeNodeclicked(CeNodeClickedEvent cevent);
    }
    ArrayList<CeNodeClickListener>  list    =   new ArrayList<>();
    public void addCeNodeClick(CeNodeClickListener toadd){
        list.add(toadd);
    }
    private void fire_CeNodeClicked(CeNodeClickedEvent cevent){
        for (CeNodeClickListener listener : list) {
            listener.CeNodeclicked(cevent);
        }
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="EVENT CeNodeAdded">
    ArrayList<CeNodeAddedListener>  list1    =   new ArrayList<>();
    public interface CeNodeAddedListener{
        public void CeNodeAdded(DefaultMutableTreeNode node);
    }
    public void addCeNodeAddedListener(CeNodeAddedListener listener){
        list1.add(listener);
    }
    private void fire_CeNodeAdded(DefaultMutableTreeNode node){
        for (CeNodeAddedListener listener : list1) {
            listener.CeNodeAdded(node);
        }
    }
    //</editor-fold>
    
    public void addTreeModelListener(TreeModelListener l){
        tree.getModel().addTreeModelListener(l);
    }
    
    
    private JTree tree;
    HashMap<String, DefaultMutableTreeNode> tree_map;

    public CeProjectManager() {
        tree_map = new HashMap<>();
        createContent(this);
    }

    private void createContent(final JPanel base) {
        DefaultMutableTreeNode tree_root = new DefaultMutableTreeNode(new CeNode(new File("PROJECTS"),true));
        
        JScrollPane tree_scroll     = new JScrollPane();
        final JTree       tree      = new JTree(tree_root);
        JToolBar    toolbar         = new JToolBar("Toolbar");
        JButton     toolbar_new     = new JButton("New");
        JButton     toolbar_delete  = new JButton("Delete");
        JButton     toolbar_refresh = new JButton("Refresh");
        
        final CeProjectManager   temp_this    =   this;
        tree.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent me) {
                TreePath node_path = tree.getPathForLocation(me.getX(), me.getY());
                
                if(node_path!=null){
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) node_path.getLastPathComponent();
                    fire_CeNodeClicked(new CeNodeClickedEvent(node, me, temp_this));
                }
            }

            @Override
            public void mousePressed(MouseEvent me) {
            }

            @Override
            public void mouseReleased(MouseEvent me) {
            }

            @Override
            public void mouseEntered(MouseEvent me) {
            }

            @Override
            public void mouseExited(MouseEvent me) {
            }
        });
        tree_scroll.setViewportView(tree);
        tree.setCellRenderer(new CeRender(UIManager.getIcon("FileChooser.homeFolderIcon"),UIManager.getIcon("FileView.fileIcon"), UIManager.getIcon("FileView.directoryIcon"), UIManager.getIcon("OptionPane.errorIcon")));

        toolbar.add(toolbar_new);
        toolbar.add(toolbar_delete);
        toolbar.add(toolbar_refresh);
        toolbar_new.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                JPopupMenu popup        = new JPopupMenu();
                JMenuItem popup_project = new JMenuItem("Project", UIManager.getIcon("FileChooser.homeFolderIcon"));
                JMenuItem popup_file    = new JMenuItem("File", UIManager.getIcon("FileView.fileIcon"));
                JMenuItem popup_folder  = new JMenuItem("Folder", UIManager.getIcon("FileView.directoryIcon"));

                popup_project.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent arg0) {
                        JFileChooser filechooser = new JFileChooser();
                        filechooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                        int i = filechooser.showSaveDialog(base);
                        if (i == JFileChooser.APPROVE_OPTION) {
                            new_project(filechooser.getSelectedFile());
                        }
                    }
                });
                popup_file.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent arg0) {
                        int type = CeProjectManager.FILE;
                        String message = "File Name:";
                        newItem(message, type);                        
                    }

                    
                });
                popup_folder.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent arg0) {
                        int type = CeProjectManager.FOLDER;
                        String message = "Folder Name:";
                        newItem(message, type);  
                    }
                });

                popup.add(popup_project);
                popup.add(popup_file);
                popup.add(popup_folder);

                JButton source = (JButton) arg0.getSource();
                popup.show(source, 0, source.getY() + source.getHeight());

            }
        });
        toolbar_delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                ArrayList<DefaultMutableTreeNode> selectedNodes = getSelectedNodes();
                
                for (DefaultMutableTreeNode node : selectedNodes) {
                    int     level   =   node.getLevel();
                    
                    if(level==0){
                        //root no se elimina
                    }
                    else if(level==1){
                        String[] options = new String[]{"Yes","No","cancel"};
                        int option = JOptionPane.showOptionDialog(tree,
                                                       "¿Desea eliminarlo del disco duro?",
                                                       "Remove Project",
                                                       JOptionPane.YES_NO_CANCEL_OPTION,
                                                       JOptionPane.QUESTION_MESSAGE,
                                                       null, 
                                                       options,
                                                       options[1]);
                        switch(option){
                            case JOptionPane.OK_OPTION:
                                removeNode(node, true);
                                break;
                            case JOptionPane.NO_OPTION:
                                removeNode(node, false);
                                break;
                            default:
                                
                        }
                    }
                    else{
                        removeNode(node, true);
                    }
                }
            }
        });
        toolbar_refresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                DefaultTreeModel        model   =   (DefaultTreeModel) tree.getModel();
                DefaultMutableTreeNode  root    =   (DefaultMutableTreeNode) model.getRoot();
                ArrayList<DefaultMutableTreeNode>   temp    =   new ArrayList<>();
                
                
                for (Enumeration children = root.children(); children.hasMoreElements();) {
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode)children.nextElement();
                    temp.add(node);
                }
                
                root.removeAllChildren();
                tree_map.clear();
                
                for (DefaultMutableTreeNode node_new : temp) {
                    File file_new = ((CeNode) node_new.getUserObject()).getFile();
                    if(file_new.exists()&&file_new.isDirectory()){
                        new_project(file_new);
                    }
                }
            }
        });

        base.setLayout(new BorderLayout());
        base.add(toolbar, BorderLayout.PAGE_START);
        base.add(tree_scroll, BorderLayout.CENTER);

        //
        this.tree = tree;

    }

    public void removeNode(DefaultMutableTreeNode node,boolean from_hd){
        DefaultTreeModel        model   =   (DefaultTreeModel) tree.getModel();
        DefaultMutableTreeNode parent   =   (DefaultMutableTreeNode) node.getParent();
        
        if(node.isLeaf()){
            node.removeFromParent();
            File file = ((CeNode)node.getUserObject()).getFile();
            tree_map.remove(getKey(file));
            if(from_hd) {
                file.delete();
            }
        }
        else{
            for(Enumeration children = node.children(); children.hasMoreElements();){
                DefaultMutableTreeNode child    =   (DefaultMutableTreeNode) children.nextElement();
                removeNode(child,from_hd);
            }
            removeNode(node,from_hd);
        }
        model.reload(parent);
    }
    
    private ArrayList<DefaultMutableTreeNode> getSelectedNodes() {
        TreePath[]                          paths = tree.getSelectionPaths();
        ArrayList<DefaultMutableTreeNode>   nodes = new ArrayList<>();
        
        if(paths!=null) {
            for (int i = 0; i < paths.length; i++) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) paths[i].getLastPathComponent();
                nodes.add(node);
            }
        }
        return nodes;
    }
    
    
    private ArrayList<DefaultMutableTreeNode> getSelectedDirNodes() {
        ArrayList<DefaultMutableTreeNode>   selectedNodes       = getSelectedNodes();
        ArrayList<DefaultMutableTreeNode>   selectedDirNodes    = new ArrayList<>();
        ArrayList<DefaultMutableTreeNode>   selectedFileNodes   = new ArrayList<>();
          
        
        for (DefaultMutableTreeNode node : selectedNodes) {
            File file = ((CeNode)node.getUserObject()).getFile();
            if(file.isDirectory()) {
                selectedDirNodes.add(node);
            }
            else {
                selectedFileNodes.add(node);
            }
        }
        
        if(!selectedFileNodes.isEmpty()){
            msg_error(String.format("Los siguientes no son directorios:\n%s", selectedFileNodes.toString()), "No es Folder Error");
        }
        
        return selectedDirNodes;
    }

    public DefaultMutableTreeNode getNode(File f){
        String key=getKey(f);
        return tree_map.get(key);
    }
    public String getKey(File f) {
        return f.getAbsolutePath();
    }

    /**
     * 
     * @param f
     * @return null if does not exists in any project
     */
    public DefaultMutableTreeNode getProjectNodeOf(File f){
        DefaultMutableTreeNode node_project = null;
        String key = getKey(f);
        if(already_exists(f)){
            DefaultMutableTreeNode get = tree_map.get(key);
            //1 porque los proyectos estan en el nivel 1, 0 es la raíz...
            node_project= getNodeAtLevel(1, get);
            
        }
        return node_project;
    }
    
    /**
     * Devuelve el nodo padre de 'node' en el nivel 'level'
     * @param level >=0
     * @param node 
     * @return null si 'node' es null o el nivel es mayor a el nivel del 'node' o puede ser que no lo encontro..
     */
    private DefaultMutableTreeNode getNodeAtLevel(int level,DefaultMutableTreeNode node){
        DefaultMutableTreeNode  nodeAtLevel   =   null;
        if(node!=null&&node.getLevel()>=level){
            DefaultMutableTreeNode  temp=node;
            boolean     found   =   false;
            
            if(temp.getLevel()==level) {
                found=true;
            }
            
            while(temp.getParent()!=null&&!found){
                temp=   (DefaultMutableTreeNode) temp.getParent();
                if(temp.getLevel()==level){
                    found=true;
                }
            }
            
            if(found){
                nodeAtLevel=temp;
            }
            
        }
        return nodeAtLevel;
    }
    
    private void new_project(File folder) {
        
        DefaultTreeModel        model       = (DefaultTreeModel) tree.getModel();
        DefaultMutableTreeNode  root        = (DefaultMutableTreeNode) model.getRoot();
        
        
        add_file(root,folder,CeProjectManager.FOLDER);
    }

    public static final int FILE    =   1;
    public static final int FOLDER  =   2;
 
  
    private void add_file(DefaultMutableTreeNode parent,File file,int type){
        
        if(file.exists()&&already_exists(file)){
            msg_info("Ya existe el archivo.", "Information");
        }
        else{
            try {
                if(type==CeProjectManager.FILE) {
                    file.createNewFile();
                }
                else {
                    file.mkdir();
                }
                
                addNodo(file, parent);
            } catch (IOException ex) {
                Logger.getLogger(CeProjectManager.class.getName()).log(Level.SEVERE, null, ex);
                msg_error(ex.getMessage(), "Create file error");
            }
        }
        
        ((DefaultTreeModel)tree.getModel()).reload(parent);
        
    }
    
    private void newItem(String message, int type) throws HeadlessException {
        ArrayList<DefaultMutableTreeNode> selectedDirNodes = getSelectedDirNodes();

        if (selectedDirNodes.isEmpty()) {
            msg_error("Ningun directorio seleccionado", "Ningun Directorio Error");
        } else {

            String filename = JOptionPane.showInputDialog(message);
            if (filename != null && !filename.trim().isEmpty()) {
                for (DefaultMutableTreeNode dirnode : selectedDirNodes) {
                    File parent = ((CeNode) dirnode.getUserObject()).getFile();

                    add_file(dirnode, new File(parent, filename), type);
                }
            }
        }
    }
    
    private DefaultMutableTreeNode analize_folder(File folder) {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(new CeNode(folder));

        if (folder.isDirectory()) {
            File[] listFiles = folder.listFiles();
            for (int i = 0; i < listFiles.length; i++) {
                File file = listFiles[i];
                if (file.isDirectory()) {
                    root.add(analize_folder(file));
                } else {
                    addNodo(file, root);
//                    DefaultMutableTreeNode node_new = new DefaultMutableTreeNode(new CeNode(file));
//                    root.add(node_new);
//                    addN
//                    String key = getKey(file);
//                    tree_map.put(key, root);
                }
            }
        }
        return root;
    }
    
    //<editor-fold defaultstate="collapsed" desc="Message Dialogs">
    private void msg_warning(String msg, String title) {
        msg_core(this, msg, title, JOptionPane.WARNING_MESSAGE);
    }
    
    private void msg_error(String msg, String title) {
        msg_core(this, msg, title, JOptionPane.WARNING_MESSAGE);
    }
    
    private void msg_info(String msg, String title) {
        msg_core(this, msg, title, JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void msg_core(Component parent, String message, String title, int type) {
        JOptionPane.showMessageDialog(parent, message, title, type);
    }
    //</editor-fold>
    
    
    

    //wtf...NO USADO....
    //<editor-fold defaultstate="collapsed" desc="CLASS CeDialog">
//    public class CeDialog extends JDialog {
//        
//        HashMap<String, Component> input;
//        JPanel body_panel;
//        
//        public CeDialog() {
//            this.setContentPane(createContent());
//        }
//        
//        private JPanel createContent() {
//            
//            JPanel base_panel = new JPanel(new BorderLayout());
//            JPanel base_body_panel = new JPanel();
//            JPanel base_button_panel = new JPanel();
//            JButton base_button_ok = new JButton("Ok");
//            JButton base_button_cancel = new JButton("Cancel");
//            
//            base_body_panel.setLayout(new BoxLayout(base_body_panel, BoxLayout.Y_AXIS));
//            base_body_panel.add(Box.createRigidArea(new Dimension(0, 20)));
//            
//            base_button_panel.setLayout(new BoxLayout(base_button_panel, BoxLayout.X_AXIS));
//            base_button_panel.add(Box.createHorizontalGlue());
//            base_button_panel.add(base_button_cancel);
//            base_button_panel.add(Box.createRigidArea(new Dimension(10, 0)));
//            base_button_panel.add(base_button_ok);
//            base_button_ok.addActionListener(new ActionListener() {
//                @Override
//                public void actionPerformed(ActionEvent arg0) {
//                    option = CeDialog.OK;
//                    exit();
//                }
//            });
//            base_button_cancel.addActionListener(new ActionListener() {
//                @Override
//                public void actionPerformed(ActionEvent arg0) {
//                    option = CeDialog.CANCEL;
//                    exit();
//                }
//            });
//            
//            base_panel.add(base_body_panel, BorderLayout.CENTER);
//            base_panel.add(base_button_panel, BorderLayout.PAGE_END);
//            //
//            this.body_panel = base_body_panel;
//            return base_panel;
//        }
//        
//        public void addJTextField(String key, String label) {
//            JPanel panel = getBodyItemPanel();
//            
//            panel.add(new JLabel(label));
//            panel.add(Box.createRigidArea(new Dimension(10, 0)));
//            panel.add(new JTextField());
//        }
//        
//        public void addJRadioButton(String key, String label, String selected, String... radio_list) {
//            JPanel panel = getBodyItemPanel();
//            JPanel radio_panel = new JPanel(new GridLayout(1, 0));
//            ButtonGroup radio_group = new ButtonGroup();
//            
//            for (int i = 0; i < radio_list.length; i++) {
//                String radio_val = radio_list[i];
//                JRadioButton radio = new JRadioButton(radio_val);
//                
//                radio.setActionCommand(radio_val);
//                radio_group.add(radio);
//                radio_panel.add(radio);
//                if (radio_val.toLowerCase().equals(selected.toLowerCase())) {
//                    radio.setSelected(true);
//                }
//            }
//            
//            
//            panel.add(new JLabel(label));
//            panel.add(Box.createRigidArea(new Dimension(10, 0)));
//            panel.add(radio_panel);
//        }
//        
//        private JPanel getBodyItemPanel() {
//            JPanel panel = new JPanel();
//            JPanel item_panel = new JPanel();
//            
//            item_panel.setLayout(new BoxLayout(item_panel, BoxLayout.X_AXIS));
//            
//            panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
//            panel.add(Box.createRigidArea(new Dimension(20, 0)));//margin left
//            panel.add(item_panel);
//            panel.add(Box.createRigidArea(new Dimension(20, 0)));//margin right
//            
//            body_panel.add(panel);
//            body_panel.add(Box.createRigidArea(new Dimension(0, 20)));//espacio para el sig.
//            
//            return item_panel;
//        }
//        
//        protected void exit() {
//            this.setVisible(false);
//        }
//        public static final int OK = 1;
//        public static final int CANCEL = 2;
//        int option = CeDialog.CANCEL;
//        
//        public int getOption() {
//            return this.option;
//        }
//    }
    //</editor-fold>
    
    
    public static void main(String[] args) {
        JFrame win = new JFrame();
        CeProjectManager cfm = new CeProjectManager();
        win.setContentPane(cfm);
        win.setSize(new Dimension(320, 240));
        win.setLocationRelativeTo(null);
        win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        win.setVisible(true);
        cfm.addCeNodeClick(new CeNodeClickListener() {

            @Override
            public void CeNodeclicked(CeNodeClickedEvent cevent) {
                System.out.println(cevent.getNode().getUserObjectPath());
            }
            
        });
        
    }
}
