package com.solaramps.api.tenant.controller.v1.crud;

import com.solaramps.api.tenant.mapper.BaseResponse;
import com.solaramps.api.tenant.model.docu.DocuType;
import com.solaramps.api.tenant.service.crud.DocuTypeService;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/docuType/crud")
public class CrudDocuTypeController implements CrudController<DocuType> {

    private final DocuTypeService docuTypeService;

    public CrudDocuTypeController(DocuTypeService docuTypeService) {
        this.docuTypeService = docuTypeService;
    }

    @Override
    public BaseResponse<?> save(DocuType obj) {
        return docuTypeService.saveResponse(obj);
    }

    @Override
    public BaseResponse<?> update(DocuType obj) {
        return docuTypeService.updateResponse(obj);
    }

    @Override
    public BaseResponse<?> getAll() {
        return docuTypeService.getAllResponse();
    }

    @Override
    public BaseResponse<?> getById(Long id) {
        return docuTypeService.getByIdResponse(id);
    }

    @Override
    public BaseResponse<?> delete(Long id) {
        return docuTypeService.deleteByIdResponse(id);
    }

    @Hidden
    @Override
    public BaseResponse<?> deleteAll() {
        return docuTypeService.deleteAllResponse();
    }

    @GetMapping("/search")
    public BaseResponse search(@RequestParam int pageNumber,
                               @RequestParam(required = false) Integer pageSize,
                               @RequestParam(required = false, defaultValue = "-1") String sort) {
        return docuTypeService.getAllDocuTypes(pageNumber, pageSize, sort);
    }
}
