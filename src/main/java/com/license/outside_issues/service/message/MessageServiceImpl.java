package com.license.outside_issues.service.message;

import com.license.outside_issues.exception.BusinessException;
import com.license.outside_issues.exception.ExceptionReason;
import com.license.outside_issues.model.Citizen;
import com.license.outside_issues.model.Message;
import com.license.outside_issues.repository.CitizenRepository;
import com.license.outside_issues.repository.MessageRepository;
import com.license.outside_issues.service.message.dtos.MessageDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;
    private final CitizenRepository citizenRepository;

    public MessageServiceImpl(MessageRepository messageRepository, CitizenRepository citizenRepository) {
        this.messageRepository = messageRepository;
        this.citizenRepository = citizenRepository;
    }

    @Override
    public Long sendMessage(MessageDTO messageDTO) {
        messageDTO.setDate(LocalDateTime.now());
        return messageRepository.save(convertDTOToEntity(messageDTO)).getFromCitizen().getId();
    }

    @Override
    public List<MessageDTO> getMessageForCitizen(Long citizenId) {
        final List<Message> byFromCitizenId = messageRepository.findByFromCitizenId(citizenId);
        return byFromCitizenId.stream()
                .map(this::convertEntityToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<MessageDTO> getChatMessages(Long fromCitizenId, Long toCitizenId) {
        final List<Message> byFromCitizenIdAndToCitizenId = messageRepository.findByFromCitizenIdAndToCitizenId(toCitizenId, fromCitizenId);
        byFromCitizenIdAndToCitizenId.addAll(messageRepository.findByFromCitizenIdAndToCitizenId(fromCitizenId, toCitizenId));
        return byFromCitizenIdAndToCitizenId.stream()
                .map(this::convertEntityToDTO)
                .sorted(Comparator.comparing(MessageDTO::getDate))
                .collect(Collectors.toList());
    }

    @Override
    public MessageDTO getLatestMessage(Long fromCitizenId, Long toCitizenId) {
        final List<Message> byFromCitizenIdAndToCitizenId = messageRepository.findByFromCitizenIdAndToCitizenId(toCitizenId, fromCitizenId);
        byFromCitizenIdAndToCitizenId.addAll(messageRepository.findByFromCitizenIdAndToCitizenId(fromCitizenId, toCitizenId));
        final Optional<MessageDTO> lastMessage = byFromCitizenIdAndToCitizenId.stream()
                .map(this::convertEntityToDTO).max(Comparator.comparing(MessageDTO::getDate));
        return lastMessage.orElseGet(MessageDTO::new);
    }

    private Message convertDTOToEntity(MessageDTO messageDTO) {
        Message message = new Message();
        message.setMessage(messageDTO.getMessage());
        message.setDate(messageDTO.getDate());
        Citizen fromCitizen = citizenRepository.findByEmail(messageDTO.getFromCitizen()).orElseThrow(() -> {
            throw new BusinessException(ExceptionReason.CITIZEN_NOT_FOUND);
        });
        Citizen toCitizen = citizenRepository.findByEmail(messageDTO.getToCitizen()).orElseThrow(() -> {
            throw new BusinessException(ExceptionReason.CITIZEN_NOT_FOUND);
        });
        message.setFromCitizen(fromCitizen);
        message.setToCitizen(toCitizen);
        return message;
    }

    private MessageDTO convertEntityToDTO(Message message) {
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setMessage(message.getMessage());
        messageDTO.setFromCitizen(message.getFromCitizen().getEmail());
        messageDTO.setToCitizen(message.getToCitizen().getEmail());
        messageDTO.setDate(message.getDate());
        return messageDTO;
    }
}
