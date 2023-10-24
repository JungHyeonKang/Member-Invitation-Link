package com.project.invitation.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Invitation extends TimeBaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "INVITATION_ID")
    private int id;

    private String inviteCode; //초대 링크

    @Enumerated(EnumType.STRING)
    private InviteCodeStatus inviteCodeStatus; // 초대 링크 상태

    private LocalDateTime expireDate; // 링크 만료 시간

    @OneToOne(mappedBy = "invitation", fetch = FetchType.LAZY)
    private Member member;

    @PrePersist
    private void persistExpireDate() {
        if (getCreatedDate() != null) {
            expireDate = getCreatedDate().plusMinutes(1); // 초대장 생성 후 1분
        }
    }

    @PreUpdate
    private void updateExpireDate() {
        if (getUpdatedDate() != null) {
            expireDate = getUpdatedDate().plusMinutes(1); // 초대장 업데이트 후 1분
        }
    }
    // 초대장 생성 메서드
    public static Invitation createInvitation() {
        Invitation invitation = new Invitation();
        invitation.setInviteCode(createInvitationCode());
        invitation.setInviteCodeStatus(InviteCodeStatus.ACTIVATE);
        return invitation;
    }
    // 초대장 수정 메서드
    public String updateInvitation() {
        String invitationCode = createInvitationCode();
        this.setInviteCodeStatus(InviteCodeStatus.ACTIVATE);
        this.setInviteCode(invitationCode);
        return invitationCode;
    }
    // 초대장 만료 메서드
    public void expireInviteCodeStatus() {
        this.inviteCodeStatus = InviteCodeStatus.EXPIRED;
    }

    // 초대 링크 생성
    private static String createInvitationCode() {
        UUID uuid = UUID.randomUUID();

        return uuid.toString();
    }
    // 초대장 만료 확인
    public boolean isInvitationExpired() {
        return this.inviteCodeStatus == InviteCodeStatus.EXPIRED ? true : false;
    }
    // 초대장 시간 만료 확인
    public boolean isInvitationTimeExpired() {
        LocalDateTime currentTime = LocalDateTime.now();
        return this.expireDate.isBefore(currentTime) ? true : false;
    }

}
