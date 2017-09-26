package com.eusecom.samfantozzi.di;

import com.eusecom.samfantozzi.DetailActivity;
import dagger.Subcomponent;
import dagger.android.AndroidInjector;


@Subcomponent(modules = DetailActivityModule.class)
public interface DetailActivityComponent extends AndroidInjector<DetailActivity>{
    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<DetailActivity>{}
}
