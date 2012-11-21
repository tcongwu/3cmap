package com.zjiao;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.zjiao.util.RandomImageGenerator;


/**
 * ���ڲ���ע���û�ʱ���漴ͼƬ�Է�ֹ�Ƿ�����
 * @author Lee Bin
 */
public class RandomImageServlet extends HttpServlet {

	public final static String RANDOM_LOGIN_KEY = "RANDOM_LOGIN_KEY";

	public void init() throws ServletException {
		System.setProperty("java.awt.headless","true");
	}
	
    public static String getRandomLoginKey(HttpServletRequest req) {
        return (String)req.getSession().getAttribute(RANDOM_LOGIN_KEY);
    }
    
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException 
    {
        HttpSession ssn = req.getSession();
        if(ssn!=null) {
            String randomString = RandomImageGenerator.random();
            ssn.setAttribute(RANDOM_LOGIN_KEY,randomString);
            res.setContentType("image/jpeg");
            RandomImageGenerator.render(randomString,res.getOutputStream());
        }
    }
}
