package cat.login;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import cat.util.CatUtil;

public class CatResign extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JPasswordField passwordField;
	private JPasswordField passwordField_1;
	private JLabel lblNewLabel;

	public CatResign() {
		setTitle("Registered cat chat room\n");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(350, 250, 600, 400);
		contentPane = new JPanel() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(new ImageIcon("images\\register.jpg").getImage(), 0,0, getWidth(), getHeight(), null);
			}
		};
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);




		final JLabel lblNewLabel1 = new JLabel();
		lblNewLabel1.setBounds(145, 75, 151, 21);
		lblNewLabel1.setForeground(Color.white);
		lblNewLabel1.setFont(new Font("微软雅黑", Font.BOLD, 20));
		lblNewLabel1.setText("用户名");
		getContentPane().add(lblNewLabel1);

		textField = new JTextField();
		textField.setBounds(230, 70, 200, 30);			//文本框的位置设置
		textField.setOpaque(false);
		textField.setFont(new Font("微软雅黑", Font.BOLD, 15));
		textField.setForeground(Color.white);
		contentPane.add(textField);
		textField.setColumns(10);

		final JLabel lblNewLabel2 = new JLabel();
		lblNewLabel2.setBounds(145, 135, 151, 21);
		lblNewLabel2.setForeground(Color.white);
		lblNewLabel2.setFont(new Font("微软雅黑", Font.BOLD, 20));
		lblNewLabel2.setText("输入密码");
		getContentPane().add(lblNewLabel2);

		passwordField = new JPasswordField();
		passwordField.setForeground(Color.white);
		passwordField.setFont(new Font("微软雅黑", Font.BOLD, 15));
		passwordField.setEchoChar('*');		//字符回显设置
		passwordField.setOpaque(false);
		passwordField.setBounds(230, 130, 200, 30);
		contentPane.add(passwordField);

		final JLabel lblNewLabel3 = new JLabel();
		lblNewLabel3.setBounds(145, 195, 151, 21);
		lblNewLabel3.setForeground(Color.white);
		lblNewLabel3.setFont(new Font("微软雅黑", Font.BOLD, 20));
		lblNewLabel3.setText("确认密码");
		getContentPane().add(lblNewLabel3);

		passwordField_1 = new JPasswordField();
		passwordField_1.setForeground(Color.white);
		passwordField_1.setFont(new Font("微软雅黑", Font.BOLD, 15));
		passwordField_1.setEchoChar('*');		//字符回显设置
		passwordField_1.setOpaque(false);
		passwordField_1.setBounds(230, 190, 200, 30);
		contentPane.add(passwordField_1);

		//注册按钮
//		final JButton btnNewButton_1 = new JButton();
//		btnNewButton_1.setIcon(new ImageIcon("images\\注册1.jpg"));
//		btnNewButton_1.setBounds(320, 198, 80, 40);
//		getRootPane().setDefaultButton(btnNewButton_1);
//		contentPane.add(btnNewButton_1);

		final JButton btnNewButton_1 = new JButton("注册");
//		btnNewButton_1.setIcon(new ImageIcon("images\\\u6CE8\u518C.jpg"));
		btnNewButton_1.setFont(new Font("微软雅黑", Font.BOLD, 20));
		btnNewButton_1.setBounds(360, 250, 80, 40);
		contentPane.add(btnNewButton_1);

		//返回按钮
//		final JButton btnNewButton = new JButton("返回");
////		btnNewButton.setIcon(new ImageIcon("images\\返回.jpg"));
//		btnNewButton.setBounds(230, 198, 80, 40);
//		contentPane.add(btnNewButton);

		final JButton btnNewButton = new JButton("返回");
//		btnNewButton.setIcon(new ImageIcon("images\\\u767B\u9646.jpg"));
		btnNewButton.setFont(new Font("微软雅黑", Font.BOLD, 20));
		btnNewButton.setBounds(190, 250, 80, 40);
		getRootPane().setDefaultButton(btnNewButton);
		contentPane.add(btnNewButton);

		//提示信息
		lblNewLabel = new JLabel();
		lblNewLabel.setBounds(55, 250, 185, 20);
		lblNewLabel.setForeground(Color.red);
		contentPane.add(lblNewLabel);

		//返回按钮监听
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnNewButton.setEnabled(false);
				//返回登陆界面
				CatLogin frame = new CatLogin();
				frame.setVisible(true);
				setVisible(false);
			}
		});

		//注册按钮监听
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Properties userPro = new Properties();
				File file = new File("Users.properties");
				CatUtil.loadPro(userPro, file);

				String u_name = textField.getText();
				String u_pwd = new String(passwordField.getPassword());
				String u_pwd_ag = new String(passwordField_1.getPassword());

				// 判断用户名是否在普通用户中已存在
				if (u_name.length() != 0) {	//首先用户名不能为空

					if (userPro.containsKey(u_name)) {		//判断用户名是否在userPro储存的信息中已经存在
						lblNewLabel.setText("用户名已存在!");
					} else {
						isPassword(userPro, file, u_name, u_pwd, u_pwd_ag);
					}
				} else {
					lblNewLabel.setText("用户名不能为空！");
				}
			}

			private void isPassword(Properties userPro, File file, String u_name, String u_pwd, String u_pwd_ag) {
				if (u_pwd.equals(u_pwd_ag)) {		//判断两次输入密码是否一致
					if (u_pwd.length() != 0) {	//密码不能为空
						userPro.setProperty(u_name, u_pwd_ag);			//userPro开始增加设置Properties属性文件（用户名和密码成为一对键值对）
						try {
							userPro.store(new FileOutputStream(file),	//将Properties类对象的属性列表保存到输出流中
									"Copyright (c) Boxcode Studio");
						} catch (FileNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						btnNewButton_1.setEnabled(false);			//注册完成自动返回登录界面
						//返回登陆界面
						CatLogin frame = new CatLogin();			//登录的界面进行构建（通过下面的三行代码完成登录界面的显示）
						frame.setVisible(true);
						setVisible(false);
					} else {
						lblNewLabel.setText("密码为空！");
					}
				} else {
					lblNewLabel.setText("密码不一致！");
				}
			}
		});
	}
}
