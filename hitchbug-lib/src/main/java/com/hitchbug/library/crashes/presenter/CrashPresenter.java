package com.hitchbug.library.crashes.presenter;

import com.hitchbug.library.core.database.SherlockDatabaseHelper;
import com.hitchbug.library.core.investigation.Crash;
import com.hitchbug.library.core.investigation.CrashViewModel;
import com.hitchbug.library.crashes.action.CrashActions;

public class CrashPresenter {
  private final SherlockDatabaseHelper database;
  private final CrashActions actions;

  public CrashPresenter(SherlockDatabaseHelper database, CrashActions actions) {
    this.database = database;
    this.actions = actions;
  }

  public void render(int crashId) {
    Crash crash = database.getCrashById(crashId);
    CrashViewModel crashViewModel = new CrashViewModel(crash);

    actions.render(crashViewModel);
    actions.renderAppInfo(crashViewModel.getAppInfoViewModel());
  }

  public void shareCrashDetails(CrashViewModel viewModel) {
    actions.openSendApplicationChooser(viewModel.getCrashInfo());
  }
}
