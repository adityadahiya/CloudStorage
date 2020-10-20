package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Notes;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.NotesService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/notes")
public class NotesController {

    @Autowired
    private NotesService notesService;
    @Autowired
    private UserService userService;

    @GetMapping("/delete/{id}")
    public String deleteNoteById(@PathVariable Integer id) {
        String notesError = null;
        int rowDeleted = this.notesService.deleteNoteById(id);
        if (rowDeleted < 0) {
            notesError = "There was an error deleting the notes. Please try again.";
        }
        if (notesError == null) {
            return "redirect:/home?msg=Notes deleted successfully";
        } else {
            return "redirect:/home?msg=" + notesError;
        }
    }

    @PostMapping("/add")
    public String addNote(@ModelAttribute("note") Notes note, Authentication authentication, Model model) {
        String notesError = null;
        String notesSuccess = null;
        String userName = (String) authentication.getPrincipal();
        User user = this.userService.getUser(userName);
        note.setUserId(user.getUserId());
        if (notesError == null) {
            int rows = 0;
            if (note.getNoteId() > 0) {
                rows = this.notesService.editNote(note);
                notesSuccess = "Note updated successfully";
            } else {
                rows = this.notesService.addNote(note);
                notesSuccess = "Note added successfully";
            }
            if (rows < 0) {
                notesError = "There was an error updating  the notes. Please try again.";
            }
        }

        if (notesError == null) {
            return "redirect:/home?msg=" + notesSuccess;
        } else {
            return "redirect:/home?msg=" + notesError;
        }
    }

}
