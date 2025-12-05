package org.ck.adventofclaude.year2025;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.ck.adventofclaude.util.AOCSolution;

public class Day05 extends AOCSolution {

  @Override
  protected void runPartOne(final Scanner in) {
    List<Range> freshRanges = new ArrayList<>();
    List<Long> ingredientIds = new ArrayList<>();

    // Parse fresh ranges (lines with "-")
    while (in.hasNextLine()) {
      String line = in.nextLine().trim();
      if (line.isEmpty()) {
        break; // Blank line separates ranges from ingredient IDs
      }
      String[] parts = line.split("-");
      long start = Long.parseLong(parts[0]);
      long end = Long.parseLong(parts[1]);
      freshRanges.add(new Range(start, end));
    }

    // Parse ingredient IDs
    while (in.hasNextLine()) {
      String line = in.nextLine().trim();
      if (!line.isEmpty()) {
        ingredientIds.add(Long.parseLong(line));
      }
    }

    // Count fresh ingredients
    long freshCount = ingredientIds.stream().filter(id -> isFresh(id, freshRanges)).count();

    print(freshCount);
  }

  @Override
  protected void runPartTwo(final Scanner in) {
    List<Range> freshRanges = new ArrayList<>();

    // Parse fresh ranges (lines with "-")
    while (in.hasNextLine()) {
      String line = in.nextLine().trim();
      if (line.isEmpty()) {
        break; // Blank line ends the ranges section
      }
      String[] parts = line.split("-");
      long start = Long.parseLong(parts[0]);
      long end = Long.parseLong(parts[1]);
      freshRanges.add(new Range(start, end));
    }

    // Merge overlapping ranges and count total IDs
    long totalFreshIds = countTotalFreshIds(freshRanges);

    print(totalFreshIds);
  }

  private long countTotalFreshIds(List<Range> ranges) {
    if (ranges.isEmpty()) {
      return 0;
    }

    // Sort ranges by start position
    ranges.sort((r1, r2) -> Long.compare(r1.start, r2.start));

    // Merge overlapping/adjacent ranges
    List<Range> merged = new ArrayList<>();
    Range current = ranges.get(0);

    for (int i = 1; i < ranges.size(); i++) {
      Range next = ranges.get(i);
      if (next.start <= current.end + 1) {
        // Overlapping or adjacent - merge them
        current = new Range(current.start, Math.max(current.end, next.end));
      } else {
        // No overlap - add current to merged list and start new range
        merged.add(current);
        current = next;
      }
    }
    merged.add(current); // Add the last range

    // Count total IDs in merged ranges
    return merged.stream().mapToLong(r -> r.end - r.start + 1).sum();
  }

  private boolean isFresh(long id, List<Range> ranges) {
    return ranges.stream().anyMatch(range -> range.contains(id));
  }

  private record Range(long start, long end) {
    boolean contains(long value) {
      return value >= start && value <= end;
    }
  }
}
