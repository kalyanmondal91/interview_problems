package org.interview.system_design.lld.filesystem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Visitor that searches for files whose name contains the given pattern.
 */
public class SearchVisitor implements FileSystemVisitor {

    private final String pattern;
    private final List<FileNode> results = new ArrayList<>();

    public SearchVisitor(String pattern) {
        this.pattern = pattern.toLowerCase();
    }

    @Override
    public void visitFile(FileNode file) {
        if (file.getName().toLowerCase().contains(pattern)) {
            results.add(file);
        }
    }

    @Override
    public void visitDirectory(DirectoryNode directory) {
        // Directories themselves are not searched; we continue traversal via children
    }

    /** Returns all matching files found during traversal. */
    public List<FileNode> getResults() {
        return Collections.unmodifiableList(results);
    }
}
