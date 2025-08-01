package com.waitfall.oss.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.waitfall.oss.config.MinioConfig;
import com.waitfall.oss.domain.vo.minio.OssUploadVO;
import io.minio.*;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author by 秋
 * @date 2025/8/1 9:35
 */
@Slf4j
@Component
public class MinioUtil {

    private static final CharSequence[] SPECIAL_SUFFIX = new CharSequence[]{"tar.bz2", "tar.Z", "tar.gz", "tar.xz"};

    @Resource
    private MinioConfig minioConfig;

    @Resource
    private MinioClient minioClient;

    @PostConstruct
    public void init() {
        // 判断是否存在桶
        try {
            if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(minioConfig.getBucketName()).build())) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(minioConfig.getBucketName()).build());
            }
        } catch (Exception e) {
            log.error("创建minio桶失败", e);
        }
    }

    /**
     * 上传文件
     *
     * @param file 上传的文件
     * @return 上传后的文件信息
     */
    @SneakyThrows
    public OssUploadVO uploadFile(MultipartFile file) {
        String name = file.getOriginalFilename();
        String suffix = getSuffix(name);
        Long size = file.getSize();
        String contentType = file.getContentType();
        // 构造objectName 桶名称/时间戳/文件名称
        String objectName = minioConfig.getBucketName() + StrUtil.SLASH + DateUtil.current() + StrUtil.SLASH +name;
        String url = minioConfig.getEndpoint() + StrUtil.SLASH + objectName;
        try (InputStream inputStream = file.getInputStream()) {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(minioConfig.getBucketName())
                    .object(objectName)
                    .stream(inputStream, inputStream.available(), -1)
                    .contentType(contentType)
                    .build());
        }
        return OssUploadVO.builder()
                .name(name)
                .objectName(objectName)
                .suffix(suffix)
                .size(size)
                .url(url)
                .contentType(contentType)
                .build();
    }

    /**
     * 上传文件
     * @param inputStream 输入流
     * @param name 文件名
     * @return 上传后的文件信息
     */
    @SneakyThrows
    public OssUploadVO uploadFile(InputStream inputStream,String name) {
        String suffix = getSuffix(name);
        String contentType = "application/octet-stream";
        // 构造objectName 桶名称/时间戳/文件名称
        String objectName = minioConfig.getBucketName() + StrUtil.SLASH + DateUtil.current() + StrUtil.SLASH +name;
        String url = minioConfig.getEndpoint() + StrUtil.SLASH + objectName;
        try (inputStream) {
            Long size = (long) inputStream.available();
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(minioConfig.getBucketName())
                    .object(objectName)
                    .stream(inputStream, inputStream.available(), -1)
                    .contentType(contentType)
                    .build());
            return OssUploadVO.builder()
                    .name(name)
                    .objectName(objectName)
                    .suffix(suffix)
                    .size(size)
                    .url(url)
                    .contentType(contentType)
                    .build();
        }
    }

    @SneakyThrows
    public void deleteFile(String objectName) {
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(minioConfig.getBucketName())
                    .object(objectName)
                    .build());
    }

    @SneakyThrows
    public void deleteFileBatch(List<String> objectNames) {
        objectNames.forEach(this::deleteFile);
    }

    /**
     * 批量上传文件
     *
     * @param files 文件列表
     * @return 上传后的文件信息列表
     */
    public List<OssUploadVO> uploadFileBatch(List<MultipartFile> files) {
        return files.stream().map(this::uploadFile).collect(Collectors.toList());
    }


    private String getSuffix(String fileName) {
        if (fileName == null) {
            return null;
        } else {
            int index = fileName.lastIndexOf(".");
            if (index == -1) {
                return "";
            } else {
                int secondToLastIndex = fileName.substring(0, index).lastIndexOf(".");
                String substr = fileName.substring(secondToLastIndex == -1 ? index : secondToLastIndex + 1);
                if (StrUtil.containsAny(substr, SPECIAL_SUFFIX)) {
                    return substr;
                } else {
                    String ext = fileName.substring(index + 1);
                    return StrUtil.containsAny(ext, '/', '\\') ? "" : ext;
                }
            }
        }
    }


}
