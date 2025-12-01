# Day 1: Secret Entrance

[Problem Link](https://adventofcode.com/2025/day/1)

## Problem Summary

### Part One
You need to open a safe by following rotation instructions for a circular dial numbered 0-99:
- The dial starts at position 50
- Instructions are in format: `L<distance>` (rotate left) or `R<distance>` (rotate right)
- Left rotations move toward lower numbers, right rotations toward higher numbers
- The dial wraps around: going left from 0 reaches 99, going right from 99 reaches 0

The password is the number of times the dial **ends at position 0** after completing a rotation.

**Example:**
```
L68  -> position 82
L30  -> position 52
R48  -> position 0  (count: 1)
L5   -> position 95
R60  -> position 55
L55  -> position 0  (count: 2)
L1   -> position 99
L99  -> position 0  (count: 3)
R14  -> position 14
L82  -> position 32
```
Result: **3** times at position 0

### Part Two
Password method 0x434C49434B ("CLICK") counts every time the dial **points at 0 during rotation**, not just at the end:
- Count all crossings of 0 that occur during the rotation movement
- A rotation like `R1000` from position 50 would cross 0 ten times

**Example (same rotations as Part One):**
```
L68  -> ends at 82, crosses 0 once during rotation
L30  -> ends at 52, no crossing
R48  -> ends at 0 (counts as crossing during rotation)
L5   -> ends at 95, no crossing
R60  -> ends at 55, crosses 0 once during rotation
L55  -> ends at 0 (counts as crossing during rotation)
L1   -> ends at 99, no crossing
L99  -> ends at 0 (counts as crossing during rotation)
R14  -> ends at 14, no crossing
L82  -> ends at 32, crosses 0 once during rotation
```
Result: **6** total crossings of 0

## Solution Approach

### Part One
A straightforward simulation with modular arithmetic:
1. Start at position 50
2. For each instruction, parse direction (L/R) and distance
3. Update position using modulo 100 for wrap-around
4. Count when final position equals 0

**Time Complexity:** O(n) where n is the number of instructions
**Space Complexity:** O(1) - only tracking position and count

### Part Two
Extended simulation that counts all intermediate crossings:
1. For **right rotations**: crossings = `(position + distance) / 100`
   - This counts how many complete cycles of 100 we make
2. For **left rotations**:
   - If `position > 0` and `distance >= position`: crossings = `((distance - position) / 100) + 1`
     - We cross 0 once to get from position to 0, then potentially more times
   - If `position == 0`: crossings = `distance / 100`
     - Starting at 0, we only count additional crossings after full cycles
   - Otherwise: no crossings

**Time Complexity:** O(n) where n is the number of instructions
**Space Complexity:** O(1) - only tracking position and count

## Implementation Details

The solution is implemented in `Day01.java` with two methods:

### Part One Implementation
```java
protected void runPartOne(final Scanner in) {
    int position = 50;
    int zeroCount = 0;

    while (in.hasNextLine()) {
        String line = in.nextLine().trim();
        if (line.isEmpty()) continue;

        char direction = line.charAt(0);
        int distance = Integer.parseInt(line.substring(1));

        if (direction == 'L') {
            position = (position - distance) % 100;
        } else {
            position = (position + distance) % 100;
        }

        // Handle negative modulo in Java
        if (position < 0) {
            position += 100;
        }

        if (position == 0) {
            zeroCount++;
        }
    }

    print(zeroCount);
}
```

### Part Two Implementation
```java
protected void runPartTwo(final Scanner in) {
    int position = 50;
    int zeroCount = 0;

    while (in.hasNextLine()) {
        String line = in.nextLine().trim();
        if (line.isEmpty()) continue;

        char direction = line.charAt(0);
        int distance = Integer.parseInt(line.substring(1));

        if (direction == 'L') {
            // Count how many times we cross 0 during left rotation
            if (position > 0 && distance >= position) {
                zeroCount += ((distance - position) / 100) + 1;
            } else if (position == 0 && distance > 0) {
                zeroCount += distance / 100;
            }
            position = (position - distance) % 100;
        } else {
            // Count how many times we cross 0 during right rotation
            zeroCount += (position + distance) / 100;
            position = (position + distance) % 100;
        }

        // Handle negative modulo in Java
        if (position < 0) {
            position += 100;
        }
    }

    print(zeroCount);
}
```

## Key Takeaways

1. **Modular Arithmetic:** Using `% 100` handles the circular nature of the dial elegantly
2. **Java Modulo Quirk:** Java's modulo operator returns negative values for negative inputs, requiring special handling
3. **Counting Crossings:** For Part Two, integer division `(position + distance) / 100` efficiently counts how many times we pass through 0
4. **Edge Cases:**
   - Starting at 0 requires special logic to avoid double-counting
   - Large rotations (e.g., R1000) work correctly with the division approach
5. **Simple vs Complex:** Part One only checks final positions, while Part Two requires understanding the rotation path
