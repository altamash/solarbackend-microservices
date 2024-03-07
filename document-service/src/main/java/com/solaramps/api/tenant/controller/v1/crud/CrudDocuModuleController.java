package com.solaramps.api.tenant.controller.v1.crud;

import com.solaramps.api.tenant.mapper.BaseResponse;
import com.solaramps.api.tenant.model.docu.DocuModule;
import com.solaramps.api.tenant.service.crud.DocuModuleService;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/docuModule/crud")
public class CrudDocuModuleController implements CrudController<DocuModule> {

    private final DocuModuleService docuModuleService;

    public CrudDocuModuleController(DocuModuleService docuModuleService) {
        this.docuModuleService = docuModuleService;
    }

    @Override
    public BaseResponse<?> save(DocuModule obj) {
        return docuModuleService.saveResponse(obj);
    }

    @Override
    public BaseResponse<?> update(DocuModule obj) {
        return docuModuleService.updateResponse(obj);
    }

    @Override
    public BaseResponse<?> getAll() {
        return docuModuleService.getAllResponse();
    }

    @Override
    public BaseResponse<?> getById(Long id) {
        return docuModuleService.getByIdResponse(id);
    }

    @Override
    public BaseResponse<?> delete(Long id) {
        return docuModuleService.deleteByIdResponse(id);
    }

    @Hidden
    @Override
    public BaseResponse<?> deleteAll() {
        return docuModuleService.deleteAllResponse();
    }
}
