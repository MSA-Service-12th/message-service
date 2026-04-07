package com.loopang.messageservice.domain.repository;

import com.loopang.messageservice.domain.model.AiHistory;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AiHistoryRepository {

  void save(AiHistory aiHistory);

  Page<AiHistory> findAll(Pageable pageable);

  Optional<AiHistory> findById(UUID id);
}
