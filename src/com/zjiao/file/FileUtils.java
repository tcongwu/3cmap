package com.zjiao.file;

import java.io.*;
import java.util.Random;

import com.zjiao.Logger;
import com.zjiao.util.StringUtils;

/**
 * @author Lee Bin
 *
 * 文件操作相关的工具类，
 * 完成路径、文件名等的操作
 */
public class FileUtils {
	
	public final static String CONVERT_QUES = "98";	//问题
	public final static String CONVERT_ANS = "99";	//答案
	public final static int CONVERT_RAMDOM = 97;	//随机数
	public final static int PARAM_ID = 1;
	public final static int PARAM_TYPE = 2;
	public final static int PARAM_NAME = 3;
	
	public final static String PREFIX_QUES = "A";	//问题前缀
	public final static String PREFIX_ANS = "B";	//答案前缀
	public final static String TYPE_DOC = "D";	//Word文档类型
	public final static String TYPE_HTML = "H";	//网页文档类型

	public final static String POSTFIX_DOC = ".doc";	//Word文档后缀
	public final static String POSTFIX_HTML = ".html";	//网页文档后缀

	public static String ROOT = null;	//根目录
	public final static String ROOT_WEB = "/";				//web根目录
	public final static String UPLOAD_WEB = "upload";				//文件上传目录
	public final static String PHOTO_PATH = "photo";				//用户照片所在目录

	/**
	 * 默认的构造函数
	 */
	public FileUtils() {
	}
	
	/**
	 * 将ID值转换成路径
	 * @param id ID值
	 * @return 路径信息
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
	 * 将ID值转换成路径
	 * @param id ID值
	 * @param separatorChar 路径分隔字符串
	 * @return 路径信息
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
	 * 获取存放试题的路径
	 * @param id 试题ID
	 * @param rl 当前角色
	 * @return 路径
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
	 * 获取存放试题的路径
	 * @param id 试题ID
	 * @param rl 当前角色
	 * @return 路径
	 */
	public static String getVisualPath(String id){
		return getVisualPath(id);
	}
	
	/**
	 * 获取存放试题的路径
	 * @param id 试题ID
	 * @param subid 学科ID
	 * @return 路径
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
	 * 获取存放试题的路径
	 * @param id 试题ID
	 * @param subid 学科ID
	 * @param name 文件名
	 * @return 路径
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
	 * 获取存放试题的位置
	 * @param id 试题ID
	 * @param rl 当前角色
	 * @param name 文件名
	 * @return 路径
	 */
	public static String getQuesPosition(String id, String name){
		return getVisualPath(id)+name+POSTFIX_HTML;
	}
	
	/**
	 * 检查访问的URI的文件的格式
	 * @param uri 资源地址
	 * @param fmt 格式
	 * @return 是否相符
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
	 * 获取URI指向的html文件对象
	 * @param uri 文件地址
	 * @return 文件对象
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
	 * 获取URI指向的doc文件对象
	 * @param uri 文件地址
	 * @return 文件对象
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
	 * 获取doc文件的URL
	 * @param uri 文件地址
	 * @return 文件对象
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
	 * 获取doc文件的URL
	 * @param uri 文件地址
	 * @return 文件对象
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
	 * 产生文件名
	 * @param type 文件类型，A－题干；B－解答
	 * @return 文件名字符串
	 */
	public static String createFileName(String type){
		return type+System.currentTimeMillis();
	}
	
	/**
	 * 从路径中找到指定的文件
	 * @param path 路径对象
	 * @param start 文件名的开始字符
	 * @param fmt 文件类型
	 * @return 文件对象
	 */
	public static File findFile(File path, String start, String fmt){
		if((path==null)||(!path.isDirectory())||(start==null)||(fmt==null)){
			return null;
		}
		
		java.io.File allfiles[] = path.listFiles();

		//寻找指定文件
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
	 * 从路径中找到指定的文件
	 * @param path 路径对象
	 * @param start 文件名的开始字符
	 * @param fmt 文件类型
	 * @return 文件对象
	 */
	public static File findFile(String id, String start, String fmt){
		if(id==null){
			return null;
		}
		File path=new File(FileUtils.ROOT+convertToPath(id));

		return findFile(path, start, fmt);
	}
	
	/**
	 * 查看是否存在题干或解答过程内容
	 * @param id 试题ID
	 * @param name 题干或解答的路径信息
	 * @return 是否存在
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
	 * 查看是否存在题干或解答过程内容
	 * @param id 试题ID
	 * @param name 题干或解答的路径信息
	 * @return 是否存在
	 */
	public static boolean isExistContent(int id, String content){
		return isExistContent(String.valueOf(id), content);
	}

	/**
	 * 新建目录
	 * @param folderPath String 如 c:/fqf
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
	 * 是否存在当前目录
	 * @param path 需要判断的目录
	 * @return boolean 是否存在
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
	 * 新建文件
	 * @param filePathAndName String 文件路径及名称 如c:/fqf.txt
	 * @param fileContent String 文件内容
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
	 * 删除文件
	 * @param filePathAndName String 文件路径及名称 如c:/fqf.txt
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
	 * 删除文件夹
	 * @param filePathAndName String 文件夹路径及名称 如c:/fqf
	 * @param fileContent String
	 * @return boolean
	 */
	public static boolean delFolder(String folderPath) {
	  try {
		delAllFile(folderPath); //删除完里面所有内容
		String filePath = folderPath;
		filePath = filePath.toString();
		java.io.File myFilePath = new java.io.File(filePath);
		myFilePath.delete(); //删除空文件夹
	  }
	  catch (Exception e) {
		return false;
	  }
	  return true;
	}

	/**
	 * 删除文件夹里面的所有文件
	 * @param path String 文件夹路径 如 c:/fqf
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
			  delAllFile(path+"/"+ tempList[i]);//先删除文件夹里面的文件
			  delFolder(path+"/"+ tempList[i]);//再删除空文件夹
			}
		  }
	  }catch(Exception e){
		  return false;
	  }
	  return true;
	}

	/**
	 * 复制单个文件
	 * @param oldPath String 原文件路径 如：c:/fqf.txt
	 * @param newPath String 复制后路径 如：f:/fqf.txt
	 * @return boolean
	 */
	public static boolean copyFile(String oldPath, String newPath) {
	  try {
		int bytesum = 0;
		int byteread = 0;
		File oldfile = new File(oldPath);
		if (oldfile.exists()) { //文件存在时
		  InputStream inStream = new FileInputStream(oldPath); //读入原文件
		  FileOutputStream fs = new FileOutputStream(newPath);
		  byte[] buffer = new byte[1444];
		  int length;
		  while ( (byteread = inStream.read(buffer)) != -1) {
			bytesum += byteread; //字节数 文件大小
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
	 * 复制整个文件夹内容
	 * @param oldPath String 原文件路径 如：c:/fqf
	 * @param newPath String 复制后路径 如：f:/fqf/ff
	 * @return boolean
	 */
	public static boolean copyFolder(String oldPath, String newPath) {

	  try {
		(new File(newPath)).mkdirs(); //如果文件夹不存在 则建立新文件夹
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
		  if(temp.isDirectory()){//如果是子文件夹
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
	 * 移动文件到指定目录
	 * @param oldPath String 如：c:/fqf.txt
	 * @param newPath String 如：d:/fqf.txt
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
	 * 移动文件到指定目录
	 * @param oldPath String 如：c:/fqf.txt
	 * @param newPath String 如：d:/fqf.txt
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
		* 生成文件
		* @param String 文件内容
		* @param String pathName 文件路径,
		* @param fileName 文件名
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
		* 获取照片尺寸
		* @param fileName 照片文件
		* @return String 含有分辨率的字符串
	*/
	public static String getImageSize(String fileName) {
		int width=0;
		int height=0;
		try{
			File fl = new File(fileName); //读入文件
			if(fl.exists()){
				java.awt.Image src = javax.imageio.ImageIO.read(fl); //构造Image对象
				width=src.getWidth(null); //得到源图宽
				height=src.getHeight(null); //得到源图长
			}
		}catch(IOException ie){
			Logger.Log(Logger.LOG_ERROR, ie);
		}
		
		return width+","+height;
	}

}
