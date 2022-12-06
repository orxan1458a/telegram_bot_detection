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
    public TelegramUser findByUniqueString(String uniqueString) {
        TelegramUser telegramUser = new TelegramUser();
        Iterable<TelegramUser> tgUserDB = telegramUserRepository.findAll();
        List<TelegramUser> list = new ArrayList<>();
        for (TelegramUser tgUser : tgUserDB) {
            list.add(tgUser);
        }
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getUniqueString() != null && list.get(i).getUniqueString().equals(uniqueString)) {
                telegramUser = list.get(i);
                break;
            }
        }
        return telegramUser;
    }

    @Override
    public void sendToUserNotification(String uniqueString, String image) throws TelegramApiException, IOException {
        telegramBot.sendToUser(uniqueString,   image );
    }

}
