# Day 6: Trash Compactor

[Problem Link](https://adventofcode.com/2025/day/6)

## Problem Summary

### Part One
After jumping into a garbage chute, you find yourself in a trash compactor where a family of cephalopods asks for help with math homework. The worksheet contains math problems arranged in an unusual format:

- Problems are arranged vertically in columns
- Each column represents a single problem
- Numbers appear vertically in each column (can be multi-digit)
- The operator (* or +) appears at the bottom of each column
- Problems are separated by columns containing only spaces

**Example:**
```
123 328  51 64
 45 64  387 23
  6 98  215 314
*   +   *   +
```

This represents four problems:
- `123 * 45 * 6 = 33210`
- `328 + 64 + 98 = 490`
- `51 * 387 * 215 = 4243455`
- `64 + 23 + 314 = 401`

**Goal:** Find the grand total by summing all problem results: `33210 + 490 + 4243455 + 401 = 4277556`

### Part Two
The cephalopods reveal they forgot to explain how to read their math! Cephalopod math is written **right-to-left** in columns:
- Problems are still separated by all-space columns
- Within each problem region, read columns from right to left
- Each column contains one number with digits arranged top-to-bottom (most significant digit at top)
- The operator is still at the bottom

**Same example, read correctly:**
```
123 328  51 64
 45 64  387 23
  6 98  215 314
*   +   *   +
```

Reading right-to-left within each problem region:
- Rightmost problem: `4 + 431 + 623 = 1058`
- Second from right: `175 * 581 * 32 = 3253600`
- Third from right: `8 + 248 + 369 = 625`
- Leftmost problem: `356 * 24 * 1 = 8544`

**Goal:** Find the new grand total: `1058 + 3253600 + 625 + 8544 = 3263827`

## Solution Approach

### Part One - Horizontal Reading
The key is to identify problem regions and parse numbers horizontally:

1. Read all input lines
2. Find the maximum line width
3. Scan through columns to identify problem regions (separated by all-space columns)
4. For each problem region:
   - Extract numbers by reading horizontally across each row, trimming whitespace
   - Extract the operator from the last row
5. Calculate each problem's result (multiply or add all numbers)
6. Sum all results

**Algorithm:**
```
col = 0
while col < maxWidth:
    if column is all spaces:
        skip to next column
    else:
        find end of problem region (next all-space column)
        for each line:
            extract substring from problemStart to problemEnd
            if it's a number, add to numbers list
            if it's an operator, store it
        calculate result using operator
        add to grand total
        col = problemEnd
```

**Time Complexity:** O(w × h) where:
- w = width of input (number of columns)
- h = height of input (number of rows)

**Space Complexity:** O(w × h) for storing all lines

### Part Two - Vertical Right-to-Left Reading
The twist requires a different parsing strategy:

1. Identify problem regions the same way (separated by all-space columns)
2. For each problem region:
   - Iterate columns from right to left (from `problemEnd - 1` down to `problemStart`)
   - For each column, read digits top-to-bottom to build a number
   - Extract the operator (still at the bottom)
3. Calculate each problem's result
4. Sum all results

**Algorithm:**
```
for each problem region:
    for col from (problemEnd - 1) down to problemStart:
        number = ""
        for each line:
            if char at col is a digit:
                append to number
            if char at col is an operator:
                store operator
        if number is not empty:
            add to numbers list
    calculate result using operator
    add to grand total
```

**Time Complexity:** O(w × h)

**Space Complexity:** O(w × h)

## Implementation Details

The solution is implemented in `Day06.java` with a unified method that switches behavior based on a boolean flag.

### Key Implementation Features

```java
private void run(final Scanner in, final boolean rightToLeft) {
    // Read all input lines
    List<String> lines = new ArrayList<>();
    while (in.hasNextLine()) {
        lines.add(in.nextLine());
    }

    // Find maximum width
    int maxWidth = lines.stream().mapToInt(String::length).max().orElse(0);

    long grandTotal = 0;

    // Process each problem region...
}
```

### Problem Region Detection
```java
// Skip separator columns (all spaces)
boolean isAllSpaces = true;
for (String line : lines) {
    if (col < line.length() && line.charAt(col) != ' ') {
        isAllSpaces = false;
        break;
    }
}

if (isAllSpaces) {
    col++;
    continue;
}

// Find end of problem region
int problemStart = col;
int problemEnd = col;

while (problemEnd < maxWidth) {
    boolean allSpaces = true;
    for (String line : lines) {
        if (problemEnd < line.length() && line.charAt(problemEnd) != ' ') {
            allSpaces = false;
            break;
        }
    }
    if (allSpaces) {
        break;
    }
    problemEnd++;
}
```

### Part Two: Right-to-Left Column Reading
```java
if (rightToLeft) {
    // Read columns right-to-left, digits top-to-bottom
    for (int c = problemEnd - 1; c >= problemStart; c--) {
        StringBuilder number = new StringBuilder();

        for (String line : lines) {
            if (c < line.length()) {
                char ch = line.charAt(c);
                if (Character.isDigit(ch)) {
                    number.append(ch);
                } else if (ch == '*' || ch == '+') {
                    operator = ch;
                }
            }
        }

        if (number.length() > 0) {
            numbers.add(Long.parseLong(number.toString()));
        }
    }
}
```

### Part One: Horizontal Reading
```java
else {
    // Read horizontally
    for (String line : lines) {
        int start = problemStart;
        int end = Math.min(problemEnd, line.length());
        if (start < end) {
            String segment = line.substring(start, end).trim();

            if (!segment.isEmpty()) {
                if (segment.equals("*") || segment.equals("+")) {
                    operator = segment.charAt(0);
                } else {
                    try {
                        numbers.add(Long.parseLong(segment));
                    } catch (NumberFormatException e) {
                        // Ignore non-numeric segments
                    }
                }
            }
        }
    }
}
```

### Calculation
```java
// Calculate result for this problem
if (!numbers.isEmpty() && operator != null) {
    long result = numbers.get(0);
    for (int i = 1; i < numbers.size(); i++) {
        if (operator == '*') {
            result *= numbers.get(i);
        } else if (operator == '+') {
            result += numbers.get(i);
        }
    }
    grandTotal += result;
}
```

## Key Takeaways

1. **Two-Dimensional Parsing:** This problem requires careful 2D grid parsing with attention to column boundaries and varying line lengths.

2. **Reading Direction Matters:** The same data can represent completely different values depending on reading direction (horizontal vs vertical, left-to-right vs right-to-left).

3. **Separator Detection:** Using all-space columns as separators requires checking every row in the column to confirm it's truly empty.

4. **Variable Line Lengths:** Real input has varying line lengths (trailing spaces may be omitted). Always check `if (col < line.length())` before accessing characters.

5. **Multi-Digit Number Parsing:** Part 1 parses multi-digit numbers horizontally (trimming whitespace), while Part 2 builds them vertically digit-by-digit.

6. **StringBuilder for Vertical Numbers:** When building numbers from individual digit characters top-to-bottom, StringBuilder is perfect for accumulating the digits.

7. **Long vs Int:** Large multiplication results require `long` to avoid overflow.

8. **Unified Solution:** Both parts share the same problem region detection logic, differing only in how they extract numbers from each region.

9. **Edge Cases:** Handle empty lines, lines shorter than expected, and regions with no valid numbers gracefully.

10. **Character Classification:** Use `Character.isDigit()` for robust digit detection instead of manual range checking.

## Results

- **Part One:** 6,343,365,546,996 (horizontal reading)
- **Part Two:** 11,136,895,955,912 (right-to-left vertical reading)
