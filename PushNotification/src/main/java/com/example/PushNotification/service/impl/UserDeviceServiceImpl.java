package com.example.PushNotification.service.impl;

import com.example.PushNotification.model.UserDevice;
import com.example.PushNotification.repository.UserDeviceDetailsRepository;
import com.example.PushNotification.service.UserDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDeviceServiceImpl implements UserDeviceService {

    @Autowired
    UserDeviceDetailsRepository userDeviceDetailsRepository;

    @Override
    public UserDevice saveDetails(UserDevice userDevice) {
        return userDeviceDetailsRepository.save(userDevice);
    }

    @Override
    public List<UserDevice> findByUserId(String userId) {
        return userDeviceDetailsRepository.findByUserId(userId);
    }

    @Override
    public List<UserDevice> findByToken(String token) {
        return userDeviceDetailsRepository.findByToken(token);
    }

    @Override
    public UserDevice deleteByToken(String token) {
        return userDeviceDetailsRepository.deleteByToken(token);
    }
}
