package cat.client;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.JWindow;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import cat.function.CatBean;
import cat.function.ClientBean;
import cat.util.CatUtil;
import cat.util.Xiu;

class CellRenderer extends JLabel implements ListCellRenderer {
	CellRenderer() {
		setOpaque(true);
	}

	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {

		setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));// ������Ϊ5�Ŀհױ߿�

		if (value != null) {
			setText(value.toString());
			setIcon(new ImageIcon("images//1.jpg"));
		}
		if (isSelected) {
			setBackground(new Color(255, 255, 153));// ���ñ���ɫ
			setForeground(Color.black);
		} else {
			// ����ѡȡ��ȡ��ѡȡ��ǰ���뱳����ɫ.
			setBackground(Color.white); // ���ñ���ɫ
			setForeground(Color.black);
		}
		setEnabled(list.isEnabled());
		setFont(new Font("sdf", Font.ROMAN_BASELINE, 13));
		setOpaque(true);
		return this;
	}
}


class UUListModel extends AbstractListModel{

	private Vector vs;

	public UUListModel(Vector vs){
		this.vs = vs;
	}

	@Override
	public Object getElementAt(int index) {
		// TODO Auto-generated method stub
		return vs.get(index);
	}

	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return vs.size();
	}
}

public class CatChatroom extends JFrame implements ActionListener{

	private static final long serialVersionUID = 6129126482250125466L;
	private int pos1;
	private int pos2;
	private static JPanel contentPane;
	private static Socket clientSocket;
	private static ObjectOutputStream oos;
	private static ObjectInputStream ois;
	private static String name;
	private static String passward;
	private static JTextPane textpane;
	private static AbstractListModel listmodel;
	private static JList list;
	private static String filePath;
	private static JLabel lblNewLabel;
	private static JProgressBar progressBar;
	private static Vector onlines;
	private static boolean isSendFile = false;
	private static boolean isReceiveFile = false;
	private static JTextPane textPane1 =null;
	// ����
	private static File file, file1,file2;
	private static URL cb, cb1,cb2;
	private static AudioClip aau,aau1, aau2;
	private JButton btnNewButton_2;
	private static MyLabel jlabel;

	private static String fontName;
	private static String fontSize;
	private  static Color fontColor;
	private JMenuItem ziti1;
	private JMenuItem ziti2;
	private JMenuItem ziti3;
	private JMenuItem ziti4;
	private JMenuItem ziti5;
	private JMenuItem daxiao1;
	private JMenuItem daxiao2;
	private JMenuItem daxiao3;
	private JMenuItem daxiao4;
	private JMenuItem daxiao5;
	private JMenuItem daxiao6;
	private JMenuItem colorChooser;
	private JMenu menu_3;
	private JMenu menu_2;
	private  static SimpleAttributeSet paneAttr;
	private static SimpleAttributeSet paneAttrSystem;
	private static SimpleAttributeSet paneAttrMsg;
	private JMenuItem menuItem;
	private JMenuItem menuItem_1;
	private JMenuItem menuItem_2;
	private JButton button_2;
	private  ScreenShotWindow ssw ;

	public ScreenShotWindow getSsw() {
		return ssw;
	}

	public void setSsw(ScreenShotWindow ssw) {
		this.ssw = ssw;
	}

	public class ScreenShotWindow extends JWindow {
		private int orgx,orgy,endx,endy;
		private BufferedImage image = new Robot().createScreenCapture(new Rectangle(0, 0,
		Toolkit.getDefaultToolkit().getScreenSize().width,Toolkit.getDefaultToolkit().getScreenSize().height));
		private BufferedImage tempImage = null;
		private BufferedImage saveImage =null;
		private ToolsWindow subOpWindows =null;
		public BufferedImage saveImage2 =null;
		
		public BufferedImage getSaveImage() {
			return saveImage;
		}

		public void setSaveImage(BufferedImage saveImage) {
			this.saveImage = saveImage;
		}
		
		public ToolsWindow getSubOpWindows() {
			return subOpWindows;
		}

		public void setSubOpWindows(ToolsWindow subOpWindows) {
			this.subOpWindows = subOpWindows;
		}

		public ScreenShotWindow() throws AWTException {

			this.setVisible(true);
			Dimension d =Toolkit.getDefaultToolkit().getScreenSize();
			this.setBounds(0,0,d.width,d.height);

			Robot robot;

			robot = new Robot();
			image =robot.createScreenCapture(new Rectangle(0, 0, d.width,d.height));		


			this.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					orgx=e.getX();
					orgy =e.getY();

					//�������ȷ���Ӵ��ڲ��򿪣���������գ���Ҳǿ�в�Ҫ����
					if(subOpWindows!=null){
						subOpWindows.setVisible(false);
					}
				}

				@Override
				public void mouseReleased(MouseEvent e) {
					if(subOpWindows==null){
						subOpWindows =new ToolsWindow(ScreenShotWindow.this,e.getX(),e.getY());				
					}else {
						subOpWindows.setLocation(e.getX(),e.getY());
					}
					subOpWindows.setVisible(true);
					subOpWindows.toFront();
				}	
			});

			this.addMouseMotionListener(new MouseMotionAdapter() {
				@Override
				public void mouseDragged(MouseEvent e) {
					endx =e.getX();
					endy =e.getY();

					//��ʱͼ�����ڻ�����Ļ���������Ļ��˸
					Image tempImage2= createImage(ScreenShotWindow.this.getWidth(),ScreenShotWindow.this.getHeight());
					Graphics g = tempImage2.getGraphics();				
					g.drawImage(tempImage, 0,0, null);

					int x = Math.min(orgx, endx);
					int y = Math.min(orgy, endy);
					int width = Math.abs(endx - orgx)+1;
					int height = Math.abs(endy - orgy)+1;
					g.setColor(Color.BLUE);
					g.drawRect(x-1, y-1, width+1, height+1);

					saveImage = image.getSubimage(x, y, width, height);
					g.drawImage(saveImage, x, y, null);

					//�����ȷ����������������ʱ�򣬱���ͼƬ�ܻ�����
					ScreenShotWindow.this.getGraphics().drawImage(tempImage2, 0, 0,ScreenShotWindow.this);		
				}
			});
		}

		@Override
		public void paint(Graphics g) {
			//������ͼ��Ļ����ɫ
			RescaleOp ro =new RescaleOp(0.8f, 0, null);
			tempImage = ro.filter(image, null);	
			//�ѽ�ͼ������
			g.drawImage(tempImage, 0, 0, this);
		}

		public class ToolsWindow extends JWindow{
			private ScreenShotWindow parent;
			public ToolsWindow(ScreenShotWindow parent ,int x,int y) {
				this.parent =parent;
				this.init();
				this.setLocation(x, y);
				//���pack()�������Զ�������ؼ��Ĵ�С�������øô��ڵĴ�С
				this.pack();
				this.setVisible(true);
			}


			private void init(){

				getContentPane().setLayout(new BorderLayout());
				JToolBar toolBar=new JToolBar("Java ��ͼ");
				
				JButton xiuButton=new JButton(new ImageIcon("images/xiu.jpg"));
				xiuButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						Xiu xiu=new Xiu(saveImage,CatChatroom.this);
						
						subOpWindows.dispose();
						parent.dispose();
					}
				});
				toolBar.add(xiuButton);
				
				//���水ť
				JButton saveButton=new JButton(new ImageIcon("images/fa.jpg"));
				saveButton.addActionListener(new ActionListener() { 
					@Override
					public void actionPerformed(ActionEvent e) {
						new Thread(){
							public void run() {
								try {
									InsertImage();
									subOpWindows.dispose();
									parent.dispose();
								} catch (BadLocationException e) {
									e.printStackTrace();
								}								
							};
						}.start();
					}
				});
				toolBar.add(saveButton);

				//�رհ�ť
				JButton closeButton=new JButton(new ImageIcon("images/zou.jpg"));
				closeButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						subOpWindows.dispose();
						parent.dispose();
					}
				});
				toolBar.add(closeButton);
		
				getContentPane().add(toolBar,BorderLayout.NORTH);
			}

			public  void  InsertImage() throws BadLocationException {
				if(saveImage!=null){
					SimpleDateFormat sdf =new SimpleDateFormat("yyyyMMddHHmmss");
					String fileName =sdf.format(new Date());
					String path ="ScreenCut/"+fileName+".jpg";						
					try {
						ImageIO.write(saveImage, "jpg", new File(path));
					} catch (IOException e) {
						e.printStackTrace();
					}
				
					List to = list.getSelectedValuesList();
					CatBean clientBean = new CatBean();
					clientBean.setType(8);
					clientBean.setName(name);
					String time = CatUtil.getTimer();
					clientBean.setTimer(time);	
					HashSet set = new HashSet();
					set.addAll(to);			
					clientBean.setClients(set);	
					clientBean.setIcon(new ImageIcon(path));
					sendMessage(clientBean);
					textpane.setCaretPosition(textpane.getDocument().getLength());
					textpane.getDocument().insertString(textpane.getDocument().getLength(), "\r\n"+time + " �Ҹ�" + to + "������һ��ͼ:\r\n" 
							+ "" , paneAttr);
					textpane.getDocument().insertString(textpane.getDocument().getLength(), "\r\n" , paneAttr);
					textpane.setCaretPosition(textpane.getDocument().getLength());
					textpane.insertIcon(new ImageIcon(path));

				}				
				else{
					System.out.println("saveimage is null");
				}
				this.dispose();
				parent.dispose();	
			}
		}

	}


	//���ڶ���
	public  void shakeWindows() {
		new Thread(){
			long begin = System.currentTimeMillis();
			long end = System.currentTimeMillis();
			Point p = CatChatroom.this.getLocationOnScreen();
			public void run(){
				int i = 1;
				while((end-begin)/1000<3){
					CatChatroom.this.setLocation(new Point((int)p.getX()-5*i,(int)p.getY()+5*i));
					end = System.currentTimeMillis();
					try {
						Thread.sleep(5);
						i=-i;
						CatChatroom.this.setLocation(p);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}

	public CatChatroom(String u_name, Socket client) {

		new Thread(){
			@Override
			public void run() {
				paneAttr =new SimpleAttributeSet();
				paneAttrMsg=new SimpleAttributeSet();
				paneAttrSystem =new SimpleAttributeSet();

				StyleConstants.setBold(paneAttr, true);
				StyleConstants.setForeground(paneAttrSystem, new Color(96, 96, 96));
				StyleConstants.setItalic(paneAttr, false);
				StyleConstants.setFontSize(paneAttr, 11);
				StyleConstants.setFontFamily(paneAttr,"����");

				StyleConstants.setBold(paneAttrMsg, true);
				StyleConstants.setForeground(paneAttrMsg, Color.black);
				StyleConstants.setItalic(paneAttrMsg, false);
				StyleConstants.setFontSize(paneAttrMsg, 15);
				StyleConstants.setFontFamily(paneAttrMsg,"����");

				StyleConstants.setForeground(paneAttrSystem, Color.blue);
				StyleConstants.setBold(paneAttrSystem, true);
				StyleConstants.setItalic(paneAttrSystem, false);
				StyleConstants.setFontSize(paneAttrSystem, 15);
				StyleConstants.setFontFamily(paneAttrSystem,"����");	
			}
		}.start();

		// ��ֵ
		name = u_name;
		clientSocket = client;
		onlines = new Vector();

		SwingUtilities.updateComponentTreeUI(this);

		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (InstantiationException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		} catch (UnsupportedLookAndFeelException e1) {
			e1.printStackTrace();
		}

		setTitle("���ߣ�"+name);
		setResizable(false);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(200, 100, 688, 543);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu menu = new JMenu("\u8BBE\u7F6E");
		menuBar.add(menu);

		menu_2 = new JMenu("\u5B57\u4F53\u8BBE\u7F6E");
		menu_2.setIcon(new ImageIcon("images/zitishezhi.jpg"));
		menu.add(menu_2);

		ziti1 = new JMenuItem("\u5B8B\u4F53");
		ziti1.setIcon(new ImageIcon("images/j1.jpg"));
		ziti1.addActionListener(this);
		ziti1.setFont(new Font("����", Font.PLAIN, 12));
		menu_2.add(ziti1);


		ziti2 = new JMenuItem("\u4EFF\u5B8B");
		ziti2.setIcon(new ImageIcon("images/j2.jpg"));
		ziti2.addActionListener(this);
		ziti2.setFont(new Font("����", Font.PLAIN, 14));
		menu_2.add(ziti2);

		ziti3 = new JMenuItem("\u6977\u4F53");
		ziti3.setIcon(new ImageIcon("images/j3.jpg"));
		ziti3.addActionListener(this);
		ziti3.setFont(new Font("����", Font.PLAIN, 14));
		menu_2.add(ziti3);

		ziti4 = new JMenuItem("\u5FAE\u8F6F\u96C5\u9ED1");
		ziti4.setIcon(new ImageIcon("images/j4.jpg"));
		ziti4.addActionListener(this);
		ziti4.setFont(new Font("΢���ź�", Font.PLAIN, 12));
		menu_2.add(ziti4);

		ziti5 = new JMenuItem("\u65B9\u6B63\u5170\u4EAD\u8D85\u7EC6\u9ED1\u7B80\u4F53");
		ziti5.setIcon(new ImageIcon("images/j5.jpg"));
		ziti5.addActionListener(this);
		ziti5.setFont(new Font("������ͤ��ϸ�ڼ���", Font.PLAIN, 12));
		menu_2.add(ziti5);

		menu_3 = new JMenu("\u5B57\u4F53\u5927\u5C0F");
		menu_3.setIcon(new ImageIcon("images/zitidaxiao.jpg"));
		menu.add(menu_3);

		daxiao1 = new JMenuItem("10");
		daxiao1.setIcon(new ImageIcon("images/k1.jpg"));
		daxiao1.addActionListener(this);
		menu_3.add(daxiao1);

		daxiao2 = new JMenuItem("12");
		daxiao2.setIcon(new ImageIcon("images/k2.jpg"));
		daxiao2.addActionListener(this);
		menu_3.add(daxiao2);

		daxiao3 = new JMenuItem("14");
		daxiao3.setIcon(new ImageIcon("images/k3.jpg"));
		daxiao3.addActionListener(this);
		menu_3.add(daxiao3);

		daxiao4 = new JMenuItem("16");
		daxiao4.setIcon(new ImageIcon("images/k4.jpg"));
		daxiao4.addActionListener(this);
		menu_3.add(daxiao4);

		daxiao5 = new JMenuItem("20");
		daxiao5.setIcon(new ImageIcon("images/k5.jpg"));
		daxiao5.addActionListener(this);
		menu_3.add(daxiao5);

		daxiao6 = new JMenuItem("30");
		daxiao6.setIcon(new ImageIcon("images/k6.jpg"));
		daxiao6.addActionListener(this);
		menu_3.add(daxiao6);

		colorChooser = new JMenuItem("\u5B57\u4F53\u989C\u8272");
		colorChooser.setIcon(new ImageIcon("images/tiaoseban.jpg"));
		colorChooser.addActionListener(this);
		menu.add(colorChooser);

		JMenu menu_1 = new JMenu("\u5173\u4E8E");
		menuBar.add(menu_1);

		menuItem = new JMenuItem("\u529F\u80FD\u8BF4\u660E");
		menuItem.addActionListener(this);
		menuItem.setIcon(new ImageIcon("images/shuooming.jpg"));
		menu_1.add(menuItem);

		menuItem_1 = new JMenuItem("\u7248\u672C\u4FE1\u606F");
		menuItem_1.addActionListener(this);
		menuItem_1.setIcon(new ImageIcon("images/shuoooming.jpg"));
		menu_1.add(menuItem_1);

		menuItem_2 = new JMenuItem("\u5EFA\u8BAE");
		menuItem_2.addActionListener(this);
		menuItem_2.setIcon(new ImageIcon("images/jianyi.jpg"));
		menu_1.add(menuItem_2);

		contentPane = new JPanel() {
			private static final long serialVersionUID = 1L;
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(new ImageIcon("images\\iii.jpg").getImage(), 0, 0, getWidth(), getHeight(), null);
			}
		};
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// ������Ϣ��ʾ����
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 10, 410, 300);
		getContentPane().add(scrollPane);

		textpane = new JTextPane();
		textpane.setEditable(false); 
		textpane.setFont(new Font("sdf", Font.BOLD, 13));
		scrollPane.setViewportView(textpane);

		// ��������
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(10, 347, 411, 97);
		getContentPane().add(scrollPane_1);

		textPane1 = new JTextPane();
		scrollPane_1.setViewportView(textPane1);

		// �رհ�ť
		final JButton btnNewButton = new JButton("\u5173\u95ED");
		btnNewButton.setFont(new Font("����", Font.BOLD, 14));
		btnNewButton.setBounds(141, 448, 71, 30);
		getContentPane().add(btnNewButton);

		// ���Ͱ�ť
		JButton btnNewButton_1 = new JButton("\u53D1\u9001");
		btnNewButton_1.setFont(new Font("����", Font.BOLD, 14));
		btnNewButton_1.setBounds(222, 448, 71, 30);
		getRootPane().setDefaultButton(btnNewButton_1);
		getContentPane().add(btnNewButton_1);

		// �����û��б�
		listmodel = new UUListModel(onlines) ;
		list = new JList(listmodel);
		list.setCellRenderer(new CellRenderer());
		list.setOpaque(false);
		Border etch = BorderFactory.createEtchedBorder();
		list.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, new Color(64, 64, 64)), "\u5728\u7EBF\u7528\u6237:",
				TitledBorder.LEADING, TitledBorder.TOP, new Font("����",Font.BOLD	, 18), new Color(0, 0, 0)));

		JScrollPane scrollPane_2 = new JScrollPane(list);
		scrollPane_2.setBounds(430, 10, 245, 375);
		scrollPane_2.setOpaque(false);
		scrollPane_2.getViewport().setOpaque(false);
		getContentPane().add(scrollPane_2);

		// �ļ�������
		progressBar = new JProgressBar();
		progressBar.setBounds(430, 390, 245, 15);
		progressBar.setMinimum(1);
		progressBar.setMaximum(100);
		getContentPane().add(progressBar);

		// �ļ�������ʾ
		lblNewLabel = new JLabel(
				"\u6587\u4EF6\u4F20\u9001\u4FE1\u606F\u680F:");
		lblNewLabel.setFont(new Font("����", Font.BOLD, 15));
		lblNewLabel.setBackground(Color.WHITE);
		lblNewLabel.setBounds(430, 410, 245, 15);
		getContentPane().add(lblNewLabel);

		JPanel panel = new JPanel();
		panel.setBounds(10, 310, 410, 35);
		contentPane.add(panel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

		btnNewButton_2 = new JButton();
		btnNewButton_2.setBorderPainted(false);
		btnNewButton_2.setOpaque(false);  
		btnNewButton_2.setContentAreaFilled(false);  
		btnNewButton_2.setMargin(new Insets(0, 0, 0, 0));  
		btnNewButton_2.setFocusPainted(false);  
		btnNewButton_2.setBorderPainted(false);  
		btnNewButton_2.setBorder(null); 
		btnNewButton_2.setIcon(new ImageIcon(CatChatroom.class.getResource("/cat/anniu/biaoqinganniu.jpg")));
		btnNewButton_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {			
				new PicsJWindow(CatChatroom.this).setVisible(true);				
			}
		});
		btnNewButton_2.setFont(new Font("����", Font.BOLD, 18));
		panel.add(btnNewButton_2);

		JButton button = new JButton();
		button.setBorderPainted(false);
		button.setOpaque(false);  
		button.setContentAreaFilled(false);  
		button.setMargin(new Insets(0, 0, 0, 0));  
		button.setFocusPainted(false);  
		button.setBorderPainted(false);  
		button.setBorder(null); 
		button.setIcon(new ImageIcon(CatChatroom.class.getResource("/cat/anniu/zhendonganniu.jpg")));
			button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				List to =list.getSelectedValuesList();
				CatBean clientBean = new CatBean();
				clientBean.setType(9);
				clientBean.setName(name);
				String time = CatUtil.getTimer();
				clientBean.setTimer(time);
				HashSet set = new HashSet();
				set.addAll(to);
				clientBean.setClients(set);		
				sendMessage(clientBean);
			}
		});
		button.setFont(new Font("����", Font.BOLD, 18));

		panel.add(button);

		button_2 = new JButton(); 
		button_2.setBorderPainted(false);
		button_2.setOpaque(false);  
		button_2.setContentAreaFilled(false);  
		button_2.setMargin(new Insets(0, 0, 0, 0));  
		button_2.setFocusPainted(false);  
		button_2.setBorderPainted(false);  
		button_2.setBorder(null); 
		button_2.setIcon(new ImageIcon(CatChatroom.class.getResource("/cat/anniu/jietuanniu.jpg")));
		button_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				new Thread(){
					public void run() {
						try {
							 ssw =new ScreenShotWindow();
						} catch (AWTException e) {
							e.printStackTrace();
						}	
					};
				}.start();
			}
		});
		button_2.setFont(new Font("����", Font.BOLD, 18));
		panel.add(button_2);
		

		JButton button_1 = new JButton("��������");
		button_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				new RobotView();
			}
		});
		button_1.setBounds(301, 448, 119, 29);
		button_1.setFont(new Font("����", Font.BOLD, 18));
		contentPane.add(button_1);


		try {
			oos = new ObjectOutputStream(clientSocket.getOutputStream());
			// ��¼���߿ͻ�����Ϣ��catbean�У������͸�������
			CatBean bean = new CatBean();
			bean.setType(0);
			bean.setName(name);
			bean.setTimer(CatUtil.getTimer());
			oos.writeObject(bean);
			oos.flush();

			// ��Ϣ��ʾ����
			file = new File("sounds\\system.wav");
			cb = file.toURL();
			aau = Applet.newAudioClip(cb);

			file1 = new File("sounds\\shake.wav");
			cb1 = file1.toURL();
			aau1 = Applet.newAudioClip(cb1);

			// ������ʾ����
			file2 = new File("sounds\\Global.wav");
			cb2 = file2.toURL();
			aau2 = Applet.newAudioClip(cb2);

			// �����ͻ������߳�
			new ClientInputThread().start();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// ���Ͱ�ť
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String info = textPane1.getText();

				List to = list.getSelectedValuesList();

				if (to.size() < 1) {
					JOptionPane.showMessageDialog(getContentPane(), "��ѡ���������");
					return;
				}
				if (to.toString().contains(name+"(��)")) {
					JOptionPane
					.showMessageDialog(getContentPane(), "�������Լ�������Ϣ");
					return;
				}
				if (info.equals("")) {
					JOptionPane.showMessageDialog(getContentPane(), "���ܷ��Ϳ���Ϣ");
					return;
				}

				CatBean clientBean = new CatBean();
				clientBean.setType(1);
				clientBean.setName(name);
				String time = CatUtil.getTimer();
				clientBean.setTimer(time);
				clientBean.setInfo(info);		
				clientBean.setAttributeSet(paneAttrMsg);
				HashSet set = new HashSet();
				set.addAll(to);			
				clientBean.setClients(set);	


				String[] strings =info.split("[|]");
				StringBuilder sBuilder = new StringBuilder() ;
			
				for(int i =0;i<strings.length;i++){
					if(!(strings[i].startsWith("src/cat/")&&strings[i].endsWith(".gif"))){
						sBuilder.append(strings[i]);
						System.out.println(strings[i]);
					}
				}
				System.out.println(sBuilder);

				// �Լ���������ҲҪ��ʵ���Լ�����Ļ����
				try {
					if(sBuilder!=null){
						textpane.getDocument().insertString(textpane.getDocument().getLength(), "\r\n"+time + " �Ҷ�" 
								+ to + "˵:\r\n" , paneAttr);
						textpane.getDocument().insertString(textpane.getDocument().getLength(),
								sBuilder + "", paneAttrMsg);
					}
					else{
						textpane.getDocument().insertString(textpane.getDocument().getLength(), "\r\n"+time + " �Ҷ�" + to + "˵:\r\n" 
								+ "" , paneAttr);
					}
				} catch (BadLocationException e1) {
					e1.printStackTrace();
				}
				if(jlabel!=null){
					insertImage2(jlabel);
				}

				sendMessage(clientBean);
				
				textPane1.setText(null);
				textPane1.requestFocus();
			}
		});

		// �رհ�ť
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(isSendFile || isReceiveFile){
					JOptionPane.showMessageDialog(contentPane,
							"���ڴ����ļ��У��������뿪...",
							"Error Message", JOptionPane.ERROR_MESSAGE);
				}else{
					btnNewButton.setEnabled(false);
					CatBean clientBean = new CatBean();
					clientBean.setType(-1);
					clientBean.setName(name);
					clientBean.setTimer(CatUtil.getTimer());
					sendMessage(clientBean);
				}
			}
		});

		// �뿪
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				if(isSendFile || isReceiveFile){
					JOptionPane.showMessageDialog(contentPane,
							"���ڴ����ļ��У��������뿪...",
							"Error Message", JOptionPane.ERROR_MESSAGE);
				}else{
					int result = JOptionPane.showConfirmDialog(getContentPane(),
							"��ȷ��Ҫ�뿪������");
					if (result == 0) {
						CatBean clientBean = new CatBean();
						clientBean.setType(-1);
						clientBean.setName(name);
						clientBean.setTimer(CatUtil.getTimer());
						sendMessage(clientBean);
					}
				}
			}
		});

		// �б����
		list.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				List to = list.getSelectedValuesList();
				if (e.getClickCount() == 2) {

					if (to.toString().contains(name+"(��)")) {
						JOptionPane
						.showMessageDialog(getContentPane(), "�������Լ������ļ�");
						return;
					}

					// ˫�����ļ��ļ�ѡ���
					JFileChooser chooser = new JFileChooser();
					chooser.setDialogTitle("ѡ���ļ���"); // ����Ŷ...
					chooser.showDialog(getContentPane(), "ѡ��"); // ���ǰ�ť������..

					// �ж��Ƿ�ѡ�����ļ�
					if (chooser.getSelectedFile() != null) {
						// ��ȡ·��
						filePath = chooser.getSelectedFile().getPath();
						File file = new File(filePath);
						// �ļ�Ϊ��
						if (file.length() == 0) {
							JOptionPane.showMessageDialog(getContentPane(),
									filePath + "�ļ�Ϊ��,��������.");
							return;
						}

						CatBean clientBean = new CatBean();
						clientBean.setType(2);// �������ļ�
						clientBean.setSize(new Long(file.length()).intValue());
						clientBean.setName(name);
						clientBean.setTimer(CatUtil.getTimer());
						clientBean.setFileName(file.getName()); // ��¼�ļ�������
						clientBean.setInfo("�������ļ�");

						// �ж�Ҫ���͸�˭
						HashSet<String> set = new HashSet<String>();
						set.addAll(list.getSelectedValuesList());
						clientBean.setClients(set);
						sendMessage(clientBean);
					}
				}
			}
		});

	}

	public JButton getBtn() {
		return 	btnNewButton_2;
	}

	class ClientInputThread extends Thread {

		@Override
		public void run() {
			try {
				// ��ͣ�Ĵӷ�����������Ϣ
				while (true) {
					ois = new ObjectInputStream(clientSocket.getInputStream());
					final CatBean  bean = (CatBean) ois.readObject();
					switch (bean.getType()) {
					case 0: {
						// �����б�
						onlines.clear();
						HashSet<String> clients = bean.getClients();
						Iterator<String> it = clients.iterator();
						while (it.hasNext()) {
							String ele = it.next();
							if (name.equals(ele)) {
								onlines.add(ele + "(��)");
							} else {
								onlines.add(ele);
							}
						}

						listmodel = new UUListModel(onlines);
						list.setModel(listmodel);
						aau2.play();
						Document docs2 = textpane.getDocument();																			
						try {																																
							docs2.insertString(docs2.getLength(), bean.getInfo() + "\r\n" , paneAttrSystem);						
						} catch (BadLocationException e) {																					
							e.printStackTrace();																									
						}		
						textpane.selectAll();
						break;
					}
					case -1: {

						return;
					}
					case 1: {
						if(bean.getClients()!=null){
							textpane.setCaretPosition(textpane.getDocument().getLength());
							String info ="\r\n"+getTimer() + "  " + bean.getName()
							+ " �� " + bean.getClients() + "˵: \r\n";
							Document docs = textpane.getDocument();																			
							try {																																
								docs.insertString(docs.getLength(), info , paneAttr);						
							} catch (BadLocationException e) {																					
								e.printStackTrace();																									
							}
							aau.play();		
							textpane.setCaretPosition(textpane.getDocument().getLength());
							String[] strings =bean.getInfo().split("[|]");
							for (int i = 0; i < strings.length; i++) {
								String string = strings[i];
								if(string.endsWith(".gif")&&string.startsWith("src/cat/qqdefaultface/")){	
									textpane.setCaretPosition(textpane.getCaretPosition());
									textpane.insertIcon(new ImageIcon(string));
								}else{
									Document docs2 = textpane.getDocument();																			
									try {											
										docs.insertString(docs2.getLength(), string, bean.getAttributeSet());						
									} catch (BadLocationException e) {																					
										e.printStackTrace();																									
									}
								}				
							}
							Document docs3 = textpane.getDocument();																			
							try {																																
								docs3.insertString(docs.getLength(), "\r\n", null);						
							} catch (BadLocationException e) {																					
								e.printStackTrace();																									
							}
							textpane.selectAll();
							break;	
						}else {
							String info ="\r\n"+getTimer() + "  " + bean.getName()
							+ " �� " + "  ��  "+ "˵: \r\n";

							aau.play();

							Document docs = textpane.getDocument();																
							try {																																	//--
								docs.insertString(docs.getLength(), info, paneAttrSystem);	
								docs.insertString(docs.getLength(), bean.getInfo() + "\r\n", bean.getAttributeSet());	
							} catch (BadLocationException e) {																				
								e.printStackTrace();																								
							}																																		//--
							textpane.selectAll();
							break;
						}						
					}

					case 2: {
						// ���ڵȴ�Ŀ��ͻ�ȷ���Ƿ�����ļ��Ǹ�����״̬�������������̴߳���
						new Thread(){
							public void run() {
								//��ʾ�Ƿ�����ļ��Ի���
								int result = JOptionPane.showConfirmDialog(
										getContentPane(), bean.getInfo());
								switch(result){
								case 0:{  //�����ļ�
									JFileChooser chooser = new JFileChooser();
									chooser.setDialogTitle("�����ļ���"); // ����Ŷ...
									//Ĭ���ļ����ƻ��з��ڵ�ǰĿ¼��
									chooser.setSelectedFile(new File(bean
											.getFileName()));
									chooser.showDialog(getContentPane(), "����"); // ���ǰ�ť������..
									//����·��
									String saveFilePath =chooser.getSelectedFile().toString();

									//�����ͻ�CatBean
									CatBean clientBean = new CatBean();
									clientBean.setType(3);
									clientBean.setName(name);  //�����ļ��Ŀͻ�����
									clientBean.setTimer(CatUtil.getTimer());
									clientBean.setFileName(saveFilePath);
									clientBean.setInfo("ȷ�������ļ�");

									// �ж�Ҫ���͸�˭
									HashSet<String> set = new HashSet<String>();
									set.add(bean.getName());
									clientBean.setClients(set);  //�ļ���Դ
									clientBean.setTo(bean.getClients());//����Щ�ͻ������ļ�

									// �����µ�tcp socket ��������, ���Ƕ������ӵĹ���,
									try {
										ServerSocket ss = new ServerSocket(0); // 0���Ի�ȡ���еĶ˿ں�
										clientBean.setIp(clientSocket.getInetAddress().getHostAddress());
										clientBean.setPort(ss.getLocalPort());
										sendMessage(clientBean); // ��ͨ�����������߷��ͷ�, �����ֱ�ӷ����ļ�����������...

										isReceiveFile=true;
										//�ȴ��ļ���Դ�Ŀͻ��������ļ�....Ŀ��ͻ��������϶�ȡ�ļ�����д�ڱ�����
										Socket sk = ss.accept();
										Document docs = textpane.getDocument();																			
										try {																																
											docs.insertString(docs.getLength(), CatUtil.getTimer() + "  " + bean.getFileName()
											+ "�ļ�������.\r\n" ,paneAttrSystem);						
										} catch (BadLocationException e) {																					
											e.printStackTrace();																									
										}		
										//										textpane.setText(CatUtil.getTimer() + "  " + bean.getFileName()
										//								+ "�ļ�������.\r\n");
										//                                        textpane.append(CatUtil.getTimer() + "  " + bean.getFileName()
										//												+ "�ļ�������.\r\n");
										DataInputStream dis = new DataInputStream(  //�������϶�ȡ�ļ�
												new BufferedInputStream(sk.getInputStream()));
										DataOutputStream dos = new DataOutputStream(  //д�ڱ�����
												new BufferedOutputStream(new FileOutputStream(
														saveFilePath)));

										int count = 0;
										int num = bean.getSize() / 100;
										int index = 0;
										while (count < bean.getSize()) {
											int t = dis.read();
											dos.write(t);
											count++;

											if(num>0){
												if (count % num == 0 && index < 100) {
													progressBar.setValue(++index);
												}
												lblNewLabel.setText("���ؽ���:" + count
														+ "/" + bean.getSize() + "  ����" + index
														+ "%");
											}else{
												lblNewLabel.setText("���ؽ���:" + count
														+ "/" + bean.getSize() +"  ����:"+new Double(new Double(count).doubleValue()/new Double(bean.getSize()).doubleValue()*100).intValue()+"%");
												if(count==bean.getSize()){
													progressBar.setValue(100);
												}
											}

										}

										//���ļ���Դ�ͻ�������ʾ���ļ��������
										PrintWriter out = new PrintWriter(sk.getOutputStream(),true);
										out.println(CatUtil.getTimer() + " ���͸�"+name+"���ļ�[" + bean.getFileName()+"]"
												+ "�ļ��������.\r\n");
										out.flush();
										dos.flush();
										dos.close();
										out.close();
										dis.close();
										sk.close();
										ss.close();

										Document docs2 = textpane.getDocument();																			
										try {																																
											docs2.insertString(docs2.getLength(), CatUtil.getTimer() + "  " + bean.getFileName()
											+ "�ļ��������.���λ��Ϊ:"+saveFilePath+"\r\n" , paneAttrSystem);						
										} catch (BadLocationException e) {																					
											e.printStackTrace();																									
										}		
										//										textpane.setText(CatUtil.getTimer() + "  " + bean.getFileName()
										//										+ "�ļ��������.���λ��Ϊ:"+saveFilePath+"\r\n");
										//										textpane.append(CatUtil.getTimer() + "  " + bean.getFileName()
										//												+ "�ļ��������.���λ��Ϊ:"+saveFilePath+"\r\n");
										isReceiveFile = false;
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

									break;
								}
								default: {
									CatBean clientBean = new CatBean();
									clientBean.setType(4);
									clientBean.setName(name);  //�����ļ��Ŀͻ�����
									clientBean.setTimer(CatUtil.getTimer());
									clientBean.setFileName(bean.getFileName());
									clientBean.setInfo(CatUtil.getTimer() + "  "
											+ name + "ȡ�������ļ�["
											+ bean.getFileName() + "]");


									// �ж�Ҫ���͸�˭
									HashSet<String> set = new HashSet<String>();
									set.add(bean.getName());
									clientBean.setClients(set);  //�ļ���Դ
									clientBean.setTo(bean.getClients());//����Щ�ͻ������ļ�

									sendMessage(clientBean);

									break;

								}
								}
							};	
						}.start();
						break;
					}
					case 3: {  //Ŀ��ͻ�Ը������ļ���Դ�ͻ���ʼ��ȡ�����ļ������͵�������
						//						textpane.append(bean.getTimer() + "  "+ bean.getName() + "ȷ�������ļ�" + ",�ļ�������..\r\n");
						//						textpane.setText(bean.getTimer() + "  "+ bean.getName() + "ȷ�������ļ�" + ",�ļ�������..\r\n");

						Document docs2 = textpane.getDocument();																			
						try {																																
							docs2.insertString(docs2.getLength(), bean.getTimer() + "  "+ bean.getName() + 
									"ȷ�������ļ�" + ",�ļ�������..\r\n" , paneAttrSystem);						
						} catch (BadLocationException e) {																					
							e.printStackTrace();																									
						}		
						new Thread(){
							public void run() {

								try {
									isSendFile = true;
									//����Ҫ�����ļ��Ŀͻ��׽���
									Socket s = new Socket(bean.getIp(),bean.getPort());
									DataInputStream dis = new DataInputStream(
											new FileInputStream(filePath));  //���ض�ȡ�ÿͻ��ղ�ѡ�е��ļ�
									DataOutputStream dos = new DataOutputStream(
											new BufferedOutputStream(s
													.getOutputStream()));  //����д���ļ�


									int size = dis.available();

									int count = 0;  //��ȡ����
									int num = size / 100;
									int index = 0;
									while (count < size) {

										int t = dis.read();
										dos.write(t);
										count++;  //ÿ��ֻ��ȡһ���ֽ�

										if(num>0){
											if (count % num == 0 && index < 100) {
												progressBar.setValue(++index);

											}
											lblNewLabel.setText("�ϴ�����:" + count + "/"
													+ size + "  ����" + index
													+ "%");
										}else{
											lblNewLabel.setText("�ϴ�����:" + count + "/"
													+ size +"  ����:"+new Double(new Double(count).doubleValue()/new Double(size).doubleValue()*100).intValue()+"%"
													);
											if(count==size){
												progressBar.setValue(100);
											}
										}
									}
									dos.flush();
									dis.close();
									//��ȡĿ��ͻ�����ʾ������ϵ���Ϣ...
									BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
									//								    textpane.append( br.readLine() + "\r\n");
									//								    textpane.setText(br.readLine() + "\r\n");
									Document docs2 = textpane.getDocument();																			
									try {																																
										docs2.insertString(docs2.getLength(), br.readLine() + "\r\n" , paneAttrSystem);						
									} catch (BadLocationException e) {																					
										e.printStackTrace();																									
									}		

									isSendFile = false;
									br.close();
									s.close();
								} catch (Exception ex) {
									ex.printStackTrace();
								}

							};
						}.start();
						break;
					}
					case 4: {
						Document docs2 = textpane.getDocument();																			
						try {														   
							docs2.insertString(docs2.getLength(), bean.getInfo() + "\r\n" , paneAttrSystem);						
						} catch (BadLocationException e) {																					
							e.printStackTrace();																									
						}		

						break;
					}
					case 9:{
						Document docs = textpane.getDocument();
						aau1.play();
						try {																																	
							docs.insertString(docs.getLength(), "\r\n"+bean.getName()+"���㷢����һ����" + "\r\n", paneAttrSystem);	
							shakeWindows();
						} catch (BadLocationException e) {																				
							e.printStackTrace();																								
						}																																		
						textpane.selectAll();
						break;
					}
					case 8:{
						Document docs = textpane.getDocument();
						aau1.play();
						try {		
							textpane.setCaretPosition(textpane.getDocument().getLength());
							docs.insertString(docs.getLength(), "\r\n"+bean.getName()+"  "+CatUtil.getTimer()+":  "+"���㷢����һ��ͼƬ" + "\r\n", paneAttr);
							textpane.setCaretPosition(textpane.getDocument().getLength());
							textpane.insertIcon(bean.getIcon());
						} catch (BadLocationException e) {																				
							e.printStackTrace();																								
						}																																		
						textpane.selectAll();
						break;
					}
					default: {
						break;
					}
					}

				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if (clientSocket != null) {
					try {
						clientSocket.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				System.exit(0);
			}
		}
	}

	private void sendMessage(CatBean clientBean) {
		try {
			oos = new ObjectOutputStream(clientSocket.getOutputStream());
			oos.writeObject(clientBean);
			oos.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String getTimer() {
		SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd  HH-mm-ss");
		return sdf.format(new Date());
	}

	//����ͼƬ�����ʹ���
	public static  void insertImage(MyLabel jlabel) {
		CatChatroom.jlabel =jlabel;
		textPane1.setCaretPosition(textPane1.getDocument().getLength());
		StyledDocument styledDocument =(StyledDocument)textPane1.getDocument();
		Style style = styledDocument.addStyle("icon", null);
		StyleConstants.setIcon(style, jlabel.getIcon());
		try {
			styledDocument.insertString(styledDocument.getLength(),"|"+jlabel.getPath()+"|", style);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	//����ͼƬ�����촰��
	public static  void insertImage2(MyLabel jlabel) {
		CatChatroom.jlabel =jlabel;
		textpane.setCaretPosition(textpane.getDocument().getLength());
		StyledDocument styledDocument =(StyledDocument)textpane.getDocument();
		Style style = styledDocument.addStyle("icon", null);
		StyleConstants.setIcon(style, jlabel.getIcon());
		try {
			styledDocument.insertString(styledDocument.getLength(),"ignore text", style);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		CatChatroom.jlabel=null;
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==colorChooser){		 
			fontColor=JColorChooser.showDialog(CatChatroom.this,	"ѡ��������ɫ", Color.white);	
		}else if(e.getSource()==ziti1){
			fontName=ziti1.getText();
		}else if(e.getSource()==ziti2){
			fontName=ziti2.getText();
		}else if(e.getSource()==ziti3){
			fontName=ziti3.getText();
		}else if(e.getSource()==ziti4){
			fontName=ziti4.getText();
		}else if(e.getSource()==ziti5){
			fontName=ziti5.getText();
		}else if (e.getSource()==daxiao1) {
			fontSize=daxiao1.getText();
		}else if (e.getSource()==daxiao2) {
			fontSize=daxiao2.getText();
		}else if (e.getSource()==daxiao3) {
			fontSize =daxiao3.getText();
		}else if (e.getSource()==daxiao4) {
			fontSize=daxiao4.getText();
		}else if (e.getSource()==daxiao5) {
			fontSize=daxiao5.getText();
		}else if (e.getSource()==daxiao6) {
			fontSize=daxiao6.getText();
		}else if(e.getSource()==menuItem){
			JOptionPane.showMessageDialog(CatChatroom.this, "���칤�߼�ʹ��˵��\r\n"
					+ "1��ѡ�������û�������Ϣ\r\n"
					+ "2��˫���û������ļ�\r\n"
					+ "3����ѡ�û�ʵ��Ⱥ����Ϣ\r\n");
		}else if(e.getSource()==menuItem_1){
			JOptionPane.showMessageDialog(CatChatroom.this, "�汾1.0\r\n"
					+ "������Ի���������Э���ʵ������¹���\r\n");
		}else if(e.getSource()==menuItem_2){
			JOptionPane.showMessageDialog(CatChatroom.this, "�о���ѡ�Ӧ����\r\n"
					+ "��Ϊ��֪��дʲô");
		}

		new Thread(){
			//������߼��жϺ���Ҫ����Ȼ��������Ȼ��ͻ�Ӱ�쵽�ҵ���������Ե����ã�����Ļ������ò�����������
			public void run() {
				if(fontColor!=null){
					StyleConstants.setForeground(paneAttrMsg, fontColor);
				}
				System.out.println(fontColor);
				if(fontName!=null){
					StyleConstants.setFontFamily(paneAttrMsg, fontName);
				}
				System.out.println(fontName);
				if(fontSize!=null){
					StyleConstants.setFontSize(paneAttrMsg, Integer.parseInt(fontSize));
				}
				System.out.println(fontSize);
			};
		}.start();

	}

}
