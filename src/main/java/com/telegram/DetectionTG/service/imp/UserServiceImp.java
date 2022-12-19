package com.telegram.DetectionTG.service.imp;

import com.telegram.DetectionTG.model.User;
import com.telegram.DetectionTG.repository.UserRepository;
import com.telegram.DetectionTG.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImp implements UserService {
    @Autowired
    UserRepository userRepository;

    @Override
    public boolean includeTelegramToken(String token) {
        List<User> usersDB = userRepository.findAll();
        boolean include = false;
        for (int i = 0; i < usersDB.size(); i++) {
            if (usersDB.get(i).getTelegramToken().equals(token)) {
                include = true;

                break;
            }
        }
        return include;
    }

    @Override
    public User findByTelegramToken(String token) {
        return userRepository.findByTelegramToken(token);
    }

    @Override
    public boolean checkTokenUnique(String key) {
        List<User> usersDB = userRepository.findAll();
        boolean isUnique = true;
        for (int i = 0; i < usersDB.size(); i++) {
            if (usersDB.get(i).getTelegramToken().equals(key)) {
                isUnique = false;
                break;
            }
        }
        return isUnique;    }
}
