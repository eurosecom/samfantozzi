package com.eusecom.samfantozzi.dagger.components;

import com.eusecom.samfantozzi.dagger.modules.ApplicationModule;
import com.eusecom.samfantozzi.dagger.modules.ClockModule;
import javax.inject.Singleton;
import dagger.Component;

@Singleton
@Component(modules={ApplicationModule.class, ClockModule.class})
public interface DemoComponent {
  //void inject(DgAeaActivity dgaeaActivity);
  //void inject(DgAeaListFragment dgaeafragment);

}