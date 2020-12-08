package com.mbrickspan.mbpan.controller;
import com.mbrickspan.mbpan.dao.FiledbMapper;
import com.mbrickspan.mbpan.entity.Filedb;
import com.mbrickspan.mbpan.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

/**
 * @author: Guessed 魏嘉辰
 * @Date: 2020/10/9
 */
@Controller
public class FileController {

    @Autowired
    private FileService fileServiceImpl;

    //检查Md5是否存在
    @PostMapping("checkFile")
    @ResponseBody
    public Boolean checkFile(@RequestParam(value = "md5File") String md5File) {
        Boolean exist = false;
        Filedb filedb=fileServiceImpl.selectfilebyfmd5(md5File,UsersController.cookie_uname);
        //通过判断md5值存不存在，来判断账户下文件是否存在
		if(filedb!=null) {
			exist = true;
		}
        return exist;
    }

    //检查碎片上传情况
    @PostMapping("checkChunk")
    @ResponseBody
    public Boolean checkChunk(@RequestParam(value = "md5File") String md5File,
                              @RequestParam(value = "chunk") Integer chunk) {
        Boolean exist = false;
        String path = "C:/mbpanupload/"+UsersController.cookie_uname+"/"+md5File+"/";//分片存放目录
        String chunkName = chunk+ ".tmp";//分片名
        File file = new File(path+chunkName);
        if (file.exists()) {
            exist = true;
        }
        return exist;
    }

    //上传
    @PostMapping("upload")
    @ResponseBody
    public Boolean upload(@RequestParam(value = "file") MultipartFile file,
                          @RequestParam(value = "md5File") String md5File,
                          @RequestParam(value = "chunk",required= false) Integer chunk) { //第几片，从0开始
        String path = "C:/mbpanupload/"+UsersController.cookie_uname+"/"+md5File+"/";
        File dirfile = new File(path);
        if (!dirfile.exists()) {//目录不存在，创建目录
            dirfile.mkdirs();
        }
        String chunkName;
        if(chunk == null) {//表示是小文件，还没有一片
            chunkName = "0.tmp";
        }else {
            chunkName = chunk+ ".tmp";
        }
        String filePath = path+chunkName;
        File savefile = new File(filePath);

        try {
            if (!savefile.exists()) {
                savefile.createNewFile();//文件不存在，则创建
            }
            file.transferTo(savefile);//将文件保存
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    //删除整个目录方法
    public static void deleteFolder(File folder) throws Exception {
        if (!folder.exists()) {
            throw new Exception("文件不存在");
        }
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    //递归直到目录下没有文件
                    deleteFolder(file);
                } else {
                    //删除
                    file.delete();
                }
            }
        }
        //删除
        folder.delete();
    }

    //合并碎片并上传数据库
    @PostMapping("merge")
    @ResponseBody
    public Boolean  merge(@RequestParam(value = "chunks",required =false) Integer chunks,
                          @RequestParam(value = "md5File") String md5File,
                          @RequestParam(value = "name") String name) throws Exception {
        //上传根目录
        String path = "C:/mbpanupload";
        //当前用户目录
        String dirpath = "C:/mbpanupload/"+UsersController.cookie_uname+"/"+md5File+"/";
        FileOutputStream fileOutputStream = new FileOutputStream(path+"/"+UsersController.cookie_uname+"/"+name);  //合成后的文件

        try {
            byte[] buf = new byte[1024];
            for(long i=0;i<chunks;i++) {
                String chunkFile=i+".tmp";
                File file = new File(dirpath+chunkFile);
                InputStream inputStream = new FileInputStream(file);
                int len = 0;
                while((len=inputStream.read(buf))!=-1){
                    fileOutputStream.write(buf,0,len);
                }
                inputStream.close();
            }

            //删除md5目录，及临时文件
            File dirfile=new File(dirpath);
            deleteFolder(dirfile);

        } catch (Exception e) {
            return false;
        }finally {
            fileOutputStream.close();
        }
        //添加记录到数据库
        Filedb filedb = new Filedb();
        filedb.setFilename(name);
        filedb.setFmd5(md5File);
        filedb.setUname(UsersController.cookie_uname);
        fileServiceImpl.insertSelective(filedb);
        return true;
    }

    @RequestMapping("download")
    public void download(String filename, HttpServletResponse response) throws UnsupportedEncodingException {
        String path = "C:/mbpanupload/"+UsersController.cookie_uname+"/"+filename;
        File file = new File(path);

        if (file.exists()) {
            response.setContentType("application/force-download");// 设置强制下载不打开
            //设置编码，解决中文名乱码
            filename = URLEncoder.encode(filename,"utf-8");
            response.addHeader("Content-Disposition", "attachment;filename="+filename);// 设置文件名
            byte[] buffer = new byte[1024];
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            try {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                OutputStream os = response.getOutputStream();
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer, 0, i);
                    i = bis.read(buffer);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }else{
            System.out.println("文件不存在！");
        }
    }

    //彻底删除文件
    @RequestMapping("shiftdel")
    public String shiftdel(String filename){
        String path = "C:/mbpanupload/"+UsersController.cookie_uname+"/"+filename;
        File file=new File(path);
        // 路径为文件且不为空则进行删除
        if (file.isFile()) {
            file.delete();
            fileServiceImpl.shiftdel(filename);
        }else{
            System.out.println("文件不存在!");
        }

        return "forward:/user/selectbin";
    }

}
