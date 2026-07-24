---
layout: problem
title: "Design Twitter"
category: design
category_display: "Design"
difficulty: Medium
time_complexity: "O(N log N) for getNewsFeed where N=total tweets from user+followees"
space_complexity: "O(U*T) where U=users, T=tweets per user"
tags: [design]
render_with_liquid: false
---

## Problem

Design Twitter Design a simplified Twitter where users can post tweets, follow/unfollow other users, and see the 10 most recent tweets in their news feed. postTweet(userId, tweetId), getNewsFeed(userId), follow(followerId, followeeId), unfollow(followerId, followeeId).

## Approach

Use a global timestamp counter to order tweets. Store follows in HashMap<userId, Set<userId>> and tweets in HashMap<userId, List<int[]{timestamp,tweetId}>>. For getNewsFeed, collect tweets from user and all followees, merge using a max-heap (priority queue by timestamp descending), and return top 10 tweet IDs.

## Solution

```java
package org.interview.coding.design;

import java.util.*;

/**
 * Problem: Design Twitter
 * Difficulty: Medium
 *
 * Description: Design a simplified Twitter where users can post tweets, follow/unfollow other users,
 * and see the 10 most recent tweets in their news feed. postTweet(userId, tweetId), getNewsFeed(userId),
 * follow(followerId, followeeId), unfollow(followerId, followeeId).
 *
 * Example:
 *   Input: postTweet(1,5), getNewsFeed(1), follow(1,2), postTweet(2,6), getNewsFeed(1), unfollow(1,2), getNewsFeed(1)
 *   Output: [5], [6,5], [5]
 *
 * Approach: Use a global timestamp counter to order tweets. Store follows in HashMap<userId, Set<userId>>
 * and tweets in HashMap<userId, List<int[]{timestamp,tweetId}>>. For getNewsFeed, collect tweets from
 * user and all followees, merge using a max-heap (priority queue by timestamp descending), and return
 * top 10 tweet IDs.
 *
 * Time Complexity: O(N log N) for getNewsFeed where N=total tweets from user+followees
 * Space Complexity: O(U*T) where U=users, T=tweets per user
 *
 * Test Cases:
 *   1. postTweet(1,5), getNewsFeed(1) → [5]
 *   2. follow(1,2), postTweet(2,6), getNewsFeed(1) → [6,5]
 *   3. Edge: unfollow(1,2), getNewsFeed(1) → [5] (only own tweets)
 */
public class DesignTwitter {

    private int timestamp;
    private final Map<Integer, Set<Integer>> follows;
    private final Map<Integer, List<int[]>> tweets; // [timestamp, tweetId]

    public DesignTwitter() {
        timestamp = 0;
        follows = new HashMap<>();
        tweets = new HashMap<>();
    }

    public void postTweet(int userId, int tweetId) {
        tweets.computeIfAbsent(userId, k -> new ArrayList<>())
              .add(new int[]{timestamp++, tweetId});
    }

    public List<Integer> getNewsFeed(int userId) {
        // max-heap by timestamp
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> b[0] - a[0]);

        // Add own tweets
        List<int[]> userTweets = tweets.getOrDefault(userId, Collections.emptyList());
        for (int[] tweet : userTweets) pq.offer(tweet);

        // Add followees' tweets
        Set<Integer> followees = follows.getOrDefault(userId, Collections.emptySet());
        for (int followeeId : followees) {
            List<int[]> followeeTweets = tweets.getOrDefault(followeeId, Collections.emptyList());
            for (int[] tweet : followeeTweets) pq.offer(tweet);
        }

        List<Integer> feed = new ArrayList<>();
        int count = 0;
        while (!pq.isEmpty() && count < 10) {
            feed.add(pq.poll()[1]);
            count++;
        }
        return feed;
    }

    public void follow(int followerId, int followeeId) {
        follows.computeIfAbsent(followerId, k -> new HashSet<>()).add(followeeId);
    }

    public void unfollow(int followerId, int followeeId) {
        follows.getOrDefault(followerId, Collections.emptySet()).remove(followeeId);
    }

    public static void main(String[] args) {
        // Test Case 1: Single user tweet feed
        DesignTwitter twitter = new DesignTwitter();
        twitter.postTweet(1, 5);
        System.out.println("Feed(1)=" + twitter.getNewsFeed(1)); // [5]

        // Test Case 2: Following another user adds their tweets to feed
        twitter.follow(1, 2);
        twitter.postTweet(2, 6);
        System.out.println("Feed(1)=" + twitter.getNewsFeed(1)); // [6, 5]

        // Test Case 3: Edge - unfollow removes followee's tweets from feed
        twitter.unfollow(1, 2);
        System.out.println("Feed(1)=" + twitter.getNewsFeed(1)); // [5]
        System.out.println("Feed(2)=" + twitter.getNewsFeed(2)); // [6] (own feed unchanged)
    }
}
```

## Complexity

- **Time:** O(N log N) for getNewsFeed where N=total tweets from user+followees
- **Space:** O(U*T) where U=users, T=tweets per user
