package org.interview.system_design.lld.library;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Member entities.
 */
public interface MemberRepository {
    void save(Member member);
    Optional<Member> findById(String memberId);
    List<Member> findByName(String name);
    List<Member> findAll();
    void delete(String memberId);
}
