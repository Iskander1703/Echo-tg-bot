package iskander.tabaev.echobottg.repository;

import iskander.tabaev.echobottg.models.EchoUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EchoUserRepository extends JpaRepository<EchoUser, Long> {
    Optional<EchoUser> findByUserId(Long userId);
}
