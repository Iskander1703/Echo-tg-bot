package iskander.tabaev.echobottg.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class EchoUserMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageId;

    private String text;

    private LocalDateTime receiveFromUser;

    private LocalDateTime sendToUser;

    private Integer messageCount;

    private String status;

    @ManyToOne
    @JoinColumn(name = "echo_user_id", nullable = false)
    private EchoUser echoUser;
}
