package com.solaramps.api.tenant.service.crud;

import com.solaramps.api.tenant.mapper.BaseResponse;
import com.solaramps.api.tenant.mapper.DocuVolumeDTO;
import com.solaramps.api.tenant.model.docu.DocuVolume;

import java.util.List;

public interface DocuVolumeServiceExt {
//    List<DocuVolume> getAllById(List<Long> ids);

//    BaseResponse setDocuModule(Long docuVolumeId, Long docuModuleId);

    BaseResponse saveResponse(DocuVolumeDTO docuVolumeDTO);

    BaseResponse updateResponse(DocuVolumeDTO docuVolumeDTO);

    List<DocuVolume> getByIdIn(List<Long> ids);
}
