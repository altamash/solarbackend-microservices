package com.solaramps.api.tenant.service.crud;

import com.solaramps.api.exception.NotFoundException;
import com.solaramps.api.tenant.mapper.BaseResponse;
import com.solaramps.api.tenant.mapper.DocuLibraryDTO;
import com.solaramps.api.tenant.mapper.DocuLibraryMapper;
import com.solaramps.api.tenant.model.docu.DocuLibrary;
import com.solaramps.api.tenant.model.docu.DocuVolMap;
import com.solaramps.api.tenant.repository.DocuLibraryRepository;
import com.solaramps.api.tenant.repository.DocuVolMapRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.solaramps.api.constants.Constants.Message.Service.CRUD.*;

@Service
public class DocuLibraryService implements CrudService<DocuLibrary> {

    private final DocuLibraryRepository repository;
    private final DocuVolMapRepository docuVolMapRepository;

    public DocuLibraryService(DocuLibraryRepository repository, DocuVolMapRepository docuVolMapRepository) {
        this.repository = repository;
        this.docuVolMapRepository = docuVolMapRepository;
    }

    @Override
    public DocuLibrary save(DocuLibrary obj) {
        return repository.save(obj);
    }

    @Override
    public DocuLibrary update(DocuLibrary obj) {
        return repository.save(obj);
    }

    @Override
    public List<DocuLibrary> getAll() {
        return repository.findAll();
    }

    @Override
    public DocuLibrary getById(Long id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException(DocuLibrary.class, id));
    }

    @Override
    public void deleteById(Long id) {
        try {
            repository.deleteById(id);
        } catch (Exception e) {
            throw new NotFoundException(DocuLibrary.class, id);
        }
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }

    public BaseResponse saveResponse(DocuLibraryDTO obj, Long docVolMapId) {
        DocuLibrary docuLibrary = DocuLibraryMapper.toDocuLibrary(obj);
        if (docVolMapId != null) {
            Optional<DocuVolMap> docVolMapOptional = docuVolMapRepository.findById(docVolMapId);
            if (!docVolMapOptional.isPresent()) {
                return BaseResponse.builder()
                        .code(404).message(notFound(DocuVolMap.class.getSimpleName(), docVolMapId)).build();
            }
            docuLibrary.setDocuVolMap(docVolMapOptional.get());
        }
        return BaseResponse.builder()
                .code(200)
                .message(String.format(SAVE_SUCCESS, "DocuLibrary"))
                .data(DocuLibraryMapper.toDocuLibraryDTO(repository.save(docuLibrary)))
                .build();
    }

    public BaseResponse updateResponse(DocuLibraryDTO obj, Long docVolMapId) {
        if (obj.getDocuId() == null) {
            return BaseResponse.builder().code(422).message(ID_REQUIRED).build();
        }
        Optional<DocuLibrary> docuLibraryDBOptional = repository.findById(obj.getDocuId());
        if (!docuLibraryDBOptional.isPresent()) {
            return BaseResponse.builder()
                    .code(404).message(notFound(DocuLibrary.class.getSimpleName(), obj.getDocuId())).build();
        }
        DocuLibrary docuLibrary = DocuLibraryMapper.toDocuLibrary(obj);
        docuLibrary = DocuLibraryMapper.toUpdatedDocuLibrary(docuLibraryDBOptional.get(), docuLibrary);
        if (docVolMapId != null) {
            Optional<DocuVolMap> docVolMapOptional = docuVolMapRepository.findById(docVolMapId);
            if (!docVolMapOptional.isPresent()) {
                return BaseResponse.builder()
                        .code(404).message(notFound(DocuVolMap.class.getSimpleName(), docVolMapId)).build();
            }
            docuLibrary.setDocuVolMap(docVolMapOptional.get());
        }
        return BaseResponse.builder()
                .code(200)
                .message(String.format(UPDATE_SUCCESS, "DocuLibrary", obj.getDocuId()))
                .data(DocuLibraryMapper.toDocuLibraryDTO(repository.save(docuLibrary)))
                .build();
    }

    @Override
    public BaseResponse saveResponse(DocuLibrary obj) {
        return BaseResponse.builder()
                .code(200)
                .message(String.format(SAVE_SUCCESS, "DocuLibrary"))
                .data(DocuLibraryMapper.toDocuLibraryDTO(repository.save(obj)))
                .build();
    }

    @Override
    public BaseResponse updateResponse(DocuLibrary obj) {
        return BaseResponse.builder()
                .code(200)
                .message(String.format(UPDATE_SUCCESS, "DocuLibrary", obj.getDocuId()))
                .data(DocuLibraryMapper.toDocuLibraryDTO(repository.save(obj)))
                .build();
    }

    @Override
    public BaseResponse getAllResponse() {
        return BaseResponse.builder()
                .code(200)
                .data(DocuLibraryMapper.toDocuLibraryDTOs(repository.findAll()))
                .build();
    }

    @Override
    public BaseResponse getByIdResponse(Long id) {
        return BaseResponse.builder()
                .code(200)
                .data(DocuLibraryMapper.toDocuLibraryDTO(repository.findById(id).orElseThrow(() -> new NotFoundException(DocuLibrary.class, id))))
                .build();
    }

    @Override
    public BaseResponse deleteByIdResponse(Long id) {
        try {
            repository.deleteById(id);
        } catch (Exception e) {
            throw new NotFoundException(DocuLibrary.class, id);
        }
        return BaseResponse.builder()
                .code(200)
                .message(String.format(String.format(DELETE_SUCCESS, "DocuLibrary", id)))
                .build();
    }

    @Override
    public BaseResponse deleteAllResponse() {
        repository.deleteAll();
        return BaseResponse.builder()
                .code(200)
                .message(String.format(DELETE_ALL_SUCCESS, "DocuLibrary"))
                .build();
    }
}
