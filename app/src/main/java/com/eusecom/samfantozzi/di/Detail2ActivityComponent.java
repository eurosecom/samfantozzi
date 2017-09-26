package com.eusecom.samfantozzi.di;

import com.eusecom.samfantozzi.Detail2Activity;
import com.eusecom.samfantozzi.DetailActivity;
import dagger.Subcomponent;
import dagger.android.AndroidInjector;

/**
 * Created by mertsimsek on 25/05/2017.
 */
@Subcomponent(modules = DetailActivityModule.class)
public interface Detail2ActivityComponent extends AndroidInjector<Detail2Activity>{
    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<Detail2Activity>{}
}
