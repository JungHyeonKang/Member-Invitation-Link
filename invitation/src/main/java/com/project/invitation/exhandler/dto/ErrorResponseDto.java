package com.project.invitation.exhandler.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ErrorResponseDto {
    String code;
    String message;
    List<String> validationError = new ArrayList<>();

    public ErrorResponseDto(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public ErrorResponseDto(String code, String message, List<String> validationError) {
        this.code = code;
        this.message = message;
        this.validationError = validationError;
    }
}
