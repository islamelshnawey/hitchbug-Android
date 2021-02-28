package com.hitchbug.library.crashes.action;

import com.hitchbug.library.crashes.viewmodel.CrashesViewModel;

public interface CrashListActions {
  void render(CrashesViewModel viewModel);

  void openCrashDetails(int crashId);
}
