package com.telegram.DetectionTG.controller;

import com.telegram.DetectionTG.service.TelegramUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class TelegramController {

    @Autowired
    private TelegramUserService telegramUserService;

    @PostMapping("/sendImage")
    public String sendImage(@RequestBody String image) throws IOException, TelegramApiException {
        telegramUserService.sendImage("tg-gekeDUd",image);
        System.out.println("send to user image");
        return "photo sent";
    }

    @PostMapping("/sendVideo")
    public String sendVideo(@RequestBody String video) throws IOException, TelegramApiException {
        telegramUserService.sendVideo("tg-gekeDUd",video);
        System.out.println("send to user video");
        return "video sent";
    }
}
