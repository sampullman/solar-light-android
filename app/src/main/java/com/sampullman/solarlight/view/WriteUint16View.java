package com.sampullman.solarlight.view;

import android.content.Context;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.threedbj.viewbuilder.ButtonBuilder;
import com.threedbj.viewbuilder.EditTextBuilder;
import com.threedbj.viewbuilder.LinearLayoutBuilder;
import com.threedbj.viewbuilder.TextViewBuilder;

import static android.view.Gravity.CENTER;


public class WriteUint16View extends LinearLayoutBuilder {
    private EditText int16View;
    private String title, buttonText;
    private int value;

    public WriteUint16View(String title, String buttonText) {
        this.title = title;
        this.buttonText = buttonText;
        horizontal().weightSum(7f);
    }

    public LinearLayout build(Context c) {
        LinearLayout layout = super.build(c, new LinearLayout(c));

        new TextViewBuilder()
            .inLinear()
            .width(0)
            .weight(2)
            .text(buttonText)
            .gravity(CENTER)
            .build(layout);

        new EditTextBuilder()
            .inLinear()
            .width(0)
            .weight(3)
            .marginRight(8)
            .build(layout);

        new ButtonBuilder()
            .inLinear()
            .text(title)
            .width(0)
            .weight(2)
            .build(layout);

        return layout;
    }

}
