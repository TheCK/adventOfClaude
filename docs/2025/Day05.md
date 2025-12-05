# Day 5: Cafeteria

[Problem Link](https://adventofcode.com/2025/day/5)

## Problem Summary

### Part One
The cafeteria's new inventory management system tracks fresh ingredient ID ranges and a list of available ingredient IDs. The database consists of:
1. Fresh ingredient ID ranges (inclusive ranges like `3-5` meaning IDs 3, 4, 5)
2. A blank line separator
3. Available ingredient IDs to check

Ranges can overlap - an ingredient ID is fresh if it falls into **any** range.

**Goal:** Count how many of the available ingredient IDs are fresh.

**Example:**
```
3-5
10-14
16-20
12-18

1
5
8
11
17
32
```

- ID 1: spoiled (not in any range)
- ID 5: fresh (in range 3-5)
- ID 8: spoiled
- ID 11: fresh (in range 10-14)
- ID 17: fresh (in ranges 16-20 and 12-18)
- ID 32: spoiled

**Answer:** **3** fresh ingredients

### Part Two
Now the Elves want to know **all** ingredient IDs that the fresh ranges consider to be fresh, regardless of what's in stock. The available ingredient IDs list is now irrelevant.

**Goal:** Count the total number of unique ingredient IDs covered by all fresh ranges.

Using the same ranges:
```
3-5
10-14
16-20
12-18
```

Fresh IDs: 3, 4, 5, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20

Note that ranges `12-18` and `16-20` overlap, covering IDs 12-20 (the union).

**Answer:** **14** total fresh ingredient IDs

## Solution Approach

### Part One
A straightforward membership check for each available ingredient ID:

1. Parse fresh ranges into a list of Range objects
2. Parse available ingredient IDs into a list
3. For each ID, check if it falls within any range
4. Count the IDs that are fresh

**Algorithm:**
```
for each ingredientId in availableIds:
    isFresh = false
    for each range in freshRanges:
        if ingredientId >= range.start AND ingredientId <= range.end:
            isFresh = true
            break
    if isFresh:
        count++
```

**Time Complexity:** O(n × m) where:
- n = number of available ingredient IDs to check
- m = number of fresh ranges

**Space Complexity:** O(n + m) for storing IDs and ranges

### Part Two
This requires counting all unique IDs covered by the ranges. The key insight is to **merge overlapping or adjacent ranges** first, then sum their sizes.

**Why merge ranges?** If we have ranges `3-5` and `4-7`, we can't just add `(5-3+1) + (7-4+1) = 3 + 4 = 7` because IDs 4 and 5 are counted twice. After merging to `3-7`, we get the correct count: `7-3+1 = 5`.

**Algorithm:**
```
1. Sort ranges by start position
2. Merge overlapping/adjacent ranges:
   - Two ranges overlap if: next.start <= current.end + 1
   - Merged range: [current.start, max(current.end, next.end)]
3. Sum the size of each merged range: (end - start + 1)
```

**Example merging:**
- Input: `3-5`, `10-14`, `16-20`, `12-18`
- After sorting: `3-5`, `10-14`, `12-18`, `16-20`
- Merging:
  - Start with `3-5`
  - `10-14`: not adjacent (10 > 5+1), add `3-5` to merged list, current = `10-14`
  - `12-18`: overlaps (12 <= 14+1), merge to `10-18`
  - `16-20`: overlaps (16 <= 18+1), merge to `10-20`
  - Final merged: `3-5`, `10-20`
- Count: `(5-3+1) + (20-10+1) = 3 + 11 = 14`

**Time Complexity:** O(m log m + m) where m = number of ranges
- O(m log m) for sorting
- O(m) for merging

**Space Complexity:** O(m) for storing ranges and merged list

## Implementation Details

The solution is implemented in `Day05.java` with separate methods for each part.

### Part One Implementation
```java
@Override
protected void runPartOne(final Scanner in) {
    List<Range> freshRanges = new ArrayList<>();
    List<Long> ingredientIds = new ArrayList<>();

    // Parse fresh ranges (lines with "-")
    while (in.hasNextLine()) {
        String line = in.nextLine().trim();
        if (line.isEmpty()) {
            break; // Blank line separates ranges from ingredient IDs
        }
        String[] parts = line.split("-");
        long start = Long.parseLong(parts[0]);
        long end = Long.parseLong(parts[1]);
        freshRanges.add(new Range(start, end));
    }

    // Parse ingredient IDs
    while (in.hasNextLine()) {
        String line = in.nextLine().trim();
        if (!line.isEmpty()) {
            ingredientIds.add(Long.parseLong(line));
        }
    }

    // Count fresh ingredients
    long freshCount = ingredientIds.stream()
        .filter(id -> isFresh(id, freshRanges))
        .count();

    print(freshCount);
}

private boolean isFresh(long id, List<Range> ranges) {
    return ranges.stream().anyMatch(range -> range.contains(id));
}

private record Range(long start, long end) {
    boolean contains(long value) {
        return value >= start && value <= end;
    }
}
```

### Part Two Implementation
```java
@Override
protected void runPartTwo(final Scanner in) {
    List<Range> freshRanges = new ArrayList<>();

    // Parse fresh ranges (lines with "-")
    while (in.hasNextLine()) {
        String line = in.nextLine().trim();
        if (line.isEmpty()) {
            break; // Blank line ends the ranges section
        }
        String[] parts = line.split("-");
        long start = Long.parseLong(parts[0]);
        long end = Long.parseLong(parts[1]);
        freshRanges.add(new Range(start, end));
    }

    // Merge overlapping ranges and count total IDs
    long totalFreshIds = countTotalFreshIds(freshRanges);

    print(totalFreshIds);
}

private long countTotalFreshIds(List<Range> ranges) {
    if (ranges.isEmpty()) {
        return 0;
    }

    // Sort ranges by start position
    ranges.sort((r1, r2) -> Long.compare(r1.start, r2.start));

    // Merge overlapping/adjacent ranges
    List<Range> merged = new ArrayList<>();
    Range current = ranges.get(0);

    for (int i = 1; i < ranges.size(); i++) {
        Range next = ranges.get(i);
        if (next.start <= current.end + 1) {
            // Overlapping or adjacent - merge them
            current = new Range(current.start, Math.max(current.end, next.end));
        } else {
            // No overlap - add current to merged list and start new range
            merged.add(current);
            current = next;
        }
    }
    merged.add(current); // Add the last range

    // Count total IDs in merged ranges
    return merged.stream().mapToLong(r -> r.end - r.start + 1).sum();
}
```

## Key Takeaways

1. **Range Problems:** Many problems involving ranges can be solved efficiently by sorting and merging overlapping intervals. This is a classic algorithmic pattern.

2. **Adjacent vs Overlapping:** When merging ranges, remember that adjacent ranges (e.g., `1-5` and `6-10`) should also be merged since they form a continuous sequence. The condition `next.start <= current.end + 1` handles both overlapping and adjacent cases.

3. **Inclusive Ranges:** The ranges in this problem are inclusive on both ends. This affects the size calculation: a range from 3 to 5 includes 3 elements (3, 4, 5), so the formula is `end - start + 1`, not `end - start`.

4. **Long vs Int:** The real input contains very large numbers (in the trillions). Using `long` instead of `int` is essential to avoid `NumberFormatException`.

5. **Stream API:** Java's Stream API provides elegant solutions for filtering and counting. The `anyMatch()` method is perfect for checking if an ID falls within any range.

6. **Record Classes:** Java records provide a concise way to create immutable data classes. The `Range` record encapsulates start/end values and the `contains()` logic cleanly.

7. **Greedy Merging:** The merging algorithm is greedy - it extends the current range as far as possible before starting a new one. This works because the ranges are sorted by start position.

8. **Two Different Questions:** Part One asks "which items are fresh?" (subset checking), while Part Two asks "how many items could be fresh?" (range coverage). The same data structure (ranges) enables both solutions efficiently.

9. **Empty Line as Delimiter:** The input format uses a blank line to separate sections. Careful parsing with `trim()` and `isEmpty()` checks ensures robust handling of the input format.

10. **Mutation Consideration:** Part Two could modify the original ranges list (sorting), so if you needed to preserve the original order, you'd create a copy first. In this case, we don't need the original order after sorting.

## Results

- **Part One:** 558 fresh ingredients
- **Part Two:** 344,813,017,450,467 total fresh ingredient IDs
