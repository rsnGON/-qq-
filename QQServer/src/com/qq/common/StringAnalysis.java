package com.qq.common;

public class StringAnalysis
{
	/**
	 * �ж�ָ�����ַ����Ƿ�Ϊ���������ַ���
	 * @param userID	Ҫ�жϵ��ַ���
	 * @return	�Ƿ�Ϊ���������ַ���
	 */
	public static boolean isDigit(String userID)
	{
		userID=userID.trim();
		for(int i=0;i<userID.length();i++)
		{
			if(!Character.isDigit(userID.charAt(i)))
			{
				return false;
			}
		}
		return true;
	}
	 public static boolean isHave(String[] strs,String s)
	 {
		 //�˷�����������������һ����Ҫ���ҵ��ַ������飬�ڶ�����Ҫ���ҵ��ַ����ַ���
		 
		 for(int i=0;i<strs.length;i++)
		 {
			 if(strs[i].equals(s))
			 {//ѭ�������ַ��������е�ÿ���ַ������Ƿ�������в��ҵ�����
				 return true;//���ҵ��˾ͷ����棬���ڼ�����ѯ
			 }
		 }
		 	return false;//û�ҵ�����false
	 }
}
