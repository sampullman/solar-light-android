package com.sampullman.solarlight.view;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.threedbj.viewbuilder.ButtonBuilder;
import com.threedbj.viewbuilder.LinearLayoutBuilder;
import com.threedbj.viewbuilder.TextViewBuilder;
import com.threedbj.viewbuilder.ViewBuilder;
import com.threedbj.viewbuilder.style.Style;

import static android.view.Gravity.CENTER;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class TestPage {
    private ReadIntView[] readViews = new ReadIntView[8];
    private WriteUint16View[] writeViews = new WriteUint16View[4];

    public View getView(Context context) {

        LinearLayout root = new LinearLayoutBuilder()
            .vertical()
            .layoutGravity(CENTER)
            .height(WRAP_CONTENT)
            .build(context);

        new TextViewBuilder()
            .inLinear()
            .height(WRAP_CONTENT)
            .gravity(CENTER)
            .paddingDp(0, 10, 0, 10)
            .textSize(22)
            .text("Solar Light Tester")
            .build(root);

        LinearLayoutBuilder rowBuilder = new LinearLayoutBuilder()
            .horizontal()
            .inLinear()
            .height(WRAP_CONTENT);

        makeReadIntRow(0, rowBuilder.build(root));

        makeReadIntRow(4, rowBuilder.marginTop(16).build(root));

        new ButtonBuilder()
            .inLinear()
            .width(MATCH_PARENT)
            .height(160)
            .marginTop(16)
            .text("REFRESH")
            .build(root);

        new ViewBuilder().height(24).build(root);

        for(int i = 0; i < writeViews.length; i += 1) {
            writeViews[i] = new WriteUint16View("UPDATE", String.format("Var %d", i+1));
            writeViews[i].marginTop(16).build(root);
        }

        return root;
    }

    private void makeReadIntRow(int startIndex, LinearLayout parent) {
        for(int i = startIndex; i < (startIndex + 4); i += 1) {
            readViews[i] = new ReadIntView(String.format("Int %d", i));
            readViews[i].style(Style.TALL)
                .inLinear()
                .build(parent);
        }
    }
}
