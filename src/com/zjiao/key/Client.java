package com.zjiao.key;

public class Client
{
    private static SimpleKeyGen keygen;

    public static void main(String[] args)
    {
        try{
			keygen = SimpleKeyGen.getInstance("PO_NUMBER");
			
			for (int i = 0 ; i < 25 ; i++)
			{
				System.out.println("key(" + (i+1)
					+ ")= " + keygen.getNextKey());
			}
        }catch(KeyException e){
        }
    }
}