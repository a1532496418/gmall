package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import io.swagger.annotations.Api;
import org.apache.commons.io.FilenameUtils;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;



@Api(tags = "文件上传接口")
@RestController
@RequestMapping("admin/product/")
public class FileUploadController {
    //做回显使用的(获取application-dev.yml文件中的fileServer.url属性)
    @Value("${fileServer.url}")
    private String fileUrl;//fileUrl=http://192.168.200.128:8080/

    @RequestMapping("fileUpload")
    public Result<String> fileUpload(MultipartFile file) throws Exception {
        /*
        1.  先读取到配置文件tracker.conf
        2.  数据初始化
        3.  创建tracker
        4.  创建storage
        5.  执行上传
         */
        String configFile = this.getClass().getResource("/tracker.conf").getFile();
        String path="";
        //判断
        if(configFile!=null){
            ClientGlobal.init(configFile);
            //tracker对象
            TrackerClient trackerClient = new TrackerClient();
            //获取到tracker的Server
            TrackerServer trackerServer = trackerClient.getConnection();
            //storage对象
            StorageClient1 storageClient1 = new StorageClient1(trackerServer,null);
            //上传文件，获取文件的后缀名
            String extName = FilenameUtils.getExtension(file.getOriginalFilename());
            path=storageClient1.upload_appender_file1(file.getBytes(),extName,null);
            //  group1/M00/00/01/wKjIgF9zVUOEAQ_2AAAAAPzxDAI115.png
            System.out.println("文件路径：\t"+path);
        }
        //  http://192.168.200.128:8080/group1/M00/00/01/wKjIgF9zVUOEAQ_2AAAAAPzxDAI115.png
        return Result.ok(fileUrl+path);
    }
}
