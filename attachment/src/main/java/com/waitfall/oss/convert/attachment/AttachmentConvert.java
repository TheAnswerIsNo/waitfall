package com.waitfall.oss.convert.attachment;

import cn.hutool.core.bean.BeanUtil;
import com.waitfall.oss.domain.entity.TAttachment;
import com.waitfall.oss.domain.vo.attachment.AttachmentVO;
import com.waitfall.oss.domain.vo.minio.OssUploadVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author by 秋
 * @date 2025/8/1 10:12
 */
@Mapper
public interface AttachmentConvert {

    AttachmentConvert INSTANCE = Mappers.getMapper(AttachmentConvert.class);

    TAttachment parseOssToEntity(OssUploadVO ossUploadVO,String objectId);

    default List<TAttachment> parseOssToEntity(List<OssUploadVO> ossUploadVOList,String objectId){
        List<TAttachment> tAttachments = BeanUtil.copyToList(ossUploadVOList, TAttachment.class);
        tAttachments.forEach(tAttachment -> tAttachment.setObjectId(objectId));
        return tAttachments;
    }

    AttachmentVO parseEntityToVO(TAttachment tAttachment);

    List<AttachmentVO> parseEntityToListVO(List<TAttachment> tAttachmentList);

}
