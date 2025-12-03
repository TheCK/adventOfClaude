package org.ck.adventofclaude.year2025;

import java.util.Scanner;
import org.ck.adventofclaude.util.AOCSolution;

public class Day03 extends AOCSolution {

  @Override
  protected void runPartOne(final Scanner in) {
    long totalJoltage = 0;

    while (in.hasNextLine()) {
      String bank = in.nextLine();
      int maxJoltage = findMaxJoltage(bank);
      totalJoltage += maxJoltage;
    }

    print(totalJoltage);
  }

  @Override
  protected void runPartTwo(final Scanner in) {
    long totalJoltage = 0;

    while (in.hasNextLine()) {
      String bank = in.nextLine();
      long maxJoltage = findMaxJoltageWithTwelveBatteries(bank);
      totalJoltage += maxJoltage;
    }

    print(totalJoltage);
  }

  private long findMaxJoltageWithTwelveBatteries(String bank) {
    StringBuilder result = new StringBuilder();
    int currentIndex = 0;
    int batteriesNeeded = 12;

    for (int position = 0; position < batteriesNeeded; position++) {
      int digitsRemaining = batteriesNeeded - position;
      // We can search up to the index where exactly digitsRemaining digits are left
      int searchEnd = bank.length() - digitsRemaining;

      // Find the maximum digit in the range [currentIndex, searchEnd]
      char maxDigit = '0';
      int maxIndex = currentIndex;

      for (int i = currentIndex; i <= searchEnd; i++) {
        if (bank.charAt(i) > maxDigit) {
          maxDigit = bank.charAt(i);
          maxIndex = i;
        }
      }

      result.append(maxDigit);
      currentIndex = maxIndex + 1;
    }

    return Long.parseLong(result.toString());
  }

  private int findMaxJoltage(String bank) {
    int maxJoltage = 0;

    // Try each position as the first battery
    for (int i = 0; i < bank.length() - 1; i++) {
      // Find the maximum digit after position i for the second battery
      char maxAfter = '0';
      for (int j = i + 1; j < bank.length(); j++) {
        if (bank.charAt(j) > maxAfter) {
          maxAfter = bank.charAt(j);
        }
      }

      // Form the two-digit number
      int joltage = (bank.charAt(i) - '0') * 10 + (maxAfter - '0');
      maxJoltage = Math.max(maxJoltage, joltage);
    }

    return maxJoltage;
  }
}
