package com.fmm.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "message_id", nullable = false, unique = true)
    private Long messageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_account_id", nullable = false)
    private User fromUser;

    @Column(name = "to_account_id", nullable = false)
    private Long toAccountId;

    @Column(name = "typeOfFight", nullable = false)
    private String typeOfFight = "BITE";

    @Column(name = "toMonsterName", nullable = false)
    private String toMonsterName;

    @Column(name = "fromMonsterName", nullable = false)
    private String fromMonsterName;

    @Column(name = "nuggets_for_accepting")
    private long nuggetsForAccepting = 0L;

    @Column(name = "time_sent")
    private String timeSent = LocalDateTime.now().toString().substring(5, 10) + " " + LocalDateTime.now().toString().substring(11, 19);

    public Message() {
    }

    public Message(User fromUser, Long toAccountId, String typeOfFight, String toMonsterName, String fromMonsterName, Long nuggetsForAccepting) {
        this.fromUser = fromUser;
        this.toAccountId = toAccountId;
        this.typeOfFight = typeOfFight;
        this.toMonsterName = toMonsterName;
        this.fromMonsterName = fromMonsterName;
        this.nuggetsForAccepting = nuggetsForAccepting;
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public User getUser() {
        return fromUser;
    }

    public Long getToAccountId() {
        return toAccountId;
    }

    public void setToAccountId(Long toAccountId) {
        this.toAccountId = toAccountId;
    }

    public String getTypeOfFight() {
        return typeOfFight;
    }

    public void setTypeOfFight(String typeOfFight) {
        this.typeOfFight = typeOfFight;
    }

    public String getToMonsterName() {
        return toMonsterName;
    }

    public void setToMonsterName(String toMonsterName) {
        this.toMonsterName = toMonsterName;
    }

    public String getFromMonsterName() {
        return fromMonsterName;
    }

    public void setFromMonsterName(String fromMonsterName) {
        this.fromMonsterName = fromMonsterName;
    }

    public Long getNuggetsForAccepting() {
        return nuggetsForAccepting;
    }

    public void setNuggetsForAccepting(long nuggetsForAccepting) {
        this.nuggetsForAccepting = nuggetsForAccepting;
    }

    public String getTimeSent() {
        return timeSent;
    }

    public void setTimeSent(String timeSent) {
        this.timeSent = timeSent;
    }
}
