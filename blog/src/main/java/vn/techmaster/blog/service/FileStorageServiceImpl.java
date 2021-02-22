//package vn.techmaster.blog.service;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.io.Resource;
//import org.springframework.core.io.UrlResource;
//import org.springframework.stereotype.Service;
//import org.springframework.util.StringUtils;
//import org.springframework.web.multipart.MultipartFile;
//import vn.techmaster.blog.configuration.FileStorageProperties;
//import vn.techmaster.blog.exception.FileStorageException;
//import vn.techmaster.blog.exception.MyFileNotFoundException;
//import vn.techmaster.blog.utils.AppConstants;
//
//import javax.imageio.ImageIO;
//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.net.MalformedURLException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.nio.file.StandardCopyOption;
//
//@Service
//public class FileStorageServiceImpl implements FileStorageService{
//    private final Path fileStorageLocation;
//
//    //Khi app khoi dong tao folder ten upload ma lay tu trong appliproperties = su dung FileStorageProperties
//    @Autowired
//    public FileStorageServiceImpl(FileStorageProperties fileStorageProperties) {
//        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();
//        //dung java nio de store va retrieve file
//        try {
//            Files.createDirectories(this.fileStorageLocation);
//        }catch (Exception ex){
//            throw new FileStorageException(AppConstants.FILE_STORAGE_EXCEPTION_PATH_NOT_FOUND,ex);
//        }
//    }
//
//    @Override
//    public String storeFile(MultipartFile file) throws IOException {
//        if(!file.getOriginalFilename().endsWith(AppConstants.PNG_FILE_FORMAT) || file.getOriginalFilename().endsWith(AppConstants.JPEG_FILE_FORMAT)
//        || file.getOriginalFilename().endsWith(AppConstants.JPG_FILE_FORMAT))
//        throw new FileStorageException(AppConstants.INVALID_FILE_FORMAT);
//
//        File f = new File(AppConstants.TEMP_DIR + file.getOriginalFilename());
//
//        f.createNewFile();
//        FileOutputStream fout=new FileOutputStream(f);
//        fout.write(file.getBytes());
//        fout.close();
//        BufferedImage image= ImageIO.read(f);
//        int height= image.getHeight();
//        int width = image.getWidth();
//        if(width>300 || height >300){
//            if(f.exists()){
//                f.delete();
//                throw new FileStorageException(AppConstants.INVALID_FILE_DIMENSIONS);
//            }
//        }
//
//        if(f.exists())
//            f.delete();
//
//        //Valid filename
//        String filename= StringUtils.cleanPath(file.getOriginalFilename());
//        try {
//            if(filename.contains(AppConstants.INVALID_FILE_DELIMITER)){
//                throw new FileStorageException(AppConstants.INVALID_FILE_PATH_NAME+filename);
//            }
//            //Sau khi valid tao mot filename moi, su dung currentmilis vi ko dc de conflict nao khi upload file, tuc cung 1 file
//            //ko dc trong cung 1 upload folder
//            String newFilename=System.currentTimeMillis()+AppConstants.FILE_SEPERATOR+filename;
//            Path targetLocation = this.fileStorageLocation.resolve(newFilename);
//            Files.copy(file.getInputStream(),targetLocation, StandardCopyOption.REPLACE_EXISTING);
//            return newFilename;
//        }catch (IOException ex){
//            throw new FileStorageException(String.format(AppConstants.FILE_STORAGE_EXCEPTION,filename),ex);
//        }
//    }
//
//    //get file from upload
//    @Override
//    public Resource loadFileAsResource(String filename) {
//        try {
//            Path filePath = this.fileStorageLocation.resolve(filename).normalize();
//            Resource resource = new UrlResource(filePath.toUri());
//            if(resource.exists()){
//                return resource;
//            }else {
//                throw new MyFileNotFoundException(AppConstants.FILE_NOT_FOUND+filename);
//            }
//        }catch (MalformedURLException ex){
//            throw new MyFileNotFoundException(AppConstants.FILE_NOT_FOUND+filename,ex);
//        }
//    }
//}
