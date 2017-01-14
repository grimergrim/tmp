package ru.nadocars.messanger.ui.profile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import ru.nadocars.messanger.R;
import ru.nadocars.messanger.ui.navigation.Navigator;
import ru.nadocars.messanger.ui.navigation.NavigatorImpl;

public class ProfileActivity extends AppCompatActivity implements ProfileView {

    ProfilePresenter mProfilePresenter;
    Navigator mNavigator;
    private ViewPager mViewPager;
    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mProfilePresenter = ProfilePresenterImpl.getPreLoginPresenter();
        mProfilePresenter.setView(this);
        mNavigator = new NavigatorImpl();

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(), 3);
        mViewPager.setAdapter(mPagerAdapter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mProfilePresenter.setView(null);
    }

    @Override
    public void onBackPressed() {
        if (mViewPager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
        }
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        private int mNumberOfPages;

        public ScreenSlidePagerAdapter(FragmentManager fragmentManager, int numberOfPages) {
            super(fragmentManager);
            mNumberOfPages = numberOfPages;
        }

        @Override
        public Fragment getItem(int position) {
            return new ScreenSlidePageFragment();
        }

        @Override
        public int getCount() {
            return mNumberOfPages;
        }
    }

}
