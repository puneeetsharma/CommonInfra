package com.example.PushNotification.model;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "UserTokens")
public class UserDevice {
    @Id
    private String notifyId;
    private String userId;
    private String token;
    private String topic;
}
