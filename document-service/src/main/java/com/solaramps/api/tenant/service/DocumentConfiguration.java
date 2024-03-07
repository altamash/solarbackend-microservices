package com.solaramps.api.tenant.service;

import com.solaramps.api.tenant.mapper.BaseResponse;
import com.solaramps.api.tenant.mapper.DocuVolMapDTO;

public interface DocumentConfiguration {

    BaseResponse saveDocumentTypeVolumeMap(DocuVolMapDTO docuVolMapDTO);

    BaseResponse updateDocumentTypeVolumeMap(DocuVolMapDTO docuVolMapDTO);

    BaseResponse getDocumentTypeVolumeMap(Long docuVolMapId);

    BaseResponse mapDocumentWithTypeAndVolume(Long docuLibraryId, Long docVolMapId);
}
