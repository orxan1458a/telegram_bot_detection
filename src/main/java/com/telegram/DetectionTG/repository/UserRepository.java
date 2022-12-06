package com.telegram.DetectionTG.repository;


import com.telegram.DetectionTG.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository  extends JpaRepository<User,Long> {
    User findByTelegramUniqueKey(String findByTelegramUniqueKey);
}
