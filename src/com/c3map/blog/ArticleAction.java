/*
 * �������� 2006-7-30
 *
 * �����������ļ�ģ��Ϊ
 * ���� > ��ѡ�� > Java > �������� > �����ע��
 */
package com.c3map.blog;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.zjiao.ErrorInformation;
import com.zjiao.Logger;
import com.zjiao.auth.UserInfo;
import com.c3map.blog.ArticleDAO;
import com.c3map.blog.ArticleErrInfo;
import com.c3map.blog.Article;
import com.zjiao.struts.ActionExtend;
import com.zjiao.util.StringUtils;


/**
 * @author Administrator
 *
 * ��������������ע�͵�ģ��Ϊ
 * ���� > ��ѡ�� > Java > �������� > �����ע��
 */
public class ArticleAction extends ActionExtend {
	
	/**
	 * �÷������õ�¼һ��ʧ�ܺ����µ�½�ɹ�ҳ����ʾ�հ׵�����
	 * @param mapping ·��ӳ�����
	 * @param form ������
	 * @param request �������
	 * @param response ��Ӧ����
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward doDefault(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception 
	{
		return mapping.findForward("home");
	}


	/**
	 * �������
	 * @param mapping ·��ӳ�����
	 * @param form ������
	 * @param request �������
	 * @param response ��Ӧ����
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward doArticle(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		
			//��ʼ��
			Article art = (Article)form;
			
			ErrorInformation errInfo=new ArticleErrInfo();
		
			//��ȡ��¼�û�����ɫ��Ϣ
			UserInfo loginUser = UserInfo.getLoginUser(request);
			//Role loginRole = Role.getUserRole(request);
		
			if((loginUser==null)||(loginUser.getUserRole()<1))//����û��Ƿ��¼
			  	errInfo.saveError(ArticleErrInfo.NEED_LOGIN);
			else//�����������ݣ����ǲ���Ϊ��
			if(StringUtils.isNull(art.getName())||StringUtils.isNull(art.getBody()))
				 errInfo.saveError(ArticleErrInfo.NEEDED_FIELD_NOT_NULL);
			else{
				//����art����
				  art.setAuthorId(loginUser.getId());
			    art.setOpenType(loginUser.getOptyBlog());
		try{
		      if(loginUser.getUserRole()>0){
			    //���û�г����򱣴浽���ݿ�
			int ret=ArticleDAO.insertBlog(art);
			if(ret<1){
					errInfo.saveError(ArticleErrInfo.ERR_UNKNOWN);
					}
					}
				}catch(SQLException e){
					Logger.Log(Logger.LOG_ERROR,e);
					errInfo.saveError(ArticleErrInfo.DATABASE_ERR);
					}
			}
			
			if(!errInfo.isEmpty()){
				errInfo.saveToRequest("err",request);
				return mapping.findForward("error");
			}
			
			return mapping.findForward("article");
	 }

	/**
	 * ɾ������
	 * @param mapping ·��ӳ�����
	 * @param form ������
	 * @param request �������
	 * @param response ��Ӧ����
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward doDeleteArticle(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		
			//��ʼ��
			Article art = (Article)form;
			Article Art=null;
			ErrorInformation errInfo=new ArticleErrInfo();
			String id=request.getParameter("id");

   	//��ȡ��¼�û�����ɫ��Ϣ
			UserInfo loginUser = UserInfo.getLoginUser(request);
		//System.out.println(loginUser.getId());
			if((loginUser==null)||(loginUser.getUserRole()<1))//����û��Ƿ��¼
				 errInfo.saveError(ArticleErrInfo.NEED_LOGIN);
			else{
				try{
					if(loginUser.getUserRole()>0){
						 ArticleDAO.deleteArt(Integer.parseInt(id),loginUser);
					}
					
				}catch(SQLException e){
					Logger.Log(Logger.LOG_ERROR,e);
					errInfo.saveError(ArticleErrInfo.DATABASE_ERR);
				}
			}
			
			if(!errInfo.isEmpty()){
				errInfo.saveToRequest("err",request);
				return mapping.findForward("error");
			}
			
			return new ActionForward("/blog/blog.jsp");
	}



	public ActionForward doEditArticle(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		
			//��ʼ��
			Article art = (Article)form;
			
			ErrorInformation errInfo=new ArticleErrInfo();
		
			//��ȡ��¼�û�����ɫ��Ϣ
			UserInfo loginUser = UserInfo.getLoginUser(request);
			String id=request.getParameter("artId");
	  if((loginUser==null)||(loginUser.getUserRole()<1))//����û��Ƿ��¼
				errInfo.saveError(ArticleErrInfo.NEED_LOGIN);
		else//�����������ݣ����ǲ���Ϊ��
	  if(StringUtils.isNull(art.getName())||StringUtils.isNull(art.getBody()))
				errInfo.saveError(ArticleErrInfo.NEEDED_FIELD_NOT_NULL);
		else{
				//����art����
				art.setAuthorId(loginUser.getId());
				art.setId(Integer.parseInt(id));
		    art.setOpenType(loginUser.getOptyBlog());
		try{
			 if(loginUser.getUserRole()>0){
						//���û�г����򱣴浽���ݿ�
			int ret=ArticleDAO.editBlog(art);
			if(ret<1){
							errInfo.saveError(ArticleErrInfo.ERR_UNKNOWN);
						}
					}
				
				}catch(SQLException e){
					Logger.Log(Logger.LOG_ERROR,e);
					errInfo.saveError(ArticleErrInfo.DATABASE_ERR);
				}
			}
			
			if(!errInfo.isEmpty()){
				errInfo.saveToRequest("err",request);
				return mapping.findForward("error");
	           }
			return mapping.findForward("editarticle");
	}
	
	/**
	 * ��ӻظ�����
	 * @param mapping ·��ӳ�����
	 * @param form ������
	 * @param request �������
	 * @param response ��Ӧ����
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward doRearticle(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		
			//��ʼ��
		 Rearticle art = (Rearticle)form;
		 ErrorInformation errInfo=new ArticleErrInfo();
		 String articleId=request.getParameter("articleId");
	   String authorId=request.getParameter("authorId");
	   String authorName=request.getParameter("authorName");
	    //��ȡ��¼�û�����ɫ��Ϣ
	   UserInfo loginUser = UserInfo.getLoginUser(request);
		 if((loginUser==null)||(loginUser.getUserRole()<1))//����û��Ƿ��¼
				errInfo.saveError(ArticleErrInfo.NEED_LOGIN);
		 else//�����������ݣ����ǲ���Ϊ��
		 if(StringUtils.isNull(art.getArtBody()))
				errInfo.saveError(ArticleErrInfo.NEEDED_FIELD_NOT_NULL);
			else{
				//����art����
				art.setAuthorId(Integer.parseInt(authorId));
				art.setAuthorName(authorName);
				art.setArticleId(Integer.parseInt(articleId));
			try{
					
					if(loginUser.getUserRole()>0){
						//���û�г����򱣴浽���ݿ�
						int ret=RearticleDAO.insertRearticle(art);
						if(ret<1){
							errInfo.saveError(ArticleErrInfo.ERR_UNKNOWN);
						}
					}
				
				}catch(SQLException e){
					Logger.Log(Logger.LOG_ERROR,e);
					errInfo.saveError(ArticleErrInfo.DATABASE_ERR);
				}
			}
			
			if(!errInfo.isEmpty()){
				errInfo.saveToRequest("err",request);
				return mapping.findForward("error");
			}
			
	     return mapping.findForward("rearticle");
	}
	
	
	/**
		 * ��ӻظ�����
		 * @param mapping ·��ӳ�����
		 * @param form ������
		 * @param request �������
		 * @param response ��Ӧ����
		 * @return ActionForward
		 * @throws Exception
		 */
		public ActionForward doEditRearticle(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
		
				//��ʼ��
				 Rearticle art = (Rearticle)form;
			   ErrorInformation errInfo=new ArticleErrInfo();
	   	   String reId=request.getParameter("reId");
			//��ȡ��¼�û�����ɫ��Ϣ
		     UserInfo loginUser = UserInfo.getLoginUser(request);
			if((loginUser==null)||(loginUser.getUserRole()<1))//����û��Ƿ��¼
					errInfo.saveError(ArticleErrInfo.NEED_LOGIN);
			else//�����������ݣ����ǲ���Ϊ��
			if(StringUtils.isNull(art.getArtBody()))
					errInfo.saveError(ArticleErrInfo.NEEDED_FIELD_NOT_NULL);
			else{
					  art.setId(Integer.parseInt(reId));
			try{
					
			 if(loginUser.getUserRole()>0){
							//���û�г����򱣴浽���ݿ�
						int ret=RearticleDAO.editRearticle(art);
						if(ret<1){
								errInfo.saveError(ArticleErrInfo.ERR_UNKNOWN);
							}
						}
				
					}catch(SQLException e){
						Logger.Log(Logger.LOG_ERROR,e);
						errInfo.saveError(ArticleErrInfo.DATABASE_ERR);
					}
				}
			
				if(!errInfo.isEmpty()){
					errInfo.saveToRequest("err",request);
					return mapping.findForward("error");
				}
			
			 return mapping.findForward("rearticle");
		}
	
	
	/**
	 * ɾ���ظ�����
	 * @param mapping ·��ӳ�����
	 * @param form ������
	 * @param request �������
	 * @param response ��Ӧ����
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward doDeleteRearticle(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		
			//��ʼ��
			Rearticle art = (Rearticle)form;
			ErrorInformation errInfo=new ArticleErrInfo();
			String id=request.getParameter("id");
			String artId=request.getParameter("artId");
		
			//��ȡ��¼�û�����ɫ��Ϣ
			UserInfo loginUser = UserInfo.getLoginUser(request);
			if((loginUser==null)||(loginUser.getUserRole()<1))//����û��Ƿ��¼
				  errInfo.saveError(ArticleErrInfo.NEED_LOGIN);
			else{
				try{
					if(loginUser.getUserRole()>0){
						//���û�г�����ɾ��
				     RearticleDAO.deleteRearticle(Integer.parseInt(id),Integer.parseInt(artId));
						}
				
				}catch(SQLException e){
					Logger.Log(Logger.LOG_ERROR,e);
					errInfo.saveError(ArticleErrInfo.DATABASE_ERR);
				}
			}
			
			if(!errInfo.isEmpty()){
				errInfo.saveToRequest("err",request);
				return mapping.findForward("error");
			}
			
			return new ActionForward("/blog/readblog.jsp");
	}
}
