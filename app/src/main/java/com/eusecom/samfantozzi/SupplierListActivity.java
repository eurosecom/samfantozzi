/*
 * Copyright 2015 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.eusecom.samfantozzi;

import android.app.SearchManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import javax.inject.Inject;


/**
 * Show List of all suppliers documents
 *
 * template from DgAeaActivity.java
 *
 */

public class  SupplierListActivity extends BaseListActivity {

    private FragmentPagerAdapter mPagerAdapter;
    private ViewPager mViewPager;
    private FloatingActionButton fab;

    @Inject
    SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suppliers);


        ((SamfantozziApp) getApplication()).dgaeacomponent().inject(this);

        getSupportActionBar().setTitle(mSharedPreferences.getString("ume", "") + " " + getString(R.string.suppliers));

            // Create the adapter that will return a fragment for each section
            mPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
                private final Fragment[] mFragments = new Fragment[]{
                        new SupplierListFragment(),
                        new EmptyKtFragment()
                };
                private final String[] mFragmentNames = new String[]{
                        getString(R.string.suppliers),
                        getString(R.string.empty)
                };

                @Override
                public Fragment getItem(int position) {
                    return mFragments[position];
                }

                @Override
                public int getCount() {
                    return mFragments.length;
                }

                @Override
                public CharSequence getPageTitle(int position) {
                    return mFragmentNames[position];
                }
            };



        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mPagerAdapter);


        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {}
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            public void onPageSelected(int position) {
                // Check if this is the page you want.
                if(position == 0){
                    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
                    fab.setVisibility(View.VISIBLE);
                    getSupportActionBar().setTitle(mSharedPreferences.getString("ume", "") + " " + getString(R.string.suppliers));
                }
                if(position == 1){
                    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
                    //fab.setVisibility(View.GONE);
                    fab.setVisibility(View.VISIBLE);
                    getSupportActionBar().setTitle(getString(R.string.empty));
                }

            }
        });


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(v -> {



            }
        );

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mViewPager=null;
        mPagerAdapter=null;

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Retrieve the SearchView and plug it into SearchManager
        getMenuInflater().inflate(R.menu.menu_listdoc, menu);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // use this method when query submitted

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // use this method for auto complete search process
                Toast.makeText(SupplierListActivity.this, newText, Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            Intent is = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(is);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
