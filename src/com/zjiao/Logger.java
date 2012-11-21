package com.zjiao;

import java.io.*;

/**
 * <p>Title:������־��Ϣ����� </p>
 * <p>Description: ͨ�����ü�����������־��Ϣ�����</p>
 * @author LiBin
 * @version 1.0
 */

public class Logger {
  /**
   * ��־�ĵ��Լ���
   */
  public static final int LOG_DEBUG = 1;

  /**
   * ��־��ע�⼶��
   */
  public static final int LOG_NOTICE = 2;

  /**
   * ��־�ľ��漶��
   */
  public static final int LOG_WARNING = 3;

  /**
   * ��־�Ĵ��󼶱�
   */
  public static final int LOG_ERROR = 4;

  /**
   * ���ڵ���־�������
   */
  protected static int logLevel = LOG_DEBUG;

  /**
   * ������־���������
   * @param level ��Ҫ���õ���־�������
   * @return ԭ������־�������
   */
  public static int setDebugLevel(int level) {
    int oldlevel;
    oldlevel = logLevel;
    logLevel = level;
    return oldlevel;
  }

  /**
   * ��ȡ���ڵ���־�������
   * @return ���ڵ���־�������
   */
  public static int getDebugLevel() {
    return logLevel;
  }

  /**
   * ʹ��OutputStream���������Ϣ
   * @param level ��Ҫ���������Ϣ����־����
   * @param out �����
   * @param message ��Ҫ������ַ���
   */
  public static void Log(int level, OutputStream out, String message) {
    try {
      if (level >= logLevel) {
        message = new java.util.Date().toString()+ ": " +message+"\n";
        out.write(message.getBytes());
      }
    }
    catch (IOException ex) {
    }
  }

  /**
   * ʹ��writer���������Ϣ
   * @param level ��Ҫ���������Ϣ����־����
   * @param out �����
   * @param message ��Ҫ������ַ���
   */
  public static void Log(int level, PrintWriter out, String message) {
    //�����󼶱����������������������Ϣ
    if (level >= logLevel) {
      out.print(new java.util.Date().toString()+": "+message+"\n");
    }
  }

  /**
   * ���һ���ض�����Ĵ�����Ϣ
   * @param level ��Ҫ���������Ϣ����־����
   * @param message ��Ҫ������ַ���
   */
  public static void Log(int level, String message) {
    Log(level, System.out, (message));
  }

  /**
   * ���һ���ض�������쳣��Ϣ
   * @param level ��Ҫ����쳣����־����
   * @param exception ��Ҫ������쳣
   */
  public static void Log(int level, Throwable exception) {
    Log(level, exceptionToString(exception));
  }

  /**
   * ���쳣ת�����û���ʶ����ַ���
   * @param exception ��Ҫת�����쳣
   * @return String �����쳣���ַ���
   */
  public static String exceptionToString(Throwable exception) {
    if (exception != null) {
      StringWriter writer = new StringWriter();

      exception.printStackTrace(new PrintWriter(writer));
      return (writer.getBuffer().toString());
    }
    else {
      return "Exception is null.";
    }
  }

  /**
   * �򵥵�toString�������ѵ�ǰ����������ַ�������ʽ���
   * @return String ��ǰ�������.
   */
  public String toString() {
    return (new java.lang.Integer(logLevel)).toString();
  }
}