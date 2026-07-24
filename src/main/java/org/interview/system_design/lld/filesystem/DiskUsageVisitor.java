package org.interview.system_design.lld.filesystem;

import java.util.HashMap;
import java.util.Map;

/**
 * Visitor that computes total disk usage per directory.
 * After traversal, call getUsageMap() to retrieve directory → bytes mappings.
 */
public class DiskUsageVisitor implements FileSystemVisitor {

    private final Map<String, Long> usageMap = new HashMap<>();
    private long totalBytes = 0;

    @Override
    public void visitFile(FileNode file) {
        totalBytes += file.getSize();
    }

    @Override
    public void visitDirectory(DirectoryNode directory) {
        // Record each directory's total size (recursive, since getSize() is recursive)
        usageMap.put(directory.getPath(), directory.getSize());
    }

    /** Returns disk usage per directory path in bytes. */
    public Map<String, Long> getUsageMap() {
        return Map.copyOf(usageMap);
    }

    /** Returns the total bytes across all visited files. */
    public long getTotalBytes() {
        return totalBytes;
    }
}
