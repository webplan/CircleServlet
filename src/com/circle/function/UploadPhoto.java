package com.circle.function;

import org.apache.struts2.ServletActionContext;
import sun.misc.BASE64Decoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Random;

/**
 * Created by snow on 15-6-14.
 */
public class UploadPhoto {
    public static String UploadPhoto(String imgStr){
        String path = ServletActionContext.getServletContext().getRealPath("/")+"upload";
        File file = new File(path);
        if (!file.exists()){
            file.mkdir();
        }
        String imgPath;
        File f;
        //生成獨一無二的imgpath
        do{
            int imageName=new Random().nextInt(1000)+1;
            imgPath = path+imageName;
            f = new File(imgPath);
        }while (f.exists());

        boolean isTrue = String2Image(imgStr,imgPath);
        if (isTrue)
            return imgPath;
        else
            return null;
    }

    /**
     * 通过BASE64Decoder解码，并生成图
     * @param imgStr    img字節流，解碼後的string
     * @param imgFilePath   要存放img的位置
     */
    public static boolean String2Image(String imgStr, String imgFilePath){
        if (imgStr==null)
            return false;
        try {
            byte[] b = new BASE64Decoder().decodeBuffer(imgStr);
            for (int i=0;i<b.length;i++){
                if (b[i]<0) // 调整异常数据
                    b[i] += 256;
            }
            //生成jpeg圖片
            OutputStream out = new FileOutputStream(imgFilePath);
            out.write(b);
            out.flush();
            out.close();
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
