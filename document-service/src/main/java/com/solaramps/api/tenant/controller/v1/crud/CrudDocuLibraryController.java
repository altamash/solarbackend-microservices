package com.solaramps.api.tenant.controller.v1.crud;

import com.solaramps.api.tenant.mapper.BaseResponse;
import com.solaramps.api.tenant.mapper.DocuLibraryDTO;
import com.solaramps.api.tenant.model.docu.DocuLibrary;
import com.solaramps.api.tenant.service.crud.DocuLibraryService;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/docu/crud")
public class CrudDocuLibraryController implements CrudController<DocuLibrary> {

    private final DocuLibraryService docuLibraryService;

    public CrudDocuLibraryController(DocuLibraryService docuLibraryService) {
        this.docuLibraryService = docuLibraryService;
    }

    @PostMapping("/save")
    public BaseResponse<?> save(@RequestBody DocuLibraryDTO obj, @RequestParam(required = false) Long docVolMapId) {
        return docuLibraryService.saveResponse(obj, docVolMapId);
    }

    @PostMapping("/update")
    public BaseResponse<?> update(@RequestBody DocuLibraryDTO obj, @RequestParam(required = false) Long docVolMapId) {
        return docuLibraryService.updateResponse(obj, docVolMapId);
    }

    @Override
    public BaseResponse<?> save(DocuLibrary obj) {
        return docuLibraryService.saveResponse(obj);
    }

    @Override
    public BaseResponse<?> update(DocuLibrary obj) {
        return docuLibraryService.updateResponse(obj);
    }

    @Override
    public BaseResponse<?> getAll() {
        return docuLibraryService.getAllResponse();
    }

    @Override
    public BaseResponse<?> getById(Long id) {
        return docuLibraryService.getByIdResponse(id);
    }

    @Override
    public BaseResponse<?> delete(Long id) {
        return docuLibraryService.deleteByIdResponse(id);
    }

    @Hidden
    @Override
    public BaseResponse<?> deleteAll() {
        return docuLibraryService.deleteAllResponse();
    }
}
