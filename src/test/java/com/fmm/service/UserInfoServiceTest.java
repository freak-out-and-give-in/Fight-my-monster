package com.fmm.service;

import com.fmm.model.User;
import com.fmm.model.UserInfo;
import com.fmm.repository.UserInfoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UserInfoServiceTest {

    //mockito default objects have a scope of per class (as opposed to, for example, per method)
    //@MockBean ensures a scope of 'per method'

    //annotation used to inject the mock object into the class being tested. this is the correct usage of it.
    @InjectMocks
    private UserInfoService userInfoService;

    //annotation used to create mock object for specific class
    @Mock
    private UserInfoRepository userInfoRepository;

    @Test
    void testGetUserInfoById() {
        User user = new User("destroy57", "snake");
        UserInfo userInfo = new UserInfo(user);
        userInfo.setNuggets(BigInteger.valueOf(100));
        userInfo.setCurrentBackground("galaxy");

        //mockito's mock objects default behaviour is 'do nothing', so you have to specify the behaviour
        //below specifies what to return when the method (w/ certain parameters) is called
        when(userInfoRepository.findById(1L)).thenReturn(Optional.of(userInfo));
        UserInfo resultUserInfo = userInfoService.getUserInfo(1L);

        assertThat(resultUserInfo).isEqualTo(userInfo);
        //this verifies the userInfoRepository method findById was called exactly once with the correct id
        verify(userInfoRepository, times(1)).findById(1L);
    }

    @Test
    void testAddUserInfo() {
        User user = new User("plane6", "piggg");
        UserInfo userInfo = new UserInfo(user);
        userInfo.setNuggets(BigInteger.valueOf(100));
        userInfo.setCurrentBackground("metal");

        userInfoService.addUserInfo(userInfo);

        ArgumentCaptor<UserInfo> captor = ArgumentCaptor.forClass(UserInfo.class);
        verify(userInfoRepository).save(captor.capture());
        assertThat(captor.getValue().getNuggets()).isEqualTo(BigInteger.valueOf(100));
        assertThat(captor.getValue().getCurrentBackground()).isEqualTo("metal");
    }

    @Test
    void testUpdateUserInfo() {
        User user = new User("plane6", "piggg");
        UserInfo userInfo = new UserInfo(user);
        userInfo.setNuggets(BigInteger.valueOf(100));
        userInfo.setCurrentBackground("metal");

        userInfoService.addUserInfo(userInfo);

        ArgumentCaptor<UserInfo> captor = ArgumentCaptor.forClass(UserInfo.class);
        verify(userInfoRepository).save(captor.capture());
        assertThat(captor.getValue().getNuggets()).isEqualTo(BigInteger.valueOf(100));
        assertThat(captor.getValue().getCurrentBackground()).isEqualTo("metal");
    }

    @Test
    void testExchangeNuggetsForAccepting() {
        User userSentRequest = new User("abcd", "time");
        userSentRequest.setId(1L);
        UserInfo userInfoSentRequest = new UserInfo(userSentRequest);
        userInfoSentRequest.setNuggets(BigInteger.valueOf(340));

        User userAcceptedRequest = new User("Run", "89");
        userAcceptedRequest.setId(2L);
        UserInfo userInfoAcceptedRequest = new UserInfo(userAcceptedRequest);
        userInfoAcceptedRequest.setNuggets(BigInteger.valueOf(4));

        when(userInfoRepository.findById(1L)).thenReturn(Optional.of(userInfoSentRequest));
        when(userInfoRepository.findById(2L)).thenReturn(Optional.of(userInfoAcceptedRequest));

        userInfoService.exchangeNuggetsForAccepting(30L, 1L, 2L);

        ArgumentCaptor<UserInfo> captor = ArgumentCaptor.forClass(UserInfo.class);
        verify(userInfoRepository, times(2)).save(captor.capture());

        List<UserInfo> capturedUserInfo = captor.getAllValues();
        assertThat(capturedUserInfo.get(0).getNuggets()).isEqualTo(310);
        assertThat(capturedUserInfo.get(1).getNuggets()).isEqualTo(34);
    }

}
