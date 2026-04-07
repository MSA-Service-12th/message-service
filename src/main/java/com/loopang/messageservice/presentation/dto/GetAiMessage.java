package com.loopang.messageservice.presentation.dto;

import com.loopang.messageservice.domain.model.AiHistory;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetAiMessage {

  private UUID aiHistoryId;
  private UUID orderId;
  private String requestData;
  private String responseData;
  private String reason;
  private LocalDateTime createdAt;

  public static GetAiMessage from(AiHistory aiHistory) {
    return GetAiMessage.builder()
        .aiHistoryId(aiHistory.getId())
        .orderId(aiHistory.getOrderId())
        .requestData(aiHistory.getRequestData())
        .responseData(aiHistory.getResponseData())
        .reason(aiHistory.getReason())
        .createdAt(aiHistory.getCreatedAt())
        .build();
  }
}
