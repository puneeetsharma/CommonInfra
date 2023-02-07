package com.example.PushNotification.dto;

import lombok.Data;

@Data
public class UserDeviceDto {
    private String notifyId;
    private String userId;
    private String token;
    private String topic;
}
