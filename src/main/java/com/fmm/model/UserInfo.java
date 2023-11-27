package com.fmm.model;

import com.fmm.exception.NotEnoughNuggetsException;
import jakarta.persistence.*;

import java.math.BigInteger;

@Entity
@Table(name = "user_info")
public class UserInfo {

    @Id
    @Column(name = "account_id", nullable = false, unique = true)
    private Long account_id;

    @OneToOne
    @JoinColumn(name = "account_id")
    @MapsId
    private User user;

    @Column(name = "nuggets", nullable = false)
    private BigInteger nuggets = BigInteger.valueOf(100000);  //BigInteger.ZERO;

    @Column(name = "current_background", nullable = false)
    private String currentBackground = "spots";

    public UserInfo() {
    }

    public UserInfo(User user) {
        this.user = user;
    }

    public Long getAccount_id() {
        return account_id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BigInteger getNuggets() {
        return nuggets;
    }

    public void setNuggets(BigInteger nuggets) {
        if (nuggets.compareTo(BigInteger.ZERO) < 0) {
            throw new NotEnoughNuggetsException("Not enough money for this purchase");
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
