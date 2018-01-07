package server;
import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.JFrame;

public class MyTcp extends JFrame{ // 创建类MyTcp
	private BufferedReader reader; // 创建BufferedReader对象
	private ServerSocket server; // 创建ServerSocket对象
	private Socket socket; // 创建Socket对象socket
	private static final long serialVersionUID = 1L;
	private PrintWriter writer; // 声明PrintWriter类对象
	private JTextArea ta = new JTextArea(); // 创建JtextArea对象
	private JTextField tf = new JTextField(); // 创建JtextField对象
	Container cc; // 声明Container对象
	private String name;
	public void setname(String n){
		name=n;
	}
	public String getname(){
		return name;
	}
	public MyTcp(String title) { // 构造方法
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
				ta.append("你对"+getname()+"说："+tf.getText() + '\n');
				ta.setSelectionEnd(ta.getText().length());
				tf.setText(""); // 将文本框清空
			}
		});
	}
	
	void getserver(MyTcp m) {
		
		try {
			server = new ServerSocket(8998); // 实例化Socket对象
			System.out.println("服务器套接字已经创建成功"); // 输出信息
			while (true) { // 如果套接字是连接状态
				ta.append("等待客户机的连接\n"); // 输出信息
				socket = server.accept(); // 实例化Socket对象
				reader = new BufferedReader(new InputStreamReader(socket
						.getInputStream())); // 实例化BufferedReader对象
				getClientMessage(m); // 调用getClientMessage()方法
			}
		} catch (Exception e) {
			e.printStackTrace(); // 输出异常信息
		}
	}
	
	private void getClientMessage(MyTcp m) {
		String kname = null;
		
		try {
			kname=reader.readLine();
			m.setname(kname);
			ta.append(kname+"连接成功\n");
			writer = new PrintWriter(socket.getOutputStream(), true);
			while (true) { // 如果套接字是连接状态
				if (reader.ready()) {
					// 获得客户端信息
					
					ta.append(kname+"说:" + reader.readLine()+"\n");
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
	
	public static void main(String[] args) { 
		MyTcp tcp = new MyTcp("服务器"); // 创建本类对象
		tcp.setSize(400,400);
		tcp.setVisible(true);
		tcp.getserver(tcp); // 调用方法
	}
}
