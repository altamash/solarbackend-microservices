package com.solaramps.api.tenant.service.crud;

import com.solaramps.api.exception.NotFoundException;
import com.solaramps.api.tenant.mapper.BaseResponse;
import com.solaramps.api.tenant.model.docu.DocuModule;
import com.solaramps.api.tenant.repository.DocuModuleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.solaramps.api.constants.Constants.Message.Service.CRUD.*;

@Service
public class DocuModuleService implements CrudService<DocuModule> {

    private final DocuModuleRepository repository;

    public DocuModuleService(DocuModuleRepository repository) {
        this.repository = repository;
    }

    @Override
    public DocuModule save(DocuModule obj) {
        return repository.save(obj);
    }

    @Override
    public DocuModule update(DocuModule obj) {
        return repository.save(obj);
    }

    @Override
    public List<DocuModule> getAll() {
        return repository.findAll();
    }

    @Override
    public DocuModule getById(Long id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException(DocuModule.class, id));
    }

    @Override
    public void deleteById(Long id) {
        try {
            repository.deleteById(id);
        } catch (Exception e) {
            throw new NotFoundException(DocuModule.class, id);
        }
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }

    @Override
    public BaseResponse saveResponse(DocuModule obj) {
        return BaseResponse.builder()
                .data(repository.save(obj))
                .message(String.format(SAVE_SUCCESS, "DocuModule"))
                .code(200)
                .build();
    }

    @Override
    public BaseResponse updateResponse(DocuModule obj) {
        return BaseResponse.builder()
                .data(repository.save(obj))
                .message(String.format(UPDATE_SUCCESS, "DocuModule", obj.getId()))
                .code(201)
                .build();
    }

    @Override
    public BaseResponse getAllResponse() {
        return BaseResponse.builder()
                .data(repository.findAll())
                .code(200)
                .build();
    }

    @Override
    public BaseResponse getByIdResponse(Long id) {
        return BaseResponse.builder()
                .data(repository.findById(id).orElseThrow(() -> new NotFoundException(DocuModule.class, id)))
                .code(200)
                .build();
    }

    @Override
    public BaseResponse deleteByIdResponse(Long id) {
        try {
            repository.deleteById(id);
        } catch (Exception e) {
            throw new NotFoundException(DocuModule.class, id);
        }
        return BaseResponse.builder()
                .message(String.format(String.format(DELETE_SUCCESS, "DocuModule", id)))
                .code(200)
                .build();
    }

    @Override
    public BaseResponse deleteAllResponse() {
        repository.deleteAll();
        return BaseResponse.builder()
                .message(String.format(DELETE_ALL_SUCCESS, "DocuModule"))
                .code(200)
                .build();
    }
}
