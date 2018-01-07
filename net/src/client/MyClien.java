package client;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

import javax.swing.*;
import javax.swing.border.*;

public class MyClien extends JFrame { // 创建类继承JFrame类
	/**
	 * 
	 */
	private BufferedReader reader;
	private static final long serialVersionUID = 1L;
	private PrintWriter writer; // 声明PrintWriter类对象
	Socket socket; // 声明Socket对象
	private JTextArea ta = new JTextArea(); // 创建JtextArea对象
	private JTextField tf = new JTextField(); // 创建JtextField对象
	Container cc; // 声明Container对象
	private String name="陌生人";
	public String getname(){
		return name;
	}
	public MyClien(String title) { // 构造方法
		super(title); // 调用父类的构造方法
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		cc = this.getContentPane(); // 实例化对象

		final JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBorder(new BevelBorder(BevelBorder.RAISED));
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		scrollPane.setViewportView(ta);
		cc.add(tf, "South"); // 将文本框放在窗体的下部
		tf.addActionListener(new ActionListener() {
			// 绑定事件
			public void actionPerformed(ActionEvent e) {
				// 将文本框中信息写入流	
				writer.println(tf.getText());
				// 将文本框中信息显示在文本域中
				ta.append("你对服务器说："+tf.getText() + '\n');
				ta.setSelectionEnd(ta.getText().length());
				tf.setText(""); // 将文本框清空
			}
		});
	}
	
	private void connect(MyClien m) { // 连接套接字方法
		
		ta.append("尝试连接\n"); // 文本域中提示信息
		try { // 捕捉异常
			socket = new Socket("127.0.0.1", 8998); // 实例化Socket对象
			writer = new PrintWriter(socket.getOutputStream(), true);
			writer.println(m.getname());
			ta.append("完成连接\n"); // 文本域中提示信息
			while (true) { // 如果套接字是连接状态
				reader = new BufferedReader(new InputStreamReader(socket
						.getInputStream())); // 实例化BufferedReader对象
				getClientMessage(); // 调用getClientMessage()方法
			}
		} catch (Exception e) {
			e.printStackTrace(); // 输出异常信息
		}
	}
	private void getClientMessage() {
		try {
			writer = new PrintWriter(socket.getOutputStream(), true);
			while (true) { // 如果套接字是连接状态
				if (reader.ready()) {
					// 获得客户端信息
					ta.append("服务器:" + reader.readLine()+"\n");
				}
			}
		} catch (Exception e) {
			e.printStackTrace(); // 输出异常信息
		}
		try {
			if (reader != null) {
				reader.close(); // 关闭流
			}
			if (socket != null) {
				socket.close(); // 关闭套接字
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) { // 主方法
		MyClien clien = new MyClien("向服务器送数据"); // 创建本例对象
		clien.setSize(400, 400); // 设置窗体大小
		clien.setVisible(true); // 将窗体显示
		
		clien.connect(clien); // 调用连接方法
	}
}
