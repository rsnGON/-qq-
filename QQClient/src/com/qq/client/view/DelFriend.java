package com.qq.client.view;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;
import javax.swing.JButton;

import com.qq.client.model.ClientConServerThread;
import com.qq.client.model.QqClientUser;
import com.qq.client.tools.ManageQqChat;

public class DelFriend extends JFrame implements ActionListener {
	
	private String ownerId;
	private String friendId;
	private JPanel contentPane;
	JButton buttondel_sure,buttondel_cancel;

	public DelFriend(String ownerId,String friendId) {
		this.ownerId = ownerId;
		this.friendId = friendId;
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(600, 300, 348, 130);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		buttondel_sure = new JButton("ȷ��");
		buttondel_sure.addActionListener(this);
		buttondel_sure.setFont(new Font("����", Font.PLAIN, 14));
		buttondel_sure.setBounds(100, 23, 65, 25);
		contentPane.add(buttondel_sure);
		buttondel_cancel = new JButton("ȡ��");
		buttondel_cancel.addActionListener(this);
		buttondel_cancel.setFont(new Font("����", Font.PLAIN, 14));
		buttondel_cancel.setBounds(180, 23, 65, 25);
		contentPane.add(buttondel_cancel);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==buttondel_sure)
		{
			ClientConServerThread.SendDelFriendRequest(this.ownerId, this.friendId);
			System.out.println(this.friendId+"���ѱ�ɾ��");
			QqChat qqChat = ManageQqChat.getQqChat(this.ownerId+" "+this.friendId);
			if (qqChat!=null)
			{
				//��hashmapɾ��QqChat
				ManageQqChat.delQqChat(this.ownerId+" "+this.friendId);
				qqChat.dispose();
			}
		}
		if (e.getSource()==buttondel_cancel)
		{
			this.dispose();
		}
		
	}
	public void analysisDelFriendResult(int result)
	{
		//1:ɾ���ɹ�2:���Ǻ���
		if (result == 1)
		{
			//���º���
			ClientConServerThread.RequestFriend(this.ownerId);
			JOptionPane.showMessageDialog(this, "ɾ�����ѳɹ�");
			this.dispose();
		}
		else if (result == 2)
		{
			JOptionPane.showMessageDialog(this, "�Ѿ����Ǻ���");
			this.dispose();
		}
	}

}
