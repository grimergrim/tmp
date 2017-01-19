package ru.nadocars.messanger.ui.profile;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.squareup.picasso.Picasso;

import ru.nadocars.messanger.R;
import ru.nadocars.messanger.api.SharedPreferencesApi;
import ru.nadocars.messanger.json.user.GetUserResponse;
import ru.nadocars.messanger.ui.navigation.Navigator;
import ru.nadocars.messanger.ui.navigation.NavigatorImpl;

import static ru.nadocars.messanger.R.id.code;

public class ProfileActivity extends AppCompatActivity implements ProfileView {

    ProfilePresenter mProfilePresenter;
    Navigator mNavigator;
    private ViewPager mViewPager;
    private PagerAdapter mPagerAdapter;
    private TextView mNameTextView;
    private TextView mMoneyTextView;
    private EditText mEmailTextView;
    private EditText mPhoneTextView;
    private ImageView mAvatarImageView;
    private ConstraintLayout mCarsConstraintLayout;
    private MaterialCalendarView mCalendarView;
    private ImageView mUserAvatar;
    private Button mUpdateButton;
    private LinearLayout mCodeLinearLayout;
    private EditText mCodeEditText;
    private Button mSaveButton;
    private String mEmail;
    private String mPhone;
    private String mToken;
    private String mSessionId;
    private long mCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        findViews();
        setListeners();
        if (mCarsConstraintLayout != null)
            mCarsConstraintLayout.setVisibility(View.GONE);
        //TODO add exit and settings buttons action
        mProfilePresenter = ProfilePresenterImpl.getPreLoginPresenter();
        mProfilePresenter.setView(this);
        mNavigator = new NavigatorImpl();
        String token = PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                .getString(SharedPreferencesApi.TOKEN, null);
        if (null != token) {
            mProfilePresenter.getUserInfo(token);
        }
        if (null != mUserAvatar) {
            mUserAvatar.setVisibility(View.GONE);
        }
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(), 3);
        mViewPager.setAdapter(mPagerAdapter);
        initCalendar();
        if (null != mEmailTextView) {
//            mEmailTextView.clearFocus();
            mEmailTextView.setFocusable(false);

        }
        if (null != mPhoneTextView) {
//            mPhoneTextView.clearFocus();\
            mPhoneTextView.setFocusable(false);
        }
        if (null != mCodeLinearLayout) {
            mCodeLinearLayout.setVisibility(View.GONE);
        }
    }

    private void findViews() {
        mNameTextView = (TextView) findViewById(R.id.user_name);
        mMoneyTextView = (TextView) findViewById(R.id.money);
        mEmailTextView = (EditText) findViewById(R.id.email);
        mPhoneTextView = (EditText) findViewById(R.id.phone_number);
        mAvatarImageView = (ImageView) findViewById(R.id.profile_user_avatar);
        mCarsConstraintLayout = (ConstraintLayout) findViewById(R.id.car_layout);
        mCalendarView = (MaterialCalendarView) findViewById(R.id.calendar_view);
        mUserAvatar = (ImageView) findViewById(R.id.user_avatar);
        mViewPager = (ViewPager) findViewById(R.id.car_photo_view_pager);
        mUpdateButton = (Button) findViewById(R.id.cancel_button);
        mCodeLinearLayout = (LinearLayout) findViewById(R.id.code_layout);
        mCodeEditText = (EditText) findViewById(code);
        mSaveButton = (Button) findViewById(R.id.save_button);
    }

    private void setListeners() {
        mEmailTextView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mEmailTextView.setFocusableInTouchMode(true);
                return false;
            }
        });
        mEmailTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (null != mEmail && !mEmail.equals(s)) {
                    mUpdateButton.setVisibility(View.VISIBLE);
                }
            }
        });
        mPhoneTextView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mPhoneTextView.setFocusableInTouchMode(true);
                return false;
            }
        });
        mPhoneTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!mPhone.equals(s)) {
                    mUpdateButton.setVisibility(View.VISIBLE);
                }
            }
        });
        mUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String token = PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                        .getString(SharedPreferencesApi.TOKEN, null);
                if (null != token && token.length() > 0) {
                    mProfilePresenter.updateUserInfo(mEmailTextView.getText().toString(),
                            mPhoneTextView.getText().toString(), token);
                } else {
                    Toast.makeText(getApplicationContext(), "Login first please", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mCodeEditText.getText() && mCodeEditText.getText().toString().equals(String.valueOf(mCode))) {
                    mProfilePresenter.updateUserInfo(mEmail, mPhone, mToken, mSessionId, mCode);
                } else {
                    showError("Не верный код");
                }
            }
        });
    }

    private void initCalendar() {
        if (null != mCalendarView) {
            mCalendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_NONE);
            mCalendarView.addDecorator(new DayViewDecorator() {
                @Override
                public boolean shouldDecorate(CalendarDay day) {
                    return true;
                }

                @Override
                public void decorate(DayViewFacade view) {
                    view.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(),
                            R.drawable.shape_calendar_view));
                }
            });
        }
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
            mEmail = profileInfo.getResponse().getEmail();
            mPhone = profileInfo.getResponse().getPhone();
            mMoneyTextView.setText(String.valueOf(profileInfo.getResponse().getBalance()) + " р.");
            mNameTextView.setText(profileInfo.getResponse().getLastName() + " " + profileInfo.getResponse().getFirstName() + " " + profileInfo.getResponse().getMiddleName());
            mEmailTextView.setText(profileInfo.getResponse().getEmail());
            mPhoneTextView.setText(profileInfo.getResponse().getPhone());
            if (profileInfo.getResponse().getIsOwner() == 1) {
                mCarsConstraintLayout.setVisibility(View.VISIBLE);
            } else {
                mCarsConstraintLayout.setVisibility(View.GONE);
            }
            mUpdateButton.setVisibility(View.GONE);
            Picasso.with(getApplicationContext()).load(profileInfo.getResponse().getAvatar()).into(mAvatarImageView);
        }

    }

    @Override
    public void showError(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void requestVerificationCode(String email, String phoneNumber, String token, String ssesionId, long code) {
        mUpdateButton.setVisibility(View.GONE);
        mCodeLinearLayout.setVisibility(View.VISIBLE);
        mToken = token;
        mSessionId = ssesionId;
        mCode = code;
    }

    @Override
    public void hideUpdateButton() {
        mUpdateButton.setVisibility(View.GONE);
    }

    @Override
    public void hideCodeLayout() {
        mCodeLinearLayout.setVisibility(View.GONE);
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
