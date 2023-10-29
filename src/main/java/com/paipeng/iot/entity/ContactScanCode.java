package com.paipeng.iot.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Generated;

import java.sql.Timestamp;

@Entity
@Table(name = "contact_scancode")
public class ContactScanCode extends BaseEntity{


    @ManyToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "send_user_id", referencedColumnName = "id", nullable = false)
    //@LazyToOne(value = LazyToOneOption.FALSE)
    private User sendUser;


    @ManyToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "receive_user_id", referencedColumnName = "id", nullable = false)
    //@LazyToOne(value = LazyToOneOption.FALSE)
    private User receiveUser;

    @Column(name = "uuid", nullable = false, length = 36, unique = true)
    private String uuid;

    @Column(name = "expire_time", columnDefinition = "TIMESTAMP")
    private Timestamp expireTime;


    @Column(name = "valid", columnDefinition = "bit default 0 ", nullable = false)
    private boolean valid;

    public User getSendUser() {
        return sendUser;
    }

    public void setSendUser(User sendUser) {
        this.sendUser = sendUser;
    }

    public User getReceiveUser() {
        return receiveUser;
    }

    public void setReceiveUser(User receiveUser) {
        this.receiveUser = receiveUser;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Timestamp getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Timestamp expireTime) {
        this.expireTime = expireTime;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }
}
