# 💻 Coding Problems — 150 Questions

> **15 categories × 10 questions** covering every topic tested in FAANG and top-tier
> engineering interviews. Each problem is tagged by difficulty and links to its
> Java implementation in the corresponding sub-package.

**Difficulty:** 🟢 Easy &nbsp; 🟡 Medium &nbsp; 🔴 Hard

---

## 📋 Category Index

| # | Category | Key Data Structures / Algorithms |
|---|----------|----------------------------------|
| 1 | [Arrays & Strings](#1-arrays--strings) | Two-pointer, sliding window, prefix sums |
| 2 | [Hashing & Sets](#2-hashing--sets) | HashMap, HashSet, frequency maps |
| 3 | [Two Pointers & Sliding Window](#3-two-pointers--sliding-window) | Left/right pointer, variable-width window |
| 4 | [Linked Lists](#4-linked-lists) | Dummy node, fast/slow pointer, in-place reversal |
| 5 | [Stacks & Queues](#5-stacks--queues) | Monotonic stack, deque, BFS |
| 6 | [Trees & BSTs](#6-trees--bsts) | DFS, BFS, in/pre/post-order, BST properties |
| 7 | [Graphs](#7-graphs) | BFS, DFS, Union-Find, Dijkstra, topological sort |
| 8 | [Dynamic Programming](#8-dynamic-programming) | Memoisation, tabulation, state design |
| 9 | [Greedy](#9-greedy) | Local optimal → global optimal, interval scheduling |
| 10 | [Backtracking](#10-backtracking) | State-space tree, pruning, permutations/subsets |
| 11 | [Heaps / Priority Queues](#11-heaps--priority-queues) | Min-heap, max-heap, K-way merge |
| 12 | [Binary Search](#12-binary-search) | Search space reduction, binary search on answer |
| 13 | [Design & OOD](#13-design--ood--data-structures) | Custom data structures, class design |
| 14 | [Concurrency](#14-concurrency--system-style-coding) | Locks, semaphores, thread-safe data structures |
| 15 | [Math & Bit Manipulation](#15-math-bit-manipulation--misc) | Bit tricks, modular arithmetic |

---

## 1. Arrays & Strings

> **Patterns:** Two-pointer, prefix sums, sliding window, in-place transforms

| Problem | Difficulty | Package |
|---------|-----------|---------|
| Two Sum | 🟢 | `arrays` |
| Best Time to Buy and Sell Stock | 🟢 | `arrays` |
| Product of Array Except Self | 🟡 | `arrays` |
| 3Sum | 🟡 | `arrays` |
| Container With Most Water | 🟡 | `arrays` |
| Trapping Rain Water | 🔴 | `arrays` |
| Longest Substring Without Repeating Characters | 🟡 | `arrays` |
| Minimum Window Substring | 🔴 | `arrays` |
| Merge Intervals | 🟡 | `arrays` |
| Rotate Image (matrix rotation) | 🟡 | `arrays` |

**Key Insight:** Most array problems collapse to either a two-pointer sweep or a prefix-sum pre-computation. Identify which in the first 60 seconds.

---

## 2. Hashing & Sets

> **Patterns:** Frequency counting, anagram detection, complement lookup, rolling hash

| Problem | Difficulty | Package |
|---------|-----------|---------|
| Group Anagrams | 🟡 | `hashing` |
| Longest Consecutive Sequence | 🟡 | `hashing` |
| Subarray Sum Equals K | 🟡 | `hashing` |
| Top K Frequent Elements | 🟡 | `hashing` |
| Valid Sudoku | 🟡 | `hashing` |
| Isomorphic Strings | 🟢 | `hashing` |
| Word Pattern | 🟢 | `hashing` |
| Contiguous Array | 🟡 | `hashing` |
| Design a HashMap from Scratch | 🟡 | `hashing` |
| First Missing Positive | 🔴 | `hashing` |

**Key Insight:** Use the array itself as a hash map (index = value) for O(1) space on First Missing Positive.

---

## 3. Two Pointers & Sliding Window

> **Patterns:** Fixed window, variable window (shrink on violation), same-direction vs opposite-direction pointers

| Problem | Difficulty | Package |
|---------|-----------|---------|
| Longest Repeating Character Replacement | 🟡 | `twopointers` |
| Minimum Size Subarray Sum | 🟡 | `twopointers` |
| Permutation in String | 🟡 | `twopointers` |
| Find All Anagrams in a String | 🟡 | `twopointers` |
| Remove Duplicates from Sorted Array | 🟢 | `twopointers` |
| Sort Colors (Dutch National Flag) | 🟡 | `twopointers` |
| Longest Palindromic Substring | 🟡 | `twopointers` |
| Valid Palindrome II | 🟢 | `twopointers` |
| Max Consecutive Ones III | 🟡 | `twopointers` |
| Fruit Into Baskets | 🟡 | `twopointers` |

---

## 4. Linked Lists

> **Patterns:** Dummy head node, Floyd's cycle detection, in-place reversal, merge

| Problem | Difficulty | Package |
|---------|-----------|---------|
| Reverse Linked List | 🟢 | `linkedlists` |
| Reverse Linked List II | 🟡 | `linkedlists` |
| Merge Two Sorted Lists | 🟢 | `linkedlists` |
| Merge k Sorted Lists | 🔴 | `linkedlists` |
| Linked List Cycle II (find cycle start) | 🟡 | `linkedlists` |
| Copy List with Random Pointer | 🟡 | `linkedlists` |
| Add Two Numbers | 🟡 | `linkedlists` |
| Reorder List | 🟡 | `linkedlists` |
| Palindrome Linked List | 🟢 | `linkedlists` |
| Flatten a Multilevel Doubly Linked List | 🟡 | `linkedlists` |

**Key Insight:** Always draw the pointer state before and after each operation — pointer bugs are invisible without a diagram.

---

## 5. Stacks & Queues

> **Patterns:** Monotonic stack (next greater/smaller), deque for sliding max, BFS with queue

| Problem | Difficulty | Package |
|---------|-----------|---------|
| Valid Parentheses | 🟢 | `stacks` |
| Min Stack | 🟡 | `stacks` |
| Evaluate Reverse Polish Notation | 🟡 | `stacks` |
| Daily Temperatures | 🟡 | `stacks` |
| Largest Rectangle in Histogram | 🔴 | `stacks` |
| Basic Calculator | 🔴 | `stacks` |
| Implement Queue using Stacks | 🟢 | `stacks` |
| Sliding Window Maximum | 🔴 | `stacks` |
| Decode String | 🟡 | `stacks` |
| Asteroid Collision | 🟡 | `stacks` |

---

## 6. Trees & BSTs

> **Patterns:** Recursive DFS, iterative DFS (explicit stack), level-order BFS, BST invariant

| Problem | Difficulty | Package |
|---------|-----------|---------|
| Validate Binary Search Tree | 🟡 | `trees` |
| Lowest Common Ancestor of a Binary Tree | 🟡 | `trees` |
| Binary Tree Level Order Traversal | 🟡 | `trees` |
| Serialize and Deserialize Binary Tree | 🔴 | `trees` |
| Binary Tree Maximum Path Sum | 🔴 | `trees` |
| Kth Smallest Element in a BST | 🟡 | `trees` |
| Construct Binary Tree from Preorder and Inorder | 🟡 | `trees` |
| Diameter of Binary Tree | 🟢 | `trees` |
| Binary Tree Right Side View | 🟡 | `trees` |
| Balance a Binary Search Tree | 🟡 | `trees` |

---

## 7. Graphs

> **Patterns:** BFS (shortest path), DFS (cycle detection, connected components), Union-Find, Dijkstra, topological sort (Kahn's / DFS)

| Problem | Difficulty | Package |
|---------|-----------|---------|
| Number of Islands | 🟡 | `graphs` |
| Clone Graph | 🟡 | `graphs` |
| Course Schedule (Topological Sort) | 🟡 | `graphs` |
| Course Schedule II | 🟡 | `graphs` |
| Word Ladder | 🔴 | `graphs` |
| Network Delay Time (Dijkstra) | 🟡 | `graphs` |
| Redundant Connection (Union-Find) | 🟡 | `graphs` |
| Alien Dictionary | 🔴 | `graphs` |
| Graph Valid Tree | 🟡 | `graphs` |
| Accounts Merge | 🟡 | `graphs` |

**Key Insight:** Union-Find (path compression + union by rank) achieves near-O(1) per operation — prefer it over BFS for connectivity problems.

---

## 8. Dynamic Programming

> **Patterns:** Top-down (memoisation), bottom-up (tabulation), 1D/2D DP tables, state compression

| Problem | Difficulty | Package |
|---------|-----------|---------|
| Climbing Stairs | 🟢 | `dynamicprogramming` |
| House Robber / House Robber II | 🟡 | `dynamicprogramming` |
| Coin Change | 🟡 | `dynamicprogramming` |
| Longest Increasing Subsequence | 🟡 | `dynamicprogramming` |
| Longest Common Subsequence | 🟡 | `dynamicprogramming` |
| Edit Distance | 🔴 | `dynamicprogramming` |
| 0/1 Knapsack | 🟡 | `dynamicprogramming` |
| Word Break | 🟡 | `dynamicprogramming` |
| Partition Equal Subset Sum | 🟡 | `dynamicprogramming` |
| Regular Expression Matching | 🔴 | `dynamicprogramming` |

**Key Insight:** Define state clearly: `dp[i]` = *what exactly* does index `i` represent? The transition follows naturally.

---

## 9. Greedy

> **Patterns:** Sort + scan, interval scheduling (earliest deadline first), local choices that don't backfire globally

| Problem | Difficulty | Package |
|---------|-----------|---------|
| Jump Game | 🟡 | `greedy` |
| Jump Game II | 🟡 | `greedy` |
| Gas Station | 🟡 | `greedy` |
| Task Scheduler | 🟡 | `greedy` |
| Non-overlapping Intervals | 🟡 | `greedy` |
| Minimum Number of Arrows to Burst Balloons | 🟡 | `greedy` |
| Partition Labels | 🟡 | `greedy` |
| Candy | 🔴 | `greedy` |
| Merge Triplets to Form Target Triplet | 🟡 | `greedy` |
| Boats to Save People | 🟡 | `greedy` |

---

## 10. Backtracking

> **Patterns:** Build incrementally, abandon (prune) when constraint violated, explore all branches

| Problem | Difficulty | Package |
|---------|-----------|---------|
| Subsets | 🟡 | `backtracking` |
| Permutations | 🟡 | `backtracking` |
| Combination Sum | 🟡 | `backtracking` |
| Word Search | 🟡 | `backtracking` |
| N-Queens | 🔴 | `backtracking` |
| Palindrome Partitioning | 🟡 | `backtracking` |
| Letter Combinations of a Phone Number | 🟡 | `backtracking` |
| Sudoku Solver | 🔴 | `backtracking` |
| Generate Parentheses | 🟡 | `backtracking` |
| Restore IP Addresses | 🟡 | `backtracking` |

---

## 11. Heaps / Priority Queues

> **Patterns:** Min-heap for K largest, max-heap for K smallest, two-heap for running median, K-way merge

| Problem | Difficulty | Package |
|---------|-----------|---------|
| Kth Largest Element in an Array | 🟡 | `heaps` |
| Find Median from Data Stream | 🔴 | `heaps` |
| Top K Frequent Words | 🟡 | `heaps` |
| K Closest Points to Origin | 🟡 | `heaps` |
| Reorganize String | 🟡 | `heaps` |
| Ugly Number II | 🟡 | `heaps` |
| Smallest Range Covering Elements from K Lists | 🔴 | `heaps` |
| Meeting Rooms II | 🟡 | `heaps` |
| Last Stone Weight | 🟢 | `heaps` |
| Kth Smallest Element in a Sorted Matrix | 🟡 | `heaps` |

---

## 12. Binary Search

> **Patterns:** Classic search, rotated array, search on answer space (binary search the predicate)

| Problem | Difficulty | Package |
|---------|-----------|---------|
| Binary Search | 🟢 | `binarysearch` |
| Search in Rotated Sorted Array | 🟡 | `binarysearch` |
| Find Minimum in Rotated Sorted Array | 🟡 | `binarysearch` |
| Search a 2D Matrix | 🟡 | `binarysearch` |
| Koko Eating Bananas | 🟡 | `binarysearch` |
| Median of Two Sorted Arrays | 🔴 | `binarysearch` |
| Capacity To Ship Packages Within D Days | 🟡 | `binarysearch` |
| Find Peak Element | 🟡 | `binarysearch` |
| Split Array Largest Sum | 🔴 | `binarysearch` |
| Time Based Key-Value Store | 🟡 | `binarysearch` |

**Key Insight:** If the question asks for a minimum/maximum satisfying some monotone condition, binary search on the answer — not on an array index.

---

## 13. Design / OOD & Data Structures

> **Patterns:** Hash map + doubly linked list (LRU), heap variants, trie, Snowflake ID generation

| Problem | Difficulty | Package |
|---------|-----------|---------|
| LRU Cache | 🟡 | `design` |
| LFU Cache | 🔴 | `design` |
| Design Twitter | 🟡 | `design` |
| Implement Trie (Prefix Tree) | 🟡 | `design` |
| Design Add and Search Words Data Structure | 🟡 | `design` |
| Design a Rate Limiter (Token Bucket) | 🟡 | `design` |
| Design a Parking Lot | 🟡 | `design` |
| Design an Elevator System | 🔴 | `design` |
| Design a Distributed Unique ID Generator (Snowflake) | 🔴 | `design` |
| Design a Key-Value Store with TTL | 🟡 | `design` |

---

## 14. Concurrency & System-Style Coding

> **Patterns:** Monitor pattern (`wait`/`notify`), semaphore counting, atomic CAS, thread pool

| Problem | Difficulty | Package |
|---------|-----------|---------|
| Producer-Consumer (bounded buffer) | 🟡 | `concurrency` |
| Dining Philosophers | 🔴 | `concurrency` |
| Print in Order / Print FooBar Alternately | 🟢 | `concurrency` |
| Implement a Thread-Safe Bounded Blocking Queue | 🟡 | `concurrency` |
| Implement a Read-Write Lock | 🔴 | `concurrency` |
| Implement a Thread Pool | 🔴 | `concurrency` |
| Multithreaded Web Crawler | 🟡 | `concurrency` |
| Semaphore-based Traffic Light Control | 🟡 | `concurrency` |
| Building H₂O | 🟡 | `concurrency` |
| Implement a Distributed Lock | 🔴 | `concurrency` |

**Key Insight:** Always identify shared state first, then protect it with the minimal necessary synchronisation. Over-synchronisation causes deadlock; under-synchronisation causes data races.

---

## 15. Math, Bit Manipulation & Misc

> **Patterns:** XOR for duplicates, bit shifting, modular arithmetic, fast exponentiation

| Problem | Difficulty | Package |
|---------|-----------|---------|
| Single Number / Single Number II | 🟢 | `math` |
| Number of 1 Bits | 🟢 | `math` |
| Counting Bits | 🟢 | `math` |
| Reverse Bits | 🟢 | `math` |
| Divide Two Integers (no `/` operator) | 🟡 | `math` |
| Sqrt(x) — integer square root | 🟢 | `math` |
| Pow(x, n) — fast exponentiation | 🟡 | `math` |
| Excel Sheet Column Number | 🟢 | `math` |
| Random Pick with Weight | 🟡 | `math` |
| Basic Calculator II | 🟡 | `math` |

---

## 🧩 Problem-Solving Checklist

Before writing code in any interview:

1. **Understand** — restate the problem; confirm edge cases (empty input, negatives, overflow)
2. **Examples** — walk through 2–3 concrete examples by hand
3. **Brute Force** — state the naive O(n²) or O(2ⁿ) solution and its complexity
4. **Optimise** — identify the bottleneck; apply the right pattern
5. **Code** — write clean, named-variable code (no single-letter vars except loop indices)
6. **Test** — trace through your example; check edge cases manually

---

## ⏱️ Complexity Reference

| Algorithm | Time | Space |
|-----------|------|-------|
| Binary search | O(log n) | O(1) |
| Hash map lookup | O(1) avg | O(n) |
| Heap insert/delete | O(log n) | O(n) |
| BFS / DFS | O(V + E) | O(V) |
| Merge sort | O(n log n) | O(n) |
| Quick sort (avg) | O(n log n) | O(log n) |
| DP (2D table) | O(n × m) | O(n × m) |
| Union-Find (with optimisations) | O(α(n)) ≈ O(1) | O(n) |

---

> **See also:** [`../system_design/lld/README.md`](../system_design/lld/README.md) · [`../system_design/hld/README.md`](../system_design/hld/README.md)
