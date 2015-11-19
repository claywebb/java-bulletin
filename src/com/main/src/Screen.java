// java-bulletin written for Walsh Jesuit High School
// Copyright (c) 2015 Clayton Webb

package com.main.src;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import marvin.gui.MarvinImagePanel;
import marvin.image.MarvinImage;

import org.apache.poi.hslf.usermodel.HSLFSlide;
import org.apache.poi.hslf.usermodel.HSLFSlideShow;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;

public class Screen extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private static final String VERSION = "1.0.2";

	public static long timeDelay = 5000; // Time spent on each photo in milliseconds
	public static String dirPath = "public/images"; // The directory of the images you want to load

	public static final String[] extensions = { "jpg", "jpeg", "gif", "png" }; // Approved Extensions
	
	
	public static int WIDTH = 1920;
	public static int HEIGHT = 1080;

	private static final String EXIT = "Exit";
	private static JFrame f = new JFrame("Walsh Bulletin");
	private static MarvinImagePanel imagePanel;
	private static MarvinImage[] images;
	private static String[] paths;
			//new String[] {
//			Screen.class.getResource("../res/1RyLFUf.jpg").getPath(),
//			Screen.class.getResource("../res/4PC1oo9.jpg").getPath(),
//			Screen.class.getResource("../res/o8MXbZK.jpg").getPath(),
//			Screen.class.getResource("../res/yluW6Wn.jpg").getPath() };
	private static int count = 0;
	private final static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	
	private Action exit = new AbstractAction(EXIT) {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			f.dispatchEvent(new WindowEvent(f, WindowEvent.WINDOW_CLOSING));
		}
	};

	public Screen() throws IOException {

		// Press "Q" or "ESC" to exit the window
		this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_Q, 0), EXIT);
		this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), EXIT);
		this.getActionMap().put(EXIT, exit);

		// update the paths with the images in the listed directory
		updateImageArray();

	}

	private void display() throws IOException {
		// Configure the display
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice dev = env.getDefaultScreenDevice();

		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setBackground(Color.BLACK);
		f.setResizable(false);
		f.setUndecorated(true);

		// Hide Cursor
		BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "blank");
		f.getContentPane().setCursor(blankCursor);

		f.add(this);
		f.pack();
		
		WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;
		HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;
		
		// create a new image panel for all of the images
		imagePanel = new MarvinImagePanel();
		f.add(imagePanel);

		ArrayList<MarvinImage> tempImages = new ArrayList<MarvinImage>();

		for (String s : paths) {
			// Convert the paths into images and load them into an array
			if (isImage(s)) {

				File file = new File(s);
				BufferedImage in = ImageIO.read(file);
				BufferedImage buffImg = new BufferedImage(in.getWidth(), in.getHeight(), BufferedImage.TYPE_INT_ARGB);

				Graphics2D g = buffImg.createGraphics();
				g.drawImage(in, 0, 0, null);
				g.dispose();

				tempImages.add(new MarvinImage(scaleImage(buffImg)));
			}
			if (isPowerPoint(s)) {
				ArrayList<BufferedImage> imgs = getImagesFromPowerPoint(s);

				for (BufferedImage b : imgs) {
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

	public static void startShow() {

		final Runnable tick = new Runnable() {
			public void run() {
				updateImage();
			}
		};

		// start a scheduled task to update the image
		updateImage();
		@SuppressWarnings("unused")
		final ScheduledFuture<?> tickHandle = scheduler.scheduleAtFixedRate(tick, timeDelay, timeDelay, TimeUnit.MILLISECONDS);

		System.out.println("Goodbye!");

	}

	public static void updateImage() {
		if (count >= images.length)
			count = 0;
		imagePanel.setImage(images[count]);
		count++;
	}

	public static void updateImageArray() throws IOException {
		ArrayList<String> files = new ArrayList<String>();

		// get the directory
		File dir = new File(dirPath);

		// Check if dir is a directory
		if (!dir.isDirectory()) {
			System.out.println(dirPath + " is not a directory!");
			return;
		}

		// Create an array of all the files in the directory
		File[] dirList = dir.listFiles();

		for (File f : dirList) {
			// Check if the file is a image
			if (f != null && (isImage(f.getName()) || (isPowerPoint(f.getName())))) {
				files.add(f.getCanonicalPath());
				System.out.println("Added " + f.getName() + " to file list!");
			}
		}

		// Update the paths array
		String[] temp = new String[files.size()];
		temp = files.toArray(temp);
		paths = temp;
	}

	public static boolean isImage(String s) {
		// if the path ends with any of the approved extensions, return true
		for (String e : extensions) {
			if (s.endsWith("." + e))
				return true;
		}
		return false;
	}

	public static boolean isPowerPoint(String s) {
		if ((s.endsWith(".pptx")) || (s.endsWith(".ppt"))) {
			return true;
		}
		return false;
	}

	public static ArrayList<BufferedImage> getImagesFromPowerPoint(String s)
			throws IOException {
		if (s.endsWith(".pptx")) {
			FileInputStream is = new FileInputStream(s);
			@SuppressWarnings("resource")
			XMLSlideShow ppt = new XMLSlideShow(is);
			is.close();

			ArrayList<BufferedImage> imgs = new ArrayList<BufferedImage>();

			Dimension pgsize = ppt.getPageSize();

			List<XSLFSlide> slide = ppt.getSlides();
			for (int i = 0; i < slide.size(); i++) {

				BufferedImage img = new BufferedImage(pgsize.width, pgsize.height, BufferedImage.TYPE_INT_ARGB);

				Graphics2D graphics = img.createGraphics();
				graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
				graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
				graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);

				graphics.setColor(Color.white);
				graphics.clearRect(0, 0, pgsize.width, pgsize.height);
				graphics.fill(new Rectangle2D.Float(0, 0, pgsize.width, pgsize.height));

				// render
				slide.get(i).draw(graphics);

				// scale save the output
				imgs.add(scaleImage(img));
			}
			return imgs;
		} else if (s.endsWith(".ppt")) {
			FileInputStream is = new FileInputStream(s);
			HSLFSlideShow ppt = new HSLFSlideShow(is);
			is.close();

			ArrayList<BufferedImage> imgs = new ArrayList<BufferedImage>();

			Dimension pgsize = ppt.getPageSize();

			List<HSLFSlide> slide = ppt.getSlides();
			for (int i = 0; i < slide.size(); i++) {

				BufferedImage img = new BufferedImage(pgsize.width, pgsize.height, BufferedImage.TYPE_INT_ARGB);

				Graphics2D graphics = img.createGraphics();
				graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
				graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
				graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);

				graphics.setColor(Color.white);
				graphics.clearRect(0, 0, pgsize.width, pgsize.height);
				graphics.fill(new Rectangle2D.Float(0, 0, pgsize.width, pgsize.height));

				// render
				slide.get(i).draw(graphics);

				// scale and save the output
				imgs.add(scaleImage(img));
			}
			return imgs;
		} else
			return new ArrayList<BufferedImage>();
	}

	public static BufferedImage scaleImage(BufferedImage img) {
		double scaleX = (double) (WIDTH / (double) img.getWidth());
		double scaleY = (double) (HEIGHT / (double) img.getHeight());

		BufferedImage a = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
		AffineTransform at = new AffineTransform();
		at.scale(scaleX, scaleY);
		AffineTransformOp scOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
		a = scOp.filter(img, a);
		return a;
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
	
			@Override
			public void run() {
				try {
					
					if(args.length > 0){
						int time = Integer.parseInt(args[0]);
						if (time > 0) timeDelay = time;
						if(args.length > 1){
							dirPath = args[1];
						}
					}
					
					new Screen().display();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public static String getVersion(){
		return VERSION;
	}
}
