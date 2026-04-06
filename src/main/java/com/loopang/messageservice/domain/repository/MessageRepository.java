package com.loopang.messageservice.domain.repository;

import com.loopang.messageservice.domain.model.Message;

public interface MessageRepository {

  void save(Message message);
}
