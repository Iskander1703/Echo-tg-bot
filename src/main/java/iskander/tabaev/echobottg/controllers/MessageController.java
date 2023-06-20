package iskander.tabaev.echobottg.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import iskander.tabaev.echobottg.dto.LastUserMessage;
import iskander.tabaev.echobottg.models.EchoUser;
import iskander.tabaev.echobottg.models.EchoUserMessage;
import iskander.tabaev.echobottg.repository.EchoUserMessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/message")
@Tag(name = "Сообщения", description = "Api для получения информации о сообщениях")
public class MessageController {

    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);


    @Autowired
    EchoUserMessageRepository echoUserMessageRepository;

    @GetMapping("/last/{userId}")
    @Operation(summary = "Получение последнего сообщения пользователя по его id в телеграмм",
            description = "Получает последнее сообщения пользователя по его id в телеграмм")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Информация о последнем сообщении успешно получена"),
            @ApiResponse(responseCode = "503", description = "Ошибка получения последнего сообщения пользователя")
    })
    public ResponseEntity<LastUserMessage> getLastMessage(@Parameter(description = "Идентификатор пользователя в телеграмм", example = "320712028") @PathVariable String userId) {
        logger.info("Requested the last message of the user with id: {}",userId);
        Optional<EchoUserMessage> optionalEchoUserMessage = echoUserMessageRepository
                .findMessageWithMaxCountByUserId(Long.valueOf(userId));
        if (optionalEchoUserMessage.isEmpty()) {
            logger.error("Last message of the user with id: {} not found",userId);
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        } else {
            logger.info("Last message of the user with id: {} successfully found",userId);
            EchoUserMessage echoUserMessage = optionalEchoUserMessage.get();
            LastUserMessage lastUserMessage = new LastUserMessage();
            lastUserMessage.setText(echoUserMessage.getText());
            lastUserMessage.setUsername(echoUserMessage.getEchoUser().getUserName());
            return ResponseEntity.ok(lastUserMessage);
        }
    }
}
