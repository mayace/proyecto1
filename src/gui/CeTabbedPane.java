package gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class CeTabbedPane extends JTabbedPane {

    class CeTabComponent extends JPanel {

        JLabel title;
        JButton closeButton;

        public CeTabComponent(String title, ActionListener action) {
            // TODO Auto-generated constructor stub
            this.title = new JLabel(title);
            this.closeButton = new JButton("x");

            this.closeButton.setContentAreaFilled(false);
            this.closeButton.addActionListener(action);

            this.setOpaque(false);
            this.add(this.title);
            this.add(this.closeButton);

        }
    }

    @Override
    public void addTab(String title, Component component) {
        // TODO Auto-generated method stub
        super.addTab(title, component);

        CeTabComponent tab_top = new CeTabComponent(title, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                // TODO Auto-generated method stub
                JPanel tab_top_pane = (JPanel) ((JButton) arg0.getSource()).getParent();
                CeTabbedPane tab_pane = (CeTabbedPane) tab_top_pane.getParent().getParent();
                tab_pane.remove(tab_pane.indexOfTabComponent(tab_top_pane));
            }
        });

        int tab_index = this.getTabCount() - 1;
        this.setTabComponentAt(tab_index, tab_top);
        this.setSelectedIndex(tab_index);
    }

    /* (non-Javadoc)
     * @see javax.swing.JTabbedPane#setTitleAt(int, java.lang.String)
     */
    @Override
    public void setTitleAt(int arg0, String arg1) {
        // TODO Auto-generated method stub
        super.setTitleAt(arg0, arg1);
        ((CeTabComponent) this.getTabComponentAt(arg0)).title.setText(arg1);
    }
}
