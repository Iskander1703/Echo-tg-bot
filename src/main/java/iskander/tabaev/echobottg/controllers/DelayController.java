package iskander.tabaev.echobottg.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import iskander.tabaev.echobottg.dto.DelayRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/delay")
@Tag(name = "Задержка", description = "Api для управления задержкой перед отправкой сообщения в боте")
public class DelayController {

    @Autowired
    private ConfigurableEnvironment environment;

    private static final Logger logger = LoggerFactory.getLogger(DelayController.class);


    @PutMapping("/update")
    @Operation(summary = "Изменение задержки", description = "Изменяет задержку перед отправкой сообщения в боте")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Задержка успешно изменена"),
            @ApiResponse(responseCode = "503", description = "Ошибка изменения задержки")
    })
    public ResponseEntity<Void> updateDelay(@Parameter(description = "Тело запроса для изменения задержки")
                                            @RequestBody DelayRequest request) {
        logger.info("Change delay value from:{}, to: {} ",
                environment.getProperty("echo.delay"), request.getDelay());

        MutablePropertySources propertySources = environment.getPropertySources();
        Map<String, Object> map = new HashMap<>();
        map.put("echo.delay", request.getDelay());
        propertySources.addFirst(new MapPropertySource("newmap", map));
        return ResponseEntity.ok().build();
    }
}
