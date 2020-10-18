package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FileUploadMapper {
    @Select("SELECT * FROM FILES WHERE fileId = #{fileId}")
    File getFile(Integer id);

    @Select("SELECT * FROM FILES WHERE fileName = #{fileName}")
    File getFileByName(String fileName);

    @Select("SELECT * FROM FILES WHERE userId = #{userId}")
    List<File> getAllFiles(User user);

    @Delete("DELETE FROM FILES WHERE fileId = #{fileId}")
    int deleteFile(Integer id);

    @Insert("INSERT INTO FILES (fileName, contentType, fileSize, userId, fileData) VALUES(#{fileName}, #{contentType}, #{fileSize}, #{userId}, #{fileData})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    int insert(File file);
}

