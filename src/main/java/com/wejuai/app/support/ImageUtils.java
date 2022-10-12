package com.wejuai.app.support;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author ZM.Wang
 */
public class ImageUtils {

    public static void fusionImage(InputStream background, InputStream back, InputStream up, OutputStream out, Integer rotate) throws IOException {
        BufferedImage backImage = editSize(ImageIO.read(back), 250, 250);
        BufferedImage upImage = editSize(ImageIO.read(up), 250, 250);
        BufferedImage backgroundImage = ImageIO.read(background);
        int width = backgroundImage.getWidth();
        int height = backgroundImage.getHeight();
        //如果有旋转度数就旋转
        BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TRANSLUCENT);
        if (rotate != null && rotate != 0 && rotate < 360) {
            upImage = rotate(upImage, rotate);
        }
        //3、开始绘图
        Graphics2D g2 = newImage.createGraphics();
        g2.drawImage(backgroundImage, 0, 0, null);
        g2.drawImage(backImage, 75, 25, null);
        g2.drawImage(upImage, 75, 25, null);
        g2.dispose();
        ImageIO.write(newImage, "png", out);
    }

    public static BufferedImage rotate(BufferedImage original, int ratio) {
        int width = original.getWidth();
        int height = original.getHeight();
        //设置生成图片的宽*高，色彩度
        BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TRANSLUCENT);
        //创建画布
        Graphics2D g2 = newImage.createGraphics();
        //根据点位旋转
        g2.rotate(Math.toRadians(ratio), width / 2D, height / 2D);
        //写入
        g2.drawImage(original, 0, 0, null);
        g2.dispose();
        return newImage;
    }

    public static BufferedImage editSize(BufferedImage original, int newWidth, int newHeight) {
        BufferedImage bi = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        //创建画布
        Graphics2D g2 = bi.createGraphics();
        //设置图片透明
        bi = g2.getDeviceConfiguration().createCompatibleImage(newWidth, newHeight, Transparency.TRANSLUCENT);
        //重新创建画布
        g2 = bi.createGraphics();
        //画图
        g2.drawImage(original, 0, 0, newWidth, newHeight, null);
        //关闭资源
        g2.dispose();
        return bi;
    }

}
