package com.loopang.messageservice.application;


import com.loopang.messageservice.application.client.AiClient;
import com.loopang.messageservice.application.client.dto.AiGenerateResult;
import com.loopang.messageservice.application.prompt.AiPromptBuilder;
import com.loopang.messageservice.domain.exception.ExternalMessageException;
import com.loopang.messageservice.domain.MessageSend;
import com.loopang.messageservice.domain.events.DeliveryCreatedEvent;
import com.loopang.messageservice.domain.exception.MessageForbiddenException;
import com.loopang.messageservice.domain.exception.MessageNotFoundException;
import com.loopang.messageservice.domain.model.AiHistory;
import com.loopang.messageservice.domain.model.Message;
import com.loopang.messageservice.domain.model.UserType;
import com.loopang.messageservice.domain.repository.AiHistoryRepository;
import com.loopang.messageservice.domain.repository.MessageRepository;
import com.loopang.messageservice.presentation.dto.MessageResponseDto;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MessageService {

  private final AiPromptBuilder aiPromptBuilder;
  private final AiClient aiClient;
  private final AiHistoryRepository aiHistoryRepository;
  private final MessageRepository messageRepository;
  private final MessageSend messageSend;


  // 배송 생성 -> 메시지 생성
  public void createAiMessage(DeliveryCreatedEvent event) {
    String requestPrompt = aiPromptBuilder.buildRequest(event);

    // 응답 메시지
    AiGenerateResult result = aiClient.generateMessage(requestPrompt);

    if (!result.isSuccess()) {
      throw new ExternalMessageException("Gemini 메시지 생성 실패: " + result.getErrorMessage());
    }

    AiHistory aiHistory = AiHistory.builder()
        .receiverId(event.hubManagerId())
        .orderId(event.orderId())
        .requestData(requestPrompt)
        .responseData(result.getMessage())
        .reason(result.getReason())
        .build();
    aiHistoryRepository.save(aiHistory);


    boolean sent = messageSend.sendHubManager(List.of(event.slackId()), result.getMessage());
    if (!sent) {
      throw new ExternalMessageException("slack 전송 실패: orderId = " + event.orderId());
    }
    Message message = Message.builder()
        .orderId(event.orderId())
        .receiverId(event.hubManagerId()) // 허브 담당자
        .content(result.getMessage())
        .isSuccess(true)
        .build();

    messageRepository.save(message);

  }


  public MessageResponseDto delete(UUID id, UserType userType) {
    if (userType == null || userType == UserType.PENDING) {
      throw new MessageForbiddenException("권한이 없습니다. 마스터 권한 사용자만 메시지를 삭제할 수 있습니다.");
    }
    Message message = messageRepository.findById(id).orElseThrow(
        () -> new MessageNotFoundException("존재하지 않는 메시지입니다."));

    message.delete();
    return MessageResponseDto.from(message);
  }
}
