package com.loopang.messageservice.application.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import com.loopang.messageservice.application.client.dto.AiGenerateResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AiClient {

  private final Client geminiClient;
  private final ObjectMapper objectMapper;

  public AiGenerateResult generateMessage(String requestPrompt) {
    try {
      String prompt = """
          너는 물류/배차 메시지 생성기다.
          반드시 JSON 객체만 반환해라.
          설명문, 인사말, 코드블록, 마크다운을 절대 포함하지 마라.
          JSON 시작은 { 문자여야 하고, JSON 끝은 } 문자여야 한다.

          반드시 아래 형식 그대로만 반환해라.

          {
            "message": "배송 담당자에게 전달할 최종 메시지",
            "reason": "이 메시지를 이렇게 만든 이유"
          }

          아래 주문/배송 정보를 기반으로 값을 채워라.

          [주문/배송 정보]
          """ + requestPrompt;

      GenerateContentResponse response =
          geminiClient.models.generateContent(
              "gemini-2.5-flash",
              prompt,
              null
          );

      String rawText = response.text();
      String cleanedText = extractJson(rawText);
      JsonNode root = objectMapper.readTree(cleanedText);

      return AiGenerateResult.builder()
          .message(root.path("message").asText(""))
          .reason(root.path("reason").asText(""))
          .success(true)
          .build();

    } catch (Exception e) {
      return AiGenerateResult.builder()
          .success(false)
          .errorMessage(e.getMessage())
          .build();
    }
  }

  private String extractJson(String text) {
    if (text == null || text.isBlank()) {
      throw new IllegalArgumentException("Gemini 응답이 비어 있습니다.");
    }

    String cleaned = text.trim();

    // ```json ... ``` 제거
    if (cleaned.startsWith("```json")) {
      cleaned = cleaned.substring(7).trim();
    } else if (cleaned.startsWith("```")) {
      cleaned = cleaned.substring(3).trim();
    }

    if (cleaned.endsWith("```")) {
      cleaned = cleaned.substring(0, cleaned.length() - 3).trim();
    }

    // 혹시 JSON 앞뒤에 불필요한 문구가 섞인 경우 대비
    int start = cleaned.indexOf('{');
    int end = cleaned.lastIndexOf('}');

    if (start == -1 || end == -1 || start > end) {
      throw new IllegalArgumentException("Gemini 응답에서 JSON 객체를 찾을 수 없습니다. 응답: " + cleaned);
    }

    return cleaned.substring(start, end + 1);
  }
}