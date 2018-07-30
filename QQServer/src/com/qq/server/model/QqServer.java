/**
 * ����qq�����������ڼ������ȴ�ĳ��qq�ͻ���������
 */
package com.qq.server.model;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.ResultSet;

import javax.crypto.spec.PSource;

import com.qq.common.LoginRegisterMessage;
import com.qq.common.Message;
import com.qq.common.MessageType;
import com.qq.common.User;
import com.qq.server.db.SqlHelper;
import com.qq.server.tools.ManageClientThread;

public class QqServer
{
	public QqServer()
	{
		try
		{
			//��9999�˿ڼ���
			System.out.println("���Ƿ���������9999����");//���ڵ���
			ServerSocket ss=new ServerSocket(9999);
			while(true)
			{
				//�������ȴ�����
				Socket s = ss.accept(); 
				//���ܿͻ��˷�������Ϣ
	//			BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
	//			String info = br.readLine();
				ObjectInputStream oss = new ObjectInputStream(s.getInputStream());
				LoginRegisterMessage message = (LoginRegisterMessage)oss.readObject();
				if (message.getMesType().equals(MessageType.message_request_login))
				{
					User u = message.getUser();
					
					Message m = new Message();//�����û�����Ϣ
					ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
					//�ж��û��Ƿ��Ѿ���¼
					if ((ManageClientThread.getClientThread(u.getUserId()))!=null)
					{
						//����һ���Ѿ���¼����Ϣ��
						m.setMesType(MessageType.message_have_logined);
						oos.writeObject(m);
					}
					else
					{
						//�û������ڻ���δ��¼
						if (new QqServerUser().checkUser(u))//�������ݿ���֤�û��˺š�
						{
							
							System.out.println("���������յ��û�ID��"+u.getUserId()+"�û����룺"+u.getPassWd());//���ڵ���
							//����һ���ɹ���½����Ϣ��
							m.setMesType(MessageType.message_login_succeed);
							oos.writeObject(m);
							
							//��һ���̣߳�������ÿͻ���ͨ��
							SerConClientThread scct = new SerConClientThread(s);
							ManageClientThread.addClientThread(u.getUserId(), scct);
							
							scct.start();
							
							//֪ͨ���������û�
							scct.notifyother(u.getUserId(),1);
							
							
							
							
						}
						else
						{
							System.out.println("��֤������1111111111111");
							//����һ����¼ʧ�ܵ���Ϣ��
							m.setMesType(MessageType.message_login_fail);
							oos.writeObject(m);
							s.close();
						}
					}
					
				}
				else if (message.getMesType().equals(MessageType.message_request_register))
				{
					User u = message.getUser();
					Message m = new Message();//�����û�����Ϣ
					ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
					//ע���û����ɹ�����true
					if (new QqServerUser().registerUser(u))
					{
						//����һ���ɹ�ע�����Ϣ��
						m.setMesType(MessageType.message_register_succeed);
						oos.writeObject(m);
					}
				}
				
				
			
			}
			
			
		} catch (Exception e)
		{
			e.printStackTrace();
			// TODO: handle exception
		}finally
		{
			
		}
	}
}
