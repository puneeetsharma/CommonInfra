package com.example.PushNotification.model;


import lombok.Data;

import java.util.List;

@Data
public class PushNotificationToMultipleUsers {

    private List<String> userId;
    private String title;
    private String message;
    private String topic;
}
