package org.interview.design_patterns.behavioral.iterator;

import java.util.NoSuchElementException;

/**
 * ============================================================
 * Design Pattern: Iterator (Behavioral)
 * ============================================================
 *
 * INTENT:
 *   Provide a way to sequentially access elements of a collection
 *   WITHOUT exposing its underlying representation (list, tree, graph).
 *
 * PROBLEM IT SOLVES:
 *   - Collections have different internal structures (array, linked list, tree).
 *   - Traversal logic scattered across the codebase.
 *   - Client code should not depend on how a collection is organized internally.
 *
 * STRUCTURE:
 *   - Iterator (interface): hasNext(), next(), optional remove()
 *   - ConcreteIterator: tracks traversal position in a specific collection
 *   - Iterable (interface): creates and returns an Iterator
 *   - ConcreteCollection: implements Iterable; provides its specific Iterator
 *   - Client: uses only Iterator interface; unaware of collection internals
 *
 * REAL-WORLD EXAMPLES:
 *   - java.util.Iterator (the inspiration for this pattern)
 *   - for-each loop in Java (syntactic sugar over Iterable)
 *   - Database cursor/ResultSet
 *   - File system directory walker
 *
 * PROS:
 *   + Single Responsibility: traversal separated from collection
 *   + Multiple iterators on the same collection (independent state)
 *   + Uniform interface across different collection types
 *
 * CONS:
 *   - Overkill for simple collections
 *   - Some iterators can't support backward traversal
 *
 * SCENARIO:
 *   A custom binary search tree that supports in-order iteration
 *   using the Iterator pattern. Client traverses without knowing
 *   the tree is internally stored as linked nodes.
 */
public class IteratorPattern {

    // ================================================================
    // Iterator Interface
    // ================================================================
    interface Iterator<T> {
        boolean hasNext();
        T next();
    }

    // ================================================================
    // Iterable Interface
    // ================================================================
    interface Iterable<T> {
        Iterator<T> iterator();
    }

    // ================================================================
    // Example 1: Custom Array-backed Collection with Range Iterator
    // ================================================================
    static class NumberRange implements Iterable<Integer> {
        private final int[] data;

        NumberRange(int[] data) {
            this.data = data.clone(); // defensive copy
        }

        @Override
        public Iterator<Integer> iterator() {
            return new ArrayIterator();
        }

        /** Forward iterator — left to right */
        private class ArrayIterator implements Iterator<Integer> {
            private int index = 0;

            @Override
            public boolean hasNext() { return index < data.length; }

            @Override
            public Integer next() {
                if (!hasNext()) throw new NoSuchElementException();
                return data[index++];
            }
        }

        /** Reverse iterator — right to left (bonus: multiple iterator types) */
        public Iterator<Integer> reverseIterator() {
            return new Iterator<>() {
                private int index = data.length - 1;

                @Override public boolean hasNext() { return index >= 0; }
                @Override public Integer next() {
                    if (!hasNext()) throw new NoSuchElementException();
                    return data[index--];
                }
            };
        }
    }

    // ================================================================
    // Example 2: Binary Search Tree with In-Order Iterator
    // The tree is stored as linked nodes; iterator hides this complexity.
    // In-order traversal of BST yields sorted sequence.
    // ================================================================
    static class BinarySearchTree implements Iterable<Integer> {
        // Internal node class — hidden from clients
        private static class Node {
            int   value;
            Node  left, right;
            Node(int v) { value = v; }
        }

        private Node root;

        /** Insert value into BST */
        public void insert(int value) {
            root = insertRec(root, value);
        }

        private Node insertRec(Node node, int value) {
            if (node == null) return new Node(value);
            if (value < node.value) node.left  = insertRec(node.left,  value);
            if (value > node.value) node.right = insertRec(node.right, value);
            return node;
        }

        /**
         * Returns an in-order iterator.
         * Client gets sorted integers without knowing about Node structure.
         * Implementation uses a stack to simulate the call stack of recursive traversal.
         */
        @Override
        public Iterator<Integer> iterator() {
            return new InOrderIterator(root);
        }

        private static class InOrderIterator implements Iterator<Integer> {
            // Stack simulates the recursive call stack
            private final java.util.Deque<Node> stack = new java.util.ArrayDeque<>();

            InOrderIterator(Node root) {
                // Push all leftmost nodes first
                pushLeft(root);
            }

            /** Push node and all its left children onto the stack */
            private void pushLeft(Node node) {
                while (node != null) {
                    stack.push(node);
                    node = node.left;
                }
            }

            @Override
            public boolean hasNext() {
                return !stack.isEmpty();
            }

            @Override
            public Integer next() {
                if (!hasNext()) throw new NoSuchElementException();
                Node node = stack.pop();    // pop the next in-order node
                pushLeft(node.right);       // prepare right subtree for future iteration
                return node.value;
            }
        }
    }

    // ================================================================
    // Example 3: Social Network Friend Graph Iterator
    // Breadth-First traversal; clients don't know it's a BFS
    // ================================================================
    static class SocialProfile implements Iterable<String> {
        private final String name;
        private final java.util.List<SocialProfile> friends = new java.util.ArrayList<>();

        SocialProfile(String name) { this.name = name; }

        public void addFriend(SocialProfile profile) { friends.add(profile); }
        public String getName() { return name; }

        /** BFS iterator over the social network (friends, friends-of-friends, ...) */
        @Override
        public Iterator<String> iterator() {
            return new BfsIterator(this);
        }

        private static class BfsIterator implements Iterator<String> {
            private final java.util.Deque<SocialProfile> queue = new java.util.ArrayDeque<>();
            private final java.util.Set<String>          seen  = new java.util.HashSet<>();

            BfsIterator(SocialProfile start) {
                enqueue(start);
            }

            private void enqueue(SocialProfile p) {
                if (seen.add(p.getName())) {
                    queue.add(p);
                }
            }

            @Override
            public boolean hasNext() { return !queue.isEmpty(); }

            @Override
            public String next() {
                if (!hasNext()) throw new NoSuchElementException();
                SocialProfile current = queue.poll();
                // Enqueue all unvisited friends
                current.friends.forEach(this::enqueue);
                return current.getName();
            }
        }
    }

    // ================================================================
    // Demo
    // ================================================================
    public static void main(String[] args) {
        System.out.println("=== Array Range Iterator ===");
        NumberRange numbers = new NumberRange(new int[]{10, 20, 30, 40, 50});

        System.out.print("Forward:  ");
        Iterator<Integer> fwd = numbers.iterator();
        while (fwd.hasNext()) System.out.print(fwd.next() + " ");
        System.out.println();

        System.out.print("Reverse:  ");
        Iterator<Integer> rev = numbers.reverseIterator();
        while (rev.hasNext()) System.out.print(rev.next() + " ");
        System.out.println();

        System.out.println("\n=== BST In-Order Iterator (produces sorted output) ===");
        BinarySearchTree bst = new BinarySearchTree();
        int[] insertOrder = {5, 3, 7, 1, 4, 6, 9};
        System.out.print("Insert order: ");
        for (int v : insertOrder) { bst.insert(v); System.out.print(v + " "); }
        System.out.println();

        System.out.print("In-order:     ");
        Iterator<Integer> bstIter = bst.iterator();
        while (bstIter.hasNext()) System.out.print(bstIter.next() + " ");
        System.out.println();

        System.out.println("\n=== Social Network BFS Iterator ===");
        SocialProfile alice = new SocialProfile("Alice");
        SocialProfile bob   = new SocialProfile("Bob");
        SocialProfile carol = new SocialProfile("Carol");
        SocialProfile dave  = new SocialProfile("Dave");
        SocialProfile eve   = new SocialProfile("Eve");

        alice.addFriend(bob);
        alice.addFriend(carol);
        bob.addFriend(dave);
        carol.addFriend(eve);
        dave.addFriend(alice);  // creates a cycle — BFS handles it with 'seen' set

        System.out.print("BFS from Alice: ");
        Iterator<String> social = alice.iterator();
        while (social.hasNext()) System.out.print(social.next() + " ");
        System.out.println();
    }
}
