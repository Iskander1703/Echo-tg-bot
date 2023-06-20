package iskander.tabaev.echobottg.controllers;

import iskander.tabaev.echobottg.EchoBotWebHook;
import iskander.tabaev.echobottg.services.BotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;


 /*
 Контроллер для вебхука от телеграмма
 Пока у меня нет ssl
  */
//@RestController
public class WebHookController {

    @Autowired
    private BotService botService;

    @Autowired
    private EchoBotWebHook echoBotWebHook;

    @RequestMapping(value = "${bot.webhook.path}", method = RequestMethod.POST)
    public ResponseEntity<?> handleWebhookUpdate(@RequestBody Update update) {
        String messageText = update.getMessage().getText();
        switch (messageText) {
            case "/start":
                Message message = update.getMessage();
                SendMessage sendMessage = botService.registerNewUser(message);
                echoBotWebHook.sendAnswerMessage(sendMessage);

        }

        return ResponseEntity.ok().build();
    }
}
