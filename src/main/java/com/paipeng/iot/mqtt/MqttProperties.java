package com.paipeng.iot.mqtt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@ConfigurationProperties("mqtt")
@Configuration
public class MqttProperties {
    private String username;
    private String password;
    private String[] hostUrl;
    private String clientId;
    private String defaultTopic;
    private Long connectionTimeout;
    private String[] subscriptionTopic;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String[] getHostUrl() {
        return hostUrl;
    }

    public String getClientId() {
        return clientId;
    }

    public String getDefaultTopic() {
        return defaultTopic;
    }

    public Long getConnectionTimeout() {
        return connectionTimeout;
    }

    public String[] getSubscriptionTopic() {
        return subscriptionTopic;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setHostUrl(String[] hostUrl) {
        this.hostUrl = hostUrl;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setDefaultTopic(String defaultTopic) {
        this.defaultTopic = defaultTopic;
    }

    public void setConnectionTimeout(Long connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public void setSubscriptionTopic(String[] subscriptionTopic) {
        this.subscriptionTopic = subscriptionTopic;
    }
}
