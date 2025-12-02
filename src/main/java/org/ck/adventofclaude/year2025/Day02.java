package org.ck.adventofclaude.year2025;

import java.util.Scanner;
import org.ck.adventofclaude.util.AOCSolution;

public class Day02 extends AOCSolution {

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
}
