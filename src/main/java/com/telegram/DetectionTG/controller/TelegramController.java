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

    @GetMapping("/")
    public String home(){
        System.out.println("home");
        return "home";
    }

    @GetMapping("/sendPhoto")
    public String sendPhoto() throws IOException, TelegramApiException {
        String image="iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAMAAAAoLQ9TAAAAA3NCSVQICAjb4U/gAAAACXBIWXMAAABvAAAAbwHxotxDAAAAGXRFWHRTb2Z0d2FyZQB3d3cuaW5rc2NhcGUub3Jnm+48GgAAAKVQTFRF////AAD/AACqAACAG0ONDyRxDiJuGUeNGkaNGkaNGkaNGUaNCx1mDB9nDB9pGkaNChhhCRVeEi93CRNeDyZuGkaNBQtVBQxWDydvEzF5GkaNSU2DTWykTlKGT3GnU1iKU2ygVXarV1uNWF2OXn2vX2OSZWmWZWmXZYOzZ2uYaIW0am6ac465dpC7eJK8e36lh57DiaDE+/v8+/z9/P39/f3+/v7+BFJz1gAAABZ0Uk5TAAEDBCZGSnB/gLO1tr6+5uru7/Dz9mzZxVgAAACgSURBVBhXTY/rGoIwCEBnWctprZtodLHsXmblpfd/tGAzv84vOAwGQhCup7RWnissjgzREErH5D62+Gwk/iGpn95vMk5uW+pyhUfhrnwj5nUSIw6E4uKlfOX1FSBGJTRa87kDEetGZFXxZAFz2/KoTqk1EzP0UJ0B0uJIYmi+Xe25uF4CRH272CICS/BbvTHjbnscmyjoNPfy+dPZqMfxF0idFQwfOpTRAAAAAElFTkSuQmCC";
        telegramUserService.sendToUserNotification("tg-gekeDUd",image);
        System.out.println("send to user");


        return "photo sent";
    }
    @PostMapping("/sendImage")
    public String sendImage(@RequestBody String image) throws IOException, TelegramApiException {
        telegramUserService.sendToUserNotification("tg-gekeDUd",image);
        System.out.println("send to user");
        return "photo sent";
    }
}
