package com.qq.client.view;

import java.applet.AudioClip;
import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JApplet;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URL;

import javax.swing.JTextField;
import javax.swing.JButton;

import com.qq.client.model.ClientConServerThread;
import com.qq.client.model.QqClientUser;

public class AddFriend extends JFrame implements ActionListener {
	
	private String owner;
	private JPanel contentPane;
	private JTextField textFieldqqid;
	JLabel labelqqid;
	JButton buttonadd;
	
	public AddFriend(String ownerId) {
		this.owner = ownerId;
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(600, 300, 348, 130);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		labelqqid = new JLabel("QQ����:");
		labelqqid.setFont(new Font("����", Font.PLAIN, 14));
		labelqqid.setBounds(26, 34, 60, 20);
		contentPane.add(labelqqid);
		
		textFieldqqid = new JTextField();
		textFieldqqid.setFont(new Font("����", Font.PLAIN, 14));
		textFieldqqid.setBounds(90, 33, 150, 21);
		contentPane.add(textFieldqqid);
		textFieldqqid.setColumns(10);
		
		buttonadd = new JButton("���");
		buttonadd.addActionListener(this);
		buttonadd.setFont(new Font("����", Font.PLAIN, 14));
		buttonadd.setBounds(252, 30, 65, 25);
		contentPane.add(buttonadd);
		setVisible(true);
	}
	public static void main(String[] args) {
		//new AddFriend();
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource()==buttonadd)
		{
			
			String friendId = textFieldqqid.getText().trim();
			if (friendId.equals(""))
			{
				JOptionPane.showMessageDialog(this, "QQ���벻��Ϊ��");
				return;
			}
			if (friendId.equals(this.owner))
			{
				JOptionPane.showMessageDialog(this, "��������Լ�Ϊ����");
				return;
			}
			//������Ӻ�������
			ClientConServerThread.SendAddFriendRequest(this.owner, friendId);
					
			
		}	
	}
	public void analysisAddFriendResult(int result)
	{
		//1:��ӳɹ�2:�Ѿ��Ǻ���3:û�и��û�
		if (result == 1)
		{
			//���º���
			ClientConServerThread.RequestFriend(this.owner);
			JOptionPane.showMessageDialog(this, "��Ӻ��ѳɹ�");
			this.dispose();
		}
		else if (result == 2)
		{
			JOptionPane.showMessageDialog(this, "���û��Ѿ��������");
			this.dispose();
		}
		else if(result == 3)
		{
			JOptionPane.showMessageDialog(this, "û�и��û�");
			this.dispose();
		}
	}
	
	
}

