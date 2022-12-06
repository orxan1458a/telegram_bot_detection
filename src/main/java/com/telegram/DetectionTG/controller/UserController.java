package com.telegram.DetectionTG.controller;

import com.telegram.DetectionTG.model.User;
import com.telegram.DetectionTG.repository.UserRepository;
import com.telegram.DetectionTG.service.RandomGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@RestController
public class UserController {
    @Autowired
    RandomGenerator randomGenerator;
    @Autowired
    UserRepository userRepository;
//    @PostConstruct

    @GetMapping("/zzz")
    public void createUser(){

        System.out.println("project runing..");
//        User user=new User();
//        user.setUserName("admin");
//        user.setPassword("admin");
//        user.setFirstName("Orxan");
//        user.setLastName("Mansurov");
//        String telegramKey=randomGenerator.randomString();
//        user.setTelegramUniqueKey(telegramKey);
//        userRepository.save(user);
//        System.out.println("new user created");

    }
}
