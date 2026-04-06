package com.loopang.messageservice.infrastructure.repository;

import com.loopang.messageservice.domain.model.AiHistory;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AiHistoryJpaRepository extends JpaRepository<AiHistory, UUID> {

}
