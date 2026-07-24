package org.interview.system_design.lld.filesystem;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * In-memory virtual file system supporting standard shell-like operations.
 * Uses the Composite tree as backing storage and Visitors for read operations.
 */
public class FileSystem {

    private final DirectoryNode root;
    private DirectoryNode currentDirectory;

    public FileSystem() {
        // Root directory has no parent path — represented as empty string
        this.root = new DirectoryNode("", "");
        this.currentDirectory = root;
    }

    // ---- Navigation ----

    /** Returns the absolute path of the current directory. */
    public String pwd() {
        String path = currentDirectory.getPath();
        return path.isEmpty() ? "/" : path;
    }

    /**
     * Changes the current directory.
     *
     * @param path absolute path starting with "/", or ".." to go up one level
     */
    public boolean cd(String path) {
        if ("..".equals(path)) {
            // Navigate to parent — find the parent of currentDirectory
            DirectoryNode parent = findParent(root, currentDirectory);
            if (parent != null) currentDirectory = parent;
            return true;
        }
        Optional<DirectoryNode> target = resolveDirectory(path);
        target.ifPresent(d -> currentDirectory = d);
        return target.isPresent();
    }

    // ---- Mutation ----

    /**
     * Creates a directory at the given path under the current directory.
     */
    public boolean mkdir(String name) {
        if (name.contains("/")) {
            System.out.println("mkdir: nested paths not supported; use absolute path");
            return false;
        }
        if (currentDirectory.getChild(name).isPresent()) {
            System.out.println("mkdir: " + name + " already exists");
            return false;
        }
        DirectoryNode dir = new DirectoryNode(name, currentDirectory.getPath());
        currentDirectory.addChild(dir);
        return true;
    }

    /**
     * Creates an empty file in the current directory.
     */
    public boolean touch(String name) {
        if (currentDirectory.getChild(name).isPresent()) {
            // Idempotent: no error if already exists
            return true;
        }
        FileNode file = new FileNode(name, currentDirectory.getPath());
        currentDirectory.addChild(file);
        return true;
    }

    /**
     * Creates a file with content in the current directory.
     */
    public boolean write(String name, String content) {
        Optional<FileSystemNode> existing = currentDirectory.getChild(name);
        if (existing.isPresent()) {
            if (existing.get() instanceof FileNode) {
                ((FileNode) existing.get()).setContent(content);
                return true;
            }
            System.out.println("write: " + name + " is a directory");
            return false;
        }
        FileNode file = new FileNode(name, currentDirectory.getPath(), content);
        currentDirectory.addChild(file);
        return true;
    }

    /**
     * Removes a file or empty directory from the current directory.
     */
    public boolean rm(String name) {
        return currentDirectory.removeChild(name);
    }

    // ---- Listing ----

    /** Lists names of all children in the current directory. */
    public List<String> ls() {
        return currentDirectory.getChildren().stream()
                .map(FileSystemNode::getName)
                .collect(Collectors.toList());
    }

    // ---- Search ----

    /**
     * Searches recursively from the current directory for files matching the pattern.
     */
    public List<FileNode> find(String pattern) {
        SearchVisitor visitor = new SearchVisitor(pattern);
        currentDirectory.accept(visitor);
        return visitor.getResults();
    }

    // ---- Disk usage ----

    /**
     * Returns total disk usage in bytes for the current directory tree.
     */
    public long du() {
        DiskUsageVisitor visitor = new DiskUsageVisitor();
        currentDirectory.accept(visitor);
        return currentDirectory.getSize();
    }

    // ---- Private helpers ----

    private Optional<DirectoryNode> resolveDirectory(String path) {
        // Support absolute paths only (starting with "/")
        DirectoryNode start = path.startsWith("/") ? root : currentDirectory;
        String[] parts = path.replaceFirst("^/", "").split("/");
        DirectoryNode current = start;
        for (String part : parts) {
            if (part.isEmpty()) continue;
            Optional<FileSystemNode> child = current.getChild(part);
            if (child.isEmpty() || !(child.get() instanceof DirectoryNode)) {
                return Optional.empty();
            }
            current = (DirectoryNode) child.get();
        }
        return Optional.of(current);
    }

    private DirectoryNode findParent(DirectoryNode node, DirectoryNode target) {
        for (FileSystemNode child : node.getChildren()) {
            if (child == target) return node;
            if (child instanceof DirectoryNode) {
                DirectoryNode found = findParent((DirectoryNode) child, target);
                if (found != null) return found;
            }
        }
        return null;
    }

    public DirectoryNode getRoot() { return root; }
    public DirectoryNode getCurrentDirectory() { return currentDirectory; }
}
