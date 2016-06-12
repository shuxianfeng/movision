package com.zhuhuibao.utils.captcha.util;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageFilter;

public class ImageUtil {
    public static void applyFilter(BufferedImage img, ImageFilter filter) {
//		FilteredImageSource src = new FilteredImageSource(img.getSource(), filter);
        Image fImg = Toolkit.getDefaultToolkit().createImage(img.getSource());
        Graphics2D g = img.createGraphics();
        g.drawImage(fImg, 0, 0, null, null);
        g.dispose();
    }
}
