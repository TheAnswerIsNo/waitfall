package com.waitfall.oss.service;

import com.waitfall.oss.convert.attachment.AttachmentConvert;
import com.waitfall.oss.domain.entity.TAttachment;
import com.waitfall.oss.domain.repository.TAttachmentRepository;
import com.waitfall.oss.domain.vo.attachment.AttachmentVO;
import com.waitfall.oss.domain.vo.minio.OssUploadVO;
import com.waitfall.oss.util.MinioUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author by 秋
 * @date 2025/7/25 17:58
 */
@Service
public class MinioService implements OssService {

    @Resource
    private TAttachmentRepository tAttachmentRepository;

    @Resource
    private MinioUtil minioUtil;


    @Override
    public AttachmentVO uploadFile(MultipartFile multipartFile,String objectId) {
        OssUploadVO ossUploadVO = minioUtil.uploadFile(multipartFile);
        TAttachment tAttachment = AttachmentConvert.INSTANCE.parseOssToEntity(ossUploadVO,objectId);
        tAttachmentRepository.save(tAttachment);
        return AttachmentConvert.INSTANCE.parseEntityToVO(tAttachment);
    }

    @Override
    public AttachmentVO uploadFile(InputStream inputStream,String name, String objectId) {
        OssUploadVO ossUploadVO = minioUtil.uploadFile(inputStream,name);
        TAttachment tAttachment = AttachmentConvert.INSTANCE.parseOssToEntity(ossUploadVO,objectId);
        tAttachmentRepository.save(tAttachment);
        return AttachmentConvert.INSTANCE.parseEntityToVO(tAttachment);
    }

    @Override
    public List<AttachmentVO> uploadFileBatch(List<MultipartFile> files,String objectId) {
        List<OssUploadVO> ossUploadVOList = minioUtil.uploadFileBatch(files);
        List<TAttachment> tAttachmentList = AttachmentConvert.INSTANCE.parseOssToEntity(ossUploadVOList,objectId);
        tAttachmentRepository.saveBatch(tAttachmentList,1000);
        return AttachmentConvert.INSTANCE.parseEntityToListVO(tAttachmentList);
    }

    @Override
    public List<AttachmentVO> getAttachmentList(String objectId) {
        List<TAttachment> list = tAttachmentRepository.lambdaQuery().eq(TAttachment::getObjectId, objectId).list();
        return AttachmentConvert.INSTANCE.parseEntityToListVO(list);
    }

    @Override
    public Map<String, List<AttachmentVO>> getAttachmentMap(List<String> objectIds) {
        List<TAttachment> list = tAttachmentRepository.lambdaQuery().in(TAttachment::getObjectId, objectIds).list();
        return list.stream().collect(Collectors.groupingBy(TAttachment::getObjectId,
                Collectors.mapping(AttachmentConvert.INSTANCE::parseEntityToVO,Collectors.toList())));
    }

    @Override
    public void deleteFile(String objectId) {
        List<TAttachment> list = tAttachmentRepository.lambdaQuery().eq(TAttachment::getObjectId, objectId).list();
        // 获取所有objectName
        List<String> objectNames = list.stream().map(TAttachment::getObjectName).collect(Collectors.toList());
        // 删除所有文件
        minioUtil.deleteFileBatch(objectNames);
    }

    @Override
    public void deleteFileBatch(List<String> objectIds) {
        List<TAttachment> list = tAttachmentRepository.lambdaQuery().in(TAttachment::getObjectId, objectIds).list();
        // 获取所有objectName
        List<String> objectNames = list.stream().map(TAttachment::getObjectName).collect(Collectors.toList());
        // 删除所有文件
        minioUtil.deleteFileBatch(objectNames);
    }
}
