package com.project.invitation.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class InviteRequestDto {

    @NotNull
    @Size(min = 1,max = 100)
    private String memberId;

    @NotNull
    @Size(min = 1,max = 100)
    private String memberName;

    @NotNull
    @Pattern(regexp = "^[0-9]{10,11}$", message = "올바른 전화번호 형식이 아닙니다. (예: 01012341234)")
    @Size(min = 1,max = 20)
    private String memberPhone;

    @NotNull
    @Email(message = "올바른 이메일 주소 양식이 아닙니다.")
    @Size(min = 1,max = 100)
    private String memberEmail;
}
