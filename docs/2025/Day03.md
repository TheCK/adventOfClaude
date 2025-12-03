# Day 3: Battery Bank Joltage

[Problem Link](https://adventofcode.com/2025/day/3)

## Problem Summary

### Part One
You need to power an escalator using batteries arranged in banks. Each bank is a line of digits representing battery joltage ratings (1-9). To activate a bank, you must select exactly **two batteries** while maintaining their order. The joltage produced is the two-digit number formed by the selected batteries.

**Goal:** Find the maximum joltage for each bank and sum them.

**Example:**
```
987654321111111
811111111111119
234234234234278
818181911112111
```

**Maximum joltages:**
- `987654321111111` → **98** (first two batteries: 9 and 8)
- `811111111111119` → **89** (battery 8 and battery 9 at the end)
- `234234234234278` → **78** (batteries 7 and 8 at the end)
- `818181911112111` → **92** (battery 9, then best option after it: 2)

**Total:** 98 + 89 + 78 + 92 = **357**

### Part Two
After hitting the "joltage limit safety override" button, you now need to select exactly **twelve batteries** from each bank (maintaining their order). The joltage is now a 12-digit number.

**Example (same banks):**
```
987654321111111
811111111111119
234234234234278
818181911112111
```

**Maximum joltages:**
- `987654321111111` → **987654321111** (skip some 1s at the end)
- `811111111111119` → **811111111119** (skip some 1s in the middle)
- `234234234234278` → **434234234278** (skip 2, 3, and 2 near the start)
- `818181911112111` → **888911112111** (skip some 1s near the front)

**Total:** 987654321111 + 811111111119 + 434234234278 + 888911112111 = **3121910778619**

## Solution Approach

### Part One
For each bank, find the maximum two-digit number by trying each position as the first battery:

1. Iterate through each possible position for the first battery (0 to n-2)
2. For each first battery position, find the maximum digit after it
3. Form the two-digit number and track the maximum
4. Sum all maximum joltages across banks

**Algorithm:**
```
for each position i from 0 to length-2:
    maxAfter = maximum digit from position i+1 to end
    joltage = digit[i] * 10 + maxAfter
    maxJoltage = max(maxJoltage, joltage)
```

**Time Complexity:** O(n × m²) where:
- n = number of banks
- m = average length of each bank

**Space Complexity:** O(1)

### Part Two
Uses a **greedy algorithm** to maximize the 12-digit number:

1. For each of the 12 positions in the result:
   - Calculate how many more digits are needed after this position
   - Determine the search range (must leave enough digits for remaining positions)
   - Find the maximum digit in that range
   - Append it to the result
   - Move past that digit in the original string

2. This ensures we get the lexicographically largest 12-digit number possible

**Why Greedy Works:** For a fixed-length selection problem, choosing the maximum digit as early as possible always produces the optimal result. Once we commit to a digit at position i, we want the best possible choices for all remaining positions.

**Algorithm:**
```
result = ""
currentIndex = 0
for position from 0 to 11:
    digitsRemaining = 12 - position
    searchEnd = bankLength - digitsRemaining

    maxDigit = maximum digit from currentIndex to searchEnd
    result += maxDigit
    currentIndex = index of maxDigit + 1
```

**Time Complexity:** O(n × k × m) where:
- n = number of banks
- k = number of batteries to select (12)
- m = average length of each bank

**Space Complexity:** O(k) for the result string

## Implementation Details

The solution is implemented in `Day03.java` with separate methods for each part:

### Part One Implementation
```java
@Override
protected void runPartOne(final Scanner in) {
    long totalJoltage = 0;

    while (in.hasNextLine()) {
        String bank = in.nextLine();
        int maxJoltage = findMaxJoltage(bank);
        totalJoltage += maxJoltage;
    }

    print(totalJoltage);
}

private int findMaxJoltage(String bank) {
    int maxJoltage = 0;

    // Try each position as the first battery
    for (int i = 0; i < bank.length() - 1; i++) {
        // Find the maximum digit after position i for the second battery
        char maxAfter = '0';
        for (int j = i + 1; j < bank.length(); j++) {
            if (bank.charAt(j) > maxAfter) {
                maxAfter = bank.charAt(j);
            }
        }

        // Form the two-digit number
        int joltage = (bank.charAt(i) - '0') * 10 + (maxAfter - '0');
        maxJoltage = Math.max(maxJoltage, joltage);
    }

    return maxJoltage;
}
```

### Part Two Implementation
```java
@Override
protected void runPartTwo(final Scanner in) {
    long totalJoltage = 0;

    while (in.hasNextLine()) {
        String bank = in.nextLine();
        long maxJoltage = findMaxJoltageWithTwelveBatteries(bank);
        totalJoltage += maxJoltage;
    }

    print(totalJoltage);
}

private long findMaxJoltageWithTwelveBatteries(String bank) {
    StringBuilder result = new StringBuilder();
    int currentIndex = 0;
    int batteriesNeeded = 12;

    for (int position = 0; position < batteriesNeeded; position++) {
        int digitsRemaining = batteriesNeeded - position;
        // We can search up to the index where exactly digitsRemaining digits are left
        int searchEnd = bank.length() - digitsRemaining;

        // Find the maximum digit in the range [currentIndex, searchEnd]
        char maxDigit = '0';
        int maxIndex = currentIndex;

        for (int i = currentIndex; i <= searchEnd; i++) {
            if (bank.charAt(i) > maxDigit) {
                maxDigit = bank.charAt(i);
                maxIndex = i;
            }
        }

        result.append(maxDigit);
        currentIndex = maxIndex + 1;
    }

    return Long.parseLong(result.toString());
}
```

## Key Takeaways

1. **Greedy Algorithms:** Part Two demonstrates how greedy algorithms work perfectly for certain optimization problems - when making the locally optimal choice at each step guarantees a globally optimal solution.

2. **Character Comparison:** Using character comparison (`bank.charAt(j) > maxAfter`) is more efficient than converting to integers for digit comparisons.

3. **Search Range Calculation:** The key insight in Part Two is calculating the search range boundary: we must leave at least `digitsRemaining` digits after our current choice to ensure we can select all 12 batteries.

4. **Long vs Int:** Part One results fit in `int` (max 99), but Part Two produces 12-digit numbers requiring `long` type.

5. **String Building:** Using `StringBuilder` for the 12-digit result is more efficient than string concatenation in a loop.

6. **Scalability:** The greedy algorithm in Part Two can easily be extended to select any number of batteries by changing the `batteriesNeeded` constant.

7. **Order Preservation:** The problem requires maintaining the original order of batteries, which makes it a selection problem rather than a permutation problem - significantly simplifying the solution.
