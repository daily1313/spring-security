package com.example.jwtauth.repository;

import com.example.jwtauth.domain.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByUsername(final String username);

    boolean existsByUsername(String username);
}
