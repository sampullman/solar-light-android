package com.sampullman.solarlight;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.sampullman.solarlight.view.TestPage;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(new TestPage().getView(this));
    }
}
