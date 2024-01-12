package com.fmm.model;

import com.fmm.exception.NotEnoughNuggetsException;
import jakarta.persistence.*;

import java.math.BigInteger;
import java.util.List;

@Entity
@Table(name = "user_info")
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @OneToOne(mappedBy = "userInfo")
    private User user;

    @OneToMany(mappedBy = "userInfo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Monster> monsters;

    @OneToMany(mappedBy = "fromUserInfo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Message> message;

    @Column(name = "nuggets", nullable = false)
    private BigInteger nuggets = BigInteger.valueOf(100000);  //starting nugget amount for an account

    @Column(name = "current_background", nullable = false)
    private String currentBackground = "spots";

    public UserInfo() {
    }

    public UserInfo(User user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Monster> getMonsters() {
        return monsters;
    }

    public void setMonsters(List<Monster> monsters) {
        this.monsters = monsters;
    }

    public List<Message> getMessage() {
        return message;
    }

    public void setMessage(List<Message> message) {
        this.message = message;
    }

    public BigInteger getNuggets() {
        return nuggets;
    }

    public void setNuggets(BigInteger nuggets) {
        if (nuggets.compareTo(BigInteger.ZERO) < 0) {
            throw new NotEnoughNuggetsException("Not enough nuggets");
        } else {
            this.nuggets = nuggets;
        }
    }

    public String getCurrentBackground() {
        return currentBackground;
    }

    public void setCurrentBackground(String currentBackground) {
        this.currentBackground = currentBackground;
    }
}
