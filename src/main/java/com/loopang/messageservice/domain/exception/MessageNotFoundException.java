package com.loopang.messageservice.domain.exception;

import com.loopang.common.exception.NotFoundException;

public class MessageNotFoundException extends NotFoundException {

  public MessageNotFoundException(String message) {
    super(message);
  }
}
