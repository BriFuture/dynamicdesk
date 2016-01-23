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

import dynamic.util.FileUtil;


/**
 * Ðü¸¡´°¿Ú
 */
public class DynamicDisplay extends JWindow {
	private final static int  TIME_GAP = 5;
	//the gif to display
	private ImageIcon gif;
	
//	private List<String> demopngNames = new ArrayList<String>();
	//the array of pngs
	private List<ImageIcon> demopngs = new ArrayList<ImageIcon>();
	private boolean loop = true;
	private List<Image> images = new ArrayList<Image>();
	//png mode current image
	private Image currentImg ;
	//png mode gap time
	private int gapTime;
	private Image bgImage;
	//png file folder path
	private String dirPath;
	//img path
	private String imagePath;
	public static final int DEFAULT_IMG_WIDTH = 150;
	public static final int DEFAULT_IMG_HEIGHT = 200;
	private int imgWidth;
	private int imgHeight;
	//x on screen
	private int locateX;
	//y on screen
	private int locateY;
	//x related to this window
	private int relateX;
	//y related to this window
	private int relateY;
	private boolean pngsMode;
	private Thread runThread;
	
	private DynamicDisplay() {
		pngsMode = true;
		//get the image to display
		setLocationRelativeTo(null);
		initFromConfig();
		display();
		bgImage = getBackgroundImage();
		setSize(imgWidth, imgHeight);
		setLocation(getPosition());
		runThread = new Thread(new DynamicControl());
		runThread.start();
		initListeners();
	}
	private static DynamicDisplay dd;
	public static DynamicDisplay getInstance() {
		if(dd == null) {
			dd = new DynamicDisplay();
		}
		return dd;
	}
	public void refresh() {
		//clear png
		dd.images.clear();
		dd.currentImg = null;
		dd.display();
		dd.setSize(imgWidth, imgHeight);
		dd.setLocation(getPosition());
		
	}
	private void initFromConfig() {
		try {
			File f = new File(FileUtil.CONFIG);
			f.createNewFile();
			Properties pro = new Properties();
			pro.load(new FileInputStream(f));
			locateX = Integer.parseInt(pro.getProperty("locateX","0"));
			locateY = Integer.parseInt(pro.getProperty("locateY","0"));
			gapTime = Integer.parseInt(pro.getProperty("gapTime","70"));
			imgWidth = Integer.parseInt(pro.getProperty("imgWidth",String.valueOf(DEFAULT_IMG_WIDTH)));
			imgHeight = Integer.parseInt(pro.getProperty("imgHeight",String.valueOf(DEFAULT_IMG_HEIGHT)));
			setAlwaysOnTop(Boolean.valueOf(pro.getProperty("isAlwaysOnTop",String.valueOf(false))));
			if(pngsMode) {
				dirPath = pro.getProperty("png_dir_path", FileUtil.DEMO_DIR);
			} else {
				imagePath = pro.getProperty("image_path",FileUtil.DEMO_GIF_PATH);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * ±£´æÅäÖÃÎÄ¼þ
	 */
	public void saveConfig() {
		try {
			File f = new File(FileUtil.CONFIG);
			//attemp to create new file if no such file
			f.createNewFile();
			Properties pro = new Properties();
			pro.load(new FileInputStream(f));
			pro.setProperty("locateX", String.valueOf(locateX));
			pro.setProperty("locateY", String.valueOf(locateY));
			pro.setProperty("gapTime", String.valueOf(gapTime));
			pro.setProperty("imgWidth", String.valueOf(imgWidth));
			pro.setProperty("imgHeight", String.valueOf(imgHeight));
			pro.setProperty("isAlwaysOnTop", String.valueOf(isAlwaysOnTop()));
			if(pngsMode) {
				pro.setProperty("png_dir_path", dirPath);
			} else {
				pro.setProperty("image_path", imagePath);
			}
			pro.store(new FileOutputStream(f), "");
		} catch (IOException e) {
			e.printStackTrace();
		}
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
        		new Rectangle(locateX, locateY, ((locateX < 0) ? 0 : locateX) + imgWidth,((locateY < 0) ? 0 : locateY)+ imgHeight));
		return image;
	}
	
	private void display() {
		if(pngsMode) {
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
						//to change the size
						BufferedImage bufimg = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_ARGB);
						bufimg.getGraphics().drawImage(i, 0, 0, imgWidth, imgHeight, null);
						images.add(bufimg);
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
			g.drawImage(bgImage, 0, 0, null);
			//init currentImg
			if(currentImg == null)
				currentImg = images.get(0);
			g.drawImage(currentImg, 0, 0, null);
//			System.out.println("X: "+ locateX + " Y: "+ locateY);
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
			}
			//Ë«»÷±£´æÎ»ÖÃ
			public void mouseClicked(MouseEvent me) {
				if(me.getClickCount() == 2) {
					saveConfig();
				}
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
					if(i >= images.size()) {
						i = 0;
					}
					currentImg = images.get(i);
					i++;
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
	/**
	 * @return the gapTime
	 */
	public int getGapTime() {
		return gapTime;
	}
	/**
	 * @param gapTime the gapTime to set
	 */
	public void setGapTime(int gapTime) {
		this.gapTime = gapTime;
	}
}
