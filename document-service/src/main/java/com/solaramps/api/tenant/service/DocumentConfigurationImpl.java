package com.solaramps.api.tenant.service;

import com.solaramps.api.exception.NotFoundException;
import com.solaramps.api.tenant.mapper.BaseResponse;
import com.solaramps.api.tenant.mapper.DocuVolMapDTO;
import com.solaramps.api.tenant.mapper.DocuVolMapMapper;
import com.solaramps.api.tenant.model.docu.DocuLibrary;
import com.solaramps.api.tenant.model.docu.DocuType;
import com.solaramps.api.tenant.model.docu.DocuVolMap;
import com.solaramps.api.tenant.model.docu.DocuVolume;
import com.solaramps.api.tenant.repository.DocuLibraryRepository;
import com.solaramps.api.tenant.repository.DocuTypeRepository;
import com.solaramps.api.tenant.repository.DocuVolMapRepository;
import com.solaramps.api.tenant.service.crud.DocuTypeService;
import com.solaramps.api.tenant.service.crud.DocuVolumeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class DocumentConfigurationImpl implements DocumentConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentConfigurationImpl.class);
    private final DocuVolMapRepository docuVolMapRepository;
    private final DocuLibraryRepository docuLibraryRepository;
    private final DocuTypeService docuTypeService;
    private final DocuVolumeService docuVolumeService;
    private final DocuTypeRepository docuTypeRepository;

    public DocumentConfigurationImpl(DocuVolMapRepository docuVolMapRepository,
                                     DocuLibraryRepository docuLibraryRepository, DocuTypeService docuTypeService,
                                     DocuVolumeService docuVolumeService,
                                     DocuTypeRepository docuTypeRepository) {
        this.docuVolMapRepository = docuVolMapRepository;
        this.docuLibraryRepository = docuLibraryRepository;
        this.docuTypeService = docuTypeService;
        this.docuVolumeService = docuVolumeService;
        this.docuTypeRepository = docuTypeRepository;
    }

    @Override
    public BaseResponse saveDocumentTypeVolumeMap(DocuVolMapDTO docuVolMapDTO) {
        DocuVolMap docuVolMap;
        Long docuTypeId = docuVolMapDTO.getDocuTypeId();
        DocuType docuType;
        if (docuTypeId != null) {
            docuType = docuTypeService.getById(docuTypeId);
            Optional<DocuVolMap> docuVolMapOptional = docuVolMapRepository.findByDocuType(docuType);
            if (docuVolMapOptional.isPresent()) {
                docuVolMap = DocuVolMapMapper.toUpdatedDocVolMap(docuVolMapOptional.get(), docuVolMapDTO);
            } else {
                docuVolMap = DocuVolMapMapper.toDocVolMap(docuVolMapDTO);
            }

            List<Long> docuVolumeIds = docuVolMapDTO.getDocuVolumeIds();
            List<DocuVolume> docuVolumes;
            if (docuVolumeIds != null && !docuVolumeIds.isEmpty()) {
                docuVolumes = docuVolumeService.getByIdIn(docuVolumeIds);
                docuVolMap.setDocuVolumes(new HashSet<>(docuVolumes));
            }
        } else {
            return new BaseResponse(HttpStatus.UNPROCESSABLE_ENTITY.value(), "docuTypeId id is required");
        }
        docuVolMap.setDocuType(docuType);
        docuVolMap = docuVolMapRepository.save(docuVolMap);
        return new BaseResponse(HttpStatus.CREATED.value(), "CREATED", DocuVolMapMapper.toDocVolMapDTO(docuVolMap));
    }

    @Override
    public BaseResponse updateDocumentTypeVolumeMap(DocuVolMapDTO docuVolMapDTO) {
        if (docuVolMapDTO.getId() == null) {
            return new BaseResponse(HttpStatus.UNPROCESSABLE_ENTITY.value(), "DocVolMap id is required");
        } else {
            Optional<DocuVolMap> docVolMapOptional = docuVolMapRepository.findById(docuVolMapDTO.getId());
            if (!docVolMapOptional.isPresent()) {
                return new BaseResponse(HttpStatus.NOT_FOUND.value(),
                        "Invalid DocVolMap id: " + docuVolMapDTO.getId());
            }
            DocuVolMap docuVolMap = docVolMapOptional.get();
            docuVolMap = DocuVolMapMapper.toUpdatedDocVolMap(docuVolMap, docuVolMapDTO); // TODO: set updatedBy too
            Long docuTypeId = docuVolMapDTO.getDocuTypeId();
            DocuType docuType;
            if (docuTypeId != null) {
                docuType = docuTypeService.getById(docuTypeId);
                docuVolMap.setDocuType(docuType);
            } else {
                return new BaseResponse(HttpStatus.UNPROCESSABLE_ENTITY.value(), "docuTypeId id is required");
            }
            List<Long> docuVolumeIds = docuVolMapDTO.getDocuVolumeIds();
            if (docuVolumeIds != null && !docuVolumeIds.isEmpty()) {
                List<DocuVolume> docuVolumes = docuVolumeService.getByIdIn(docuVolumeIds);
                docuVolMap.setDocuVolumes(new HashSet<>(docuVolumes));
            }
            docuVolMap.setDocuType(docuType);
            docuVolMap = docuVolMapRepository.save(docuVolMap);
            return new BaseResponse(HttpStatus.OK.value(), "UPDATED", DocuVolMapMapper.toDocVolMapDTO(docuVolMap));
        }
    }

    @Override
    public BaseResponse getDocumentTypeVolumeMap(Long docuVolMapId) {
        return new BaseResponse(HttpStatus.OK.value(),
                DocuVolMapMapper.toDocVolMapDTO(docuVolMapRepository.findById(docuVolMapId).orElseThrow(() ->
                        new NotFoundException(DocuVolMap.class, docuVolMapId))));
    }

    @Override
    public BaseResponse mapDocumentWithTypeAndVolume(Long docuLibraryId, Long docVolMapId) {
        Optional<DocuLibrary> docuLibraryOptional = docuLibraryRepository.findById(docuLibraryId);
        if (!docuLibraryOptional.isPresent()) {
            return new BaseResponse(HttpStatus.NOT_FOUND.value(), "DocuLibrary not found with id: " + docuLibraryId);
        }
        DocuLibrary docuLibrary = docuLibraryOptional.get();
        Optional<DocuVolMap> docVolMapIdOptional = docuVolMapRepository.findById(docVolMapId);
        if (!docVolMapIdOptional.isPresent()) {
            return new BaseResponse(HttpStatus.NOT_FOUND.value(), "DocVolMap not found with id: " + docVolMapId);
        }
        DocuVolMap docuVolMap = docVolMapIdOptional.get();
        docuLibrary.setDocuVolMap(docuVolMap);
        return new BaseResponse(HttpStatus.OK.value(), "SUCCESS");
    }
}
