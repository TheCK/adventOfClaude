# Day 1: Not Quite Lisp

[Problem Link](https://adventofcode.com/2015/day/1)

## Problem Summary

### Part One
Santa needs to navigate through an apartment building using instructions made of parentheses:
- `(` means go up one floor
- `)` means go down one floor
- Starting floor is 0 (ground floor)

Find the final floor Santa ends up on.

**Examples:**
- `(())` and `()()` result in floor 0
- `(((` and `(()(()(`  result in floor 3
- `())` and `))(` result in floor -1
- `)))` and `)())())` result in floor -3

### Part Two
Find the position (1-indexed) of the first character that causes Santa to enter the basement (floor -1).

**Examples:**
- `)` causes him to enter the basement at position 1
- `()())` causes him to enter the basement at position 5

## Solution Approach

### Part One
This is a straightforward counting problem:
1. Read the entire instruction string
2. Iterate through each character
3. Increment floor counter for `(`, decrement for `)`
4. Return the final floor value

**Time Complexity:** O(n) where n is the length of the instruction string
**Space Complexity:** O(1) - only storing a single counter

### Part Two
Similar to Part One, but we need to track when we first reach floor -1:
1. Read the entire instruction string
2. Iterate through each character, tracking the current floor
3. After processing each character, check if floor == -1
4. If so, return the current position (1-indexed)
5. Continue until we find the first basement entry

**Time Complexity:** O(n) where n is the length of the instruction string
**Space Complexity:** O(1) - only storing a floor counter and position

## Implementation Details

The solution is implemented in `Day01.java` with two separate methods:

### Part One Implementation
```java
protected void runPartOne(final Scanner in) {
    String instructions = in.nextLine();

    int floor = 0;
    for (char c : instructions.toCharArray()) {
        if (c == '(') {
            floor++;
        } else if (c == ')') {
            floor--;
        }
    }

    print(floor);
}
```

### Part Two Implementation
```java
protected void runPartTwo(final Scanner in) {
    String instructions = in.nextLine();

    int floor = 0;
    for (int i = 0; i < instructions.length(); i++) {
        char c = instructions.charAt(i);
        if (c == '(') {
            floor++;
        } else if (c == ')') {
            floor--;
        }

        if (floor == -1) {
            print(i + 1); // Position is 1-indexed
            return;
        }
    }
}
```

## Key Takeaways

1. **Simple State Tracking:** Both parts only require tracking a single integer (current floor)
2. **Early Exit Optimization:** Part Two can return immediately upon finding the first basement entry
3. **Index vs Position:** Remember that character positions are 1-indexed in the problem statement, while array indices are 0-indexed in Java
4. **Input Format:** The entire instruction set is provided as a single line, making it straightforward to process sequentially
