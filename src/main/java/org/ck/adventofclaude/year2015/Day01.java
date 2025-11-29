package org.ck.adventofclaude.year2015;

import java.util.Scanner;
import org.ck.adventofclaude.util.AOCSolution;

public class Day01 extends AOCSolution {

  @Override
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

  @Override
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
}
