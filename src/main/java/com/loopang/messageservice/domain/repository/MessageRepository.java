package com.loopang.messageservice.domain.repository;

import com.loopang.messageservice.domain.model.Message;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository {

  void save(Message message);

  Optional<Message> findById(UUID id);
}
