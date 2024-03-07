package com.solaramps.api.tenant.service.crud;

import com.solaramps.api.exception.NotFoundException;
import com.solaramps.api.tenant.mapper.BaseResponse;
import com.solaramps.api.tenant.mapper.DocuVolumeDTO;
import com.solaramps.api.tenant.mapper.DocuVolumeMapper;
import com.solaramps.api.tenant.model.docu.DocuModule;
import com.solaramps.api.tenant.model.docu.DocuVolume;
import com.solaramps.api.tenant.repository.DocuTypeRepository;
import com.solaramps.api.tenant.repository.DocuVolMapRepository;
import com.solaramps.api.tenant.repository.DocuVolumeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.solaramps.api.constants.Constants.Message.Service.CRUD.*;

@Service
public class DocuVolumeService implements CrudService<DocuVolume>, DocuVolumeServiceExt {

    private final DocuVolumeRepository repository;
    private final DocuTypeRepository docuTypeRepository;
    private final DocuVolMapRepository docuVolMapRepository;
    private final DocuModuleService docuModuleService;

    public DocuVolumeService(DocuVolumeRepository repository, DocuTypeRepository docuTypeRepository,
                             DocuVolMapRepository docuVolMapRepository, DocuModuleService docuModuleService) {
        this.repository = repository;
        this.docuTypeRepository = docuTypeRepository;
        this.docuVolMapRepository = docuVolMapRepository;
        this.docuModuleService = docuModuleService;
    }

    @Override
    public DocuVolume save(DocuVolume obj) {
        return repository.save(obj);
    }

    @Override
    public DocuVolume update(DocuVolume obj) {
        return repository.save(obj);
    }

    @Override
    public List<DocuVolume> getAll() {
        return repository.findAll();
    }

    @Override
    public DocuVolume getById(Long id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException(DocuVolume.class, id));
    }

    @Override
    public void deleteById(Long id) {
        try {
            repository.deleteById(id);
        } catch (Exception e) {
            throw new NotFoundException(DocuVolume.class, id);
        }
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }

    @Override
    public BaseResponse saveResponse(DocuVolume obj) {
        return BaseResponse.builder()
                .data(DocuVolumeMapper.toDocuVolumeDTO(repository.save(obj)))
                .code(200)
                .build();
    }

    @Override
    public BaseResponse updateResponse(DocuVolume obj) {
        return BaseResponse.builder()
                .data(DocuVolumeMapper.toDocuVolumeDTO(repository.save(obj)))
                .code(200)
                .build();
    }

    @Override
    public BaseResponse getAllResponse() {
        return BaseResponse.builder()
                .data(DocuVolumeMapper.toDocuVolumeDTOs(repository.findAll()))
                .code(200)
                .build();
    }

    @Override
    public BaseResponse getByIdResponse(Long id) {
        return BaseResponse.builder()
                .data(DocuVolumeMapper.toDocuVolumeDTO(repository.findById(id).orElseThrow(() -> new NotFoundException(DocuVolume.class, id))))
                .code(200)
                .build();
    }

    @Override
    public BaseResponse deleteByIdResponse(Long id) {
        try {
            repository.deleteById(id);
        } catch (Exception e) {
            throw new NotFoundException(DocuVolume.class, id);
        }
        return BaseResponse.builder()
                .message(String.format(String.format(DELETE_SUCCESS, "DocuVolume", id)))
                .code(200)
                .build();
    }

    @Override
    public BaseResponse deleteAllResponse() {
        repository.deleteAll();
        return BaseResponse.builder()
                .message(String.format(DELETE_ALL_SUCCESS, "DocuVolume"))
                .code(200)
                .build();
    }

    @Override
    public BaseResponse saveResponse(DocuVolumeDTO docuVolumeDTO) {
        DocuModule docuModule = docuModuleService.getById(docuVolumeDTO.getDocuModuleId());
        DocuVolume docuVolume = DocuVolumeMapper.toDocuVolume(docuVolumeDTO);
        docuVolume.setDocuModule(docuModule);
        docuVolume = repository.save(docuVolume);
        return BaseResponse.builder()
                .code(201)
                .message(String.format(SAVE_SUCCESS, "DocuVolume"))
                .data(DocuVolumeMapper.toDocuVolumeDTO(docuVolume))
                .build();
    }

    @Override
    public BaseResponse updateResponse(DocuVolumeDTO docuVolumeDTO) {
        if (docuVolumeDTO.getId() == null) {
            return new BaseResponse(HttpStatus.UNPROCESSABLE_ENTITY.value(), "DocuVolume id is required");
        } else {
            Optional<DocuVolume> docVolMapOptional = repository.findById(docuVolumeDTO.getId());
            if (!docVolMapOptional.isPresent()) {
                return new BaseResponse(HttpStatus.NOT_FOUND.value(),"Invalid DocuVolume id: " + docuVolumeDTO.getId());
            }
            DocuModule docuModule = docuModuleService.getById(docuVolumeDTO.getDocuModuleId());
            DocuVolume docuVolume = DocuVolumeMapper.toUpdatedDocuVolumeMap(docVolMapOptional.get(), docuVolumeDTO);
            docuVolume.setDocuModule(docuModule);
            docuVolume = repository.save(docuVolume);
            return BaseResponse.builder()
                    .code(200)
                    .message(String.format(UPDATE_SUCCESS, "DocuVolume", docuVolumeDTO.getId()))
                    .data(DocuVolumeMapper.toDocuVolumeDTO(docuVolume))
                    .build();
        }
    }

    @Override
    public List<DocuVolume> getByIdIn(List<Long> ids) {
        return repository.findByIdIn(ids);
    }
}
