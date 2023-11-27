package com.fmm.service;

import com.fmm.model.UserInfo;
import com.fmm.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserInfoService {

    private final UserInfoRepository userInfoRepository;

    @Autowired
    public UserInfoService(UserInfoRepository userInfoRepository) {
        this.userInfoRepository = userInfoRepository;
    }

    public UserInfo getUserInfo(Long id) {
        return userInfoRepository.findById(id).get();
    }

    public void addUserInfo(UserInfo userInfo) {
        userInfoRepository.save(userInfo);
    }

    public void updateUserInfo(UserInfo userInfo) {
        userInfoRepository.save(userInfo);
    }
}
