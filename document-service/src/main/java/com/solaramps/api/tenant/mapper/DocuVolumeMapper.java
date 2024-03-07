package com.solaramps.api.tenant.mapper;

import com.solaramps.api.tenant.model.docu.DocuVolume;

import java.util.List;
import java.util.stream.Collectors;

public class DocuVolumeMapper {

    public static DocuVolume toDocuVolume(DocuVolumeDTO docuVolumeDTO) {
        if (docuVolumeDTO == null) {
            return null;
        }
        return DocuVolume.builder()
                .id(docuVolumeDTO.getId())
                .volumeName(docuVolumeDTO.getVolumeName())
                .codeRefId(docuVolumeDTO.getCodeRefId())
                .build();
    }

    public static DocuVolumeDTO toDocuVolumeDTO(DocuVolume docuVolume) {
        if (docuVolume == null) {
            return null;
        }
        return DocuVolumeDTO.builder()
                .id(docuVolume.getId())
                .volumeName(docuVolume.getVolumeName())
                .codeRefId(docuVolume.getCodeRefId())
                .docuModuleId(docuVolume.getDocuModule() != null ? docuVolume.getDocuModule().getId() : null)
                .createdById(docuVolume.getCreatedBy() != null ? docuVolume.getCreatedBy().getAcctId() : null)
                .updatedById(docuVolume.getUpdatedBy() != null ? docuVolume.getUpdatedBy().getAcctId() : null)
                .createdAt(docuVolume.getCreatedAt())
                .updatedAt(docuVolume.getUpdatedAt())
                .build();
    }

    public static DocuVolume toUpdatedDocuVolumeMap(DocuVolume docuVolume, DocuVolumeDTO docuVolumeUpdate) {
        docuVolume.setVolumeName(docuVolumeUpdate.getVolumeName() == null ? docuVolume.getVolumeName() : docuVolumeUpdate.getVolumeName());
        docuVolume.setCodeRefId(docuVolumeUpdate.getCodeRefId() == null ? docuVolume.getCodeRefId() : docuVolumeUpdate.getCodeRefId());
        return docuVolume;
    }

    public static List<DocuVolumeDTO> toDocuVolumeDTOs(List<DocuVolume> docuVolumes) {
        return docuVolumes.stream().map(DocuVolumeMapper::toDocuVolumeDTO).collect(Collectors.toList());
    }
}
