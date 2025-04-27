package org.shtiroy_ap.telegram.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.shtiroy_ap.telegram.entity.Log;
import org.shtiroy_ap.telegram.repository.LogRepository;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalDateTime;

@Service
public class LogService {
    private final LogRepository repository;
    private final Logger log = LogManager.getLogger(LogService.class.getName());

    public LogService(LogRepository repository) {
        this.repository = repository;
    }

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

    public boolean insertLog(CallbackQuery callbackQuery){
        boolean result = true;
        try {
            Log log = new Log();
            log.setChatId(callbackQuery.getMessage().getChatId());
            log.setMessage(callbackQuery.getData());
            log.setCreateStamp(LocalDateTime.now());
            repository.save(log);
            return result;
        } catch (Exception ex){
            log.error("error insert log {}", ex.getMessage());
        }
        return false;
    }
}
