package iskander.tabaev.echobottg;

import iskander.tabaev.echobottg.controllers.MessageController;
import iskander.tabaev.echobottg.services.BotService;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class EchoBotPolling extends TelegramLongPollingBot {

    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);


    /*
    В ExecutorService используется LinkedBlockingQueue,
    именно поэтому смысла в очереди нет.
     */

    @Autowired
    private Environment environment;
    private ExecutorService executor;

    @Value("${bot.name}")
    private String botName;

    @Autowired
    private TelegramBotsApi telegramBotsApi;
    @Value("${bot.token}")
    private String botToken;

    @Autowired
    private BotService botService;

    @PostConstruct
    public void initPolling() {
        try {
            telegramBotsApi.registerBot(this);
        } catch (TelegramApiException e) {
        }
        this.configureCommands();
        executor = Executors.newFixedThreadPool(Integer.parseInt(environment.getProperty("thread.count")));
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        String textMessage = update.getMessage().getText();

        if (textMessage.equals("/start")) {
            SendMessage sendMessage = botService.registerNewUser(message);
            sendAnswerMessage(sendMessage);

        } else if (textMessage.equals("/delay")) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(message.getChatId()));
            sendMessage.setText(environment.getProperty("echo.delay"));
            sendAnswerMessage(sendMessage);
        } else {
            SendMessage sendMessage = botService.generateEchoMessage(message);
            executor.execute(new sendEchoMessageTask(sendMessage));
        }
    }

    public void configureCommands() {
        List<BotCommand> commands = new ArrayList<>();
        commands.add(new BotCommand("start", "Начать работу с ботом"));
        commands.add(new BotCommand("delay", "Получить информацию о задержке"));

        SetMyCommands setMyCommands = new SetMyCommands();
        setMyCommands.setCommands(commands);

        try {
            execute(setMyCommands);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendAnswerMessage(SendMessage message) {
        try {
            logger.info("Start send message to user with id: {}, message text: {}",
                    message.getChatId(), message.getText());
            execute(message);
            logger.info("Successfully send message to user with id: {}, message text: {}",
                    message.getChatId(), message.getText());
        } catch (TelegramApiException e) {
            logger.info("Error send message to user with id: {}, message text: {}",
                    message.getChatId(), message.getText());
            e.printStackTrace();
        }
    }

    class sendEchoMessageTask implements Runnable {

        private SendMessage sendMessage;

        public sendEchoMessageTask(SendMessage sendMessage) {
            this.sendMessage = sendMessage;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(Long.parseLong(environment.getProperty("echo.delay")));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            EchoBotPolling.this.sendAnswerMessage(this.sendMessage);

        }
    }
}
