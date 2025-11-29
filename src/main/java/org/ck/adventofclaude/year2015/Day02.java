package org.ck.adventofclaude.year2015;

import java.util.Scanner;
import org.ck.adventofclaude.util.AOCSolution;

public class Day02 extends AOCSolution {

  @Override
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

  @Override
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
}
