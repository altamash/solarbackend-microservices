package com.solaramps.api.tenant.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.solaramps.api.saas.model.tenant.MasterTenant;
import com.solaramps.api.saas.repository.MasterTenantRepository;
import com.solaramps.api.tenant.mapper.*;
import com.solaramps.api.tenant.model.CodeTypeRefMap;
import com.solaramps.api.tenant.model.docu.DocuLibrary;
import com.solaramps.api.tenant.model.docu.DocuVolMap;
import com.solaramps.api.tenant.repository.CodeTypeRefMapRepository;
import com.solaramps.api.tenant.repository.DocuLibraryRepository;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.solaramps.api.constants.Constants.Message.Service.CRUD.notFound;

@Service
public class DocumentManagementImpl implements DocumentManagement {

    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentManagementImpl.class);
//    private static final String MONGO_BASE_URL = "http://localhost:8089/productsapi";
    private final String sasToken;
    private final String blobService;
    private final String container;
    private final String mongoBaseUrl;
    private final DocuLibraryRepository docuLibraryRepository;
    private final CodeTypeRefMapRepository codeTypeRefMapRepository;
    private final MasterTenantRepository masterTenantRepository;
    private final StorageService storageService;

    public DocumentManagementImpl(@Value("${app.storage.azureBlobSasToken}") String sasToken,
                                  @Value("${app.storage.blobService}") String blobService,
                                  @Value("${app.storage.container}") String container,
                                  @Value("${app.mongoBaseUrl}") String mongoBaseUrl,
                                  DocuLibraryRepository docuLibraryRepository,
                                  CodeTypeRefMapRepository codeTypeRefMapRepository,
                                  MasterTenantRepository masterTenantRepository,
                                  StorageService storageService) {
        this.sasToken = sasToken;
        this.blobService = blobService;
        this.container = container;
        this.mongoBaseUrl = mongoBaseUrl;
        this.docuLibraryRepository = docuLibraryRepository;
        this.codeTypeRefMapRepository = codeTypeRefMapRepository;
        this.masterTenantRepository = masterTenantRepository;
        this.storageService = storageService;
    }

    @Override
    public BaseResponse uploadDocument(MultipartFile file, List<String> allowedFormats, Long compKey,
                                       Long docuLibraryId, String storagePathCode,
                                       boolean relativeUrl) {
        CodeTypeRefMap codeTypeRefMap = codeTypeRefMapRepository.findByRefCode(storagePathCode);
        if (codeTypeRefMap == null) {
            return BaseResponse.builder()
                    .message("storagePathCode (storage file path) not found with code: " + storagePathCode)
                    .code(HttpStatus.NOT_FOUND.value())
                    .build();
        }
        DocuLibrary docuLibrary = null;
        if (docuLibraryId != null) {
            Optional<DocuLibrary> docuLibraryOptional = docuLibraryRepository.findById(docuLibraryId);
            if (docuLibraryOptional.isPresent()) {
                docuLibrary = docuLibraryOptional.get();
            } else {
                return BaseResponse.builder()
                        .message("Invalid docuLibraryId: " + docuLibraryId)
                        .code(HttpStatus.UNPROCESSABLE_ENTITY.value())
                        .build();
            }
        } else {
            return BaseResponse.builder()
                    .message("docuLibraryId is required")
                    .code(HttpStatus.UNPROCESSABLE_ENTITY.value())
                    .build();
        }
        try {
            if (allowedFormats == null || allowedFormats.contains("*") || allowedFormats.contains(file.getContentType())) {
//                String blobService, String sasToken, MultipartFile file, String container, String directoryString, String fileName,
//                        Boolean relativeUrl
                String uri = storageService.storeInContainer(
                        blobService,
                        sasToken,
                        file,
                        container,
                        codeTypeRefMap.getRefTable(),
                        compKey + String.valueOf(docuLibraryId) + "." + FilenameUtils.getExtension(file.getOriginalFilename()), // tenant_id + docu_library_id.ext
                        relativeUrl);
                if (docuLibrary != null) {
                    docuLibrary.setUri(uri);
                    docuLibrary.setState("PUBLISHED");
                    docuLibraryRepository.save(docuLibrary);
                }
                return BaseResponse.builder()
                        .message("SUCCESS")
                        .data(uri)
                        .code(HttpStatus.OK.value())
                        .build();
            } else {
                return BaseResponse.builder()
                        .message("Invalid format: " + file.getContentType())
                        .code(HttpStatus.CONFLICT.value())
                        .build();
            }
        } catch (Exception e) {
            return BaseResponse.builder()
                    .message(e.getMessage())
                    .code(HttpStatus.UNPROCESSABLE_ENTITY.value())
                    .build();
        }
    }

    @Override
    public BaseResponse addOrUpdateDocuLibrary(Long docuLibraryId, DocuMeasureDTO docuMeasureDTO, Long compKey) {
        /*{
            "docuLibraryId": null
            "type": "ADV",
            "measure": "Service Level Agreement",
            "notes": null,
            "format": "DOCUMENT",
            "uri": null
            "locked": true,
            "docu_alias" : "alias_1"
        }*/
        /*if (measureJSON.getId() == null) { // TODO: add test case
            return BaseResponse.builder()
                    .message("Document measure id is required")
                    .code(HttpStatus.BAD_REQUEST.value())
                    .build();
        }*/
        try {
            DocuLibrary docuLibrary;
            if (docuLibraryId != null) {
                Optional<DocuLibrary> docuLibraryOptional = docuLibraryRepository.findById(docuLibraryId);
                if (docuLibraryOptional.isPresent()) {
                    docuLibrary = docuLibraryOptional.get();
                    docuLibrary = DocuLibraryMapper.toUpdatedDocuLibrary(docuLibrary, docuMeasureDTO);
                } else {
                    return BaseResponse.builder()
                            .message("Invalid docuLibraryId: " + docuLibraryId)
                            .code(HttpStatus.UNPROCESSABLE_ENTITY.value())
                            .build();
                }
            } else {
                docuLibrary = DocuLibraryMapper.toDocuLibrary(docuMeasureDTO);
            }
            docuLibrary = docuLibraryRepository.save(docuLibrary);
            docuMeasureDTO.setDocuLibraryId(docuLibrary.getDocuId());
            HttpResponse<String> response = updateDocumentMeasure(docuMeasureDTO, compKey);
            return BaseResponse.builder()
                    .message(new ObjectMapper().readValue(response.body(), MongoResponse.class).getMessage())
                    .data(DocuLibraryMapper.toDocuLibraryDTO(docuLibrary))
                    .code(HttpStatus.OK.value())
                    .build();
        } catch (Exception e) {
            return BaseResponse.builder()
                    .message(e.getMessage())
                    .code(HttpStatus.UNPROCESSABLE_ENTITY.value())
                    .build();
        }
    }

    private HttpResponse<String> updateDocumentMeasure(DocuMeasureDTO docuMeasureDTO, Long compKey) {
        // Project id: 632dfbf8aba17f6406285a5c
        // Section id: 632dfbf8aba17f6406285a5d
        // Measure id: 64490a871a76b2497bbb6bd6
        ObjectMapper mapper = new ObjectMapper();
        try {
            String measure = mapper.writeValueAsString(docuMeasureDTO);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(mongoBaseUrl + "/measures/updateTenantMeasure"))
                    .header("Tenant-id", getMongoTenantName(compKey))
                    .PUT(HttpRequest.BodyPublishers.ofString(measure))
                    .build();
            return HttpClient.newBuilder()
                    .build()
                    .send(request, HttpResponse.BodyHandlers.ofString());
        } catch (JsonProcessingException | URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    private String getMongoTenantName(Long compKey) {
        MasterTenant masterTenant = masterTenantRepository.findByCompanyKey(compKey);
        return masterTenant.getDbName();
    }

    @Override
    public BaseResponse softDeleteDocument(Long docuLibraryId, String measureIdHash, Long compKey) {
        try {
            DocuLibrary docuLibrary = null;
            if (docuLibraryId != null) {
                Optional<DocuLibrary> docuLibraryOptional = docuLibraryRepository.findById(docuLibraryId);
                if (docuLibraryOptional.isPresent()) {
                    docuLibrary = docuLibraryOptional.get();
                } else {
                    return BaseResponse.builder().message("Invalid docuLibraryId: " + docuLibraryId)
                            .code(HttpStatus.UNPROCESSABLE_ENTITY.value()).build();
                }
            }
            HttpResponse<String> response = softDelete(docuLibrary, measureIdHash, compKey);
            return BaseResponse.builder()
                    .message("Soft deleted DocuLibrary with id " + docuLibraryId + " (set uri to null, state to 'DRAFT' and deleted storage file)" +
                            (response != null ? "; " + new ObjectMapper().readValue(response.body(), MongoResponse.class).getMessage() : ""))
                    .code(HttpStatus.OK.value()).data(docuLibrary).build();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return BaseResponse.builder().message(e.getMessage()).code(HttpStatus.UNPROCESSABLE_ENTITY.value()).build();
        }
    }

    private HttpResponse<String> softDelete(DocuLibrary docuLibrary, String measureIdHash, Long compKey) {
        String uri = docuLibrary.getUri();
        docuLibrary.setUri(null);
        docuLibrary.setState("DRAFT");
        docuLibrary.setStatus("DELETED");
        docuLibraryRepository.save(docuLibrary);
        if (uri != null) {
            String relativeUrl = uri.substring(blobService.length() + 1);
            int index = relativeUrl.indexOf("/");
            String container = relativeUrl.substring(0, index);
            String path = relativeUrl.substring(index + 1);
            storageService.deleteBlob(blobService, sasToken, container, path);
        }
        HttpResponse<String> response = null;
        if (measureIdHash != null) {
            String[] ids = measureIdHash.split(",");
            if (ids.length == 3) {
                DocuMeasureSoftDeleteDTO docuMeasureSoftDeleteDTO = DocuMeasureSoftDeleteDTO.builder()
                        .projectId(ids[0]).sectionId(ids[1]).measureId(ids[2]).build();
                response = softDeleteDocumentMeasure(docuMeasureSoftDeleteDTO, compKey);
            }
        }
        return response;
    }

    @Override
    public BaseResponse softDeleteDocuments(List<DocuMeasureIdDTO> docuMeasureIdDTOs, Long compKey) {
        List<String> response = new ArrayList<>();
        try {
            docuMeasureIdDTOs.forEach(docuMeasureIdDTO -> {
                if (docuMeasureIdDTO.getDocuLibraryId() != null) {
                    Optional<DocuLibrary> docuLibraryOptional = docuLibraryRepository.findById(docuMeasureIdDTO.getDocuLibraryId());
                    if (docuLibraryOptional.isPresent()) {
                        response.add(softDelete(docuLibraryOptional.get(), docuMeasureIdDTO.getMeasureId(), compKey).body());
                    }
                }
            });
        } catch (Exception e) {
            return BaseResponse.builder()
                    .message(e.getMessage())
                    .code(HttpStatus.UNPROCESSABLE_ENTITY.value())
                    .build();
        }
        return BaseResponse.builder()
                .message("SUCCESS")
                .code(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .data(response)
                .build();
    }

    @Override
    public BaseResponse downloadDocument(Long docuLibraryId) {
        Optional<DocuLibrary> docuLibraryOptional = docuLibraryRepository.findById(docuLibraryId);
        if (!docuLibraryOptional.isPresent()) {
            return BaseResponse.builder()
                    .code(404)
                    .message(notFound(DocuVolMap.class.getSimpleName(), docuLibraryId))
                    .build();
        }
        return BaseResponse.builder()
                .code(200)
                .message("SUCCESS")
                .data(docuLibraryOptional.get().getUri())
                .build();
    }

    @Override
    public BaseResponse downloadDocuments(List<Long> docuLibraryIds) {
        List<DocuLibrary> docuLibraries = docuLibraryRepository.findAllByDocuIdIn(docuLibraryIds);
        return BaseResponse.builder()
                .code(200)
                .message("SUCCESS")
                .data(docuLibraries.stream().map(lib -> lib.getUri()).collect(Collectors.toList()))
                .build();
    }

    private HttpResponse<String> softDeleteDocumentMeasure(DocuMeasureSoftDeleteDTO docuMeasureSoftDeleteDTO, Long compKey) {

        ObjectMapper mapper = new ObjectMapper();
        try {

            String measure = mapper.writeValueAsString(docuMeasureSoftDeleteDTO);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(mongoBaseUrl + "/measures/softDeleteDocumentMeasure"))
                    .header("Tenant-id", getMongoTenantName(compKey))
                    .PUT(HttpRequest.BodyPublishers.ofString(measure))
                    .build();
            return HttpClient.newBuilder()
                    .build()
                    .send(request, HttpResponse.BodyHandlers.ofString());
        } catch (JsonProcessingException | URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    /*public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {
        String uri = "https://devstoragesi.blob.core.windows.net/dev/tenant%2F1001%2Fproject%2Fdocumentation%2Fdownload.jpeg";
        String blobService = "https://devstoragesi.blob.core.windows.net";
        String relativeUrl = uri.substring(blobService.length() + 1);
        int index = relativeUrl.indexOf("/");
        String container = relativeUrl.substring(0, index);
        String path = relativeUrl.substring(index + 1);
        System.out.println(container);
        System.out.println(path);

//        HttpRequest.newBuilder(new URI("https://postman-echo.com/get"));

        String documentMeasure = "{\n" +
                "  \"_id\": {\n" +
                "    \"$oid\": \"64490b361a76b2497bbb6bd9\"\n" +
                "  },\n" +
                "  \"actions\": null,\n" +
                "  \"code\": \"SLA\",\n" +
                "  \"comp_events\": null,\n" +
                "  \"created_at\": \"2022-07-18 00:00:00\",\n" +
                "  \"format\": \"DOCUMENT\",\n" +
                "  \"locked\": true,\n" +
                "  \"mandatory\": true,\n" +
                "  \"measure\": \"Service Level Agreement\",\n" +
                "  \"notes\": null,\n" +
                "  \"pct\": false,\n" +
                "  \"reg_module\": null,\n" +
                "  \"reg_module_id\": null,\n" +
                "  \"related_measure\": null,\n" +
                "  \"system_used\": false,\n" +
                "  \"type\": \"ADV\",\n" +
                "  \"updated_at\": \"2022-07-18 00:00:00\",\n" +
                "  \"validation_params\": null,\n" +
                "  \"validation_rule\": null,\n" +
                "  \"visibility_level\": \"*\",\n" +
                "  \"visible\": true,\n" +
                "  \"level\": -1,\n" +
                "  \"seq\": 0,\n" +
                "  \"default_value\": null,\n" +
                "  \"self_register\": true,\n" +
                "  \"flag\": null,\n" +
                "  \"allowed_formats\": [\n" +
                "    \"application/pdf\",\n" +
                "    \"application/docx\"\n" +
                "  ],\n" +
                "  \"hint\": \"\",\n" +
                "  \"docu_format\": \"\",\n" +
                "  \"docu_alias\": \"\",\n" +
                "  \"docu_library_id\": null\n" +
                "}";
        ObjectMapper mapper = new ObjectMapper();
        DocuMeasureDTO docuMeasureDTO = mapper.readValue(documentMeasure, DocuMeasureDTO.class);
        String measure = mapper.writeValueAsString(docuMeasureDTO);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("https://simongo.azurewebsites.net/productsapi/measures/updateTenantMeasures"))
                .header("Tenant-id", "ec1001")
                .PUT(HttpRequest.BodyPublishers.ofString(measure))
                .build();
        HttpResponse<String> response = HttpClient.newBuilder()
                .build()
                .send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response);
    }*/
}
