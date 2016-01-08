package dynamic.util;

import java.awt.Image;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class ImageUtil {
	
	public static final Image getImage(String imagePath) {
		try {
			Image img = ImageIO.read(new File(imagePath));
			return img;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}
	/*public static final ImageIcon getImageIcon(String imagePath) {
		try {
			ImageIcon img = new ImageIcon(imagePath);
			return img;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}*/
}
