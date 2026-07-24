#!/usr/bin/env python3
import os, re

BASE_DIR = os.path.dirname(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))
CODING_SRC = os.path.join(BASE_DIR, "src/main/java/org/interview/coding")
LLD_SRC = os.path.join(BASE_DIR, "src/main/java/org/interview/system_design/lld")
HLD_SRC = os.path.join(BASE_DIR, "src/main/java/org/interview/system_design/hld")
SITE_DIR = os.path.join(BASE_DIR, "site")
CODING_OUT = os.path.join(SITE_DIR, "_coding")
LLD_OUT = os.path.join(SITE_DIR, "_lld")
HLD_OUT = os.path.join(SITE_DIR, "_hld")

DIFFICULTY_MAP = {"easy": "Easy", "medium": "Medium", "hard": "Hard"}

CATEGORY_DISPLAY = {
    "arrays": "Arrays", "backtracking": "Backtracking",
    "binarysearch": "Binary Search", "concurrency": "Concurrency",
    "design": "Design", "dynamicprogramming": "Dynamic Programming",
    "graphs": "Graphs", "greedy": "Greedy", "hashing": "Hashing",
    "heaps": "Heaps", "linkedlists": "Linked Lists", "math": "Math",
    "stacks": "Stacks", "trees": "Trees", "twopointers": "Two Pointers",
}

LLD_TITLE_MAP = {
    "cabbooking": "Cab Booking System", "cache": "Cache System",
    "chess": "Chess Game", "coffeemachine": "Coffee Machine",
    "distributedlock": "Distributed Lock", "elevator": "Elevator System",
    "filesystem": "File System", "library": "Library Management System",
    "logging": "Logging Framework", "moviebooking": "Movie Booking System",
    "notification": "Notification System", "parkinglot": "Parking Lot System",
    "payment": "Payment System", "pricingengine": "Pricing Engine",
    "ratelimiter": "Rate Limiter", "shoppingcart": "Shopping Cart",
    "snakeladder": "Snake and Ladder Game", "splitwise": "Splitwise",
    "tictactoe": "Tic Tac Toe Game", "vendingmachine": "Vending Machine",
}

HLD_TITLE_MAP = {
    "distributed_search": "Distributed Search Engine",
    "dropbox": "Dropbox", "google_drive": "Google Drive",
    "instagram_feed": "Instagram Feed", "payment_system": "Payment System",
    "uber": "Uber", "whatsapp": "WhatsApp",
}

LEETCODE_MAP = {
    "TwoSum": 1, "LongestSubstringWithoutRepeatingChars": 3,
    "ThreeSum": 15, "ContainerWithMostWater": 11, "TrappingRainWater": 42,
    "MergeIntervals": 56, "RotateImage": 48, "ProductOfArrayExceptSelf": 238,
    "MinimumWindowSubstring": 76, "BestTimeToBuyAndSellStock": 121,
    "WordSearch": 79, "LargestRectangleInHistogram": 84,
    "BinaryTreeLevelOrderTraversal": 102, "ValidateBinarySearchTree": 98,
    "BinaryTreeMaximumPathSum": 124, "LongestConsecutiveSequence": 128,
    "WordLadder": 127, "LongestIncreasingSubsequence": 300, "CoinChange": 322,
    "NumberOfIslands": 200, "CourseSchedule": 207, "HouseRobber": 198,
    "FindMedianFromDataStream": 295, "LFUCache": 460, "LRUCache": 146,
    "CloneGraph": 133, "ImplementTrie": 208, "WordBreak": 139,
    "DecodeString": 394, "TopKFrequentElements": 347,
    "KthLargestElementInArray": 215, "SearchInRotatedSortedArray": 33,
    "BinarySearch": 704, "MinStack": 155, "DailyTemperatures": 739,
    "ValidParentheses": 20, "LinkedListCycleII": 142,
    "ReverseLinkedList": 206, "MergeKSortedLists": 23,
    "DiameterOfBinaryTree": 543, "LowestCommonAncestor": 236,
    "BalanceBST": 1382, "Permutations": 46, "CombinationSum": 39,
    "NQueens": 51, "EditDistance": 72, "GasStation": 134,
    "TaskScheduler": 621, "PartitionLabels": 763, "AccountsMerge": 721,
    "ValidSudoku": 36, "FirstMissingPositive": 41, "SingleNumber": 136,
    "SortColors": 75, "FindAllAnagramsInString": 438,
    "MinimumSizeSubarraySum": 209, "RemoveDuplicatesFromSortedArray": 26,
}


def to_kebab(name):
    return re.sub(r"(?<!^)(?=[A-Z])", "-", name).lower()


def to_title(name):
    return re.sub(r"(?<!^)(?=[A-Z])", " ", name).strip()


def extract_javadoc(src):
    info = {"difficulty": "Medium", "time_complexity": "O(n)",
            "space_complexity": "O(n)", "description": "", "approach": ""}
    m = re.search(r"/[*][*](.*?)[*]/", src, re.DOTALL)
    if not m:
        return info
    doc = m.group(1)
    newline = chr(10)
    for line in doc.split(newline):
        line = line.strip().lstrip("*").strip()
        ll = line.lower()
        if ll.startswith("difficulty:"):
            val = line[len("difficulty:"):].strip()
            info["difficulty"] = DIFFICULTY_MAP.get(val.lower(), val)
        elif ll.startswith("time complexity:"):
            info["time_complexity"] = line[len("time complexity:"):].strip()
        elif ll.startswith("space complexity:"):
            info["space_complexity"] = line[len("space complexity:"):].strip()

    clean = re.sub(r"^\s*[*]\s?", "", doc, flags=re.MULTILINE)
    desc_lines, approach_lines = [], []
    in_d, in_a = False, False
    for line in clean.split(newline):
        s = line.strip()
        l = s.lower()
        if l.startswith("description:") or l.startswith("problem:"):
            in_d, in_a = True, False
            rest = s.split(":", 1)[1].strip() if ":" in s else ""
            if rest: desc_lines.append(rest)
            continue
        if l.startswith("approach:"):
            in_d, in_a = False, True
            rest = s.split(":", 1)[1].strip() if ":" in s else ""
            if rest: approach_lines.append(rest)
            continue
        stops = ["time complexity:", "space complexity:", "difficulty:", "example:", "@"]
        if in_d:
            if any(l.startswith(x) for x in stops + ["approach:"]): in_d = False
            elif s: desc_lines.append(s)
        if in_a:
            if any(l.startswith(x) for x in stops): in_a = False
            elif s: approach_lines.append(s)
    if desc_lines: info["description"] = " ".join(desc_lines)
    if approach_lines: info["approach"] = " ".join(approach_lines)
    return info


def gen_coding():
    n = 0
    for cat in sorted(os.listdir(CODING_SRC)):
        cat_path = os.path.join(CODING_SRC, cat)
        if not os.path.isdir(cat_path): continue
        out_dir = os.path.join(CODING_OUT, cat)
        os.makedirs(out_dir, exist_ok=True)
        for fname in sorted(os.listdir(cat_path)):
            if not fname.endswith(".java"): continue
            cls = fname[:-5]
            slug = to_kebab(cls)
            title = to_title(cls)
            with open(os.path.join(cat_path, fname)) as f: src = f.read()
            info = extract_javadoc(src)
            lc = LEETCODE_MAP.get(cls, "")
            cat_d = CATEGORY_DISPLAY.get(cat, cat.title())
            q = chr(34)
            fm = ["---", "layout: problem", "render_with_liquid: false",
                  "title: " + q + title.replace(q, "'") + q,
                  "category: " + cat,
                  "category_display: " + q + cat_d + q,
                  "difficulty: " + info["difficulty"],
                  "time_complexity: " + q + info["time_complexity"].replace(q,"'") + q,
                  "space_complexity: " + q + info["space_complexity"].replace(q,"'") + q]
            if lc: fm.append("leetcode: " + str(lc))
            fm.append("tags: [" + cat + "]")
            fm.append("---")
            desc = info["description"] or "Solve the " + title + " problem."
            approach = info["approach"] or "See solution code for implementation."
            nl = chr(10)
            bt = chr(96) * 3
            page = nl.join(fm) + nl + nl
            page += "## Problem" + nl + nl + desc + nl + nl
            page += "## Approach" + nl + nl + approach + nl + nl
            page += "## Solution" + nl + nl + bt + "java" + nl + src.strip() + nl + bt + nl + nl
            page += "## Complexity" + nl + nl
            page += "- **Time:** " + info["time_complexity"] + nl
            page += "- **Space:** " + info["space_complexity"] + nl
            with open(os.path.join(out_dir, slug + ".md"), "w") as f: f.write(page)
            n += 1
            print("  OK " + cat + "/" + slug + ".md")
    return n


def gen_lld():
    n = 0
    os.makedirs(LLD_OUT, exist_ok=True)
    for sname in sorted(os.listdir(LLD_SRC)):
        sp = os.path.join(LLD_SRC, sname)
        if not os.path.isdir(sp): continue
        jf = {}
        for fn in sorted(os.listdir(sp)):
            if fn.endswith(".java"):
                with open(os.path.join(sp, fn)) as f: jf[fn] = f.read()
        if not jf: continue
        readme = ""
        rp = os.path.join(sp, "README.md")
        if os.path.exists(rp):
            with open(rp) as f: readme = f.read()
        title = LLD_TITLE_MAP.get(sname, sname.title())
        q = chr(34)
        nl = chr(10)
        bt = chr(96) * 3
        fm = ["---", "layout: lld", "render_with_liquid: false",
              "title: " + q + title + q,
              "system: " + sname,
              "description: " + q + "LLD of " + title + q,
              "files:"]
        for fn in jf: fm.append("  - " + q + fn + q)
        fm.append("---")
        page = nl.join(fm) + nl + nl
        page += readme + nl + nl if readme else "## " + title + nl + nl + "Complete Java LLD implementation." + nl + nl
        page += "## Source Files" + nl + nl
        page += '<div class=' + q + 'lld-tabs' + q + '>' + nl
        page += '<div class=' + q + 'tab-buttons' + q + '>' + nl
        for i, fn in enumerate(jf):
            act = " active" if i == 0 else ""
            page += '<button class=' + q + 'tab-btn' + act + q + ' data-tab=' + q + fn + q + '>' + fn + '</button>' + nl
        page += '</div>' + nl
        for i, (fn, code) in enumerate(jf.items()):
            act = " active" if i == 0 else ""
            sid = fn.replace(".", "-")
            page += '<div class=' + q + 'tab-content' + act + q + ' id=' + q + sid + q + '>' + nl + nl
            page += bt + "java" + nl + code.strip() + nl + bt + nl + nl
            page += '</div>' + nl
        page += '</div>' + nl
        with open(os.path.join(LLD_OUT, sname + ".md"), "w") as f: f.write(page)
        n += 1
        print("  OK _lld/" + sname + ".md (" + str(len(jf)) + " files)")
    return n


def gen_hld():
    n = 0
    os.makedirs(HLD_OUT, exist_ok=True)
    q = chr(34)
    nl = chr(10)
    for fname in sorted(os.listdir(HLD_SRC)):
        if not fname.endswith(".md"): continue
        name = fname[:-3]
        title = HLD_TITLE_MAP.get(name, to_title(name))
        with open(os.path.join(HLD_SRC, fname)) as f: content = f.read()
        desc = ""
        for line in content.split(nl):
            if line.strip() and not line.startswith("#"):
                desc = line.strip()[:150].replace(q, "'")
                break
        fm = ["---", "layout: hld",
              "title: " + q + title + q,
              "system: " + name,
              "description: " + q + desc + q,
              "---"]
        with open(os.path.join(HLD_OUT, name + ".md"), "w") as f:
            f.write(nl.join(fm) + nl + nl + content)
        n += 1
        print("  OK _hld/" + name + ".md")
    return n


def gen_coding_idx():
    nl = chr(10)
    lines = ["---", "layout: default", "title: 'Coding Problems'", "---", "",
             "# Coding Problems", "", "150+ problems with complete Java solutions.", ""]
    for cat in sorted(CATEGORY_DISPLAY.keys()):
        cp = os.path.join(CODING_SRC, cat)
        if not os.path.isdir(cp): continue
        jfs = sorted([f for f in os.listdir(cp) if f.endswith(".java")])
        if not jfs: continue
        lines += ["## " + CATEGORY_DISPLAY[cat], "",
                  "| Problem | Difficulty | Time | Space |",
                  "|---------|-----------|------|-------|"]
        for fn in jfs:
            cls = fn[:-5]; slug = to_kebab(cls); title = to_title(cls)
            with open(os.path.join(cp, fn)) as f: src = f.read()
            info = extract_javadoc(src)
            link = "[" + title + "]({{ site.baseurl }}/coding/" + cat + "/" + slug + "/)"
            lines.append("| " + link + " | " + info["difficulty"] + " | " + info["time_complexity"] + " | " + info["space_complexity"] + " |")
        lines.append("")
    op = os.path.join(SITE_DIR, "coding/index.md")
    os.makedirs(os.path.dirname(op), exist_ok=True)
    with open(op, "w") as f: f.write(nl.join(lines))
    print("  OK coding/index.md")


def gen_lld_idx():
    nl = chr(10)
    descs = {"cabbooking": "Ride-hailing platform", "cache": "LRU/LFU Cache",
             "chess": "Chess Game", "coffeemachine": "Coffee Machine",
             "distributedlock": "Distributed Lock", "elevator": "Elevator System",
             "filesystem": "File System", "library": "Library Management",
             "logging": "Logging Framework", "moviebooking": "Movie Booking",
             "notification": "Notification System", "parkinglot": "Parking Lot",
             "payment": "Payment System", "pricingengine": "Pricing Engine",
             "ratelimiter": "Rate Limiter", "shoppingcart": "Shopping Cart",
             "snakeladder": "Snake and Ladder", "splitwise": "Splitwise",
             "tictactoe": "Tic Tac Toe", "vendingmachine": "Vending Machine"}
    lines = ["---", "layout: default", "title: 'Low Level Design'", "---", "",
             "# Low Level Design", "", "20 complete LLD systems in Java.", "",
             "| System | Description |", "|--------|-------------|"]
    for s in sorted(descs.keys()):
        if not os.path.isdir(os.path.join(LLD_SRC, s)): continue
        link = "[" + LLD_TITLE_MAP.get(s, s) + "]({{ site.baseurl }}/lld/" + s + "/)"
        lines.append("| " + link + " | " + descs[s] + " |")
    lines.append("")
    op = os.path.join(SITE_DIR, "lld/index.md")
    os.makedirs(os.path.dirname(op), exist_ok=True)
    with open(op, "w") as f: f.write(nl.join(lines))
    print("  OK lld/index.md")


def gen_hld_idx():
    nl = chr(10)
    lines = ["---", "layout: default", "title: 'High Level Design'", "---", "",
             "# High Level Design", "", "7 detailed HLD case studies.", "",
             "| System | Description |", "|--------|-------------|"]
    for fn in sorted(os.listdir(HLD_SRC)):
        if not fn.endswith(".md") or fn == "README.md": continue
        name = fn[:-3]; title = HLD_TITLE_MAP.get(name, to_title(name))
        link = "[" + title + "]({{ site.baseurl }}/hld/" + name + "/)"
        lines.append("| " + link + " | System design for " + title + " |")
    lines.append("")
    op = os.path.join(SITE_DIR, "hld/index.md")
    os.makedirs(os.path.dirname(op), exist_ok=True)
    with open(op, "w") as f: f.write(nl.join(lines))
    print("  OK hld/index.md")


def gen_dp_idx():
    nl = chr(10)
    lines = ["---", "layout: default", "title: 'Design Patterns'", "---", "",
             "# Design Patterns", "",
             "Common patterns: Singleton, Factory, Builder, Observer, Strategy, State, Decorator, Command.", "",
             "| Pattern | Where Used |", "|---------|-----------|",
             "| Singleton | ThreadPool, DistributedUniqueIDGenerator |",
             "| Strategy | RateLimiter |", "| State | VendingMachine |",
             "| Observer | NotificationSystem |", ""]
    op = os.path.join(SITE_DIR, "design-patterns/index.md")
    os.makedirs(os.path.dirname(op), exist_ok=True)
    with open(op, "w") as f: f.write(nl.join(lines))
    print("  OK design-patterns/index.md")


if __name__ == "__main__":
    print("Generating coding pages...")
    n = gen_coding(); print(str(n) + " coding pages")
    print("Generating LLD pages...")
    n = gen_lld(); print(str(n) + " LLD pages")
    print("Generating HLD pages...")
    n = gen_hld(); print(str(n) + " HLD pages")
    print("Generating index pages...")
    gen_coding_idx(); gen_lld_idx(); gen_hld_idx(); gen_dp_idx()
    print("Done!")
