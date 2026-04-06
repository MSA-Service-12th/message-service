package com.loopang.messageservice.infrastructure.kafka.listener;

import com.loopang.common.messaging.IdempotentConsumer;
import com.loopang.common.util.JsonUtil;
import com.loopang.messageservice.application.MessageService;
import com.loopang.messageservice.domain.events.DeliveryCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class DeliveryCreatedEventListener {

  private final MessageService messageService;
  private final JsonUtil jsonUtil;

  @Transactional
  @IdempotentConsumer("delivery-created")
  @KafkaListener(topics = "${topics.delivery.message-created}", groupId = "message-group")
  public void orderCreated(Message<String> message, Acknowledgment ack) {
    try {
      DeliveryCreatedEvent event = jsonUtil.fromJson(message.getPayload(), DeliveryCreatedEvent.class);
      if(event != null) {
        messageService.createAiMessage(event);
        log.info("메시지 업데이트 처리 완료: orderId={}", event.orderId());
      }
      ack.acknowledge();
    } catch (Exception e) {
      log.error("메시지 업데이트 처리 실패: {}", e.getMessage(), e);
      throw e;
    }
  }

}