package com.zjiao.struts;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * 实现了事件映射扩展
 * @author Lee Bin
 */
public abstract class ActionExtend extends Action {

	/**
	 * Action类的入口，用于根据不同的提交按钮名称执行相对应的方法
	 * 按钮的名称是eventSubmit_Xxxx，对应执行的方法是doXxxx
	 */
	public ActionForward execute(ActionMapping mapping, 
								 ActionForm form,
								 HttpServletRequest req, 
								 HttpServletResponse res) throws Exception 
	{
		String param = null;
		String value = null;
		
		for (Enumeration params = req.getParameterNames(); params.hasMoreElements();) {
			String t_param = (String) params.nextElement();
			if (t_param.startsWith(SUBMIT_BUTTON_PREFIX)) {
				value = req.getParameter(t_param);
				param = METHOD_PREFIX + t_param.substring(SUBMIT_BUTTON_PREFIX.length());
				break;
			}
		}

		if (param == null)
			param = "doDefault";
		
		try{
			return callActionMethod(mapping, form, req, res, param, value);
		}catch(InvocationTargetException e){
			throw (Exception)e.getCause();
		}
	}

	/**
	 * 调用事件处理方法
	 * @param mapping
	 * @param form
	 * @param req
	 * @param res
	 * @param methodName
	 * @param value
	 * @return ActionForward
	 * @throws Exception
	 */
	protected ActionForward callActionMethod(ActionMapping mapping,
											 ActionForm form, 
											 HttpServletRequest req, 
											 HttpServletResponse res,
											 String methodName, 
											 String value) throws Exception 
	{
		Method doMethod = null;
		Object params[] = (Object[]) null;
		for (int i = 0; i < methodParams.length;)
			try {
				doMethod = getClass().getDeclaredMethod(methodName,methodParams[i]);
				if (doMethod.getParameterTypes().length == 4){
					params = (new Object[] { mapping, form, req, res });
					break;
				}
				if (doMethod.getParameterTypes().length != 5)
					continue;
				params = new Object[]{mapping,form,req,res,value};
				break;
			} catch (NoSuchMethodException excp) {
				i++;
			}

		if (doMethod != null) {
			if (paramMapping(doMethod.getName()))
				BeanUtils.populate(this, req.getParameterMap());
			Object ret = doMethod.invoke(this, params);
			if (doMethod.getReturnType().equals(ActionForward.class))
				return (ActionForward) ret;
			if (doMethod.getReturnType().equals(String.class))
				return new ActionForward((String) ret);
		}
		return null;
	}

	/**
	 * 返回是否将表单的域映射到Action类的属性上
	 * 子类可以覆盖该方法已启用自动映射功能
	 * 建议还是使用Struts的Formbean更符合设计模式
	 * @param method
	 * @return boolean
	 */
	protected boolean paramMapping(String method) {
		return false;
	}

	public static final String SUBMIT_BUTTON_PREFIX = "eventSubmit_";
	public static final String METHOD_PREFIX = "do";
	
	private static final Class method1Params[];
	private static final Class method2Params[];
	private static final Class method3Params[];
	private static final Class method4Params[];
	private static final Class methodParams[][];
	
	static {
		method1Params = (new Class[] {
				org.apache.struts.action.ActionMapping.class,
				org.apache.struts.action.ActionForm.class,
				javax.servlet.ServletRequest.class,
				javax.servlet.ServletResponse.class });
		method2Params = (new Class[] {
				org.apache.struts.action.ActionMapping.class,
				org.apache.struts.action.ActionForm.class,
				javax.servlet.ServletRequest.class,
				javax.servlet.ServletResponse.class, java.lang.String.class });
		method3Params = (new Class[] {
				org.apache.struts.action.ActionMapping.class,
				org.apache.struts.action.ActionForm.class,
				javax.servlet.http.HttpServletRequest.class,
				javax.servlet.http.HttpServletResponse.class });
		method4Params = (new Class[] {
				org.apache.struts.action.ActionMapping.class,
				org.apache.struts.action.ActionForm.class,
				javax.servlet.http.HttpServletRequest.class,
				javax.servlet.http.HttpServletResponse.class,
				java.lang.String.class });
		methodParams = (new Class[][] { method1Params, method2Params,
				method3Params, method4Params });
	}
}