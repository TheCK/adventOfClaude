package org.ck.adventofclaude.year2025;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.ck.adventofclaude.util.AOCSolution;

public class Day06 extends AOCSolution {

  @Override
  protected void runPartOne(final Scanner in) {
    run(in, false);
  }

  @Override
  protected void runPartTwo(final Scanner in) {
    run(in, true);
  }

  private void run(final Scanner in, final boolean rightToLeft) {
    List<String> lines = new ArrayList<>();
    while (in.hasNextLine()) {
      lines.add(in.nextLine());
    }

    if (lines.isEmpty()) {
      print(0);
      return;
    }

    // Find the maximum width to handle all columns
    int maxWidth = lines.stream().mapToInt(String::length).max().orElse(0);

    long grandTotal = 0;

    // Find problem regions (groups of columns separated by all-space columns)
    int col = 0;
    while (col < maxWidth) {
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

      // Found start of a problem region - find its end
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

      // Extract numbers and operator from this problem region
      List<Long> numbers = new ArrayList<>();
      Character operator = null;

      if (rightToLeft) {
        // Part 2: Read columns right-to-left, digits top-to-bottom
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
      } else {
        // Part 1: Read horizontally
        for (String line : lines) {
          // Extract the substring for this problem
          int start = problemStart;
          int end = Math.min(problemEnd, line.length());
          if (start < end) {
            String segment = line.substring(start, end).trim();

            if (!segment.isEmpty()) {
              // Check if it's an operator
              if (segment.equals("*") || segment.equals("+")) {
                operator = segment.charAt(0);
              } else {
                // Try to parse as number
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

      col = problemEnd;
    }

    print(grandTotal);
  }
}
