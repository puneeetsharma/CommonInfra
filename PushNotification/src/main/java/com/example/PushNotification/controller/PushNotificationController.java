package com.example.PushNotification.controller;

import com.example.PushNotification.FeignUsers.FeignQuiz;
import com.example.PushNotification.dto.UserDeviceDto;
import com.example.PushNotification.model.*;
import com.example.PushNotification.service.PushNotificationService;
import com.example.PushNotification.service.UserDeviceService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class PushNotificationController {
	
	
//    private PushNotificationService pushNotificationService;
//
//    public PushNotificationController(PushNotificationService pushNotificationService) {
//        this.pushNotificationService = pushNotificationService;
//    }
//
    @Autowired
    PushNotificationService pushNotificationService;

    @Autowired
    UserDeviceService userDeviceService;

    @Autowired
    FeignQuiz feignQuiz;


    @PostMapping("/notification/token")
    public ResponseEntity sendTokenNotification(@RequestBody PushNotificationRequest request) {
        pushNotificationService.sendPushNotificationToToken(request);
        System.out.println("princr");
        return new ResponseEntity<>(new PushNotificationResponse(HttpStatus.OK.value(), "Notification has been sent."), HttpStatus.OK);
    }

    @PostMapping("/saveDeviceDetails/{userId}/{token}/{topic}")
    public String saveDeviceDetails(@PathVariable(value = "userId") String userId,
                                        @PathVariable(value = "token") String token,
                                        @PathVariable(value = "topic") String topic)
    {
        if(userDeviceService.findByToken(token).size()==0 || userDeviceService.findByUserId(userId).size()==0 ) {
            UserDeviceDto userDeviceDto = new UserDeviceDto();
            userDeviceDto.setUserId(userId);
            userDeviceDto.setToken(token);
            userDeviceDto.setTopic(topic);
            UserDevice userDevice = new UserDevice();
            BeanUtils.copyProperties(userDeviceDto, userDevice);
            userDeviceService.saveDetails(userDevice);
            return "saved";
        }
        return "alreadyPresent";
    }

    @PostMapping("/notification")
    public ResponseEntity sendTokenNotificationWithUserId(@RequestBody PushNotificationByUserId pushNotificationByUserId) {
        for(int i=0;i<userDeviceService.findByUserId(pushNotificationByUserId.getUserId()).size();i++) {
            PushNotificationRequest request = new PushNotificationRequest();
            request.setTitle(pushNotificationByUserId.getTitle());
            request.setMessage(pushNotificationByUserId.getMessage());
            request.setTopic(pushNotificationByUserId.getTopic());
            request.setToken(userDeviceService.findByUserId(pushNotificationByUserId.getUserId()).get(i).getToken());
            pushNotificationService.sendPushNotificationToToken(request);
        }
        return new ResponseEntity<>(new PushNotificationResponse(HttpStatus.OK.value(), "Notification has been sent."), HttpStatus.OK);
    }

    @PostMapping("/notifyLogin/{userId}")
    public String sendLoginNotificationWithUserId(@PathVariable(value = "userId") String userId) {
        System.out.println("jjjjjjjj");
        System.out.println(userDeviceService.findByUserId(userId).size());
        for(int i=0;i<userDeviceService.findByUserId(userId).size();i++) {
                PushNotificationRequest request = new PushNotificationRequest();
                request.setTitle("Login Successful");
                request.setMessage("Login Successful");
                request.setTopic(userDeviceService.findByUserId(userId).get(i).getTopic());
                System.out.println("puneet");
                request.setToken(userDeviceService.findByUserId(userId).get(i).getToken());
                System.out.println("jjjjjjjj");
                pushNotificationService.sendPushNotificationToToken(request);
            }
            return "notified";
    }

    @PostMapping("/notifyLoginToken/{token}")
    public String sendLoginNotificationWithUserIdAndToken(@PathVariable(value = "token") String token) {
        PushNotificationRequest request=new PushNotificationRequest();
        request.setTitle("Login Successful");
        request.setMessage("Login Successful");
        request.setTopic("quiz");
        System.out.println("puneet");
        request.setToken(token);
        pushNotificationService.sendPushNotificationToToken(request);
        return "Notification has been sent";
    }

    @PostMapping("/sendNotificationToMultipleUsers")
    public ResponseEntity sendNotificationToMultipleUsers(@RequestBody PushNotificationToMultipleUsers pushNotificationToMultipleUsers) {
        for(int i=0;i<pushNotificationToMultipleUsers.getUserId().size();i++) {
            PushNotificationRequest request = new PushNotificationRequest();
            request.setTitle(pushNotificationToMultipleUsers.getTitle());
            request.setMessage(pushNotificationToMultipleUsers.getMessage());
            request.setTopic(pushNotificationToMultipleUsers.getTopic());
            request.setToken(userDeviceService.findByUserId(pushNotificationToMultipleUsers.getUserId().get(i)).get(i).getToken());
            pushNotificationService.sendPushNotificationToToken(request);
        }
        return new ResponseEntity<>(new PushNotificationResponse(HttpStatus.OK.value(), "Notification has been sent."), HttpStatus.OK);
    }

    @PostMapping("/sendNotificationToQuiz")
    public ResponseEntity sendNotificationToQuiz(@RequestBody PushNotificationQuiz pushNotificationQuiz) {
        List<String> quizUserIds = feignQuiz.getQuizList();
        for(int i=0;i<quizUserIds.size();i++) {
            PushNotificationRequest request = new PushNotificationRequest();
            request.setTitle(pushNotificationQuiz.getTitle());
            request.setMessage(pushNotificationQuiz.getMessage());
            request.setTopic(pushNotificationQuiz.getTopic());
            if(userDeviceService.findByUserId(quizUserIds.get(i)).size()==0) {
                continue;
            }
            request.setToken(userDeviceService.findByUserId(quizUserIds.get(i)).get(0).getToken());
            pushNotificationService.sendPushNotificationToToken(request);
        }
        return new ResponseEntity<>(new PushNotificationResponse(HttpStatus.OK.value(), "Notification has been sent."), HttpStatus.OK);
    }

    @DeleteMapping("/deleteByToken")
    public String deleteByToken(@PathVariable(value = "token") String token)
    {
        if(userDeviceService.deleteByToken(token)!=null)
        {
            return "Deleted";
        }
        else
        {
           return "Token not Present";
        }
    }
    
}