package org.ck.adventofclaude.util;

public class InputEncrpyter {
  static void main(final String[] args) {
    System.err.println(EncryptionHelper.encrypt("", System.getenv("AOC_KEY")));
  }
}
