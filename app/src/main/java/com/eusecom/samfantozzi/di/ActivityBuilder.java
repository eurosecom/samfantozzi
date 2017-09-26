package com.eusecom.samfantozzi.di;

import android.app.Activity;

import com.eusecom.samfantozzi.Detail2Activity;
import com.eusecom.samfantozzi.DetailActivity;
import com.eusecom.samfantozzi.MainActivity;
import dagger.Binds;
import dagger.Module;
import dagger.android.ActivityKey;
import dagger.android.AndroidInjector;
import dagger.multibindings.IntoMap;


/**
 * Created by mertsimsek on 25/05/2017.
 */
@Module
public abstract class ActivityBuilder {

    @Binds
    @IntoMap
    @ActivityKey(MainActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindMainActivity(MainActivityComponent.Builder builder);

    @Binds
    @IntoMap
    @ActivityKey(DetailActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindDetailActivity(DetailActivityComponent.Builder builder);

    @Binds
    @IntoMap
    @ActivityKey(Detail2Activity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindDetail2Activity(Detail2ActivityComponent.Builder builder);

}
