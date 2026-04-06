package com.loopang.messageservice.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "p_ai_history")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AiHistory {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "ai_history_id")
  private UUID id;

  @Column(nullable = false)
  private UUID receiverId;

  @Column(nullable = false)
  private UUID orderId;

  @Column(columnDefinition = "TEXT", nullable = false)
  private String requestData;

  @Column(columnDefinition = "TEXT", nullable = false)
  private String responseData;

  @Column(columnDefinition = "TEXT")
  private String reason;

  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Builder
  private AiHistory(
      UUID receiverId,
      UUID orderId,
      String requestData,
      String responseData,
      String reason
  ) {
    this.receiverId = receiverId;
    this.orderId = orderId;
    this.requestData = requestData;
    this.responseData = responseData;
    this.reason = reason;
  }

  @PrePersist
  public void prePersist() {
    this.createdAt = LocalDateTime.now();
  }

  public static AiHistory of(
      UUID receiverId,
      UUID orderId,
      String requestData,
      String responseData,
      String reason
  ) {
    return AiHistory.builder()
        .receiverId(receiverId)
        .orderId(orderId)
        .requestData(requestData)
        .responseData(responseData)
        .reason(reason)
        .build();
  }
}