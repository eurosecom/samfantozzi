package com.eusecom.samfantozzi;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoggingViewModelDemoActivity extends AppCompatActivity {

    @BindView(R.id.click_count_text)
    protected TextView clickCountText;

    private LogClickCounterViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loggingactivity_viewmodel_demo);
        ButterKnife.bind(this);

        // the factory and its dependencies instead should be injected with DI framework like Dagger
        LoggingClickCounterViewModelFactory factory =
                new LoggingClickCounterViewModelFactory(new LoggingClickInterceptor());

        viewModel = ViewModelProviders.of(this, factory).get(LoggingClickCounterViewModel.class);
        displayClickCount(viewModel.getCount());
    }

    @OnClick(R.id.increment_button)
    public void incrementClickCount(View button) {
        viewModel.setCount(viewModel.getCount() + 1);
        displayClickCount(viewModel.getCount());
    }

    private void displayClickCount(int clickCount) {
        clickCountText.setText(String.valueOf(clickCount));
    }
}
