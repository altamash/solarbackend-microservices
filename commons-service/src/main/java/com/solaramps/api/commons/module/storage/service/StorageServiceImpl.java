package com.solaramps.api.commons.module.storage.service;

import com.azure.core.util.BinaryData;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.models.StorageAccountInfo;
import com.microsoft.azure.storage.StorageCredentials;
import com.microsoft.azure.storage.StorageCredentialsSharedAccessSignature;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlobDirectory;
import com.solaramps.api.commons.module.storage.dto.AccountInfo;
import com.solaramps.api.commons.module.storage.dto.BaseResponse;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;

@Service
public class StorageServiceImpl implements StorageService {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Value("${app.storage.azureBlobSasToken}")
    private String sasToken;
    @Value("${app.storage.blobService}")
    private String blobService;
    @Value("${app.storage.container}")
    private String storageContainer;

    //Azure BillingCredits File
    public static final String PREFIX = "tempFile";
    public static final String SUFFIX = ".csv";

    /**
     * @param container
     * @param directoryReference
     * @param fileName
     * @return
     */
    @Override
    public String getBlobUrl(String container, String directoryReference, String fileName) {
        BlobContainerClient containerClient = getBlobContainer(container);
        String blobUrl = null;
        try {
            BlobClient blobClient =
                    containerClient.getBlobClient(directoryReference + "/" + fileName);
            blobUrl = blobClient.getBlobUrl();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return blobUrl;
    }

    @Override
    public String storeInContainer(String blobService, String sasToken, MultipartFile file, String container, String directoryReference, String fileName,
                                   Boolean relativeUrl)
            throws URISyntaxException, StorageException, IOException {
        try (ByteArrayInputStream dataStream = new ByteArrayInputStream(file.getBytes())) {
            return uploadInputStream(blobService, sasToken, dataStream, file.getSize(), container, directoryReference, fileName,
                    relativeUrl);
        }
    }

    // Generic function to uload file in a container and directory
    @Override
    public String uploadInputStream(String blobService, String sasToken, InputStream inputStream, Long length, String container, String directoryReference,
                                    String fileName, Boolean relativeUrl)
            throws URISyntaxException, StorageException, UnsupportedEncodingException {
        BlobContainerClient containerClient = getBlobContainer(blobService, sasToken, container);
        if (directoryReference != null && !directoryReference.isEmpty()) {
            createDirectory(directoryReference);
        }
        /* Upload the file to the container */
        String blobUrl = null;
        if (directoryReference.startsWith("/")) {
            directoryReference = directoryReference.substring(1);
        }
        if (directoryReference.endsWith("/")) {
            directoryReference = directoryReference.substring(0, directoryReference.length() - 1);
        }
        try {
            BlobClient blobClient =
                    containerClient.getBlobClient(directoryReference + "/" + fileName);
            blobClient.upload(inputStream, length, true);
            blobUrl = blobClient.getBlobUrl();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        if (relativeUrl) {
            /*blobUrl = URLEncoder.encode(URLDecoder.decode(blobUrl, StandardCharsets.UTF_8.name())
                    .substring(blobService.length() + container.length() +
                            String.valueOf(compKey).length() + 3), StandardCharsets.UTF_8.name());
            blobUrl = blobUrl.replaceAll("\\+", " ");*/
            blobUrl = blobUrl.substring(blobService.length());
        }
        return blobUrl;
    }

    @Override
    public Boolean downloadToOutputStream(OutputStream outputStream, String container, String relativeFileUrl) {
        BlobContainerClient containerClient = getBlobContainer(container);

        /* Upload the file to the container */
        try {
            BlobClient blobClient =
                    !containerClient.getBlobClient(relativeFileUrl).exists() ? null :
                            containerClient.getBlobClient(relativeFileUrl);
            if (blobClient == null) {
                return false;
            }
            blobClient.download(outputStream);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
//            throw e;
        }
        return true;
    }

    @Override
    public String uploadByteArray(byte[] bytes, String container, String directoryReference, String fileName)
            throws URISyntaxException, StorageException {
        BlobContainerClient containerClient = getBlobContainer(container);
        if (directoryReference != null && !directoryReference.isEmpty()) {
            createDirectory(directoryReference);
        }
        String blobUrl = null;
        try {
            BlobClient blobClient =
                    containerClient.getBlobClient(directoryReference + "/" + fileName);
            blobClient.upload(BinaryData.fromBytes(bytes), true);
            blobUrl = blobClient.getBlobUrl();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return blobUrl;
    }

    @Override
    public byte[] downloadToByteArray(String container, String url, String name) {
        byte[] bytes = new byte[0];
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            downloadToOutputStream(os, container, url + "/" + name);
            try (InputStream is = new ByteArrayInputStream(os.toByteArray())) {
                bytes = IOUtils.toByteArray(is);
            }
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return bytes;
    }

    private BlobContainerClient getBlobContainer(String container) {
        /* Create a new BlobServiceClient with a SAS Token */
        BlobServiceClient blobServiceClient = getBlobServiceClient();
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(container);
        if (!containerClient.exists()) {
            containerClient = blobServiceClient.createBlobContainer(container);
        }
        return containerClient;
    }

    private BlobContainerClient getBlobContainer(String blobService, String sasToken, String container) {
        /* Create a new BlobServiceClient with a SAS Token */
        BlobServiceClient blobServiceClient = getBlobServiceClient(blobService, sasToken);
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(container);
        if (!containerClient.exists()) {
            containerClient = blobServiceClient.createBlobContainer(container);
        }
        return containerClient;
    }

    private CloudBlobDirectory createDirectory(String directoryReference) throws URISyntaxException, StorageException {
        StorageCredentials creds = new StorageCredentialsSharedAccessSignature(sasToken);
        CloudBlobClient cloudBlobClient = new CloudBlobClient(new URI(blobService), creds);
        CloudBlobContainer blobContainer = cloudBlobClient.getContainerReference(storageContainer);
        if (blobContainer.exists()) {
            return blobContainer.getDirectoryReference(directoryReference);
        }
        return null;
    }

    private CloudBlobDirectory createDirectory(String container, String directoryReference) throws URISyntaxException
            , StorageException {
        StorageCredentials creds = new StorageCredentialsSharedAccessSignature(sasToken);
        CloudBlobClient cloudBlobClient = new CloudBlobClient(new URI(blobService), creds);
        CloudBlobContainer blobContainer = cloudBlobClient.getContainerReference(container);
        if (blobContainer.exists()) {
            return blobContainer.getDirectoryReference(directoryReference);
        }
        return null;
    }

    @Override
    public File getBlob(String container, String url, String name) {

        File tempFile = null;
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            Boolean blobClient = downloadToOutputStream(os, container, url + "/" + name);
            if (!blobClient) {
                return null;
            }
            try (InputStream is = new ByteArrayInputStream(os.toByteArray())) {
                tempFile = File.createTempFile(PREFIX, SUFFIX);
                tempFile.deleteOnExit();
                try (FileOutputStream out = new FileOutputStream(tempFile)) {
                    FileUtils.copyInputStreamToFile(is, tempFile);
                }
            }
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return tempFile;
    }

    @Override
    public void deleteBlob(String blobService, String sasToken, String container, String path) {
        BlobServiceClient blobServiceClient = getBlobServiceClient(blobService, sasToken);
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(container);
        BlobClient blobClient =
                containerClient.getBlobClient(path);
        blobClient.delete();
    }

    private BlobServiceClient getBlobServiceClient() {
        return new BlobServiceClientBuilder()
                .endpoint(blobService)
                .sasToken(sasToken)
                .buildClient();
    }

    private BlobServiceClient getBlobServiceClient(String blobService, String sasToken) {
        return new BlobServiceClientBuilder()
                .endpoint(blobService)
                .sasToken(sasToken)
                .buildClient();
    }

    @Override
    public BaseResponse pingStorage(String blobService, String sasToken) {
        try {
//            StorageCredentials creds = new StorageCredentialsSharedAccessSignature(sasToken);
//            CloudBlobClient cloudBlobClient = new CloudBlobClient(new URI(blobService), creds);
//            CloudBlobContainer blobContainer = cloudBlobClient.getContainerReference(container);
            StorageAccountInfo information = new BlobServiceClientBuilder()
                    .endpoint(blobService)
                    .sasToken(sasToken).buildClient().getAccountInfo();
            return BaseResponse.builder()
                    .data(AccountInfo.builder()
                            .skuName(information.getSkuName().toString())
                            .accountKind(information.getAccountKind().toString())
                            .build())
                    .code(HttpStatus.OK.value())
                    .build();
        } catch (Exception e) {
            return BaseResponse.builder()
                    .code(HttpStatus.UNPROCESSABLE_ENTITY.value())
                    .message("Invalid account")
                    .build();
        }
    }
}

