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
 * 随即图片生成器
 * 该类用于用户注册时候需要用户根据图片内容进行填写正确后方可注册
 * @author Lee Bin
 */
public class RandomImageGenerator {
    
    public static String random() {
        return RandomStringUtils.randomNumeric(4);
    }
    /**
     * 根据要求的数字生成图片,背景为白色,字体大小14,字体颜色黑色粗体
     * @param num	要生成的数字
     * @param out	输出流
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
