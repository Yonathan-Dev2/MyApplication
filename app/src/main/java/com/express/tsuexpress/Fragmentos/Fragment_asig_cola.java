package com.express.tsuexpress.Fragmentos;


import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;

import com.express.tsuexpress.R;

public class Fragment_asig_cola extends Fragment {

    private AppBarLayout appBar;
    private TableLayout tabs;
    private ViewPager viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.fragment_asig_cola, container, false);

        View contenedor = (View) container.getParent();
        appBar = (AppBarLayout)contenedor.findViewById(R.id.appBarLayout);
        tabs = new TableLayout(getActivity());

        appBar.addView(tabs);

        viewPager = (ViewPager)view.findViewById(R.id.pager);
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        return view;
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        appBar.removeView(tabs);
    }

    public class ViewPagerAdapter extends FragmentStatePagerAdapter{
        public ViewPagerAdapter(FragmentManager fragmentManager){
            super(fragmentManager);
        }

        String[] tituloTabs={"ENTREGA", "RECOJO"};

        @Override
        public Fragment getItem(int position){
            switch (position){
            case 0:
                return new Fragmen_asig_entr();
            case 1:
                return new Fragment_asig_reco();
            }
            return  null;
        }

        @Override
        public int getCount(){
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position){

            return tituloTabs[position];
        }

    }

}
