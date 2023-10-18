package com.project.invitation.service;

import com.project.invitation.domain.Invitation;
import com.project.invitation.domain.InviteCodeStatus;
import com.project.invitation.domain.Member;
import com.project.invitation.domain.MemberStatus;
import com.project.invitation.dto.InviteRequestDto;
import com.project.invitation.dto.InviteResponseDto;
import com.project.invitation.dto.JoinResponseDto;
import com.project.invitation.exception.ExistingMemberException;
import com.project.invitation.exception.ExpiredLinkException;
import com.project.invitation.exception.InvalidLinkException;
import com.project.invitation.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    @Transactional
    public InviteResponseDto invite(InviteRequestDto dto){
        String inviteCode;

        Optional<Member> findMember = memberRepository.findByMemberId(dto.getMemberId());

        if (!findMember.isPresent()) {// 신규멤버일때
            //멤버 데이터 생성 후 초대 코드 반환
            inviteCode = inviteNewMember(dto);

        }else{// 이미 초대된 멤버일때

            Member member = findMember.get();

            MemberStatus memberStatus = member.getMemberStatus();

            // 이미 활성화된 멤버일때
            if (memberStatus == MemberStatus.ACTIVATE) {

                throw new ExistingMemberException("이미 그룹에 가입 되었습니다");

            }else{ // 임시회원 일때

                //코드 새로 생성후 업데이트
                inviteCode = updateInviteCode(member);
            }
        }

        InviteResponseDto inviteResponseDto = new InviteResponseDto(inviteCode);

        return inviteResponseDto;
    }

    @Transactional
    public JoinResponseDto join(String inviteCode) {
        String message = "";
        Optional<Member> findMember = memberRepository.findByInvitationInviteCode(inviteCode);

        if (findMember.isPresent()) {
            Member member = findMember.get();

            // 링크가 만료되지 않았을 때
            if (member.getInviteCodeStatus() == InviteCodeStatus.ACTIVATE) {

                LocalDateTime expireDate = member.getInvitation().getExpireDate();
                // 링크가 만료되지 않았지만 시간이 지난 링크 일때
                if (isTimeElapsed(expireDate)) {
                    member.expireInviteCode();
                    throw new ExpiredLinkException("만료된 링크입니다.");
                } else {
                    // 유효한 링크일때
                    if (member.getMemberStatus() == MemberStatus.TEMPORARY) {
                        member.setMemberStatus(MemberStatus.ACTIVATE);
                        member.expireInviteCode();
                        message = "그룹에 입장하셨습니다!";
                    }else{ // 유효하지만 이미 가입되어 있을 때
                        member.expireInviteCode();
                        throw new ExistingMemberException("이미 그룹에 가입 되었습니다");
                    }
                }
            }else{ //만료된 링크 일때
                throw new ExpiredLinkException("만료된 링크입니다.");
            }
        }else{ // 유효하지 않은 링크 일때
            throw new InvalidLinkException("유효하지 않은 링크입니다");
        }

        return new JoinResponseDto(message);
    }

    // 임시회원일때, 초대 코드만 변경 메서드
    private String updateInviteCode(Member member) {
        String code = createCode();
        member.setInviteCode(code);
        member.activateInviteCode();
        return code;
    }

    // 신규 멤버 정보 생성 메서드
    private Member createNewMember(InviteRequestDto dto) {
        String code = createCode();

        Invitation invitation = Invitation.createInvitation(code);

        Member temporaryMember = Member.createTemporaryMember(dto.getMemberId(),dto.getMemberName(), dto.getMemberPhone(), dto.getMemberEmail(),invitation);

        return temporaryMember;
    }
    // 신규 멤버 초대 메서드
    private String inviteNewMember(InviteRequestDto dto) {
        String inviteCode = "";

        Member member = createNewMember(dto);

        Member newMember = memberRepository.save(member);

        inviteCode = newMember.getInvitation().getInviteCode();

        return inviteCode;
    }

    // 링크 생성
    private String createCode() {
        UUID uuid = UUID.randomUUID();

        return uuid.toString();
    }

    // 링크 만료 시간 체크 메서드
    private boolean isTimeElapsed(LocalDateTime expireDate) {
        LocalDateTime currentTime = LocalDateTime.now();
        if(expireDate.isBefore(currentTime)) return  true;
        else return false;
    }



}
