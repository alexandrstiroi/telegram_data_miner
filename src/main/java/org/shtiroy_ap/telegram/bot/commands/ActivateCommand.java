package org.shtiroy_ap.telegram.bot.commands;

import lombok.RequiredArgsConstructor;
import org.shtiroy_ap.telegram.entity.AllowedUser;
import org.shtiroy_ap.telegram.entity.UserRetries;
import org.shtiroy_ap.telegram.repository.AllowedUserRepository;
import org.shtiroy_ap.telegram.repository.UserRetriesRepository;
import org.shtiroy_ap.telegram.util.Consts;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class ActivateCommand implements Command{
    private final AllowedUserRepository repository;
    private final UserRetriesRepository retriesRepository;

    @Override
    public SendMessage apply(Update update) {
        long chatId = update.getMessage().getChatId();
        String pinCode = update.getMessage().getText();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        Optional<UserRetries> user = retriesRepository.findByChatId(chatId);
        if (user.isPresent() && user.get().getTries() >= 4) {
            sendMessage.setText(Consts.ACTIVATE_COMMAND_LIMIT);
        }
        if (pinCode.split(" ").length >= 2 && pinCode.split(" ")[1].equals("123456789") ) {
            AllowedUser allowedUser = new AllowedUser();
            allowedUser.setActive(true);
            allowedUser.setChatId(chatId);
            allowedUser.setUsername(update.getMessage().getFrom().getUserName());
            allowedUser.setFirstname(update.getMessage().getFrom().getFirstName());
            repository.save(allowedUser);
            sendMessage.setText(Consts.ACTIVATE_COMMAND);
        } else {
            if (user.isEmpty()){
                UserRetries userRetries = new UserRetries();
                userRetries.setChatId(chatId);
                userRetries.setTries(1);
                userRetries.setCreateStamp(LocalDateTime.now());
                userRetries.setModifyStamp(LocalDateTime.now());
                retriesRepository.save(userRetries);
                sendMessage.setText(Consts.ACTIVATE_COMMAND_NOT);
            }
            if (user.isPresent() && user.get().getTries() < 4) {
                user.get().setTries(user.get().getTries() + 1);
                user.get().setModifyStamp(LocalDateTime.now());
                retriesRepository.save(user.get());
                sendMessage.setText(Consts.ACTIVATE_COMMAND_NOT);
            }
        }
        return sendMessage;
    }
}
