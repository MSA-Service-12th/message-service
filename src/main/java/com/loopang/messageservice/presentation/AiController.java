package com.loopang.messageservice.presentation;


import com.loopang.common.response.CommonResponse;
import com.loopang.messageservice.application.MessageService;
import com.loopang.messageservice.domain.model.UserType;
import com.loopang.messageservice.presentation.dto.GetAiMessage;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
public class AiController {

  private MessageService messageService;

  public AiController(MessageService messageService) {
    this.messageService = messageService;
  }

  // 조회
  @GetMapping("/{id}")
  public CommonResponse<GetAiMessage> getAiMessage(
      @PathVariable UUID id,
      @RequestHeader(value = "X-User-Role") String userRole) {
    return CommonResponse.success(messageService.getAiMessage(id, UserType.from(userRole)), "ai 메시지가 조회되었습니다.");
  }

  // 검색
  @GetMapping
  public CommonResponse<List<GetAiMessage>> searchAiMessage(
      @RequestHeader(value = "X-User-Role") String userRole,
      Pageable pageable) {
    Page<GetAiMessage> page = messageService.searchAiMessage(UserType.from(userRole), pageable);
    return CommonResponse.success(page.getContent(), "ai 메시지가 조회되었습니다.");
  }
}
