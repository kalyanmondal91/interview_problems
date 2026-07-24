---
layout: lld
render_with_liquid: false
title: "File System"
system: filesystem
description: "LLD of File System"
files:
  - "DirectoryNode.java"
  - "DiskUsageVisitor.java"
  - "FileNode.java"
  - "FilePermission.java"
  - "FileSystem.java"
  - "FileSystemNode.java"
  - "FileSystemVisitor.java"
  - "Main.java"
  - "Permissions.java"
  - "SearchVisitor.java"
  - "SizePrinterVisitor.java"
---

## File System

Complete Java LLD implementation.

## Source Files

<div class="lld-tabs">
<div class="tab-buttons">
<button class="tab-btn active" data-tab="DirectoryNode.java">DirectoryNode.java</button>
<button class="tab-btn" data-tab="DiskUsageVisitor.java">DiskUsageVisitor.java</button>
<button class="tab-btn" data-tab="FileNode.java">FileNode.java</button>
<button class="tab-btn" data-tab="FilePermission.java">FilePermission.java</button>
<button class="tab-btn" data-tab="FileSystem.java">FileSystem.java</button>
<button class="tab-btn" data-tab="FileSystemNode.java">FileSystemNode.java</button>
<button class="tab-btn" data-tab="FileSystemVisitor.java">FileSystemVisitor.java</button>
<button class="tab-btn" data-tab="Main.java">Main.java</button>
<button class="tab-btn" data-tab="Permissions.java">Permissions.java</button>
<button class="tab-btn" data-tab="SearchVisitor.java">SearchVisitor.java</button>
<button class="tab-btn" data-tab="SizePrinterVisitor.java">SizePrinterVisitor.java</button>
</div>
<div class="tab-content active" id="DirectoryNode-java">
<pre><code class="language-java">package org.interview.system_design.lld.filesystem;

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
    private final List&lt;FileSystemNode&gt; children = new ArrayList&lt;&gt;();

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
        if (parentPath == null || parentPath.isEmpty()) return &quot;/&quot; + name;
        return parentPath.endsWith(&quot;/&quot;) ? parentPath + name : parentPath + &quot;/&quot; + name;
    }

    @Override
    public void accept(FileSystemVisitor visitor) {
        visitor.visitDirectory(this);
        // Visitor decides whether to recurse into children
        children.forEach(child -&gt; child.accept(visitor));
    }

    public void addChild(FileSystemNode node) {
        children.add(node);
    }

    public boolean removeChild(String name) {
        return children.removeIf(n -&gt; n.getName().equals(name));
    }

    public Optional&lt;FileSystemNode&gt; getChild(String name) {
        return children.stream().filter(n -&gt; n.getName().equals(name)).findFirst();
    }

    public List&lt;FileSystemNode&gt; getChildren() {
        return Collections.unmodifiableList(children);
    }

    @Override
    public String toString() {
        return String.format(&quot;Dir{%s, children=%d}&quot;, getPath(), children.size());
    }
}</code></pre>
</div>
<div class="tab-content" id="DiskUsageVisitor-java">
<pre><code class="language-java">package org.interview.system_design.lld.filesystem;

import java.util.HashMap;
import java.util.Map;

/**
 * Visitor that computes total disk usage per directory.
 * After traversal, call getUsageMap() to retrieve directory → bytes mappings.
 */
public class DiskUsageVisitor implements FileSystemVisitor {

    private final Map&lt;String, Long&gt; usageMap = new HashMap&lt;&gt;();
    private long totalBytes = 0;

    @Override
    public void visitFile(FileNode file) {
        totalBytes += file.getSize();
    }

    @Override
    public void visitDirectory(DirectoryNode directory) {
        // Record each directory&#x27;s total size (recursive, since getSize() is recursive)
        usageMap.put(directory.getPath(), directory.getSize());
    }

    /** Returns disk usage per directory path in bytes. */
    public Map&lt;String, Long&gt; getUsageMap() {
        return Map.copyOf(usageMap);
    }

    /** Returns the total bytes across all visited files. */
    public long getTotalBytes() {
        return totalBytes;
    }
}</code></pre>
</div>
<div class="tab-content" id="FileNode-java">
<pre><code class="language-java">package org.interview.system_design.lld.filesystem;

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
        this(name, parentPath, &quot;&quot;);
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
        return parentPath.endsWith(&quot;/&quot;) ? parentPath + name : parentPath + &quot;/&quot; + name;
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
        return String.format(&quot;File{%s, size=%d bytes}&quot;, getPath(), getSize());
    }
}</code></pre>
</div>
<div class="tab-content" id="FilePermission-java">
<pre><code class="language-java">package org.interview.system_design.lld.filesystem;

/** Standard UNIX-style file permission bits. */
public enum FilePermission {
    READ,
    WRITE,
    EXECUTE
}</code></pre>
</div>
<div class="tab-content" id="FileSystem-java">
<pre><code class="language-java">package org.interview.system_design.lld.filesystem;

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
        this.root = new DirectoryNode(&quot;&quot;, &quot;&quot;);
        this.currentDirectory = root;
    }

    // ---- Navigation ----

    /** Returns the absolute path of the current directory. */
    public String pwd() {
        String path = currentDirectory.getPath();
        return path.isEmpty() ? &quot;/&quot; : path;
    }

    /**
     * Changes the current directory.
     *
     * @param path absolute path starting with &quot;/&quot;, or &quot;..&quot; to go up one level
     */
    public boolean cd(String path) {
        if (&quot;..&quot;.equals(path)) {
            // Navigate to parent — find the parent of currentDirectory
            DirectoryNode parent = findParent(root, currentDirectory);
            if (parent != null) currentDirectory = parent;
            return true;
        }
        Optional&lt;DirectoryNode&gt; target = resolveDirectory(path);
        target.ifPresent(d -&gt; currentDirectory = d);
        return target.isPresent();
    }

    // ---- Mutation ----

    /**
     * Creates a directory at the given path under the current directory.
     */
    public boolean mkdir(String name) {
        if (name.contains(&quot;/&quot;)) {
            System.out.println(&quot;mkdir: nested paths not supported; use absolute path&quot;);
            return false;
        }
        if (currentDirectory.getChild(name).isPresent()) {
            System.out.println(&quot;mkdir: &quot; + name + &quot; already exists&quot;);
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
        Optional&lt;FileSystemNode&gt; existing = currentDirectory.getChild(name);
        if (existing.isPresent()) {
            if (existing.get() instanceof FileNode) {
                ((FileNode) existing.get()).setContent(content);
                return true;
            }
            System.out.println(&quot;write: &quot; + name + &quot; is a directory&quot;);
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
    public List&lt;String&gt; ls() {
        return currentDirectory.getChildren().stream()
                .map(FileSystemNode::getName)
                .collect(Collectors.toList());
    }

    // ---- Search ----

    /**
     * Searches recursively from the current directory for files matching the pattern.
     */
    public List&lt;FileNode&gt; find(String pattern) {
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

    private Optional&lt;DirectoryNode&gt; resolveDirectory(String path) {
        // Support absolute paths only (starting with &quot;/&quot;)
        DirectoryNode start = path.startsWith(&quot;/&quot;) ? root : currentDirectory;
        String[] parts = path.replaceFirst(&quot;^/&quot;, &quot;&quot;).split(&quot;/&quot;);
        DirectoryNode current = start;
        for (String part : parts) {
            if (part.isEmpty()) continue;
            Optional&lt;FileSystemNode&gt; child = current.getChild(part);
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
}</code></pre>
</div>
<div class="tab-content" id="FileSystemNode-java">
<pre><code class="language-java">package org.interview.system_design.lld.filesystem;

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
}</code></pre>
</div>
<div class="tab-content" id="FileSystemVisitor-java">
<pre><code class="language-java">package org.interview.system_design.lld.filesystem;

/**
 * Visitor interface for traversing the file system tree.
 * Pattern: Visitor
 */
public interface FileSystemVisitor {
    void visitFile(FileNode file);
    void visitDirectory(DirectoryNode directory);
}</code></pre>
</div>
<div class="tab-content" id="Main-java">
<pre><code class="language-java">package org.interview.system_design.lld.filesystem;

import java.util.List;

/**
 * Demo runner for the File System LLD.
 * Patterns: Composite (directory tree), Visitor (size printer, search, disk usage)
 */
public class Main {

    private static void banner(String text) {
        System.out.println();
        System.out.println(&quot;=== &quot; + text + &quot; ===&quot;);
    }

    public static void main(String[] args) {

        // Scenario 1: Build tree and print with SizePrinterVisitor
        banner(&quot;Scenario 1: Build directory tree + SizePrinterVisitor&quot;);

        DirectoryNode root     = new DirectoryNode(&quot;&quot;, &quot;&quot;);
        DirectoryNode projects = new DirectoryNode(&quot;projects&quot;, root.getPath());
        DirectoryNode java     = new DirectoryNode(&quot;java&quot;, projects.getPath());
        DirectoryNode python   = new DirectoryNode(&quot;python&quot;, projects.getPath());
        DirectoryNode docs     = new DirectoryNode(&quot;docs&quot;, root.getPath());

        FileNode appJava  = new FileNode(&quot;App.java&quot;,  java.getPath(),    &quot;public class App {}&quot;);
        FileNode utilJava = new FileNode(&quot;Util.java&quot;, java.getPath(),    &quot;public class Util {}&quot;);
        FileNode scriptPy = new FileNode(&quot;script.py&quot;, python.getPath(),  &quot;print(&#x27;hello&#x27;)&quot;);
        FileNode readme   = new FileNode(&quot;README.md&quot;, docs.getPath(),    &quot;# Project&quot;);
        FileNode license  = new FileNode(&quot;LICENSE&quot;,   docs.getPath(),    &quot;MIT License...&quot;);

        java.addChild(appJava);
        java.addChild(utilJava);
        python.addChild(scriptPy);
        projects.addChild(java);
        projects.addChild(python);
        docs.addChild(readme);
        docs.addChild(license);
        root.addChild(projects);
        root.addChild(docs);

        SizePrinterVisitor printer = new SizePrinterVisitor();
        root.accept(printer);

        // Scenario 2: SearchVisitor -- find .java files
        banner(&quot;Scenario 2: SearchVisitor -- find files containing &#x27;.java&#x27;&quot;);
        SearchVisitor searcher = new SearchVisitor(&quot;.java&quot;);
        root.accept(searcher);
        List&lt;FileNode&gt; found = searcher.getResults();
        System.out.println(&quot;  Found &quot; + found.size() + &quot; match(es):&quot;);
        found.forEach(f -&gt; System.out.println(&quot;    &quot; + f.getPath()));

        // Scenario 3: DiskUsageVisitor
        banner(&quot;Scenario 3: DiskUsageVisitor -- per-directory sizes&quot;);
        DiskUsageVisitor du = new DiskUsageVisitor();
        root.accept(du);
        du.getUsageMap().forEach((dir, size) -&gt;
                System.out.printf(&quot;  %-20s %d bytes%n&quot;, dir, size));

        // Scenario 4: FileSystem shell-style operations
        banner(&quot;Scenario 4: FileSystem shell operations&quot;);
        FileSystem fs = new FileSystem();

        fs.mkdir(&quot;home&quot;);
        fs.cd(&quot;/home&quot;);
        fs.mkdir(&quot;user&quot;);
        fs.cd(&quot;/home/user&quot;);
        fs.mkdir(&quot;documents&quot;);
        fs.cd(&quot;/home/user/documents&quot;);
        fs.touch(&quot;notes.txt&quot;);
        fs.touch(&quot;report.pdf&quot;);
        fs.cd(&quot;/&quot;);
        fs.mkdir(&quot;var&quot;);
        fs.cd(&quot;/var&quot;);
        fs.mkdir(&quot;log&quot;);
        fs.cd(&quot;/var/log&quot;);
        fs.touch(&quot;system.log&quot;);
        fs.cd(&quot;/&quot;);

        System.out.println(&quot;  pwd: &quot; + fs.pwd());
        System.out.println(&quot;  ls /: &quot; + fs.ls());
        fs.cd(&quot;/home/user&quot;);
        System.out.println(&quot;  cd /home/user -&gt; pwd: &quot; + fs.pwd());
        System.out.println(&quot;  ls: &quot; + fs.ls());
        fs.cd(&quot;/home/user/documents&quot;);
        System.out.println(&quot;  ls documents: &quot; + fs.ls());

        // Scenario 5: rm and find
        banner(&quot;Scenario 5: rm a file, find across tree&quot;);
        fs.cd(&quot;/home/user/documents&quot;);
        System.out.println(&quot;  Before rm: ls documents = &quot; + fs.ls());
        fs.rm(&quot;notes.txt&quot;);
        System.out.println(&quot;  After  rm: ls documents = &quot; + fs.ls());

        fs.cd(&quot;/&quot;);
        List&lt;FileNode&gt; logFiles = fs.find(&quot;system.log&quot;);
        System.out.println(&quot;  find &#x27;system.log&#x27;: &quot; + logFiles.size() + &quot; result(s)&quot;);
        logFiles.forEach(f -&gt; System.out.println(&quot;    &quot; + f.getPath()));

        System.out.println();
        System.out.println(&quot;=== File System Demo Complete ===&quot;);
    }
}</code></pre>
</div>
<div class="tab-content" id="Permissions-java">
<pre><code class="language-java">package org.interview.system_design.lld.filesystem;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

/**
 * Holds permission sets for owner, group, and others.
 */
public class Permissions {

    private final Set&lt;FilePermission&gt; owner;
    private final Set&lt;FilePermission&gt; group;
    private final Set&lt;FilePermission&gt; others;

    public Permissions(Set&lt;FilePermission&gt; owner,
                       Set&lt;FilePermission&gt; group,
                       Set&lt;FilePermission&gt; others) {
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

    public Set&lt;FilePermission&gt; getOwner() { return Collections.unmodifiableSet(owner); }
    public Set&lt;FilePermission&gt; getGroup() { return Collections.unmodifiableSet(group); }
    public Set&lt;FilePermission&gt; getOthers() { return Collections.unmodifiableSet(others); }

    @Override
    public String toString() {
        return String.format(&quot;owner=%s, group=%s, others=%s&quot;, owner, group, others);
    }
}</code></pre>
</div>
<div class="tab-content" id="SearchVisitor-java">
<pre><code class="language-java">package org.interview.system_design.lld.filesystem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Visitor that searches for files whose name contains the given pattern.
 */
public class SearchVisitor implements FileSystemVisitor {

    private final String pattern;
    private final List&lt;FileNode&gt; results = new ArrayList&lt;&gt;();

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
    public List&lt;FileNode&gt; getResults() {
        return Collections.unmodifiableList(results);
    }
}</code></pre>
</div>
<div class="tab-content" id="SizePrinterVisitor-java">
<pre><code class="language-java">package org.interview.system_design.lld.filesystem;

/**
 * Visitor that prints the name and size of every node it visits.
 */
public class SizePrinterVisitor implements FileSystemVisitor {

    private int depth = 0;

    @Override
    public void visitFile(FileNode file) {
        System.out.printf(&quot;%s[FILE] %s (%d bytes)%n&quot;,
                indent(), file.getName(), file.getSize());
    }

    @Override
    public void visitDirectory(DirectoryNode directory) {
        System.out.printf(&quot;%s[DIR]  %s (%d bytes total)%n&quot;,
                indent(), directory.getName(), directory.getSize());
        depth++;
        // Children are visited by DirectoryNode.accept → children.forEach
        // We adjust depth back after visitDirectory returns to its caller,
        // but since accept calls children recursively, we must manage depth here.
        // Reset depth after this directory&#x27;s children are processed by the caller.
    }

    private String indent() {
        return &quot;  &quot;.repeat(depth);
    }

    /** Call this after visiting a directory&#x27;s children to restore indentation. */
    public void exitDirectory() {
        if (depth &gt; 0) depth--;
    }
}</code></pre>
</div>
</div>
