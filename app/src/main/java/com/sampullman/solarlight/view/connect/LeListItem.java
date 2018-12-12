package com.sampullman.solarlight.view.connect;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sampullman.solarlight.R;
import com.threedbj.viewbuilder.LinearLayoutBuilder;
import com.threedbj.viewbuilder.TextViewBuilder;
import com.threedbj.viewbuilder.ViewBuilder;
import com.threedbj.viewbuilder.generic.GenericViewBuilder;
import com.threedbj.viewbuilder.style.Style;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class LeListItem extends LinearLayoutBuilder {
    private static Item ITEM = new Item();
    public static class Item extends Style {
        public void apply(GenericViewBuilder builder) {
            builder.inLinear();
            builder.width(0);
            builder.height(WRAP_CONTENT);
        }
    }

    public static final int ITEM_ID = View.generateViewId();

    private TextView itemText;

    public LeListItem() {
        horizontal();
        style(Style.WIDE);
        height(50);
        backgroundColor(R.color.white);
        backgroundPressedColor(R.color.light_gray);
    }

    public LinearLayout build(Context c) {
        LinearLayout layout = super.build(c, new LinearLayout(c));
        layout.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);

        new ViewBuilder()
            .style(ITEM)
            .weight(0.2f)
            .build(layout);

        itemText = new TextViewBuilder()
            .style(ITEM)
            .weight(0.8f)
            .textSize(18f)
            .paddingDp(0, 9, 0, 9)
            .id(R.id.le_connect_list_item)
            .build(layout);
        return layout;
    }

}
