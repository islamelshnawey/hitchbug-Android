package com.hitchbug.library.crashes.presenter;

import com.hitchbug.library.core.database.SherlockDatabaseHelper;
import com.hitchbug.library.core.investigation.Crash;
import com.hitchbug.library.core.investigation.CrashViewModel;
import com.hitchbug.library.crashes.action.CrashListActions;
import com.hitchbug.library.crashes.viewmodel.CrashesViewModel;

import java.util.ArrayList;
import java.util.List;

public class CrashListPresenter {
  private final CrashListActions actions;

  public CrashListPresenter(CrashListActions actions) {
    this.actions = actions;
  }

  public void render(SherlockDatabaseHelper database) {
    List<Crash> crashes = database.getCrashes();

    ArrayList<CrashViewModel> crashViewModels = new ArrayList<>();

    for (Crash crash : crashes) {
      crashViewModels.add(new CrashViewModel(crash));
    }
    actions.render(new CrashesViewModel(crashViewModels));
  }

  public void onCrashClicked(CrashViewModel viewModel) {
    actions.openCrashDetails(viewModel.getIdentifier());
  }
}
