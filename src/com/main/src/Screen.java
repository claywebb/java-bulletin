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
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import com.aspose.slides.ISlide;
import com.aspose.slides.Presentation;

import marvin.gui.MarvinImagePanel;
import marvin.image.MarvinImage;
import marvin.io.MarvinImageIO;

public class Screen extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final long timeDelay = 1000;    //Time spent on each photo in milliseconds
	public static final String dirPath = "";	// The directory of the images you want to load
	
	public static final String[] extensions = {"jpg", "jpeg", "gif", "png"};	//Approved Extensions
	
	private static final String EXIT = "Exit";
	private static JFrame f = new JFrame("FullScreenTest");
	private static MarvinImagePanel imagePanel;
	private static MarvinImage[] images;
	private static String[] paths = new String[]{Screen.class.getResource("../res/1RyLFUf.jpg").getPath(),
												Screen.class.getResource("../res/4PC1oo9.jpg").getPath(),
												Screen.class.getResource("../res/o8MXbZK.jpg").getPath(),
												Screen.class.getResource("../res/yluW6Wn.jpg").getPath()
										};
	private static int count = 0;
	private final static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
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


	public Screen() throws IOException {
		
		//Press "Q" or "ESC" to exit the window
		this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_Q, 0), EXIT);	
		this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), EXIT);	
		this.getActionMap().put(EXIT, exit);
		
		//update the paths with the images in the listed directory
		updateImageArray();

		
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

	private void display() throws IOException {
		//Configure the display
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
			
		//create a new image panel for all of the images
		imagePanel = new MarvinImagePanel();
		f.add(imagePanel);

		ArrayList<MarvinImage> tempImages = new ArrayList<MarvinImage>();
		
		for(String s : paths){
			//Convert the paths into images and load them into an array
			if(isImage(s))
				tempImages.add(MarvinImageIO.loadImage(s));
			
			if(isPowerPoint(s)){
				ArrayList<BufferedImage> imgs = getImagesFromPowerPoint(s);
				
				for(BufferedImage b : imgs){
					tempImages.add(new MarvinImage(b));
				}
			}
		}
		
		MarvinImage[] temp = new MarvinImage[tempImages.size()];
		temp = tempImages.toArray(temp);
		images = temp;

	
		dev.setFullScreenWindow(f);
		
		startShow();

}
	
	public static void startShow(){

		final Runnable tick = new Runnable() {
			public void run() { updateImage(); }					
		};
		
		//start a scheduled task to update the image
		@SuppressWarnings("unused")
		final ScheduledFuture<?> tickHandle = scheduler.scheduleAtFixedRate(tick, 0, timeDelay, TimeUnit.MILLISECONDS);

		System.out.println("Goodbye!");

	}
	
	public static void updateImage(){
		if(count >= images.length) count = 0;
		imagePanel.setImage(images[count]);
		count++;
	}
	
	public static void updateImageArray() throws IOException{
		ArrayList<String> files = new ArrayList<String>();
		
		//get the directory
		File dir = new File(dirPath);
		
		//Check if dir is a directory
		if (!dir.isDirectory()){ System.out.println(dirPath + " is not a directory!"); return; }
		
		//Create an array of all the files in the directory
		File[] dirList = dir.listFiles();
		
		for(File f : dirList){
			//Check if the file is a image
			if (f != null && (isImage(f.getName())||(isPowerPoint(f.getName())))){
				files.add(f.getCanonicalPath());
				System.out.println("Added " + f.getName() + " to file list!");
			}
		}
		
		//Update the paths array
		String[] temp = new String[files.size()];
		temp = files.toArray(temp);
		paths = temp;
	}
	
	public static boolean isImage(String s){
		//if the path ends with any of the approved extensions, return true
		for(String e : extensions){
			if (s.endsWith("." + e)) return true;
		}
		return false;
	}
	
	public static boolean isPowerPoint(String s){
		if ((s.endsWith(".pptx"))||(s.endsWith(".ppt"))){
			return true;
		}
		return false;
	}
	
	public static ArrayList<BufferedImage> getImagesFromPowerPoint(String s){
		Presentation pres = new Presentation(s);
		ArrayList<BufferedImage> imgs = new ArrayList<BufferedImage>();
		
		int width = 1920;
		int height = 1080;
		
		float scaledWidth = (float)(1.0 / pres.getSlideSize().getSize().getWidth())*width;
		float scaledHeight = (float)(1.0 / pres.getSlideSize().getSize().getHeight())*height;
		
		for (int i = 0; i < pres.getSlides().size(); i++){
			ISlide sld = pres.getSlides().get_Item(i);
			BufferedImage image = sld.getThumbnail(scaledWidth, scaledHeight);
			
			imgs.add(image);
		}
		
		return imgs;
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				try {
					new Screen().display();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}
}
