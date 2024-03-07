package com.solaramps.api.tenant.service;

import com.solaramps.api.helper.Utility;
import com.solaramps.api.tenant.mapper.*;
import com.solaramps.api.tenant.model.docu.DocuLibrary;
import com.solaramps.api.tenant.model.docu.DocuType;
import com.solaramps.api.tenant.model.docu.DocuVolMap;
import com.solaramps.api.tenant.model.docu.DocuVolume;
import com.solaramps.api.tenant.repository.DocuLibraryRepository;
import com.solaramps.api.tenant.repository.DocuLibrarySpecification;
import com.solaramps.api.tenant.repository.DocuVolMapRepository;
import com.solaramps.api.tenant.service.crud.DocuTypeService;
import com.solaramps.api.tenant.service.crud.DocuVolumeService;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DocumentSearchImpl implements DocumentSearch {

    private final DocuLibraryRepository docuLibraryRepository;
    private final DocuVolMapRepository docuVolMapRepository;
    private final DocuTypeService docuTypeService;
    private final DocuVolumeService docuVolumeService;

    public DocumentSearchImpl(DocuLibraryRepository docuLibraryRepository, DocuVolMapRepository docuVolMapRepository,
                              DocuTypeService docuTypeService, DocuVolumeService docuVolumeService) {
        this.docuLibraryRepository = docuLibraryRepository;
        this.docuVolMapRepository = docuVolMapRepository;
        this.docuTypeService = docuTypeService;
        this.docuVolumeService = docuVolumeService;
    }

    @Override
    public BaseResponse search(String string, Long docuTypeId, List<Long> docuVolumeIds, List<Long> docuModuleIds,
                               String fromDateString, String toDateString, int pageNumber, Integer pageSize, String sort,
                               Integer sortDirection) {
        LocalDateTime fromDate = null;
        LocalDateTime toDate = null;

        if (fromDateString != null) {
            fromDate = Utility.getLocalDateTime(fromDateString);
        }
        if (toDate != null) {
            toDate = Utility.getLocalDateTime(toDateString).plusDays(1);
        }
        // "docuVolMap.docuVolumes.volumeName" "docuVolMap.docuVolumes.docuModule.moduleName" "docuName" "createdAt"
        if ("volumeName".equalsIgnoreCase(sort)) {
            sort = "docuVolMap.docuVolumes.volumeName";
        } else if ("moduleName".equalsIgnoreCase(sort)) {
            sort = "docuVolMap.docuVolumes.docuModule.moduleName";
        } else if ("documentName".equalsIgnoreCase(sort)) {
            sort = "docuName";
        } else if ("uploadedOn".equalsIgnoreCase(sort)) {
            sort = "createdAt";
        }
        Pageable pageable = Utility.getPageable(pageNumber, pageSize, sort, sortDirection);
        Page<DocuLibrary> result;
        Specification<DocuLibrary> specification = null;
        if (docuTypeId != null || (docuVolumeIds != null && !docuVolumeIds.isEmpty()) || (docuModuleIds != null && !docuModuleIds.isEmpty())) {
            if (docuTypeId != null && docuVolumeIds != null && !docuVolumeIds.isEmpty() && docuModuleIds != null && !docuModuleIds.isEmpty()) {
                specification = Specification.where(DocuLibrarySpecification.findByDocuType(docuTypeId))
                        .and(Specification.where(DocuLibrarySpecification.findByDocuVolumeIn(docuVolumeIds)))
                        .and(Specification.where(DocuLibrarySpecification.findByDocuModuleIn(docuModuleIds)));
            } else if ((docuTypeId != null) && (docuVolumeIds != null) && !docuVolumeIds.isEmpty()) {
                specification = Specification.where(DocuLibrarySpecification.findByDocuType(docuTypeId))
                        .and(Specification.where(DocuLibrarySpecification.findByDocuVolumeIn(docuVolumeIds)));
            } else if ((docuTypeId != null) && (docuModuleIds != null) && !docuModuleIds.isEmpty()) {
                specification = Specification.where(DocuLibrarySpecification.findByDocuType(docuTypeId))
                        .and(Specification.where(DocuLibrarySpecification.findByDocuModuleIn(docuModuleIds)));
            } else if ((docuVolumeIds != null) && !docuVolumeIds.isEmpty() && (docuModuleIds != null) && !docuModuleIds.isEmpty()) {
                specification = Specification.where(DocuLibrarySpecification.findByDocuVolumeIn(docuVolumeIds))
                        .and(Specification.where(DocuLibrarySpecification.findByDocuModuleIn(docuModuleIds)));
            } else if (docuTypeId != null) {
                specification = Specification.where(DocuLibrarySpecification.findByDocuType(docuTypeId));
            } else if ((docuVolumeIds != null) && !docuVolumeIds.isEmpty()) {
                specification = Specification.where(Specification.where(DocuLibrarySpecification.findByDocuVolumeIn(docuVolumeIds)));
            } else if ((docuModuleIds != null) && !docuModuleIds.isEmpty()) {
                specification = Specification.where(DocuLibrarySpecification.findByDocuModuleIn(docuModuleIds));
            }
            if (StringUtils.isNumeric(string)) {
                specification = specification.and(Specification.where(DocuLibrarySpecification
                        .withFieldValue("docuId", Long.parseLong(string))
                        .and(Specification.where(DocuLibrarySpecification.textInAllColumns(string)))));
            } else if (string != null) {
                specification = specification.and(Specification
                        .where(DocuLibrarySpecification.textInAllColumns(string)));
            }
            if (fromDate != null) {
                specification = specification.and(Specification.where(DocuLibrarySpecification.findByFromDateGE(fromDate)));
            }
            if (toDate != null) {
                specification = specification.and(Specification.where(DocuLibrarySpecification.findByToDateLT(toDate)));
            }
            result = docuLibraryRepository.findAll(specification, pageable);
        } else {
            if (string == null) {
                if (fromDate != null || toDate != null) {
                    if (fromDate != null) {
                        if (specification == null) {
                            specification = Specification.where(DocuLibrarySpecification.findByFromDateGE(fromDate));
                        } else {
                            specification = specification.and(Specification.where(DocuLibrarySpecification.findByFromDateGE(fromDate)));
                        }
                    }
                    if (toDate != null) {
                        if (specification == null) {
                            specification = Specification.where(DocuLibrarySpecification.findByToDateLT(toDate));
                        } else {
                            specification = specification.and(Specification.where(DocuLibrarySpecification.findByToDateLT(toDate)));
                        }
                    }
                    result = docuLibraryRepository.findAll(specification, pageable);
                } else {
                    result = docuLibraryRepository.findAll(pageable);
                }
            } else {
                if (StringUtils.isNumeric(string)) {
                    specification = Specification.where(DocuLibrarySpecification
                            .withFieldValue("docuId", Long.parseLong(string))
                            .and(Specification.where(DocuLibrarySpecification.textInAllColumns(string))));
                } else {
                    specification = Specification.where(DocuLibrarySpecification.textInAllColumns(string));
                }
                if (fromDate != null) {
                    specification = specification.and(Specification.where(DocuLibrarySpecification.findByFromDateGE(fromDate)));
                }
                if (toDate != null) {
                    specification = specification.and(Specification.where(DocuLibrarySpecification.findByToDateLT(toDate)));
                }
                result = docuLibraryRepository.findAll(specification, pageable);
            }
        }
        List<DocuLibraryDTO> docuLibraryDTOListExpanded = new ArrayList<>();
        result.getContent().forEach(docuLibrary -> {
            if (docuLibrary.getDocuVolMap() != null && docuLibrary.getDocuVolMap().getDocuVolumes() != null &&
                    docuLibrary.getDocuVolMap().getDocuVolumes().size() > 0) {
                docuLibrary.getDocuVolMap().getDocuVolumes().forEach(volume -> {
                    DocuLibraryDTO docuLibraryDTO = DocuLibraryMapper.toDocuLibraryDTO(docuLibrary);
                    docuLibraryDTO.setVolumeName(volume.getVolumeName());
                    docuLibraryDTO.setModuleName(volume.getDocuModule() != null ? volume.getDocuModule().getModuleName() : null);
                    docuLibraryDTOListExpanded.add(docuLibraryDTO);
                });
            } else {
                docuLibraryDTOListExpanded.add(DocuLibraryMapper.toDocuLibraryDTO(docuLibrary));
            }
        });

        PagedDocuLibraryDTO pagedDocuLibraryDTO = PagedDocuLibraryDTO.builder()
                .totalItems(result.getTotalElements())
                .documents(docuLibraryDTOListExpanded)
                .build();
        return new BaseResponse(HttpStatus.OK.value(), "SUCCESS", pagedDocuLibraryDTO);
    }

    @Override
    public BaseResponse getSummaryByDocuType(Long docuTypeId) {
        DocuType docuType = docuTypeService.getById(docuTypeId);
        List<DocuVolMap> docuVolMaps = docuVolMapRepository.findAllByDocuType(docuType);
        Integer maxFileSizeAllowed = docuVolMaps.stream().filter(map -> map.getMaxFileSize() != null).mapToInt(map -> map.getMaxFileSize()).reduce(0, Integer::sum);
        List<DocuVolumeDTO> volumesAttached = new ArrayList<>();
        List<DocuVolume> volumes = docuVolMaps.stream().flatMap(docuVolMap -> docuVolMap.getDocuVolumes().stream()).collect(Collectors.toList());
        volumes.forEach(volume -> {
            DocuVolumeDTO docuVolumeDTO = DocuVolumeMapper.toDocuVolumeDTO(volume);
            docuVolumeDTO.setNumberOfDocuments(docuLibraryRepository.findDocuCountByDocuVolMapIds(new ArrayList<>(volume.getDocuVolMaps().stream()
                    .map(m -> m.getId()).collect(Collectors.toSet()))));
            volumesAttached.add(docuVolumeDTO);
        });
        Long totalDocuments = docuVolMaps.stream().flatMap(docuVolMap -> docuVolMap.getDocuLibraries().stream()).collect(Collectors.counting());
        return new BaseResponse(HttpStatus.OK.value(), "SUCCESS", DocuTypeSummaryDTO.builder()
                .documentTypeName(docuType.getDocuType())
                .required(true) // TODO: check
                .maxFileSizeAllowed(maxFileSizeAllowed)
                .createdBy(docuType.getCreatedBy() != null ? docuType.getCreatedBy().getFirstName() + " " + docuType.getCreatedBy().getLastName() : null) // TODO: check
                .createdAt(docuType.getCreatedAt())
                .volumesAttached(volumesAttached)
                .supportedFileTypes(getSupportedFileTypeCounts(docuVolMaps))
                .totalDocuments(totalDocuments)
                .totalDocumentsInAllTypes(docuVolMapRepository.findAll().stream()
                        .flatMap(docuVolMap -> docuVolMap.getDocuLibraries().stream()).collect(Collectors.counting()))
                .build());
    }

    private List<SupportedFileTypeCount> getSupportedFileTypeCounts(List<DocuVolMap> docuVolMaps) {
        List<String> supportedFileTypes = docuVolMaps.stream().flatMap(docuVolMap -> docuVolMap.getAllowedFileType().stream()).collect(Collectors.toList());
        List<SupportedFileTypeCount> supportedFileTypesCount = new ArrayList<>();
        List<String> extensions = docuVolMaps.stream().flatMap(docuVolMap -> docuVolMap.getDocuLibraries().stream()).filter(d -> d.getUri() != null).map(m -> m.getUri()).map(m -> FilenameUtils.getExtension(m)).filter(f -> !f.isEmpty()).collect(Collectors.toList());
        supportedFileTypes.forEach(type -> supportedFileTypesCount.add(SupportedFileTypeCount.builder()
                .name(type)
                .number(Collections.frequency(extensions, type))
                .build()));
        return supportedFileTypesCount;
    }

    @Override
    public BaseResponse getSummaryByDocuVolume(Long docuVolumeId) {
        DocuVolume docuVolume = docuVolumeService.getById(docuVolumeId);
        List<DocuVolMap> docuVolMaps = docuVolMapRepository.findAllByDocuVolume(docuVolume);
        Integer maxFileSizeAllowed = docuVolMaps.stream().filter(map -> map.getMaxFileSize() != null) .mapToInt(map -> map.getMaxFileSize()).reduce(0, Integer::sum);
        List<DocuTypeDTO> documentTypes = docuVolMaps.stream().map(docuVolMap -> docuVolMap.getDocuType()).map(m -> DocuTypeMapper.toDocuTypeDTO(m)).collect(Collectors.toList());
        documentTypes.forEach(docType -> docType.setNumberOfDocuments(docuLibraryRepository.findDocuCountByDocuTypeId(docType.getId())));
        Long totalDocuments = docuVolMaps.stream().flatMap(docuVolMap -> docuVolMap.getDocuLibraries().stream()).collect(Collectors.counting());
        return new BaseResponse(HttpStatus.OK.value(), "SUCCESS", DocuVolumeSummaryDTO.builder()
                .volumeName(docuVolume.getVolumeName())
                .numberOfDocumentTypes(docuVolMaps.size())
                .maxFileSizeAllowed(maxFileSizeAllowed)
                .createdBy(docuVolume.getCreatedBy() != null ? docuVolume.getCreatedBy().getFirstName() + " " + docuVolume.getCreatedBy().getLastName() : null) // TODO: check
                .createdAt(docuVolume.getCreatedAt())
                .documentTypes(documentTypes)
                .supportedFileTypes(getSupportedFileTypeCounts(docuVolMaps))
                .totalDocuments(totalDocuments)
                .build());
    }
}
