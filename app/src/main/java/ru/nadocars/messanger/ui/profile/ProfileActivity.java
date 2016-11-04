package ru.nadocars.messanger.ui.profile;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import ru.nadocars.messanger.R;
import ru.nadocars.messanger.ui.navigation.Navigator;
import ru.nadocars.messanger.ui.navigation.NavigatorImpl;

public class ProfileActivity extends AppCompatActivity implements ProfileView {

    ProfilePresenter mProfilePresenter;
    Navigator mNavigator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mProfilePresenter = ProfilePresenterImpl.getPreLoginPresenter();
        mProfilePresenter.setView(this);
        mNavigator = new NavigatorImpl();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mProfilePresenter.setView(null);
    }
}
