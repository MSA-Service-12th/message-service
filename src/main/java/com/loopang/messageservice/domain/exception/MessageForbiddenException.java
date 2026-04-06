package com.loopang.messageservice.domain.exception;

import com.loopang.common.exception.ForbiddenException;

public class MessageForbiddenException extends ForbiddenException {

  public MessageForbiddenException(String message) {
    super(message);
  }
}
