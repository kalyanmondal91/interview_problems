package org.interview.design_patterns.structural.composite;

import java.util.ArrayList;
import java.util.List;

/**
 * ============================================================
 * Design Pattern: Composite (Structural)
 * ============================================================
 *
 * INTENT:
 *   Compose objects into tree structures to represent part-whole
 *   hierarchies. Composite lets clients treat individual objects
 *   and compositions of objects uniformly.
 *
 * PROBLEM IT SOLVES:
 *   - You need to represent hierarchical structures (trees)
 *   - You want client code to work the same way with simple elements
 *     and complex containers (no if-else checks for type)
 *
 * STRUCTURE:
 *   - Component (interface/abstract): common interface for both Leaf and Composite
 *   - Leaf: basic element, no children
 *   - Composite: has children (Leaf or other Composites); delegates operations
 *   - Client: works only with Component interface
 *
 * REAL-WORLD EXAMPLES:
 *   - File system: File (leaf) + Directory (composite)
 *   - UI widget trees: Button (leaf) + Panel (composite)
 *   - HTML DOM: text nodes (leaf) + elements (composite)
 *   - Company org chart: Employee (leaf) + Department (composite)
 *
 * PROS:
 *   + Simplifies client code (treats individual and groups uniformly)
 *   + Easy to add new kinds of components
 *   + Tree structures map naturally
 *
 * CONS:
 *   - Can make design overly general; hard to restrict component types
 *   - Hard to add type-specific behavior without breaking generality
 *
 * SCENARIO:
 *   File system with Files (leaves) and Directories (composites).
 *   Both have a name and a size. Directory size = sum of children sizes.
 */
public class CompositePattern {

    // ================================================================
    // Component — common interface for Files and Directories
    // ================================================================
    interface FileSystemEntry {
        String getName();
        long getSize();   // bytes
        void print(String indent);
    }

    // ================================================================
    // Leaf — represents a single file (no children)
    // ================================================================
    static class File implements FileSystemEntry {
        private final String name;
        private final long   size;

        File(String name, long size) {
            this.name = name;
            this.size = size;
        }

        @Override public String getName() { return name; }

        @Override public long getSize()   { return size; }

        @Override
        public void print(String indent) {
            System.out.printf("%s📄 %s (%,d bytes)%n", indent, name, size);
        }
    }

    // ================================================================
    // Composite — represents a directory (holds children)
    // Implements Component so it can be treated just like a File
    // ================================================================
    static class Directory implements FileSystemEntry {
        private final String name;
        // Children can be Files OR Directories — both are FileSystemEntry
        private final List<FileSystemEntry> children = new ArrayList<>();

        Directory(String name) {
            this.name = name;
        }

        /** Add a file or subdirectory */
        public void add(FileSystemEntry entry) {
            children.add(entry);
        }

        /** Remove a child entry */
        public void remove(FileSystemEntry entry) {
            children.remove(entry);
        }

        @Override public String getName() { return name; }

        /**
         * Size of a directory = recursive sum of all children sizes.
         * Client calls this the same way as File.getSize() — uniformity!
         */
        @Override
        public long getSize() {
            return children.stream()
                           .mapToLong(FileSystemEntry::getSize)
                           .sum();
        }

        @Override
        public void print(String indent) {
            System.out.printf("%s📁 %s/ (%,d bytes total)%n", indent, name, getSize());
            // Recursively print children with increased indentation
            for (FileSystemEntry child : children) {
                child.print(indent + "  ");
            }
        }
    }

    // ================================================================
    // Demo
    // ================================================================
    public static void main(String[] args) {
        // Build a sample file system tree:
        //
        // root/
        //   bin/
        //     ls  (4096 B)
        //     grep (8192 B)
        //   home/
        //     alice/
        //       resume.pdf (512000 B)
        //       photo.jpg  (2048000 B)
        //     bob/
        //       notes.txt (1024 B)
        //   etc/
        //     hosts (256 B)
        //     passwd (1024 B)
        //   readme.md (2048 B)

        // Leaves
        File ls          = new File("ls",         4_096);
        File grep        = new File("grep",        8_192);
        File resume      = new File("resume.pdf",  512_000);
        File photo       = new File("photo.jpg",   2_048_000);
        File notes       = new File("notes.txt",   1_024);
        File hosts       = new File("hosts",       256);
        File passwd      = new File("passwd",      1_024);
        File readme      = new File("readme.md",   2_048);

        // Composites
        Directory bin    = new Directory("bin");
        bin.add(ls);
        bin.add(grep);

        Directory alice  = new Directory("alice");
        alice.add(resume);
        alice.add(photo);

        Directory bob    = new Directory("bob");
        bob.add(notes);

        Directory home   = new Directory("home");
        home.add(alice);
        home.add(bob);

        Directory etc    = new Directory("etc");
        etc.add(hosts);
        etc.add(passwd);

        Directory root   = new Directory("root");
        root.add(bin);
        root.add(home);
        root.add(etc);
        root.add(readme);

        // Client treats root directory and individual files identically
        System.out.println("=== File System Tree ===");
        root.print("");

        System.out.println("\n=== Size Queries (uniform API) ===");
        System.out.printf("Size of single file (readme.md): %,d bytes%n", readme.getSize());
        System.out.printf("Size of /home:                   %,d bytes%n", home.getSize());
        System.out.printf("Size of /root (total):           %,d bytes%n", root.getSize());

        // Demonstrate that client code is IDENTICAL for leaf and composite:
        FileSystemEntry[] entries = { readme, alice, root };
        System.out.println("\n=== Uniform treatment: entries = [readme, alice, root] ===");
        for (FileSystemEntry e : entries) {
            System.out.printf("  %-20s → %,d bytes%n", e.getName(), e.getSize());
        }
    }
}
