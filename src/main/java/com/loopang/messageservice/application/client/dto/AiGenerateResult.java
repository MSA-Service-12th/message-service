package com.loopang.messageservice.application.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiGenerateResult {

  private String message;          // 생성된 메시지

  private String reason;           // 생성 사유

  private Integer inputTokens;     // 입력 토큰 수
  private Integer outputTokens;    // 출력 토큰 수
  private Integer totalTokens;     // 총 토큰 수

  private String model;            // 사용 모델
  private String finishReason;     // 응답 생성 종료 이유(stop / length / error 등)

  private boolean success;         // 성공 여부
  private String errorMessage;     // 실패 시 에러 메시지
}