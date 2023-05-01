package com.license.outside_issues.service.message;

import com.license.outside_issues.service.citizen.dtos.ChatCitizenDTO;
import com.license.outside_issues.service.message.dtos.MessageDTO;

import java.util.List;

public interface MessageService {
    Long sendMessage(MessageDTO messageDTO);
    List<MessageDTO> getMessageForCitizen(Long citizenId);
    List<MessageDTO> getChatMessages(Long fromCitizenId, Long toCitizenId);
    List<MessageDTO> getLatestMessage(Long fromCitizenId, Long toCitizenId);
    MessageDTO findLatestMessageByEmail(String email);
    List<ChatCitizenDTO> getChatUsersByRole(String role);
    MessageDTO findLatestMessageForCitizen(Long fromId, Long toId);
}
