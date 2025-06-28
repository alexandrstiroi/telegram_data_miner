package org.shtiroy_ap.telegram.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.shtiroy_ap.telegram.entity.PinCode;
import org.shtiroy_ap.telegram.entity.User;
import org.shtiroy_ap.telegram.repository.PinCodeRepository;
import org.shtiroy_ap.telegram.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.shtiroy_ap.telegram.util.StringConstants.BOT_ERROR;

/**
 * Сервис авторизации.
 */
@Service
public class AuthService {
    @Value("${telegram.bot.pin-code}")
    private String pinCode;

    private final UserRepository userRepository;
    private final PinCodeRepository pinCodeRepository;
    private final Logger log = LogManager.getLogger(AuthService.class.getName());

    public AuthService(UserRepository userRepository, PinCodeRepository pinCodeRepository) {
        this.userRepository = userRepository;
        this.pinCodeRepository = pinCodeRepository;
    }

    public boolean isAuthorized(Long chatId) {
        return userRepository.findById(chatId)
                .map(User::isAuthorized)
                .orElse(false);
    }

    public void handleAuthorization(Update update, AbsSender sender) {
        Long chatId = update.getMessage().getChatId();
        String text = update.getMessage().getText();

        Optional<User> existing = userRepository.findById(chatId);
        if (existing.isEmpty()) {
            Optional<PinCode> optionalPinCode = pinCodeRepository.findByCode(text);
            if (optionalPinCode.isPresent()){
                PinCode pin = optionalPinCode.get();
                if (!pin.isAvailable()) {
                    log.warn("Превышено количество активаций для PIN: {}", pinCode);
                    sendMessage(sender, chatId, "🔐 Превышено количество активаций для PIN");
                    return;
                }
                User user = new User(chatId, update.getMessage().getFrom().getUserName(), true, pin.getCompany(),
                        LocalDateTime.now());
                userRepository.save(user);
                pin.incrementUsage();
                pinCodeRepository.save(pin);
                sendMessage(sender, chatId, "✅ Авторизация успешна!");
            } else {
                sendMessage(sender, chatId, "🔐 Введите PIN-код для доступа:");
            }
        } else {
            sendMessage(sender, chatId, "🔐 Ожидаем правильный PIN-код:");
        }
    }

    private void sendMessage(AbsSender sender, Long chatId, String text) {
        try {
            sender.execute(new SendMessage(chatId.toString(), text));
        } catch (TelegramApiException e) {
            log.error(BOT_ERROR, e.getMessage());
        }
    }
}