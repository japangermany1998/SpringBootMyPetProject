package vn.techmaster.topcar.service;

import java.io.File;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.techmaster.topcar.exception.StorageException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class StorageService {
    @Value("${upload.path}")
    String path;

    public String uploadFile(MultipartFile file){
        if(file.isEmpty()){
            throw new StorageException("Fail to store empty file!");
        }

        String filename=file.getOriginalFilename();
        try {
            InputStream inputStream=file.getInputStream();
            Files.copy(inputStream, Paths.get(path+filename), StandardCopyOption.REPLACE_EXISTING);
        }catch (IOException e){
            String msg=String.format("Failed to upload file %s",filename);
            throw new StorageException(msg,e);
        }
        return filename;
    }
}
