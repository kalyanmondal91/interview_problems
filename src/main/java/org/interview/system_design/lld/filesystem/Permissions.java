package org.interview.system_design.lld.filesystem;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

/**
 * Holds permission sets for owner, group, and others.
 */
public class Permissions {

    private final Set<FilePermission> owner;
    private final Set<FilePermission> group;
    private final Set<FilePermission> others;

    public Permissions(Set<FilePermission> owner,
                       Set<FilePermission> group,
                       Set<FilePermission> others) {
        this.owner = EnumSet.copyOf(owner.isEmpty() ? EnumSet.noneOf(FilePermission.class) : owner);
        this.group = EnumSet.copyOf(group.isEmpty() ? EnumSet.noneOf(FilePermission.class) : group);
        this.others = EnumSet.copyOf(others.isEmpty() ? EnumSet.noneOf(FilePermission.class) : others);
    }

    /** Returns default permissions: owner=rwx, group=r-x, others=r-- */
    public static Permissions defaultPermissions() {
        return new Permissions(
                EnumSet.of(FilePermission.READ, FilePermission.WRITE, FilePermission.EXECUTE),
                EnumSet.of(FilePermission.READ, FilePermission.EXECUTE),
                EnumSet.of(FilePermission.READ)
        );
    }

    public Set<FilePermission> getOwner() { return Collections.unmodifiableSet(owner); }
    public Set<FilePermission> getGroup() { return Collections.unmodifiableSet(group); }
    public Set<FilePermission> getOthers() { return Collections.unmodifiableSet(others); }

    @Override
    public String toString() {
        return String.format("owner=%s, group=%s, others=%s", owner, group, others);
    }
}
