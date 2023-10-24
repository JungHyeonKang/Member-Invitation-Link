package com.project.invitation.service;
import com.project.invitation.domain.Invitation;
import com.project.invitation.domain.Member;
import com.project.invitation.dto.InviteRequestDto;
import com.project.invitation.dto.InviteResponseDto;
import com.project.invitation.dto.JoinResponseDto;
import com.project.invitation.exception.ExistingMemberException;
import com.project.invitation.exception.ExpiredLinkException;
import com.project.invitation.exception.InvalidLinkException;
import com.project.invitation.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public InviteResponseDto invite(InviteRequestDto dto){
        //멤버 아이디로 조회
        Optional<Member> findMember = memberRepository.findByMemberId(dto.getMemberId());

        if (findMember.isEmpty()) {// 신규멤버일때 멤버 데이터 생성 후 초대 코드 반환
            // 임시회원 생성
            Member temporaryMember = getTemporaryMember(dto);

            Member newMember = memberRepository.save(temporaryMember);

            return new InviteResponseDto(newMember.getInvitation().getInviteCode());

        }else{// 이미 초대된 멤버일때
            Member member = findMember.get();

             // 임시회원 일때
            if (member.isMemberTemporary()) {
                //초대 코드 업데이트
                String inviteCode = member.updateInviteCode();

                return new InviteResponseDto(inviteCode);

            }else{  // 이미 활성화된 멤버일때
                throw new ExistingMemberException();
            }
        }
    }

    @Transactional(noRollbackFor = ExpiredLinkException.class)
    public JoinResponseDto join(String inviteCode) {
        // 초대 코드를 통해 회원 조회
        Optional<Member> findMember = memberRepository.findMemberWithInviteCode(inviteCode);

        // 회원이 조회 되지 않을 때
        if (findMember.isEmpty()) {

            throw new InvalidLinkException();

        } else {
            Member member = findMember.get();

            // 만료된 링크
            if (member.getInvitation().isInvitationExpired()) {

                throw new ExpiredLinkException();

            }else{// 활성화 링크

                // 유효기간이 지난 링크 (1분)
                if (member.getInvitation().isInvitationTimeExpired()) {
                    // 초대장 만료상태로 변경
                    member.getInvitation().expireInviteCodeStatus();

                    throw new ExpiredLinkException("시간이 지나 만료된 링크입니다.");

                }else{ // 유효기간 남은 활성화 링크

                    // 초대장 만료상태로 변경
                    member.getInvitation().expireInviteCodeStatus();
                    // 회원 활성화
                    member.activateMember();

                    return new JoinResponseDto("그룹에 입장하셨습니다");
                }
            }
        }

    }
    // 임시회원 생성 메서드
    private Member getTemporaryMember(InviteRequestDto dto) {
        // 초대장 생성
        Invitation invitation = Invitation.createInvitation();
        // 임시 회원  생성
        Member temporaryMember = Member.createTemporaryMember(dto.getMemberId(), dto.getMemberName(), dto.getMemberPhone(), dto.getMemberEmail(),invitation);

        return temporaryMember;
    }






}
