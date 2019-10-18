package com.mogikanen9.rest.api.fileapi;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileService {

    private Path tmpFolderPath;

    public FileService(@Value("${file.tmp-dir}") String tmpDirValue) {
        this.tmpFolderPath = Paths.get(tmpDirValue);
    }

    public Resource loadFileAsResource(String fileName) throws FileNotFoundException {
        try {
            Path filePath = this.tmpFolderPath.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new FileNotFoundException(String.format("File not found -> %s", fileName));
            }
        } catch (MalformedURLException ex) {
            throw new FileNotFoundException(
                    String.format("File not found -> %s, errorMsg->%s", fileName, ex.getMessage()));
        }
    }

    public Path storeFile(MultipartFile file) throws IOException {
        Path storedFilePath = tmpFolderPath.resolve(file.getOriginalFilename());
        Files.copy(file.getInputStream(), storedFilePath, StandardCopyOption.REPLACE_EXISTING);
        return storedFilePath;
    }
}