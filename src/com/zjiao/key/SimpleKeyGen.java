package com.zjiao.key;

import java.util.HashMap;

public class SimpleKeyGen{
  //定义保存各键值的哈希表
  private static HashMap kengens = new HashMap(10);
  //定义保存当前键值变量
  private SimpleKeyInfo keyinfo;

  /**
   * 默认的构造函数
   */
  private SimpleKeyGen() {
  }

  /**
   * 带有键值名称的构造函数
   * @param keyName 键值名称
   */
  private SimpleKeyGen(String keyName) {
    keyinfo = new SimpleKeyInfo(keyName);
  }
  
  /**
   * 获取键值信息
   * @return SimpleKeyInfo
   */
  private SimpleKeyInfo getKeyinfo(){
  	return keyinfo;
  }

  /**
   * 多例模式，用于根据键值名称获取相应的实例
   * @param keyName 键值名称
   * @return 与键值名称相符的SimpleKeyGen的一个实例
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
   * 获取下一个键值
   * @return 下一个键值
   */
  public int getNextKey() throws KeyException {
    return keyinfo.getNextKey();
  }
}