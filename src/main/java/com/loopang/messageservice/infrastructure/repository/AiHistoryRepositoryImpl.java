package com.loopang.messageservice.infrastructure.repository;

import com.loopang.messageservice.domain.model.AiHistory;
import com.loopang.messageservice.domain.repository.AiHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AiHistoryRepositoryImpl implements AiHistoryRepository {

  private final AiHistoryJpaRepository jpaRepository;

  @Override
  public void save(AiHistory aiHistory) {
    jpaRepository.save(aiHistory);
  }
}
