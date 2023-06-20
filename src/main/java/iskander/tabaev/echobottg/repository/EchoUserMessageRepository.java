package iskander.tabaev.echobottg.repository;

import iskander.tabaev.echobottg.models.EchoUserMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EchoUserMessageRepository extends JpaRepository<EchoUserMessage, Long> {

    @Query("SELECT m FROM EchoUserMessage m WHERE m.echoUser.userId = :userId AND m.messageCount = (SELECT MAX(mm.messageCount) FROM EchoUserMessage mm WHERE mm.echoUser.userId = :userId)")
    Optional<EchoUserMessage> findMessageWithMaxCountByUserId(@Param("userId") Long userId);
}
