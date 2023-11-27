package com.fmm.controller;

import com.fmm.exception.UsernameAlreadyExistsException;
import com.fmm.model.UserInfo;
import com.fmm.repository.UserInfoRepository;
import com.fmm.repository.RoleRepository;
import com.fmm.util.MyUserPrincipal;
import com.fmm.model.User;
import com.fmm.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Arrays;
import java.util.Collections;

@RestController
public class UserCreationController {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final UserInfoRepository userInfoRepository;

    @Autowired
    public UserCreationController(UserRepository userRepository, RoleRepository roleRepository, UserInfoRepository userInfoRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userInfoRepository = userInfoRepository;
    }

    @Transactional
    @PostMapping("/users")
    public RedirectView createUser(@ModelAttribute("user") User user, HttpServletRequest request) {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new UsernameAlreadyExistsException("This username is taken");
        }

        if (!user.isAcceptTermsAndConditions()) {
            return new RedirectView("/registration");
        }

        if (user.getUsername().isEmpty() || user.getPassword().isEmpty()) {
            return new RedirectView("/registration");
        }

        if (user.getUsername().length() > 20 || user.getPassword().length() > 30) {
            return new RedirectView("/registration");
        }

        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        user.setRoles(Collections.singletonList(roleRepository.findByName("ROLE_USER")));

        userRepository.save(user);
        userInfoRepository.save(new UserInfo(user));

        //below is so you don't have to login after registering
        MyUserPrincipal myUserPrincipal = new MyUserPrincipal(user);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken
                (myUserPrincipal, myUserPrincipal.getPassword(), myUserPrincipal.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        request.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder
                .getContext());

        return new RedirectView("/fmm/my-profile");
    }
}

