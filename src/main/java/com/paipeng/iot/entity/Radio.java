package com.paipeng.iot.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "radio")
public class Radio extends BaseEntity {
    @Column(nullable = false, length = 64, unique = true)
    private String name;

    @Column(name = "url", nullable = false, length = 256)
    private String url;

    @Column(name = "valid", columnDefinition = "bit default 0 ", nullable = false)
    private boolean valid;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }
}
