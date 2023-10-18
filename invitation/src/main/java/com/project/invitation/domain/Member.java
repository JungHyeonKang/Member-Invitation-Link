package com.project.invitation.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Entity
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class Member {

    @Id
    private String memberId;

    @Column(name = "member_name")
    private String name;

    @Column(name = "member_phone")
    private String phone;

    @Column(name = "member_email")
    private String email;

    @Enumerated(EnumType.STRING)
    private MemberStatus memberStatus;

    @Embedded
    private Invitation invitation;

    //임시 회원 생성메서드
    public static Member createTemporaryMember(String memberId, String name, String phone, String email, Invitation invitation) {
        Member member = new Member();
        member.setMemberId(memberId);
        member.setName(name);
        member.setPhone(phone);
        member.setEmail(email);
        member.setInvitation(invitation);
        member.setMemberStatus(MemberStatus.TEMPORARY);
        return member;
    }
    public InviteCodeStatus getInviteCodeStatus() {
        InviteCodeStatus inviteCodeStatus = invitation.getInviteCodeStatus();
        return inviteCodeStatus;
    }
    public void setInviteCode(String inviteCode) {
        invitation.setInviteCode(inviteCode);
    }
    // 링크 만료 메서드
    public void expireInviteCode() {
        invitation.setInviteCodeStatus(InviteCodeStatus.EXPIRED);
    }
    // 링크 활성화 메서드
    public void activateInviteCode() {
        invitation.setInviteCodeStatus(InviteCodeStatus.ACTIVATE);
    }


}
