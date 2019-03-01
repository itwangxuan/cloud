package com.qhjys.springcloud.util;

import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
@Component
public class FileUtil {

//    @Value("${qhjys.file.path}")
    private String imagePath;

    /**
     * base64编码生成图片（包括缩略图）
     * @param base64Str
     * @return 返回正常图片路径[0]和缩略图路径[1]
     */
    public String[] createFile(String base64Str){
        //String data = "data:image/jpeg;base64,";
        if(base64Str.indexOf(";base64,") != -1) {
            base64Str = base64Str.substring(base64Str.indexOf(";base64,") + 8, base64Str.length());
        }
        String[] imgPath = new String[2];
        String migname = "";
        String imgFilePath = "";
        sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
        Date sysDate = new Date();
        try
        {
            String[] dateFolder = DateUtil.formatDateByFormat(sysDate,"yyyy-MM-dd").split("-");
            String folderPath = imagePath+""+dateFolder[0]+""+dateFolder[1];
            File fp = new File(folderPath);
            //根据年月日创建文件夹
            if (!fp.exists()) {
                fp.mkdirs();// 目录不存在的情况下，创建目录。
            }

            //创建缩略图文件夹
            File thumbnail = new File(folderPath+"/thumbnail");
            //根据年月日创建文件夹
            if (!thumbnail.exists()) {
                thumbnail.mkdirs();// 目录不存在的情况下，创建目录。
            }

            //Base64解码
            byte[] b = decoder.decodeBuffer(base64Str);
            for(int j=0;j<b.length;++j)  {
                if(b[j]<0)
                {//调整异常数据
                    b[j]+=256;
                }
            }
            //生成jpeg图片
            migname= sysDate.getTime()+".png";
            imgFilePath = folderPath+"/"+migname;//新生成的图片
            OutputStream out = new FileOutputStream(imgFilePath);
            out.write(b);
            out.flush();
            out.close();
            createThumbnail(imgFilePath, migname,150);


            imgPath[0] = dateFolder[0]+dateFolder[1]+"/"+migname;
            imgPath[1] = dateFolder[0]+dateFolder[1]+"/thumbnail/"+migname;
        }catch (Exception e){
            e.printStackTrace();
            log.error("文件创建出现异常",e);
            return null;
        }
        log.info("--------------------------图片上传成功-------------------");
        return imgPath;
    }


    /**
     * 生成图片的缩略图
     * @param filePath 图片文件绝对地址(包含文件名称)
     * @param fileName 图片文件名称
     * @param width 长宽最少值()
     * @return
     */
    private void createThumbnail(String filePath,String fileName,int width){
        try {
            Thumbnails.of(filePath)
                    .size(width, width)
                    .toFile(filePath.replace(fileName, "thumbnail/"+fileName));
            //return filePath.replace(fileName, "thumbnail/"+fileName);
        } catch (IOException e) {
            e.printStackTrace();
            //return null;
        }
    }



    /**
     * 批量下载本地图片
     * @param zipName zip名称
     * @param fileUrl key为文件路径 value为文件名称
     * @param response
     */
    public void downloadPicture(String zipName, Map<String,String> files, HttpServletResponse response) throws Exception{
        FileInputStream fis = null;
        ZipOutputStream zos = null;
        try {
            zipName = URLEncoder.encode(zipName+".zip", "UTF-8");//转换中文否则可能会产生乱码
            response.setContentType("application/octet-stream");// 指明response的返回对象是文件流
            response.setHeader("Content-Disposition", "attachment;filename=" + zipName);// 设置在下载框默认显示的文件名
            zos = new ZipOutputStream(response.getOutputStream());

            for (String item : files.keySet()) {
                String prefix = files.get(item);
                String suffix = item.substring(item.lastIndexOf(".")+1);
                // 获取路径
                String filePath = item.substring(item.indexOf("-") + 1, item.length());
                File file = new File(filePath);
                if (file.exists()) {
                    fis = new FileInputStream(file);
                    zos.putNextEntry(new ZipEntry(prefix+"."+suffix));
                    byte[] buffer = new byte[1024];
                    int r = 0;
                    while ((r = fis.read(buffer)) != -1) {
                        zos.write(buffer, 0, r);
                    }
                    fis.close();
                }
            }

        } catch (UnsupportedEncodingException e) {
            throw e;
        }finally {
            // 不能放到循环中，需要全部写出之后才能关闭，否则导致下载的文件报错：“不可预料的压缩文件末端”
            /*if (fis != null) {
                fis.close();
            }*/
            zos.flush();
            zos.close();
        }
    }

    /**
     * 图片构建临时zip包
     * @param zipName zip名称
     * @param fileUrl key为文件路径 value为文件名称
     * @param path 目的压缩文件保存路径
     */
    public  String temporaryZipFile(String path, String zipName, Map<String,String> files){
        try {
            path = path.replace("\\", "//") + zipName;
            FileOutputStream outputStream = new FileOutputStream(path);
            ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(outputStream));
            for (String item : files.keySet()) {
                String prefix = files.get(item);
                File file = new File(item);
                if (file.exists()) {
                    FileInputStream fis = new FileInputStream(file);
                    zos.putNextEntry(new ZipEntry(URLEncoder.encode(prefix+"."+"jpg", "GBK").replace("%", "").toLowerCase()));
                    byte[] buffer = new byte[1024];
                    int r = 0;
                    while ((r = fis.read(buffer)) != -1) {
                        zos.write(buffer, 0, r);
                    }
                    fis.close();
                }
            }
            zos.flush();
            zos.close();
            return path;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            log.error("上传出现异常！",e);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("上传出现异常！",e);
        }
        return null;
    }

    /**
     * 创建文件
     * @param file
     * @return
     */
    public String createFile(MultipartFile file){
        String path = "";
        Date sysDate = new Date();
        try {
            String[] dateFolder = DateUtil.formatDateByFormat(sysDate,"yyyy-MM-dd").split("-");
            String folderPath = imagePath+""+dateFolder[0]+""+dateFolder[1];
            File fp = new File(folderPath);
            //根据年月日创建文件夹
            if (!fp.exists()) {
                fp.mkdirs();// 目录不存在的情况下，创建目录。
            }
            //生成文件
            String fileName = sysDate.getTime()+file.getOriginalFilename();
            path = folderPath+"/"+fileName;
            File newFile=new File(path);
            //通过CommonsMultipartFile的方法直接写文件（注意这个时候）
            file.transferTo(newFile);
            path = dateFolder[0]+""+dateFolder[1]+"/"+fileName;
        }catch (Exception e){
            e.printStackTrace();
            log.error("上传出现异常！",e);
        }
        log.info("--------------------------文件上传成功-------------------");
        return path;
    }

    /**
     * 删除单个文件
     * @param   path    被删除文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public  boolean deleteFile(String path) {
        File file = new File(path);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            file.delete();
            log.info("--------------------------文件删除成功-------------------");
            return true;
        }
        log.info("--------------------------文件删除失败-------------------");
        return false;
    }
}
