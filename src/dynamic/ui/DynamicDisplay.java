package dynamic.ui;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JWindow;


/**
 * Ðü¸¡´°¿Ú
 * @author yangenxiong yangenxiong2009@gmail.com
 * @version  1.0
 * <br/>ÍøÕ¾: <a href="http://www.crazyit.org">·è¿ñJavaÁªÃË</a>
 * <br>Copyright (C), 2009-2010, yangenxiong
 * <br>This program is protected by copyright laws.
 */
public class DynamicDisplay extends JWindow {
	private ImageIcon gif;
	private List<String> demopngNames = new ArrayList<String>();
	private List<ImageIcon> demopngs = new ArrayList<ImageIcon>();
	private boolean loop = true;
	private List<Image> images = new ArrayList<Image>();
	//png mode current image
	private Image currentImg ;
	//png mode gap time
	private int gapTime = 100;
	private Image bgImage;
	private String dirPath = System.getProperty("java.class.path") + "/../res/demos.png/";
	private String imagePath = System.getProperty("java.class.path") + "/../res/demo.gif";
	private String config = System.getProperty("java.class.path") + "/../res/config.properties";
	public static final int DEFAULT_IMG_WIDTH = 150;
	public static final int DEFAULT_IMG_HEIGHT = 200;
	//x on screen
	private int locateX;
	//y on screen
	private int locateY;
	//x related to this window
	private int relateX;
	//y related to this window
	private int relateY;
	private int imgWidth = DEFAULT_IMG_WIDTH;
	private int imgHeight = DEFAULT_IMG_HEIGHT;
	private boolean pngsMode = false;
	private Thread runThread;
	
	public DynamicDisplay() {
//		System.out.println("suspend window"+getLocateX());
		pngsMode = true;
		try {
			File f = new File(config);
			Properties pro = new Properties();
			pro.load(new FileInputStream(f));
			locateX = Integer.parseInt(pro.getProperty("locateX"));
			locateY = Integer.parseInt(pro.getProperty("locateY"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			locateX = 0;
			locateY = 0;
		}
//		pro.
		//get the image to display
		display();
//		System.out.println(imgHeight);
		bgImage = getBackgroundImage();
		
		setLocationRelativeTo(null);
		setSize(imgWidth, imgHeight);
		setLocation(getPosition());
//		this.setAlwaysOnTop(true);
		initListeners();
		runThread = new Thread(new DynamicControl());
		runThread.start();
	}

	/**
	 * to do
	 */
	public void savePosition() {
		//locateX; locateY;
	}
	private Point getPosition() {
		Point pos = new Point();
		pos.x = locateX;
		pos.y = locateY;
		return pos;
	}
	/**
	 * ·µ»Ø±³¾°Í¼Æ¬£¬·ÀÖ¹Í¼Æ¬ÖØµþ
	 * @return
	 */
	private Image getBackgroundImage() {
		Robot robot = null;
        try {
            robot = new Robot();
        } catch (Exception e) {
        	e.printStackTrace();
        }
        BufferedImage image = robot.createScreenCapture(
        		new Rectangle(locateX, locateY, locateX + imgWidth, locateY+ imgHeight));
//        JOptionPane.showConfirmDialog(null, args, null, JOptionPane.YES_NO_OPTION, 
//        JOptionPane.ERROR_MESSAGE, new ImageIcon(image));
		return image;
	}
	private void display() {
		if(pngsMode) {
//			System.out.println("main:"+this.getClass().getResource("/dynamic/Main.class").getPath());
//			System.out.println("baseFrame:"+this.getClass().getResource("/").getPath());
//			URL fileUrl = this.getClass().getClassLoader().getResource("/res/demos.png/demo_p01.png");
//			System.out.println("flag:"+fileUrl);
//			String filepath = getClass().getClassLoader().getResource("/res/demos.png/").getPath();
//			String filepath = System.getProperty("java.class.path") + "/../res/demos.png/";
//			System.out.println(filepath);
			//get pngs file dir
			File dir = new File(dirPath);
			if(dir.isDirectory()) {
				for(File f : dir.listFiles()) {
//					System.out.println(f.getPath()+"=====>"+filepath+f.getName());
//					demopngNames.add(f.getName());
//					File f1 = new File(f.getAbsolutePath());
//					demopngs.add(new ImageIcon(f.getPath()));
					try {
						Image i = ImageIO.read(f);
//						BufferedImage bi = (BufferedImage) i;
//						BufferedImage bufimg = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_BGR);
						
						images.add(i);
						imgWidth = i.getWidth(null);
						imgHeight = i.getHeight(null);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} else {
				throw new RuntimeException("the path is not directory");
			}
		} else {
			gif = new ImageIcon(imagePath);
			imgWidth = gif.getIconWidth();
			imgHeight = gif.getIconHeight();
		}
	}

	@Override
	public void paint(Graphics g) {
		if(pngsMode) {
//			super.paint(g);
			g.drawImage(bgImage, 0, 0, null);
//			g.clearRect(0, 0, imgWidth, imgHeight);
//			g.setColor(getBackground());
			//init currentImg
			if(currentImg == null)
				currentImg = images.get(0);
			g.drawImage(currentImg, 0, 0, null);
//				System.out.println("X: "+ locateX + " Y: "+ locateY);
		} else {
			g.drawImage(gif.getImage(), locateX, locateY, this);
		}
	}
	
	private void initListeners() {
		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent me) {
				int screenX = me.getXOnScreen();
				int screenY = me.getYOnScreen();
//				System.out.println("X: "+ screenX + " Y: "+ screenY);
				setLocateX(screenX - relateX);
				setLocateY(screenY - relateY);
				setLocation(locateX, locateY);
//				System.out.println("=====dragme.btn"+me.getButton());
//				System.out.println("dragme.btn"+me.getButton());
			}
		});
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent me) {
				relateX = me.getX();
				relateY = me.getY();
				loop = false;
//				System.out.println("me.btn"+me.getButton());
//				System.out.println("X: "+ relateX+ " Y: "+ relateY);
			}
			@Override
			public void mouseReleased(MouseEvent me) {
				setVisible(false);
				bgImage = getBackgroundImage();
				setVisible(true);
				loop = true;
				runThread = new Thread(new DynamicControl());
				runThread.start();
				try {
					
					File f = new File(config);
					Properties pro = new Properties();
					pro.load(new FileInputStream(f));
					pro.setProperty("locateX", String.valueOf(locateX));
					pro.setProperty("locateY", String.valueOf(locateY));
					pro.store(new FileOutputStream(f), "");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			public void mouseClicked(MouseEvent me) {
				
			}
		});
	}
	class DynamicControl implements Runnable {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			int i=0;
			while(loop) {
				repaint();
				try {
					currentImg = images.get(i++);
					if(i==images.size()) {
						i = 0;
					}
					Thread.sleep(gapTime);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
	//			loop = false;
			}
		}
	}

	/**
	 * @return the gif
	 */
	public ImageIcon getGif() {
		return gif;
	}

	/**
	 * @param gif the gif to set
	 */
	public void setGif(ImageIcon gif) {
		this.gif = gif;
	}

	/**
	 * @return the images
	 */
	public List<Image> getImages() {
		return images;
	}

	/**
	 * @param images the images to set
	 */
	public void setImages(List<Image> images) {
		this.images = images;
	}


	/**
	 * @return the demopngNames
	 */
	public List<String> getDemopngNames() {
		return demopngNames;
	}

	/**
	 * @param demopngNames the demopngNames to set
	 */
	public void setDemopngNames(List<String> demopngNames) {
		this.demopngNames = demopngNames;
	}

	/**
	 * @return the demopngs
	 */
	public List<ImageIcon> getDemopngs() {
		return demopngs;
	}

	/**
	 * @param demopngs the demopngs to set
	 */
	public void setDemopngs(List<ImageIcon> demopngs) {
		this.demopngs = demopngs;
	}

	/**
	 * @return the pngsMode
	 */
	public boolean isPngsMode() {
		return pngsMode;
	}

	/**
	 * @param pngsMode the pngsMode to set
	 */
	public void setPngsMode(boolean pngsMode) {
		this.pngsMode = pngsMode;
	}

	/**
	 * @return the imagePath
	 */
	public String getImagePath() {
		return imagePath;
	}

	/**
	 * @param imagePath the imagePath to set
	 */
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	/**
	 * @return the locateX
	 */
	public int getLocateX() {
		return locateX;
	}

	/**
	 * @param locateX the locateX to set
	 */
	public void setLocateX(int locateX) {
		this.locateX = locateX;
	}

	/**
	 * @return the locateY
	 */
	public int getLocateY() {
		return locateY;
	}

	/**
	 * @param locateY the locateY to set
	 */
	public void setLocateY(int locateY) {
		this.locateY = locateY;
	}

	/**
	 * @return the imgWidth
	 */
	public int getImgWidth() {
		return imgWidth;
	}

	/**
	 * @param imgWidth the imgWidth to set
	 */
	public void setImgWidth(int imgWidth) {
		this.imgWidth = imgWidth;
	}

	/**
	 * @return the imgHeight
	 */
	public int getImgHeight() {
		return imgHeight;
	}

	/**
	 * @param imgHeight the imgHeight to set
	 */
	public void setImgHeight(int imgHeight) {
		this.imgHeight = imgHeight;
	}

	/**
	 * @return the dirPath
	 */
	public String getDirPath() {
		return dirPath;
	}

	/**
	 * @param dirPath the dirPath to set
	 */
	public void setDirPath(String dirPath) {
		this.dirPath = dirPath;
	}
}
