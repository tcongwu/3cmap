package com.zjiao.file;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

import org.apache.struts.action.ActionForm;

/*
 *  验证上传文件参数的有效性
 */
public class UploadFileForm extends ActionForm {
	
	public final static long MAX_SIZE = 1048576L;

	FormFile uploadFile = null;
	int link = 0;

	/**
	 * 验证参数的有效性
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest req) {
		ActionErrors aes = new ActionErrors();
		if (uploadFile != null) {
			if (uploadFile.getFileSize() > MAX_SIZE)
				aes.add("uploadFile", new ActionError("upload_file_size_exceed"));
			if (uploadFile.getFileSize() <= 0)
				aes.add("uploadFile", new ActionError("upload_file_illegal"));
		}
		return aes;
	}

	public FormFile getUploadFile() {
		return uploadFile;
	}

	public void setUploadFile(FormFile uploadFile) {
		this.uploadFile = uploadFile;
	}

	public int getLink() {
		return link;
	}
	public void setLink(int link) {
		this.link = link;
	}
}
