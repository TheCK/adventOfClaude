package org.ck.adventofclaude.year2025;

import java.util.Scanner;
import org.ck.adventofclaude.util.AOCSolution;

public class Day01 extends AOCSolution {

  @Override
  protected void runPartOne(final Scanner in) {
    int position = 50;
    int zeroCount = 0;

    while (in.hasNextLine()) {
      String line = in.nextLine().trim();
      if (line.isEmpty()) {
        continue;
      }

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

  @Override
  protected void runPartTwo(final Scanner in) {
    int position = 50;
    int zeroCount = 0;

    while (in.hasNextLine()) {
      String line = in.nextLine().trim();
      if (line.isEmpty()) {
        continue;
      }

      char direction = line.charAt(0);
      int distance = Integer.parseInt(line.substring(1));

      if (direction == 'L') {
        // Count how many times we cross 0 during left rotation
        if (position > 0 && distance >= position) {
          // We cross 0 at least once, then every 100 clicks after that
          zeroCount += ((distance - position) / 100) + 1;
        } else if (position == 0 && distance > 0) {
          // Starting at 0, we cross 0 again every 100 clicks
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
}
