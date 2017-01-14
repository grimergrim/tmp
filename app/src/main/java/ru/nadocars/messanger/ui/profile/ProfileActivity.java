package ru.nadocars.messanger.ui.profile;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import ru.nadocars.messanger.R;
import ru.nadocars.messanger.api.SharedPreferencesApi;
import ru.nadocars.messanger.json.GetUserResponse;
import ru.nadocars.messanger.ui.navigation.Navigator;
import ru.nadocars.messanger.ui.navigation.NavigatorImpl;

public class ProfileActivity extends AppCompatActivity implements ProfileView {

    ProfilePresenter mProfilePresenter;
    Navigator mNavigator;
    private ViewPager mViewPager;
    private PagerAdapter mPagerAdapter;
    private TextView mNameTextView;
    private TextView mMoneyTextView;
    private TextView mEmailTextView;
    private TextView mPhoneTextView;
    private ImageView mAvatarImageView;
    private ConstraintLayout mCarsConstraintLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        //Find views
        mNameTextView = (TextView) findViewById(R.id.user_name);
        mMoneyTextView = (TextView) findViewById(R.id.money);
        mEmailTextView = (TextView) findViewById(R.id.email);
        mPhoneTextView = (TextView) findViewById(R.id.phoneNumber);
        mAvatarImageView = (ImageView) findViewById(R.id.profile_user_avatar);
        mCarsConstraintLayout = (ConstraintLayout) findViewById(R.id.car_layout);
        if (mCarsConstraintLayout != null)
            mCarsConstraintLayout.setVisibility(View.GONE);

        mProfilePresenter = ProfilePresenterImpl.getPreLoginPresenter();
        mProfilePresenter.setView(this);
        mNavigator = new NavigatorImpl();
        String token = PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                .getString(SharedPreferencesApi.TOKEN, null);
        if (null != token) {
            mProfilePresenter.getUserInfo(token);
        }
        ImageView userAvatar = (ImageView) findViewById(R.id.user_avatar);
        if (null != userAvatar) {
            userAvatar.setVisibility(View.GONE);
        }

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

    @Override
    public void setProfileInfo(GetUserResponse profileInfo) {
        if (null != profileInfo) {
            mMoneyTextView.setText(String.valueOf(profileInfo.getResponse().getBalance()) + " р.");
            mNameTextView.setText(profileInfo.getResponse().getLastName() + " " + profileInfo.getResponse().getFirstName() + " " + profileInfo.getResponse().getMiddleName());
            mEmailTextView.setText(profileInfo.getResponse().getEmail());
            mPhoneTextView.setText(profileInfo.getResponse().getPhone());
            if (profileInfo.getResponse().getIsOwner() == 1) {
                mCarsConstraintLayout.setVisibility(View.VISIBLE);
            } else {
                mCarsConstraintLayout.setVisibility(View.GONE);
            }
            Picasso.with(getApplicationContext()).load(profileInfo.getResponse().getAvatar()).into(mAvatarImageView);
        }

    }

    @Override
    public void showError(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
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
