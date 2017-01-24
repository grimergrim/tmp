package ru.nadocars.messanger.ui.profile;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ru.nadocars.messanger.R;
import ru.nadocars.messanger.api.BundleApi;
import ru.nadocars.messanger.api.SharedPreferencesApi;
import ru.nadocars.messanger.asynctasks.LogOutTask;
import ru.nadocars.messanger.data.repo.ContactsRepo;
import ru.nadocars.messanger.data.repo.MessagesRepo;
import ru.nadocars.messanger.json.car.Item;
import ru.nadocars.messanger.json.car.Photo;
import ru.nadocars.messanger.json.car.calendar.GetCarCalendarResponse;
import ru.nadocars.messanger.json.user.GetUserResponse;
import ru.nadocars.messanger.ui.navigation.Navigator;
import ru.nadocars.messanger.ui.navigation.NavigatorImpl;

import static ru.nadocars.messanger.R.id.code;

public class ProfileActivity extends AppCompatActivity implements ProfileView {

    protected static final int AVATAR_CAMERA_REQUEST = 0;
    protected static final int AVATAR_GALLERY_REQUEST = 1;
    protected static final int CAR_CAMERA_REQUEST = 2;
    protected static final int CAR_GALLERY_REQUEST = 3;

    private ProfilePresenter mProfilePresenter;
    private Navigator mNavigator;

    private ViewPager mViewPager;
    private long mVerificationCode;
    private int mCarCounter;
    private String mCurrentCarId;
    private String mEmail;
    private String mPhone;
    private String mDayPrice;
    private String mWeekPrice;
    private String mMonthPrice;
    private String mSessionId;
    private String mToken;

    private Calendar mToDateCalendar;
    private Calendar mFromDateCalendar;
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
    private TextView mCarTitleEditText;
    private Button mAddPhotoButton;
    private Button mDeletePhotoButton;
    private Button mNextCarButton;
    private LinearLayout mToDateLinearLayout;
    private TextView mToDateTextView;
    private LinearLayout mFromDateLinearLayout;
    private TextView mFromDateTextView;
    private Spinner mFromSpinner;
    private Spinner mToSpinner;
    private Button mMarkDaysButton;
    private EditText mDayPriceEditText;
    private TextView mDayPriceTitleTextView;
    private EditText mWeekPriceEditText;
    private TextView mWeekPriceTitleTextView;
    private EditText mMonthPriceEditText;
    private TextView mMonthPriceTitleTextView;
    private Button mExitbutton;
    private Button mGoToWebButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProfilePresenter = ProfilePresenterImpl.getPreLoginPresenter();
        mProfilePresenter.setView(this);
        mNavigator = new NavigatorImpl();
        mCarCounter = 0;
        mToDateCalendar = Calendar.getInstance();
        mFromDateCalendar = Calendar.getInstance();
        mToken = PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                .getString(SharedPreferencesApi.TOKEN, null);
        setContentView(R.layout.activity_profile);
        findViews();
        setListeners();
        initializeSpinners();
        if (mCarsConstraintLayout != null) {
            mCarsConstraintLayout.setVisibility(View.GONE);
        }
        if (null != mToken) {
            mProfilePresenter.getUserInfo(mToken);
            mProfilePresenter.getCars(mToken);
        }
        if (null != mUserAvatar) {
            mUserAvatar.setVisibility(View.GONE);
        }
        if (null != mEmailTextView) {
            mEmailTextView.setFocusable(false);

        }
        if (null != mPhoneTextView) {
            mPhoneTextView.setFocusable(false);
        }
        if ((null != mDayPriceEditText)) {
            mDayPriceEditText.setFocusable(false);
        }
        if ((null != mWeekPriceEditText)) {
            mWeekPriceEditText.setFocusable(false);
        }
        if ((null != mMonthPriceEditText)) {
            mMonthPriceEditText.setFocusable(false);
        }
        if (null != mCodeLinearLayout) {
            mCodeLinearLayout.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(BundleApi.VERIFICATION_CODE, mVerificationCode);
        outState.putInt(BundleApi.CAR_COUNTER, mCarCounter);
        outState.putString(BundleApi.CURRENT_CAR_ID, mCurrentCarId);
        outState.putString(BundleApi.EMAIL, mEmail);
        outState.putString(BundleApi.PHONE, mPhone);
        outState.putString(BundleApi.DAY_PRICE, mDayPrice);
        outState.putString(BundleApi.WEEK_PRICE, mWeekPrice);
        outState.putString(BundleApi.MONTH_PRICE, mMonthPrice);
        outState.putString(BundleApi.SESSION_ID, mSessionId);
        outState.putString(BundleApi.TOKEN, mToken);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mVerificationCode = savedInstanceState.getLong(BundleApi.VERIFICATION_CODE);
        mCarCounter = savedInstanceState.getInt(BundleApi.CAR_COUNTER);
        mCurrentCarId = savedInstanceState.getString(BundleApi.CURRENT_CAR_ID);
        mEmail = savedInstanceState.getString(BundleApi.EMAIL);
        mPhone = savedInstanceState.getString(BundleApi.PHONE);
        mDayPrice = savedInstanceState.getString(BundleApi.DAY_PRICE);
        mWeekPrice = savedInstanceState.getString(BundleApi.WEEK_PRICE);
        mMonthPrice = savedInstanceState.getString(BundleApi.MONTH_PRICE);
        mSessionId = savedInstanceState.getString(BundleApi.SESSION_ID);
        mToken = savedInstanceState.getString(BundleApi.TOKEN);

    }

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
        mAddPhotoButton = (Button) findViewById(R.id.add_photo);
        mDeletePhotoButton = (Button) findViewById(R.id.remove_photo);
        mNextCarButton = (Button) findViewById(R.id.next_car);
        mToDateLinearLayout = (LinearLayout) findViewById(R.id.to_date);
        mToDateTextView = (TextView) findViewById(R.id.to_date_text_view);
        mFromDateLinearLayout = (LinearLayout) findViewById(R.id.from_date);
        mFromDateTextView = (TextView) findViewById(R.id.from_date_text_view);
        mFromSpinner = (Spinner) findViewById(R.id.from_spinner);
        mToSpinner = (Spinner) findViewById(R.id.to_spinner);
        mMarkDaysButton = (Button) findViewById(R.id.mark_days_button);
        mDayPriceEditText = (EditText) findViewById(R.id.day_price);
        mDayPriceTitleTextView = (TextView) findViewById(R.id.day_price_title);
        mWeekPriceEditText = (EditText) findViewById(R.id.week_price);
        mWeekPriceTitleTextView = (TextView) findViewById(R.id.week_price_title);
        mMonthPriceEditText = (EditText) findViewById(R.id.month_price);
        mMonthPriceTitleTextView = (TextView) findViewById(R.id.month_price_title);
        mGoToWebButton = (Button) findViewById(R.id.go_to_web);
        mExitbutton = (Button) findViewById(R.id.exit_button);
    }

    private void setListeners() {
        if (mExitbutton != null) {
            mExitbutton.setVisibility(View.VISIBLE);
            mExitbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    logOut();
                }
            });
        }
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
                if (null != mPhone && !mPhone.equals(s)) {
                    mUpdateButton.setVisibility(View.VISIBLE);
                }
            }
        });
        mDayPriceEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mDayPriceEditText.setFocusableInTouchMode(true);
                return false;
            }
        });
        mDayPriceEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (null != mDayPrice && !mDayPrice.equals(s)) {
                    mDayPriceTitleTextView.setText("Обновить");
                }
            }
        });
        mWeekPriceEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mWeekPriceEditText.setFocusableInTouchMode(true);
                return false;
            }
        });
        mWeekPriceEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (null != mWeekPrice && !mWeekPrice.equals(s)) {
                    mWeekPriceTitleTextView.setText("Обновить");
                    if (checkPrice(s.toString()) && (Double.parseDouble(s.toString())) == 0) {
                        mMonthPriceEditText.setText("0");
                    }
                }
            }
        });
        mMonthPriceEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mMonthPriceEditText.setFocusableInTouchMode(true);
                return false;
            }
        });
        mMonthPriceEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (null != mMonthPrice && !mMonthPrice.equals(s) && !s.toString().equals("0")) {
                    mMonthPriceTitleTextView.setText("Обновить");
                    if (checkPrice(mWeekPriceEditText.getText().toString())
                            && (Double.parseDouble(mWeekPriceEditText.getText().toString())) == 0) {
                        mMonthPriceEditText.setText("0");
                    }
                }
            }
        });
        mDayPriceTitleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDayPriceTitleTextView.getText().toString().equals("Обновить")
                        && checkPrice(mDayPriceEditText.getText().toString())) {
                    if (!(Double.parseDouble(mDayPriceEditText.getText().toString()) > 0)) {
                        showError("Цена должна быть больше 0");
                    } else {
                        mProfilePresenter.updateCarPrice(mToken, mCurrentCarId, "day",
                                mDayPriceEditText.getText().toString());
                    }
                }
            }
        });
        mWeekPriceTitleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mWeekPriceTitleTextView.getText().toString().equals("Обновить")
                        && checkPrice(mDayPriceEditText.getText().toString())) {
                    mProfilePresenter.updateCarPrice(mToken, mCurrentCarId, "week",
                            mWeekPriceEditText.getText().toString());
                }
            }
        });
        mMonthPriceTitleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMonthPriceTitleTextView.getText().toString().equals("Обновить")
                        && checkPrice(mDayPriceEditText.getText().toString())) {
                    mProfilePresenter.updateCarPrice(mToken, mCurrentCarId, "month",
                            mMonthPriceEditText.getText().toString());
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
                if (null != mCodeEditText.getText() && mCodeEditText.getText().toString().equals(String.valueOf(mVerificationCode))) {
                    mProfilePresenter.updateUserInfo(mEmail, mPhone, mToken, mSessionId, mVerificationCode);
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
        mDeletePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProfilePresenter.deleteCarPhoto(mToken, mCurrentCarId,
                        mProfilePresenter.getGetCarsResponse().getResponse().getItems().get(mCarCounter - 1).getPhotos()
                                .get(mViewPager.getCurrentItem()).getId());
            }
        });
        mNextCarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCarInfo();
            }
        });
        mToDateLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(ProfileActivity.this, toDateSetListener, mToDateCalendar
                        .get(Calendar.YEAR), mToDateCalendar.get(Calendar.MONTH),
                        mToDateCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        mFromDateLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(ProfileActivity.this, fromDateSetListener, mFromDateCalendar
                        .get(Calendar.YEAR), mFromDateCalendar.get(Calendar.MONTH),
                        mFromDateCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        mMarkDaysButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProfilePresenter.sendBusyDays(mToken, mCurrentCarId,
                        mFromDateTextView.getText().toString(),
                        mFromSpinner.getSelectedItem().toString(),
                        mToDateTextView.getText().toString(),
                        mToSpinner.getSelectedItem().toString());
            }
        });
        mGoToWebButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://nadocars.ru/profile/"));
                startActivity(intent);
            }
        });
    }

    private boolean checkPrice(String price) {
        if (!price.matches("[0-9]+")) {
            showError("Только цифры");
            return false;
        } else if (!(price.length() > 0)) {
            showError("Укажите цену");
            return false;
        } else {
            return true;
        }
    }

    private void initializeSpinners() {
        String[] fromTimeslist = getResources().getStringArray(R.array.time_array_from);
        ArrayAdapter<String> fromDataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, fromTimeslist);
        fromDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mFromSpinner.setAdapter(fromDataAdapter);
        String[] toTimeslist = getResources().getStringArray(R.array.time_array_to);
        ArrayAdapter<String> toDataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, toTimeslist);
        toDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mToSpinner.setAdapter(toDataAdapter);
    }

    private DatePickerDialog.OnDateSetListener toDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mToDateCalendar.set(Calendar.YEAR, year);
            mToDateCalendar.set(Calendar.MONTH, monthOfYear);
            mToDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setToDateTextView();
        }
    };

    private DatePickerDialog.OnDateSetListener fromDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mFromDateCalendar.set(Calendar.YEAR, year);
            mFromDateCalendar.set(Calendar.MONTH, monthOfYear);
            mFromDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            fromToDateTextView();
        }
    };

    private void setToDateTextView() {
        String myFormat = "dd.MM.yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        mToDateTextView.setText(sdf.format(mToDateCalendar.getTime()));
    }

    private void fromToDateTextView() {
        String myFormat = "dd.MM.yyyy"; //In which you need put here
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(myFormat, Locale.US);
        mFromDateTextView.setText(simpleDateFormat.format(mFromDateCalendar.getTime()));
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
        Bitmap bitmap;
        String selectedImagePath = null;
        if (resultCode == RESULT_OK && requestCode == AVATAR_CAMERA_REQUEST
                || resultCode == RESULT_OK && requestCode == CAR_CAMERA_REQUEST) {
            File file = new File(Environment.getExternalStorageDirectory().toString());
            for (File temp : file.listFiles()) {
                if (temp.getName().equals("temp.jpg")) {
                    file = temp;
                    break;
                }
            }
            if (!file.exists()) {
                Toast.makeText(getBaseContext(), "Error while capturing image", Toast.LENGTH_LONG).show();
                return;
            }
            try {

//                int rotate = 0;
//                try {
//                    ExifInterface exif = new ExifInterface(file.getAbsolutePath());
//                    int orientation = exif.getAttributeInt(
//                            ExifInterface.TAG_ORIENTATION,
//                            ExifInterface.ORIENTATION_NORMAL);
//                    switch (orientation) {
//                        case ExifInterface.ORIENTATION_ROTATE_270:
//                            rotate = 270;
//                            break;
//                        case ExifInterface.ORIENTATION_ROTATE_180:
//                            rotate = 180;
//                            break;
//                        case ExifInterface.ORIENTATION_ROTATE_90:
//                            rotate = 90;
//                            break;
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                Matrix matrix = new Matrix();
//                matrix.postRotate(rotate);
//                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                if (requestCode == AVATAR_CAMERA_REQUEST) {
                    bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                    //TODO добавить сохранение пропорции при смене размера изображения
                    //TODO добавить разворот
                    bitmap = Bitmap.createScaledBitmap(bitmap, 400, 400, true);
                    mAvatarImageView.setImageBitmap(bitmap);
                    selectedImagePath = saveResizedImage(bitmap, file.getAbsolutePath());
                    if (null != selectedImagePath) {
                        sendAvatarToServer(selectedImagePath);
                    }
                } else {
                    sendCarPhotoToServer(file.getAbsolutePath());
                    addPhotoToList(file.getAbsolutePath());
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
                    //TODO добавить сохранение пропорции при смене размера изображения
                    //TODO добавить разворот
                    bitmap = Bitmap.createScaledBitmap(bitmap, 400, 400, false);
                    mAvatarImageView.setImageBitmap(bitmap);
                    selectedImagePath = saveResizedImage(bitmap, selectedImagePath);
                    if (null != selectedImagePath) {
                        sendAvatarToServer(selectedImagePath);
                    }
                } else {
                    sendCarPhotoToServer(selectedImagePath);
                    addPhotoToList(selectedImagePath);
                }
            } else {
                Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String saveResizedImage(Bitmap bitmap, String selectedImagePath) {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(selectedImagePath);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            return selectedImagePath;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendAvatarToServer(String uri) {
        mProfilePresenter.uploadAvatar(mToken, uri);
    }

    private void sendCarPhotoToServer(String uri) {
        mProfilePresenter.uploadCarPhoto(mToken, mCurrentCarId, uri);
    }

    private void addPhotoToList(String uri) {
        List<String> photoList = ((ScreenSlidePagerAdapter) mViewPager.getAdapter()).getPhotoList();
        photoList.add(uri);
        ((ScreenSlidePagerAdapter) mViewPager.getAdapter()).setPhotoList(photoList);
        mViewPager.setCurrentItem(mViewPager.getAdapter().getCount());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mProfilePresenter.setView(null);
    }

    @Override
    public void setProfileInfo(GetUserResponse profileInfo) {
        if (null != profileInfo) {
            mEmail = profileInfo.getResponse().getEmail();
            mPhone = profileInfo.getResponse().getPhone();
            mMoneyTextView.setText(String.valueOf(profileInfo.getResponse().getBalance()) + " р.");
            String fullName = "";
            String firstName = "";
            String secondName = "";
            String thirdName = "";
            if (null != profileInfo.getResponse().getFirstName() && profileInfo.getResponse().getFirstName().length() > 0) {
                firstName = profileInfo.getResponse().getFirstName();
            }
            if (null != profileInfo.getResponse().getLastName() && profileInfo.getResponse().getLastName().length() > 0) {
                secondName = profileInfo.getResponse().getLastName();
            }
            if (null != profileInfo.getResponse().getMiddleName() && profileInfo.getResponse().getMiddleName().length() > 0) {
                thirdName = profileInfo.getResponse().getMiddleName();
            }
            mNameTextView.setText(secondName + " " + firstName + " " + thirdName);
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
        mVerificationCode = code;
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
    public void setCarsInfo() {
//        mGetCarsResponse = carsInfo;
        setCarInfo();
    }

    @Override
    public void setBusyDays(final GetCarCalendarResponse getCarCalendarResponse) {
        if (null != mCalendarView) {
            mCalendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_NONE);
            ArrayList<DayViewDecorator> dayViewDecorators = new ArrayList<>();
            dayViewDecorators.add(new DayViewDecorator() {
                @Override
                public boolean shouldDecorate(CalendarDay day) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss",
                            getResources().getConfiguration().locale);
                    String fromDateAsString;
                    String toDateAsString;
                    Date fromDate;
                    Date toDate;
                    Calendar calendarFrom = Calendar.getInstance();
                    Calendar calendarTo = Calendar.getInstance();
                    for (ru.nadocars.messanger.json.car.calendar.Item item : getCarCalendarResponse.getResponse().getItems()) {
                        fromDateAsString = item.getBusyFrom();
                        toDateAsString = item.getBusyTo();
                        try {
                            fromDate = dateFormat.parse(fromDateAsString);
                            toDate = dateFormat.parse(toDateAsString);
                            calendarFrom.setTime(fromDate);
                            calendarTo.setTime(toDate);
                            int busyYearFrom = calendarFrom.get(Calendar.YEAR);
                            int busyMonthFrom = calendarFrom.get(Calendar.MONTH);
                            int busyDayFrom = calendarFrom.get(Calendar.DAY_OF_MONTH);
                            int busyYearTo = calendarTo.get(Calendar.YEAR);
                            int busyMonthTo = calendarTo.get(Calendar.MONTH);
                            int busyDayTo = calendarTo.get(Calendar.DAY_OF_MONTH);
                            int calendarYear = day.getYear();
                            int calendarMonth = day.getMonth();
                            int calendarDay = day.getDay();
                            if (calendarYear >= busyYearFrom && calendarYear <= busyYearTo &&
                                    calendarMonth >= busyMonthFrom && calendarMonth <= busyMonthTo &&
                                    calendarDay >= busyDayFrom && calendarDay <= busyDayTo) {
                                return true;
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    return false;
                }

                @Override
                public void decorate(DayViewFacade view) {
                    view.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(),
                            R.drawable.shape_calendar_busy_day));
                }
            });
            dayViewDecorators.add(new DayViewDecorator() {
                @Override
                public boolean shouldDecorate(CalendarDay day) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss",
                            getResources().getConfiguration().locale);
                    String fromDateAsString;
                    String toDateAsString;
                    Date fromDate;
                    Date toDate;
                    Calendar calendarFrom = Calendar.getInstance();
                    Calendar calendarTo = Calendar.getInstance();
                    for (ru.nadocars.messanger.json.car.calendar.Item item : getCarCalendarResponse.getResponse().getItems()) {
                        fromDateAsString = item.getBusyFrom();
                        toDateAsString = item.getBusyTo();
                        try {
                            fromDate = dateFormat.parse(fromDateAsString);
                            toDate = dateFormat.parse(toDateAsString);
                            calendarFrom.setTime(fromDate);
                            calendarTo.setTime(toDate);
                            int busyYearFrom = calendarFrom.get(Calendar.YEAR);
                            int busyMonthFrom = calendarFrom.get(Calendar.MONTH);
                            int busyDayFrom = calendarFrom.get(Calendar.DAY_OF_MONTH);
                            int busyYearTo = calendarTo.get(Calendar.YEAR);
                            int busyMonthTo = calendarTo.get(Calendar.MONTH);
                            int busyDayTo = calendarTo.get(Calendar.DAY_OF_MONTH);
                            int calendarYear = day.getYear();
                            int calendarMonth = day.getMonth();
                            int calendarDay = day.getDay();
                            if (calendarYear >= busyYearFrom && calendarYear <= busyYearTo &&
                                    calendarMonth >= busyMonthFrom && calendarMonth <= busyMonthTo &&
                                    calendarDay >= busyDayFrom && calendarDay <= busyDayTo) {
                                return false;
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    return true;
                }

                @Override
                public void decorate(DayViewFacade view) {
                    view.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(),
                            R.drawable.shape_calendar_free_day));
                }
            });
            mCalendarView.addDecorators(dayViewDecorators);
        }
    }

    @Override
    public void updateCalendar() {
        mProfilePresenter.getCarCalendar(mCurrentCarId);
    }

    @Override
    public void updatePriceStatus(String viewName) {
        switch (viewName) {
            case "day":
                mDayPriceTitleTextView.setText("в день");
                break;
            case "week":
                mWeekPriceTitleTextView.setText("в неделю");
                break;
            case "month":
                mMonthPriceTitleTextView.setText("в месяц");
                break;
        }
    }

    @Override
    public void setCarPhotoId(String photoId) {
        Photo photo = new Photo();
        photo.setId(photoId);
        List<Photo> photos = mProfilePresenter.getGetCarsResponse().getResponse().getItems().get(mCarCounter - 1).getPhotos();
        photos.add(photo);
        mProfilePresenter.getGetCarsResponse().getResponse().getItems().get(mCarCounter - 1).setPhotos(photos);
        System.out.println();
    }

    @Override
    public void deletePhotoFromViewPager(String carId, String photoId) {
        int y = -1;
        for (Item item : mProfilePresenter.getGetCarsResponse().getResponse().getItems()) {
            if (item.getId().equals(carId)) {
                List<Photo> photos = new ArrayList<>();
                for (int i = 0; i < item.getPhotos().size(); i++) {
                    if (!item.getPhotos().get(i).getId().equals(photoId)) {
                        photos.add(item.getPhotos().get(i));
                    } else {
                        y = i;
                    }
                }
                item.setPhotos(photos);
            }
        }
        List<String> photoList = ((ScreenSlidePagerAdapter) mViewPager.getAdapter()).getPhotoList();
        List<String> photoList2 = new ArrayList<>();
        if (y != -1) {
            for (int i = 0; i < photoList.size(); i++) {
                if (i != y) {
                    photoList2.add(photoList.get(i));
                }
            }
            ((ScreenSlidePagerAdapter) mViewPager.getAdapter()).setPhotoList(photoList2);
            if (y > 0) {
                mViewPager.setCurrentItem(y - 1);
            }
        }
    }

    private void setCarInfo() {
        mCurrentCarId = mProfilePresenter.getGetCarsResponse().getResponse().getItems().get(mCarCounter).getId();
        Item car = mProfilePresenter.getGetCarsResponse().getResponse().getItems().get(mCarCounter);
        mProfilePresenter.getCarCalendar(car.getId());
        String carName = car.getMark() + " " + car.getModel() + " " + car.getYear();
        mCarTitleEditText.setText(carName);
        mDayPriceEditText.setText(String.valueOf(car.getDayPrice()));
        mWeekPriceEditText.setText(String.valueOf(car.getWeekPrice()));
        mMonthPriceEditText.setText(String.valueOf(car.getMonthPrice()));
        mDayPrice = String.valueOf(car.getDayPrice());
        mWeekPrice = String.valueOf(car.getWeekPrice());
        mMonthPrice = String.valueOf(car.getMonthPrice());
        List<String> photoUrls = new ArrayList<>();
        for (Photo photo : car.getPhotos()) {
            photoUrls.add(photo.getImage600x360());
        }
        PagerAdapter pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(), photoUrls);
        mViewPager.setAdapter(pagerAdapter);
        if (mCarCounter < mProfilePresenter.getGetCarsResponse().getResponse().getItems().size() - 1) {
            mCarCounter++;
        } else {
            mCarCounter = 0;
        }
    }

}
