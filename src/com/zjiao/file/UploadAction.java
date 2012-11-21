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
 * ͼƬ�ϴ�
 *
 * @author Lee Bin
 */
public class UploadAction extends ActionExtend {

	public final static DateFormat PIC_PATH = new SimpleDateFormat("/yyyy/MM/dd");
	public final static int MAX_WIDTH = 440; 
	public final static int MAX_HEIGHT = 450;
	
	protected static List filesDenied = null;
	    
	/**
	 * �ϴ���Ƭ�������ڴ��������û�����Ƭ�ϴ�����
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
		//��ʼ��
		UserInfo loginUser = UserInfo.getLoginUser(request);
		ErrorInformation errInfo=new FileErrInfo();
		String photo=loginUser.getPhoto();
		boolean chg=false;
		
		//ֻ�е�¼�û������ϴ�
		if(loginUser==null){
			errInfo.saveError(FileErrInfo.NEED_LOGIN);
		}else{
			
			//�������ڴ����Ƭ��Ŀ¼
			UploadFileForm uff = (UploadFileForm)form;
			String spath='/' + FileUtils.UPLOAD_WEB + "/" + FileUtils.PHOTO_PATH + PIC_PATH.format(new Date());
			String uploadPath = servlet.getServletContext().getRealPath(spath);//�ϴ�����·��
			if(!FileUtils.newFolder(uploadPath)){
				errInfo.saveError(FileErrInfo.FILE_ERR);
			}else{
				
				//����ϴ�����Ƭ�Ƿ����Ҫ��
				FormFile formFile = uff.getUploadFile();
				
				//����ļ���С
				if(formFile.getFileSize()>600000){
					errInfo.saveError(FileErrInfo.FILESIZE_ERR);
				}else{
					
					String extendName = getFileExtendName(formFile.getFileName());
					//ֻ�����ϴ�jpg��jpeg��gif��png���͵���Ƭ
					if(!"jpg".equalsIgnoreCase(extendName)&&!"jpeg".equalsIgnoreCase(extendName)&&!"gif".equalsIgnoreCase(extendName)&&!"png".equalsIgnoreCase(extendName)){
						errInfo.saveError(FileErrInfo.FILETYPE_ERR);
					}else{
						
						//�ϴ�ͼƬ
						String fn = String.valueOf(loginUser.getId());
						StringBuffer filePath = new StringBuffer(uploadPath);
						filePath.append(File.separator);
						filePath.append(fn);
						File file = new File(filePath.toString());
						FileOutputStream fos = new FileOutputStream(file);
						try {
							//д�뵽Ŀ¼
							fos.write(formFile.getFileData());
						} finally {
							fos.close();
						}
					}
				}
			}
			
			//�����û����ݣ�ָ�����ϴ�����Ƭ
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
	 * �ϴ��ļ��������ڴ�������HTML�༭���в���ͼƬҳ����ύ����
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
		//��ʼ��
		ErrorInformation errInfo=new FileErrInfo();

		//��ȡ���·��
		ActionForward outUrl=null;
		String url=request.getParameter("url");

		if(StringUtils.isNull(url)){
			outUrl=mapping.getInputForward();
		}else{
			outUrl=new ActionForward("/"+url,false);
		}
		
		//��ȡ��¼�û�����ɫ��Ϣ
		UserInfo loginUser = UserInfo.getLoginUser(request);
		//ֻ�е�¼�û������ϴ�
		if(loginUser==null){
			errInfo.saveError(FileErrInfo.NEED_LOGIN);
			errInfo.saveToRequest("err",request);
	    return outUrl;
		}
		UploadFileForm uff = (UploadFileForm)form;
		String uploadDirName = servlet.getInitParameter("uploadDir");//�ϴ�Ŀ¼��
		String spath='/' + uploadDirName + PIC_PATH.format(new Date());
		String uploadPath = servlet.getServletContext().getRealPath(spath);//�ϴ�����·��
		if(!FileUtils.newFolder(uploadPath)){
			errInfo.saveError(FileErrInfo.FILE_ERR);
			errInfo.saveToRequest("err",request);
			return outUrl;
		}
		FormFile formFile = uff.getUploadFile();	
		//����ļ���С
		if(formFile.getFileSize()>500000){
			errInfo.saveError(FileErrInfo.FILESIZE_ERR);
			errInfo.saveToRequest("err",request);
	    return outUrl;
		}	
		String extendName = getFileExtendName(formFile.getFileName());
		//����Ƿ�Ϊ�������ϴ����ļ�����
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
		    //д�뵽Ŀ¼
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
	 * �������ӵ�HTML��ǩ
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
	 * ����ͼ���HTML��ǩ
	 * @param path
	 * @param uri
	 * @return
	 */
	protected String genImageTag(File path, String uri){
		//����ͼ��Ĵ�С
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
	 * �õ�һ��Ψһ���ļ���
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
	 * ��ȡ�ļ�����չ��
	 * @param file
	 * @return
	 */
	protected static String getFileExtendName(String file) {
		int idx = file.lastIndexOf('.');
		return (idx == -1 || idx == (file.length() - 1)) ? "" : file.substring(idx + 1).toLowerCase();
	}

}
