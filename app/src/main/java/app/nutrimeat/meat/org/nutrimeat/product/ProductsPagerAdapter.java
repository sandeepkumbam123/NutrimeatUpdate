package app.nutrimeat.meat.org.nutrimeat.product;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import app.nutrimeat.meat.org.nutrimeat.AppConstants;

/**
 * Created by Admin on 6/6/2017.
 */

public class ProductsPagerAdapter extends FragmentPagerAdapter implements AppConstants {
    public ProductsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return Products.newInstance(CHICKEN);
            case 1:
                return Products.newInstance(MUTTON);
            case 2:
                return Products.newInstance(SEA_FOODS);
            case 3:
                return Products.newInstance(OTHERS);
        }
        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "CHICKEN";
            case 1:
                return "MEAT";
            case 2:
                return "SEA FOOD";
            case 3:
                return "OTHERS";
        }
        return super.getPageTitle(position);
    }
}
