package com.fmm.util;

import com.fmm.model.UserInfo;
import com.fmm.repository.UserInfoRepository;
import com.fmm.model.Privilege;
import com.fmm.repository.PrivilegeRepository;
import com.fmm.model.Role;
import com.fmm.repository.RoleRepository;
import com.fmm.model.User;
import com.fmm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    boolean alreadySetup = false;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PrivilegeRepository privilegeRepository;

    private final UserInfoRepository userInfoRepository;

    @Autowired
    public SetupDataLoader(UserRepository userRepository, RoleRepository roleRepository, PrivilegeRepository privilegeRepository,
                           UserInfoRepository userInfoRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.privilegeRepository = privilegeRepository;
        this.userInfoRepository = userInfoRepository;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if (alreadySetup)
            return;
        Privilege deletePrivilege
                = createPrivilegeIfNotFound("DELETE_PRIVILEGE");

        List<Privilege> adminPrivileges = Collections.singletonList(
                deletePrivilege);

        createRoleIfNotFound("ROLE_ADMIN", adminPrivileges);
        createRoleIfNotFound("ROLE_USER", Collections.singletonList(deletePrivilege));

        //default admin made on creation is below
        Role adminRole = roleRepository.findByName("ROLE_ADMIN");
        User user = new User("a", new BCryptPasswordEncoder().encode("b"));
        user.setRoles(Collections.singletonList(adminRole));
        user.setEnabled(true);

        //without if statement this tries to add the same user on startup every time
        // and so throws error after 2nd+ starting of program
        if (userRepository.findByUsername(user.getUsername()) == null) {
            userRepository.save(user);
            UserInfo userInfo = new UserInfo(user);
            userInfoRepository.save(userInfo);
            //add in anything that needs to be created on startup (but only once) here
        }

        alreadySetup = true;
    }

    @Transactional
    public Privilege createPrivilegeIfNotFound(String name) {

        Privilege privilege = privilegeRepository.findByName(name);
        if (privilege == null) {
            privilege = new Privilege(name);
            privilegeRepository.save(privilege);
        }
        return privilege;
    }

    @Transactional
    public Role createRoleIfNotFound(
            String name, Collection<Privilege> privileges) {

        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role(name);
            role.setPrivileges(privileges);
            roleRepository.save(role);
        }
        return role;
    }
}
