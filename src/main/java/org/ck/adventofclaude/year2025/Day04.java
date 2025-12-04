package org.ck.adventofclaude.year2025;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.ck.adventofclaude.util.AOCSolution;

public class Day04 extends AOCSolution {

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
              if (newRow >= 0 && newRow < rows && newCol >= 0 && newCol < cols) {
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
            // Count adjacent paper rolls
            int adjacentRolls = 0;

            // Check all 8 directions
            for (int dr = -1; dr <= 1; dr++) {
              for (int dc = -1; dc <= 1; dc++) {
                if (dr == 0 && dc == 0) continue; // Skip the center cell

                int newRow = row + dr;
                int newCol = col + dc;

                // Check bounds
                if (newRow >= 0 && newRow < rows && newCol >= 0 && newCol < cols) {
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
}
