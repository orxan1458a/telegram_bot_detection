package com.telegram.DetectionTG.service;


import com.telegram.DetectionTG.model.User;

public interface UserService {
    boolean includeTelegramToken(String token);
    User findByTelegramToken(String token);
    boolean checkTokenUnique(String key);


}
