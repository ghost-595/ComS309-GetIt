package com.iastate.edu.coms309.sb4.getit.client.screens.admin;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.iastate.edu.coms309.sb4.getit.client.R;

public class ManageAccounts extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_accounts);

        //Configure back button
        Button backButton = (Button) findViewById(R.id.nav_back_manage_accounts);
        BackButtonListener backListener = new BackButtonListener();
        backButton.setOnClickListener(backListener);
    }

    public void goBack() {
        this.finish();
    }

    private class BackButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            goBack();
        }

    }
}
