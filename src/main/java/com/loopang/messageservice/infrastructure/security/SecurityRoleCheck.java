package com.loopang.messageservice.infrastructure.security;

import com.loopang.messageservice.domain.exception.MessageForbiddenException;
import com.loopang.messageservice.domain.model.UserType;
import com.loopang.messageservice.domain.service.RoleCheck;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityRoleCheck implements RoleCheck {

  @Override
  public void checkDelete(UserType userType) {
    if (userType != UserType.MASTER) {
      throw new MessageForbiddenException("권한이 없습니다. 마스터 권한 사용자만 메시지를 삭제할 수 있습니다.");
    }
  }

  @Override
  public void checkSearch(UserType userType) {
    if (userType != UserType.MASTER) {
      throw new MessageForbiddenException("권한이 없습니다. 마스터 권한 사용자만 메시지를 조회할 수 있습니다.");
    }
  }
}
