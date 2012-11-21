package com.zjiao.key;

/**
 * ���м����ɹ����е�Ӧ���쳣
 * @author Lee Bin
 */
public class KeyException extends Exception {

	/**
	 * default
	 */
	public KeyException() {
		super();
	}

	/**
	 * @param message
	 */
	public KeyException(String message) {
		super(message);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public KeyException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param cause
	 */
	public KeyException(Throwable cause) {
		super(cause);
	}

}