package com.solaramps.api.commons;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

import static org.apache.commons.io.IOUtils.toByteArray;

@Component
public class Utility {

    private static final Logger LOGGER = LoggerFactory.getLogger(Utility.class);
    public static final String SYSTEM_DATE_FORMAT = "yyyy-MM-dd";

    public static String getFormattedMillis(long millis) {
        return String.format("%02d min, %02d sec",
                TimeUnit.MILLISECONDS.toMinutes(millis),
                TimeUnit.MILLISECONDS.toSeconds(millis) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
    }

    public static String convertFileToBase64(File file) throws IOException {
        byte[] fileData = toByteArray(new FileInputStream(file));
        return Base64.getEncoder().encodeToString(fileData);
    }

    public static File getFileFromUrl(String url, String prefix, String suffix) {
        if (url == null) {
            return null;
        }
        File tempFile = null;
        try {
            prefix = prefix == null ? RandomStringUtils.randomAlphanumeric(20) : prefix;
            suffix = suffix == null ? FilenameUtils.getExtension(url) : suffix;
            tempFile = File.createTempFile(prefix, suffix);
            Files.copy(new URL(url).openStream(), tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (final IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        tempFile.deleteOnExit();
        return tempFile;
    }

    public static File getFileFromUrl(String uri) {
        return getFileFromUrl(uri, null, null);
    }

    public static void main(String[] args) {
        getFileFromUrl("https://devstoragesi.blob.core.windows.net/devpublic/demo/demo_logo.png");
    }
}
