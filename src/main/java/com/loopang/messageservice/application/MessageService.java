package com.loopang.messageservice.application;


import com.loopang.messageservice.application.client.AiClient;
import com.loopang.messageservice.application.client.dto.AiGenerateResult;
import com.loopang.messageservice.application.prompt.AiPromptBuilder;
import com.loopang.messageservice.domain.exception.ExternalMessageException;
import com.loopang.messageservice.domain.MessageSend;
import com.loopang.messageservice.domain.events.DeliveryCreatedEvent;
import com.loopang.messageservice.domain.exception.MessageNotFoundException;
import com.loopang.messageservice.domain.model.AiHistory;
import com.loopang.messageservice.domain.model.Message;
import com.loopang.messageservice.domain.model.UserType;
import com.loopang.messageservice.domain.repository.AiHistoryRepository;
import com.loopang.messageservice.domain.repository.MessageRepository;
import com.loopang.messageservice.domain.service.RoleCheck;
import com.loopang.messageservice.presentation.dto.GetAiMessage;
import com.loopang.messageservice.presentation.dto.MessageResponseDto;
import com.loopang.messageservice.presentation.dto.MessageResponseDto.GetMessage;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
  private final RoleCheck roleCheck;


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

    checkDeletable(userType, roleCheck);
    Message message = messageRepository.findById(id).orElseThrow(
        () -> new MessageNotFoundException("존재하지 않는 메시지입니다."));

    message.delete();
    return MessageResponseDto.from(message);
  }


  // 단건 조회
  public GetMessage getMessage(UUID id, UserType userType) {
    roleCheck.checkSearch(userType);

    Message message = messageRepository.findById(id)
        .orElseThrow(() -> new MessageNotFoundException("존재하지 않는 메시지입니다."));

    return GetMessage.from(message);
  }

  // 전체 조회
  public Page<GetMessage> searchMessage(UserType userType, Pageable pageable) {
    roleCheck.checkSearch(userType);
    return messageRepository.findAll(pageable).map(GetMessage::from);
  }

  private void checkDeletable(UserType userType, RoleCheck roleCheck) {
    roleCheck.checkDelete(userType);
  }

  // ai 단건 조회
  public GetAiMessage getAiMessage(UUID id, UserType userType) {
    roleCheck.checkSearch(userType);
    AiHistory aiHistory = aiHistoryRepository.findById(id)
        .orElseThrow(() -> new MessageNotFoundException("존재하지 않는 메시지입니다."));

    return GetAiMessage.from(aiHistory);
  }

  // ai 전체 조회
  public Page<GetAiMessage> searchAiMessage(UserType userType, Pageable pageable) {
    roleCheck.checkSearch(userType);
    return aiHistoryRepository.findAll(pageable).map(GetAiMessage::from);
  }
}
