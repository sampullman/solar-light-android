package com.sampullman.solarlight.view;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.threedbj.viewbuilder.LinearLayoutBuilder;
import com.threedbj.viewbuilder.TextViewBuilder;

import static android.view.Gravity.CENTER;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class ReadIntView extends LinearLayoutBuilder {
    private TextView titleView;
    private TextView intView;
    private String title;
    private int value;

    public ReadIntView(String title) {
        this.title = title;
        vertical();
    }

    public void setValue(int value) {
        this.value = value;
        update();
    }

    private void update() {
        intView.setText(Integer.toString(value));
    }

    public LinearLayout build(Context c) {
        LinearLayout layout = super.build(c, new LinearLayout(c));

        if(title != null) {
            new TextViewBuilder()
                .inLinear()
                .height(WRAP_CONTENT)
                .gravity(CENTER)
                .textSize(15)
                .text(title)
                .build(layout);
        }

        intView = new TextViewBuilder()
            .inLinear()
            .height(WRAP_CONTENT)
            .gravity(CENTER)
            .textSize(18)
            .build(layout);
        update();

        return layout;
    }

}
