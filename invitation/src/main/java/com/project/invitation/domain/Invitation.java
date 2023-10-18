package com.project.invitation.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Invitation extends TimeBaseEntity {

    private String inviteCode; //초대 링크

    @Enumerated(EnumType.STRING)
    private InviteCodeStatus inviteCodeStatus; // 초대 링크 상태

    private LocalDateTime expireDate; // 링크 만료 시간 (생성후 1분동안 유효)

    @PrePersist
    private void persistExpireDate() {
        if (getCreatedDate() != null) {
            expireDate = getCreatedDate().plusMinutes(1);
        }
    }

    @PreUpdate
    private void updateExpireDate() {
        if (getUpdatedDate() != null) {
            expireDate = getUpdatedDate().plusMinutes(1);
        }
    }

    public static Invitation createInvitation( String inviteCode) {
        Invitation invitation = new Invitation();
        invitation.setInviteCode(inviteCode);
        invitation.setInviteCodeStatus(InviteCodeStatus.ACTIVATE);
        return invitation;
    }

}
