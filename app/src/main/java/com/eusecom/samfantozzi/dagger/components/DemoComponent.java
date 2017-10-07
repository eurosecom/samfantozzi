package com.eusecom.samfantozzi.dagger.components;

import com.eusecom.samfantozzi.AbsServerAsActivity;
import com.eusecom.samfantozzi.DgAbsServerListFragment;
import com.eusecom.samfantozzi.DgAeaActivity;
import com.eusecom.samfantozzi.DgAeaListFragment;
import com.eusecom.samfantozzi.dagger.modules.ApplicationModule;
import com.eusecom.samfantozzi.dagger.modules.ClockModule;
import javax.inject.Singleton;
import dagger.Component;

@Singleton
@Component(modules={ApplicationModule.class, ClockModule.class})
public interface DemoComponent {
  void inject(DgAeaActivity dgaeaActivity);
  void inject(AbsServerAsActivity absActivity);
  void inject(DgAeaListFragment dgaeafragment);
  void inject(DgAbsServerListFragment absserverfragment);

}