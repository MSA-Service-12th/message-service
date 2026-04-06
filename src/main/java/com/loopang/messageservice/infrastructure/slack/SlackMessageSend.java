package com.loopang.messageservice.infrastructure.slack;

import com.fasterxml.jackson.databind.JsonNode;
import com.loopang.messageservice.domain.MessageSend;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Service
public class SlackMessageSend implements MessageSend {

  @Value("${slack.token}")
  private String token;

  @Override
  public boolean sendHubManager(List<String> slackIds, String message) {

    if (slackIds != null) {

      RestClient client = RestClient.builder()
          .baseUrl("https://slack.com/api")
          .build();
      // Channel ID 처리
      ResponseEntity<JsonNode> response = client.post()
          .uri("/conversations.open")
          .header("Authorization", "Bearer " + token)
          .contentType(MediaType.APPLICATION_JSON)
          .body(Map.of("users", String.join(",", slackIds)))
          .retrieve()
          .toEntity(JsonNode.class);
      JsonNode node = response.getBody();

      JsonNode postNode = response.getBody();
      if (node == null || !response.getStatusCode().is2xxSuccessful() || !node.path("ok").asBoolean(false)) {
        return false;
      }
//      if (!response.getStatusCode().is2xxSuccessful() ||  node.get("ok") == null || !node.get("ok").toString().equals("true")) return false;

//      String channelId = node.get("channel").get("id").textValue();
      JsonNode channelNode = node.path("channel").path("id");
      if (channelNode.isMissingNode()) {
        return false;
      }
      String channelId = channelNode.textValue();

      // 메세지 발송 처리
      response = client.post()
          .uri("/chat.postMessage")
          .header("Authorization", "Bearer " + token)
          .body(Map.of("channel", channelId, "text", message, "as_user", true))
          .contentType(MediaType.APPLICATION_JSON)
          .retrieve()
          .toEntity(JsonNode.class);



//      return response.getStatusCode().is2xxSuccessful() && node.get("ok") != null && node.get("ok").toString().equals("true");
      return response.getStatusCode().is2xxSuccessful() && postNode != null && postNode.path("ok").asBoolean(false);
    }

    return false;
  }
}
