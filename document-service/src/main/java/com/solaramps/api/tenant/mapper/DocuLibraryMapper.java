package com.solaramps.api.tenant.mapper;

import com.solaramps.api.tenant.model.docu.DocuLibrary;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DocuLibraryMapper {

    public static DocuLibrary toDocuLibrary(DocuLibraryDTO docuLibraryDTO) {
        if (docuLibraryDTO == null) {
            return null;
        }
        return DocuLibrary.builder()
                .docuId(docuLibraryDTO.getDocuId())
                .docuType(docuLibraryDTO.getDocuType())
                .codeRefType(docuLibraryDTO.getCodeRefType())
                .codeRefId(docuLibraryDTO.getCodeRefId())
                .docuName(docuLibraryDTO.getDocuName())
                .notes(docuLibraryDTO.getNotes())
                .tags(docuLibraryDTO.getTags())
                .size(docuLibraryDTO.getSize())
                .format(docuLibraryDTO.getFormat())
                .uri(docuLibraryDTO.getUri())
                .status(docuLibraryDTO.getStatus())
                .visibilityKey(docuLibraryDTO.getVisibilityKey())
                .compKey(docuLibraryDTO.getCompKey())
                .resourceInterval(docuLibraryDTO.getResourceInterval())
                .lockedInd(docuLibraryDTO.getLockedInd())
                .linkToMeasure(docuLibraryDTO.getLinkToMeasure())
                .state(docuLibraryDTO.getState())
                .docuAlias(docuLibraryDTO.getDocuAlias())
                .digitallySigned(docuLibraryDTO.getDigitallySigned())
                .signRefNo(docuLibraryDTO.getSignRefNo())
                .build();
    }
    public static DocuLibrary toDocuLibrary(DocuMeasureDTO docuMeasureDTO) {
        if (docuMeasureDTO == null) {
            return null;
        }
        return DocuLibrary.builder()
//                .docuId(documentJSON.getDocuId())
//                .codeRefType(documentJSON.getCodeRefType())
//                .codeRefId(documentJSON.getCodeRefId())
//                .tags(documentJSON.getTags())
//                .size(documentJSON.getSize())
//                .status(documentJSON.getStatus())
//                .visibilityKey(documentJSON.getVisibilityKey())
//                .compKey(documentJSON.getCompKey())
//                .resourceInterval(documentJSON.getResourceInterval())
//                .digitallySigned(documentJSON.getDigitallySigned())
//                .signRefNo(documentJSON.getSignRefNo())
                .docuType(docuMeasureDTO.getType())
                .docuName(docuMeasureDTO.getMeasure())
                .notes(docuMeasureDTO.getNotes())
                .format(docuMeasureDTO.getFormat())
                .uri(docuMeasureDTO.getUri())
                .lockedInd(docuMeasureDTO.getLocked())
                .linkToMeasure(true)
                .state(docuMeasureDTO.getUri() != null ? "PUBLISHED" : "DRAFT")
                .docuAlias(docuMeasureDTO.getDocuAlias())
                .build();
    }

    public static DocuLibraryDTO toDocuLibraryDTO(DocuLibrary docuLibrary) {
        if (docuLibrary == null) {
            return null;
        }
        return DocuLibraryDTO.builder()
                .docuId(docuLibrary.getDocuId())
                .docuType(docuLibrary.getDocuType())
                .codeRefType(docuLibrary.getCodeRefType())
                .codeRefId(docuLibrary.getCodeRefId())
                .docuName(docuLibrary.getDocuName())
                .notes(docuLibrary.getNotes())
                .tags(docuLibrary.getTags())
                .size(docuLibrary.getSize())
                .format(docuLibrary.getFormat())
                .uri(docuLibrary.getUri())
                .status(docuLibrary.getStatus())
                .visibilityKey(docuLibrary.getVisibilityKey())
                .compKey(docuLibrary.getCompKey())
                .resourceInterval(docuLibrary.getResourceInterval())
                .lockedInd(docuLibrary.getLockedInd())
                .linkToMeasure(docuLibrary.getLinkToMeasure())
                .state(docuLibrary.getState())
                .docuAlias(docuLibrary.getDocuAlias())
                .digitallySigned(docuLibrary.getDigitallySigned())
                .signRefNo(docuLibrary.getSignRefNo())
                .organizationId(docuLibrary.getOrganization() != null ? docuLibrary.getOrganization().getId() : null)
                .entityId(docuLibrary.getEntity() != null ? docuLibrary.getEntity().getId() : null)
                .contractId(docuLibrary.getContract() != null ? docuLibrary.getContract().getId() : null)
                .volumeNames(docuLibrary.getDocuVolMap() != null && docuLibrary.getDocuVolMap().getDocuVolumes() != null ?
                        docuLibrary.getDocuVolMap().getDocuVolumes().stream().map(v -> v.getVolumeName()).collect(Collectors.toList()) : Collections.EMPTY_LIST)
                .moduleNames(docuLibrary.getDocuVolMap() != null && docuLibrary.getDocuVolMap().getDocuVolumes() != null ?
                        docuLibrary.getDocuVolMap().getDocuVolumes().stream()
                                .filter(v -> v.getDocuModule() != null).map(m -> m.getDocuModule().getModuleName())
                                .collect(Collectors.toList()) : Collections.EMPTY_LIST)
                .createdAt(docuLibrary.getCreatedAt())
                .build();
    }

    public static DocuLibrary toUpdatedDocuLibrary(DocuLibrary docuLibrary, DocuLibrary docuLibraryUpdate) {
        docuLibrary.setDocuType(docuLibraryUpdate.getDocuType() == null ? docuLibrary.getDocuType() : docuLibraryUpdate.getDocuType());
        docuLibrary.setCodeRefType(docuLibraryUpdate.getCodeRefType() == null ? docuLibrary.getCodeRefType() : docuLibraryUpdate.getCodeRefType());
        docuLibrary.setCodeRefId(docuLibraryUpdate.getCodeRefId() == null ? docuLibrary.getCodeRefId() : docuLibraryUpdate.getCodeRefId());
        docuLibrary.setDocuName(docuLibraryUpdate.getDocuName() == null ? docuLibrary.getDocuName() : docuLibraryUpdate.getDocuName());
        docuLibrary.setNotes(docuLibraryUpdate.getNotes() == null ? docuLibrary.getNotes() : docuLibraryUpdate.getNotes());
        docuLibrary.setTags(docuLibraryUpdate.getTags() == null ? docuLibrary.getTags() : docuLibraryUpdate.getTags());
        docuLibrary.setSize(docuLibraryUpdate.getSize() == null ? docuLibrary.getSize() : docuLibraryUpdate.getSize());
        docuLibrary.setFormat(docuLibraryUpdate.getFormat() == null ? docuLibrary.getFormat() : docuLibraryUpdate.getFormat());
        docuLibrary.setUri(docuLibraryUpdate.getUri() == null ? docuLibrary.getUri() : docuLibraryUpdate.getUri());
        docuLibrary.setStatus(docuLibraryUpdate.getStatus() == null ? docuLibrary.getStatus() : docuLibraryUpdate.getStatus());
        docuLibrary.setVisibilityKey(docuLibraryUpdate.getVisibilityKey() == null ? docuLibrary.getVisibilityKey() : docuLibraryUpdate.getVisibilityKey());
        docuLibrary.setCompKey(docuLibraryUpdate.getCompKey() == null ? docuLibrary.getCompKey() : docuLibraryUpdate.getCompKey());
        docuLibrary.setResourceInterval(docuLibraryUpdate.getResourceInterval() == null ? docuLibrary.getResourceInterval() : docuLibraryUpdate.getResourceInterval());
        docuLibrary.setLockedInd(docuLibraryUpdate.getLockedInd() == null ? docuLibrary.getLockedInd() : docuLibraryUpdate.getLockedInd());
        docuLibrary.setLinkToMeasure(docuLibraryUpdate.getLinkToMeasure() == null ? docuLibrary.getLinkToMeasure() : docuLibraryUpdate.getLinkToMeasure());
        docuLibrary.setState(docuLibraryUpdate.getState() == null ? docuLibrary.getState() : docuLibraryUpdate.getState());
        docuLibrary.setDocuAlias(docuLibraryUpdate.getDocuAlias() == null ? docuLibrary.getDocuAlias() : docuLibraryUpdate.getDocuAlias());
        docuLibrary.setDigitallySigned(docuLibraryUpdate.getDigitallySigned() == null ? docuLibrary.getDigitallySigned() : docuLibraryUpdate.getDigitallySigned());
        docuLibrary.setSignRefNo(docuLibraryUpdate.getSignRefNo() == null ? docuLibrary.getSignRefNo() : docuLibraryUpdate.getSignRefNo());
        return docuLibrary;
    }

    public static DocuLibrary toUpdatedDocuLibrary(DocuLibrary docuLibrary, DocuMeasureDTO docuMeasureDTO) {
        docuLibrary.setDocuType(docuMeasureDTO.getType() == null ? docuLibrary.getDocuType() : docuMeasureDTO.getType());
        docuLibrary.setDocuName(docuMeasureDTO.getMeasure() == null ? docuLibrary.getDocuName() : docuMeasureDTO.getMeasure());
        docuLibrary.setNotes(docuMeasureDTO.getNotes() == null ? docuLibrary.getNotes() : docuMeasureDTO.getNotes());
        docuLibrary.setFormat(docuMeasureDTO.getFormat() == null ? docuLibrary.getFormat() : docuMeasureDTO.getFormat());
        docuLibrary.setUri(docuMeasureDTO.getUri() == null ? docuLibrary.getUri() : docuMeasureDTO.getUri());
        docuLibrary.setLockedInd(docuMeasureDTO.getLocked() == null ? docuLibrary.getLockedInd() : docuMeasureDTO.getLocked());
        docuLibrary.setLinkToMeasure(true);
        docuLibrary.setState(docuLibrary.getUri() != null || docuMeasureDTO.getUri() != null ? "PUBLISHED" : "DRAFT");
        docuLibrary.setDocuAlias(docuMeasureDTO.getDocuAlias() == null ? docuLibrary.getDocuAlias() : docuMeasureDTO.getDocuAlias());
        return docuLibrary;
    }

    public static Set<DocuLibrary> toDocuLibraryes(List<DocuLibraryDTO> docuLibraryDTOs) {
        return docuLibraryDTOs.stream().map(DocuLibraryMapper::toDocuLibrary).collect(Collectors.toSet());
    }

    public static Set<DocuLibraryDTO> toDocuLibraryesSet(Set<DocuLibrary> docuLibraries) {
        return docuLibraries.stream().map(DocuLibraryMapper::toDocuLibraryDTO).collect(Collectors.toSet());
    }

    public static List<DocuLibraryDTO> toDocuLibraryDTOs(List<DocuLibrary> docuLibrary) {
        return docuLibrary.stream().map(DocuLibraryMapper::toDocuLibraryDTO).collect(Collectors.toList());
    }

    public static Set<DocuLibrary> toDocuLibraryes(Set<DocuLibraryDTO> docuLibraryDTOs) {
        return docuLibraryDTOs.stream().map(a -> toDocuLibrary(a)).collect(Collectors.toSet());
    }
}
