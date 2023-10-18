package com.project.invitation.repository;

import com.project.invitation.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long> {

    Optional<Member> findByInvitationInviteCode(String inviteCode);

    Optional<Member> findByMemberId(String memberId);

}
