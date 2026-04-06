package com.loopang.messageservice.domain.events;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

// 배달 -> ai 이벤트 발행
public record DeliveryCreatedEvent(
    // 주문
    UUID orderId, // 주문 id
    String name, // 주문자 이름
    String email, // 주문자 메일
    LocalDateTime createdAt, // 주문 시간
    String itemName, // 주문상품 이름
    Integer quantity, // 주문상품 수량
    String requirement, // 요청 사항

    // 배송
    UUID deliveryId, // 배송 id
    UUID hubManagerId, // 허브 담당자 id
    String slackTs, // 허브 담당자 slack id
    String companyCourierName, // 배송 담당자 이름
    String companyCourierEmail, // 배송 담당자 이메일
    String slackId, // 슬랙 id
    String departureHubAddress, // 출발지(허브)
    List<String> receiptHubAddresses, // 경유지(허브)
    String receiptCompanyAddress, // 도착지(업체)
    List<Integer> expectedTime // 예상 소요시간
) {}
