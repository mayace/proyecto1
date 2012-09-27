import java.awt.Dimension;
import java.awt.Rectangle;

import gui.CeFrame;

import javax.swing.JFrame;


public class Main {

	public static void main(String[] args) {
		CeFrame	win	=	new CeFrame();
		win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		win.setTitle("Compiladores 2 - Práctica 1");
//		win.setMinimumSize(new Dimension(640,480));
//		win.pack();
		win.setSize(new Dimension(800,600));
		win.setLocationRelativeTo(null);
		win.setVisible(true);
	}

}
