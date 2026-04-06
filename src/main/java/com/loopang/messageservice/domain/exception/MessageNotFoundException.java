package com.loopang.messageservice.domain.exception;

import jakarta.ws.rs.NotFoundException;

public class MessageNotFoundException extends NotFoundException {

  public MessageNotFoundException(String message) {
    super(message);
  }
}
