package ru.nadocars.messanger.ui.profile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ru.nadocars.messanger.R;
import ru.nadocars.messanger.api.SharedPreferencesApi;
import ru.nadocars.messanger.asynctasks.LogOutTask;
import ru.nadocars.messanger.data.repo.ContactsRepo;
import ru.nadocars.messanger.data.repo.MessagesRepo;
import ru.nadocars.messanger.json.car.GetCarsResponse;
import ru.nadocars.messanger.json.car.Item;
import ru.nadocars.messanger.json.car.Photo;
import ru.nadocars.messanger.json.user.GetUserResponse;
import ru.nadocars.messanger.ui.navigation.Navigator;
import ru.nadocars.messanger.ui.navigation.NavigatorImpl;

import static ru.nadocars.messanger.R.id.code;

public class ProfileActivity extends AppCompatActivity implements ProfileView {

    protected static final int AVATAR_CAMERA_REQUEST = 0;
    protected static final int AVATAR_GALLERY_REQUEST = 1;
    protected static final int CAR_CAMERA_REQUEST = 2;
    protected static final int CAR_GALLERY_REQUEST = 3;

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
    private TextView mCarTitleEditText;
    private EditText mDayPriceEditText;
    private EditText mWeekPriceEditText;
    private EditText mMonthPriceEditText;
    private Button mAddPhotoButton;
    private Button mDeletePhotoButton;

    private long mCode;

    private Bitmap bitmap;
    private String selectedImagePath;
    private String mImagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        findViews();
        setListeners();
        if (mCarsConstraintLayout != null)
            mCarsConstraintLayout.setVisibility(View.GONE);
        Button exitbutton = (Button) findViewById(R.id.exit_button);
        Button settingsButton = (Button) findViewById(R.id.check_interval);
        mProfilePresenter = ProfilePresenterImpl.getPreLoginPresenter();
        mProfilePresenter.setView(this);
        mNavigator = new NavigatorImpl();
        String token = PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                .getString(SharedPreferencesApi.TOKEN, null);
        if (null != token) {
            mProfilePresenter.getUserInfo(token);
            mProfilePresenter.getCars(token);
        }
        if (null != mUserAvatar) {
            mUserAvatar.setVisibility(View.GONE);
        }
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
        if (settingsButton != null) {
            settingsButton.setVisibility(View.GONE);
            settingsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    openSettings();
                }
            });
        }
        if (exitbutton != null) {
            exitbutton.setVisibility(View.VISIBLE);
            exitbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    logOut();
                }
            });
        }
    }

//    //открыть настройки
//    private void openSettings() {
//        IntervalDialogFragment intervalDialogFragment;
//        intervalDialogFragment = new IntervalDialogFragment();
//        intervalDialogFragment.show(getFragmentManager(), "SetIntervalDialog");
//    }

    //логаут
    private void logOut() {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        LogOutTask logOutTask = new LogOutTask(this, defaultSharedPreferences.getString(SharedPreferencesApi.TOKEN, null));
        logOutTask.execute();
        SharedPreferences.Editor editor = defaultSharedPreferences.edit();
        editor.remove(SharedPreferencesApi.LOGIN);
        editor.remove(SharedPreferencesApi.PASSWORD);
        editor.remove(SharedPreferencesApi.AVATAR);
        editor.remove(SharedPreferencesApi.TOKEN);
        editor.apply();
        ContactsRepo contactsRepo = new ContactsRepo();
        contactsRepo.deleteAll();
        MessagesRepo messagesRepo = new MessagesRepo();
        messagesRepo.deleteAll();
        mNavigator.navigateToLogin(this);
        finish();
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
        mCarTitleEditText = (TextView) findViewById(R.id.car_title);
        mDayPriceEditText = (EditText) findViewById(R.id.day_price);
        mWeekPriceEditText = (EditText) findViewById(R.id.week_price);
        mMonthPriceEditText = (EditText) findViewById(R.id.month_price);
        mAddPhotoButton = (Button) findViewById(R.id.add_photo);
        mDeletePhotoButton = (Button) findViewById(R.id.remove_photo);
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
        mAvatarImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDialog(AVATAR_CAMERA_REQUEST, AVATAR_GALLERY_REQUEST);
            }
        });
        mAddPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDialog(CAR_CAMERA_REQUEST, CAR_GALLERY_REQUEST);
            }
        });
    }

    private void startDialog(int cameraRequestCode, int galleryRequestCode) {
        GetPictureDialogFragment getPictureDialogFragment = new GetPictureDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("camera request code", cameraRequestCode);
        bundle.putInt("gallery request code", galleryRequestCode);
        getPictureDialogFragment.setArguments(bundle);
        getPictureDialogFragment.show(getSupportFragmentManager(), "Input email fragment");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        bitmap = null;
        selectedImagePath = null;
        if (resultCode == RESULT_OK && requestCode == AVATAR_CAMERA_REQUEST
                || resultCode == RESULT_OK && requestCode == CAR_CAMERA_REQUEST) {
            File f = new File(Environment.getExternalStorageDirectory().toString());
            for (File temp : f.listFiles()) {
                if (temp.getName().equals("temp.jpg")) {
                    f = temp;
                    break;
                }
            }
            if (!f.exists()) {
                Toast.makeText(getBaseContext(), "Error while capturing image", Toast.LENGTH_LONG).show();
                return;
            }
            try {
                bitmap = BitmapFactory.decodeFile(f.getAbsolutePath());
                if (requestCode == AVATAR_CAMERA_REQUEST) {
                    bitmap = Bitmap.createScaledBitmap(bitmap, 400, 400, true);
                }
                int rotate = 0;
                try {
                    ExifInterface exif = new ExifInterface(f.getAbsolutePath());
                    int orientation = exif.getAttributeInt(
                            ExifInterface.TAG_ORIENTATION,
                            ExifInterface.ORIENTATION_NORMAL);
                    switch (orientation) {
                        case ExifInterface.ORIENTATION_ROTATE_270:
                            rotate = 270;
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_180:
                            rotate = 180;
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_90:
                            rotate = 90;
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Matrix matrix = new Matrix();
                matrix.postRotate(rotate);
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                if (requestCode == AVATAR_CAMERA_REQUEST) {
                    mAvatarImageView.setImageBitmap(bitmap);
                    sendAvatarToServer(bitmap);
                } else if (requestCode == CAR_CAMERA_REQUEST) {
                    addPhotoToList(f.getAbsolutePath());
                    sendCarPhotoToServer(bitmap);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (resultCode == RESULT_OK && requestCode == AVATAR_GALLERY_REQUEST
                || resultCode == RESULT_OK && requestCode == CAR_GALLERY_REQUEST) {
            if (data != null) {
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(data.getData(), filePathColumn, null, null, null);
                if (cursor != null) {
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    selectedImagePath = cursor.getString(columnIndex);
                    cursor.close();
                }
                bitmap = BitmapFactory.decodeFile(selectedImagePath);
                if (requestCode == AVATAR_GALLERY_REQUEST) {
                    bitmap = Bitmap.createScaledBitmap(bitmap, 400, 400, false);
                    mAvatarImageView.setImageBitmap(bitmap);
                    sendAvatarToServer(bitmap);
                } else if (requestCode == CAR_GALLERY_REQUEST) {
                    addPhotoToList(selectedImagePath);
                    sendCarPhotoToServer(bitmap);
                }
            } else {
                Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void sendAvatarToServer(Bitmap bitmap) {
        //TODO add send method to presenter
    }

    private void sendCarPhotoToServer(Bitmap bitmap) {
        //TODO add send method to presenter
    }

    private void addPhotoToList(String uri) {
        List<String> photoList = ((ScreenSlidePagerAdapter) mViewPager.getAdapter()).getPhotoList();
        photoList.add(uri);
        ((ScreenSlidePagerAdapter) mViewPager.getAdapter()).setPhotoList(photoList);
        mViewPager.setCurrentItem(mViewPager.getAdapter().getCount());
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

    @Override
    public void setCarsInfo(GetCarsResponse carsInfo) {
        Item car = carsInfo.getResponse().getItems().get(0);
        String carName = car.getMark() + " " + car.getModel() + " " + car.getYear();
        mCarTitleEditText.setText(carName);
        mDayPriceEditText.setText(String.valueOf(car.getDayPrice()));
        mWeekPriceEditText.setText(String.valueOf(car.getWeekPrice()));
        mMonthPriceEditText.setText(String.valueOf(car.getMonthPrice()));
        List<String> photoUrls = new ArrayList<>();
        for (Photo photo : car.getPhotos()) {
            photoUrls.add(photo.getImage600x360());
        }
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(), photoUrls);
        mViewPager.setAdapter(mPagerAdapter);

    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        private List<String> mPhotoList;

        public ScreenSlidePagerAdapter(FragmentManager fragmentManager, List<String> photos) {
            super(fragmentManager);
            mPhotoList = photos;
        }

        @Override
        public Fragment getItem(int position) {
            Bundle bundle = new Bundle();
            bundle.putString("url", mPhotoList.get(position));
            ScreenSlidePageFragment screenSlidePageFragment = new ScreenSlidePageFragment();
            screenSlidePageFragment.setArguments(bundle);
            return screenSlidePageFragment;
        }

        @Override
        public int getCount() {
            return mPhotoList.size();
        }

        public List<String> getPhotoList() {
            return mPhotoList;
        }

        public void setPhotoList(List<String> photoList) {
            mPhotoList = photoList;
            notifyDataSetChanged();
        }
    }

}
