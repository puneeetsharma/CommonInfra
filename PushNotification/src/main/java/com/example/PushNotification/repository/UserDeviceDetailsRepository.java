package com.example.PushNotification.repository;

import com.example.PushNotification.model.UserDevice;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDeviceDetailsRepository extends MongoRepository<UserDevice,String> {
    public List<UserDevice> findByUserId(String userId);
    public List<UserDevice> findByToken(String token);
    public  UserDevice deleteByToken(String token);
}
