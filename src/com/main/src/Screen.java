package com.main.src;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

public class Screen extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String EXIT = "Exit";
	private JFrame f = new JFrame("FullScreenTest");
	private Action exit = new AbstractAction(EXIT) {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			f.dispatchEvent(new WindowEvent(f, WindowEvent.WINDOW_CLOSING));
		}
	};

//	private JButton b = new JButton(exit);

	public Screen() {
		
//		Uncomment to add a exit button	
//		this.add(b);
//		f.getRootPane().setDefaultButton(b);
		
		//Press "Q" or "ESC" to exit the window
		this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_Q, 0), EXIT);	
		this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), EXIT);	
		this.getActionMap().put(EXIT, exit);
		
//		Uncomment to get a tool tip about mouse position
		/*
		this.addMouseMotionListener(new MouseAdapter() {

			@Override
			public void mouseMoved(MouseEvent e) {
				Screen.this.setToolTipText("(" + e.getX() + "," + e.getY()
						+ ")");
			}
		});
		*/
	}

	private void display() {
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice dev = env.getDefaultScreenDevice();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setBackground(Color.darkGray);
		f.setResizable(false);
		f.setUndecorated(true);
		
		//Hide Cursor
		BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0,0) , "blank");
		f.getContentPane().setCursor(blankCursor);
		
		f.add(this);
		f.pack();
		dev.setFullScreenWindow(f);
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				new Screen().display();
			}
		});
	}
}
