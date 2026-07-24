package org.interview.system_design.lld.filesystem;

/**
 * Visitor interface for traversing the file system tree.
 * Pattern: Visitor
 */
public interface FileSystemVisitor {
    void visitFile(FileNode file);
    void visitDirectory(DirectoryNode directory);
}
