package com.qq.client.model;

import com.qq.common.User;

public class QqClientUser
{
	public int checkUser(User u)//0��¼ʧ��1��¼�ɹ�2�Ѿ���¼
	{
		return new QqClientConnServer().sendLoginInfoToServer(u);
	}
	public boolean registerUser(User u)
	{
		return new QqClientConnServer().SendRegisetrInfoToServer(u);
	}

}
