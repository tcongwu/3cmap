package com.zjiao.util;

import javax.mail.*;
import javax.mail.internet.*;

import com.zjiao.Logger;
import com.zjiao.auth.UserInfo;

import java.util.*;

/**
 * @author Lee Bin
 *
 * 用于生成发送取回密码的邮件
 */
public class PassMailSender {
	private static String smtpServer=null; //用来发送邮件的服务器地址
	private String from=null; //发送邮件的地址
	private String to; //接收邮件的地址
	private String sub; //邮件的主题
	private String content; //邮件的内容

	/**
	 * 默认的构造函数
	 */
	public PassMailSender() {
		if(smtpServer==null)
		  //smtpServer="smtp.21cn.com";
		  smtpServer="127.0.0.1";
	}

	/**
	 * 设置发送邮件的服务器
	 * @param servname 服务器的域名或IP地址
	 */
	public void setSmtpServer(String servname){
	  smtpServer=servname;
	}

	/**
	 * 设置发送邮件的地址
	 * @param sadrs 邮件地址
	 */
	public void setSendAdrs(String sadrs) {
	  from = sadrs;
	}

	/**
	 * 设置接收邮件的地址
	 * @param aadrs 邮件地址
	 */
	public void setAcptAdrs(String aadrs) {
	  to = aadrs;
	}

	/**
	 * 设置邮件的主题
	 * @param subject 邮件主题
	 */
	public void setSub(String subject) {
	  sub = subject;
	}

	/**
	 * 设置邮件的发送内容
	 * @param cont 邮件内容
	 */
	public void setContent(String cont) {
	  content = cont;
	}

	/**
	 * 根据参数送出用户想要获取的密码
	 * @param uinfo 用户信息对象
	 * @return boolean 发送邮件是否成功
	 */
	public boolean sendPassMail(UserInfo uinfo) {
		if(uinfo==null)
			return false;
		
	  java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy年MM月dd日");

	  try {

			/* 生成邮件的内容文本 */
			//设置邮件接收地址
			to=uinfo.getEmail();
			if(!StringUtils.isEmail(to))
			  return false;

			//生成邮件内容
			content = "尊敬的" + uinfo.getRealName() + "：\n\n"
				+ "    您的用户密码是：" + uinfo.getPassword() + "\n\n"
				+ "                                           助教网\n"
				+ "                                                   "
				+ sdf.format(new Date());
	  }
	  catch (Exception ex) {
			Logger.Log(Logger.LOG_ERROR, ex);
			return false;
	  }

	  try{
			//设置发送邮件地址
			from="webmaster@zjiao.com";
			//设置邮件的标题
			sub="来自“助教网”的取回密码邮件";
			//发送邮件
			send();

	  }catch (Exception ex) {
			Logger.Log(Logger.LOG_ERROR, ex);
			return false;
	  }

	  return true;
	}
	
	public static boolean sendMail(UserInfo uinfo){
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy年MM月dd日");
		String mailHost = "mail.tsinghua.edu.cn";	//发送邮件服务器地址
		String mailUser = "binli";			//发送邮件服务器的用户帐号
		String mailPassword = "high681";	//发送邮件服务器的用户密码
		String[] toAddress = new String[1];
		toAddress[0]=uinfo.getEmail();
		//使用纯文本格式发送邮件
		MailSender sendmail = MailSender.getTextMailSender(mailHost, mailUser,mailPassword);
		try {
			sendmail.setSubject("来自“助教网”的取回密码邮件");
			sendmail.setSendDate(new Date());

			//生成邮件内容
			String content = "尊敬的" + uinfo.getPassword() + "用户：\n\n"
				+ "    您的用户密码是：" + uinfo.getPassword() + "\n\n"
				+ "                                           助教网\n"
				+ "                                                   "
				+ sdf.format(new Date());
			sendmail.setMailContent(content); //
			sendmail.setMailFrom("binli@tsinghua.edu.cn","管理员");
			sendmail.setMailTo(toAddress, "to");

			//开始发送邮件
			System.out.println("正在发送邮件，请稍候.......");
			sendmail.sendMail();
			System.out.println("恭喜你，邮件已经成功发送!");
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
		
		return true;
	}

	/**
	 * 发送邮件
	 * @throws Exception 抛出邮件发送过程中出现的异常
	 */
	public void send() throws Exception{

		Properties props = System.getProperties();
		props.put("mail.smtp.host", smtpServer);
		props.put("mail.smtp.auth", "true"); //设置smtp认证，很关键的一句
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
