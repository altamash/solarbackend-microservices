package com.solaramps.api.tenant.mapper;

import com.solaramps.api.tenant.model.docu.DocuType;

import java.util.List;
import java.util.stream.Collectors;

public class DocuTypeMapper {

    public static DocuType toDocuType(DocuTypeDTO docuTypeDTO) {
        if (docuTypeDTO == null) {
            return null;
        }
        return DocuType.builder()
                .id(docuTypeDTO.getId())
                .docuType(docuTypeDTO.getDocuType())
                .build();
    }

    public static DocuTypeDTO toDocuTypeDTO(DocuType docuType) {
        if (docuType == null) {
            return null;
        }
        return DocuTypeDTO.builder()
                .id(docuType.getId())
                .docuType(docuType.getDocuType())
                .docuTypeConfigId(docuType.getDocuVolMap() != null ? docuType.getDocuVolMap().getId() : null)
                .createdById(docuType.getCreatedBy() != null ? docuType.getCreatedBy().getAcctId() : null)
                .updatedById(docuType.getUpdatedBy() != null ? docuType.getUpdatedBy().getAcctId() : null)
                .createdAt(docuType.getCreatedAt())
                .updatedAt(docuType.getUpdatedAt())
                .build();
    }

    public static DocuType toUpdatedDocuType(DocuType docuLibrary, DocuTypeDTO docuLibraryUpdate) {
        docuLibrary.setDocuType(docuLibraryUpdate.getDocuType() == null ? docuLibrary.getDocuType() : docuLibraryUpdate.getDocuType());
        return docuLibrary;
    }

    public static List<DocuTypeDTO> toDocuTypeDTOs(List<DocuType> docuLibrary) {
        return docuLibrary.stream().map(DocuTypeMapper::toDocuTypeDTO).collect(Collectors.toList());
    }
}
