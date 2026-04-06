package com.loopang.messageservice.domain;


import java.util.List;

public interface MessageSend {
  boolean sendHubManager(List<String> slackIds, String message);
}
