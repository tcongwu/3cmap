package com.zjiao;

import java.io.*;

/**
 * <p>Title:控制日志信息的输出 </p>
 * <p>Description: 通过设置级别来处理日志信息的输出</p>
 * @author LiBin
 * @version 1.0
 */

public class Logger {
  /**
   * 日志的调试级别
   */
  public static final int LOG_DEBUG = 1;

  /**
   * 日志的注意级别
   */
  public static final int LOG_NOTICE = 2;

  /**
   * 日志的警告级别
   */
  public static final int LOG_WARNING = 3;

  /**
   * 日志的错误级别
   */
  public static final int LOG_ERROR = 4;

  /**
   * 现在的日志输出级别
   */
  protected static int logLevel = LOG_DEBUG;

  /**
   * 设置日志的输出级别
   * @param level 需要设置的日志输出级别
   * @return 原来的日志输出级别
   */
  public static int setDebugLevel(int level) {
    int oldlevel;
    oldlevel = logLevel;
    logLevel = level;
    return oldlevel;
  }

  /**
   * 获取现在的日志输出级别
   * @return 现在的日志输出级别
   */
  public static int getDebugLevel() {
    return logLevel;
  }

  /**
   * 使用OutputStream输出错误信息
   * @param level 需要输出错误信息的日志级别
   * @param out 输出流
   * @param message 需要输出的字符串
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
   * 使用writer输出错误信息
   * @param level 需要输出错误信息的日志级别
   * @param out 输出流
   * @param message 需要输出的字符串
   */
  public static void Log(int level, PrintWriter out, String message) {
    //当错误级别大于输出级别，则输出错误信息
    if (level >= logLevel) {
      out.print(new java.util.Date().toString()+": "+message+"\n");
    }
  }

  /**
   * 输出一个特定级别的错误信息
   * @param level 需要输出错误信息的日志级别
   * @param message 需要输出的字符串
   */
  public static void Log(int level, String message) {
    Log(level, System.out, (message));
  }

  /**
   * 输出一个特定级别的异常信息
   * @param level 需要输出异常的日志级别
   * @param exception 需要输出的异常
   */
  public static void Log(int level, Throwable exception) {
    Log(level, exceptionToString(exception));
  }

  /**
   * 把异常转换成用户能识别的字符串
   * @param exception 需要转换的异常
   * @return String 描述异常的字符串
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
   * 简单的toString方法，把当前输出级别以字符串的形式输出
   * @return String 当前输出级别.
   */
  public String toString() {
    return (new java.lang.Integer(logLevel)).toString();
  }
}