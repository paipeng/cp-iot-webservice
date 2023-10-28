package com.paipeng.iot.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "device")
public class Device extends BaseEntity {
    @Column(name = "udid", nullable = false, length = 36, unique = true)
    private String udid;
    @Column(nullable = false, length = 64, unique = true)
    private String name;

    @Column(name = "description", nullable = true, length = 64)
    private String description;

    @Column(name = "expire", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp expire;

    @Column(name = "led", columnDefinition = "bit default 0 ", nullable = false)
    private boolean led;

    @Column(name = "temperature", columnDefinition = "bit default 0 ", nullable = false)
    private boolean temperature;

    @Column(name = "photosensitive", columnDefinition = "bit default 0 ", nullable = false)
    private boolean photosensitive;

    @Column(name = "message_board", columnDefinition = "bit default 0 ", nullable = false)
    private boolean messageBoard;

    @Column(name = "voice_control", columnDefinition = "bit default 0 ", nullable = false)
    private boolean voiceControl;
    @Column(name = "location", nullable = true, length = 128)
    private String location;

    @Column(name = "latitude", precision = 11, scale = 8)
    private BigDecimal latitude;

    @Column(name = "longitude", precision = 11, scale = 8)
    private BigDecimal longitude;

    @Column(name = "online", columnDefinition = "bit default 0 ", nullable = false)
    private boolean online;


    @Column(name = "pager", columnDefinition = "bit default 0 ", nullable = false)
    private boolean pager;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "device_user", joinColumns = @JoinColumn(name = "device_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> users;

    @OneToMany(cascade = {CascadeType.REFRESH}, fetch = FetchType.LAZY, mappedBy = "device")
    @LazyCollection(value = LazyCollectionOption.EXTRA)
    private List<Record> records;



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getExpire() {
        return expire;
    }

    public void setExpire(Timestamp expire) {
        this.expire = expire;
    }

    public boolean isLed() {
        return led;
    }

    public void setLed(boolean led) {
        this.led = led;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public boolean getPhotosensitive() {
        return photosensitive;
    }

    public void setPhotosensitive(boolean photosensitive) {
        this.photosensitive = photosensitive;
    }

    public String getUdid() {
        return udid;
    }

    public void setUdid(String udid) {
        this.udid = udid;
    }

    public boolean isTemperature() {
        return temperature;
    }

    public void setTemperature(boolean temperature) {
        this.temperature = temperature;
    }

    public boolean isPhotosensitive() {
        return photosensitive;
    }

    public boolean isMessageBoard() {
        return messageBoard;
    }

    public void setMessageBoard(boolean messageBoard) {
        this.messageBoard = messageBoard;
    }

    public boolean isVoiceControl() {
        return voiceControl;
    }

    public void setVoiceControl(boolean voiceControl) {
        this.voiceControl = voiceControl;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public boolean isPager() {
        return pager;
    }

    public void setPager(boolean pager) {
        this.pager = pager;
    }

    @JsonIgnore
    public List<Record> getRecords() {
        return records;
    }

    public void setRecords(List<Record> records) {
        this.records = records;
    }

    @JsonIgnore
    public List<String> getFeatureStrings() {
        List<String> featureStrings = new ArrayList<>();
        featureStrings.add("owner:STRING=" + getName());
        featureStrings.add("udid:STRING=" + getUdid());
        featureStrings.add("expire:DATE=" + getExpire());
        featureStrings.add("nanogrid:INT=" + (isLed()?"1":"0"));
        return featureStrings;
    }
}
