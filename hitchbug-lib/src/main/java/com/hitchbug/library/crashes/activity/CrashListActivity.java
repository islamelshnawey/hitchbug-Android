package com.hitchbug.library.crashes.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hitchbug.library.R;
import com.hitchbug.library.core.database.SherlockDatabaseHelper;
import com.hitchbug.library.crashes.action.CrashListActions;
import com.hitchbug.library.crashes.adapter.CrashAdapter;
import com.hitchbug.library.crashes.presenter.CrashListPresenter;
import com.hitchbug.library.crashes.viewmodel.CrashesViewModel;

public class CrashListActivity extends BaseActivity implements CrashListActions {

  private CrashListPresenter presenter;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.crash_list_activity);

    enableHomeButton((Toolbar) findViewById(R.id.toolbar));
    setTitle(R.string.app_name);

    SherlockDatabaseHelper database = new SherlockDatabaseHelper(this);
    presenter = new CrashListPresenter(this);
    presenter.render(database);
  }

  @Override
  public void render(CrashesViewModel viewModel) {
    RecyclerView crashList = (RecyclerView) findViewById(R.id.crash_list);
    CrashAdapter crashAdapter = new CrashAdapter(viewModel.getCrashViewModels(), presenter);
    crashList.setAdapter(crashAdapter);
    crashList.setLayoutManager(new LinearLayoutManager(this));

    LinearLayout noCrashFoundLayout = (LinearLayout) findViewById(R.id.no_crash_found_layout);
    //noinspection WrongConstant
    noCrashFoundLayout.setVisibility(viewModel.getCrashNotFoundViewState().getVisibility());
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.sherlock, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == R.id.github_link) {
      Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.github_link)));
      startActivity(browserIntent);
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public void openCrashDetails(int crashId) {
    Intent intent = new Intent(this, CrashActivity.class);
    intent.putExtra(CrashActivity.CRASH_ID, crashId);

    startActivity(intent);
  }
}
