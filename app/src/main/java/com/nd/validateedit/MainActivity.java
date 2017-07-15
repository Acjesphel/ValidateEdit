package com.nd.validateedit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private UserValidateInputView et_validate_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et_validate_code = (UserValidateInputView) findViewById(R.id.et_validate_code);

    }
}
