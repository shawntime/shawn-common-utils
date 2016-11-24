package com.shawntime.common.utils;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

public class ImageUtils {
	   
	    public static boolean thumbnailImage(File imgFile, int w, int h,String path){
	            try {
	                // ImageIO 支持的图片类型 : [BMP, bmp, jpg, JPG, wbmp, jpeg, png, PNG, JPEG, WBMP, GIF, gif]
	                String types = Arrays.toString(ImageIO.getReaderFormatNames());
	                String suffix = null;
	                // 获取图片后缀
	                if(imgFile.getName().indexOf(".") > -1) {
	                    suffix = imgFile.getName().substring(imgFile.getName().lastIndexOf(".") + 1);
	                }// 类型和图片后缀全部小写，然后判断后缀是否合法
	                if(suffix == null || types.toLowerCase().indexOf(suffix.toLowerCase()) < 0){
	                    return false;
	                }
	                Image img = ImageIO.read(imgFile);
	                //BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
	                BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
	                Graphics g = bi.getGraphics();
	                g.drawImage(img, 0, 0, w, h, Color.LIGHT_GRAY, null);
	                g.dispose();
	                // 将图片保存在原目录并加上前缀
	                
	                ImageIO.write(bi, getFormatName(imgFile), new File(path));
	                return true;
	            } catch (IOException e) {
	            	e.printStackTrace();
	            	return false;
	            }
	    }
	    /**
	     * 图片切成正方形，根据长宽最短
	     * @param sourcePath  原图片地址
	     * @param descpath 目标图片地址
	     * @param type 1 表示居中
	     * @return true 表示成功 false表示失败
	     */
	    public static boolean cut(String sourcePath, String descpath,int type) {  
	    	int b=0;
			int x=0;
			int y=0;
	        if(type==1){
	        	BufferedImage catsource=getBufferedImage(new File(sourcePath));
	        	int w=catsource.getWidth();
				int h=catsource.getHeight();
				
				if(w>h){
					b=h;
					x=(w-b)/2;
				}else{
					b=w;
					y=(h-b)/2;
				}
	        }
	        FileInputStream is = null;  
	        ImageInputStream iis = null;  
	        try {  
	            is = new FileInputStream(sourcePath);  
	            String fileSuffix = sourcePath.substring(sourcePath  
	                    .lastIndexOf(".") + 1);  
	            Iterator<ImageReader> it = ImageIO  
	                    .getImageReadersByFormatName(fileSuffix);  
	            ImageReader reader = it.next();  
	            iis = ImageIO.createImageInputStream(is);  
	            reader.setInput(iis, true);  
	            ImageReadParam param = reader.getDefaultReadParam();  
	            Rectangle rect = new Rectangle(x, y, b, b);
	            param.setSourceRegion(rect);
	            BufferedImage bi = reader.read(0, param);  
	            ImageIO.write(bi, getFormatName(new File(sourcePath)), new File(descpath)); 
	            return true;
	        } catch (Exception ex) {  
	            ex.printStackTrace();  
	        } finally {  
	            if (is != null) {  
	                try {  
	                    is.close();  
	                } catch (IOException e) {  
	                    e.printStackTrace();  
	                }  
	                is = null;  
	            }  
	            if (iis != null) {  
	                try {  
	                    iis.close();  
	                } catch (IOException e) {  
	                    e.printStackTrace();  
	                }  
	                iis = null;  
	            }  
	        }  
	      return false;
	    } 
	    public static BufferedImage getBufferedImage(File file){
	    	BufferedImage bi=null;
			try {
				bi = ImageIO.read(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
	    	return bi;
	    }
	    public static boolean download(String urlString,String filepath){
	    	try {
	    		//递归创建文件夹
	    		mkDir(new File(filepath).getParentFile());
	    		 // 构造URL
		        URL url = new URL(urlString);
		        // 打开连接
		        URLConnection con = url.openConnection();
		        // 输入流
		        InputStream is = con.getInputStream();
		        // 1K的数据缓冲
		        byte[] bs = new byte[1024];
		        // 读取到的数据长度
		        int len;
		        // 输出的文件流
		        OutputStream os = new FileOutputStream(filepath);
		        // 开始读取
		        while ((len = is.read(bs)) != -1) {
		          os.write(bs, 0, len);
		        }
		        // 完毕，关闭所有链接
		        os.close();
		        is.close();
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
	    	return true;
	    	
	    }
	    public static BufferedImage getBufferedImage(String url){
	    	BufferedImage bi=null;
			try {
				bi = ImageIO.read(new URL(url));
			} catch (IOException e) {
				e.printStackTrace();
			}
	    	return bi;
	    }
	    public static String getFormatName(Object o) {
	        try {
	            // Create an image input stream on the image
	            ImageInputStream iis = ImageIO.createImageInputStream(o);
	            // Find all image readers that recognize the image format
	            Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);
	            if (!iter.hasNext()) {
	                // No readers found
	                return null;
	            }
	            // Use the first reader
	            ImageReader reader = iter.next();
	    
	            // Close stream
	            iis.close();
	    
	            // Return the format name
	            return reader.getFormatName();
	        } catch (IOException e) {
	            //
	        }
	        
	        // The image could not be read
	        return null;
	    }
	    public static void mkDir(File file){
//	    	  if(file.getParentFile().exists()){
//	    	   file.mkdir();
//
//	    	  }else{
//	    	   mkDir(file.getParentFile());
//	    	   file.mkdir();
//	    	  }
	    	//批量创建文件夹
	    	 file.mkdirs();
	    }

}
