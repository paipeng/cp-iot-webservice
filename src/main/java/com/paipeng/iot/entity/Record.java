package com.paipeng.iot.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;

@Entity
@Table(name = "record")
public class Record extends BaseEntity {


    @ManyToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "device_id", referencedColumnName = "id", nullable = false)
    //@LazyToOne(value = LazyToOneOption.FALSE)
    private Device device;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "record_type", columnDefinition = "int default 0", nullable = false)
    private RecordType recordType;

    @Column(name = "state", columnDefinition = "int default 0", nullable = false)
    private int state;

    @Column(name = "message", length = 16)
    private String message;

    @Column(name = "value", columnDefinition = "float default 0.0", nullable = false)
    private float value;

    @JsonIgnore
    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }


    @Transient
    public String getDeviceName() {
        return device.getName();
    }

    public RecordType getRecordType() {
        return recordType;
    }

    public void setRecordType(RecordType recordType) {
        this.recordType = recordType;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }
}
