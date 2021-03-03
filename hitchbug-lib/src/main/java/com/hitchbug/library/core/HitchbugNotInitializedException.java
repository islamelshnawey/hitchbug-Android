package com.hitchbug.library.core;

public class HitchbugNotInitializedException extends RuntimeException {
  public HitchbugNotInitializedException() {
    super("Initialize Sherlock using Sherlock.init(context) before using its methods");
  }
}
