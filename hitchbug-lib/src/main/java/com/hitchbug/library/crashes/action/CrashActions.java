package com.hitchbug.library.crashes.action;

import com.hitchbug.library.core.investigation.CrashViewModel;
import com.hitchbug.library.crashes.viewmodel.AppInfoViewModel;

public interface CrashActions {
  void openSendApplicationChooser(String crashDetails);

  void renderAppInfo(AppInfoViewModel viewModel);

  void render(CrashViewModel viewModel);
}
