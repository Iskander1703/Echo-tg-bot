package iskander.tabaev.echobottg.models;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
public class EchoUser {

    @Id
    private Long userId;

    private LocalDateTime firstUseDate;

    private String firstName;

    private String secondName;

    private String userName;

    private Integer messageCount;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "echoUser")
    private List<EchoUserMessage> messages;

}
