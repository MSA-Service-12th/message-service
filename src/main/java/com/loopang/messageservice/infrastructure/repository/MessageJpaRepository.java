package com.loopang.messageservice.infrastructure.repository;

import com.loopang.messageservice.domain.model.Message;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageJpaRepository extends JpaRepository<Message, UUID> {

}
