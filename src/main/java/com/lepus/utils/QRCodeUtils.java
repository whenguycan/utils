package com.lepus.utils;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

/**
 * 二维码
 * @author whenguycan
 * @date 2017年2月4日 下午4:37:05
 */
public class QRCodeUtils {

	public static void main(String[] args){
		String realPath = "C:\\Users\\Administrator\\Desktop\\app-release-233.png";
		String logoPath = "";
		QRCodeUtils.createImageFile("http://192.168.1.233:8080/jcsj/upload/app-release.apk", 180, 180, new File(logoPath), true,new File(realPath));
		
	}
	
	/**
	 * 写入二维码到文件
	 * @return
	 */
	public static BufferedImage createImageFile(String contents,int width,int height,File logo,boolean needCompress,File target){
		BufferedImage image = createImage(contents, width, height, logo, needCompress);
		try {
			ImageIO.write(image, "png", target);
			return image;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 获取二维码图片文件流
	 * @return
	 */
	public static InputStream createImageStream(String contents,int width,int height,File logo,boolean needCompress){
		BufferedImage image = createImage(contents, width, height, logo, needCompress);
		try {
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			ImageIO.write(image,"png",os);
			return new ByteArrayInputStream(os.toByteArray());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 生成二维码图片文件
	 * @return
	 */
	private static BufferedImage createImage(String contents,int width,int height,File logo,boolean needCompress){
		try {
			Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
			hints.put(EncodeHintType.CHARACTER_SET,"UTF-8");					//设置字符编码
			hints.put(EncodeHintType.ERROR_CORRECTION,ErrorCorrectionLevel.Q);	//设置纠错级别，默认为L
			BitMatrix matrix = new MultiFormatWriter().encode(contents, BarcodeFormat.QR_CODE, width, width, hints);
			BufferedImage image = new BufferedImage(width, width, BufferedImage.TYPE_INT_RGB);
			for(int x=0;x<width;x++){
				for(int y=0;y<width;y++){
					image.setRGB(x,y,matrix.get(x,y) ? 0xFF000000 : 0xFFFFFFFF);
				}
			}
			insertImage(image,logo,needCompress);
			return image;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	//加入logo
	private static void insertImage(BufferedImage image,File logo,boolean needCompress){
		if(logo == null){
			return;
		}
		try {
			Image src = ImageIO.read(logo);
			int width = src.getWidth(null);
			int height = src.getHeight(null);
			if(needCompress){
				width = image.getWidth() / 4;
				height = image.getHeight() / 4;
				Image img = src.getScaledInstance(width, height, Image.SCALE_SMOOTH);
				Graphics g = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB).getGraphics();
				g.drawImage(img, 0, 0, null);
				g.dispose();
				src = img;
			}
			Graphics2D graph = image.createGraphics();
			int x = (image.getWidth() - width) / 2;
			int y = (image.getHeight() - height) / 2;
			graph.drawImage(src, x, y, width, height, null);	//绘制图像
			Shape shape = new RoundRectangle2D.Float(x, y, width, height, 8, 8);
			graph.setStroke(new BasicStroke(3f));
			graph.draw(shape);		//绘制边框
			graph.dispose();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
