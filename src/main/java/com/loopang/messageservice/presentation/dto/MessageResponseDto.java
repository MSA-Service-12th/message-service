package com.loopang.messageservice.presentation.dto;

import com.loopang.messageservice.domain.model.Message;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageResponseDto {
  private UUID id;

  public static MessageResponseDto from(Message message) {
    return new MessageResponseDto(message.getId());
  }
}
