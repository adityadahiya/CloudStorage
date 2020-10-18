package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credentials;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialsService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/credentials")
public class CredentialsController {

    private CredentialsService credentialsService;
    private UserService userService;

    public CredentialsController(CredentialsService credentialsService, UserService userService) {
        this.credentialsService = credentialsService;
        this.userService = userService;
    }

    @GetMapping("/delete/{id}")
    public String deleteCredentialsById(@PathVariable Integer id) {
        String credentialError = null;
        int rowDeleted = this.credentialsService.deleteCredentials(id);
        if (rowDeleted < 0) {
            credentialError = "There was an error deleting the credentials. Please try again.";
        }
        if (credentialError == null) {
            return "redirect:/home?msg=Credentials deleted successfully";
        } else {
            return "redirect:/home?msg=" + credentialError;
        }
    }

    @PostMapping("/add")
    public String addCredential(Credentials credential, Authentication authentication, Model model) {
        String credentialError = null;
        String credentialSuccess = null;
        String userName = (String) authentication.getPrincipal();
        User user = this.userService.getUser(userName);
        credential.setUserId(user.getUserId());

        if (credentialError == null) {
            int row = 0;
            if (credential.getCredentialId() > 0) {
                row = this.credentialsService.updateCredentials(credential);
                credentialSuccess = "Credentials updated successfully";
            } else {
                row = this.credentialsService.addCredentials(credential);
                credentialSuccess = "Credentials added successfully";
            }
            if (row < 0) {
                credentialError = "There was an error updating  the credentials. Please try again.";
            }
        }

        if (credentialError == null) {
            return "redirect:/home?msg=" + credentialSuccess;
        } else {
            return "redirect:/home?msg=" + credentialError;
        }

    }

    @GetMapping("/decrypt/{credentialId}")
    @ResponseBody
    public List<String> decryptCredential(@PathVariable("credentialId") Integer credentialId, HttpSession session,
                                          Model model) {
        return Arrays.asList(credentialsService.decryptPassword(credentialId));
    }
}
