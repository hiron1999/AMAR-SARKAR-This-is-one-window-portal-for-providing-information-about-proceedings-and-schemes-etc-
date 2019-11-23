package com.example.server_connect;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class Freagmentpage_Adapter extends FragmentPagerAdapter {
    int tabcount;
    private List<Fragment>fragmentlist=new ArrayList<>();
    private List<String>titlelist=new ArrayList<>();
    public Freagmentpage_Adapter(@NonNull FragmentManager fm ,int tabcount) {
        super(fm);
        this.tabcount=tabcount;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
       /* switch (position){
            case 0:
                return new Scheme();
            case 1:
                return new new_tab();
            case 2:
                return new job_tab();
                default:
                    return null;
        }*/
       return  fragmentlist.get(position);


    }

    @Override
    public int getCount() {
        return tabcount;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titlelist.get(position);
    }
    public void addfreagment(Fragment fragment,String title){
        fragmentlist.add(fragment);
        titlelist.add(title);
    }
}
