package com.zjiao.key;

import java.util.HashMap;

public class SimpleKeyGen{
  //���屣�����ֵ�Ĺ�ϣ��
  private static HashMap kengens = new HashMap(10);
  //���屣�浱ǰ��ֵ����
  private SimpleKeyInfo keyinfo;

  /**
   * Ĭ�ϵĹ��캯��
   */
  private SimpleKeyGen() {
  }

  /**
   * ���м�ֵ���ƵĹ��캯��
   * @param keyName ��ֵ����
   */
  private SimpleKeyGen(String keyName) {
    keyinfo = new SimpleKeyInfo(keyName);
  }
  
  /**
   * ��ȡ��ֵ��Ϣ
   * @return SimpleKeyInfo
   */
  private SimpleKeyInfo getKeyinfo(){
  	return keyinfo;
  }

  /**
   * ����ģʽ�����ڸ��ݼ�ֵ���ƻ�ȡ��Ӧ��ʵ��
   * @param keyName ��ֵ����
   * @return ���ֵ���������SimpleKeyGen��һ��ʵ��
   */
  public static synchronized SimpleKeyGen getInstance(String keyName) throws KeyException {
    SimpleKeyGen keygen;
    if (kengens.containsKey(keyName)) {
      keygen = (SimpleKeyGen) kengens.get(keyName);
    }else {
      keygen = new SimpleKeyGen(keyName);
      if(keygen.getKeyinfo().isSuccess()){
				kengens.put(keyName,keygen);
      }else{
      	keygen=null;
				throw new KeyException("create key generator failed.");
      }
    }
    return keygen;
  }

  /**
   * ��ȡ��һ����ֵ
   * @return ��һ����ֵ
   */
  public int getNextKey() throws KeyException {
    return keyinfo.getNextKey();
  }
}