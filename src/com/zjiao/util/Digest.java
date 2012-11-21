package com.zjiao.util;

import java.security.*;

/**
 *
 * <p>Title: ������ϢժҪ</p>
 * <p>Description: ʵ��MD5��SHA</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Tsinghua University</p>
 * @author LiBin
 * @version 1.0
 */
public class Digest {
  static char charmap[] = {
      '4','Q','O','P',
      '2','W','S','X',
      '3','E','D','C',
      '9','R','F','V',
      '5','T','G','B',
      '6','Y','H','N',
      '7','U','J','M',
      '8','I','K','Z'
  };

  /**
   * ȱʡ�Ĺ��캯��
   */
  public Digest() {
  }

  /**
   * �����ַ�����MD5ֵ������������ı�ʾ�ַ���
   * @param msg ������ַ���
   * @return ���MD5ֵ
   */
  static public String MD5(String msg) {
    try {
      MessageDigest md = MessageDigest.getInstance("MD5");
      md.update(msg.getBytes());
      byte[] dst = md.digest();
      return Byte2Hex(dst);
    }
    catch (Exception ex) {
      return null;
    }
  }

  /**
   * �����ַ�����SHAֵ������������ı�ʾ�ַ���
   * @param msg ������ַ���
   * @return ���SHAֵ
   */
  static public String SHA(String msg) {
    try {
      MessageDigest md = MessageDigest.getInstance("SHA");
      md.update(msg.getBytes());
      byte[] dst = md.digest();
      return Byte2Hex(dst);
    }
    catch (Exception ex) {
      return null;
    }
  }

  /**
   * �����ַ�����MD5ֵ������������ı�ʾ�ַ���
   * @param msg ������ַ���
   * @return ���MD5ֵ
   */
  static public String MD5_Id(String msg) {
    try {
      MessageDigest md = MessageDigest.getInstance("MD5");
      md.update(msg.getBytes());
      byte[] dst = md.digest();
      return EESEncode(dst).substring(0, 12);
    }
    catch (Exception ex) {
      return null;
    }
  }

  /**
   * �����ַ�����SHAֵ������������ı�ʾ�ַ���
   * @param msg ������ַ���
   * @return ���SHAֵ
   */
  static public String SHA_Id(String msg) {
    try {
      MessageDigest md = MessageDigest.getInstance("SHA");
      md.update(msg.getBytes());
      byte[] dst = md.digest();
      return EESEncode(dst).substring(0, 28);
    }
    catch (Exception ex) {
      return null;
    }
  }

  /**
   * encode byte array to a string
   * @param ba ����Ķ�������������
   * @return ��������Ժ���ַ���
   */
  static public String EESEncode(byte[] ba) throws Exception
  {
    /**
     * @todo: Ŀǰֻ����������ַ���Ϊ5�ı��������������Ĳ��ֱ���������Ҫ����ʣ���ֽ�
     */
    StringBuffer buffer = new StringBuffer();
    int i = 0;
    int len = ba.length;
    byte a,b,c,d,e;
    for(int count=len/5; count>0; count--)
    {
      a = ba[i++];
      b = ba[i++];
      c = ba[i++];
      d = ba[i++];
      e = ba[i++];
      buffer.append(charmap[(a >>> 3) & 0x1F]);
      buffer.append(charmap[((a << 2) & 0x1C) + ((b >>> 6) & 0x03)]);
      buffer.append(charmap[(b >>> 1) & 0x1F]);
      buffer.append(charmap[((b << 4) & 0x10) + ((c >>> 4) & 0x0F)]);
      buffer.append(charmap[((c << 1) & 0x1E) + ((d >>> 7) & 0x01)]);
      buffer.append(charmap[(d >>> 2) & 0x1F]);
      buffer.append(charmap[((d << 3) & 0x18) + ((e >>> 5) & 0x07)]);
      buffer.append(charmap[e & 0x1F]);
    }
    return buffer.toString();
  }

  /**
   * ��byte[]����ת��Ϊ16������ʾ���ַ���
   * @param ba byte[]����
   * @return String
   */
  static private String Byte2Hex(byte[] ba) {
    try {
      int len = ba.length;
      char[] res = new char[len * 2];
      int tmp, i, j, k;
      for (i = 0; i < len; i++) {
        tmp = (int) ba[i];
        if (tmp < 0) {
          tmp += 256;
        }
        j = tmp / 16;
        k = tmp % 16;
        for (j = 0, k = tmp / 16; j < 2; j++, k = tmp % 16) {
          if (k < 10) {
            res[i * 2 + j] = (char) (k + 48);
          }
          else {
            res[i * 2 + j] = (char) (k + 87);
          }
        }
      }
      return new String(res);
    }
    catch (Exception ex) {
      return null;
    }
  }

}