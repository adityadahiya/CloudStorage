package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileUploadMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
@Transactional
public class FileUploadService {

    private final FileUploadMapper fileMapper;

    public FileUploadService(FileUploadMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public int addFile(MultipartFile file, User user) throws IOException {
        byte[] fileContent = new byte[(int) file.getSize()];
        InputStream inputStream = null;
        try {
            // create an input stream pointing to the file
            inputStream = file.getInputStream();
            // read the contents of file into byte array
            inputStream.read(fileContent);
            return fileMapper.insert(new File(null, file.getOriginalFilename(), file.getContentType(), String.valueOf(file.getSize()), user.getUserId(), fileContent));
        } catch (IOException e) {
            throw new IOException("Unable to convert file to byte array. " +
                    e.getMessage());
        } finally {
            // close input stream
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    public File getFileById(Integer id) {
        return fileMapper.getFile(id);
    }

    public boolean isFileNameAvailable(String fileName) {
        return fileMapper.getFileByName(fileName) == null;
    }

    public List<File> getAllFiles(User user) {
        return fileMapper.getAllFiles(user);
    }

    public int deleteFileById(Integer id) {
        return fileMapper.deleteFile(id);
    }
}
