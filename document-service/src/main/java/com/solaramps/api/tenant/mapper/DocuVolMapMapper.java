package com.solaramps.api.tenant.mapper;

import com.solaramps.api.tenant.model.docu.DocuVolMap;

import java.util.Collections;
import java.util.stream.Collectors;

public class DocuVolMapMapper {

    public static DocuVolMap toDocVolMap(DocuVolMapDTO docuVolMapDTO) {
        if (docuVolMapDTO == null) {
            return null;
        }
        return DocuVolMap.builder()
                .id(docuVolMapDTO.getId())
                .docuTypePAVId(docuVolMapDTO.getDocuTypePAVId())
                .volPAVId(docuVolMapDTO.getVolPAVId())
                .isMandatory(docuVolMapDTO.getIsMandatory())
                .noFilesAllowed(docuVolMapDTO.getNoFilesAllowed())
                .maxFileSize(docuVolMapDTO.getMaxFileSize())
                .allowedFileType(docuVolMapDTO.getAllowedFileType())
                .build();
    }

    public static DocuVolMapDTO toDocVolMapDTO(DocuVolMap docuVolMap) {
        if (docuVolMap == null) {
            return null;
        }
        return DocuVolMapDTO.builder()
                .id(docuVolMap.getId())
                .docuTypePAVId(docuVolMap.getDocuTypePAVId())
                .volPAVId(docuVolMap.getVolPAVId())
                .docuTypeId(docuVolMap.getDocuType() != null ? docuVolMap.getDocuType().getId() : null)
                .docuVolumeIds(docuVolMap.getDocuVolumes() != null ? docuVolMap.getDocuVolumes()
                        .stream().map(v -> v.getId()).collect(Collectors.toList()) : Collections.EMPTY_LIST)
                .isMandatory(docuVolMap.getIsMandatory())
                .noFilesAllowed(docuVolMap.getNoFilesAllowed())
                .maxFileSize(docuVolMap.getMaxFileSize())
                .allowedFileType(docuVolMap.getAllowedFileType())
                .createdById(docuVolMap.getCreatedBy() != null ? docuVolMap.getCreatedBy().getAcctId() : null)
                .updatedById(docuVolMap.getUpdatedBy() != null ? docuVolMap.getUpdatedBy().getAcctId() : null)
                .createdAt(docuVolMap.getCreatedAt())
                .updatedAt(docuVolMap.getUpdatedAt())
                .build();
    }

    public static DocuVolMap toUpdatedDocVolMap(DocuVolMap docuVolMap, DocuVolMapDTO docVolMapUpdate) {
        docuVolMap.setDocuTypePAVId(docVolMapUpdate.getDocuTypePAVId() == null ? docuVolMap.getDocuTypePAVId() : docVolMapUpdate.getDocuTypePAVId());
        docuVolMap.setVolPAVId(docVolMapUpdate.getVolPAVId() == null ? docuVolMap.getVolPAVId() : docVolMapUpdate.getVolPAVId());
        docuVolMap.setIsMandatory(docVolMapUpdate.getIsMandatory() == null ? docuVolMap.getIsMandatory() : docVolMapUpdate.getIsMandatory());
        docuVolMap.setNoFilesAllowed(docVolMapUpdate.getNoFilesAllowed() == null ? docuVolMap.getNoFilesAllowed() : docVolMapUpdate.getNoFilesAllowed());
        docuVolMap.setMaxFileSize(docVolMapUpdate.getMaxFileSize() == null ? docuVolMap.getMaxFileSize() : docVolMapUpdate.getMaxFileSize());
        docuVolMap.setAllowedFileType(docVolMapUpdate.getAllowedFileType() == null ? docuVolMap.getAllowedFileType() : docVolMapUpdate.getAllowedFileType());
        return docuVolMap;
    }
}
