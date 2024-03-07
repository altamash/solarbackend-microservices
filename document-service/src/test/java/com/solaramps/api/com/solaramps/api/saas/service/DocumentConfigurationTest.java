package com.solaramps.api.com.solaramps.api.saas.service;

import com.microsoft.azure.storage.StorageException;
import com.solaramps.api.saas.model.tenant.MasterTenant;
import com.solaramps.api.saas.repository.MasterTenantRepository;
import com.solaramps.api.tenant.mapper.BaseResponse;
import com.solaramps.api.tenant.mapper.DocuLibraryDTO;
import com.solaramps.api.tenant.mapper.DocuMeasureDTO;
import com.solaramps.api.tenant.model.CodeTypeRefMap;
import com.solaramps.api.tenant.model.docu.DocuLibrary;
import com.solaramps.api.tenant.repository.CodeTypeRefMapRepository;
import com.solaramps.api.tenant.repository.DocuLibraryRepository;
import com.solaramps.api.tenant.service.DocumentManagement;
import com.solaramps.api.tenant.service.DocumentManagementImpl;
import com.solaramps.api.tenant.service.StorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class DocumentConfigurationTest {

    private static String FILE_NAME = "hello.txt";
    private static String FILE_CONTENT = "Hello, World!";
    private static String FILE_STORAGE_URL = "file_storage_url";
    private static String BLOB_SERVICE = "blobServiceBaseUrl";
    private static String CONTAINER = "dev";
    private static String MONGO_BASE_URL = "https://simongo.azurewebsites.net/productsapi";
    private static String MONGO_TENANT_NAME = "ec1001";
    private final DocuLibraryRepository docuLibraryRepository = Mockito.mock(DocuLibraryRepository.class);
    private final CodeTypeRefMapRepository codeTypeRefMapRepository = Mockito.mock(CodeTypeRefMapRepository.class);
    private final MasterTenantRepository masterTenantRepository = Mockito.mock(MasterTenantRepository.class);
    private final StorageService storageService = Mockito.mock(StorageService.class);
    private DocumentManagement documentManagement;

    @BeforeEach
    void setup() {
        this.documentManagement = new DocumentManagementImpl(null, BLOB_SERVICE, CONTAINER, MONGO_BASE_URL,
                docuLibraryRepository, codeTypeRefMapRepository, masterTenantRepository, storageService);
    }

    /* uploadDocument tests */
    @Test
    void uploadDocumentNegative_docuLibraryOptionalIsEmpty() {
        when(docuLibraryRepository.findById(1l)).thenReturn(Optional.empty());
        when(codeTypeRefMapRepository.findByRefCode(any())).thenReturn(new CodeTypeRefMap());
        BaseResponse baseResponse = documentManagement.uploadDocument(null, null, null, 1l, null, false);
        assertThat(baseResponse).isNotNull();
        assertThat(baseResponse.getCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY.value());
        verify(docuLibraryRepository).findById(1l);
        verifyNoMoreInteractions(docuLibraryRepository);
        verifyNoInteractions(storageService);
    }

    @Test
    void uploadDocumentNegative_allowedFormatsIsNonEmptyAndNotMatching() {
        MockMultipartFile file = new MockMultipartFile("file", FILE_NAME, MediaType.TEXT_PLAIN_VALUE, FILE_CONTENT.getBytes());
        when(docuLibraryRepository.findById(1l)).thenReturn(Optional.of(DocuLibrary.builder().build()));
        when(codeTypeRefMapRepository.findByRefCode(any())).thenReturn(new CodeTypeRefMap());
        BaseResponse baseResponse = documentManagement
                .uploadDocument(file, List.of(MediaType.APPLICATION_JSON_VALUE), null, 1l, null, false);
        assertThat(baseResponse).isNotNull();
        assertThat(baseResponse.getCode()).isEqualTo(HttpStatus.CONFLICT.value());
        verify(docuLibraryRepository).findById(1l);
        verifyNoMoreInteractions(docuLibraryRepository);
        verifyNoInteractions(storageService);
    }

    @Test
    void uploadDocumentNegative_RuntimeException() {
        MockMultipartFile file = new MockMultipartFile("file", FILE_NAME, MediaType.TEXT_PLAIN_VALUE, FILE_CONTENT.getBytes());
        RuntimeException exception = new RuntimeException();
        when(docuLibraryRepository.findById(null)).thenThrow(exception);
        when(codeTypeRefMapRepository.findByRefCode(any())).thenReturn(new CodeTypeRefMap());
        BaseResponse baseResponse = documentManagement.uploadDocument(file, List.of(MediaType.TEXT_PLAIN_VALUE),
                null, 1l, null, false);
        assertThat(baseResponse).isNotNull();
        assertThat(baseResponse.getCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY.value());
        verify(docuLibraryRepository).findById(1l);
        verifyNoMoreInteractions(docuLibraryRepository);
        verifyNoInteractions(storageService);
    }

    @Test
    void uploadDocumentPositive_docuLibraryIsNotNull() throws URISyntaxException, IOException, StorageException {
        DocuLibrary docuLibrary = DocuLibrary.builder().build();
        when(docuLibraryRepository.findById(1l)).thenReturn(Optional.of(docuLibrary));
        when(codeTypeRefMapRepository.findByRefCode(any())).thenReturn(new CodeTypeRefMap());
        MockMultipartFile file = new MockMultipartFile("file", FILE_NAME, MediaType.TEXT_PLAIN_VALUE, FILE_CONTENT.getBytes());
        when(storageService.storeInContainer(BLOB_SERVICE, null, file, CONTAINER, null, "11.txt", false))
                .thenReturn(FILE_STORAGE_URL);
        BaseResponse baseResponse = documentManagement
                .uploadDocument(file, List.of(MediaType.TEXT_PLAIN_VALUE), 1l, 1l, null, false);
        assertThat(baseResponse).isNotNull();
        assertThat(baseResponse.getCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(baseResponse.getData()).isEqualTo(FILE_STORAGE_URL);
        verify(docuLibraryRepository).findById(1l);
        verify(storageService, times(1)).storeInContainer(BLOB_SERVICE, null, file, CONTAINER, null, "11.txt", false);
        verifyNoMoreInteractions(storageService);
        verify(docuLibraryRepository).save(docuLibrary);
    }

    @Test
    void uploadDocumentPositive_DocuLibraryIsNull() {
        MockMultipartFile file = new MockMultipartFile("file", FILE_NAME, MediaType.TEXT_PLAIN_VALUE, FILE_CONTENT.getBytes());
        when(codeTypeRefMapRepository.findByRefCode(any())).thenReturn(new CodeTypeRefMap());
        BaseResponse baseResponse = documentManagement
                .uploadDocument(file, List.of(MediaType.TEXT_PLAIN_VALUE), 1l, null, null, false);
        assertThat(baseResponse).isNotNull();
        assertThat(baseResponse.getCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY.value());
        verifyNoInteractions(docuLibraryRepository);
        verifyNoInteractions(storageService);
    }

    /* addOrUpdateDocuLibrary tests */
    @Test
    void addOrUpdateDocuLibraryNegative_docuLibraryIdNull() {
        DocuLibrary docuLibrary = DocuLibrary.builder().docuId(1l).build();
        when(docuLibraryRepository.save(any())).thenReturn(docuLibrary);
        when(masterTenantRepository.findByCompanyKey(any())).thenReturn(MasterTenant.builder().dbName(MONGO_TENANT_NAME).build());
        BaseResponse baseResponse = documentManagement.addOrUpdateDocuLibrary(null, new DocuMeasureDTO(), null);
        assertThat(baseResponse).isNotNull();
        assertThat(baseResponse.getCode()).isEqualTo(HttpStatus.OK.value());
        verify(docuLibraryRepository, times(0)).findById(anyLong());
        verify(docuLibraryRepository, times(1)).save(any());
        verifyNoMoreInteractions(docuLibraryRepository);
    }

    @Test
    void addOrUpdateDocuLibraryNegative_docuLibraryOptionalIsEmpty() {
        when(docuLibraryRepository.findById(1l)).thenReturn(Optional.empty());
        BaseResponse baseResponse = documentManagement.addOrUpdateDocuLibrary(1l, null, null);
        assertThat(baseResponse).isNotNull();
        assertThat(baseResponse.getCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY.value());
        verify(docuLibraryRepository, times(1)).findById(anyLong());
        verify(docuLibraryRepository, times(0)).save(any());
        verifyNoMoreInteractions(docuLibraryRepository);
    }

    @Test
    void addOrUpdateDocuLibraryNegative_RuntimeException() {
        RuntimeException exception = new RuntimeException();
        when(docuLibraryRepository.save(any())).thenThrow(exception);
        BaseResponse baseResponse = documentManagement.addOrUpdateDocuLibrary(null, null, null);
        assertThat(baseResponse).isNotNull();
        assertThat(baseResponse.getCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY.value());
        verify(docuLibraryRepository, times(1)).save(any());
        verifyNoMoreInteractions(docuLibraryRepository);
    }

    @Test
    void addOrUpdateDocuLibraryPositive_DocuLibraryIsNotNull() {
        DocuLibrary docuLibrary = DocuLibrary.builder().docuId(1l).build();
        when(docuLibraryRepository.save(any())).thenReturn(docuLibrary);
        when(masterTenantRepository.findByCompanyKey(any())).thenReturn(MasterTenant.builder().dbName(MONGO_TENANT_NAME).build());
        BaseResponse baseResponse = documentManagement.addOrUpdateDocuLibrary(null, new DocuMeasureDTO(), null);
        assertThat(baseResponse).isNotNull();
        assertThat(baseResponse.getCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(((DocuLibraryDTO) baseResponse.getData()).getDocuId()).isEqualTo(docuLibrary.getDocuId());
        verify(docuLibraryRepository, times(0)).findById(anyLong());
        verify(docuLibraryRepository, times(1)).save(any());
        verifyNoMoreInteractions(docuLibraryRepository);
    }

    @Test
    void addOrUpdateDocuLibraryPositive_DocuLibraryIsNull() {
        DocuLibrary docuLibrary = DocuLibrary.builder().docuId(1l).build();
        when(docuLibraryRepository.findById(1l)).thenReturn(Optional.of(docuLibrary));
        when(docuLibraryRepository.save(any())).thenReturn(docuLibrary);
        when(masterTenantRepository.findByCompanyKey(any())).thenReturn(MasterTenant.builder().dbName(MONGO_TENANT_NAME).build());
        BaseResponse baseResponse = documentManagement.addOrUpdateDocuLibrary(1l, new DocuMeasureDTO(), null);
        assertThat(baseResponse).isNotNull();
        assertThat(baseResponse.getCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(((DocuLibraryDTO) baseResponse.getData()).getDocuId()).isEqualTo(docuLibrary.getDocuId());
        verify(docuLibraryRepository, times(1)).findById(anyLong());
        verify(docuLibraryRepository, times(1)).save(any());
        verifyNoMoreInteractions(docuLibraryRepository);
    }

    /* softDeleteDocumentMeasure tests */
    @Test
    void softDeleteDocumentMeasureNegative_docuLibraryOptionalIsEmpty() {
        when(docuLibraryRepository.findById(1l)).thenReturn(Optional.empty());
        BaseResponse baseResponse = documentManagement.softDeleteDocument(1l, null, null);
        assertThat(baseResponse).isNotNull();
        assertThat(baseResponse.getCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY.value());
        verify(docuLibraryRepository, times(1)).findById(anyLong());
        verify(docuLibraryRepository, times(0)).save(any());
        verifyNoMoreInteractions(docuLibraryRepository);
        verify(storageService, times(0)).deleteBlob(anyString(), anyString(), anyString(), anyString());
        verifyNoMoreInteractions(storageService);
    }

    @Test
    void softDeleteDocumentMeasurePositive_docuLibraryOptionalIsNotEmpty() {
        DocuLibrary docuLibrary = DocuLibrary.builder().docuId(1l).uri(BLOB_SERVICE + "/dev/filename.txt").build();
        when(docuLibraryRepository.findById(1l)).thenReturn(Optional.of(docuLibrary));
        BaseResponse baseResponse = documentManagement.softDeleteDocument(1l, null, null);
        assertThat(baseResponse).isNotNull();
        assertThat(((DocuLibrary) baseResponse.getData()).getUri()).isNull();
        assertThat(((DocuLibrary) baseResponse.getData()).getState()).isEqualTo("DRAFT");
        assertThat(baseResponse.getCode()).isEqualTo(HttpStatus.OK.value());
        verify(docuLibraryRepository, times(1)).findById(anyLong());
        verify(docuLibraryRepository, times(1)).save(any());
        verifyNoMoreInteractions(docuLibraryRepository);
        verify(storageService, times(1)).deleteBlob(BLOB_SERVICE, null, "dev", "filename.txt");
        verifyNoMoreInteractions(storageService);
    }
}
