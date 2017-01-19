//package ru.nadocars.messanger.ui.profile;
//
//import android.app.Activity;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.widget.CheckBox;
//import android.widget.CompoundButton;
//
//import ru.nadocars.messanger.R;
//
//public class MyActivity extends AppCompatActivity {
//
//    private CheckBox mCheckBox;
//    private Menu mMenu;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_my);
//        findViews();
//        setListeners();
//    }
//
//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        mMenu = menu;
//        menu.setGroupVisible(R.id.group1, mCheckBox.isChecked());
//        return super.onPrepareOptionsMenu(menu);
//    }
//
//    private void findViews() {
//        mCheckBox = (CheckBox) findViewById(R.id.checkBox);
//    }
//
//    private void setListeners() {
//        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                mMenu.setGroupVisible(R.id.group1, mCheckBox.isChecked());
//            }
//        });
//
//    }
//
//}
