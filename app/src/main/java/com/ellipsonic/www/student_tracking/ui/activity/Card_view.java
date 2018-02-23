package com.ellipsonic.www.student_tracking.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ellipsonic.www.student_tracking.R;

public class Card_view extends AppCompatActivity  implements View.OnClickListener {

    TextView textcart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_view);
        textcart= (TextView) findViewById(R.id.card_view);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.card_view:
                Intent intent = new Intent(getApplicationContext(), com.ellipsonic.www.student_tracking.ui.activity.RegisterActivity.class);
                startActivity(intent);
                break;

        }
    }



}
