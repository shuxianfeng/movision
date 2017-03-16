package com.movision.utils.ueditor;

import com.movision.utils.Utils;
import com.movision.utils.Watermark;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @Author shuxf
 * @Date 2017/3/16 10:51
 */
public class ImageUtil {

    private static final Logger log = LoggerFactory.getLogger(ImageUtil.class);

    private File srcFile;    //源文件
    private File destFile;    //目标文件
    private double angle;    //旋转角度
    private float quality;
    private double scale;    //伸缩比例
    private int width;    //指定生成图片的宽度
    private int height;    //指定生成图片的高度
    private int givenWidth;            //压缩目标宽度
    private int givenHeight;        //压缩目标高度
    private boolean fixedGivenSize;    //生成的图片是否以给定的大小不变
    private boolean keepRatio;
    private Color bgcolor;    //图片背景
    private ArrayList<Watermark> watermarkArr;

    public ImageUtil(File srcFile) {
        this.srcFile = srcFile;
    }

    /**
     * 初始化图片属性
     */
    private void init() {
        this.destFile = null;
        this.angle = 0d;
        this.quality = 0.75f;
        this.scale = 0d;
        this.width = 0;
        this.height = 0;
        this.fixedGivenSize = false;
        this.keepRatio = false;
        this.bgcolor = Color.BLACK;
        this.watermarkArr = new ArrayList<Watermark>();
    }

    public ImageUtil keepRatio(boolean keepRatio) {
        this.keepRatio = keepRatio;
        return this;
    }

    /**
     * 指定源文件图片
     *
     * @param srcImage {@link File}
     * @return {@link ImageUtil}
     */
    public static ImageUtil fromFile(File srcImage) {
        ImageUtil image = new ImageUtil(srcImage);
        image.init();
        return image;
    }

    /**
     * 指定生成图片的宽度和高度
     *
     * @param width  {@link Integer} 宽度
     * @param height {@link Integer} 高度
     * @return {@link ImageUtil}
     */
    public ImageUtil size(int width, int height) {
        if (width <= 1 || height <= 1) {
            throw new IllegalStateException("width or height value error!");
        }
        this.width = width;
        this.height = height;
        return this;
    }

    /**
     * 压缩图片的质量
     *
     * @param quality {@link Float}
     * @return
     */
    public ImageUtil quality(float quality) {
        this.quality = quality;
        return this;
    }

    /**
     * 指定生成图片的文件
     *
     * @param destFile {@link File}
     */
    public boolean toFile(File destFile) {

        boolean flag = false;
        this.destFile = destFile;
        BufferedImage srcImage = null;
        try {
            // 读取原图片
            log.info("this.srcFile = " + this.srcFile.getPath());
            log.info("是否可读 = " + this.srcFile.canRead());
            srcImage = ImageIO.read(this.srcFile);
            //1 若设置了旋转，则进行旋转
            if (this.angle != 0) {
                try {
                    srcImage = this.rotateImage(srcImage);
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("rotate error!");
                    return flag;
                }
            }
        } catch (IOException e1) {
            e1.printStackTrace();
            System.out.println("read image error!");
            return flag;
        }
        //2 处理尺寸，
        BufferedImage destImage = this.resize(srcImage);
        //3 若固定宽高比例，则保持该比例
        if (this.keepRatio) {
            int w = srcImage.getWidth();
            int h = srcImage.getHeight();
            if (w < this.givenWidth && h < this.givenHeight) {
                this.givenHeight = h;
                this.givenWidth = w;
            }

            destImage = this.keepImageRatio(destImage, this.givenWidth, this.givenHeight);
        }
        //4 处理水印
        if (this.watermarkArr != null) {
            for (Watermark watermark : watermarkArr) {
                destImage = watermark.apply(destImage);
            }
        }
        try {
            this.makeImage(destImage);
            flag = true;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("create image error!");
        }
        return flag;
    }

    /**
     * 设置背景颜色
     *
     * @param bgcolor
     * @return
     */
    public ImageUtil bgcolor(Color bgcolor) {
        this.bgcolor = bgcolor;
        return this;
    }

    /**
     * 生成的图片是否以给定的大小不变
     *
     * @param fixedGivenSize {@link Boolean}
     * @return {@link ImageUtil}
     */
    public ImageUtil fixedGivenSize(boolean fixedGivenSize) {
        this.fixedGivenSize = fixedGivenSize;
        return this;
    }

    private BufferedImage rotateImage(BufferedImage img) throws IOException {
        int width = img.getWidth();
        int height = img.getHeight();

        BufferedImage newImage;

        double[][] newPositions = new double[4][];
        newPositions[0] = this.calculatePosition(0, 0);
        newPositions[1] = this.calculatePosition(width, 0);
        newPositions[2] = this.calculatePosition(0, height);
        newPositions[3] = this.calculatePosition(width, height);

        double minX = Math.min(
                Math.min(newPositions[0][0], newPositions[1][0]),
                Math.min(newPositions[2][0], newPositions[3][0]));
        double maxX = Math.max(
                Math.max(newPositions[0][0], newPositions[1][0]),
                Math.max(newPositions[2][0], newPositions[3][0]));
        double minY = Math.min(
                Math.min(newPositions[0][1], newPositions[1][1]),
                Math.min(newPositions[2][1], newPositions[3][1]));
        double maxY = Math.max(
                Math.max(newPositions[0][1], newPositions[1][1]),
                Math.max(newPositions[2][1], newPositions[3][1]));

        int newWidth = (int) Math.round(maxX - minX);
        int newHeight = (int) Math.round(maxY - minY);

        //newImage = new BufferedImageBuilder(newWidth, newHeight).build();
        newImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g = newImage.createGraphics();
        Utils.setRenderingHint(g);
        if (this.bgcolor != null) {
            g.setPaint(this.bgcolor);
            g.fillRect(0, 0, newWidth, newHeight);
        }
        /*
		 * TODO consider RenderingHints to use. The following are hints which
		 * have been chosen to give decent image quality. In the future, there
		 * may be a need to have a way to change these settings.
		 */
        double w = newWidth / 2.0;
        double h = newHeight / 2.0;

        int centerX = (int) Math.round((newWidth - width) / 2.0);
        int centerY = (int) Math.round((newHeight - height) / 2.0);
        g.rotate(Math.toRadians(angle), w, h);
        g.drawImage(img, centerX, centerY, null);
        g.dispose();
        return newImage;
    }

    /**
     * 重新设置图片大小，
     *
     * @param srcImage
     * @return
     */
    private BufferedImage resize(BufferedImage srcImage) {
        int width = srcImage.getWidth();    //图片的实际宽度
        int height = srcImage.getHeight();    //图片的实际高度
        if (this.width > 0 && this.height > 0) {
            // 本次不走这一步
            if (this.fixedGivenSize) {
                //若生成的图片以给定的大小不变
                this.givenWidth = this.width;
                this.givenHeight = this.height;

                if (!this.keepRatio) {
                    //若没有固定宽高比例，则重新设置宽高
                    width = this.width;
                    height = this.height;
                }
            }

            if (this.keepRatio) {

                int drawWidth = this.width;    //中间变量-宽度
                int drawHeight = this.height;    //中间变量-高度
                double sourceRatio = (double) width / (double) height;    //原图片的比例
                double targetRatio = (double) this.width / (double) this.height;    //目标的比例

                if (Double.compare(sourceRatio, targetRatio) != 0) {
                    if (sourceRatio > targetRatio) {
                        //若原比例大于目标比例，则重新设置高度
                        drawHeight = (int) Math.round(this.width / sourceRatio);
                    } else {
                        //反之，重新设置宽度
                        drawWidth = (int) Math.round(this.height * sourceRatio);
                    }
                }
//				System.out.println("drawHeight="+drawHeight);
//				System.out.println("drawWidth="+drawWidth);

                //必进此分支
                if (!this.fixedGivenSize) {

                    this.givenWidth = drawWidth;
                    this.givenHeight = drawHeight;
//					System.out.println("givenWidth="+givenWidth);
//					System.out.println("givenHeight="+givenHeight);
                }
                width = drawWidth;
                height = drawHeight;
//				System.out.println("width="+width);
//				System.out.println("height="+height);
            }
        } else if (this.scale > 0) {
            width = (int) (width * this.scale);
            height = (int) (height * this.scale);
        } else if (this.width > 0 && this.height == 0) {
            height = this.width * height / width;
            width = this.width;
        } else if (this.width == 0 && this.height > 0) {
            width = this.height * width / height;
            height = this.height;
        }
        if (width <= 1 || height <= 1) {
            throw new IllegalStateException("width or height value error!");
        }
        this.width = width;
        this.height = height;

        this.givenWidth = (this.givenWidth == 0 ? width : this.givenWidth);
        this.givenHeight = (this.givenHeight == 0 ? height : this.givenHeight);

        return Utils.createImage(srcImage, width, height, this.bgcolor);
    }

    /**
     * 保存图片的原比例，并计算原图片
     *
     * @param img          {@link BufferedImage} 原图片
     * @param targetWidth  {@link Integer} 目标宽度
     * @param targetHeight {@link Integer} 目标高度
     * @return 返回计算结果数组
     */
    private BufferedImage keepImageRatio(BufferedImage img, int targetWidth,
                                         int targetHeight) {
        int sourceWidth = img.getWidth();
        int sourceHeight = img.getHeight();
        int x = 0;
        int y = 0;
        int drawWidth = targetWidth;
        int drawHeight = targetHeight;

        double sourceRatio = (double) sourceWidth / (double) sourceHeight;
        double targetRatio = (double) targetWidth / (double) targetHeight;

		/*
		 * If the ratios are not the same, then the appropriate width and height
		 * must be picked.
		 */
        if (Double.compare(sourceRatio, targetRatio) != 0) {
            if (sourceRatio > targetRatio) {
                drawHeight = (int) Math.round(targetWidth / sourceRatio);
            } else {
                drawWidth = (int) Math.round(targetHeight * sourceRatio);
            }
        }
        x = (targetWidth - drawWidth) / 2;
        y = (targetHeight - drawHeight) / 2;
        targetWidth = (targetWidth == 0) ? 1 : targetWidth;
        targetHeight = (targetHeight == 0) ? 1 : targetHeight;
		/*
		BufferedImage resizedImage = Utils.createImage(img, targetWidth,
				targetHeight, this.bgcolor);
				*/
        int type = BufferedImage.TYPE_INT_ARGB;
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, type);
        Graphics2D g = resizedImage.createGraphics();
        Utils.setRenderingHint(g);
        if (this.bgcolor != null) {
            g.setPaint(this.bgcolor);
            g.fillRect(0, 0, targetWidth, targetHeight);
        }
        g.drawImage(img, x, y, drawWidth, drawHeight, null);
        g.dispose();

        return resizedImage;
    }

    /**
     * jdk中imageio包处理图片
     *
     * @param newImage
     * @throws IOException
     */
    public void makeImage(BufferedImage newImage) throws IOException {
        String fileExtension = getExtension(destFile);
        if (fileExtension.equalsIgnoreCase("jpg")
                || fileExtension.equalsIgnoreCase("jpeg")
                || fileExtension.equalsIgnoreCase("bmp")) {
            newImage = this.copy(newImage, BufferedImage.TYPE_INT_RGB);
        }

        ImageWriter imgWriter = ImageIO.getImageWritersByFormatName(fileExtension)
                .next();
        ImageWriteParam imgWriteParam = imgWriter.getDefaultWriteParam();
        if (imgWriteParam.canWriteCompressed()) {
            imgWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            imgWriteParam.setCompressionQuality(this.quality);
        }
        ImageOutputStream outputStream = ImageIO
                .createImageOutputStream(destFile);
        imgWriter.setOutput(outputStream);
        IIOImage outputImage = new IIOImage(newImage, null, null);
        imgWriter.write(null, outputImage, imgWriteParam);
        imgWriter.dispose();
        outputStream.close();
    }

    private double[] calculatePosition(double x, double y) {
        double angle = this.angle;
        angle = Math.toRadians(angle);

        double nx = (Math.cos(angle) * x) - (Math.sin(angle) * y);
        double ny = (Math.sin(angle) * x) + (Math.cos(angle) * y);

        return new double[]{nx, ny};
    }

    /**
     * 返回文件格式
     *
     * @param f {@link File} 文件
     * @return 返回文件格式
     */
    private static String getExtension(File f) {
        String fileName = f.getName();
        if (fileName.indexOf('.') != -1
                && fileName.lastIndexOf('.') != fileName.length() - 1) {
            int lastIndex = fileName.lastIndexOf('.');
            return fileName.substring(lastIndex + 1);
        }
        return null;
    }

    /**
     * Returns a {@link BufferedImage} with the specified image type, where the
     * graphical content is a copy of the specified image.
     *
     * @param img       The image to copy.
     * @param imageType The image type for the image to return.
     * @return A copy of the specified image.
     */
    public BufferedImage copy(BufferedImage img, int imageType) {
        int width = img.getWidth();
        int height = img.getHeight();

        BufferedImage newImage = new BufferedImage(width, height, imageType);
        Graphics g = newImage.createGraphics();

        g.drawImage(img, 0, 0, null);

        g.dispose();

        return newImage;
    }
}
