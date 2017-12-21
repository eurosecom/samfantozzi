package com.eusecom.samfantozzi.dagger.components;

import com.eusecom.samfantozzi.CashListKtActivity;
import com.eusecom.samfantozzi.CashListKtFragment;
import com.eusecom.samfantozzi.ChooseAccountActivity;
import com.eusecom.samfantozzi.ChooseCompanyActivity;
import com.eusecom.samfantozzi.ChooseMonthActivity;
import com.eusecom.samfantozzi.InvoiceListFragment;
import com.eusecom.samfantozzi.InvoiceListKtActivity;
import com.eusecom.samfantozzi.NewCashDocFragment;
import com.eusecom.samfantozzi.NewCashDocKtActivity;
import com.eusecom.samfantozzi.SupplierListActivity;
import com.eusecom.samfantozzi.SupplierListFragment;
import com.eusecom.samfantozzi.dagger.modules.ApplicationModule;
import com.eusecom.samfantozzi.dagger.modules.DgAeaModule;

import javax.inject.Singleton;
import dagger.Component;

@Singleton
@Component(modules={ApplicationModule.class, DgAeaModule.class})
public interface DgAeaComponent {
  void inject(SupplierListActivity supplierListActivity);
  void inject(SupplierListFragment supplierfragment);
  void inject(CashListKtActivity cashListKtActivity);
  void inject(NewCashDocKtActivity newCashDocKtActivity);
  void inject(NewCashDocFragment newCashDocFragment);
  void inject(CashListKtFragment cashListKtFragment);
  void inject(InvoiceListFragment invoicefragment);
  void inject(InvoiceListKtActivity invoiceListKtActivity);
  void inject(ChooseMonthActivity chooseMonthActivity);
  void inject(ChooseCompanyActivity chooseCompanyActivity);
  void inject(ChooseAccountActivity chooseAccountActivity);





}