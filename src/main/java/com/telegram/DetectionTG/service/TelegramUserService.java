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
    void sendImage(String uniqueString,String image) throws TelegramApiException, IOException;
    void sendVideo(String uniqueString,String video) throws TelegramApiException, IOException;

}
