package com.loopang.messageservice.domain.model;

import com.loopang.common.domain.BaseUserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "p_message")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Message extends BaseUserEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "message_id")
  private UUID id;         // 메시지 ID

  @Column(nullable = false)
  private UUID orderId; // 주문 ID

  @Column(nullable = false)
  private UUID receiverId; // 수신자 ID

  @Column(columnDefinition = "TEXT", nullable = false)
  private String content;

  private int retryCount = 0;

  private boolean isSuccess;



  @Builder
  public Message(UUID orderId, UUID receiverId, String content, int retryCount,
      boolean isSuccess) {
    this.orderId = orderId;
    this.receiverId = receiverId;
    this.content = content;
    this.retryCount = retryCount;
    this.isSuccess = isSuccess;
  }
}
