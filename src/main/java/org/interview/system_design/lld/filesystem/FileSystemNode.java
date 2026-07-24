package org.interview.system_design.lld.filesystem;

/**
 * Component interface for the Composite pattern.
 * Both files (leaves) and directories (composites) implement this.
 * Pattern: Composite
 */
public interface FileSystemNode {
    /** Returns the file or directory name. */
    String getName();

    /** Returns the size in bytes. Directories return the sum of their children. */
    long getSize();

    /** Returns the absolute path from the root. */
    String getPath();

    /** Accepts a visitor, delegating to the appropriate visit method. */
    void accept(FileSystemVisitor visitor);
}
