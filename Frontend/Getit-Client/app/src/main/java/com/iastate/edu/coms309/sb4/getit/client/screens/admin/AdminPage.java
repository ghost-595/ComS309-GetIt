package com.iastate.edu.coms309.sb4.getit.client.screens.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.iastate.edu.coms309.sb4.getit.client.R;

public class AdminPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);

        //Configure back button
        Button backButton = (Button) findViewById(R.id.nav_back_admin);
        BackButtonListener backListener = new BackButtonListener();
        backButton.setOnClickListener(backListener);

        //Create button
        Button manageAccountsButton = (Button) findViewById(R.id.nav_manage_accounts);

        //Create listener
        ManageAccountsButtonListener manageAccountsListener = new ManageAccountsButtonListener();

        //Attach listener to button
        manageAccountsButton.setOnClickListener(manageAccountsListener);
    }

    //Navigation functions

    public void goBack() {
        this.finish();
    }

    private class BackButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            goBack();
        }

    }

    public void doManageAccounts() {
        Intent intent = new Intent(this, ManageAccounts.class);
        startActivity(intent);
    }

    private class ManageAccountsButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            doManageAccounts();
        }

    }
}
