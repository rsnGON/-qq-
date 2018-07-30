package com.qq.client.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;
import javax.swing.JButton;

import com.qq.client.model.ClientConServerThread;
import com.qq.client.tools.ManageQqChat;
import com.qq.client.tools.ObjectFile;
import com.qq.common.Message;
import com.qq.common.MessageType;

public class QqChat extends JFrame implements ActionListener {

	String owerId;
	String friendId;
	
	private boolean isMoved;
	private Point pre_point;
	private Point end_point;
	
	private JPanel contentPane;

	JPanel panelnorth;
	JPanel panelcenter;
	private JPanel paneleast;
	
	JLabel labelnorth;
	JLabel labeleast;
	private JPanel panelc1;
	private JPanel panelc2;
	private JPanel panelc3;
	
	private JTextArea textAreachattingrecords;
	JScrollPane scrollPane1;
	JScrollPane scrollPane2;
	private JTextArea textAreasendmessage;
	private JButton buttonclose;
	private JButton buttonsend;
	private JButton buttonmin;
	private JButton buttonexit;
	private JLabel labelqqid;
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		QqChat frame = new QqChat("1","2");
	}

	/**
	 * Create the frame.
	 */
	public QqChat(String ownerId,String friendId) {
		this.owerId = ownerId;
		this.friendId = friendId;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(300, 100, 632, 550);
		setUndecorated(true);
		setDragable(this);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
//		setContentPane(contentPane);
		
		panelnorth = new JPanel();
		panelnorth.setLayout(null);
		panelnorth.setPreferredSize(new Dimension(632, 81));
		
//		labelnorth=new JLabel(new ImageIcon("image/chat/north.jpg"));
		labelnorth=new JLabel();
//		labelnorth.setPreferredSize(new Dimension(632, 81));
		labelnorth.setBounds(0, 0, 632, 81);
		panelnorth.add(labelnorth);
		
		contentPane.add(panelnorth, BorderLayout.NORTH);
		
		buttonmin = new JButton(new ImageIcon("image/chat/min2.jpg"));
		buttonmin.addActionListener(this);
		buttonmin.setBounds(550, 0, 25, 25);
		panelnorth.add(buttonmin);
		
		buttonexit = new JButton(new ImageIcon("image/chat/exit2.jpg"));
		buttonexit.addActionListener(this);
		buttonexit.setBounds(580, 0, 25, 25);
		panelnorth.add(buttonexit);
		
		ImageIcon image1=new ImageIcon("image/chat/qq.jpg");
		image1.setImage(image1.getImage().getScaledInstance(50,50,Image.SCALE_DEFAULT));
		JLabel labelqqportrait = new JLabel(image1);
		labelqqportrait.setBounds(1, 1, 50, 50);
		panelnorth.add(labelqqportrait);
		panelnorth.setBackground(new  Color(243,236,243));
		
		labelqqid = new JLabel(friendId);
		labelqqid.setBounds(69, 10, 54, 15);
		panelnorth.add(labelqqid);
		
		
		panelcenter = new JPanel();
		panelcenter.setBounds(0, 81, 450, 428);
		
		panelcenter.setLayout(new BorderLayout(0, 0));
		
		panelc1 = new JPanel();
		
		panelc1.setLayout(new BorderLayout(0, 0));
		
		textAreachattingrecords = new JTextArea();
		textAreachattingrecords.setEditable(false);
		
		scrollPane1=new JScrollPane(textAreachattingrecords);
		scrollPane1.setPreferredSize(new Dimension(450, 300));
		panelc1.add(scrollPane1, BorderLayout.NORTH);
		panelcenter.add(panelc1, BorderLayout.NORTH);
		
		panelc2 = new JPanel();		
		panelc2.setLayout(new BorderLayout(0, 0));	
		textAreasendmessage = new JTextArea();
		scrollPane2=new JScrollPane(textAreasendmessage);
		scrollPane2.setPreferredSize(new Dimension(450, 190));
		panelc2.add(scrollPane2, BorderLayout.NORTH);
		panelcenter.add(panelc2, BorderLayout.CENTER);
		
		contentPane.add(panelcenter, BorderLayout.CENTER);
		
		panelc3 = new JPanel();
		panelc3.setPreferredSize(new Dimension(450, 40));
		panelcenter.add(panelc3, BorderLayout.SOUTH);
		panelc3.setLayout(null);
		
		buttonclose = new JButton(new ImageIcon("image/chat/close.jpg"));
		buttonclose.addActionListener(this);
		buttonclose.setBounds(258, 9, 69, 24);
		panelc3.add(buttonclose);
		
		buttonsend = new JButton(new ImageIcon("image/chat/send.jpg"));
		buttonsend.addActionListener(this);
		buttonsend.setBounds(353, 9, 86, 23);
		panelc3.add(buttonsend);
		
		paneleast = new JPanel();
		paneleast.setBounds(450, 81, 136, 428);
		
		labeleast=new JLabel(new ImageIcon("image/chat/east.jpg"));
		paneleast.add(labeleast);
		
		contentPane.add(paneleast, BorderLayout.EAST);
		
		getContentPane().add(contentPane);
		String path2="image/chat/qq.jpg";
		Image image2=getToolkit().getImage(path2);
		setIconImage(image2);
		setVisible(true);
		
		//�ӱ����ļ���ȡδ��ʾ����Ϣ
		ReadMessageFromFile(ownerId,friendId);
	}
	//��ȡdat�ļ������δ��ʾ����Ϣ
		public void ReadMessageFromFile(String userid,String friendid)
		{
			ObjectFile objectFile = new ObjectFile();
			ArrayList<Object> list = objectFile.ReadAllObjectFromFile(userid,friendid);
			//ɾ���ļ�,��Ϊ�´β���Ҫ�ˡ�
		    objectFile.deleteObjectFile(userid,friendid);
			
			for (int i = 0; i < list.size(); i++)
			{
				showMessage((Message)list.get(i));
			}	
		}
		//��ʾ��Ϣ
		public void showMessage(Message message)
		{
			
			//this.jta.setForeground(new Color(0,255,0));
			String info;
			info = message.getSendTime();
			this.textAreachattingrecords.append(message.getSender()+" "+info+"\r\n");//��ʾʱ��
			//info = message.getSender()+"��"+message.getGetter()+"˵"+message.getContext()+"\r\n";
			info = message.getContext()+"\r\n";
			this.textAreachattingrecords.append(info);//��ʾ����
			
			this.textAreachattingrecords.setCaretPosition(textAreachattingrecords.getText().length());//����Ϣ������ʾ�����һ����Ϣ
		}
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if (e.getSource()==buttonclose)
		{
			System.out.println("chat,close");
			
			//��hashmapɾ��QqChat
			ManageQqChat.delQqChat(this.owerId+" "+this.friendId);
			dispose();
		}else if (e.getSource()==buttonsend) 
		{
			Message message = new Message();
			String time = new Date().toString();
			String context = textAreasendmessage.getText();
			//���÷�����Ϣ������
			message.setSender(this.owerId);
			message.setGetter(this.friendId);
			message.setMesType(MessageType.message_comm_mes);
			message.setContext(context);
			message.setSendTime(time);
			
			
			String info;
			info = this.owerId+" "+time;
			//this.jta.setForeground(new Color(255,0,0));
			this.textAreachattingrecords.append(info+"\r\n");//��ʾʱ��
			//info = message.getSender()+"��"+message.getGetter()+"˵"+message.getContext()+"\r\n";
			this.textAreachattingrecords.append(context+"\r\n");//��ʾ����
			
			this.textAreachattingrecords.setCaretPosition(textAreachattingrecords.getText().length());//����Ϣ������ʾ�����һ����Ϣ
			
			textAreasendmessage.setText("");//�������ִ��ڵ���������
			
			ClientConServerThread.SendChatMessage(message);	
//			if (!textAreasendmessage.getText().equals(""))
//			{
//				System.out.println("send message");
//				textAreachattingrecords.append(textAreasendmessage.getText()+"\n");
//				textAreasendmessage.setText("");
//			}
			
		}else if (e.getSource()==buttonmin)
		{
			setExtendedState(JFrame.ICONIFIED);
		}else if (e.getSource()==buttonexit) 
		{
			System.out.println("chat,close");
			
			//��hashmapɾ��QqChat
			ManageQqChat.delQqChat(this.owerId+" "+this.friendId);
			dispose();
		}
		
	}
	
	
	private void setDragable(final QqChat qqChat) {
		this.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseReleased(java.awt.event.MouseEvent e) {
				isMoved = false;// ����ͷ����Ժ��ǲ�������ק����
				qqChat.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}

			public void mousePressed(java.awt.event.MouseEvent e) {
				isMoved = true;
				pre_point = new Point(e.getX(), e.getY());// �õ�����ȥ��λ��
				qqChat.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
		});
		//�϶�ʱ��ǰ�������ȥ��갴��ȥʱ�����꣬���ǽ�����Ҫ�ƶ���������
		this.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
			public void mouseDragged(java.awt.event.MouseEvent e) {
				if (isMoved) {// �ж��Ƿ������ק
					end_point = new Point(qqChat.getLocation().x + e.getX() - pre_point.x,
							qqChat.getLocation().y + e.getY() - pre_point.y);
					qqChat.setLocation(end_point);
				}
			}
		});
	}
}
