package com.loopang.messageservice.infrastructure.repository;

import com.loopang.messageservice.domain.model.Message;
import com.loopang.messageservice.domain.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MessageRepositoryImpl implements MessageRepository {

  private final MessageJpaRepository jpaRepository;

  @Override
  public void save(Message message) {
    jpaRepository.save(message);
  }
}
