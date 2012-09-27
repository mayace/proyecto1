package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.undo.UndoManager;

import compiler.CeFile;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JSplitPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.text.AttributeSet;
import javax.swing.text.Document;

public class CeEditor extends JPanel {

    public CeConsole getConsole() {
        return this.console;
    }

    
    //<editor-fold defaultstate="collapsed" desc="CLASS CeTextEditor">
    public class CeTextEditor extends JPanel{
        JEditorPane input;
        File        file;
        UndoManager input_undo;
        
        int _editor_line=1;
        
        public CeTextEditor(File file){
            //pre
            this.file=file;
            createContent(this);
            
            //pos
            try {
                loadText();
            } catch (IOException ex) {
                Logger.getLogger(CeEditor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        private void createContent(final JPanel base){
            
            JScrollPane     input_scroll    =   new JScrollPane();
            final JEditorPane     input_header    =   new JTextPane();
            final JEditorPane     input           =   new JTextPane();
            UndoManager     input_undo      =   new UndoManager();
            Font font = new Font("inconsolata", Font.PLAIN, 16);
            
            
            input.setFont(font);
            input.getDocument().addUndoableEditListener(input_undo);
            input.getDocument().addDocumentListener(new DocumentListener() {
                Document header_doc = input_header.getDocument();
                @Override
                public void removeUpdate(DocumentEvent e) {
                    char[] text = getText().toCharArray();
                    input_header.setText(" 1 ");
                    _editor_line = 1;
                    for (int i = 0; i < text.length; i++) {
                        if (Character.compare(text[i], '\n') == 0) {
                            _editor_line++;
                            append(header_doc,"\n " + _editor_line + " ",null);
                        }
                    }
                }

                @Override
                public void insertUpdate(DocumentEvent e) {
                    try {
                        String inserted = input.getText(e.getOffset(), e.getLength());
                        String[] split = inserted.split("\n");

                        if (split.length == 0) {
                            _editor_line++;
                            append(header_doc,"\n " + _editor_line + " ",null);
                        } else {
                            for (int i = 1; i < split.length; i++) {
                                _editor_line++;
                                append(header_doc,"\n " + _editor_line + " ",null);
                            }
                        }
                    } catch (BadLocationException exc) {
                        exc.printStackTrace();
                    }
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                }

                private void append(Document doc,String text,AttributeSet attr) {
                    try {
                        int length = doc.getLength()-1;
                        if(length<0) {
                            length=0;
                        }
                        doc.insertString(length, text,attr);
                    } catch (BadLocationException ex) {
                        Logger.getLogger(CeEditor.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });

            input_header.setFont(font);
            input_header.setBackground(Color.gray);
            input_header.setText(" 1 ");
            
            input_scroll.setViewportView(input);
            input_scroll.setRowHeaderView(input_header);
            
            base.setLayout(new BorderLayout());
            base.add(input_scroll);
            
            //
            this.input      =   input;
            this.input_undo =   input_undo;
        }
        
        public File getFile() {
            return file;
        }
        
        public UndoManager getUndoManager() {
            return input_undo;
        }
        
        private void loadText() throws IOException{
            File f= this.file;
            if(!f.exists()) {
                f.createNewFile();
            }
            byte[] bytes = getFileBytes();
            
            setText(new String(bytes));
        }
        
        public void saveText() throws IOException{
            Files.write(this.file.toPath(), getText().getBytes());
        }
        
        
        public String getText(){
            return this.input.getText();
        }
        public DefaultStyledDocument getDefaultStyledDocument(){
            return (DefaultStyledDocument) this.input.getDocument();
        }

        private byte[] getFileBytes() throws IOException {
            byte[] bytes = Files.readAllBytes(file.toPath());
            return bytes;
        }

        public void setText(String text) {
            this.input.setText(text);
        }
    }
    //</editor-fold>
    
    private CeConsole       console;
    private CeTabbedPane    tabbedPane;
    
    public CeEditor(){
        createContent(this);
    }
    

    private void createContent(final JPanel base) {
        final JSplitPane    base_splitpane1     =   new JSplitPane();
        JToolBar            base_toolbar        =   new JToolBar("Toolbar");
        JButton             base_toolbar_new    =   new JButton("New");
        JButton             base_toolbar_open   =   new JButton("Open");
        JButton             base_toolbar_save   =   new JButton("Save");
        final JButton             base_toolbar_undo   =   new JButton("Undo");
        final JButton             base_toolbar_redo   =   new JButton("Redo");
        JToggleButton       toolbar_console     =   new JToggleButton("Console");
        
        CeConsole        base_console        =   new CeConsole();
        
        JPanel              base_input_panel    =   new JPanel(new BorderLayout());
        final CeTabbedPane  input_tabbpane      =   new CeTabbedPane();
        
        JPanel              base_console_panel  =   new JPanel(new BorderLayout());
        
        base_toolbar_new.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                JFileChooser    filechooser =   getJFileChooser();
                int option = filechooser.showSaveDialog(base);
                
                if(option==JFileChooser.APPROVE_OPTION){
                    File selectedFile = filechooser.getSelectedFile();
                    addTab(selectedFile);
                }
            }
        });
        base_toolbar_open.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                JFileChooser filechooser = getJFileChooser();
                int option = filechooser.showOpenDialog(base);
                if(option==JFileChooser.APPROVE_OPTION){
                    File selectedFile = filechooser.getSelectedFile();
                    addTab(selectedFile);
                }
            }

            
        });
        base_toolbar_save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                CeTextEditor editor = getCurrentTabEditor();
                if(editor!=null){
                    try {
                        editor.saveText();
                    } catch (IOException ex) {
                        Logger.getLogger(CeEditor.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        toolbar_console.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent ie) {
                if(ie.getStateChange()==ItemEvent.SELECTED){
                    base_splitpane1.setDividerLocation(0.8);
                    base_splitpane1.setEnabled(true);
                }
                else{
                    base_splitpane1.setDividerLocation(0.999);
                    base_splitpane1.setEnabled(false);
                }
            }
        });
        base_toolbar.add(base_toolbar_new);
        base_toolbar.add(base_toolbar_open);
        base_toolbar.add(base_toolbar_save);
        base_toolbar.addSeparator();
        base_toolbar.add(base_toolbar_undo);
        base_toolbar.add(base_toolbar_redo);
        base_toolbar.addSeparator();
        base_toolbar.add(toolbar_console);
        
        input_tabbpane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent ce) {
                final CeTextEditor editor = getCurrentTabEditor();
                if (editor != null) {
                    final UndoManager undoManager = editor.getUndoManager();

                    base_toolbar_undo.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent ae) {
                            if (undoManager.canUndo()) {
                                undoManager.undo();
                            }
                        }
                    });
                    base_toolbar_redo.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent ae) {
                            if (undoManager.canRedo()) {
                                undoManager.redo();
                            }
                        }
                    });
                }
            }
        });
        base_input_panel.setBackground(Color.DARK_GRAY);
        base_input_panel.add(input_tabbpane);
        
        base_console_panel.setBackground(Color.black);
        base_console_panel.add(base_console);
        
        base_splitpane1.setOrientation(JSplitPane.VERTICAL_SPLIT);
        base_splitpane1.setTopComponent(base_input_panel);
        base_splitpane1.setBottomComponent(base_console_panel);
        base_splitpane1.setDividerLocation(1);
        
        base.setLayout(new BorderLayout());
        base.add(base_toolbar,BorderLayout.PAGE_START);
        base.add(base_splitpane1,BorderLayout.CENTER);
        //
        this.tabbedPane =   input_tabbpane;
        this.console    =   base_console;
    }

    
    private JFileChooser getJFileChooser() {
        JFileChooser filechooser = new JFileChooser();
        return filechooser;
    }
    
    /**
     *
     * @return -1 si no hay tab
     */
    public int getCurrentTabIndex() {
        return tabbedPane.getSelectedIndex();
    }
    /**
     *
     * @return null si no hay tab
     */
    public CeTextEditor getCurrentTabEditor() {
        int index = getCurrentTabIndex();
        
        return (index==-1?null:(CeTextEditor) tabbedPane.getComponentAt(index));
    }
    
    private void addTab(File selectedFile) {
        tabbedPane.addTab(selectedFile.getName(),new CeTextEditor(selectedFile));
    }
    
    public void openFile(File f){
        addTab(f);
    }
    
    ///kdsfjskldsñdf
    //<editor-fold defaultstate="collapsed" desc="unused">
//    public CeEditor(File file) throws IOException {
//        this._editor_file = new CeFile(file.getAbsolutePath());
//        this._editor_line = 1;
//        this._editor_undo = new UndoManager();
//        this._editor_header = new JTextArea();
//        this._editor_scrolls = new JScrollPane();
//        this._editor = new JTextPane();
//        
//        Font f = new Font("Inconsolata", 0, 16);
//        this._editor.setFont(f);
//        this._editor.getDocument().addDocumentListener(new DocumentListener() {
//            @Override
//            public void removeUpdate(DocumentEvent e) {
//                // TODO Auto-generated method stub
//                char[] text = _editor.getText().toCharArray();
//                _editor_header.setText(" 1 ");
//                _editor_line = 1;
//                for (int i = 0; i < text.length; i++) {
//                    if (Character.compare(text[i], '\n') == 0) {
//                        _editor_line++;
//                        _editor_header.append("\n " + _editor_line + " ");
//                    }
//                }
//            }
//            
//            @Override
//            public void insertUpdate(DocumentEvent e) {
//                // TODO Auto-generated method stub
//                try {
//                    String inserted = _editor.getText(e.getOffset(), e.getLength());
//                    String[] split = inserted.split("\n");
//                    
//                    if (split.length == 0) {
//                        _editor_line++;
//                        _editor_header.append("\n " + _editor_line + " ");
//                    } else {
//                        for (int i = 1; i < split.length; i++) {
//                            _editor_line++;
//                            _editor_header.append("\n " + _editor_line + " ");
//                        }
//                    }
//                } catch (BadLocationException exc) {
//                    exc.printStackTrace();
//                }
//            }
//            
//            @Override
//            public void changedUpdate(DocumentEvent e) {
//                // TODO Auto-generated method stub
//            }
//        });
//        
//        this._editor.getDocument().addUndoableEditListener(this._editor_undo);
//        
//        
//        this._editor_header.setFont(f);
//        this._editor_header.setFocusable(false);
//        this._editor_header.setBackground(Color.GRAY);
//        
//        this._editor_scrolls.setViewportView(this._editor);
//        this._editor_scrolls.setRowHeaderView(this._editor_header);
//        
//        
//        this.setLayout(new BorderLayout());
//        this.add(_editor_scrolls);
//        
//        if (file != null) {
//            byte[] _file_bytes = Files.readAllBytes(file.toPath());
//            this._editor.setText(new String(_file_bytes));
//        }
//        
//    }
//    
//    public void save() throws IOException {
//        Files.write(this._editor_file.toPath(), this._editor.getText().getBytes());
//    }
//    
//    public CeFile get_file() {
//        return _editor_file;
//    }
//    
//    public void set_file(File _file) {
//        this._editor_file = new CeFile(_file.getAbsolutePath());
//    }
//    
//    public String getText() {
//        return this._editor.getText();
//    }
//    
//    public DefaultStyledDocument getDoc() {
//        return (DefaultStyledDocument) this._editor.getDocument();
//    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="TEST main()">
    public static void main(String[] args) {
        JFrame win  =   new JFrame();
        CeEditor ceditor = new CeEditor();
        win.setContentPane(ceditor);
        win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        win.setSize(640, 480);
        win.setLocationRelativeTo(null);
        win.setVisible(true);
    }
    //</editor-fold>
}
