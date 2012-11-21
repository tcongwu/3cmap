package com.zjiao.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import org.apache.commons.lang.RandomStringUtils;
/**
 * �漴ͼƬ������
 * ���������û�ע��ʱ����Ҫ�û�����ͼƬ���ݽ�����д��ȷ�󷽿�ע��
 * @author Lee Bin
 */
public class RandomImageGenerator {
    
    public static String random() {
        return RandomStringUtils.randomNumeric(4);
    }
    /**
     * ����Ҫ�����������ͼƬ,����Ϊ��ɫ,�����С14,������ɫ��ɫ����
     * @param num	Ҫ���ɵ�����
     * @param out	�����
     * @throws IOException
     */
    public static void render(String num, OutputStream out) throws IOException{
        if(num.getBytes().length>4)
            throw new IllegalArgumentException("The length of param num cannot exceed 4.");
        int width = 40;
        int height = 15;
        BufferedImage bi = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);        
        Graphics2D g = (Graphics2D)bi.getGraphics();
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0,0,width,height);
        Font mFont = new Font("Arial Black", Font.ITALIC, 14);
        g.setFont(mFont);
        g.setColor(Color.RED);
        g.drawString(num,2,13);
        ImageIO.write(bi,"jpg",out);
    }
}
