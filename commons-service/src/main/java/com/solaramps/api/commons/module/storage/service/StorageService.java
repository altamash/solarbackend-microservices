package com.solaramps.api.commons.module.storage.service;

import com.microsoft.azure.storage.StorageException;
import com.solaramps.api.commons.module.storage.dto.BaseResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URISyntaxException;

public interface StorageService {
    String getBlobUrl(String container, String directoryReference, String fileName);

    String storeInContainer(String blobService, String sasToken, MultipartFile file, String container, String directoryString, String fileName,
                            Boolean relativeUrl) throws URISyntaxException, StorageException, IOException;

    String uploadInputStream(String blobService, String sasToken, InputStream inputStream, Long length, String container, String directoryReference,
                             String fileName, Boolean relativeUrl) throws URISyntaxException,
            StorageException, UnsupportedEncodingException;

    Boolean downloadToOutputStream(OutputStream outputStream, String container, String relativeFileUrl);

    String uploadByteArray(byte[] bytes, String container, String directoryReference, String fileName)
            throws URISyntaxException, StorageException;

    byte[] downloadToByteArray(String container, String url, String name);

    File getBlob(String container, String url, String name);

    void deleteBlob(String blobService, String sasToken, String container, String path);

    BaseResponse pingStorage(String blobService, String sasToken);
}
