package dynamic.ui;

import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

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
	
	private DynamicDisplay dd = DynamicDisplay.getInstance();
	
	//UI
	private JLabel widthLabel = new JLabel("图片宽度:");
	private JLabel heightLabel = new JLabel("图片高度:");
	private JLabel gapTimeLabel = new JLabel("播放间隔:");
	private JTextField widthField = new JTextField(5);
	private JTextField heightField = new JTextField(5);
	private JTextField gapTimeField = new JTextField(5);
	private JCheckBox topCheckBox = new JCheckBox("置顶");
	private JButton confirmBtn = new JButton("确认");
	public BaseFrame(){
		init();
//		initConfig();
		createTrayIcon();
		initListeners();
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		setVisible(false);
		dd.setVisible(true);
		
		
	}
	private void init() {
		JPanel panel = new JPanel();
		
		Box box1 = Box.createHorizontalBox();
		box1.add(Box.createHorizontalStrut(30));
		box1.add(widthLabel);
		box1.add(Box.createHorizontalStrut(30));
		box1.add(widthField);
		widthField.setText(String.valueOf(dd.getImgWidth()));
		box1.add(Box.createHorizontalStrut(30));
		
		Box box2 = Box.createHorizontalBox();
		box2.add(Box.createHorizontalStrut(30));
		box2.add(heightLabel);
		box2.add(Box.createHorizontalStrut(30));
		box2.add(heightField);
		heightField.setText(String.valueOf(dd.getImgHeight()));
		box2.add(Box.createHorizontalStrut(30));
		
		Box box3 = Box.createHorizontalBox();
		box3.add(Box.createHorizontalStrut(30));
		box3.add(gapTimeLabel);
		box3.add(Box.createHorizontalStrut(30));
		box3.add(gapTimeField);
		gapTimeField.setText(String.valueOf(dd.getGapTime()));
		box3.add(Box.createHorizontalStrut(30));
		
		Box box4 = Box.createHorizontalBox();
		box4.add(topCheckBox);
		topCheckBox.setSelected(dd.isAlwaysOnTop());
		Box box5 = Box.createHorizontalBox();
		box5.add(confirmBtn);
		
		Box vBox = Box.createVerticalBox();
		vBox.add(Box.createVerticalStrut(20));
		vBox.add(box1);
		vBox.add(Box.createVerticalStrut(20));
		vBox.add(box2);
		vBox.add(Box.createVerticalStrut(20));
		vBox.add(box3);
		vBox.add(Box.createVerticalStrut(20));
		vBox.add(box4);
		vBox.add(Box.createVerticalStrut(20));
		vBox.add(box5);
		vBox.add(Box.createVerticalStrut(20));
		panel.add(vBox);
		add(panel);
		pack();
		setLocation(500, 200);
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
		confirmBtn.addMouseListener(new MouseAdapter() {
			
			public void mouseClicked(MouseEvent me){
				dd.setImgWidth(Integer.parseInt(widthField.getText()));
				dd.setImgHeight(Integer.parseInt(heightField.getText()));
				dd.setGapTime(Integer.parseInt(gapTimeField.getText()));
				dd.setAlwaysOnTop(topCheckBox.isSelected());
				dd.saveConfig();
				dd.refresh();
			}
		});
	}
}
