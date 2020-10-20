package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.FileUploadService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/file")
public class FileUploadController {

    @Autowired
    private FileUploadService fileUploadService;
    @Autowired
    private UserService userService;

    @GetMapping("/delete/{id}")
    public String deleteFileById(@PathVariable Integer id) {
        String fileUploadError = null;
        int rowDeleted = this.fileUploadService.deleteFileById(id);
        if (rowDeleted < 0) {
            fileUploadError = "There was an error deleting the file. Please try again.";
        }
        if (fileUploadError == null) {
            return "redirect:/home?msg=File deleted successfully";
        } else {
            return "redirect:/home?msg=" + fileUploadError;
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getFile(@PathVariable Integer id) {
        File fileDB = this.fileUploadService.getFileById(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDB.getFileName() + "\"")
                .body(fileDB.getFileData());
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("fileUpload") MultipartFile file, Authentication authentication, Model model) throws IOException {
        String fileUploadError = null;
        String userName = (String) authentication.getPrincipal();
        User user = this.userService.getUser(userName);

        if (file.isEmpty()) {
            fileUploadError = "File is empty";
        }
        if (file.getSize() / (1024 * 1024) > 10) {
            fileUploadError = "File size is greater than 10MB";
        }

        if (!fileUploadService.isFileNameAvailable(file.getOriginalFilename()) && fileUploadError == null) {
            fileUploadError = "File with this name already exists.";
        }

        if (fileUploadError == null) {
            int rowsAdded = this.fileUploadService.addFile(file, user);
            if (rowsAdded < 0) {
                fileUploadError = "There was an error uploading the file. Please try again.";
            }
        }

        if (fileUploadError == null) {
            return "redirect:/home?msg=File uploaded successfully";
        } else {
            return "redirect:/home?msg=" + fileUploadError;
        }
    }


}
