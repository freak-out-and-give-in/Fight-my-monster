package com.fmm.dto;

import java.util.List;

public class UserDto {

    private Long id;

    private String username;

    private List<String> roles;

    private List<String> monsters;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public List<String> getMonsters() {
        return monsters;
    }

    public void setMonsters(List<String> monsters) {
        this.monsters = monsters;
    }
}
