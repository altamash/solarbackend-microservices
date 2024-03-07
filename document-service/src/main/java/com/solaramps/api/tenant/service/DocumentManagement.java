package com.solaramps.api.tenant.service;

import com.solaramps.api.tenant.mapper.BaseResponse;
import com.solaramps.api.tenant.mapper.DocuMeasureDTO;
import com.solaramps.api.tenant.mapper.DocuMeasureIdDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DocumentManagement {

    BaseResponse uploadDocument(MultipartFile file, List<String> allowedFormats, Long compKey, Long docuLibraryId,
                                String storagePathCode, boolean relativeUrl);

    BaseResponse addOrUpdateDocuLibrary(Long docuLibraryId, DocuMeasureDTO docuMeasureDTO, Long compKey);

    BaseResponse softDeleteDocument(Long docuLibraryId, String measureIdHash, Long compKey);

    BaseResponse softDeleteDocuments(List<DocuMeasureIdDTO> docuMeasureIdDTOs, Long compKey);

    BaseResponse downloadDocument(Long docuLibraryId);

    BaseResponse downloadDocuments(List<Long> docuLibraryIds);
}
