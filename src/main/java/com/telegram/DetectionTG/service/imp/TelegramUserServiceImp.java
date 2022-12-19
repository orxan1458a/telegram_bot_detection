package com.telegram.DetectionTG.service.imp;

import com.telegram.DetectionTG.model.TelegramUser;
import com.telegram.DetectionTG.repository.TelegramUserRepository;
import com.telegram.DetectionTG.service.TelegramBot;
import com.telegram.DetectionTG.service.TelegramUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TelegramUserServiceImp implements TelegramUserService {

    @Autowired
    private TelegramUserRepository telegramUserRepository;

    @Autowired
    private TelegramBot telegramBot;

    @Override
    public void save(TelegramUser telegramUser) {
        telegramUserRepository.save(telegramUser);
    }

    @Override
    public List<TelegramUser> findAll() {
        return (List<TelegramUser>) telegramUserRepository.findAll();
    }

    @Override
    public Optional<TelegramUser> findById(long id) {
        return telegramUserRepository.findById(id);
    }



    @Override
    public void sendImage(String uniqueString, String image) throws TelegramApiException, IOException {
        telegramBot.sendImageToUser(uniqueString,   image );
    }

    @Override
    public void sendVideo(String telegramToken, String video) throws TelegramApiException, IOException {
        telegramBot.sendVideoToUser(telegramToken,video);
    }

}
