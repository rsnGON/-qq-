/*
 * �������˵Ŀ��ƽ��棬��������������������رշ�����
 * ���Թ���ͼ���û�
 */
package com.qq.server.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.accessibility.Accessible;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.qq.server.model.QqServer;

public class ServerFrame extends JFrame implements ActionListener
{

	JPanel jp1;
	JButton jb1,jb2;
	public static void main(String[] args)
	{
		// TODO Auto-generated method stub
		ServerFrame sf = new ServerFrame();
	}
	public ServerFrame()
	{
		jp1 = new JPanel();
		jb1 = new JButton("����������");
		jb1.addActionListener(this);
		jb2 = new JButton("�رշ�����");
		jp1.add(jb1);
		jp1.add(jb2);
		
		this.add(jp1);
		this.setSize(500,400);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	@Override
	public void actionPerformed(ActionEvent e)
	{
		// TODO Auto-generated method stub
		if (e.getSource() == jb1)
		{
			new QqServer();
		}
	}

}
