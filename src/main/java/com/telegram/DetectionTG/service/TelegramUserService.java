package com.telegram.DetectionTG.service;


import com.telegram.DetectionTG.model.TelegramUser;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface TelegramUserService {
    void save(TelegramUser telegramUser);
    List<TelegramUser> findAll();
    Optional<TelegramUser> findById(long id);
    TelegramUser findByUniqueString(String uniqueString);
    void sendToUserNotification(String uniqueString,String image) throws TelegramApiException, IOException;
}
