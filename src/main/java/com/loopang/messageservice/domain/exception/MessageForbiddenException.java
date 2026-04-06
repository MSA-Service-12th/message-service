package com.loopang.messageservice.domain.exception;

import jakarta.ws.rs.ForbiddenException;

public class MessageForbiddenException extends ForbiddenException {

  public MessageForbiddenException(String message) {
    super(message);
  }
}
