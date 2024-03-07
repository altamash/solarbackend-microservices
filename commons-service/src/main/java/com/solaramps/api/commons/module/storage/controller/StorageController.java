package com.solaramps.api.commons.module.storage.controller;

import com.microsoft.azure.storage.StorageException;
import com.solaramps.api.commons.module.storage.dto.BaseResponse;
import com.solaramps.api.commons.module.storage.service.StorageService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;

//@CrossOrigin
@RestController("StorageController")
@RequestMapping(value = "/storage")
public class StorageController {

    private final StorageService storageService;

    public StorageController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/pingStorage")
    public BaseResponse pingStorage(@RequestParam String blogService, @RequestParam String sasToken) {
        return storageService.pingStorage(blogService, sasToken);
    }

    @PostMapping(value = "/storeInContainer", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String storeInContainer(@RequestPart(value = "file") MultipartFile file,
                                   @RequestParam(value = "blobService") String blobService,
                                   @RequestParam(value = "sasToken") String sasToken,
                                   @RequestParam(value = "container") String container,
                                   @RequestParam(value = "directoryReference") String directoryReference,
                                   @RequestParam(value = "fileName") String fileName,
                                   @RequestParam(value = "relativeUrl", defaultValue = "false") Boolean relativeUrl)
            throws URISyntaxException, StorageException, IOException {
        return storageService.storeInContainer(blobService, sasToken, file, container, directoryReference, fileName, relativeUrl);
    }

    @GetMapping("/delete")
    void deleteBlob(@RequestParam(value = "blobService") String blobService,
                    @RequestParam(value = "sasToken") String sasToken,
                    @RequestParam(value = "container") String container,
                    @RequestParam(value = "path") String path) {
        storageService.deleteBlob(blobService, sasToken, container, path);
    }
}
