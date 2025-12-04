# Day 4: Printing Department

[Problem Link](https://adventofcode.com/2025/day/4)

## Problem Summary

### Part One
The printing department has large rolls of paper (@) arranged on a grid. Forklifts can only access a roll of paper if there are **fewer than four rolls of paper** in the eight adjacent positions (up, down, left, right, and four diagonals).

**Goal:** Count how many rolls of paper can be accessed by forklifts.

**Example:**
```
..@@.@@@@.
@@@.@.@.@@
@@@@@.@.@@
@.@@@@..@.
@@.@@@@.@@
.@@@@@@@.@
.@.@.@.@@@
@.@@@.@@@@
.@@@@@@@@.
@.@.@@@.@.
```

Accessible rolls (marked with x):
```
..xx.xx@x.
x@@.@.@.@@
@@@@@.x.@@
@.@@@@..@.
x@.@@@@.@x
.@@@@@@@.@
.@.@.@.@@@
x.@@@.@@@@
.@@@@@@@@.
x.x.@@@.x.
```

**Answer:** **13** accessible rolls

### Part Two
Once a roll of paper is accessed and removed, other rolls might become accessible (since adjacency counts change). The forklifts keep removing accessible rolls until no more can be removed.

**Goal:** Count the total number of rolls removed through this iterative process.

Using the same example grid, the process unfolds as:
1. Remove 13 accessible rolls
2. Remove 12 more rolls (now accessible after first removal)
3. Remove 7 more rolls
4. Remove 5 more rolls
5. Remove 2 more rolls
6. Remove 1 more roll
7. Remove 1 more roll
8. Remove 1 more roll
9. Stop (no more accessible rolls)

**Total:** **43** rolls removed

## Solution Approach

### Part One
For each paper roll (@) in the grid:

1. Count the number of adjacent paper rolls in all 8 directions
2. If the count is less than 4, the roll is accessible
3. Sum all accessible rolls

**Algorithm:**
```
for each position (row, col) in grid:
    if grid[row][col] == '@':
        adjacentCount = 0
        for each direction (dr, dc) in 8 directions:
            if grid[row+dr][col+dc] == '@':
                adjacentCount++

        if adjacentCount < 4:
            accessibleCount++
```

**Time Complexity:** O(n × m) where n and m are grid dimensions (each cell checked once, 8 directions is constant)

**Space Complexity:** O(n × m) for storing the grid

### Part Two
Uses an **iterative removal process**:

1. Find all accessible rolls in the current state (< 4 adjacent rolls)
2. Remove all of them simultaneously (convert '@' to '.')
3. Repeat until no more rolls can be removed
4. Track the cumulative count of removed rolls

**Why Simultaneous Removal?** Removing rolls one at a time would change adjacency counts during a single iteration, leading to incorrect results. All accessible rolls in each iteration must be identified first, then removed together.

**Algorithm:**
```
totalRemoved = 0
changed = true

while changed:
    toRemove = []

    for each position (row, col) in grid:
        if grid[row][col] == '@':
            adjacentCount = count adjacent '@' symbols
            if adjacentCount < 4:
                toRemove.add((row, col))

    if toRemove is not empty:
        for each position in toRemove:
            grid[position] = '.'
            totalRemoved++
        changed = true
    else:
        changed = false
```

**Time Complexity:** O(k × n × m) where:
- k = number of iterations (removal rounds)
- n × m = grid dimensions

In the worst case, k could be O(min(n, m)), making overall complexity O(n² × m) or O(n × m²)

**Space Complexity:** O(n × m) for the mutable grid

## Implementation Details

The solution is implemented in `Day04.java` with separate methods for each part:

### Part One Implementation
```java
@Override
protected void runPartOne(final Scanner in) {
    List<String> grid = new ArrayList<>();
    while (in.hasNextLine()) {
        grid.add(in.nextLine());
    }

    int accessibleCount = 0;
    int rows = grid.size();
    int cols = grid.get(0).length();

    // Check each position in the grid
    for (int row = 0; row < rows; row++) {
        for (int col = 0; col < cols; col++) {
            if (grid.get(row).charAt(col) == '@') {
                // Count adjacent paper rolls
                int adjacentRolls = 0;

                // Check all 8 directions
                for (int dr = -1; dr <= 1; dr++) {
                    for (int dc = -1; dc <= 1; dc++) {
                        if (dr == 0 && dc == 0) continue; // Skip the center cell

                        int newRow = row + dr;
                        int newCol = col + dc;

                        // Check bounds
                        if (newRow >= 0 && newRow < rows &&
                            newCol >= 0 && newCol < cols) {
                            if (grid.get(newRow).charAt(newCol) == '@') {
                                adjacentRolls++;
                            }
                        }
                    }
                }

                // If fewer than 4 adjacent rolls, it's accessible
                if (adjacentRolls < 4) {
                    accessibleCount++;
                }
            }
        }
    }

    print(accessibleCount);
}
```

### Part Two Implementation
```java
@Override
protected void runPartTwo(final Scanner in) {
    List<String> grid = new ArrayList<>();
    while (in.hasNextLine()) {
        grid.add(in.nextLine());
    }

    // Convert to mutable grid (using char arrays)
    int rows = grid.size();
    int cols = grid.get(0).length();
    char[][] mutableGrid = new char[rows][cols];
    for (int row = 0; row < rows; row++) {
        mutableGrid[row] = grid.get(row).toCharArray();
    }

    int totalRemoved = 0;
    boolean changed = true;

    while (changed) {
        changed = false;
        List<int[]> toRemove = new ArrayList<>();

        // Find all accessible rolls in current state
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (mutableGrid[row][col] == '@') {
                    int adjacentRolls = 0;

                    // Check all 8 directions
                    for (int dr = -1; dr <= 1; dr++) {
                        for (int dc = -1; dc <= 1; dc++) {
                            if (dr == 0 && dc == 0) continue;

                            int newRow = row + dr;
                            int newCol = col + dc;

                            if (newRow >= 0 && newRow < rows &&
                                newCol >= 0 && newCol < cols) {
                                if (mutableGrid[newRow][newCol] == '@') {
                                    adjacentRolls++;
                                }
                            }
                        }
                    }

                    // If fewer than 4 adjacent rolls, mark for removal
                    if (adjacentRolls < 4) {
                        toRemove.add(new int[] {row, col});
                    }
                }
            }
        }

        // Remove all marked rolls
        if (!toRemove.isEmpty()) {
            changed = true;
            totalRemoved += toRemove.size();
            for (int[] pos : toRemove) {
                mutableGrid[pos[0]][pos[1]] = '.';
            }
        }
    }

    print(totalRemoved);
}
```

## Key Takeaways

1. **Iterative State Changes:** Part Two demonstrates a common pattern where changes in one iteration affect what's possible in the next iteration. This requires careful tracking of state and termination conditions.

2. **Simultaneous Updates:** When multiple elements depend on the current state, they must all be evaluated before any updates occur. This prevents cascading changes within a single iteration that would produce incorrect results.

3. **Mutable vs Immutable Data Structures:** Part One can use immutable strings, but Part Two requires a mutable grid (char[][]) to efficiently update the state between iterations.

4. **Boundary Checking:** Grid problems require careful boundary validation when checking adjacent cells. The solution checks bounds before accessing array elements to avoid IndexOutOfBoundsException.

5. **Direction Vectors:** Using nested loops with offsets (-1, 0, 1) is a clean way to iterate through all 8 adjacent directions without duplicating code.

6. **Termination Detection:** The `changed` flag ensures the loop stops when no more rolls can be removed, preventing infinite loops.

7. **List for Batch Operations:** Collecting all positions to remove in a list before modifying the grid ensures clean separation between the "find" and "remove" phases.

8. **Edge Effect Propagation:** Removing outer/edge rolls can cause inner rolls to become accessible - this cascading effect is why the iterative approach is necessary and why the total removed (8451) is much larger than the initial accessible count (1395).
