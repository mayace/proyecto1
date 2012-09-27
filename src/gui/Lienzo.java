package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.plaf.metal.MetalBorders;

public class Lienzo extends JPanel{

    
    private void createContent(final JPanel base) {
        JToolBar            toolbar         =   new JToolBar("Lienzo Toolbar");
        JButton             toolbar_dir     =   new JButton("Images");
        JCheckBox           toolbar_print   =   new JCheckBox("Print");
        final JComboBox     toolbar_list    =   new JComboBox();
        
        toolbar_dir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                JFileChooser    filechooser     =   new JFileChooser();
                filechooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int option = filechooser.showOpenDialog(base);
                if(option==JFileChooser.APPROVE_OPTION){
                    File selectedDir = filechooser.getSelectedFile();
                    File[] list = selectedDir.listFiles(new FilenameFilter() {
                        @Override
                        public boolean accept(File file, String name) {
                            boolean accepted = false;
                            String[] split = name.split("\\.");

                            if (split != null && split.length > 1) {
                                String extension = split[split.length - 1].toLowerCase();
                                switch (extension) {
                                    case "gif":
                                        accepted = true;
                                        break;
                                    case "ico":
                                        accepted = true;
                                        break;
                                    case "png":
                                        accepted = true;
                                        break;
                                    case "jpeg":
                                        accepted = true;
                                        break;
                                    case "jpg":
                                        accepted = true;
                                        break;
                                    case "tif":
                                        accepted = true;
                                        break;
                                    case "tiff":
                                        accepted = true;
                                        break;
                                    default:

                                }
                            }
                            return accepted;
                        }
                    });

                    DefaultComboBoxModel<File>    model=  new DefaultComboBoxModel<>(list);
                    toolbar_list.setModel(model);
                }
            }
        });
        
        toolbar_print.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                is_brush=!is_brush;
            }
        });
        toolbar_list.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                File f  =(File) toolbar_list.getSelectedItem();
                if(f!=null&&f.exists()){
                    bird.setImage(f.getAbsolutePath());
                    bird.setDefaults();
                }
            }
        });
        
        toolbar.add(toolbar_dir);
        toolbar.addSeparator();
        toolbar.add(toolbar_list);
        toolbar.addSeparator();
        toolbar.add(toolbar_print);
        add(toolbar);
    }

    
    
    //<editor-fold defaultstate="collapsed" desc="CLASS Bird">
    public final class Bird{
        private int     x       =   0;
        private int     y       =   0;
        private int     width   =   24;
        private int     height  =   24;
        private double  angle   =   0;           //grados
        
        private BufferedImage   image;
        
        
        public Bird(String filename) {
            setImage(filename);
            setDefaults();
        }
        
        
        public void setDefaults(){
            x           =   0;
            y           =   0;
            
            if(image!=null){
                width       =   image.getWidth();
                height      =   image.getHeight();
            }
            else{
                width       =   24;
                height      =   24;
            }
        }
        
        public void setImage(String filename){
            if(filename!=null&&!filename.trim().isEmpty()) {
                try {
                    image=ImageIO.read(new File(filename));
                } catch (IOException ex) {
                    Logger.getLogger(Lienzo.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else{
                image           =   new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2   =   image.createGraphics();
                
                g2.setColor(Color.yellow);
                g2.fillRect(0, 0, image.getWidth(), image.getHeight());
            }
        }
        
        
        public void paint(Graphics g){
            int max = (int) Math.sqrt(2*Math.pow(Math.max(image.getWidth(), image.getHeight()),2));//pitagoras...
            BufferedImage   bi  =   new BufferedImage(max, max, image.getType());
            Graphics2D g2 = bi.createGraphics();
            g2.setPaint(Color.ORANGE);
            g2.fillRect(0, 0, max, max);
            
            g2.translate(max/6,0);
            
            
            
            g2.rotate(Math.toRadians(15));
            g2.drawImage(image,0,0,null);
            
            g.drawImage(bi, x, y, null);
        }
        
        
        
        
        public int getX() {
            return x;
        }
        
        public void setX(int x) {
            this.x = x;
        }
        
        public int getY() {
            return y;
        }
        
        public void setY(int y) {
            this.y = y;
        }
        
        public int getWidth() {
            return width;
        }
        
        public void setWidth(int with) {
            this.width = with;
        }
        
        public int getHeight() {
            return height;
        }
        
        public void setHeight(int height) {
            this.height = height;
        }
        
        public Image getImage() {
            return image;
        }
        
        
    }
    //</editor-fold>
    
    Bird            bird        =   new Bird("/home/ce/Downloads/256px-KTurtle_logo.svg.png");
    boolean         is_brush    =   false;
    BufferedImage   image;
    double          angle       =   0;
    
    
    public Lienzo(){
        
        createContent(this);
        
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e){
                moveBird(e.getX(), e.getY());
            }
        });
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e){
                moveBird(e.getX(), e.getY());
            }
        });
        
    }
    
    private void moveBird(int x, int y){
        final int b_x = bird.getX();
        final int b_y = bird.getY();
        
        
        final int b_width   = bird.getWidth();
        final int b_height  = bird.getHeight();
        
        //repaint(b_x, b_y, b_width, b_height);
        repaint();
        
        bird.setX(x-bird.getWidth()/2);
        bird.setY(y-bird.getHeight()/2);
        //repaint(bird.getX(),bird.getY(),bird.getWidth(),bird.getHeight());
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D  g2  =   (Graphics2D) g;
        g2.drawRenderedImage(getImage(), AffineTransform.getTranslateInstance(0, 0));
        
        bird.paint(g);
    }
    
    
    
    private BufferedImage getImage() {
        int curr_width      = getWidth();
        int curr_height     = getHeight();
        if(image==null||curr_width!=image.getWidth()||curr_height!=image.getHeight()){
            BufferedImage   old =   image;
            image   =   new BufferedImage(curr_width, curr_height,BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = image.createGraphics();
            g2.setColor(Color.yellow);
            if(old!=null){
                g2.drawRenderedImage(old, AffineTransform.getTranslateInstance(0, 0));
            }
            g2.dispose();
        }
        return image;
    }
    @Override
    public Dimension getPreferredSize(){
        return new Dimension(480, 320);
    }
    
    
    
    
    
    public static void main(String args[]){
        JFrame  win =   new JFrame("Lienzo");
        win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        win.add(new Lienzo());
        win.pack();
        win.setLocationRelativeTo(null);
        
        win.setVisible(true);
    }
    
}
