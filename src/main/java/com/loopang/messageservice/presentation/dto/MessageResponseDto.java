package com.loopang.messageservice.presentation.dto;

import com.loopang.messageservice.domain.model.Message;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
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

  @Getter
  @Builder
  public static class GetMessage {
    private String content;
    private UUID orderId;
    private UUID receiverId;
    private boolean isSuccess;
    private LocalDateTime createdAt;


    public static GetMessage from(Message message) {
      return GetMessage.builder()
          .content(message.getContent())
          .orderId(message.getOrderId())
          .receiverId(message.getReceiverId())
          .isSuccess(message.isSuccess())
          .createdAt(message.getCreatedAt())
          .build();
    }
  }
}
