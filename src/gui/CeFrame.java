package gui;

import compiler.CeParseUtils;
import compiler.CeSymTable;
import gui.CeEditor.CeTextEditor;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;

import gui.CeProjectManager.CeNode;
import gui.CeProjectManager.CeNodeClickedEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;

public class CeFrame extends JFrame {

	public CeFrame() {
		this.setJMenuBar(createMenuBar());
		this.setContentPane(createContentPane());
	}

	private JPanel createContentPane() {

		JPanel base = new JPanel(new BorderLayout());
		JPanel base_body = new JPanel();
		JSplitPane base_splitpane = new JSplitPane();
		JSplitPane base_splitpane1 = new JSplitPane();

		JToolBar toolbar = new JToolBar("CeFrame toolbar");
		JButton toolbar_compile = new JButton("Compile");
		JButton toolbar_graph = new JButton("Graph");


		JPanel inspector_tree_panel = new JPanel();
		final CeProjectManager inspector_tree = new CeProjectManager();
		JPanel inspector_panel = new JPanel();
		JTabbedPane inspector_tabbpane = new JTabbedPane();
		JScrollPane inspector_scroll = new JScrollPane();

		JPanel canvas_panel = new JPanel();

		final CeEditor editor = new CeEditor();
		JPanel editor_panel = new JPanel();

		///setConfigs
		toolbar_compile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				CeTextEditor ceditor = editor.getCurrentTabEditor();
				if (ceditor != null) {
					File file = ceditor.getFile();

					DefaultMutableTreeNode project_node = inspector_tree.getProjectNodeOf(file);
					if (project_node == null) {
						System.out.println("no se encontró proyecto");
					} else {
//                        System.out.println(project_node.toString());
						CeParseUtils utils = new CeParseUtils();
						utils.setConsole(editor.getConsole());
						utils.setFile(file);
						utils.put("symtable", new CeSymTable());
						utils.put("model", inspector_tree.getTree().getModel());
						utils.setDocument(editor.getCurrentTabEditor().getDefaultStyledDocument());

						try {
							ceditor.saveText();
							utils.parse();
						} catch (Exception ex) {
							Logger.getLogger(CeFrame.class.getName()).log(Level.SEVERE, null, ex);
						}
					}



				}
			}
		});
		toolbar.add(toolbar_compile);
		toolbar.add(toolbar_graph);

		inspector_tree.addCeNodeAddedListener(new CeProjectManager.CeNodeAddedListener() {
			@Override
			public void CeNodeAdded(DefaultMutableTreeNode node) {
//                CeParseUtils    utils   =   new CeParseUtils();
//                utils.setConsole(editor.getConsole());
//                utils.setTreeNode(node);
//                utils.setDocument(editor.getCurrentTabEditor().getDefaultStyledDocument());
//                try {
//                    
//                    utils.parse();
//                } catch (Exception ex) {
//                    Logger.getLogger(CeFrame.class.getName()).log(Level.SEVERE, null, ex);
//                }
			}
		});
		inspector_tree.addCeNodeClick(new CeProjectManager.CeNodeClickListener() {
			@Override
			public void CeNodeclicked(CeNodeClickedEvent cevent) {
				if (cevent.getMe().getClickCount() == 2) {
					DefaultMutableTreeNode node = cevent.getNode();
					if (node.isLeaf()) {
						CeNode ce_obj = (CeProjectManager.CeNode) node.getUserObject();
						File file = ce_obj.getFile();
						editor.openFile(file);
					}
				}

			}
		});
		inspector_tree_panel.setLayout(new BorderLayout());
		inspector_tree_panel.add(inspector_tree);
		inspector_panel.setBorder(BorderFactory.createTitledBorder("Inspector"));
		inspector_panel.setLayout(new BorderLayout());
		inspector_panel.add(inspector_tabbpane);
		inspector_tabbpane.addTab("Proyecto", inspector_tree_panel);
		inspector_tabbpane.addTab("Variables", new JPanel());
		inspector_tabbpane.addTab("Funciones", new JPanel());
		inspector_scroll.setViewportView(inspector_panel);

		editor_panel.setBorder(BorderFactory.createTitledBorder("Editor"));
		editor_panel.setLayout(new BorderLayout());
		editor_panel.add(editor);

		base_splitpane.setLeftComponent(base_splitpane1);
		base_splitpane.setRightComponent(canvas_panel);
		base_splitpane.setDividerLocation(1.0);
		base_splitpane1.setLeftComponent(inspector_scroll);
		base_splitpane1.setRightComponent(editor_panel);
		base_splitpane1.setDividerLocation(0.2);



		canvas_panel.setBorder(BorderFactory.createTitledBorder("Lienzo"));


		base_body.setLayout(new BorderLayout());
		base_body.setPreferredSize(new Dimension(800, 600));
		base_body.add(base_splitpane);

		base.add(toolbar, BorderLayout.PAGE_START);
		base.add(base_body, BorderLayout.CENTER);

		return base;
	}

	private JMenuBar createMenuBar() {
		JMenuBar menubar	= new JMenuBar();
		JMenu menu_file = new JMenu();
		JMenu menu_compile = new JMenu();
		final JMenu menu_look = new JMenu("Look and Feel");
		JMenu menu_help = new JMenu();
		JMenu menu_file_new = new JMenu();
		JMenuItem menu_file_new_project = new JMenuItem();
		JMenuItem menu_file_new_logc = new JMenuItem();
		JMenuItem menu_file_new_logp = new JMenuItem();
		JMenuItem menu_file_new_logl = new JMenuItem();
		JMenuItem menu_file_open = new JMenuItem();
		JMenuItem menu_file_save = new JMenuItem();
		JMenuItem menu_file_saveAs = new JMenuItem();
		JMenuItem menu_file_exit = new JMenuItem();
		JMenuItem menu_compile_1 = new JMenuItem();//compilar y ejecutar
		JMenuItem menu_compile_2 = new JMenuItem();//mostrar grafo
		JMenuItem menu_compile_3 = new JMenuItem();//tabla de simbolos
		JMenuItem menu_compile_4 = new JMenuItem();//tabla de errores
		JMenuItem menu_compile_5 = new JMenuItem();
		JMenuItem menu_compile_6 = new JMenuItem();
		JMenuItem menu_help_about = new JMenuItem();

		menu_file_new_project.setText("Project");
		menu_file_new_project.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				menu_file_new_project_ActionPerformed(arg0);
			}
		});
		menu_file_new_logc.setText("LOGC File");
		menu_file_new_logl.setText("LOGL File");
		menu_file_new_logp.setText("LOGP File");
		menu_file_new.setText("New");
		menu_file_new.add(menu_file_new_project);
		menu_file_new.add(menu_file_new_logc);
		menu_file_new.add(menu_file_new_logp);
		menu_file_new.add(menu_file_new_logl);

//		menu_file_new.setMnemonic('n');
//		menu_file_new.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,InputEvent.CTRL_MASK));
//		menu_file_new.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent arg0) {
//				//				JOptionPane.showMessageDialog(null, arg0.getActionCommand());
//				menu_file_new_ActionPerformed(arg0);
//			}
//		});

		menu_file_open.setText("Open");
		menu_file_open.setMnemonic('o');
		menu_file_open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
		menu_file_open.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				//				JOptionPane.showMessageDialog(null, arg0.getActionCommand());
				menu_file_open_ActionPerformed(arg0);
			}
		});

		menu_file_save.setText("Save");
		menu_file_save.setMnemonic('s');
		menu_file_save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		menu_file_save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				//				JOptionPane.showMessageDialog(null, arg0.getActionCommand());
				menu_file_save_ActionPerformed(arg0);
			}
		});

		menu_file_saveAs.setText("Save As");
		menu_file_saveAs.setMnemonic('A');
		//		menu_file_saveAs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,InputEvent.CTRL_MASK));
		menu_file_saveAs.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				//				JOptionPane.showMessageDialog(null, arg0.getActionCommand());
				menu_file_saveAs_ActionPerformed(arg0);
			}
		});

		menu_file_exit.setText("Quit");
		menu_file_exit.setMnemonic('q');
		menu_file_exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK));
		menu_file_exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				//				JOptionPane.showMessageDialog(null, arg0.getActionCommand());
				menu_file_exit_ActionPerformed(arg0);
			}
		});

		menu_compile_1.setText("Compile and Execute");
		menu_compile_1.setMnemonic('1');
		menu_compile_1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, InputEvent.CTRL_MASK));
		menu_compile_1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				compile();
			}
		});

		menu_compile_2.setText("Graph");
		menu_compile_2.setMnemonic('2');
		menu_compile_2.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, InputEvent.CTRL_MASK));
		menu_compile_2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
			}
		});

		menu_compile_3.setText("Symbol Table");
		menu_compile_3.setMnemonic('3');
		menu_compile_3.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_3, InputEvent.CTRL_MASK));
		menu_compile_3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
			}
		});

		menu_compile_4.setText("Error Table");
		menu_compile_4.setMnemonic('4');
		menu_compile_4.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_4, InputEvent.CTRL_MASK));
		menu_compile_4.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
			}
		});

		menu_compile_5.setText("gg");
		menu_compile_5.setMnemonic('5');
		menu_compile_5.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_5, InputEvent.CTRL_MASK));
		menu_compile_5.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
			}
		});

		menu_compile_6.setText("gg");
		menu_compile_6.setMnemonic('6');
		menu_compile_6.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_6, InputEvent.CTRL_MASK));
		menu_compile_6.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
			}
		});

		menu_help_about.setText("About");
		menu_help_about.setMnemonic('a');
		//		menu_help_about.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,InputEvent.CTRL_MASK));
		menu_help_about.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(null, arg0.getActionCommand());
			}
		});

		
		UIManager.LookAndFeelInfo[] installedLookAndFeels = javax.swing.UIManager.getInstalledLookAndFeels();
        
        for (final UIManager.LookAndFeelInfo l : installedLookAndFeels) {
            JMenuItem item=new JMenuItem(l.getName());
            menu_look.add(item);
            item.addActionListener(new ActionListener() {
                
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        javax.swing.UIManager.setLookAndFeel(l.getClassName());
                        SwingUtilities.updateComponentTreeUI(menu_look.getRootPane());
                    } catch (ClassNotFoundException ex) {
                        addError(ex.getMessage());
                    } catch (InstantiationException ex) {
                        addError(ex.getMessage());
                    } catch (IllegalAccessException ex) {
                        addError(ex.getMessage());
                    } catch (UnsupportedLookAndFeelException ex) {
                        addError(ex.getMessage());
                    }
                }
                
                private void addError(String message) {
                    System.err.println(message);
                }
            });
        }
		
		
		
		menu_file.setText("File");
		menu_file.setMnemonic('f');
		menu_file.add(menu_file_new);
		menu_file.add(menu_file_open);
		menu_file.add(menu_file_save);
		menu_file.add(menu_file_saveAs);
		menu_file.add(menu_file_exit);

		menu_compile.setText("Compile");
		menu_compile.setMnemonic('c');
		menu_compile.add(menu_compile_1);
		menu_compile.add(menu_compile_2);
		menu_compile.add(menu_compile_3);
		menu_compile.add(menu_compile_4);
		menu_compile.add(menu_compile_5);
		menu_compile.add(menu_compile_6);

		menu_help.setText("Help");
		menu_help.setMnemonic('h');
		menu_help.add(menu_help_about);


		menubar.add(menu_file);
		menubar.add(menu_compile);
		menubar.add(menu_look);
		menubar.add(menu_help);

		return menubar;
	}

	/**
	 * *********************************************
	 * fsdfklsd *********************************************
	 */
	private void doc_new(CeTabbedPane tab_pane, File doc_file) {
	}

	private void doc_open() {
	}

	private void doc_save(CeEditor doc_editor) {
	}

	private void doc_saveAs(CeEditor doc_editor) {
	}

	protected void compile() {
	}

	/**
	 * *********************************************
	 * Actions *********************************************
	 */
	protected void menu_file_new_project_ActionPerformed(ActionEvent arg0) {
		JFileChooser filechooser = new JFileChooser();
		filechooser.setDialogType(JFileChooser.DIRECTORIES_ONLY);
		filechooser.showSaveDialog(this);

	}

	protected void menu_file_new_ActionPerformed(ActionEvent arg0) {
	}

	protected void menu_file_open_ActionPerformed(ActionEvent arg0) {
		doc_open();
	}

	protected void menu_file_save_ActionPerformed(ActionEvent arg0) {
	}

	protected void menu_file_saveAs_ActionPerformed(ActionEvent arg0) {
	}

	protected void menu_file_exit_ActionPerformed(ActionEvent arg0) {
		System.exit(0);
	}

	/**
	 * *********************************************
	 * Messages *********************************************
	 */
	public void msg_error(String title, String msg) {
		if (title == null || title.isEmpty()) {
			title = "Error";
		}
		JOptionPane.showMessageDialog(this, msg, title, JOptionPane.ERROR_MESSAGE);
	}
}
