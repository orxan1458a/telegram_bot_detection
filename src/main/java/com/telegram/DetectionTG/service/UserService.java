package com.telegram.DetectionTG.service;


import com.telegram.DetectionTG.model.User;

public interface UserService {
    User findByTelegramUniqueKey(String TelegramUniqueKey);
    boolean checkStringUnique(String key);
    boolean  includeUniqueString(String key);
}
