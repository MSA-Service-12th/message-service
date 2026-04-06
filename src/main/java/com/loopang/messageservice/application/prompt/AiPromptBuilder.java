package com.loopang.messageservice.application.prompt;

import com.loopang.messageservice.domain.events.DeliveryCreatedEvent;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class AiPromptBuilder {

  private static final DateTimeFormatter DATE_TIME_FORMATTER =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  private final DeliveryDeadlineCalculator deliveryDeadlineCalculator;

  public AiPromptBuilder(DeliveryDeadlineCalculator deliveryDeadlineCalculator) {
    this.deliveryDeadlineCalculator = deliveryDeadlineCalculator;
  }

  public String buildRequest(DeliveryCreatedEvent event) {
    String deadline = deliveryDeadlineCalculator.calculateDeadline(
        event.createdAt(),
        event.expectedTime()
    );

    return """
        역할: 너는 물류/배차 메시지를 작성하는 전문가다.
        목표: 아래 주문/배송 정보를 바탕으로 배송 담당자에게 전달할 최종 메시지 1개를 작성한다.

        반드시 아래 출력 형식을 100%% 그대로 지켜라.
        절대 긴 문단으로 작성하지 마라.
        반드시 줄바꿈과 섹션 구조를 유지하라.
        설명, 인사말, 추가 안내 문구를 넣지 마라.
        결과 메시지만 출력하라.

        ===== 출력 형식 =====
        [배송 요청 안내]

        👤 배송 담당자
        - 이름: {배송담당자명}
        - 이메일: {배송담당자이메일}

        📦 주문 정보
        - 주문번호: {주문번호}
        - 주문자 이름: {주문자명}
        - 주문자 이메일: {주문자이메일}
        - 주문 시간: {주문시간}
        - 상품: {상품명} {수량}

        🚚 배송 경로
        - 출발: {발송지}
        - 경유: {경유지}
        - 도착: {도착지}

        📌 요청 사항
        - {요청사항}

        ⏰ 최종 발송 시한
        - {최종발송시한}까지 배송을 완료해야 합니다.
        ====================

        추가 규칙:
        - 항목 제목과 순서를 절대 바꾸지 마라.
        - 각 줄은 반드시 줄바꿈으로 구분한다.
        - 상품 수량은 반드시 숫자 뒤에 "개"를 붙인다.
        - 경유지는 "A → B → C" 형식으로 출력한다.
        - 요청 사항이 없으면 "없음"으로 출력한다.
        - 최종 발송 시한은 반드시 "~까지 배송을 완료해야 합니다." 문장으로 출력한다.
        - 설명성 문장을 추가하지 마라.

        ===== 실제 데이터 =====
        주문번호: %s
        주문자 이름: %s
        주문자 이메일: %s
        주문 시간: %s
        상품명: %s
        수량: %s
        발송지: %s
        경유지: %s
        도착지: %s
        요청사항: %s
        배송담당자명: %s
        배송담당자이메일: %s
        최종발송시한: %s
        ====================
        """.formatted(
        event.orderId(),
        safe(event.name()),
        safe(event.email()),
        event.createdAt().format(DATE_TIME_FORMATTER),
        safe(event.itemName()),
        formatQuantity(event.quantity()),
        safe(event.departureHubAddress()),
        formatHubAddresses(event.receiptHubAddresses()),
        safe(event.receiptCompanyAddress()),
        formatRequirement(event.requirement()),
        safe(event.companyCourierName()),
        safe(event.companyCourierEmail()),
        deadline
    );
  }

  private String formatRequirement(String requirement) {
    if (requirement == null || requirement.isBlank()) {
      return "없음";
    }
    return requirement.trim();
  }

  private String formatQuantity(Object quantity) {
    if (quantity == null) {
      return "0개";
    }

    String value = quantity.toString().trim();

    if (value.endsWith("개")) {
      return value;
    }

    return value + "개";
  }

  private String formatHubAddresses(List<String> receiptHubAddresses) {
    return receiptHubAddresses.stream()
        .map(String::trim)
        .collect(Collectors.joining(" → "));
  }

  private String safe(String value) {
    return value == null ? "" : value.trim();
  }
}