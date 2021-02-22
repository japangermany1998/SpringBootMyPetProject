package vn.techmaster.blog.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

//store file va download file
public interface FileStorageService {
    public String storeFile(MultipartFile file) throws IOException;

    public Resource loadFileAsResource(String filename);
}
