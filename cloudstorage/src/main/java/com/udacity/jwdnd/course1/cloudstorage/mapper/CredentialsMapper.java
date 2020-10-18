package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Credentials;
import com.udacity.jwdnd.course1.cloudstorage.model.Notes;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CredentialsMapper {
    @Select("SELECT * FROM CREDENTIALS WHERE credentialId = #{credentialId}")
    Credentials getCredentialsById(Integer id);

    @Select("SELECT * FROM CREDENTIALS WHERE userId = #{userId}")
    List<Credentials> getAllCredentials(User user);

    @Delete("DELETE FROM CREDENTIALS WHERE credentialId = #{credentialId}")
    int deleteCredentials(Integer id);

    @Insert("INSERT INTO CREDENTIALS (url, userName, key, password, userId) VALUES(#{url}, #{userName}, #{key}, #{password}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "credentialId")
    int insert(Credentials credentials);

    @Update("UPDATE CREDENTIALS SET url = #{url}, userName =  #{userName}, key =  #{key}, password =  #{password} WHERE credentialId = #{credentialId}")
    int  updateCredentials(Credentials credentials);
}

