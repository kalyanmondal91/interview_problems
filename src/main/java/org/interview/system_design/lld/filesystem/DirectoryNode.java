package org.interview.system_design.lld.filesystem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Composite node representing a directory that can contain files and sub-directories.
 * Pattern: Composite (composite)
 */
public class DirectoryNode implements FileSystemNode {

    private final String name;
    private final String parentPath;
    private final List<FileSystemNode> children = new ArrayList<>();

    public DirectoryNode(String name, String parentPath) {
        this.name = name;
        this.parentPath = parentPath;
    }

    @Override
    public String getName() { return name; }

    @Override
    public long getSize() {
        return children.stream().mapToLong(FileSystemNode::getSize).sum();
    }

    @Override
    public String getPath() {
        if (parentPath == null || parentPath.isEmpty()) return "/" + name;
        return parentPath.endsWith("/") ? parentPath + name : parentPath + "/" + name;
    }

    @Override
    public void accept(FileSystemVisitor visitor) {
        visitor.visitDirectory(this);
        // Visitor decides whether to recurse into children
        children.forEach(child -> child.accept(visitor));
    }

    public void addChild(FileSystemNode node) {
        children.add(node);
    }

    public boolean removeChild(String name) {
        return children.removeIf(n -> n.getName().equals(name));
    }

    public Optional<FileSystemNode> getChild(String name) {
        return children.stream().filter(n -> n.getName().equals(name)).findFirst();
    }

    public List<FileSystemNode> getChildren() {
        return Collections.unmodifiableList(children);
    }

    @Override
    public String toString() {
        return String.format("Dir{%s, children=%d}", getPath(), children.size());
    }
}
