package com.zjiao.util;

import javax.mail.*;
import javax.mail.internet.*;

import com.zjiao.Logger;
import com.zjiao.auth.UserInfo;

import java.util.*;

/**
 * @author Lee Bin
 *
 * �������ɷ���ȡ��������ʼ�
 */
public class PassMailSender {
	private static String smtpServer=null; //���������ʼ��ķ�������ַ
	private String from=null; //�����ʼ��ĵ�ַ
	private String to; //�����ʼ��ĵ�ַ
	private String sub; //�ʼ�������
	private String content; //�ʼ�������

	/**
	 * Ĭ�ϵĹ��캯��
	 */
	public PassMailSender() {
		if(smtpServer==null)
		  //smtpServer="smtp.21cn.com";
		  smtpServer="127.0.0.1";
	}

	/**
	 * ���÷����ʼ��ķ�����
	 * @param servname ��������������IP��ַ
	 */
	public void setSmtpServer(String servname){
	  smtpServer=servname;
	}

	/**
	 * ���÷����ʼ��ĵ�ַ
	 * @param sadrs �ʼ���ַ
	 */
	public void setSendAdrs(String sadrs) {
	  from = sadrs;
	}

	/**
	 * ���ý����ʼ��ĵ�ַ
	 * @param aadrs �ʼ���ַ
	 */
	public void setAcptAdrs(String aadrs) {
	  to = aadrs;
	}

	/**
	 * �����ʼ�������
	 * @param subject �ʼ�����
	 */
	public void setSub(String subject) {
	  sub = subject;
	}

	/**
	 * �����ʼ��ķ�������
	 * @param cont �ʼ�����
	 */
	public void setContent(String cont) {
	  content = cont;
	}

	/**
	 * ���ݲ����ͳ��û���Ҫ��ȡ������
	 * @param uinfo �û���Ϣ����
	 * @return boolean �����ʼ��Ƿ�ɹ�
	 */
	public boolean sendPassMail(UserInfo uinfo) {
		if(uinfo==null)
			return false;
		
	  java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy��MM��dd��");

	  try {

			/* �����ʼ��������ı� */
			//�����ʼ����յ�ַ
			to=uinfo.getEmail();
			if(!StringUtils.isEmail(to))
			  return false;

			//�����ʼ�����
			content = "�𾴵�" + uinfo.getRealName() + "��\n\n"
				+ "    �����û������ǣ�" + uinfo.getPassword() + "\n\n"
				+ "                                           ������\n"
				+ "                                                   "
				+ sdf.format(new Date());
	  }
	  catch (Exception ex) {
			Logger.Log(Logger.LOG_ERROR, ex);
			return false;
	  }

	  try{
			//���÷����ʼ���ַ
			from="webmaster@zjiao.com";
			//�����ʼ��ı���
			sub="���ԡ�����������ȡ�������ʼ�";
			//�����ʼ�
			send();

	  }catch (Exception ex) {
			Logger.Log(Logger.LOG_ERROR, ex);
			return false;
	  }

	  return true;
	}
	
	public static boolean sendMail(UserInfo uinfo){
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy��MM��dd��");
		String mailHost = "mail.tsinghua.edu.cn";	//�����ʼ���������ַ
		String mailUser = "binli";			//�����ʼ����������û��ʺ�
		String mailPassword = "high681";	//�����ʼ����������û�����
		String[] toAddress = new String[1];
		toAddress[0]=uinfo.getEmail();
		//ʹ�ô��ı���ʽ�����ʼ�
		MailSender sendmail = MailSender.getTextMailSender(mailHost, mailUser,mailPassword);
		try {
			sendmail.setSubject("���ԡ�����������ȡ�������ʼ�");
			sendmail.setSendDate(new Date());

			//�����ʼ�����
			String content = "�𾴵�" + uinfo.getPassword() + "�û���\n\n"
				+ "    �����û������ǣ�" + uinfo.getPassword() + "\n\n"
				+ "                                           ������\n"
				+ "                                                   "
				+ sdf.format(new Date());
			sendmail.setMailContent(content); //
			sendmail.setMailFrom("binli@tsinghua.edu.cn","����Ա");
			sendmail.setMailTo(toAddress, "to");

			//��ʼ�����ʼ�
			System.out.println("���ڷ����ʼ������Ժ�.......");
			sendmail.sendMail();
			System.out.println("��ϲ�㣬�ʼ��Ѿ��ɹ�����!");
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
		
		return true;
	}

	/**
	 * �����ʼ�
	 * @throws Exception �׳��ʼ����͹����г��ֵ��쳣
	 */
	public void send() throws Exception{

		Properties props = System.getProperties();
		props.put("mail.smtp.host", smtpServer);
		props.put("mail.smtp.auth", "true"); //����smtp��֤���ܹؼ���һ��
		Session sendMailSession;
		Store store;
		sendMailSession = Session.getInstance(props, null);
		Message newMessage = new MimeMessage(sendMailSession);
		newMessage.setFrom(new InternetAddress(from));
		newMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
		newMessage.setSubject(sub);
		newMessage.setSentDate(new java.util.Date());
		newMessage.setText(content);
		Transport.send(newMessage);

	}

}
