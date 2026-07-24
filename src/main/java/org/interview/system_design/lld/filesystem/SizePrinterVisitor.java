package org.interview.system_design.lld.filesystem;

/**
 * Visitor that prints the name and size of every node it visits.
 */
public class SizePrinterVisitor implements FileSystemVisitor {

    private int depth = 0;

    @Override
    public void visitFile(FileNode file) {
        System.out.printf("%s[FILE] %s (%d bytes)%n",
                indent(), file.getName(), file.getSize());
    }

    @Override
    public void visitDirectory(DirectoryNode directory) {
        System.out.printf("%s[DIR]  %s (%d bytes total)%n",
                indent(), directory.getName(), directory.getSize());
        depth++;
        // Children are visited by DirectoryNode.accept → children.forEach
        // We adjust depth back after visitDirectory returns to its caller,
        // but since accept calls children recursively, we must manage depth here.
        // Reset depth after this directory's children are processed by the caller.
    }

    private String indent() {
        return "  ".repeat(depth);
    }

    /** Call this after visiting a directory's children to restore indentation. */
    public void exitDirectory() {
        if (depth > 0) depth--;
    }
}
