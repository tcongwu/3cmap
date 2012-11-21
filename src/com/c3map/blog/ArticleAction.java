/*
 * 创建日期 2006-7-30
 *
 * 更改所生成文件模板为
 * 窗口 > 首选项 > Java > 代码生成 > 代码和注释
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
 * 更改所生成类型注释的模板为
 * 窗口 > 首选项 > Java > 代码生成 > 代码和注释
 */
public class ArticleAction extends ActionExtend {
	
	/**
	 * 该方法放置登录一次失败后重新登陆成功页面显示空白的问题
	 * @param mapping 路径映射对象
	 * @param form 表单对象
	 * @param request 请求对象
	 * @param response 响应对象
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
	 * 添加文章
	 * @param mapping 路径映射对象
	 * @param form 表单对象
	 * @param request 请求对象
	 * @param response 响应对象
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward doArticle(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		
			//初始化
			Article art = (Article)form;
			
			ErrorInformation errInfo=new ArticleErrInfo();
		
			//获取登录用户及角色信息
			UserInfo loginUser = UserInfo.getLoginUser(request);
			//Role loginRole = Role.getUserRole(request);
		
			if((loginUser==null)||(loginUser.getUserRole()<1))//检查用户是否登录
			  	errInfo.saveError(ArticleErrInfo.NEED_LOGIN);
			else//检查主题和内容，它们不能为空
			if(StringUtils.isNull(art.getName())||StringUtils.isNull(art.getBody()))
				 errInfo.saveError(ArticleErrInfo.NEEDED_FIELD_NOT_NULL);
			else{
				//完善art对象
				  art.setAuthorId(loginUser.getId());
			    art.setOpenType(loginUser.getOptyBlog());
		try{
		      if(loginUser.getUserRole()>0){
			    //如果没有出错，则保存到数据库
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
	 * 删除文章
	 * @param mapping 路径映射对象
	 * @param form 表单对象
	 * @param request 请求对象
	 * @param response 响应对象
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward doDeleteArticle(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		
			//初始化
			Article art = (Article)form;
			Article Art=null;
			ErrorInformation errInfo=new ArticleErrInfo();
			String id=request.getParameter("id");

   	//获取登录用户及角色信息
			UserInfo loginUser = UserInfo.getLoginUser(request);
		//System.out.println(loginUser.getId());
			if((loginUser==null)||(loginUser.getUserRole()<1))//检查用户是否登录
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
		
			//初始化
			Article art = (Article)form;
			
			ErrorInformation errInfo=new ArticleErrInfo();
		
			//获取登录用户及角色信息
			UserInfo loginUser = UserInfo.getLoginUser(request);
			String id=request.getParameter("artId");
	  if((loginUser==null)||(loginUser.getUserRole()<1))//检查用户是否登录
				errInfo.saveError(ArticleErrInfo.NEED_LOGIN);
		else//检查主题和内容，它们不能为空
	  if(StringUtils.isNull(art.getName())||StringUtils.isNull(art.getBody()))
				errInfo.saveError(ArticleErrInfo.NEEDED_FIELD_NOT_NULL);
		else{
				//完善art对象
				art.setAuthorId(loginUser.getId());
				art.setId(Integer.parseInt(id));
		    art.setOpenType(loginUser.getOptyBlog());
		try{
			 if(loginUser.getUserRole()>0){
						//如果没有出错，则保存到数据库
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
	 * 添加回复文章
	 * @param mapping 路径映射对象
	 * @param form 表单对象
	 * @param request 请求对象
	 * @param response 响应对象
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward doRearticle(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		
			//初始化
		 Rearticle art = (Rearticle)form;
		 ErrorInformation errInfo=new ArticleErrInfo();
		 String articleId=request.getParameter("articleId");
	   String authorId=request.getParameter("authorId");
	   String authorName=request.getParameter("authorName");
	    //获取登录用户及角色信息
	   UserInfo loginUser = UserInfo.getLoginUser(request);
		 if((loginUser==null)||(loginUser.getUserRole()<1))//检查用户是否登录
				errInfo.saveError(ArticleErrInfo.NEED_LOGIN);
		 else//检查主题和内容，它们不能为空
		 if(StringUtils.isNull(art.getArtBody()))
				errInfo.saveError(ArticleErrInfo.NEEDED_FIELD_NOT_NULL);
			else{
				//完善art对象
				art.setAuthorId(Integer.parseInt(authorId));
				art.setAuthorName(authorName);
				art.setArticleId(Integer.parseInt(articleId));
			try{
					
					if(loginUser.getUserRole()>0){
						//如果没有出错，则保存到数据库
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
		 * 添加回复文章
		 * @param mapping 路径映射对象
		 * @param form 表单对象
		 * @param request 请求对象
		 * @param response 响应对象
		 * @return ActionForward
		 * @throws Exception
		 */
		public ActionForward doEditRearticle(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
		
				//初始化
				 Rearticle art = (Rearticle)form;
			   ErrorInformation errInfo=new ArticleErrInfo();
	   	   String reId=request.getParameter("reId");
			//获取登录用户及角色信息
		     UserInfo loginUser = UserInfo.getLoginUser(request);
			if((loginUser==null)||(loginUser.getUserRole()<1))//检查用户是否登录
					errInfo.saveError(ArticleErrInfo.NEED_LOGIN);
			else//检查主题和内容，它们不能为空
			if(StringUtils.isNull(art.getArtBody()))
					errInfo.saveError(ArticleErrInfo.NEEDED_FIELD_NOT_NULL);
			else{
					  art.setId(Integer.parseInt(reId));
			try{
					
			 if(loginUser.getUserRole()>0){
							//如果没有出错，则保存到数据库
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
	 * 删除回复文章
	 * @param mapping 路径映射对象
	 * @param form 表单对象
	 * @param request 请求对象
	 * @param response 响应对象
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward doDeleteRearticle(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		
			//初始化
			Rearticle art = (Rearticle)form;
			ErrorInformation errInfo=new ArticleErrInfo();
			String id=request.getParameter("id");
			String artId=request.getParameter("artId");
		
			//获取登录用户及角色信息
			UserInfo loginUser = UserInfo.getLoginUser(request);
			if((loginUser==null)||(loginUser.getUserRole()<1))//检查用户是否登录
				  errInfo.saveError(ArticleErrInfo.NEED_LOGIN);
			else{
				try{
					if(loginUser.getUserRole()>0){
						//如果没有出错，则删除
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
