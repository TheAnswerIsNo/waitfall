package com.waitfall.oss.service;

import com.waitfall.oss.domain.vo.attachment.AttachmentVO;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * @author by 秋
 * @date 2025/7/25 17:57
 */
public interface OssService {


    /**
     * 上传文件
     * @param file 文件
     * @param objectId 关联表id
     * @return 附件
     */
    AttachmentVO uploadFile(MultipartFile file,String objectId);

    /**
     * 上传文件
     * @param inputStream 文件流
     * @param name 文件名
     * @param objectId 关联表id
     * @return 附件
     */
    AttachmentVO uploadFile(InputStream inputStream,String name,String objectId);

    /**
     * 批量上传文件
     * @param files 文件列表
     * @param objectId 关联表id
     * @return 附件列表
     */
    List<AttachmentVO> uploadFileBatch(List<MultipartFile> files,String objectId);

    /**
     * 根据关联表id查询附件列表
     * @param objectId 关联表id
     * @return 附件列表
     */
    List<AttachmentVO> getAttachmentList(String objectId);

    /**
     * 根据关联表id列表查询附件列表
     * @param objectIds 关联表id列表
     * @return 附件列表
     */
    Map<String,List<AttachmentVO>> getAttachmentMap(List<String> objectIds);

    /**
     * 删除文件
     * @param objectId 关联表id
     */
    void deleteFile(String objectId);

    /**
     * 批量删除文件
     * @param objectIds 关联表id列表
     */
    void deleteFileBatch(List<String> objectIds);
}
