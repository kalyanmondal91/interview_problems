package org.interview.system_design.lld.library;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * HashMap-backed in-memory implementation of MemberRepository.
 */
public class InMemoryMemberRepository implements MemberRepository {
    private final Map<String, Member> membersById = new HashMap<>();

    @Override
    public void save(Member member) {
        membersById.put(member.getMemberId(), member);
    }

    @Override
    public Optional<Member> findById(String memberId) {
        return Optional.ofNullable(membersById.get(memberId));
    }

    @Override
    public List<Member> findByName(String name) {
        String lower = name.toLowerCase();
        return membersById.values().stream()
                .filter(m -> m.getName().toLowerCase().contains(lower))
                .collect(Collectors.toList());
    }

    @Override
    public List<Member> findAll() {
        return new ArrayList<>(membersById.values());
    }

    @Override
    public void delete(String memberId) {
        membersById.remove(memberId);
    }
}
