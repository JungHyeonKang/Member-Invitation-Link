package com.project.invitation.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Entity
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class Member extends TimeBaseEntity {

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

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "invitation_id")
    private Invitation invitation;


    //임시 회원 생성메서드
    public static Member createTemporaryMember(String memberId, String name, String phone, String email, Invitation invitation) {
        Member member = new Member();
        invitation.createInvitation();
        member.setMemberId(memberId);
        member.setName(name);
        member.setPhone(phone);
        member.setEmail(email);
        member.setInvitation(invitation);
        member.setMemberStatus(MemberStatus.TEMPORARY);
        return member;
    }

    // Invitation 과 연관메서드
    public void setInvitation(Invitation invitation) {
        this.invitation = invitation;
        invitation.setMember(this);
    }
    // 초대장 수정 메서드
    public String updateInviteCode() {
        return this.invitation.updateInvitation();
    }
    //회원 활성화 메서드
    public void activateMember() {
        this.memberStatus = MemberStatus.ACTIVATE;
    }
    // 임시회원 확인
    public boolean isMemberTemporary() {
        return this.memberStatus == MemberStatus.TEMPORARY ? true : false;
    }







}
