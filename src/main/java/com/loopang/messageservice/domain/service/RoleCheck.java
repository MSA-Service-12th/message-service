package com.loopang.messageservice.domain.service;

import com.loopang.messageservice.domain.model.UserType;
import java.util.UUID;

public interface RoleCheck {

  void checkDelete(UserType userType);

  void checkSearch(UserType userType);
}
