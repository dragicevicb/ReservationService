package com.example.reservationservice.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class CreateNotificationDTO {

    public CreateNotificationDTO(Long recipientId, List<String> email, Map<String, String> content, String typeName) {
        this.recipientId = recipientId;
        this.email = email;
        this.content = content;
        this.typeName = typeName;
    }

    @NotNull
    Long recipientId;
    @NotBlank
    private List<String> email;
    @NotBlank
    private Map<String, String> content;
    @NotBlank
    private String typeName;

}
