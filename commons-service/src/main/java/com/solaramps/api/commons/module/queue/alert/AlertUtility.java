package com.solaramps.api.commons.module.queue.alert;

import com.microsoft.azure.storage.StorageException;
import com.solaramps.api.commons.Utility;
import com.solaramps.api.commons.module.notification.email.suprSend.service.SuprSendServiceImpl;
import com.solaramps.api.commons.module.storage.service.StorageService;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Component
public class AlertUtility {

    private final Logger LOGGER = LoggerFactory.getLogger(SuprSendServiceImpl.class);

    private final StorageService storageService;
    private final String sasToken;
    private final String blobService;
    private final String container;
    private SimpleDateFormat dateFormat;

    public AlertUtility(StorageService storageService,
                        @Value("${app.storage.azureBlobSasToken}") String sasToken,
                        @Value("${app.storage.blobService}") String blobService,
                        @Value("${app.storage.container}") String container) {
        this.storageService = storageService;
        this.sasToken = sasToken;
        this.blobService = blobService;
        this.container = container;
        dateFormat = new SimpleDateFormat(Utility.SYSTEM_DATE_FORMAT);
    }

    public static String updateFileName(MultipartFile file, String fileName) {
        if (file != null) {
            if (fileName == null) {
                fileName = UUID.randomUUID() + "." + FilenameUtils.getExtension(file.getOriginalFilename());
            } else {
                fileName = fileName + "." + FilenameUtils.getExtension(file.getOriginalFilename());
            }
        }
        return fileName;
    }

    public String getFilePath(MultipartFile file, String fileName, String attachmentDirRef) {
        String filePath = null;
        if (file != null) {
            try {
                filePath = storageService.storeInContainer(blobService, sasToken, file, container,
                        attachmentDirRef + "/" + dateFormat.format(new Date()) , fileName, false);
            } catch (IOException | URISyntaxException | StorageException ex) {
                LOGGER.error(ex.getMessage(), ex);
                throw new RuntimeException(ex);
            }
        }
        return filePath;
    }
}
