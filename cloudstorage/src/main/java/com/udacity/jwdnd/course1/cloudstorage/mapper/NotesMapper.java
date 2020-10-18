package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.Notes;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NotesMapper {
    @Select("SELECT * FROM NOTES WHERE noteId = #{noteId}")
    Notes getNoteById(Integer id);

    @Select("SELECT * FROM NOTES WHERE userId = #{userId}")
    List<Notes> getAllNotes(User user);

    @Delete("DELETE FROM NOTES WHERE noteId = #{noteId}")
    int deleteNote(Integer id);

    @Insert("INSERT INTO NOTES (noteTitle, noteDescription, userId) VALUES(#{noteTitle}, #{noteDescription}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "noteId")
    int insert(Notes note);

    @Update("UPDATE NOTES SET noteTitle = #{noteTitle}, noteDescription =  #{noteDescription} WHERE noteId = #{noteId}")
    int updateNote(Notes note);
}

