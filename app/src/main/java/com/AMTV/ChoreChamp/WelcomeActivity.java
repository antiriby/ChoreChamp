package com.AMTV.ChoreChamp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener {
    private Button login, register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        login = (Button) findViewById(R.id.btnWelcomeLogin);
        login.setOnClickListener(this);

        register = (Button) findViewById(R.id.btnWelcomeRegister);
        register.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnWelcomeLogin:
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case R.id.btnWelcomeRegister:
                startActivity(new Intent(this,RegisterActivity.class));
                break;
        }
    }
}