package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialsService;
import com.udacity.jwdnd.course1.cloudstorage.services.FileUploadService;
import com.udacity.jwdnd.course1.cloudstorage.services.NotesService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/home")
public class HomeController {

    @Autowired
    private FileUploadService fileUploadService;
    @Autowired
    private NotesService notesService;
    @Autowired
    private CredentialsService credentialsService;
    @Autowired
    private UserService userService;

    @GetMapping()
    public String getHomePage(Model model, Authentication authentication) {
        String userName = (String) authentication.getPrincipal();
        User user = this.userService.getUser(userName);
        model.addAttribute("fileData", this.fileUploadService.getAllFiles(user));
        model.addAttribute("notesData", this.notesService.getAllNotes(user));
        model.addAttribute("credentialsData", this.credentialsService.getAllCredentials(user));
        return "home";
    }
}
