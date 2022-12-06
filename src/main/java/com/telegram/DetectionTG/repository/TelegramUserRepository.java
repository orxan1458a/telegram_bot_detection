package com.telegram.DetectionTG.repository;

import com.telegram.DetectionTG.model.TelegramUser;
import org.springframework.data.repository.CrudRepository;

public interface TelegramUserRepository extends CrudRepository<TelegramUser, Long> {
}
