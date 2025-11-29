package org.ck.adventofclaude.year2025;

import org.ck.adventofclaude.util.BaseAOCTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@Disabled
class Day12Test extends BaseAOCTest {
  @ParameterizedTest
  @ValueSource(strings = {"01a"})
  void testPartOneExamples(final String name) throws Exception {
    runTest(new Day01()::partOne, "day12/%s".formatted(name));
  }

  @ParameterizedTest
  @ValueSource(strings = {"02a"})
  void testPartTwoExamples(final String name) throws Exception {
    runTest(new Day01()::partTwo, "day12/%s".formatted(name));
  }
}
