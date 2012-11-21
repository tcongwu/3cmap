package com.zjiao.file;

import java.io.*;
import java.util.Random;

import com.zjiao.Logger;
import com.zjiao.util.StringUtils;

/**
 * @author Lee Bin
 *
 * �ļ�������صĹ����࣬
 * ���·�����ļ����ȵĲ���
 */
public class FileUtils {
	
	public final static String CONVERT_QUES = "98";	//����
	public final static String CONVERT_ANS = "99";	//��
	public final static int CONVERT_RAMDOM = 97;	//�����
	public final static int PARAM_ID = 1;
	public final static int PARAM_TYPE = 2;
	public final static int PARAM_NAME = 3;
	
	public final static String PREFIX_QUES = "A";	//����ǰ׺
	public final static String PREFIX_ANS = "B";	//��ǰ׺
	public final static String TYPE_DOC = "D";	//Word�ĵ�����
	public final static String TYPE_HTML = "H";	//��ҳ�ĵ�����

	public final static String POSTFIX_DOC = ".doc";	//Word�ĵ���׺
	public final static String POSTFIX_HTML = ".html";	//��ҳ�ĵ���׺

	public static String ROOT = null;	//��Ŀ¼
	public final static String ROOT_WEB = "/";				//web��Ŀ¼
	public final static String UPLOAD_WEB = "upload";				//�ļ��ϴ�Ŀ¼
	public final static String PHOTO_PATH = "photo";				//�û���Ƭ����Ŀ¼

	/**
	 * Ĭ�ϵĹ��캯��
	 */
	public FileUtils() {
	}
	
	/**
	 * ��IDֵת����·��
	 * @param id IDֵ
	 * @return ·����Ϣ
	 */
	private static String convertToPath(String id){
		if(StringUtils.isNull(id)){
			return null;
		}
		
		StringBuffer path=new StringBuffer();
		for(int i=0;i<id.length();i++){
			if(i%2==0){
				path.append(File.separatorChar);
			}
			path.append(id.charAt(i));
		}
		
		return path.toString();
	}
	
	/**
	 * ��IDֵת����·��
	 * @param id IDֵ
	 * @param separatorChar ·���ָ��ַ���
	 * @return ·����Ϣ
	 */
	private static String convertToPath(String id, char separatorChar){
		if(StringUtils.isNull(id)){
			return null;
		}
		
		StringBuffer path=new StringBuffer();
		for(int i=0;i<id.length();i++){
			if(i%2==0){
				path.append(separatorChar);
			}
			path.append(id.charAt(i));
		}
		
		return path.toString();
	}
	
	/**
	 * ��ȡ��������·��
	 * @param id ����ID
	 * @param rl ��ǰ��ɫ
	 * @return ·��
	 */
	public static String getPhysicalPath(String id){
		StringBuffer path=new StringBuffer();
		path.append(ROOT);
		path.append(File.separatorChar);
		path.append(convertToPath(id));
		path.append(File.separatorChar);
		
		return path.toString();
	}
	
	/**
	 * ��ȡ��������·��
	 * @param id ����ID
	 * @param rl ��ǰ��ɫ
	 * @return ·��
	 */
	public static String getVisualPath(String id){
		return getVisualPath(id);
	}
	
	/**
	 * ��ȡ��������·��
	 * @param id ����ID
	 * @param subid ѧ��ID
	 * @return ·��
	 */
	public static String getVisualPath(String id, String subid){
		StringBuffer path=new StringBuffer();
		path.append(ROOT_WEB);
		path.append('/');
		path.append(subid);
		path.append(convertToPath(id, '/'));
		path.append('/');
		
		return path.toString();
	}
	
	/**
	 * ��ȡ��������·��
	 * @param id ����ID
	 * @param subid ѧ��ID
	 * @param name �ļ���
	 * @return ·��
	 */
	public static String getVisualPath(String id, String subid, String name){
		StringBuffer path=new StringBuffer();
		path.append(ROOT_WEB);
		path.append('/');
		path.append(subid);
		path.append(convertToPath(id, '/'));
		path.append('/');
		path.append(name);
		path.append(POSTFIX_HTML);
		
		return path.toString();
	}
	
	/**
	 * ��ȡ��������λ��
	 * @param id ����ID
	 * @param rl ��ǰ��ɫ
	 * @param name �ļ���
	 * @return ·��
	 */
	public static String getQuesPosition(String id, String name){
		return getVisualPath(id)+name+POSTFIX_HTML;
	}
	
	/**
	 * �����ʵ�URI���ļ��ĸ�ʽ
	 * @param uri ��Դ��ַ
	 * @param fmt ��ʽ
	 * @return �Ƿ����
	 */
	private static boolean checkFormat(String uri, String fmt){
		if((uri==null)||StringUtils.isNull(fmt)){
			return false;
		}
		
		if((uri.endsWith("/"))||(uri.endsWith("\\"))||(uri.endsWith("."))){
			return false;
		}
		
		if(uri.indexOf("..")!=-1){
			return false;
		}
		
		String upperURI=uri.toUpperCase();
		if((upperURI.indexOf("/WEB-INF")!=-1)||(upperURI.indexOf("/META-INF")!=-1)){
			return false;
		}
		
		if(!upperURI.endsWith(fmt.toUpperCase())){
			return false;
		}
		
		return true;
	}
	
	/**
	 * ��ȡURIָ���html�ļ�����
	 * @param uri �ļ���ַ
	 * @return �ļ�����
	 */
	public static File getHtmlFile(String uri){
		if((uri==null)||(!uri.endsWith(".html"))||(uri.indexOf("..")!=-1)){
			return null;
		}
		Logger.Log(Logger.LOG_DEBUG, "uri is "+uri);
		
		String[] params=uri.split("/");
		Logger.Log(Logger.LOG_DEBUG, "params is "+params[0]+":"+params[1]+":"+params[2]+":"+params[3]+":"+params.length);
		if(params.length!=4){
			return null;
		}
		
		if(CONVERT_QUES.equals(params[PARAM_TYPE])){
			return findFile(params[PARAM_ID], PREFIX_QUES, ".html");
		}
		else if(CONVERT_ANS.equals(params[PARAM_TYPE])){
			return findFile(params[PARAM_ID], PREFIX_ANS, ".html");
		}
		else{
			File file=new File(getPhysicalPath(params[PARAM_ID])+params[PARAM_NAME]);
			if(file.exists()&&file.isFile()){
				return file;
			}else{
				return null;
			}
		}
	}
	
	/**
	 * ��ȡURIָ���doc�ļ�����
	 * @param uri �ļ���ַ
	 * @return �ļ�����
	 */
	public static File getDocFile(String uri){
		if((uri==null)||(!uri.endsWith(".doc"))||(uri.indexOf("..")!=-1)){
			return null;
		}
		
		String[] params=uri.split("/");
		if(params.length!=4){
			return null;
		}
		
		if(CONVERT_QUES.equals(params[PARAM_TYPE])){
			return findFile(params[PARAM_ID], PREFIX_QUES, ".doc");
		}
		else if(CONVERT_ANS.equals(params[PARAM_TYPE])){
			return findFile(params[PARAM_ID], PREFIX_ANS, ".doc");
		}
		else{
			File file=new File(getPhysicalPath(params[PARAM_ID])+params[PARAM_NAME]);
			if(file.exists()&&file.isFile()){
				return file;
			}else{
				return null;
			}
		}
	}
	
	/**
	 * ��ȡdoc�ļ���URL
	 * @param uri �ļ���ַ
	 * @return �ļ�����
	 */
	public static String getDocFilePath(int id, String content){
		Random rm=new Random();
		
		if(content==null){
			return null;
		}
		StringBuffer path=new StringBuffer();
		path.append('/');
		path.append(id);
		path.append('/');
		path.append(rm.nextInt(97)+1);
		path.append('/');
		path.append(content);
		path.append(".doc");
		
		return path.toString();
	}
	
	/**
	 * ��ȡdoc�ļ���URL
	 * @param uri �ļ���ַ
	 * @return �ļ�����
	 */
	public static String getDocFilePath(String id, String content){
		Random rm=new Random();
		
		if(content==null){
			return null;
		}
		StringBuffer path=new StringBuffer();
		path.append('/');
		path.append(id);
		path.append('/');
		path.append(rm.nextInt(97)+1);
		path.append('/');
		path.append(content);
		path.append(".doc");
		
		return path.toString();
	}
	
	/**
	 * �����ļ���
	 * @param type �ļ����ͣ�A����ɣ�B�����
	 * @return �ļ����ַ���
	 */
	public static String createFileName(String type){
		return type+System.currentTimeMillis();
	}
	
	/**
	 * ��·�����ҵ�ָ�����ļ�
	 * @param path ·������
	 * @param start �ļ����Ŀ�ʼ�ַ�
	 * @param fmt �ļ�����
	 * @return �ļ�����
	 */
	public static File findFile(File path, String start, String fmt){
		if((path==null)||(!path.isDirectory())||(start==null)||(fmt==null)){
			return null;
		}
		
		java.io.File allfiles[] = path.listFiles();

		//Ѱ��ָ���ļ�
		for (int i=0;i< allfiles.length; i++){
			if( allfiles[i].isFile() ){
				if (allfiles[i].getName().endsWith(fmt)&&allfiles[i].getName().startsWith(start))				{				
					return allfiles[i];
				}
			}
		}
		return null;
	}
	
	/**
	 * ��·�����ҵ�ָ�����ļ�
	 * @param path ·������
	 * @param start �ļ����Ŀ�ʼ�ַ�
	 * @param fmt �ļ�����
	 * @return �ļ�����
	 */
	public static File findFile(String id, String start, String fmt){
		if(id==null){
			return null;
		}
		File path=new File(FileUtils.ROOT+convertToPath(id));

		return findFile(path, start, fmt);
	}
	
	/**
	 * �鿴�Ƿ������ɻ����������
	 * @param id ����ID
	 * @param name ��ɻ����·����Ϣ
	 * @return �Ƿ����
	 */
	public static boolean isExistContent(String id, String content){
		if(StringUtils.isNull(id)||StringUtils.isNull(content))
			return false;
		
		File file=new File(getPhysicalPath(id)+content+POSTFIX_HTML);
		if(file.exists()&&file.isFile()){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * �鿴�Ƿ������ɻ����������
	 * @param id ����ID
	 * @param name ��ɻ����·����Ϣ
	 * @return �Ƿ����
	 */
	public static boolean isExistContent(int id, String content){
		return isExistContent(String.valueOf(id), content);
	}

	/**
	 * �½�Ŀ¼
	 * @param folderPath String �� c:/fqf
	 * @return boolean
	 */
	public static boolean newFolder(String folderPath) {
	  try {
			String filePath = folderPath;
			filePath = filePath.toString();
			java.io.File myFilePath = new java.io.File(filePath);
			if (!myFilePath.exists()) {
			  myFilePath.mkdirs();
			}
	  }
	  catch (Exception e) {
			return false;
	  }
	  
	  return true;
	}
  
	/**
	 * �Ƿ���ڵ�ǰĿ¼
	 * @param path ��Ҫ�жϵ�Ŀ¼
	 * @return boolean �Ƿ����
	 */
	public static boolean isExistFolder(String path){
	  try {
		String filePath = path;
		filePath = filePath.toString();
		java.io.File myFilePath = new java.io.File(filePath);
		if (myFilePath.exists()) {
		  return true;
		}else{
		  return false;
		}
	  }
	  catch (Exception e) {
		return false;
	  }
  	
	}

	/**
	 * �½��ļ�
	 * @param filePathAndName String �ļ�·�������� ��c:/fqf.txt
	 * @param fileContent String �ļ�����
	 * @return boolean
	 */
	public static boolean newFile(String filePathAndName, String fileContent) {

	  try {
		String filePath = filePathAndName;
		filePath = filePath.toString();
		File myFilePath = new File(filePath);
		if (!myFilePath.exists()) {
		  myFilePath.createNewFile();
		}
		FileWriter resultFile = new FileWriter(myFilePath);
		PrintWriter myFile = new PrintWriter(resultFile);
		String strContent = fileContent;
		myFile.println(strContent);
		resultFile.close();

	  }
	  catch (Exception e) {
		return false;
	  }
	  return true;
	}

	/**
	 * ɾ���ļ�
	 * @param filePathAndName String �ļ�·�������� ��c:/fqf.txt
	 * @param fileContent String
	 * @return boolean
	 */
	public static boolean delFile(String filePathAndName) {
	  try {
			String filePath = filePathAndName;
			filePath = filePath.toString();
			java.io.File myDelFile = new java.io.File(filePath);
			myDelFile.delete();
	  }
	  catch (Exception e) {
			return false;
	  }
	  return true;
	}

	/**
	 * ɾ���ļ���
	 * @param filePathAndName String �ļ���·�������� ��c:/fqf
	 * @param fileContent String
	 * @return boolean
	 */
	public static boolean delFolder(String folderPath) {
	  try {
		delAllFile(folderPath); //ɾ����������������
		String filePath = folderPath;
		filePath = filePath.toString();
		java.io.File myFilePath = new java.io.File(filePath);
		myFilePath.delete(); //ɾ�����ļ���
	  }
	  catch (Exception e) {
		return false;
	  }
	  return true;
	}

	/**
	 * ɾ���ļ�������������ļ�
	 * @param path String �ļ���·�� �� c:/fqf
	 */
	public static boolean delAllFile(String path) {
	  try{
		  File file = new File(path);
		  if (!file.exists()) {
			return true;
		  }
		  if (!file.isDirectory()) {
			return true;
		  }
		  String[] tempList = file.list();
		  File temp = null;
		  for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
			  temp = new File(path + tempList[i]);
			}
			else {
			  temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
			  temp.delete();
			}
			if (temp.isDirectory()) {
			  delAllFile(path+"/"+ tempList[i]);//��ɾ���ļ���������ļ�
			  delFolder(path+"/"+ tempList[i]);//��ɾ�����ļ���
			}
		  }
	  }catch(Exception e){
		  return false;
	  }
	  return true;
	}

	/**
	 * ���Ƶ����ļ�
	 * @param oldPath String ԭ�ļ�·�� �磺c:/fqf.txt
	 * @param newPath String ���ƺ�·�� �磺f:/fqf.txt
	 * @return boolean
	 */
	public static boolean copyFile(String oldPath, String newPath) {
	  try {
		int bytesum = 0;
		int byteread = 0;
		File oldfile = new File(oldPath);
		if (oldfile.exists()) { //�ļ�����ʱ
		  InputStream inStream = new FileInputStream(oldPath); //����ԭ�ļ�
		  FileOutputStream fs = new FileOutputStream(newPath);
		  byte[] buffer = new byte[1444];
		  int length;
		  while ( (byteread = inStream.read(buffer)) != -1) {
			bytesum += byteread; //�ֽ��� �ļ���С
			System.out.println(bytesum);
			fs.write(buffer, 0, byteread);
		  }
		  inStream.close();
		}
	  }
	  catch (Exception e) {
		return false;
	  }
	  return true;
	}

	/**
	 * ���������ļ�������
	 * @param oldPath String ԭ�ļ�·�� �磺c:/fqf
	 * @param newPath String ���ƺ�·�� �磺f:/fqf/ff
	 * @return boolean
	 */
	public static boolean copyFolder(String oldPath, String newPath) {

	  try {
		(new File(newPath)).mkdirs(); //����ļ��в����� �������ļ���
		File a=new File(oldPath);
		String[] file=a.list();
		File temp=null;
		for (int i = 0; i < file.length; i++) {
		  if(oldPath.endsWith(File.separator)){
			temp=new File(oldPath+file[i]);
		  }
		  else{
			temp=new File(oldPath+File.separator+file[i]);
		  }

		  if(temp.isFile()){
			FileInputStream input = new FileInputStream(temp);
			FileOutputStream output = new FileOutputStream(newPath + "/" +
				(temp.getName()).toString());
			byte[] b = new byte[1024 * 5];
			int len;
			while ( (len = input.read(b)) != -1) {
			  output.write(b, 0, len);
			}
			output.flush();
			output.close();
			input.close();
		  }
		  if(temp.isDirectory()){//��������ļ���
			copyFolder(oldPath+"/"+file[i],newPath+"/"+file[i]);
		  }
		}
	  }
	  catch (Exception e) {
		return false;
	  }
	  return true;
	}

	/**
	 * �ƶ��ļ���ָ��Ŀ¼
	 * @param oldPath String �磺c:/fqf.txt
	 * @param newPath String �磺d:/fqf.txt
	 */
	public static boolean moveFile(String oldPath, String newPath) {
	  if(!copyFile(oldPath, newPath)){
		  return false;
	  }
	  if(!delFile(oldPath)){
		  return false;
	  }
	  return true;
	}

	/**
	 * �ƶ��ļ���ָ��Ŀ¼
	 * @param oldPath String �磺c:/fqf.txt
	 * @param newPath String �磺d:/fqf.txt
	 */
	public static boolean moveFolder(String oldPath, String newPath) {
	  if(!copyFolder(oldPath, newPath)){
		  return false;
	  }
	  if(!delFolder(oldPath)){
		  return false;
	  }
	  return true;
	}

	/**
		* �����ļ�
		* @param String �ļ�����
		* @param String pathName �ļ�·��,
		* @param fileName �ļ���
	*/
	public static void writeFile(String content,String pathName,String fileName) {
		try{
			newFolder(pathName);

			FileOutputStream fos = new FileOutputStream(pathName + fileName);
			byte contentByte[] = content.getBytes();
			fos.write(contentByte);
			fos.close();
		}catch(IOException ie){
			Logger.Log(Logger.LOG_ERROR, ie);
		}
	}
	
	/**
		* ��ȡ��Ƭ�ߴ�
		* @param fileName ��Ƭ�ļ�
		* @return String ���зֱ��ʵ��ַ���
	*/
	public static String getImageSize(String fileName) {
		int width=0;
		int height=0;
		try{
			File fl = new File(fileName); //�����ļ�
			if(fl.exists()){
				java.awt.Image src = javax.imageio.ImageIO.read(fl); //����Image����
				width=src.getWidth(null); //�õ�Դͼ��
				height=src.getHeight(null); //�õ�Դͼ��
			}
		}catch(IOException ie){
			Logger.Log(Logger.LOG_ERROR, ie);
		}
		
		return width+","+height;
	}

}
