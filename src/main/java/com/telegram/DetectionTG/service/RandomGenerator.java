package com.telegram.DetectionTG.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RandomGenerator {

    @Autowired
    UserService userService;
    String characters = "ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz";

    public String randomString() {

        int lenString = 7;
        String randomstring = "tg-";
        boolean isUnique = false;
        while(!isUnique) {
            for (int i = 0; i < lenString; i++) {
                int rnum = (int) Math.floor(Math.random() * characters.length());
                randomstring += characters.substring(rnum, rnum + 1);
            }
            isUnique = userService.checkTokenUnique(randomstring);
        }
        return randomstring;

    }
}