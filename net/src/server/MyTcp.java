package server;
import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.JFrame;

public class MyTcp extends JFrame{ // ������MyTcp
	private BufferedReader reader; // ����BufferedReader����
	private ServerSocket server; // ����ServerSocket����
	private Socket socket; // ����Socket����socket
	private static final long serialVersionUID = 1L;
	private PrintWriter writer; // ����PrintWriter�����
	private JTextArea ta = new JTextArea(); // ����JtextArea����
	private JTextField tf = new JTextField(); // ����JtextField����
	Container cc; // ����Container����
	private String name;
	public void setname(String n){
		name=n;
	}
	public String getname(){
		return name;
	}
	public MyTcp(String title) { // ���췽��
		super(title); // ���ø���Ĺ��췽��
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		cc = this.getContentPane(); // ʵ��������

		final JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBorder(new BevelBorder(BevelBorder.RAISED));
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		scrollPane.setViewportView(ta);
		cc.add(tf, "South"); // ���ı�����ڴ�����²�
		tf.addActionListener(new ActionListener() {
			// ���¼�
			public void actionPerformed(ActionEvent e) {
				// ���ı�������Ϣд����
				writer.println(tf.getText());
				// ���ı�������Ϣ��ʾ���ı�����
				ta.append("���"+getname()+"˵��"+tf.getText() + '\n');
				ta.setSelectionEnd(ta.getText().length());
				tf.setText(""); // ���ı������
			}
		});
	}
	
	void getserver(MyTcp m) {
		
		try {
			server = new ServerSocket(8998); // ʵ����Socket����
			System.out.println("�������׽����Ѿ������ɹ�"); // �����Ϣ
			while (true) { // ����׽���������״̬
				ta.append("�ȴ��ͻ���������\n"); // �����Ϣ
				socket = server.accept(); // ʵ����Socket����
				reader = new BufferedReader(new InputStreamReader(socket
						.getInputStream())); // ʵ����BufferedReader����
				getClientMessage(m); // ����getClientMessage()����
			}
		} catch (Exception e) {
			e.printStackTrace(); // ����쳣��Ϣ
		}
	}
	
	private void getClientMessage(MyTcp m) {
		String kname = null;
		
		try {
			kname=reader.readLine();
			m.setname(kname);
			ta.append(kname+"���ӳɹ�\n");
			writer = new PrintWriter(socket.getOutputStream(), true);
			while (true) { // ����׽���������״̬
				if (reader.ready()) {
					// ��ÿͻ�����Ϣ
					
					ta.append(kname+"˵:" + reader.readLine()+"\n");
				}
			}
		} catch (Exception e) {
			e.printStackTrace(); // ����쳣��Ϣ
		}
		try {
			if (reader != null) {
				reader.close(); // �ر���
			}
			if (socket != null) {
				socket.close(); // �ر��׽���
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) { 
		MyTcp tcp = new MyTcp("������"); // �����������
		tcp.setSize(400,400);
		tcp.setVisible(true);
		tcp.getserver(tcp); // ���÷���
	}
}
