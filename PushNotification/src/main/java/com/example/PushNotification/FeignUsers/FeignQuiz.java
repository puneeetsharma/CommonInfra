package com.example.PushNotification.FeignUsers;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(url = "http://10.20.5.27:9091/QuizUser", value = "QuizUsers")
public interface FeignQuiz {
    @GetMapping("/getQuizUserIds")
    List<String> getQuizList();
}
