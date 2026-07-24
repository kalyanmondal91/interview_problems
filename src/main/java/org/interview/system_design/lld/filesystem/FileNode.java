package org.interview.system_design.lld.filesystem;

import java.time.LocalDateTime;

/**
 * Leaf node representing a file in the file system.
 * Pattern: Composite (leaf)
 */
public class FileNode implements FileSystemNode {

    private final String name;
    private String content;
    private final String parentPath;
    private final LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private Permissions permissions;

    public FileNode(String name, String parentPath) {
        this(name, parentPath, "");
    }

    public FileNode(String name, String parentPath, String content) {
        this.name = name;
        this.parentPath = parentPath;
        this.content = content;
        this.createdAt = LocalDateTime.now();
        this.modifiedAt = this.createdAt;
        this.permissions = Permissions.defaultPermissions();
    }

    @Override
    public String getName() { return name; }

    @Override
    public long getSize() {
        return content == null ? 0 : content.getBytes().length;
    }

    @Override
    public String getPath() {
        return parentPath.endsWith("/") ? parentPath + name : parentPath + "/" + name;
    }

    @Override
    public void accept(FileSystemVisitor visitor) {
        visitor.visitFile(this);
    }

    public String getContent() { return content; }

    public void setContent(String content) {
        this.content = content;
        this.modifiedAt = LocalDateTime.now();
    }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getModifiedAt() { return modifiedAt; }
    public Permissions getPermissions() { return permissions; }
    public void setPermissions(Permissions permissions) { this.permissions = permissions; }

    @Override
    public String toString() {
        return String.format("File{%s, size=%d bytes}", getPath(), getSize());
    }
}
