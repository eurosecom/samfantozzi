package com.eusecom.samfantozzi.dagger.components;

import com.eusecom.samfantozzi.AbsServerAsActivity;
import com.eusecom.samfantozzi.ChooseCompanyActivity;
import com.eusecom.samfantozzi.ChooseMonthActivity;
import com.eusecom.samfantozzi.DgAbsServerListFragment;
import com.eusecom.samfantozzi.DgAeaActivity;
import com.eusecom.samfantozzi.DgAeaListFragment;
import com.eusecom.samfantozzi.dagger.modules.ApplicationModule;
import com.eusecom.samfantozzi.dagger.modules.DgAeaModule;

import javax.inject.Singleton;
import dagger.Component;

@Singleton
@Component(modules={ApplicationModule.class, DgAeaModule.class})
public interface DgAeaComponent {
  void inject(DgAeaActivity dgaeaActivity);
  void inject(AbsServerAsActivity absActivity);
  void inject(DgAeaListFragment dgaeafragment);
  void inject(DgAbsServerListFragment absserverfragment);
  void inject(ChooseMonthActivity chooseMonthActivity);
  void inject(ChooseCompanyActivity chooseCompanyActivity);

}