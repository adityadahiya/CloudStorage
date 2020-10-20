package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialsMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credentials;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CredentialsService {

    private final CredentialsMapper credentialsMapper;
    private final EncryptionService encryptionService;

    public CredentialsService(CredentialsMapper credentialsMapper, EncryptionService encryptionService) {
        this.credentialsMapper = credentialsMapper;
        this.encryptionService = encryptionService;
    }

    private Credentials encryptPassword(Credentials credential) {
        String key = RandomStringUtils.random(16, true, true);
        credential.setKey(key);
        credential.setPassword(encryptionService.encryptValue(credential.getPassword(), key));
        return credential;
    }

    public String decryptPassword(Integer credentialId) {
        Credentials credential = getCredentialsById(credentialId);
        return encryptionService.decryptValue(credential.getPassword(), credential.getKey());
    }

    public int addCredentials(Credentials credentials) {
        return credentialsMapper.insert(this.encryptPassword(credentials));
    }

    public int updateCredentials(Credentials credentials) {
        return credentialsMapper.updateCredentials(this.encryptPassword(credentials));
    }

    public Credentials getCredentialsById(Integer id) {
        return credentialsMapper.getCredentialsById(id);
    }

    public List<Credentials> getAllCredentials(User user) {
        return credentialsMapper.getAllCredentials(user);
    }

    public int deleteCredentials(Integer id) {
        return credentialsMapper.deleteCredentials(id);
    }
}
