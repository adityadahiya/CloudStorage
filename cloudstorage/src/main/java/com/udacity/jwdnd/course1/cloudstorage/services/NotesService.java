package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NotesMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Notes;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class NotesService {

    private final NotesMapper notesMapper;

    public NotesService(NotesMapper notesMapper) {
        this.notesMapper = notesMapper;
    }

    public int addNote(Notes note) {
        return notesMapper.insert(note);
    }

    public int editNote(Notes note) {
        return notesMapper.updateNote(note);
    }

    public Notes getNoteById(Integer id) {
        return notesMapper.getNoteById(id);
    }

    public List<Notes> getAllNotes(User user) {
        return notesMapper.getAllNotes(user);
    }
    public int deleteNoteById(Integer id) {
        return notesMapper.deleteNote(id);
    }
}
