package gui;


import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;

public class CeConsole extends JPanel {

    JEditorPane console;

    public CeConsole() {
        createContent(this);
    }

    private void createContent(JPanel base) {
        JScrollPane scroll = new JScrollPane();
        JEditorPane console = new JTextPane();
        JToolBar toolbar = new JToolBar("Console Toolbar");
        JButton toolbar_clear = new JButton("Clear");

        toolbar_clear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                clear();
            }
        });
        toolbar.add(toolbar_clear);

        scroll.setViewportView(console);

        base.setLayout(new BorderLayout());
        base.add(toolbar, BorderLayout.PAGE_START);
        base.add(scroll, BorderLayout.CENTER);

        //
        this.console = console;
    }

    public void print(String text) {
        print(text, null);
    }

    public void println(String text) {
        print(text + "\n");
    }

    public void print(String text, AttributeSet attr) {
        DefaultStyledDocument doc = getDefaultlStyledDocument();
        int length = doc.getLength();
        try {
            doc.insertString(length, text, attr);
        } catch (BadLocationException ex) {
            Logger.getLogger(CeEditor.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void println(String text, AttributeSet attr) {
        print(text, attr);
    }

    public String getText() {
        return this.console.getText();
    }

    public DefaultStyledDocument getDefaultlStyledDocument() {
        return (DefaultStyledDocument) this.console.getDocument();
    }

    public void clear() {
        setText("");
    }

    public void setText(String text) {
        this.console.setText(text);
    }
}
