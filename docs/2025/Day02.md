# Day 2: Gift Shop

[Problem Link](https://adventofcode.com/2025/day/2)

## Problem Summary

### Part One
A young Elf accidentally added invalid product IDs to the gift shop database. You need to identify and sum all invalid IDs within given ranges.

An ID is **invalid** if it consists of a digit sequence repeated **exactly twice**:
- `11` (1 repeated twice)
- `6464` (64 repeated twice)
- `123123` (123 repeated twice)
- `101` is valid (not a repeated pattern)

**Input Format:** Comma-separated ranges on a single line:
```
11-22,95-115,998-1012,1188511880-1188511890,222220-222224,1698522-1698528,446443-446449,38593856-38593862
```

**Example Results:**
- `11-22`: Invalid IDs are 11, 22
- `95-115`: Invalid ID is 99
- `998-1012`: Invalid ID is 1010
- `1188511880-1188511890`: Invalid ID is 1188511885
- `222220-222224`: Invalid ID is 222222
- `446443-446449`: Invalid ID is 446446
- `38593856-38593862`: Invalid ID is 38593859

Sum of all invalid IDs in example: **1227775554**

### Part Two
The invalid ID definition is expanded: an ID is **invalid** if it consists of a digit sequence repeated **at least twice**:
- `111` (1 repeated three times)
- `12341234` (1234 repeated twice)
- `123123123` (123 repeated three times)
- `1212121212` (12 repeated five times)
- `1111111` (1 repeated seven times)

**Example Results (same ranges):**
- `11-22`: Invalid IDs are 11, 22 (unchanged)
- `95-115`: Invalid IDs are 99, 111 (added 111)
- `998-1012`: Invalid IDs are 999, 1010 (added 999)
- `565653-565659`: Invalid ID is 565656 (new)
- `824824821-824824827`: Invalid ID is 824824824 (new)
- `2121212118-2121212124`: Invalid ID is 2121212121 (new)

Sum of all invalid IDs in example: **4174379265**

## Solution Approach

### Part One
For each ID in each range, check if it consists of a digit sequence repeated exactly twice:
1. Parse the comma-separated ranges
2. For each range, iterate through all IDs from start to end
3. Check if the ID length is even
4. Split the ID string in half and compare both halves
5. If they're equal, it's invalid - add to sum

**Time Complexity:** O(n × m × k) where:
- n = number of ranges
- m = average range size
- k = average number of digits in IDs

**Space Complexity:** O(k) for string conversions

### Part Two
Extended pattern matching to detect any repeated sequence (not just two repetitions):
1. Parse ranges and iterate through IDs as before
2. For each ID, try all possible pattern lengths from 1 to length/2
3. For each pattern length that evenly divides the total length:
   - Extract the pattern from the beginning
   - Repeat the pattern to match the full length
   - Check if it equals the original ID
4. If any pattern matches, it's invalid - add to sum

**Time Complexity:** O(n × m × k²) where:
- n = number of ranges
- m = average range size
- k = average number of digits (trying k/2 pattern lengths, each requiring k comparisons)

**Space Complexity:** O(k) for string operations

## Implementation Details

The solution is implemented in `Day02.java` with separate methods for each part:

### Part One Implementation
```java
@Override
protected void runPartOne(final Scanner in) {
    String line = in.nextLine();
    String[] ranges = line.split(",");

    long sum = 0;
    for (String range : ranges) {
        String[] parts = range.split("-");
        long start = Long.parseLong(parts[0]);
        long end = Long.parseLong(parts[1]);

        for (long id = start; id <= end; id++) {
            if (isInvalidId(id)) {
                sum += id;
            }
        }
    }

    print(sum);
}

private boolean isInvalidId(long id) {
    String str = String.valueOf(id);
    int len = str.length();

    // Must be even length to be split in half
    if (len % 2 != 0) {
        return false;
    }

    // Split in half and check if both halves are equal
    String firstHalf = str.substring(0, len / 2);
    String secondHalf = str.substring(len / 2);

    return firstHalf.equals(secondHalf);
}
```

### Part Two Implementation
```java
@Override
protected void runPartTwo(final Scanner in) {
    String line = in.nextLine();
    String[] ranges = line.split(",");

    long sum = 0;
    for (String range : ranges) {
        String[] parts = range.split("-");
        long start = Long.parseLong(parts[0]);
        long end = Long.parseLong(parts[1]);

        for (long id = start; id <= end; id++) {
            if (isInvalidIdPartTwo(id)) {
                sum += id;
            }
        }
    }

    print(sum);
}

private boolean isInvalidIdPartTwo(long id) {
    String str = String.valueOf(id);
    int len = str.length();

    // Try all possible pattern lengths from 1 to len/2
    for (int patternLen = 1; patternLen <= len / 2; patternLen++) {
        // Check if the length is divisible by pattern length
        if (len % patternLen == 0) {
            String pattern = str.substring(0, patternLen);
            int repetitions = len / patternLen;

            // Check if repeating the pattern creates the full string
            if (pattern.repeat(repetitions).equals(str)) {
                return true;
            }
        }
    }

    return false;
}
```

## Key Takeaways

1. **String Pattern Matching:** Converting numbers to strings makes pattern detection straightforward
2. **Java String.repeat():** The built-in `repeat()` method (Java 11+) elegantly handles pattern repetition checking
3. **Optimization:** Only checking pattern lengths that evenly divide the total length avoids unnecessary comparisons
4. **Progressive Difficulty:** Part One is a special case of Part Two (exactly 2 repetitions vs. at least 2)
5. **Long Type Usage:** Some IDs exceed `Integer.MAX_VALUE` (2.1 billion), requiring `long` type
6. **Brute Force Viability:** Despite the nested loops, the ranges are small enough that checking each ID individually is efficient
7. **Pattern Length Iteration:** Starting from the smallest pattern (length 1) ensures we catch all valid patterns, including numbers like `1111111` (pattern "1" repeated 7 times)
