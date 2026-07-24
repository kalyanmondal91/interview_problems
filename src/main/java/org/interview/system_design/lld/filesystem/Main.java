package org.interview.system_design.lld.filesystem;

import java.util.List;

/**
 * Demo runner for the File System LLD.
 * Patterns: Composite (directory tree), Visitor (size printer, search, disk usage)
 */
public class Main {

    private static void banner(String text) {
        System.out.println();
        System.out.println("=== " + text + " ===");
    }

    public static void main(String[] args) {

        // Scenario 1: Build tree and print with SizePrinterVisitor
        banner("Scenario 1: Build directory tree + SizePrinterVisitor");

        DirectoryNode root     = new DirectoryNode("", "");
        DirectoryNode projects = new DirectoryNode("projects", root.getPath());
        DirectoryNode java     = new DirectoryNode("java", projects.getPath());
        DirectoryNode python   = new DirectoryNode("python", projects.getPath());
        DirectoryNode docs     = new DirectoryNode("docs", root.getPath());

        FileNode appJava  = new FileNode("App.java",  java.getPath(),    "public class App {}");
        FileNode utilJava = new FileNode("Util.java", java.getPath(),    "public class Util {}");
        FileNode scriptPy = new FileNode("script.py", python.getPath(),  "print('hello')");
        FileNode readme   = new FileNode("README.md", docs.getPath(),    "# Project");
        FileNode license  = new FileNode("LICENSE",   docs.getPath(),    "MIT License...");

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
        banner("Scenario 2: SearchVisitor -- find files containing '.java'");
        SearchVisitor searcher = new SearchVisitor(".java");
        root.accept(searcher);
        List<FileNode> found = searcher.getResults();
        System.out.println("  Found " + found.size() + " match(es):");
        found.forEach(f -> System.out.println("    " + f.getPath()));

        // Scenario 3: DiskUsageVisitor
        banner("Scenario 3: DiskUsageVisitor -- per-directory sizes");
        DiskUsageVisitor du = new DiskUsageVisitor();
        root.accept(du);
        du.getUsageMap().forEach((dir, size) ->
                System.out.printf("  %-20s %d bytes%n", dir, size));

        // Scenario 4: FileSystem shell-style operations
        banner("Scenario 4: FileSystem shell operations");
        FileSystem fs = new FileSystem();

        fs.mkdir("home");
        fs.cd("/home");
        fs.mkdir("user");
        fs.cd("/home/user");
        fs.mkdir("documents");
        fs.cd("/home/user/documents");
        fs.touch("notes.txt");
        fs.touch("report.pdf");
        fs.cd("/");
        fs.mkdir("var");
        fs.cd("/var");
        fs.mkdir("log");
        fs.cd("/var/log");
        fs.touch("system.log");
        fs.cd("/");

        System.out.println("  pwd: " + fs.pwd());
        System.out.println("  ls /: " + fs.ls());
        fs.cd("/home/user");
        System.out.println("  cd /home/user -> pwd: " + fs.pwd());
        System.out.println("  ls: " + fs.ls());
        fs.cd("/home/user/documents");
        System.out.println("  ls documents: " + fs.ls());

        // Scenario 5: rm and find
        banner("Scenario 5: rm a file, find across tree");
        fs.cd("/home/user/documents");
        System.out.println("  Before rm: ls documents = " + fs.ls());
        fs.rm("notes.txt");
        System.out.println("  After  rm: ls documents = " + fs.ls());

        fs.cd("/");
        List<FileNode> logFiles = fs.find("system.log");
        System.out.println("  find 'system.log': " + logFiles.size() + " result(s)");
        logFiles.forEach(f -> System.out.println("    " + f.getPath()));

        System.out.println();
        System.out.println("=== File System Demo Complete ===");
    }
}
