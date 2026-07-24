---
layout: default
title: 'Coding Problems'
---

# Coding Problems

150+ problems with complete Java solutions.

## Arrays

| Problem | Difficulty | Time | Space |
|---------|-----------|------|-------|
| [Best Time To Buy And Sell Stock]({{ site.baseurl }}/coding/arrays/best-time-to-buy-and-sell-stock/) | Easy | O(n) | O(1) |
| [Container With Most Water]({{ site.baseurl }}/coding/arrays/container-with-most-water/) | Medium | O(n) | O(1) |
| [Longest Substring Without Repeating Chars]({{ site.baseurl }}/coding/arrays/longest-substring-without-repeating-chars/) | Medium | O(n) | O(min(n, m)) where m is the character set size |
| [Merge Intervals]({{ site.baseurl }}/coding/arrays/merge-intervals/) | Medium | O(n log n) due to sorting | O(n) for the output |
| [Minimum Window Substring]({{ site.baseurl }}/coding/arrays/minimum-window-substring/) | Hard | O(\|s\| + \|t\|) | O(\|s\| + \|t\|) |
| [Product Of Array Except Self]({{ site.baseurl }}/coding/arrays/product-of-array-except-self/) | Medium | O(n) | O(1) extra (output array not counted) |
| [Rotate Image]({{ site.baseurl }}/coding/arrays/rotate-image/) | Medium | O(n^2) | O(1) |
| [Three Sum]({{ site.baseurl }}/coding/arrays/three-sum/) | Medium | O(n^2) | O(1) extra (excluding output) |
| [Trapping Rain Water]({{ site.baseurl }}/coding/arrays/trapping-rain-water/) | Hard | O(n) | O(1) |
| [Two Sum]({{ site.baseurl }}/coding/arrays/two-sum/) | Easy | O(n) | O(n) |

## Backtracking

| Problem | Difficulty | Time | Space |
|---------|-----------|------|-------|
| [Combination Sum]({{ site.baseurl }}/coding/backtracking/combination-sum/) | Medium | O(2^(target/min)) | O(target/min) |
| [Generate Parentheses]({{ site.baseurl }}/coding/backtracking/generate-parentheses/) | Medium | O(4^n / sqrt(n)) | O(n) |
| [Letter Combinations Phone Number]({{ site.baseurl }}/coding/backtracking/letter-combinations-phone-number/) | Medium | O(4^n * n) | O(n) |
| [N Queens]({{ site.baseurl }}/coding/backtracking/n-queens/) | Hard | O(n!) | O(n^2) |
| [Palindrome Partitioning]({{ site.baseurl }}/coding/backtracking/palindrome-partitioning/) | Medium | O(n * 2^n) | O(n) |
| [Permutations]({{ site.baseurl }}/coding/backtracking/permutations/) | Medium | O(n * n!) | O(n) |
| [Restore I P Addresses]({{ site.baseurl }}/coding/backtracking/restore-i-p-addresses/) | Medium | O(1) - bounded by at most 3^4 = 81 combinations | O(1) |
| [Subsets]({{ site.baseurl }}/coding/backtracking/subsets/) | Medium | O(n * 2^n) | O(n) |
| [Sudoku Solver]({{ site.baseurl }}/coding/backtracking/sudoku-solver/) | Hard | O(9^(empty cells)) | O(81) |
| [Word Search]({{ site.baseurl }}/coding/backtracking/word-search/) | Medium | O(m*n*4^L) | O(L) where L is word length |

## Binary Search

| Problem | Difficulty | Time | Space |
|---------|-----------|------|-------|
| [Binary Search]({{ site.baseurl }}/coding/binarysearch/binary-search/) | Easy | O(log n) | O(1) |
| [Capacity To Ship Packages]({{ site.baseurl }}/coding/binarysearch/capacity-to-ship-packages/) | Medium | O(n log(sum(weights))) | O(1) |
| [Find Minimum In Rotated Sorted Array]({{ site.baseurl }}/coding/binarysearch/find-minimum-in-rotated-sorted-array/) | Medium | O(log n) | O(1) |
| [Find Peak Element]({{ site.baseurl }}/coding/binarysearch/find-peak-element/) | Medium | O(log n) | O(1) |
| [Koko Eating Bananas]({{ site.baseurl }}/coding/binarysearch/koko-eating-bananas/) | Medium | O(n log(max(piles))) | O(1) |
| [Median Of Two Sorted Arrays]({{ site.baseurl }}/coding/binarysearch/median-of-two-sorted-arrays/) | Hard | O(log(min(m,n))) | O(1) |
| [Search A2 D Matrix]({{ site.baseurl }}/coding/binarysearch/search-a2-d-matrix/) | Medium | O(log(m*n)) | O(1) |
| [Search In Rotated Sorted Array]({{ site.baseurl }}/coding/binarysearch/search-in-rotated-sorted-array/) | Medium | O(log n) | O(1) |
| [Split Array Largest Sum]({{ site.baseurl }}/coding/binarysearch/split-array-largest-sum/) | Hard | O(n log(sum(nums))) | O(1) |
| [Time Based Key Value Store]({{ site.baseurl }}/coding/binarysearch/time-based-key-value-store/) | Medium | O(1) for set, O(log n) for get | O(n) total stored entries |

## Concurrency

| Problem | Difficulty | Time | Space |
|---------|-----------|------|-------|
| [Bounded Blocking Queue]({{ site.baseurl }}/coding/concurrency/bounded-blocking-queue/) | Medium | O(1) for push/pop/peek (amortized, ignoring block time) | O(capacity) |
| [Building H2 O]({{ site.baseurl }}/coding/concurrency/building-h2-o/) | Medium | O(N) where N = number of molecules | O(1) |
| [Dining Philosophers]({{ site.baseurl }}/coding/concurrency/dining-philosophers/) | Medium | O(philosophers * meals) overall | O(N) for N philosopher threads and N locks |
| [Distributed Lock]({{ site.baseurl }}/coding/concurrency/distributed-lock/) | Hard | O(1) for tryLock and unlock | O(L) where L = number of distinct lock keys |
| [Multithreaded Web Crawler]({{ site.baseurl }}/coding/concurrency/multithreaded-web-crawler/) | Hard | O(V + E) where V=pages, E=links | O(V) for visited set |
| [Print In Order]({{ site.baseurl }}/coding/concurrency/print-in-order/) | Easy | O(1) per method | O(1) |
| [Producer Consumer]({{ site.baseurl }}/coding/concurrency/producer-consumer/) | Medium | O(N) total for N items | O(capacity) for buffer |
| [Read Write Lock]({{ site.baseurl }}/coding/concurrency/read-write-lock/) | Medium | O(1) for all lock/unlock operations | O(1) |
| [Thread Pool]({{ site.baseurl }}/coding/concurrency/thread-pool/) | Medium | O(1) for submit, O(N) for shutdown | O(queue_size + N) for queued tasks and worker threads |
| [Traffic Light]({{ site.baseurl }}/coding/concurrency/traffic-light/) | Medium | O(1) per car arrival | O(1) |

## Design

| Problem | Difficulty | Time | Space |
|---------|-----------|------|-------|
| [Design Add Search Words Data Structure]({{ site.baseurl }}/coding/design/design-add-search-words-data-structure/) | Medium | O(L) for addWord, O(26^L) worst case for search with all dots | O(N*L) total trie space |
| [Design Elevator System]({{ site.baseurl }}/coding/design/design-elevator-system/) | Hard | O(N) per requestElevator, O(N) per step | O(N + R) where R=total requests queued |
| [Design Parking Lot]({{ site.baseurl }}/coding/design/design-parking-lot/) | Medium | O(N) for parkVehicle where N=total spots, O(1) for unparkVehicle | O(N) for spot storage |
| [Design Rate Limiter]({{ site.baseurl }}/coding/design/design-rate-limiter/) | Medium | O(1) per allow() call | O(U) where U = number of unique users |
| [Design Twitter]({{ site.baseurl }}/coding/design/design-twitter/) | Medium | O(N log N) for getNewsFeed where N=total tweets from user+followees | O(U*T) where U=users, T=tweets per user |
| [Distributed Unique I D Generator]({{ site.baseurl }}/coding/design/distributed-unique-i-d-generator/) | Hard | O(1) amortized per ID generation | O(1) |
| [Implement Trie]({{ site.baseurl }}/coding/design/implement-trie/) | Medium | O(L) for all operations where L = length of word/prefix | O(N*L) for N words of average length L |
| [Key Value Store With T T L]({{ site.baseurl }}/coding/design/key-value-store-with-t-t-l/) | Medium | O(1) for get/set/delete, O(N) for cleanup sweep | O(N) for N entries |
| [L F U Cache]({{ site.baseurl }}/coding/design/l-f-u-cache/) | Hard | O(1) for get and put | O(capacity) |
| [L R U Cache]({{ site.baseurl }}/coding/design/l-r-u-cache/) | Medium | O(1) for get and put | O(capacity) |

## Dynamic Programming

| Problem | Difficulty | Time | Space |
|---------|-----------|------|-------|
| [Climbing Stairs]({{ site.baseurl }}/coding/dynamicprogramming/climbing-stairs/) | Easy | O(n) | O(1) |
| [Coin Change]({{ site.baseurl }}/coding/dynamicprogramming/coin-change/) | Medium | O(amount * coins.length) | O(amount) |
| [Edit Distance]({{ site.baseurl }}/coding/dynamicprogramming/edit-distance/) | Hard | O(m * n) | O(m * n) |
| [House Robber]({{ site.baseurl }}/coding/dynamicprogramming/house-robber/) | Medium | O(n) | O(1) |
| [Knapsack01]({{ site.baseurl }}/coding/dynamicprogramming/knapsack01/) | Medium | O(n * W) | O(W) |
| [Longest Common Subsequence]({{ site.baseurl }}/coding/dynamicprogramming/longest-common-subsequence/) | Medium | O(m * n) | O(m * n) |
| [Longest Increasing Subsequence]({{ site.baseurl }}/coding/dynamicprogramming/longest-increasing-subsequence/) | Medium | O(n log n) | O(n) |
| [Partition Equal Subset Sum]({{ site.baseurl }}/coding/dynamicprogramming/partition-equal-subset-sum/) | Medium | O(n * target) | O(target) |
| [Regular Expression Matching]({{ site.baseurl }}/coding/dynamicprogramming/regular-expression-matching/) | Hard | O(m * n) | O(m * n) |
| [Word Break]({{ site.baseurl }}/coding/dynamicprogramming/word-break/) | Medium | O(n^2 * m) where n=s.length, m=avg word length | O(n + dict size) |

## Graphs

| Problem | Difficulty | Time | Space |
|---------|-----------|------|-------|
| [Accounts Merge]({{ site.baseurl }}/coding/graphs/accounts-merge/) | Medium | O(N * K * alpha(N*K)) where N=accounts, K=avg emails | O(N * K) |
| [Alien Dictionary]({{ site.baseurl }}/coding/graphs/alien-dictionary/) | Hard | O(C) — C is total number of characters across all words | O(U + min(U^2, N)) — U is unique chars, N is word count |
| [Clone Graph]({{ site.baseurl }}/coding/graphs/clone-graph/) | Medium | O(V + E) — visit each node and edge once | O(V) — HashMap stores all node clones |
| [Course Schedule]({{ site.baseurl }}/coding/graphs/course-schedule/) | Medium | O(V + E) — process each node and edge once | O(V + E) — adjacency list and in-degree array |
| [Course Schedule I I]({{ site.baseurl }}/coding/graphs/course-schedule-i-i/) | Medium | O(V + E) — each vertex and edge processed once | O(V + E) — adjacency list storage |
| [Graph Valid Tree]({{ site.baseurl }}/coding/graphs/graph-valid-tree/) | Medium | O(n * alpha(n)) ≈ O(n) | O(n) |
| [Network Delay Time]({{ site.baseurl }}/coding/graphs/network-delay-time/) | Medium | O((V + E) log V) — Dijkstra with min-heap | O(V + E) — adjacency list and distance array |
| [Number Of Islands]({{ site.baseurl }}/coding/graphs/number-of-islands/) | Medium | O(m*n) — each cell visited at most once | O(m*n) — recursion stack in worst case |
| [Redundant Connection]({{ site.baseurl }}/coding/graphs/redundant-connection/) | Medium | O(n * α(n)) ≈ O(n) — α is inverse Ackermann function | O(n) — parent and rank arrays |
| [Word Ladder]({{ site.baseurl }}/coding/graphs/word-ladder/) | Hard | O(M^2 * N) — M is word length, N is dictionary size | O(M^2 * N) — queue and visited set |

## Greedy

| Problem | Difficulty | Time | Space |
|---------|-----------|------|-------|
| [Boats To Save People]({{ site.baseurl }}/coding/greedy/boats-to-save-people/) | Medium | O(n log n) | O(1) |
| [Candy]({{ site.baseurl }}/coding/greedy/candy/) | Hard | O(n) | O(n) |
| [Gas Station]({{ site.baseurl }}/coding/greedy/gas-station/) | Medium | O(n) | O(1) |
| [Jump Game]({{ site.baseurl }}/coding/greedy/jump-game/) | Medium | O(n) | O(1) |
| [Jump Game I I]({{ site.baseurl }}/coding/greedy/jump-game-i-i/) | Medium | O(n) | O(1) |
| [Merge Triplets To Form Target Triplet]({{ site.baseurl }}/coding/greedy/merge-triplets-to-form-target-triplet/) | Medium | O(n) | O(1) |
| [Min Arrows To Burst Balloons]({{ site.baseurl }}/coding/greedy/min-arrows-to-burst-balloons/) | Medium | O(n log n) | O(1) |
| [Non Overlapping Intervals]({{ site.baseurl }}/coding/greedy/non-overlapping-intervals/) | Medium | O(n log n) | O(1) |
| [Partition Labels]({{ site.baseurl }}/coding/greedy/partition-labels/) | Medium | O(n) | O(1) — at most 26 distinct chars |
| [Task Scheduler]({{ site.baseurl }}/coding/greedy/task-scheduler/) | Medium | O(n) where n = tasks.length | O(1) |

## Hashing

| Problem | Difficulty | Time | Space |
|---------|-----------|------|-------|
| [Contiguous Array]({{ site.baseurl }}/coding/hashing/contiguous-array/) | Medium | O(n) | O(n) |
| [Design Hash Map]({{ site.baseurl }}/coding/hashing/design-hash-map/) | Easy | O(n/k) average per operation, where k is number of buckets | O(n + k) |
| [First Missing Positive]({{ site.baseurl }}/coding/hashing/first-missing-positive/) | Hard | O(n) | O(1) |
| [Group Anagrams]({{ site.baseurl }}/coding/hashing/group-anagrams/) | Medium | O(n * k log k) where k is the max string length | O(n * k) |
| [Isomorphic Strings]({{ site.baseurl }}/coding/hashing/isomorphic-strings/) | Easy | O(n) | O(1) — at most 256 unique ASCII characters |
| [Longest Consecutive Sequence]({{ site.baseurl }}/coding/hashing/longest-consecutive-sequence/) | Medium | O(n) | O(n) |
| [Subarray Sum Equals K]({{ site.baseurl }}/coding/hashing/subarray-sum-equals-k/) | Medium | O(n) | O(n) |
| [Top K Frequent Elements]({{ site.baseurl }}/coding/hashing/top-k-frequent-elements/) | Medium | O(n) | O(n) |
| [Valid Sudoku]({{ site.baseurl }}/coding/hashing/valid-sudoku/) | Medium | O(1) — always 81 cells | O(1) — fixed 27 sets with max 9 elements each |
| [Word Pattern]({{ site.baseurl }}/coding/hashing/word-pattern/) | Easy | O(n) where n is the number of words | O(n) |

## Heaps

| Problem | Difficulty | Time | Space |
|---------|-----------|------|-------|
| [Find Median From Data Stream]({{ site.baseurl }}/coding/heaps/find-median-from-data-stream/) | Hard | O(log n) per addNum, O(1) per findMedian | O(n) |
| [K Closest Points To Origin]({{ site.baseurl }}/coding/heaps/k-closest-points-to-origin/) | Medium | O(n log k) | O(k) |
| [Kth Largest Element In Array]({{ site.baseurl }}/coding/heaps/kth-largest-element-in-array/) | Medium | O(n log k) | O(k) |
| [Kth Smallest Element Sorted Matrix]({{ site.baseurl }}/coding/heaps/kth-smallest-element-sorted-matrix/) | Medium | O(k log n) | O(n) |
| [Last Stone Weight]({{ site.baseurl }}/coding/heaps/last-stone-weight/) | Easy | O(n log n) | O(n) |
| [Meeting Rooms I I]({{ site.baseurl }}/coding/heaps/meeting-rooms-i-i/) | Medium | O(n log n) | O(n) |
| [Reorganize String]({{ site.baseurl }}/coding/heaps/reorganize-string/) | Medium | O(n log k) where k is number of distinct characters | O(k) |
| [Smallest Range Covering K Lists]({{ site.baseurl }}/coding/heaps/smallest-range-covering-k-lists/) | Hard | O(n log k) where n is total elements | O(k) |
| [Top K Frequent Words]({{ site.baseurl }}/coding/heaps/top-k-frequent-words/) | Medium | O(n log k) | O(n) |
| [Ugly Number I I]({{ site.baseurl }}/coding/heaps/ugly-number-i-i/) | Medium | O(n) | O(n) |

## Linked Lists

| Problem | Difficulty | Time | Space |
|---------|-----------|------|-------|
| [Add Two Numbers]({{ site.baseurl }}/coding/linkedlists/add-two-numbers/) | Medium | O(max(m, n)) | O(max(m, n)) |
| [Copy List With Random Pointer]({{ site.baseurl }}/coding/linkedlists/copy-list-with-random-pointer/) | Medium | O(n) | O(n) |
| [Flatten Multilevel Doubly Linked List]({{ site.baseurl }}/coding/linkedlists/flatten-multilevel-doubly-linked-list/) | Medium | O(n) | O(n) — stack depth proportional to nesting |
| [Linked List Cycle I I]({{ site.baseurl }}/coding/linkedlists/linked-list-cycle-i-i/) | Medium | O(n) | O(1) |
| [Merge K Sorted Lists]({{ site.baseurl }}/coding/linkedlists/merge-k-sorted-lists/) | Hard | O(n log k) | O(k) |
| [Merge Two Sorted Lists]({{ site.baseurl }}/coding/linkedlists/merge-two-sorted-lists/) | Easy | O(n + m) | O(1) |
| [Palindrome Linked List]({{ site.baseurl }}/coding/linkedlists/palindrome-linked-list/) | Easy | O(n) | O(1) |
| [Reorder List]({{ site.baseurl }}/coding/linkedlists/reorder-list/) | Medium | O(n) | O(1) |
| [Reverse Linked List]({{ site.baseurl }}/coding/linkedlists/reverse-linked-list/) | Easy | O(n) | O(1) |
| [Reverse Linked List I I]({{ site.baseurl }}/coding/linkedlists/reverse-linked-list-i-i/) | Medium | O(n) | O(1) |

## Math

| Problem | Difficulty | Time | Space |
|---------|-----------|------|-------|
| [Basic Calculator I I]({{ site.baseurl }}/coding/math/basic-calculator-i-i/) | Medium | O(n) where n = length of expression | O(n) for the stack |
| [Counting Bits]({{ site.baseurl }}/coding/math/counting-bits/) | Easy | O(n) | O(n) for output array |
| [Divide Two Integers]({{ site.baseurl }}/coding/math/divide-two-integers/) | Medium | O(log²n) | O(1) |
| [Excel Sheet Column Number]({{ site.baseurl }}/coding/math/excel-sheet-column-number/) | Easy | O(L) for titleToNumber, O(log_26 n) for columnToTitle | O(L) for output string |
| [Number Of1 Bits]({{ site.baseurl }}/coding/math/number-of1-bits/) | Easy | O(1) (at most 32 iterations) | O(1) |
| [Pow X N]({{ site.baseurl }}/coding/math/pow-x-n/) | Medium | O(log n) | O(1) |
| [Random Pick With Weight]({{ site.baseurl }}/coding/math/random-pick-with-weight/) | Medium | O(n) constructor, O(log n) per pickIndex | O(n) for prefix sums |
| [Reverse Bits]({{ site.baseurl }}/coding/math/reverse-bits/) | Easy | O(1) — exactly 32 iterations | O(1) |
| [Single Number]({{ site.baseurl }}/coding/math/single-number/) | Easy / Medium | O(n) for both | O(1) for both |
| [Sqrt X]({{ site.baseurl }}/coding/math/sqrt-x/) | Easy | O(log x) | O(1) |

## Stacks

| Problem | Difficulty | Time | Space |
|---------|-----------|------|-------|
| [Asteroid Collision]({{ site.baseurl }}/coding/stacks/asteroid-collision/) | Medium | O(n) | O(n) |
| [Basic Calculator]({{ site.baseurl }}/coding/stacks/basic-calculator/) | Hard | O(n) | O(n) |
| [Daily Temperatures]({{ site.baseurl }}/coding/stacks/daily-temperatures/) | Medium | O(n) | O(n) |
| [Decode String]({{ site.baseurl }}/coding/stacks/decode-string/) | Medium | O(n * maxK) where maxK is the maximum repeat count | O(n) |
| [Evaluate Reverse Polish Notation]({{ site.baseurl }}/coding/stacks/evaluate-reverse-polish-notation/) | Medium | O(n) | O(n) |
| [Implement Queue Using Stacks]({{ site.baseurl }}/coding/stacks/implement-queue-using-stacks/) | Easy | O(1) amortized for all operations | O(n) |
| [Largest Rectangle In Histogram]({{ site.baseurl }}/coding/stacks/largest-rectangle-in-histogram/) | Hard | O(n) | O(n) |
| [Min Stack]({{ site.baseurl }}/coding/stacks/min-stack/) | Medium | O(1) for all operations | O(n) |
| [Sliding Window Maximum]({{ site.baseurl }}/coding/stacks/sliding-window-maximum/) | Hard | O(n) | O(k) |
| [Valid Parentheses]({{ site.baseurl }}/coding/stacks/valid-parentheses/) | Easy | O(n) | O(n) |

## Trees

| Problem | Difficulty | Time | Space |
|---------|-----------|------|-------|
| [Balance B S T]({{ site.baseurl }}/coding/trees/balance-b-s-t/) | Medium | O(n) — inorder traversal + tree construction | O(n) — sorted list plus recursion stack |
| [Binary Tree Level Order Traversal]({{ site.baseurl }}/coding/trees/binary-tree-level-order-traversal/) | Medium | O(n) — visit each node once | O(w) — w is max width of tree (queue size) |
| [Binary Tree Maximum Path Sum]({{ site.baseurl }}/coding/trees/binary-tree-maximum-path-sum/) | Hard | O(n) — visit each node once | O(h) — recursion stack height |
| [Binary Tree Right Side View]({{ site.baseurl }}/coding/trees/binary-tree-right-side-view/) | Medium | O(n) — visit each node once | O(w) — w is maximum width of the tree |
| [Construct Binary Tree From Preorder Inorder]({{ site.baseurl }}/coding/trees/construct-binary-tree-from-preorder-inorder/) | Medium | O(n) — each node processed once with O(1) index lookup | O(n) — HashMap plus recursion stack |
| [Diameter Of Binary Tree]({{ site.baseurl }}/coding/trees/diameter-of-binary-tree/) | Easy | O(n) — visit each node once | O(h) — recursion stack depth |
| [Kth Smallest Element B S T]({{ site.baseurl }}/coding/trees/kth-smallest-element-b-s-t/) | Medium | O(h + k) — h is tree height, traverse up to h+k nodes | O(h) — stack stores at most h nodes |
| [Lowest Common Ancestor]({{ site.baseurl }}/coding/trees/lowest-common-ancestor/) | Medium | O(n) — visit each node once | O(h) — recursion stack depth |
| [Serialize Deserialize Binary Tree]({{ site.baseurl }}/coding/trees/serialize-deserialize-binary-tree/) | Hard | O(n) — visit each node once in both operations | O(n) — queue and string storage |
| [Validate Binary Search Tree]({{ site.baseurl }}/coding/trees/validate-binary-search-tree/) | Medium | O(n) — visit each node once | O(h) — recursion stack depth h (height of tree) |

## Two Pointers

| Problem | Difficulty | Time | Space |
|---------|-----------|------|-------|
| [Find All Anagrams In String]({{ site.baseurl }}/coding/twopointers/find-all-anagrams-in-string/) | Medium | O(n) | O(1) |
| [Fruit Into Baskets]({{ site.baseurl }}/coding/twopointers/fruit-into-baskets/) | Medium | O(n) | O(1) — at most 3 entries in map at any time |
| [Longest Palindromic Substring]({{ site.baseurl }}/coding/twopointers/longest-palindromic-substring/) | Medium | O(n^2) | O(1) |
| [Longest Repeating Character Replacement]({{ site.baseurl }}/coding/twopointers/longest-repeating-character-replacement/) | Medium | O(n) | O(1) — 26 uppercase letters |
| [Max Consecutive Ones I I I]({{ site.baseurl }}/coding/twopointers/max-consecutive-ones-i-i-i/) | Medium | O(n) | O(1) |
| [Minimum Size Subarray Sum]({{ site.baseurl }}/coding/twopointers/minimum-size-subarray-sum/) | Medium | O(n) | O(1) |
| [Permutation In String]({{ site.baseurl }}/coding/twopointers/permutation-in-string/) | Medium | O(n) where n = s2.length | O(1) — fixed 26-length arrays |
| [Remove Duplicates From Sorted Array]({{ site.baseurl }}/coding/twopointers/remove-duplicates-from-sorted-array/) | Easy | O(n) | O(1) |
| [Sort Colors]({{ site.baseurl }}/coding/twopointers/sort-colors/) | Medium | O(n) | O(1) |
| [Valid Palindrome I I]({{ site.baseurl }}/coding/twopointers/valid-palindrome-i-i/) | Easy | O(n) | O(1) |
