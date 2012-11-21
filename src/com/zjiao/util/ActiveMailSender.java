package com.zjiao.util;

import javax.mail.*;
import javax.mail.internet.*;

import com.zjiao.auth.UserInfo;

import java.util.*;

/**
 * @author Lee Bin
 *
 * 用于生成发送用户帐号激活邮件
 */
public class ActiveMailSender {
	private static String smtpServer=null; //用来发送邮件的服务器地址
	private String from=null; //发送邮件的地址
	private String to; //接收邮件的地址
	private String sub; //邮件的主题
	private String content; //邮件的内容

	/**
	 * 默认的构造函数
	 */
	public ActiveMailSender() {
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
	
	public static boolean sendMail(UserInfo uinfo, String acode){
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy年MM月dd日");
		String mailHost = "mail.tsinghua.edu.cn";	//发送邮件服务器地址
		String mailUser = "binli";			//发送邮件服务器的用户帐号
		String mailPassword = "high681";	//发送邮件服务器的用户密码
		String[] toAddress = new String[1];
		toAddress[0]=uinfo.getEmail();
		//使用纯文本格式发送邮件
		MailSender sendmail = MailSender.getHtmlMailSender(mailHost, mailUser,mailPassword);
		try {
			sendmail.setSubject("[email验证]感谢你在3Cmap.com注册");
			sendmail.setSendDate(new Date());

			//生成邮件内容
			String content = uinfo.getRealName() + "，你好：<br><br>"
				+ "       欢迎你加入<a href=\"http://www.3cmap.com\">3Cmap.com</a>。请点击以下链接激活帐号完成注册：<br><br>"
				+ "           <a href=\"http://www.3cmap.com/activate.do?code=" + acode + "\" target=\"_blank\">http://www.3cmap.com/activate.do?code=" + acode + "</a><br><br>"
				+ "       如果以上网址无法点击，请将它拷贝到浏览器(例如IE)的地址栏中。<br>"
				+ "       希望你在3Cmap的体验健康愉快。感谢你对3Cmap的支持。<br><br>"
				+ "           <a href=\"http://www.3cmap.com\">http://www.3cmap.com</a><br><br>"
				+ "       (这是一封自动产生的email，请勿回复。)<br><br>"
				+ "                                       "+sdf.format(new Date());
			sendmail.setMailContent(content); //
			sendmail.setMailFrom("sys@3cmap.com","3Cmap");
			sendmail.setMailTo(toAddress, "to");

			//开始发送邮件
			System.out.println("正在发送邮件给"+uinfo.getRealName()+"，请稍候.......");
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