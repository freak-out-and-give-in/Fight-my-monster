package com.fmm.controller;

import com.fmm.enumeration.TypeOfFight;
import com.fmm.model.Message;
import com.fmm.model.User;
import com.fmm.model.UserInfo;
import com.fmm.repository.MessageRepository;
import com.fmm.repository.UserInfoRepository;
import com.fmm.repository.UserRepository;
import com.sun.security.auth.UserPrincipal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigInteger;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations="classpath:application-test.properties")
//@EnabledIf(expression = "#{environment['spring.profiles.active'] == 'test'}", loadContext = true)
@ActiveProfiles("test")
@Testcontainers
class PlayerControllerIntegrationTest {

    @Container
    static final MySQLContainer<?> databaseContainer = new MySQLContainer<>("mysql:latest");

    @DynamicPropertySource
    static void mySQLProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", databaseContainer::getJdbcUrl);
        registry.add("spring.datasource.username", databaseContainer::getUsername);
        registry.add("spring.datasource.password", databaseContainer::getPassword);
    }

    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private MessageRepository messageRepository;

    @BeforeEach
    void setup(WebApplicationContext webApplicationContext) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build();
    }

    @Test
    void showPlayersPage() throws Exception {
        String myUserUsername = "time";

        User myUser = new User(myUserUsername, "earth");
        UserInfo myUserInfo = new UserInfo(myUser);

        userRepository.save(myUser);
        userInfoRepository.save(myUserInfo);
        messageRepository.save(new Message(myUserInfo, 2L, TypeOfFight.EAT, "e", "x", 4L));

        mockMvc.perform(MockMvcRequestBuilders.get("/fmm/players")
                        .with(request -> {
                            request.setUserPrincipal(new UserPrincipal(myUserUsername));
                            return request;
                        }))
                .andExpect(status().isOk())
                .andExpect(view().name("/parts/players/players"))
                .andExpect(model().attributeExists("User"))
                .andExpect(model().attribute("User", hasProperty("username", is(myUserUsername))))
                .andExpect(model().attributeExists("Users"))
                .andExpect(model().attribute("Users", hasSize(1)))
                .andExpect(model().attributeExists("Background"))
                .andExpect(model().attribute("Background", is("spots")))
                .andExpect(model().attributeExists("Nuggets"))
                .andExpect(model().attribute("Nuggets", is(BigInteger.valueOf(100000))))
                .andExpect(model().attributeExists("selectedUser"))
                .andExpect(model().attribute("selectedUser", hasProperty("username", is(nullValue()))))
                .andExpect(model().attribute("selectedUser", hasProperty("password", is(nullValue()))))
                .andExpect(model().attributeExists("MessagesReceivedCount"))
                .andExpect(model().attribute("MessagesReceivedCount", is(1L)));

    }
}