package id.merv.cdp.book.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import id.merv.cdp.book.fragment.ChooseBookFragment;
import id.merv.cdp.book.fragment.DownloadedBookFragment;

/**
 * Created by akm on 07/03/16.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> fragmentList = new ArrayList<Fragment>();

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);

        DownloadedBookFragment downloadedBookFragment = new DownloadedBookFragment();
        ChooseBookFragment chooseBookFragment = new ChooseBookFragment();

        fragmentList.add(downloadedBookFragment);
        fragmentList.add(chooseBookFragment);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return fragmentList.get(0);
            case 1:
                return fragmentList.get(1);

        }
        return null;
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position){
        if (position == 0){
            return "Downloaded Books";
        }else if (position == 1){
            return "Choose Book";
        }else {
            return null;
        }
    }
}
