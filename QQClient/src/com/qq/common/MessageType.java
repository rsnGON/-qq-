/**
 * ������Ϣ������
 */
package com.qq.common;

public interface MessageType
{
	String message_request_login = "0";//�����¼��Ϣ
	String message_login_succeed = "1";//������½�ɹ�
	String message_login_fail = "2";//������¼ʧ��
	String message_comm_mes = "3";//��ͨ������Ϣ
	String message_get_onLineFriend = "4";//Ҫ�����ߺ��ѵ���Ϣ
	String message_ret_onLineFriend="5";//�������ߺ��ѵ���Ϣ
	String message_request_register="6";//����ע����Ϣ
	String message_register_succeed= "7";//����ע��ɹ�
	String message_register_fail = "8";//����ע��ʧ��
	String message_get_friendlist = "9";//Ҫ������б���Ϣ
	String message_ret_friendlist = "10";//���غ����б���Ϣ
	String message_request_offline = "11";//��������
	String message_offline_succeed = "12";//���߳ɹ�
	String message_ret_offlineFriend = "13";//�������ߺ�����Ϣ
	String message_have_logined = "14";//�����Ѿ���¼
	String message_request_addFriend = "15";//������Ӻ���
	String message_request_delFriend = "16";//����ɾ������
	String message_ret_addFriend_result = "17";//������Ӻ�����Ϣ�Ľ��
	String message_ret_delFriend_result = "18";//����ɾ��������Ϣ�Ľ��
	String message_request_offline_message = "19";//������ѷ��͵�������Ϣ
	String message_offline_message = "20";//������Ϣ
}
