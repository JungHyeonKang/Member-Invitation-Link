package com.project.invitation.service;

import com.project.invitation.domain.Invitation;
import com.project.invitation.domain.InviteCodeStatus;
import com.project.invitation.domain.Member;
import com.project.invitation.domain.MemberStatus;
import com.project.invitation.dto.InviteRequestDto;
import com.project.invitation.dto.InviteResponseDto;
import com.project.invitation.exception.ExistingMemberException;
import com.project.invitation.exception.ExpiredLinkException;
import com.project.invitation.exception.InvalidLinkException;
import com.project.invitation.repository.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MemberService memberService;

    @Test
    public void 신규회원초대() throws Exception {

        //given
        String memberId = "testID";
        String memberName = "강정현";
        String memberPhone = "01012341234";
        String memberEmail = "test@naver.com";
        InviteRequestDto inviteRequestDto = createInviteRequestDto(memberId, memberName, memberPhone, memberEmail);

        //when
        InviteResponseDto dto = memberService.invite(inviteRequestDto);

        Optional<Member> findMember = memberRepository.findByMemberId(memberId);
        //then
        // 회원 저장 후 초대 코드와 저장한 회원 조회 후 그 회원의 초대 코드 같은지 비교
        assertEquals(dto.getInviteCode(),findMember.get().getInvitation().getInviteCode());
    }

    @Test
    public void 임시회원다시초대() throws Exception {
        //given
        String memberId = "testID";
        String memberName = "강정현";
        String memberPhone = "01012341234";
        String memberEmail = "test@naver.com";

        Invitation invitation = createInvitation();

        InviteRequestDto inviteRequestDto = createInviteRequestDto(memberId, memberName, memberPhone, memberEmail);

        Member member = createMemberEntity(memberId, memberName, memberPhone, memberEmail, invitation);

        //when
        Member savedMember = memberRepository.save(member);

        InviteResponseDto dto = memberService.invite(inviteRequestDto);


        /*
        * then
        * 멤버가 임시회원이고 기존 초대 코드와 새로만든 초대 코드가 다른지 비교
        */
        assertEquals(savedMember.getMemberStatus(), MemberStatus.TEMPORARY);
        assertNotEquals(dto.getInviteCode(),invitation.getInviteCode());

    }

    @Test
    public void 활성화회원초대() throws Exception {
        //given
        String memberId = "testID";
        String memberName = "강정현";
        String memberPhone = "01012341234";
        String memberEmail = "test@naver.com";

        Invitation invitation = createInvitation();

        InviteRequestDto inviteRequestDto = createInviteRequestDto(memberId, memberName, memberPhone, memberEmail);

        Member member = createMemberEntity(memberId, memberName, memberPhone, memberEmail, invitation);

        //when
        member.setMemberStatus(MemberStatus.ACTIVATE);
        Member savedMember = memberRepository.save(member);

        /*
         * then
         * 활성화회원 초대시 ExistingMemberException 에러 발생
         */
        assertEquals(savedMember.getMemberStatus(), MemberStatus.ACTIVATE);
        assertThrows(ExistingMemberException.class,()->{
            memberService.invite(inviteRequestDto);
        });
    }

    @Test
    public void 그룹입장() throws Exception {
        //given
        String memberId = "testID";
        String memberName = "강정현";
        String memberPhone = "01012341234";
        String memberEmail = "test@naver.com";

        Invitation invitation = createInvitation();

        InviteRequestDto inviteRequestDto = createInviteRequestDto(memberId, memberName, memberPhone, memberEmail);

        InviteResponseDto dto = memberService.invite(inviteRequestDto);

        String inviteCode = dto.getInviteCode();

        //when
        Optional<Member> findMember = memberRepository.findMemberWithInviteCode(inviteCode);

        /*
         * then
         * 초대코드와 맞는 멤버 정보가 있고 코드가 활성화 상태인지 비교
         */
        assertTrue(findMember.isPresent());
         assertEquals(findMember.get().getInvitation().getInviteCodeStatus(), InviteCodeStatus.ACTIVATE);

    }
    @Test
    public void 일분후초대코드만료() throws Exception {
        //given
        String memberId = "testID";
        String memberName = "강정현";
        String memberPhone = "01012341234";
        String memberEmail = "test@naver.com";

        Invitation invitation = createInvitation();
        invitation.setExpireDate(LocalDateTime.now());
        InviteRequestDto inviteRequestDto = createInviteRequestDto(memberId, memberName, memberPhone, memberEmail);

        //when
        InviteResponseDto dto = memberService.invite(inviteRequestDto);

        String inviteCode = dto.getInviteCode();
        LocalDateTime currentTime = invitation.getExpireDate().plusMinutes(1);

        Optional<Member> findMember = memberRepository.findMemberWithInviteCode(inviteCode);

        if (currentTime.isAfter(invitation.getExpireDate())) {
           findMember.get().getInvitation().expireInviteCodeStatus();
       }
        /*
         * then
         * 유효한 초대 코드이지만 코드 만료 시간이 지났다면 ExpiredLinkException 에러 발생
         */
        assertTrue(findMember.isPresent());
        assertThrows(ExpiredLinkException.class,()->{
           memberService.join(findMember.get().getInvitation().getInviteCode());
       });
    }

    private InviteRequestDto createInviteRequestDto(String memberId, String memberName, String memberPhone, String memberEmail) {
        InviteRequestDto inviteRequestDto = new InviteRequestDto();
        inviteRequestDto.setMemberId(memberId);
        inviteRequestDto.setMemberEmail(memberEmail);
        inviteRequestDto.setMemberName(memberName);
        inviteRequestDto.setMemberPhone(memberPhone);
        return inviteRequestDto;
    }

    private Member createMemberEntity(String memberId, String memberName, String memberPhone, String memberEmail, Invitation invitation) {
        Member member = new Member();
        member.setMemberId(memberId);
        member.setEmail(memberEmail);
        member.setName(memberName);
        member.setPhone(memberPhone);
        member.setInvitation(invitation);
        member.setMemberStatus(MemberStatus.TEMPORARY);
        return member;
    }

    private Invitation createInvitation() {
        Invitation invitation = new Invitation();
        invitation.setInviteCode("ABCD");
        invitation.setInviteCodeStatus(InviteCodeStatus.ACTIVATE);

        return invitation;
    }


}
