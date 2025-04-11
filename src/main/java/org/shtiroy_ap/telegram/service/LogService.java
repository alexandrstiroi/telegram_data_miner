package org.shtiroy_ap.telegram.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shtiroy_ap.telegram.entity.Log;
import org.shtiroy_ap.telegram.repository.LogRepository;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
@Slf4j
public class LogService {
    private final LogRepository repository;

    public boolean insertLog(Update update){
        boolean result = true;
        try {
            Log log = new Log();
            log.setChatId(update.getMessage().getChatId());
            log.setMessage(update.getMessage().getText());
            log.setCreateStamp(LocalDateTime.now());
            repository.save(log);
            return result;
        } catch (Exception ex){
            log.error("error insert log {}", ex.getMessage());
        }
        return false;
    }
}
