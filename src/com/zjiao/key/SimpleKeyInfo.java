package com.zjiao.key;

class SimpleKeyInfo{
	
  private int keyMax; //缓冲池中的最大值
  private int keyMin; //缓冲池中的最小值
  private int nextKey; //下一个键值
  private int max; //当前键值的最大值
  private int step; //增值的步幅
  private int poolSize; //缓冲池大小
  private String keyName; //键值名称
  
  private boolean isOK=false;

  /**
   * 构造函数
   * @param keyName 键值名称
   */
  public SimpleKeyInfo(String keyName) {
    this.keyName = keyName;
    init();
  }
  
  /**
   * 判断键值是否初始化成功
   * @return boolean 是否成功
   */
  public boolean isSuccess(){
  	return isOK;
  }

  /**
   * 获取缓冲池中的最大值
   * @return 缓冲池中的最大值
   */
  public int getKeyMax() {
    return keyMax;
  }

  /**
   * 获取缓冲池中的最小值
   * @return 缓冲池中的最小值
   */
  public int getKeyMin() {
    return keyMin;
  }
  
  /**
   * 获取键值的最大值
   * @return max
   */
  public int getMax(){
  	return max;
  }
  
  /**
   * 获取键值增幅
   * @return step
   */
  public int getStep(){
  	return step;
  }
  
  /**
   * 获取缓冲池大小
   * @return poolSize
   */
  public int getPoolSize(){
  	return poolSize;
  }

  /**
   * 获取下一个键值
   * @return 下一个键值
   */
  public synchronized int getNextKey() throws KeyException {
    if (nextKey > keyMax) {
			retrieveNewKey();
    }
    nextKey=nextKey+step;
    return nextKey;
  }

  /**
   * 获取键值的新缓冲池
   * @throws KeyException 获取过程中出现的异常
   */
  private void retrieveNewKey() throws KeyException {
  	if((nextKey+poolSize)>max)
  		throw new KeyException("key is used up.");
  	
  	//获取键值的新缓冲池
  	if(KeyInit.getNewKey(poolSize,keyName)){
  		
			keyMin=keyMin+poolSize;
			keyMax=keyMin+poolSize-1;
  		
  	}else{
			throw new KeyException("can't get new pool.");
  	}
  	
  }

  /**
   * 初始化键值信息
   */
  private void init() {
  	//获取键值信息
  	int[] info=KeyInit.getKeyInfo(keyName);
  	if((info!=null)&&(info.length==5)&&KeyInit.getNewKey(info[KeyInit.POOLSIZE],keyName)){
			//System.out.println(info[0]+","+info[1]+","+info[2]+","+info[3]+","+info[4]);
			this.max=info[KeyInit.MAX];
			this.nextKey=info[KeyInit.CURVALUE];
			this.step=info[KeyInit.STEP];
			this.poolSize=info[KeyInit.POOLSIZE];
			
			this.keyMin=info[KeyInit.CURVALUE];
			this.keyMax=this.keyMin+poolSize-1;
			
			isOK=true;
   	}
   	
   	if((step>poolSize)||(keyMax>max)){
			isOK=false;
   	}
  }
}