package com.zjiao.file;

import java.io.*;
import java.text.SimpleDateFormat;

import javax.servlet.*;
import javax.servlet.http.*;
import com.jspsmart.upload.*;
import com.zjiao.ErrorInformation;
import com.zjiao.Logger;
import com.zjiao.auth.AuthUserDAO;
import com.zjiao.auth.UserInfo;
import com.zjiao.util.StringUtils;

public class UploadPhoto extends HttpServlet {
	
	private static final String CONTENT_TYPE = "text/html; charset=GBK";
	private ServletConfig config;
	
	/**
	 * 初始化
	 */
	final public void init(ServletConfig config) throws ServletException {
		this.config = config;
	}
	
	final public ServletConfig getServletConfig() {
		return config;
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType(CONTENT_TYPE);
		RequestDispatcher dispatcher=config.getServletContext().getRequestDispatcher("/error.jsp");
		
		//初始化
		UserInfo loginUser = UserInfo.getLoginUser(request);
		ErrorInformation errInfo=new FileErrInfo();
		String photo=null;
		
		//定义变量
		int count=0;
		SmartUpload mySmartUpload = new SmartUpload();

		if(loginUser==null){
			errInfo.saveError(FileErrInfo.NEED_LOGIN);
		  errInfo.saveToRequest("err", request);
		  dispatcher.forward(request, response);
		}
		else{
			photo=loginUser.getPhoto();
			
			try {
				//初始化
				mySmartUpload.initialize(config,request,response);
				//设置照片文件大小为600K
				mySmartUpload.setMaxFileSize(320000);
				//设置照片文件类型
				mySmartUpload.setAllowedFilesList("JPG,GIF,PNG,jpg,jpeg,gif,png");
				//完成上载
				mySmartUpload.upload();

			} catch (Exception e){
				Logger.Log(Logger.LOG_ERROR, e);
				errInfo.saveError(FileErrInfo.FILESIZE_ERR);
				errInfo.saveToRequest("err", request);
				dispatcher.forward(request, response);
			}
		
			SimpleDateFormat sf = new SimpleDateFormat("/yyyy/MM/dd");
			String uploadDirName = "/upload/photo";//上传目录名
			String spath=uploadDirName + sf.format(new java.util.Date());
			String uploadPath = config.getServletContext().getRealPath(spath);//上传磁盘路径
			if(!FileUtils.newFolder(uploadPath)){
				errInfo.saveError(FileErrInfo.FILE_ERR);
				errInfo.saveToRequest("err", request);
				dispatcher.forward(request, response);
			}

			String fn=null;
			StringBuffer filePath=new StringBuffer(uploadPath);
			try{
				//保存图片文件到服务器磁盘
				com.jspsmart.upload.File myFile = mySmartUpload.getFiles().getFile(0);
				if(!myFile.isMissing()){
					String filename = myFile.getFileName();
					String extendName = StringUtils.getFileExtendName(filename);
					fn = loginUser.getId()+"."+extendName;
					filePath.append(java.io.File.separator);
					filePath.append(fn);
	
					myFile.saveAs(filePath.toString(), com.jspsmart.upload.SmartUpload.SAVE_PHYSICAL);
				}
	
			}catch(Exception fex){
				errInfo.saveError(FileErrInfo.FILE_ERR);
				errInfo.saveToRequest("err", request);
				dispatcher.forward(request, response);
			}
			
			//更新用户数据，指向新上传的照片
			if(errInfo.isEmpty()&&(fn!=null)){
				StringBuffer pho=new StringBuffer(spath);
				pho.append('/');
				pho.append(fn);
				pho.append(',');
				pho.append(FileUtils.getImageSize(filePath.toString()));
				
				loginUser.setPhoto(pho.toString());
				try{
					int ret=AuthUserDAO.updateUserF(loginUser);
					if(ret!=1)
						errInfo.saveError(FileErrInfo.ERR_UNKNOWN);
				}catch(Exception e){
					Logger.Log(Logger.LOG_ERROR, e);
					errInfo.saveError(FileErrInfo.DATABASE_ERR);
				}
			}
		}
			
		if(!errInfo.isEmpty()){
			loginUser.setPhoto(photo);
			errInfo.saveToRequest("err",request);
			dispatcher.forward(request, response);
		}
		
		response.sendRedirect("/profile/photo"+loginUser.getUserType()+".jsp");
	}
	
	/**
	 * 结束servlet
	 */
	public void  destroy () {
	}

}