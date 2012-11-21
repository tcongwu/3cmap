package com.zjiao;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Lee Bin
 *
 * 错误信息处理接口
 * 所有子模块的错误信息处理类都实现该接口
 */
public interface ErrorInformation {
	
	/**
	 * 保存错误信息
	 * @param err
	 */
	public void saveError(int err);
	
	/**
	 * 判断是否存在错误信息
	 * @return boolean 是否存在
	 */
	public boolean isEmpty();
	
	/**
	 * 获取错误内容
	 * @return String 错误内容
	 */
	public String getInformation();

	/**
	 * 根据错误号获取错误内容
	 * @param err 错误编号
	 * @return String 错误内容
	 */
	public String getInformation(int err);
	
	/**
	 * 把错误信息存入请求对象中
	 * @param req
	 */
	public void saveToRequest(String name, HttpServletRequest req);
	
}
