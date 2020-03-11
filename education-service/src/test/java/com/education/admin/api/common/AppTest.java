package com.education.admin.api.common;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;

/**
 * Unit test for simple App.
 */
public class AppTest

{

    /**
     21      * 将网络图片进行Base64位编码
     22      *
     23      * @param imgUrl
     24      *            图片的url路径，如http://.....xx.jpg
     25      * @return
     26      */
     public static String encodeImgageToBase64(URL imageUrl) {// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
             ByteArrayOutputStream outputStream = null;
             try {
         BufferedImage bufferedImage = ImageIO.read(imageUrl);
                     outputStream = new ByteArrayOutputStream();
                     ImageIO.write(bufferedImage, "jpg", outputStream);
                 } catch (MalformedURLException e1) {
                     e1.printStackTrace();
                 } catch (IOException e) {
                     e.printStackTrace();
                 }
             // 对字节数组Base64编码
             BASE64Encoder encoder = new BASE64Encoder();
             return encoder.encode(outputStream.toByteArray());// 返回Base64编码过的字节数组字符串
         }

             /**
 44      * 将本地图片进行Base64位编码
 45      *
 46      * @param imgUrl
 47      *            图片的url路径，如http://.....xx.jpg
 48      * @return
 49      */
             public static String encodeImgageToBase64(File imageFile) {// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
             ByteArrayOutputStream outputStream = null;
    try {
                     BufferedImage bufferedImage = ImageIO.read(imageFile);
                     outputStream = new ByteArrayOutputStream();
                     ImageIO.write(bufferedImage, "jpg", outputStream);
                 } catch (MalformedURLException e1) {
                     e1.printStackTrace();
                 } catch (IOException e) {
                     e.printStackTrace();
                 }
             // 对字节数组Base64编码
             BASE64Encoder encoder = new BASE64Encoder();
             return encoder.encode(outputStream.toByteArray());// 返回Base64编码过的字节数组字符串
         }

             /**
 67      * 将Base64位编码的图片进行解码，并保存到指定目录
 68      *
 69      * @param base64
 70      *            base64编码的图片信息
 71      * @return
 72      */
             public static void decodeBase64ToImage(String base64, String path,
             String imgName) {
             BASE64Decoder decoder = new BASE64Decoder();
             try {
                     FileOutputStream write = new FileOutputStream(new File(path
                                     + imgName));
                     byte[] decoderBytes = decoder.decodeBuffer(base64);
                     write.write(decoderBytes);
                     write.close();
                 } catch (IOException e) {
                     e.printStackTrace();
                 }
         }

    public static void main(String[] args) throws IOException {


        String baseImg64 ="iVBORw0KGgoAAAANSUhEUgAAADwAAAA1CAYAAAAd84i6AAACTElEQVRoQ+3ZzatNURzG8c81IFEiI4rykkSRZOAfMDCTt6IURSkDeUmRdzcxEJGUlyJzAyYGkjIgE8LES0ZGDDCgEP1q3Trl5pxu7d/t7LtWnU7tvc/e6/t71llrPc8eMMbawBjjVYHbrnhVuCrcsgrUId0yQf/BqQpXhVtWgewhPRGb8AtfsRJX8CarrtnAO/ASjwvgTOzHYXzJgM4EDnUHcQMvClwcO4TreNc24CjubqzCnqL0QmzEaXxvG3DwTMOJAnke33C1fGfwptvDUDkmqi1YgdfYi48ptKQDr8UsXMBUHMUybG7jf3g6zuA4PhRFJ+FcmcQuZqicOUvPK7C78LkDLtblWJ6iGI23TOBYgk7hDh51KHwMNzuWqkahM4GHZumt+IG3ZeK6jyf40yhpuXk2cAbTf59RgUddgoY7kKHwFMSnlxYGolETkQG8BIt7oS376+c9XjuiyzKAR9Sxpn5UgZuqbMd9l2IDXmE5LrU58ZiPs9hXIMMxhVvaiU8JxU53SxEALChBQBj+cEy3cBn32gYce+lwRr9L4hHby6Fj79toHsaXKGfCMMCRYB7Ez6ZVzp6l12A9tpeYdgZul+Qjcq5QvdGWDRyG/yTu4gHWlaEc9jBsYgz3Rls2cMCMw1xMLiofwVO0LvEYTrnZuIYDeNaotKPkh1cjIp5tJamMeGdOCejj9UvjLXtIB1zAPsSi8o4pFE4J4aOa2cCNK9jtARW4W4X6/XxVuN8V7Nb/qnC3CvX7+apwvyvYrf9V4W4V6vfzY07hv0NjWjYc8NWrAAAAAElFTkSuQmCC".replaceAll("data:image/png;base64,","");/*截取图片字符....xyz*/

        decodeBase64ToImage(baseImg64, "D:\\", "test.png");

        /*baseImg64 = URLDecoder.decode(baseImg64, "UTF-8");*//*解码URI*//*

        baseImg64 = baseImg64.replaceAll(" ","+");*//*找回原来编码后图片数据中的+号*//*

        BASE64Decoder decoder = new BASE64Decoder();

        byte[] bytes = decoder.decodeBuffer(baseImg64);*//*使用BASE64Decoder解码*//*

        BufferedImage bufferedImage = new BufferedImage(40, 40, 1);

        FileOutputStream fos = new FileOutputStream ("D:\\test.png");
        fos.write(bytes);
        ImageIO.write(bufferedImage, "png", fos);*/


    }


}
