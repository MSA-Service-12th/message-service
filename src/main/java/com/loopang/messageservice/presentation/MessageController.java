package com.loopang.messageservice.presentation;


import com.loopang.common.response.CommonResponse;
import com.loopang.messageservice.application.MessageService;
import com.loopang.messageservice.domain.model.UserType;
import com.loopang.messageservice.presentation.dto.MessageResponseDto;
import com.loopang.messageservice.presentation.dto.MessageResponseDto.GetMessage;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

  private MessageService messageService;

  public MessageController(MessageService messageService) {
    this.messageService = messageService;
  }

  // 삭제
  @DeleteMapping("/{id}")
  public CommonResponse<MessageResponseDto> delete(
      @PathVariable UUID id,
      @RequestHeader(value = "X-User-Role") String userRole) {

    return CommonResponse.success(messageService.delete(id, UserType.from(userRole)), "메시지가 삭제되었습니다.");
  }

  // 조회
  @GetMapping("/{id}")
  public CommonResponse<GetMessage> getMessage(
      @PathVariable UUID id,
      @RequestHeader(value = "X-User-Role") String userRole) {
    return CommonResponse.success(messageService.getMessage(id, UserType.from(userRole)), "메시지가 조회되었습니다.");
  }

  // 검색
  @GetMapping
  public CommonResponse<List<GetMessage>> searchMessage(
      @RequestHeader(value = "X-User-Role") String userRole,
      Pageable pageable) {
    Page<GetMessage> page = messageService.searchMessage(UserType.from(userRole), pageable);
    return CommonResponse.success(page.getContent(), "메시지가 조회되었습니다.");
  }

}
