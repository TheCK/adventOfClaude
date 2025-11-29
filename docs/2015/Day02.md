# Day 2: I Was Told There Would Be No Math

[Problem Link](https://adventofcode.com/2015/day/2)

## Problem Summary

### Part One
The elves need to order wrapping paper for presents. Each present is a box with dimensions length (l), width (w), and height (h).

The required wrapping paper is:
- Surface area: `2*l*w + 2*w*h + 2*h*l`
- Plus extra slack: area of the smallest side

Calculate the total square feet of wrapping paper needed for all presents.

**Examples:**
- `2x3x4` requires `2*6 + 2*12 + 2*8 = 52` square feet plus `6` square feet of slack = `58` total
- `1x1x10` requires `2*1 + 2*10 + 2*10 = 42` square feet plus `1` square foot of slack = `43` total

### Part Two
The elves also need ribbon. The ribbon required for each present is:
- Wrap ribbon: shortest perimeter (smallest distance around any face)
- Bow ribbon: cubic volume of the present

Calculate the total feet of ribbon needed for all presents.

**Examples:**
- `2x3x4` requires `2+2+3+3 = 10` feet to wrap plus `2*3*4 = 24` feet for bow = `34` total
- `1x1x10` requires `1+1+1+1 = 4` feet to wrap plus `1*1*10 = 10` feet for bow = `14` total

## Solution Approach

### Part One
This is a geometry problem involving calculating surface areas:
1. Parse each line as dimensions in format `lxwxh`
2. Calculate the three side areas: `l*w`, `w*h`, `h*l`
3. Surface area = `2 * (side1 + side2 + side3)`
4. Find the minimum side area for slack
5. Sum total = surface area + smallest side
6. Accumulate totals for all presents

**Time Complexity:** O(n) where n is the number of presents
**Space Complexity:** O(1) - only storing counters

### Part Two
This requires finding the smallest perimeter and calculating volume:
1. Parse dimensions as before
2. Find the two smallest dimensions (exclude the largest)
3. Calculate perimeter: `2 * (smallest1 + smallest2)`
4. Calculate volume: `l * w * h`
5. Total ribbon = perimeter + volume
6. Accumulate totals for all presents

**Time Complexity:** O(n) where n is the number of presents
**Space Complexity:** O(1) - only storing counters

## Implementation Details

The solution is implemented in `Day02.java` with two separate methods:

### Part One Implementation
```java
protected void runPartOne(final Scanner in) {
    int totalPaper = 0;

    while (in.hasNextLine()) {
        String line = in.nextLine();
        String[] parts = line.split("x");
        int l = Integer.parseInt(parts[0]);
        int w = Integer.parseInt(parts[1]);
        int h = Integer.parseInt(parts[2]);

        int side1 = l * w;
        int side2 = w * h;
        int side3 = h * l;

        int surfaceArea = 2 * side1 + 2 * side2 + 2 * side3;
        int smallestSide = Math.min(side1, Math.min(side2, side3));

        totalPaper += surfaceArea + smallestSide;
    }

    print(totalPaper);
}
```

### Part Two Implementation
```java
protected void runPartTwo(final Scanner in) {
    int totalRibbon = 0;

    while (in.hasNextLine()) {
        String line = in.nextLine();
        String[] parts = line.split("x");
        int l = Integer.parseInt(parts[0]);
        int w = Integer.parseInt(parts[1]);
        int h = Integer.parseInt(parts[2]);

        // Find the two smallest dimensions by excluding the largest
        int max = Math.max(l, Math.max(w, h));
        int perimeter;
        if (max == l) {
            perimeter = 2 * (w + h);
        } else if (max == w) {
            perimeter = 2 * (l + h);
        } else {
            perimeter = 2 * (l + w);
        }

        int volume = l * w * h;
        totalRibbon += perimeter + volume;
    }

    print(totalRibbon);
}
```

## Key Takeaways

1. **Input Parsing:** Split string on "x" delimiter and parse integers
2. **Geometry Formulas:** Surface area = sum of all face areas doubled; perimeter = 2*(side1 + side2)
3. **Finding Minimum:** Use nested `Math.min()` to find smallest of three values
4. **Finding Two Smallest:** Identify the maximum value and use the other two dimensions for perimeter calculation
5. **Accumulation Pattern:** Process each present independently and sum the results
