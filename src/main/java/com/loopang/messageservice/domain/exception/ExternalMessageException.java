package com.loopang.messageservice.domain.exception;

import com.loopang.common.exception.CustomException;
import org.springframework.http.HttpStatus;

public class ExternalMessageException extends CustomException {

  public ExternalMessageException(String message) {
    super(HttpStatus.BAD_GATEWAY, message);
  }
}
