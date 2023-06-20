package iskander.tabaev.echobottg.services;

import iskander.tabaev.echobottg.models.EchoUser;
import iskander.tabaev.echobottg.models.EchoUserMessage;
import iskander.tabaev.echobottg.repository.EchoUserMessageRepository;
import iskander.tabaev.echobottg.repository.EchoUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class BotService {

    @Autowired
    private EchoUserRepository echoUserRepository;

    @Autowired
    private EchoUserMessageRepository echoUserMessageRepository;

    public ExecutorService executor = Executors.newFixedThreadPool(5);

    public SendMessage registerNewUser(Message message) {

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(message.getChatId()));
        Optional<EchoUser> optionalEchoUser = echoUserRepository.findByUserId(message.getChatId());


        if (optionalEchoUser.isPresent()) {
            EchoUser echoUser = optionalEchoUser.get();
            sendMessage.setText("Вы уже являетесь пользователь бота\n" +
                    "Ваш счетчик сообщений: \n" + echoUser.getMessageCount());
        } else {

            User tgUser = message.getFrom();
            EchoUser echoUser = new EchoUser();
            echoUser.setUserId(message.getChatId());
            echoUser.setMessageCount(0);
            echoUser.setFirstUseDate(LocalDateTime.now());
            echoUser.setUserName(tgUser.getUserName());
            echoUser.setFirstName(tgUser.getFirstName());
            echoUser.setSecondName(tgUser.getLastName());

            EchoUser saveEchoUser = echoUserRepository.save(echoUser);

            if (saveEchoUser.getUserId() != null) {
                System.out.println("Пользователь успешно сохранен. ID: " + echoUser.getUserId());
                sendMessage.setText("Бот готов к использованию");
            } else {
                System.out.println("Ошибка сохранения пользователя");
                sendMessage.setChatId("Бот не готов к использованию");
            }
        }
        return sendMessage;
    }

    public SendMessage generateEchoMessage(Message message) {
        Long chatId = message.getChatId();
        Optional<EchoUser> optionalEchoUser = echoUserRepository.findByUserId(chatId);
        EchoUser echoUser = optionalEchoUser.get();

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        Integer messageCount = echoUser.getMessageCount() + 1;
        saveData(message, messageCount, echoUser);
        sendMessage.setText(message.getText() + " " + messageCount);

        return sendMessage;
    }

    private void saveData(Message message, Integer messageCount, EchoUser echoUser) {
        EchoUserMessage echoUserMessage = new EchoUserMessage();
        echoUserMessage.setEchoUser(echoUser);
        echoUserMessage.setSendToUser(null);
        echoUserMessage.setMessageCount(messageCount);
        echoUserMessage.setReceiveFromUser(LocalDateTime.now());
        echoUserMessage.setText(message.getText());
        echoUser.getMessages().add(echoUserMessage);
        echoUserMessageRepository.save(echoUserMessage);

        echoUser.setMessageCount(messageCount);
        echoUserRepository.save(echoUser);
    }


}
