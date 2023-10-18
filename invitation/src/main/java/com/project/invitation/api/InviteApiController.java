package com.project.invitation.api;


import com.project.invitation.dto.InviteRequestDto;
import com.project.invitation.dto.InviteResponseDto;
import com.project.invitation.dto.JoinResponseDto;
import com.project.invitation.exception.InvalidInviteException;
import com.project.invitation.exception.InvalidLinkException;
import com.project.invitation.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class InviteApiController {

    private final MemberService memberService;


    //회원 초대 컨트롤러
    @PostMapping("/api/invite")
    public InviteResponseDto inviteMember(@RequestBody @Valid InviteRequestDto inviteRequestDto , BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            List<String> errorList = new ArrayList<>();
            for (FieldError error : errors) {
                String errorMessage = error.getDefaultMessage();
                errorList.add(errorMessage);
            }
            throw new InvalidInviteException("올바르지 않은 데이터 양식입니다.",errorList);
        }

        InviteResponseDto inviteResponseDto = memberService.invite(inviteRequestDto);
        log.info("inviteCode {}", inviteResponseDto.getInviteCode());
        return inviteResponseDto;
    }

    //초대 수락 컨트롤러
    @PostMapping("/api/join/{inviteCode}")
    public JoinResponseDto join(@PathVariable String inviteCode) {
        log.info("inviteCode {}", inviteCode);
        if (inviteCode != null && !inviteCode.equals("")) {
            JoinResponseDto joinResponseDto = memberService.join(inviteCode);
            return joinResponseDto;
        }else{
            throw new InvalidLinkException("유효하지 않은 코드입니다.");
        }

    }
}
