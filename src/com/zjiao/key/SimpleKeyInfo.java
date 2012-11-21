package com.zjiao.key;

class SimpleKeyInfo{
	
  private int keyMax; //������е����ֵ
  private int keyMin; //������е���Сֵ
  private int nextKey; //��һ����ֵ
  private int max; //��ǰ��ֵ�����ֵ
  private int step; //��ֵ�Ĳ���
  private int poolSize; //����ش�С
  private String keyName; //��ֵ����
  
  private boolean isOK=false;

  /**
   * ���캯��
   * @param keyName ��ֵ����
   */
  public SimpleKeyInfo(String keyName) {
    this.keyName = keyName;
    init();
  }
  
  /**
   * �жϼ�ֵ�Ƿ��ʼ���ɹ�
   * @return boolean �Ƿ�ɹ�
   */
  public boolean isSuccess(){
  	return isOK;
  }

  /**
   * ��ȡ������е����ֵ
   * @return ������е����ֵ
   */
  public int getKeyMax() {
    return keyMax;
  }

  /**
   * ��ȡ������е���Сֵ
   * @return ������е���Сֵ
   */
  public int getKeyMin() {
    return keyMin;
  }
  
  /**
   * ��ȡ��ֵ�����ֵ
   * @return max
   */
  public int getMax(){
  	return max;
  }
  
  /**
   * ��ȡ��ֵ����
   * @return step
   */
  public int getStep(){
  	return step;
  }
  
  /**
   * ��ȡ����ش�С
   * @return poolSize
   */
  public int getPoolSize(){
  	return poolSize;
  }

  /**
   * ��ȡ��һ����ֵ
   * @return ��һ����ֵ
   */
  public synchronized int getNextKey() throws KeyException {
    if (nextKey > keyMax) {
			retrieveNewKey();
    }
    nextKey=nextKey+step;
    return nextKey;
  }

  /**
   * ��ȡ��ֵ���»����
   * @throws KeyException ��ȡ�����г��ֵ��쳣
   */
  private void retrieveNewKey() throws KeyException {
  	if((nextKey+poolSize)>max)
  		throw new KeyException("key is used up.");
  	
  	//��ȡ��ֵ���»����
  	if(KeyInit.getNewKey(poolSize,keyName)){
  		
			keyMin=keyMin+poolSize;
			keyMax=keyMin+poolSize-1;
  		
  	}else{
			throw new KeyException("can't get new pool.");
  	}
  	
  }

  /**
   * ��ʼ����ֵ��Ϣ
   */
  private void init() {
  	//��ȡ��ֵ��Ϣ
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