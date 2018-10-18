package com.sampullman.solarlight;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.threedbj.viewbuilder.LinearLayoutBuilder;
import com.threedbj.viewbuilder.TextViewBuilder;

import static android.view.Gravity.CENTER;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout root = new LinearLayoutBuilder()
            .vertical()
            .layoutGravity(CENTER)
            .height(WRAP_CONTENT)
            .build(this);

        new TextViewBuilder()
            .inLinear()
            .height(WRAP_CONTENT)
            .gravity(CENTER)
            .paddingDp(0, 10, 0, 10)
            .textSize(22)
            .text("Solar Light Tester")
            .build(root);

        setContentView(root);
    }
}
