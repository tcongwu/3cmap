package com.zjiao.file;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

import com.zjiao.ErrorInformation;
import com.zjiao.Logger;
import com.zjiao.auth.AuthUserDAO;
import com.zjiao.auth.UserInfo;
import com.zjiao.struts.ActionExtend;
import com.zjiao.util.StringUtils;

/**
 * 图片上传
 *
 * @author Lee Bin
 */
public class UploadAction extends ActionExtend {

	public final static DateFormat PIC_PATH = new SimpleDateFormat("/yyyy/MM/dd");
	public final static int MAX_WIDTH = 440; 
	public final static int MAX_HEIGHT = 450;
	
	protected static List filesDenied = null;
	    
	/**
	 * 上传照片处理，用于处理来自用户的照片上传请求
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward doUploadPhoto(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception 
	{
		//初始化
		UserInfo loginUser = UserInfo.getLoginUser(request);
		ErrorInformation errInfo=new FileErrInfo();
		String photo=loginUser.getPhoto();
		boolean chg=false;
		
		//只有登录用户允许上传
		if(loginUser==null){
			errInfo.saveError(FileErrInfo.NEED_LOGIN);
		}else{
			
			//创建用于存放照片的目录
			UploadFileForm uff = (UploadFileForm)form;
			String spath='/' + FileUtils.UPLOAD_WEB + "/" + FileUtils.PHOTO_PATH + PIC_PATH.format(new Date());
			String uploadPath = servlet.getServletContext().getRealPath(spath);//上传磁盘路径
			if(!FileUtils.newFolder(uploadPath)){
				errInfo.saveError(FileErrInfo.FILE_ERR);
			}else{
				
				//检查上传的照片是否符合要求
				FormFile formFile = uff.getUploadFile();
				
				//检查文件大小
				if(formFile.getFileSize()>600000){
					errInfo.saveError(FileErrInfo.FILESIZE_ERR);
				}else{
					
					String extendName = getFileExtendName(formFile.getFileName());
					//只允许上传jpg、jpeg、gif和png类型的照片
					if(!"jpg".equalsIgnoreCase(extendName)&&!"jpeg".equalsIgnoreCase(extendName)&&!"gif".equalsIgnoreCase(extendName)&&!"png".equalsIgnoreCase(extendName)){
						errInfo.saveError(FileErrInfo.FILETYPE_ERR);
					}else{
						
						//上传图片
						String fn = String.valueOf(loginUser.getId());
						StringBuffer filePath = new StringBuffer(uploadPath);
						filePath.append(File.separator);
						filePath.append(fn);
						File file = new File(filePath.toString());
						FileOutputStream fos = new FileOutputStream(file);
						try {
							//写入到目录
							fos.write(formFile.getFileData());
						} finally {
							fos.close();
						}
					}
				}
			}
			
			//更新用户数据，指向新上传的照片
			if(errInfo.isEmpty()){
				loginUser.setPhoto(spath);
				chg=true;
				try{
					int ret=AuthUserDAO.updateUser(loginUser);
					if(ret!=1)
						errInfo.saveError(FileErrInfo.ERR_UNKNOWN);
				}catch(Exception e){
					Logger.Log(Logger.LOG_ERROR, e);
					errInfo.saveError(FileErrInfo.DATABASE_ERR);
				}
			}
			
		}
		
		if(!errInfo.isEmpty()){
			if(chg){
				loginUser.setPhoto(photo);
			}
			errInfo.saveToRequest("err",request);
			return mapping.findForward("error");
		}
		
		return new ActionForward("/profile/photo"+loginUser.getUserType()+".jsp", true);
	}
	    
	/**
	 * 上传文件处理，用于处理来自HTML编辑器中插入图片页面的提交请求
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward doUpload(ActionMapping mapping, ActionForm form,
	        HttpServletRequest request, HttpServletResponse response)
	        throws Exception 
	{
		//初始化
		ErrorInformation errInfo=new FileErrInfo();

		//获取输出路径
		ActionForward outUrl=null;
		String url=request.getParameter("url");

		if(StringUtils.isNull(url)){
			outUrl=mapping.getInputForward();
		}else{
			outUrl=new ActionForward("/"+url,false);
		}
		
		//获取登录用户及角色信息
		UserInfo loginUser = UserInfo.getLoginUser(request);
		//只有登录用户允许上传
		if(loginUser==null){
			errInfo.saveError(FileErrInfo.NEED_LOGIN);
			errInfo.saveToRequest("err",request);
	    return outUrl;
		}
		UploadFileForm uff = (UploadFileForm)form;
		String uploadDirName = servlet.getInitParameter("uploadDir");//上传目录名
		String spath='/' + uploadDirName + PIC_PATH.format(new Date());
		String uploadPath = servlet.getServletContext().getRealPath(spath);//上传磁盘路径
		if(!FileUtils.newFolder(uploadPath)){
			errInfo.saveError(FileErrInfo.FILE_ERR);
			errInfo.saveToRequest("err",request);
			return outUrl;
		}
		FormFile formFile = uff.getUploadFile();	
		//检查文件大小
		if(formFile.getFileSize()>500000){
			errInfo.saveError(FileErrInfo.FILESIZE_ERR);
			errInfo.saveToRequest("err",request);
	    return outUrl;
		}	
		String extendName = getFileExtendName(formFile.getFileName());
		//检查是否为可允许上传的文件类型
		String sFilesDenied = servlet.getInitParameter("filesDenied");
		if(sFilesDenied!=null && filesDenied==null){
			synchronized(this){
	    		filesDenied = new Vector();
	    		StringTokenizer st = new StringTokenizer(sFilesDenied,",");
	    		while(st.hasMoreElements())
	    			filesDenied.add(st.nextToken());
			}
		}
		if(filesDenied!=null){
			for(int i=0;i<filesDenied.size();i++){
				String en = (String)filesDenied.get(i);
				if(en.equalsIgnoreCase(extendName)){
					errInfo.saveError(FileErrInfo.FILETYPE_ERR);
					errInfo.saveToRequest("err",request);
					return outUrl;
				}
			}
		}
		String fn = getUniqueFileName(uploadPath, extendName);
		StringBuffer filePath = new StringBuffer(uploadPath);
		filePath.append(File.separator);
		filePath.append(fn);
		File file = new File(filePath.toString());
		FileOutputStream fos = new FileOutputStream(file);
		try {
		    //写入到目录
			fos.write(formFile.getFileData());
		} finally {
			fos.close();
		}
		
		String fileURI = spath + '/' + fn;		
		String html = null;		
		String context = request.getContextPath() + '/';
		
		if(isImage(fileURI) && uff.getLink()==0)
			html = genImageTag(file, fileURI);
		else{
			String fileName = new String(formFile.getFileName().getBytes(),"GBK");
			html = genLinkTag(file, fileName, context, fileURI);
		}
		request.setAttribute("html_tag", html);
		request.setAttribute("fileurl",fileURI);
		
	  return outUrl;
	}
	
	protected boolean isImage(String uri){
		String URI = uri.toUpperCase();
		return URI.endsWith(".JPG") ||
			   URI.endsWith(".GIF") ||
			   URI.endsWith(".PNG") ||
			   URI.endsWith(".BMP");
	}
	
	/**
	 * 生成链接的HTML标签
	 * @param path
	 * @param uri
	 * @return
	 */
	protected String genLinkTag(File path, String fileName, String context, String uri){
		StringBuffer html = new StringBuffer(128);
		html.append("<a href='");
		html.append(uri);
		html.append("' target='_blank'>");
		String fileIcon = getFileIcon(uri);
		if(fileIcon!=null){
			html.append("<img border='0' src='");
			html.append(context);
			html.append(fileIcon);
			html.append("' align='absmiddle'/>&nbsp;");
		}
		html.append(fileName);
		html.append("</a>");
		return html.toString();
	}
	
	protected String getFileIcon(String uri){
		String ext = getFileExtendName(uri);
		String icon_uri = "/editor/filemanager/browser/default/images/icons/" + ext + ".gif";
		String icon_path = servlet.getServletContext().getRealPath(icon_uri);
		File f = new File(icon_path);
		return f.exists()?icon_uri.substring(1):null;
	}
	
	/**
	 * 生成图像的HTML标签
	 * @param path
	 * @param uri
	 * @return
	 */
	protected String genImageTag(File path, String uri){
		//计算图像的大小
		int width = 0, height=0;
		try{
			BufferedImage bi = ImageIO.read(path);
			width = bi.getWidth();
			height = bi.getHeight();
		}catch(Exception e){
			getServlet().log("uploadImage process image failed.",e);
		}
		StringBuffer imgstr = new StringBuffer(128);
		imgstr.append("<img src='");
		imgstr.append(uri);
		imgstr.append("' border='0'");
		boolean sizeok = false;
		if(width > MAX_WIDTH ) {
		    imgstr.append(" width='");
		    imgstr.append(MAX_WIDTH);
		    imgstr.append("'");
	    	int nHeight = (MAX_WIDTH * height)/ width;
	    	imgstr.append(" height='");
	    	imgstr.append(nHeight);
	    	imgstr.append("'");
	    	sizeok = true;
		}
		if(!sizeok && (height > MAX_HEIGHT)) {
		    imgstr.append(" height='");
		    imgstr.append(MAX_HEIGHT);
		    imgstr.append("'");
	    	int nWidth = (MAX_HEIGHT * width) / height;
	    	imgstr.append(" width='");
	    	imgstr.append(nWidth);
	    	imgstr.append("'");
		}
		imgstr.append("/>");
		return imgstr.toString();
	}
	
	/**
	 * 得到一个唯一的文件名
	 * @param extName
	 * @return
	 */
	protected String getUniqueFileName(String uploadPath, String extName) {
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmssSSSS.");
		String fn = null;
		do {
			fn = sdf.format(new Date()) + extName;
			if (new File(uploadPath + File.separator + fn).exists())
				continue;
			break;
		} while (true);
		return fn;
	}
	
	/**
	 * 获取文件的扩展名
	 * @param file
	 * @return
	 */
	protected static String getFileExtendName(String file) {
		int idx = file.lastIndexOf('.');
		return (idx == -1 || idx == (file.length() - 1)) ? "" : file.substring(idx + 1).toLowerCase();
	}

}
