package iskander.tabaev.echobottg;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/*
Конфигурация для вебхука
 */
public class EchoBotWebHook extends TelegramWebhookBot {

    //  private static final Logger log = LoggerFactory.getLogger(EchoBot.class);

    private ExecutorService executor = Executors.newFixedThreadPool(5);

    @Value("${bot.name}")
    private String botName;

    @Autowired
    private TelegramBotsApi telegramBotsApi;
    @Value("${bot.token}")
    private String botToken;
    @Value("${bot.uri}")
    private String botUri;

    @Value("${bot.webhook.path}")
    private String webhookPath;


    @PostConstruct
    public void setWebHook() {
        try {
            SetWebhook setWebhook = SetWebhook.builder()
                    .url(botUri)
                    .build();
            this.setWebhook(setWebhook);
        } catch (TelegramApiException e) {
        }
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
    public String getBotPath() {
        return null;
    }


    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        return null;
    }

    public void sendAnswerMessage(SendMessage message) {
        try {
            this.execute(message);
            System.out.println("Сообщение успешно отправлено");
        } catch (TelegramApiException e) {
            System.out.println("Ошибка отправки сообщения: " + e.getMessage());
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
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                EchoBotWebHook.this.execute(this.sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

}
