package com.solaramps.api.tenant.service.crud;

import com.solaramps.api.exception.NotFoundException;
import com.solaramps.api.helper.Utility;
import com.solaramps.api.tenant.mapper.*;
import com.solaramps.api.tenant.model.docu.DocuType;
import com.solaramps.api.tenant.model.docu.DocuVolMap;
import com.solaramps.api.tenant.model.docu.DocuVolume;
import com.solaramps.api.tenant.repository.DocuLibraryRepository;
import com.solaramps.api.tenant.repository.DocuTypeRepository;
import com.solaramps.api.tenant.repository.DocuVolMapRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.solaramps.api.constants.Constants.Message.Service.CRUD.*;

@Service
public class DocuTypeService implements CrudService<DocuType>, DocuTypeServiceExt {

    private final DocuTypeRepository repository;
    private final DocuVolMapRepository docuVolMapRepository;
    private final DocuLibraryRepository docuLibraryRepository;

    public DocuTypeService(DocuTypeRepository repository, DocuVolMapRepository docuVolMapRepository, DocuLibraryRepository docuLibraryRepository) {
        this.repository = repository;
        this.docuVolMapRepository = docuVolMapRepository;
        this.docuLibraryRepository = docuLibraryRepository;
    }

    @Override
    public DocuType save(DocuType obj) {
        return repository.save(obj);
    }

    @Override
    public DocuType update(DocuType obj) {
        return repository.save(obj);
    }

    @Override
    public List<DocuType> getAll() {
        return repository.findAll();
    }

    @Override
    public DocuType getById(Long id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException(DocuType.class, id));
    }

    @Override
    public void deleteById(Long id) {
        try {
            repository.deleteById(id);
        } catch (Exception e) {
            throw new NotFoundException(DocuType.class, id);
        }
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }

    @Override
    public BaseResponse saveResponse(DocuType obj) {
        return BaseResponse.builder()
                .data(DocuTypeMapper.toDocuTypeDTO(repository.save(obj)))
                .message(String.format(SAVE_SUCCESS, "DocuType"))
                .code(200)
                .build();
    }

    @Override
    public BaseResponse updateResponse(DocuType obj) {
        return BaseResponse.builder()
                .data(DocuTypeMapper.toDocuTypeDTO(repository.save(obj)))
                .message(String.format(UPDATE_SUCCESS, "DocuType", obj.getId()))
                .code(200)
                .build();
    }

    @Override
    public BaseResponse getAllResponse() {
        List<DocuType> docuTypes = repository.findAll();
        List<DocuTypeDTO> docuTypeDTOs = new ArrayList<>();
        docuTypes.forEach(docuType -> {
            Optional<DocuVolMap> allByDocuTypeOptional = docuVolMapRepository.findByDocuType(docuType);
            if (allByDocuTypeOptional.isPresent()) {
                docuType.setDocuVolMap(allByDocuTypeOptional.get());
            }
            DocuTypeDTO docuTypeDTO = DocuTypeMapper.toDocuTypeDTO(docuType);
            docuTypeDTO.setNumberOfDocuments(docuLibraryRepository.findDocuCountByDocuTypeId(docuType.getId()));

            Set<DocuVolume> volumes = null;
            Set<DocuVolumeDTO> volumeDTOs = new HashSet<>();
            if (docuType.getDocuVolMap() != null && docuType.getDocuVolMap().getDocuVolumes() != null) {
                volumes = docuType.getDocuVolMap().getDocuVolumes();
                volumes.forEach(volume -> {
                    DocuVolumeDTO docuVolumeDTO = DocuVolumeMapper.toDocuVolumeDTO(volume);
                    docuVolumeDTO.setNumberOfDocuments(docuLibraryRepository.findDocuCountByDocuVolMapIds(new ArrayList<>(volume.getDocuVolMaps().stream()
                            .map(m -> m.getId()).collect(Collectors.toSet()))));
                    volumeDTOs.add(docuVolumeDTO);
                });
            }
            docuTypeDTO.setVolumes(volumeDTOs);
            docuTypeDTOs.add(docuTypeDTO);
        });
        return BaseResponse.builder()
                .data(docuTypeDTOs)
                .code(200)
                .build();
    }

    @Override
    public BaseResponse getByIdResponse(Long id) {
        return BaseResponse.builder()
                .data(DocuTypeMapper.toDocuTypeDTO(repository.findById(id).orElseThrow(() -> new NotFoundException(DocuType.class, id))))
                .code(200)
                .build();
    }

    @Override
    public BaseResponse deleteByIdResponse(Long id) {
        try {
            repository.deleteById(id);
        } catch (Exception e) {
            throw new NotFoundException(DocuType.class, id);
        }
        return BaseResponse.builder()
                .message(String.format(String.format(DELETE_SUCCESS, "DocuType", id)))
                .code(200)
                .build();
    }

    @Override
    public BaseResponse deleteAllResponse() {
        repository.deleteAll();
        return BaseResponse.builder()
                .message(String.format(DELETE_ALL_SUCCESS, "DocuType"))
                .code(200)
                .build();
    }

    @Override
    public BaseResponse getAllDocuTypes(int pageNumber, Integer pageSize, String sort) {
        Pageable pageable = Utility.getPageable(pageNumber, pageSize, sort, null);
        Page<DocuType> result = repository.findAll(pageable);
        PagedDocuTypeDTO pagedDocuTypeDTO = PagedDocuTypeDTO.builder()
                .totalItems(result.getTotalElements())
                .documents(DocuTypeMapper.toDocuTypeDTOs(result.getContent()))
                .build();
        return new BaseResponse(HttpStatus.OK.value(), "SUCCESS", pagedDocuTypeDTO);
    }
}
