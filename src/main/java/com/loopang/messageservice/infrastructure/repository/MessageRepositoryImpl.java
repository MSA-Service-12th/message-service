package com.loopang.messageservice.infrastructure.repository;

import com.loopang.messageservice.domain.model.Message;
import com.loopang.messageservice.domain.repository.MessageRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MessageRepositoryImpl implements MessageRepository {

  private final MessageJpaRepository jpaRepository;

  @Override
  public void save(Message message) {
    jpaRepository.save(message);
  }

  @Override
  public Optional<Message> findById(UUID id) {
    return jpaRepository.findById(id);
  }

  @Override
  public Page<Message> findAll(Pageable pageable) {
    return jpaRepository.findAll(pageable);
  }
}
