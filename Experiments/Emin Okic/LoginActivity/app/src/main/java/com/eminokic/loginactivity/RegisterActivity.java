package com.eminokic.loginactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity {
    EditText mtextUsername;
    EditText mtextPassword;
    EditText mtextConfirmPassword;
    Button mButtonRegister;
    TextView mTextViewLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mtextUsername = (EditText)findViewById(R.id.edittext_username);
        mtextPassword = (EditText)findViewById(R.id.edittext_password);
        mtextConfirmPassword = (EditText)findViewById(R.id.edittext_confirm_password);
        mButtonRegister = (Button)findViewById(R.id.button_login);
        mTextViewLogin = (TextView)findViewById(R.id.textview_register);
        mTextViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent LoginIntent = new Intent(RegisterActivity.this,MainActivity.class);
                startActivity(LoginIntent);
            }
        });

    }
}
