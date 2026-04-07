package com.loopang.messageservice.domain.repository;

import com.loopang.messageservice.domain.model.Message;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MessageRepository {

  void save(Message message);

  Optional<Message> findById(UUID id);

  Page<Message> findAll(Pageable pageable);
}
