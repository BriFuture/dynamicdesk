package dynamic.ui;

import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.swing.JFrame;

import dynamic.util.FileUtil;
import dynamic.util.ImageUtil;

public class BaseFrame extends JFrame{
	//任务栏图标
	private TrayIcon trayIcon;
	//任务栏图标菜单
	private PopupMenu popupMenu = new PopupMenu();
	private MenuItem setItem = new MenuItem("显示设置窗口");
	private MenuItem saveItem = new MenuItem("保存当前设置");
	private MenuItem quitItem = new MenuItem("退出");
	private DynamicDisplay dd = new DynamicDisplay();
	
	public BaseFrame(){
//		initConfig();
		createTrayIcon();
		initListeners();
		setDefaultCloseOperation(HIDE_ON_CLOSE);
//		setLocation(450, 100);
//		setSize(new Dimension(500,500));
//		setUndecorated(true);
		setVisible(false);
		dd.setVisible(true);
	}
	//not need now
	private void initConfig() {
		Properties pro = new Properties();
		try {
			File f = new File(FileUtil.CONFIG);
			f.createNewFile();
			pro.load(new FileInputStream(f));
//			pro.getProperty("locateX");
//			System.out.println(pro.getProperty("locateX"));
			
			if(pro.getProperty("locateX") == null) {
				pro.setProperty("locateX", String.valueOf(0));
				pro.setProperty("locateY", String.valueOf(0));
				try {
					pro.store(new FileOutputStream(f), "");
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			if(pro.getProperty("dir") == null) {
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private BufferedImage trayIconImage = (BufferedImage) ImageUtil.getImage(System.getProperty("java.class.path") + "/../res/trayIcon.png");
	
	/**
	 * 创建任务栏图标
	 */
	private void createTrayIcon() {
		popupMenu.add(setItem);
		popupMenu.add(saveItem);
		popupMenu.add(quitItem);
		try {
			SystemTray tray = SystemTray.getSystemTray();
			this.trayIcon = new TrayIcon(trayIconImage, "魔性的动态图", this.popupMenu);
			this.trayIcon.setToolTip("退出动态图");
			tray.add(this.trayIcon);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void initListeners() {
		quitItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				System.exit(0);
			}
		});
		saveItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				dd.saveConfig();
			}
		});
		setItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(isVisible()) {
					setVisible(false);
				} else {
					setVisible(true);
				}
			}
		});
	}
}
