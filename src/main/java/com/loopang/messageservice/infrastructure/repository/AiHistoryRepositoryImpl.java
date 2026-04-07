package com.loopang.messageservice.infrastructure.repository;

import com.loopang.messageservice.domain.model.AiHistory;
import com.loopang.messageservice.domain.repository.AiHistoryRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AiHistoryRepositoryImpl implements AiHistoryRepository {

  private final AiHistoryJpaRepository jpaRepository;

  @Override
  public void save(AiHistory aiHistory) {
    jpaRepository.save(aiHistory);
  }

  @Override
  public Page<AiHistory> findAll(Pageable pageable) {
    return jpaRepository.findAll(pageable);
  }

  @Override
  public Optional<AiHistory> findById(UUID id) {
    return jpaRepository.findById(id);
  }
}
