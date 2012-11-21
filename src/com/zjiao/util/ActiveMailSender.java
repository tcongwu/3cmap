package com.zjiao.util;

import javax.mail.*;
import javax.mail.internet.*;

import com.zjiao.auth.UserInfo;

import java.util.*;

/**
 * @author Lee Bin
 *
 * �������ɷ����û��ʺż����ʼ�
 */
public class ActiveMailSender {
	private static String smtpServer=null; //���������ʼ��ķ�������ַ
	private String from=null; //�����ʼ��ĵ�ַ
	private String to; //�����ʼ��ĵ�ַ
	private String sub; //�ʼ�������
	private String content; //�ʼ�������

	/**
	 * Ĭ�ϵĹ��캯��
	 */
	public ActiveMailSender() {
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
	
	public static boolean sendMail(UserInfo uinfo, String acode){
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy��MM��dd��");
		String mailHost = "mail.tsinghua.edu.cn";	//�����ʼ���������ַ
		String mailUser = "binli";			//�����ʼ����������û��ʺ�
		String mailPassword = "high681";	//�����ʼ����������û�����
		String[] toAddress = new String[1];
		toAddress[0]=uinfo.getEmail();
		//ʹ�ô��ı���ʽ�����ʼ�
		MailSender sendmail = MailSender.getHtmlMailSender(mailHost, mailUser,mailPassword);
		try {
			sendmail.setSubject("[email��֤]��л����3Cmap.comע��");
			sendmail.setSendDate(new Date());

			//�����ʼ�����
			String content = uinfo.getRealName() + "����ã�<br><br>"
				+ "       ��ӭ�����<a href=\"http://www.3cmap.com\">3Cmap.com</a>�������������Ӽ����ʺ����ע�᣺<br><br>"
				+ "           <a href=\"http://www.3cmap.com/activate.do?code=" + acode + "\" target=\"_blank\">http://www.3cmap.com/activate.do?code=" + acode + "</a><br><br>"
				+ "       ���������ַ�޷�������뽫�������������(����IE)�ĵ�ַ���С�<br>"
				+ "       ϣ������3Cmap�����齡����졣��л���3Cmap��֧�֡�<br><br>"
				+ "           <a href=\"http://www.3cmap.com\">http://www.3cmap.com</a><br><br>"
				+ "       (����һ���Զ�������email������ظ���)<br><br>"
				+ "                                       "+sdf.format(new Date());
			sendmail.setMailContent(content); //
			sendmail.setMailFrom("sys@3cmap.com","3Cmap");
			sendmail.setMailTo(toAddress, "to");

			//��ʼ�����ʼ�
			System.out.println("���ڷ����ʼ���"+uinfo.getRealName()+"�����Ժ�.......");
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