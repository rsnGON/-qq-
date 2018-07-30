/**
 * ���ܣ��Ƿ�������ĳ���ͻ��˵�ͨ���߳�
 */
package com.qq.server.model;

import java.awt.List;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.qq.common.Message;
import com.qq.common.MessageType;
import com.qq.common.StringAnalysis;
import com.qq.common.User;
import com.qq.server.tools.ManageClientThread;

public class SerConClientThread extends Thread
{
	Socket socket;
	public SerConClientThread(Socket s)
	{
		//�ѷ������͸ÿͻ��˵����Ӹ���this.socket
		this.socket = s;
	
	}
	
	//֪ͨ�����û������û��Լ����߻�������
	public void notifyother(String iAm,int type)//type=1,���ߡ�type=0,���ߡ�
	{
		HashMap hm = ManageClientThread.hm;
		Iterator it = hm.keySet().iterator();
		Message message = new Message();
		message.setContext(iAm);
		message.setSender(iAm);//ָ����ĳ���û����ģ�����ǡ�0�������Ƿ����������ͻ��˵ģ����ǿͻ�֮���ͨ��
		if (type==1)
		{
			message.setMesType(MessageType.message_ret_onLineFriend);
		}
		else
		{
			message.setMesType(MessageType.message_ret_offlineFriend);
		}
		//��ȡ�����б�
		String friendList[] = QqServerUser.getFriendList(iAm).split(" ");
		
		while (it.hasNext())
		{	
			String onLineUserId = it.next().toString();
			if (StringAnalysis.isHave(friendList,onLineUserId))
			{
				try
				{
					//������Ϣ
					if (type==1)
					{
						System.out.println("֪ͨ"+onLineUserId+":"+iAm+"������");
					}
					else {
						System.out.println("֪ͨ"+onLineUserId+":"+iAm+"������");
					}
					
					ObjectOutputStream oss = new ObjectOutputStream
					(ManageClientThread.getClientThread(onLineUserId).socket.getOutputStream());
					message.setGetter(onLineUserId);
					oss.writeObject(message);				
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}	
		}
	
	}
	//��������Ϣ�����û�
	public void  sendOfflineMessageToUser(String userId)
	{
		ArrayList<Message> list = QqServerUser.getUserOfflineMessage(userId);//����û���������Ϣ
		QqServerUser.delUserOfflineMessage(userId);//ɾ�����ݿ�������Ϣ
		try
		{
			ObjectOutputStream oss;
			System.out.println("������Ϣ����Ϊ��"+list.size());
			for (int i = 0; i < list.size(); i++)
			{
				oss = new ObjectOutputStream
						(ManageClientThread.getClientThread(userId).socket.getOutputStream()); 
				oss.writeObject((Message)list.get(i));
			}
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	//���ͻ��˷������ߺ�����Ϣ
	public void  sendOnlineFriend(Message message)
	{
		System.out.println(message.getSender()+"Ҫ�������ߺ���");
		//�����ߺ��ѷ��ظ��ÿͻ���
		String res = ManageClientThread.getAllOnLineUserId(message.getSender());
		Message m = new Message();
		m.setMesType(MessageType.message_ret_onLineFriend);
		m.setContext(res);
		m.setGetter(message.getSender());
		m.setSender("0");//��ʾ���������͸��û��ġ�
		try
		{
			SerConClientThread scct = ManageClientThread.getClientThread(message.getSender());
			if (scct == null)
			{
				//�����߾Ͳ��������ߺ���
				return;
			}
			ObjectOutputStream oos = new ObjectOutputStream(scct.socket.getOutputStream());
			oos.writeObject(m);
		} catch (Exception e)
		{
			e.printStackTrace();
			// TODO: handle exception
		}
		
	}
	//���ͻ��˷��ͺ����б�
	public void sendFriendList(Message message)
	{
		System.out.println(message.getSender()+"Ҫ���ĺ����б�");
		//�Ѻ����б��ظ��ÿͻ���Friendlist
		String res = QqServerUser.getFriendList(message.getSender());
		System.out.println(res);//��ӡ�����б�
		Message m = new Message();
		m.setMesType(MessageType.message_ret_friendlist);
		m.setContext(res);
		m.setGetter(message.getSender());
		try
		{
			SerConClientThread scct = ManageClientThread.getClientThread(message.getSender());
			if (scct == null)
			{
				//�����߾Ͳ����ͺ����б�
				return;
			}
			ObjectOutputStream oos = new ObjectOutputStream(scct.socket.getOutputStream());
			oos.writeObject(m);
		} catch (Exception e)
		{
			e.printStackTrace();
			// TODO: handle exception
		}
		
	}
	public void run()
	{
		while (true)
		{
			//������߳̾Ϳ��Խ��ܿͻ��˵���Ϣ
			try
			{
				ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
				Message message = (Message)ois.readObject();
				if (message.getMesType().equals(MessageType.message_comm_mes))
				{
					System.out.println(message.getSender()+"��"+message.getGetter()+"��:"+message.getContext());
					
					
					SerConClientThread scct = ManageClientThread.getClientThread(message.getGetter());
					if (scct==null)
					{
						//�����ߡ��Ȱ����ݴ洢���ݿ⡣
						message.setMesType(MessageType.message_offline_message);//����Ϣ���������Ϣ
						QqServerUser.StoreMessageToDatabase(message);
					}
					else 
					{
						//ת����Ϣ
						ObjectOutputStream oos = new ObjectOutputStream(scct.socket.getOutputStream());
						oos.writeObject(message);
						//System.out.println("ת���ɹ�");
					}
					
				}else if (message.getMesType().equals(MessageType.message_get_onLineFriend))
				{
					sendOnlineFriend(message);
					
				}else if(message.getMesType().equals(MessageType.message_get_friendlist))
				{
					
					sendFriendList(message);
					
				}
				else if (message.getMesType().equals(MessageType.message_request_offline))
				{
					//֪ͨ�������ߺ�����������
					notifyother(message.getSender(), 0);
					System.out.println(message.getSender()+"���߳ɹ�");
					Message m = new Message();
					m.setMesType(MessageType.message_offline_succeed);
					m.setGetter(message.getSender());
					ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
					oos.writeObject(m);
					ManageClientThread.delClientThread(message.getSender());
					//��ֹ�߳�
					break;
				}else if (message.getMesType().equals(MessageType.message_request_addFriend))
				{
					//Ϊ���û���Ӻ���(ǿ����ӣ��Ժ���ԸĽ�)
					int result;//1:��ӳɹ�2:�Ѿ��Ǻ���3:û�и��û�
					result = new QqServerUser().addFriend(message.getSender(),message.getContext());
					if (result == 1)
					{
						//֪ͨ�����û�
						Message tofriendMess = new Message();
						tofriendMess.setSender(message.getContext());//���챻���û�����������º����б�
						sendFriendList(tofriendMess);
						sendOnlineFriend(tofriendMess);
					}
					Message m = new Message();
					m.setMesType(MessageType.message_ret_addFriend_result);
					m.setGetter(message.getSender());
					m.setContext(Integer.toString(result));
					ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
					oos.writeObject(m);	
					
				}else if(message.getMesType().equals(MessageType.message_request_delFriend))
				{
					//Ϊ���û�ɾ������(ǿ��ɾ�����Ժ���ԸĽ�)
					int result;//1:ɾ���ɹ�2:���Ǻ���
					result = new QqServerUser().delFriend(message.getSender(),message.getContext());
					if (result == 1)
					{
						//֪ͨ��ɾ�û�
						System.out.println(message.getContext()+"��ɾ��");
						Message tofriendMess = new Message();
						tofriendMess.setSender(message.getContext());//���챻���û�����������º����б�
						sendFriendList(tofriendMess);
						sendOnlineFriend(tofriendMess);
					}
					Message m = new Message();
					m.setMesType(MessageType.message_ret_delFriend_result);
					m.setGetter(message.getSender());
					m.setContext(Integer.toString(result));
					ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
					oos.writeObject(m);	
				}else if(message.getMesType().equals(MessageType.message_request_offline_message))
				{
					//����������Ϣ���û�
					sendOfflineMessageToUser(message.getSender());
				}
				
			
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
}



