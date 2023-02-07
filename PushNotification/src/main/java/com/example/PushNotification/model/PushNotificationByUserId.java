package com.example.PushNotification.model;

import lombok.Data;

@Data
public class PushNotificationByUserId {
    private String userId;
    private String title;
    private String message;
    private String topic;

}
