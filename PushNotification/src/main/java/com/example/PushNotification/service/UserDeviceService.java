package com.example.PushNotification.service;

import com.example.PushNotification.model.UserDevice;
import org.springframework.context.annotation.Lazy;

import java.util.List;

public interface UserDeviceService {
    public UserDevice saveDetails(UserDevice userDevice);

    public List<UserDevice> findByUserId(String userId);
    public List<UserDevice> findByToken(String token);
    public  UserDevice deleteByToken(String token);
}
