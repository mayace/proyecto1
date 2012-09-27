package compiler;

import gui.CeEditor;

import java.io.StringReader;

import javax.swing.JEditorPane;
import javax.swing.JTextArea;

import compiler.logc.C_Parser;
import compiler.logc.C_Scanner;
import compiler.logl.L_Parser;
import compiler.logl.L_Scanner;
import compiler.logp.P_Parser;
import compiler.logp.P_Scanner;
import gui.CeConsole;
import gui.CeProjectManager;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java_cup.internal_error;
import javax.rmi.CORBA.Util;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public class CeParseUtils extends HashMap<String, Object> {


    public void setTreeNode(DefaultMutableTreeNode node){
        put("node",node);
    }
    public void setConsole(CeConsole console){
        put("console",console);
    }
    public void setDocument(DefaultStyledDocument document){
        put("document",document);
    }
    public void setFile(File f){
        put("file",f);
    }
    

    public DefaultTreeModel getModel(){
        return (DefaultTreeModel) getObject("model");
    }
    public CeConsole getConsole() {
        return (CeConsole) getObject("console");
    }
    
//    public CeConsole getConsole(String key) {
//        return (CeConsole) getObject(key);
//    }

    public DefaultStyledDocument getDocument() {
        return (DefaultStyledDocument) getObject("document");
    }
//    public DefaultStyledDocument getDocument(String key) {
//        return (DefaultStyledDocument) getObject(key);
//    }

//    public File getFile(String key) {
//        return (File) getObject(key);
//    }
    public File getFile() {
        return (File) getObject("file");
    }

    public DefaultMutableTreeNode getTreeNode(){
        return (DefaultMutableTreeNode) getObject("node");
    }
    
    public CeSymTable   getSymTable(String key){
        return (CeSymTable) getObject(key);
    }
    
    public Object getObject(String key){
        return this.get(key);
    }
    
    public void parse() throws  Exception {
        CeParseUtils utils = this;
        File file = utils.getFile();
        String extension = CeParseUtils.getExtension(file);
      
            if (extension != null) {
                switch (extension.trim().toLowerCase()) {
                    case "logl":
//                    L_Scanner ls = new L_Scanner(input);
//                    L_Parser lp = new L_Parser(ls);
//
////					ls.setStyledDoc(this.editor.getDoc());
//                    ls.setconsole(console);
//
//                    lp.setConsole(console);
//                    lp.parse();
                        break;
                    case "logp":
//                    P_Scanner ps = new P_Scanner(input);
//                    P_Parser pp = new P_Parser(ps);
//
////					ps.setStyledDoc(this.editor.getDoc());
//                    ps.setconsole(console);
//
//                    pp.setConsole(console);
//                    pp.parse();
                        break;
                    case "logc":
                        C_Scanner cs = new C_Scanner(new FileReader(file));
                        C_Parser cp = new C_Parser(cs);

                        cs.setUtils(utils);
                        cp.setUtils(utils);
                        cp.parse();
                        break;
                    default:
                        utils.getConsole().println("Extension no reconocida");
                }
            } else {
                utils.getConsole().println("Extension no reconocida.");
            }
       

    }

    public static String getExtension(File file) {
        String name = file.getName();
        int dot_index = name.lastIndexOf('.');

        String extension = (dot_index == -1 || dot_index + 1 == name.length() ? null : name.substring(dot_index + 1));
        return extension;
    }
    
    
    public static void parseCup(String parser,String sym,String cupfile) throws Exception{
        ArrayList<String>   args   =   new ArrayList<>() ;
        args.add("-parser");
        args.add(parser);
        args.add("-symbols");
        args.add(sym);
        args.add(cupfile);
        java_cup.Main.main(args.toArray(new String[0]));
    }
}
