# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is an Advent of Code solving repository using Java 25 and Maven. Solutions are implemented by Claude Code to solve [Advent of Code](https://adventofcode.com/) problems.

## Build and Test Commands

**Build the project:**
```bash
mvn clean compile
```

**Run all tests:**
```bash
mvn test
```

**Run a single day's tests:**
```bash
mvn test -Dtest=Day01Test
```

**Format code (automatic on compile):**
- Spotless is configured to run automatically during compilation
- Manual formatting: `mvn spotless:apply`
- Code style: Google Java Format with import ordering and unused import removal

## Architecture

### Solution Structure

Each Advent of Code solution follows this pattern:

1. **Solution class** (`src/main/java/org/ck/adventofclaude/yearYYYY/DayXX.java`):
   - Extends `AOCSolution` abstract class
   - Implements `runPartOne(Scanner in)` and `runPartTwo(Scanner in)` methods
   - Reads input from `Scanner` (piped from stdin during tests)
   - Uses `print(Object o)` method to output results

2. **Test class** (`src/test/java/org/ck/adventofclaude/yearYYYY/DayXXTest.java`):
   - Extends `BaseAOCTest` which provides test infrastructure
   - `@Disabled` annotation should be removed when solution is ready
   - Example tests use `@ParameterizedTest` with `@ValueSource` for multiple test cases
   - Example test naming: `day01/01a`, `day01/02a` for part 1/2 examples

3. **Test resources** (`src/test/resources/org/ck/adventofclaude/yearYYYY/dayXX/`):
   - `XX.txt` - Encrypted real input (requires `AOC_KEY` environment variable)
   - `XX.result.txt` - Expected output for real input
   - `XXa.txt` - Example input (unencrypted, for example tests)
   - `XXa.result.txt` - Expected output for examples

### Base Classes

- **AOCSolution**: Abstract base for all solutions, provides `partOne()` and `partTwo()` entry points that pipe stdin to abstract `runPartOne/Two` methods
- **BaseAOCTest**: Provides `testPartOne()` and `testPartTwo()` that automatically run encrypted real inputs against the solution
- **BaseTest**: Low-level test utilities for piping resources to stdin and capturing stdout

### Test Execution Model

- Tests pipe input files to `System.in` and capture `System.out`
- Real puzzle inputs are encrypted and require `AOC_KEY` environment variable
- Test framework automatically loads the corresponding solution class by removing "Test" from test class name
- Tests run in parallel (configured: 5 forks, parallel execution)

## Working with a New Day

To implement a new day's solution:

1. Find the stub in `src/main/java/org/ck/adventofclaude/year2025/DayXX.java`
2. Implement the `runPartOne` and `runPartTwo` methods
3. Remove `@Disabled` from the corresponding test class
4. Run the tests to validate against encrypted inputs
5. Provide an explanation of the solution in the README

Example implementation pattern:
```java
@Override
protected void runPartOne(final Scanner in) {
  // Read input using Scanner
  // Solve part 1
  print(result); // Output answer
}
```
