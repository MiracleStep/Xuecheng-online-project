package com.xuecheng.test.fastdfs;

import org.csource.common.MyException;
import org.csource.fastdfs.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestFastDFS {

    //上传测试
    @Test
    public void testUpload() throws IOException, MyException {
        ClientGlobal.initByProperties("config/fastdfs-client.properties");
        //定义TrackerClient，用于请求TrackerServer
        TrackerClient trackerClient = new TrackerClient();
        //连接tracker
        TrackerServer trackerServer = trackerClient.getConnection();
        //获取storage
        StorageServer storageServer = trackerClient.getStoreStorage(trackerServer);
        //创建stroageClient
        StorageClient storageClient = new StorageClient(trackerServer, storageServer);
        //上传文件
        String filePath = "D:\\Auserfile\\program\\intelliJ\\xcEduService01\\test-fastdfs\\src\\main\\resources\\c-mini-logo.jpg";
        String[] fileId = storageClient.upload_file(filePath, "jpg", null);
        System.out.println(fileId);

    }


    //下载测试
    @Test
    public void testdownLoad() throws IOException, MyException {
        ClientGlobal.initByProperties("config/fastdfs-client.properties");
        //定义TrackerClient，用于请求TrackerServer
        TrackerClient trackerClient = new TrackerClient();
        //连接tracker
        TrackerServer trackerServer = trackerClient.getConnection();
        //获取storage
        StorageServer storageServer = trackerClient.getStoreStorage(trackerServer);
        //创建stroageClient
        StorageClient storageClient = new StorageClient(trackerServer, storageServer);
        //下载文件
        String filePath = "group1/M00/00/00/wKhVgWCGo6OAb07mAAAajCEAdq0626.jpg";
        byte[] bytes = storageClient.download_file("group1", "M00/00/00/wKhVg2CHbZiAU4PhAAAajIJa_mc323.jpg");
        FileOutputStream fileOutputStream = new FileOutputStream(new File("d:/log1.jpg"));
        fileOutputStream.write(bytes);

    }

}
