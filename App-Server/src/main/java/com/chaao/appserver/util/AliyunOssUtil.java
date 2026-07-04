package com.chaao.appserver.util;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.ObjectMetadata;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

@Slf4j
@Component
public class AliyunOssUtil {

    @Autowired
    private OSS ossClient;

    @Value("${aliyun.oss.bucket-name}")
    private String bucketName;

    @Value("${aliyun.oss.endpoint}")
    private String endpoint;

    /**
     * 上传 MultipartFile 文件
     * @param file 前端上传的文件
     * @param objectName OSS 中的文件路径（如：images/2023/10/test.jpg）
     * @return 文件的完整访问 URL
     */
    public String uploadFile(MultipartFile file, String objectName) {
        try (InputStream inputStream = file.getInputStream()) {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            
            ossClient.putObject(bucketName, objectName, inputStream, metadata);
            log.info("文件上传成功: {}", objectName);
            return getFileUrl(objectName);
        } catch (Exception e) {
            log.error("文件上传失败: {}", objectName, e);
            throw new RuntimeException("文件上传失败", e);
        }
    }

    /**
     * 上传字节数组
     */
    public String uploadBytes(byte[] bytes, String objectName) {
        try (InputStream inputStream = new ByteArrayInputStream(bytes)) {
            ossClient.putObject(bucketName, objectName, inputStream);
            return getFileUrl(objectName);
        } catch (Exception e) {
            log.error("字节数组上传失败: {}", objectName, e);
            throw new RuntimeException("文件上传失败", e);
        }
    }

    /**
     * 获取文件的公开访问 URL
     */
    public String getFileUrl(String objectName) {
        String host = endpoint;
        if (!endpoint.startsWith("http")) {
            host = "https://" + endpoint;
        }
        if (!host.contains(bucketName)) {
            host = host.replace("://", "://" + bucketName + ".");
        }
        return host + "/" + objectName;
    }

    /**
     * 生成带过期时间的临时签名 URL（适用于私有 Bucket 的文件预览）
     * @param expiration 过期时间（例如：new Date(System.currentTimeMillis() + 3600 * 1000)）
     */
    public String generatePresignedUrl(String objectName, Date expiration) {
        URL url = ossClient.generatePresignedUrl(bucketName, objectName, expiration);
        return url.toString();
    }

    /**
     * 删除文件
     */
    public void deleteFile(String objectName) {
        try {
            ossClient.deleteObject(bucketName, objectName);
            log.info("文件删除成功: {}", objectName);
        } catch (Exception e) {
            log.error("文件删除失败: {}", objectName, e);
            throw new RuntimeException("文件删除失败", e);
        }
    }
}