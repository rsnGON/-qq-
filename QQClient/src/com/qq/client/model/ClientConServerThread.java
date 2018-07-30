/**
 * ���ǿͻ��˺ͷ������˱���ͨѶ���߳�
 */

package com.qq.client.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.qq.client.tools.ManageAddFriend;
import com.qq.client.tools.ManageClientConServerThread;
import com.qq.client.tools.ManageDelFriend;
import com.qq.client.tools.ManageQqChat;
import com.qq.client.tools.ManageQqFriendList;
import com.qq.client.tools.ObjectFile;
import com.qq.client.view.AddFriend;
import com.qq.client.view.DelFriend;
import com.qq.client.view.QqChat;
import com.qq.client.view.QqFriendList;
import com.qq.common.Message;
import com.qq.common.MessageType;
import com.qq.common.User;

public class ClientConServerThread extends Thread
{
	private Socket s;
	public Socket getS()
	{
		return s;
	}
	public void setS(Socket s)
	{
		this.s = s;
	}
	public ClientConServerThread(Socket s)
	{
		this.s = s;
	}
	public void run()
	{
		//��ͣ�Ķ�ȡ�ӷ������˷�������Ϣ
		while(true)
		{
			try
			{
				ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
				Message m = (Message)ois.readObject();
				if (m.getMesType().equals(MessageType.message_comm_mes)
						|| m.getMesType().equals(MessageType.message_offline_message))
				{	
					
					System.out.println(m.getSender()+"��"+m.getGetter()+"˵"+m.getContext()+"\r\n");
					//�Ѵӷ�������õ���Ϣ����ʾ������ʾ���������
					QqChat qqChat = ManageQqChat.getQqChat(m.getGetter()+" "+m.getSender());
					
					QqFriendList qqFriendList = ManageQqFriendList.getQqFriendList(m.getGetter());
					
					if (m.getMesType().equals(MessageType.message_comm_mes))
					{
						qqFriendList.sound = true;		
					}
					if (qqFriendList.sound)
					{
						qqFriendList.HaveMessage(m.getSender());//friendlist֪ͨ�û�����Ϣ��
					}
					if (m.getMesType().equals(MessageType.message_offline_message))
					{
						qqFriendList.sound = false;
					}
						
				
					
					
					if (qqChat==null)
					{
						//���촰��û��
						
						//��Ϣ�浽�����ļ�
						new ObjectFile().WriteObjectToFile(m, m.getGetter(),m.getSender());
						
						////����
						QqFriendList qqFriendList1 = ManageQqFriendList.getQqFriendList("1");
						if (qqFriendList1==null)
						{
							//bug?
							System.out.println("0000000000000000000000");
						}
						////����
						
						
					}
					else
					{
						//��ʾ
						qqChat.showMessage(m);
					}
					
				}
				else if (m.getMesType().equals(MessageType.message_ret_onLineFriend))
				{
					System.out.println("�յ����ѵ�������Ϣ");
//					String con = m.getContext();
//					String friengs[] = con.split(" ");
					String getter = m.getGetter();
					System.out.println(getter+":"+m.getContext());
					//�޸���Ӧ�ĺ����б�
					QqFriendList  qqFriendList =  ManageQqFriendList.getQqFriendList(getter);
					//�������ߺ���
					System.out.println("���º�������");
					if (qqFriendList!=null)
					{
						qqFriendList.updateOnlineFriend(m);
						if (!m.getSender().equals("0"))//����ǡ�0�������Ƿ����������ͻ��˵ģ����ǿͻ�֮���ͨ��
						{								//������ǡ�0��������ĳ���ͻ��˺�����ʾ��˵��������
							qqFriendList.haveFriendOnline();
						}
						
					}
				}else if(m.getMesType().equals(MessageType.message_ret_offlineFriend))
				{
					String getter = m.getGetter();
					QqFriendList  qqFriendList =  ManageQqFriendList.getQqFriendList(getter);
					//�������ߺ���
					System.out.println("�������ߺ���"+m.getContext());
					if (qqFriendList!=null)
					{
						qqFriendList.updateOfflineFriend(m);
					}
				}else if (m.getMesType().equals(MessageType.message_ret_friendlist))
				{
					System.out.println("�յ������б�");
					System.out.println(m.getContext());//�����б�
					String getter = m.getGetter();
					//�޸���Ӧ�ĺ����б�
					QqFriendList  qqFriendList =  ManageQqFriendList.getQqFriendList(getter);
					//�������ߺ���
					System.out.println("���º����б�");
					if (qqFriendList!=null)
					{
						System.out.println("���ڸ���");
						qqFriendList.updateFriendList(m);
					}
//					//����һ��Ҫ�󷵻����ߺ��ѵ������
//					ObjectOutputStream oos = new ObjectOutputStream
//					(ManageClientConServerThread.getClientConServerThread(m.getGetter()).getS().getOutputStream());
//				
//					Message message = new Message();
//					message.setMesType(MessageType.message_get_onLineFriend);
//					message.setSender(m.getGetter());
//					oos.writeObject(message);
				}
				else if (m.getMesType().equals(MessageType.message_offline_succeed))
				{
					ManageClientConServerThread.delClientConServerthread(m.getGetter());
					ManageQqFriendList.delQqFriendList(m.getGetter());
					s.close();
					break;
				}else if (m.getMesType().equals(MessageType.message_ret_addFriend_result))
				{
					//������Ӻ������󷵻ؽ��
					int result = Integer.parseInt(m.getContext());//1:��ӳɹ�2:�Ѿ��Ǻ���3:û�и��û�
					AddFriend addFriend = ManageAddFriend.getAddFriend(m.getGetter());
					addFriend.analysisAddFriendResult(result);	
				}else if(m.getMesType().equals(MessageType.message_ret_delFriend_result))
				{
					//����ɾ���������󷵻ؽ��
					int result = Integer.parseInt(m.getContext());//1:ɾ���ɹ�2:���Ǻ���
				
					DelFriend delFriend = ManageDelFriend.getDelFriend(m.getGetter());
					delFriend.analysisDelFriendResult(result);	
				}
				
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	//������������
	public static void SendChatMessage(Message message)
	{
		//���͸�������
		try
		{
			ObjectOutputStream oos = new ObjectOutputStream
			(ManageClientConServerThread.getClientConServerThread(message.getSender()).getS().getOutputStream());
			oos.writeObject(message);
		
		} catch (Exception e2)
		{
			e2.printStackTrace();
		}
	}
	//��������
	public static void RequestOffline(String uid)
	{
		try
		{
			ObjectOutputStream oos = new ObjectOutputStream
			(ManageClientConServerThread.getClientConServerThread(uid).getS().getOutputStream());
			Message message = new Message();
			message.setMesType(MessageType.message_request_offline);
			message.setSender(uid);
			oos.writeObject(message);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	public static void RequestFriend(String uid)
	{
	////����һ��Ҫ�󷵻غ����б�������
		try
		{
			ObjectOutputStream oos = new ObjectOutputStream
			(ManageClientConServerThread.getClientConServerThread(uid).getS().getOutputStream());
			Message message = new Message();
			message.setMesType(MessageType.message_get_friendlist);
			message.setSender(uid);
			oos.writeObject(message);
		} catch (IOException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//����һ��Ҫ�󷵻����ߺ��ѵ������
		try
		{
			ObjectOutputStream oos = new ObjectOutputStream
			(ManageClientConServerThread.getClientConServerThread(uid).getS().getOutputStream());
			Message message = new Message();
			message.setMesType(MessageType.message_get_onLineFriend);
			message.setSender(uid);
		
			oos.writeObject(message);
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//�������ߺ���������Ϣ
	public static void RequestOfflineMessage(String uid)
	{
		try
		{
			ObjectOutputStream oos = new ObjectOutputStream
					(ManageClientConServerThread.getClientConServerThread(uid).getS().getOutputStream());
			Message message = new Message();
			message.setSender(uid);
			message.setMesType(MessageType.message_request_offline_message);
			oos.writeObject(message);
		} catch (Exception e)
		{
			e.printStackTrace();
			// TODO: handle exception
		}	
	}
	//������Ӻ�������
	public static void SendAddFriendRequest(String uid,String friendId)
	{
		try
		{
			ObjectOutputStream oos = new ObjectOutputStream
					(ManageClientConServerThread.getClientConServerThread(uid).getS().getOutputStream());
			Message message = new Message();
			message.setContext(friendId);
			message.setSender(uid);
			message.setMesType(MessageType.message_request_addFriend);
			oos.writeObject(message);
		} catch (Exception e)
		{
			e.printStackTrace();
			// TODO: handle exception
		}	
	}
	//����ɾ����������
	public static void SendDelFriendRequest(String uid,String friendId)
	{
		try
		{
			ObjectOutputStream oos = new ObjectOutputStream
					(ManageClientConServerThread.getClientConServerThread(uid).getS().getOutputStream());
			Message message = new Message();
			message.setContext(friendId);
			message.setSender(uid);
			message.setMesType(MessageType.message_request_delFriend);
			oos.writeObject(message);
		} catch (Exception e)
		{
			e.printStackTrace();
			// TODO: handle exception
		}	
	}
}
