package com.telegram.DetectionTG.service;


import com.google.common.io.ByteSource;
import com.telegram.DetectionTG.config.BotConfig;
import com.telegram.DetectionTG.model.TelegramUser;
import com.telegram.DetectionTG.model.User;
import com.telegram.DetectionTG.repository.TelegramUserRepository;
import com.vdurmont.emoji.EmojiParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

    @Autowired
    private TelegramUserService telegramUserService;
    @Autowired
    private UserService userService;
    @Autowired
    private TelegramUserRepository telegramUserRepository;
    final BotConfig config;

    static final String HELP_TEXT = "This bot is created to demonstrate Spring capabilities.\n\n" +
            "You can execute commands from the main menu on the left or by typing a command:\n\n" +
            "Type /start to see a welcome message\n\n" +
            "Type /mydata to see data stored about yourself\n\n" +
            "Type /help to see this message again";

    static final String YES_BUTTON = "YES_BUTTON";
    static final String NO_BUTTON = "NO_BUTTON";

    static final String ERROR_TEXT = "Error occurred: ";

    public TelegramBot(BotConfig config) {
        this.config = config;
        List<BotCommand> listofCommands = new ArrayList<>();
        listofCommands.add(new BotCommand("/start", "get a welcome message"));
        listofCommands.add(new BotCommand("/mydata", "get your data stored"));
        listofCommands.add(new BotCommand("/deletedata", "delete my data"));
        listofCommands.add(new BotCommand("/help", "info how to use this bot"));
        listofCommands.add(new BotCommand("/settings", "set your preferences"));
        try {
            this.execute(new SetMyCommands(listofCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("Error setting bot's command list: " + e.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {

            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            System.out.println(messageText + "   " + chatId);
            if (messageText.contains("/send") && config.getOwnerId() == chatId) {
                var textToSend = EmojiParser.parseToUnicode(messageText.substring(messageText.indexOf(" ")));
                var users = telegramUserService.findAll();
                for (TelegramUser user : users) {
                    prepareAndSendMessage(user.getChatId(), textToSend);
                }
            } else {
                if (messageText.length() > 3 && messageText.substring(0, 3).equals("tg-")) {
                    boolean keyIncludeDB = userService.includeTelegramToken(messageText);

                    User user = userService.findByTelegramToken(messageText);
                    if (keyIncludeDB) {
                        TelegramUser telegramOldDevice = telegramUserRepository.findByUserId(user.getId());
                        if (telegramOldDevice != null) {
                            telegramOldDevice.setUser(null);
                            telegramUserRepository.save(telegramOldDevice);
                            TelegramUser telegramNewDevice = telegramUserRepository.findById(chatId).get();
                            telegramNewDevice.setUser(user);
                            telegramUserRepository.save(telegramNewDevice);

                            String messageToUser = "Salam, " + user.getFirstName() + " " + user.getLastName() + "! Səni başqa bir cihaz \uD83D\uDCF2 ilə tanımışdım.Yəqin ki bu yeni cihazdır.\n\nYaxşı məlumatları artıq bu cihaza göndərəcəm.";
                            prepareAndSendMessage(chatId, messageToUser);
                        } else {
                            TelegramUser telegramUser = telegramUserRepository.findById(chatId).get();
                            telegramUser.setUser(user);
                            telegramUserRepository.save(telegramUser);

                            String messageToUser = EmojiParser.parseToUnicode("Salam dəyərli istifadəçi " + user.getFirstName() + " " + user.getLastName() + ",Telegrama xoş gəlmisiz!" + " :blush:");
                            prepareAndSendMessage(chatId, messageToUser);
                        }
                    } else {
                        prepareAndSendMessage(chatId, "Belə bir token mövcud deyil.");

                    }
                } else {
                    switch (messageText) {
                        case "/start":
                            registerUser(update.getMessage());
                            startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                            break;
                        case "/help":
                            prepareAndSendMessage(chatId, HELP_TEXT);
                            break;
                        default:
                            prepareAndSendMessage(chatId, "Sorry, command was not recognized");
                    }
                }
            }
        } else if (update.hasCallbackQuery()) {
            String callbackData = update.getCallbackQuery().getData();
            long messageId = update.getCallbackQuery().getMessage().getMessageId();
            long chatId = update.getCallbackQuery().getMessage().getChatId();

            if (callbackData.equals(YES_BUTTON)) {
                String text = "You pressed YES button";
                executeEditMessageText(text, chatId, messageId);
            } else if (callbackData.equals(NO_BUTTON)) {
                String text = "You pressed NO button";
                executeEditMessageText(text, chatId, messageId);
            }
        }
    }


    private void registerUser(Message msg) {

        if (telegramUserService.findById(msg.getChatId()).isEmpty()) {

            var chatId = msg.getChatId();
            var chat = msg.getChat();

            TelegramUser user = new TelegramUser();

            user.setChatId(chatId);
            user.setFirstName(chat.getFirstName());
            user.setLastName(chat.getLastName());
            user.setUserName(chat.getUserName());
            user.setRegisteredAt(new Timestamp(System.currentTimeMillis()));

            telegramUserService.save(user);
            log.info("user saved: " + user);
        }
    }

    private void startCommandReceived(long chatId, String name) {

        String answer = EmojiParser.parseToUnicode("Salam, " + name + ", platformaya xoş gəlmisiz!" + " :blush:");
        log.info("Replied to user " + name);

        sendMessage(chatId, answer);
    }

    private void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();

        List<KeyboardRow> keyboardRows = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();

        row.add("weather");
        row.add("get random joke");

        keyboardRows.add(row);

        row = new KeyboardRow();

        row.add("register");
        row.add("check my data");
        row.add("delete my data");

        keyboardRows.add(row);

        keyboardMarkup.setKeyboard(keyboardRows);

        message.setReplyMarkup(keyboardMarkup);

        executeMessage(message);
    }


    private void executeEditMessageText(String text, long chatId, long messageId) {
        EditMessageText message = new EditMessageText();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        message.setMessageId((int) messageId);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error(ERROR_TEXT + e.getMessage());
        }
    }

    private void executeMessage(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error(ERROR_TEXT + e.getMessage());
        }
    }

    private void prepareAndSendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);
        executeMessage(message);
    }

    public void sendImageToUser(String token, String textToSend) throws TelegramApiException, IOException {

        User user = userService.findByTelegramToken(token);
        TelegramUser telegramUser = telegramUserRepository.findByUserId(user.getId());
        InputFile inputFile = new InputFile();
        byte[] bytes = Base64.getDecoder().decode(textToSend);
        InputStream inputStream = ByteSource.wrap(bytes).openStream();
        inputStream.close();
        inputFile.setMedia(inputStream, "person");
        sendPhoto(String.valueOf(telegramUser.getChatId()), inputFile);
    }

    void sendPhoto(String chatId, InputFile photo) throws TelegramApiException {
        var message = new SendPhoto();
        message.setChatId(chatId);
        message.setCaption("Person");
        message.setPhoto(photo);

        execute(message);
    }

    public void sendVideoToUser(String token, String textToSend) throws TelegramApiException, IOException {
        User user = userService.findByTelegramToken(token);
        TelegramUser telegramUser = telegramUserRepository.findByUserId(user.getId());
        InputFile inputFile = new InputFile();
        byte[] bytes = Base64.getDecoder().decode(textToSend);
        InputStream inputStream = ByteSource.wrap(bytes).openStream();
        inputStream.close();
        inputFile.setMedia(inputStream, "person");
        sendVideo(String.valueOf(telegramUser.getChatId()), inputFile);

    }

    void sendVideo(String chatId, InputFile video) throws TelegramApiException {
        var message = new SendVideo();
        message.setChatId(chatId);
        message.setCaption("video");
        message.setVideo(video);
        execute(message);
    }
}
