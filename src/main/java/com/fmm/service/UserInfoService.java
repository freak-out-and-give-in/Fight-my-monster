package com.fmm.service;

import com.fmm.dto.MessageDto;
import com.fmm.model.User;
import com.fmm.model.UserInfo;
import com.fmm.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;

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

    @Transactional
    public void exchangeNuggetsForAccepting(Long nuggetsForAccepting, Long sentRequestUserId, Long acceptedRequestUserId) {
        BigInteger bigIntegerNuggetsForAccepting = BigInteger.valueOf(nuggetsForAccepting);

        if (nuggetsForAccepting > 0) {
            UserInfo sentRequestUserInfo = getUserInfo(sentRequestUserId);
            sentRequestUserInfo.setNuggets(sentRequestUserInfo.getNuggets().subtract(bigIntegerNuggetsForAccepting));
            userInfoRepository.save(sentRequestUserInfo);

            UserInfo acceptedRequestUserInfo = getUserInfo(acceptedRequestUserId);
            acceptedRequestUserInfo.setNuggets(acceptedRequestUserInfo.getNuggets().add(bigIntegerNuggetsForAccepting));
            userInfoRepository.save(acceptedRequestUserInfo);
        }
    }
}
