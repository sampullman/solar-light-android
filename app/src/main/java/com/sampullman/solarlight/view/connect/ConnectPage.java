package com.sampullman.solarlight.view.connect;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.threedbj.viewbuilder.FrameLayoutBuilder;
import com.threedbj.viewbuilder.LinearLayoutBuilder;
import com.threedbj.viewbuilder.ListViewBuilder;
import com.threedbj.viewbuilder.TextViewBuilder;
import com.threedbj.viewbuilder.style.Style;

import timber.log.Timber;

import static android.view.Gravity.CENTER;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class ConnectPage {
    private ConnectPageInterface pageInterface;
    TextView listTitle;

    public interface ConnectPageInterface {
        LeListAdapter getLeAdapter();
        void onItemClick(BluetoothDevice device);
    }

    public ConnectPage(ConnectPageInterface pageInterface) {
        this.pageInterface = pageInterface;
    }

    public View getView(Context context) {
        final LeListAdapter leAdapter = pageInterface.getLeAdapter();
        leAdapter.setLeItemClickListener((device) -> pageInterface.onItemClick(device));

        LinearLayout root = new LinearLayoutBuilder()
            .vertical()
            .layoutGravity(CENTER)
            .height(WRAP_CONTENT)
            .build(context);

        listTitle = new TextViewBuilder()
            .inLinear()
            .height(80)
            .build(root);

        new ListViewBuilder()
            .adapter(leAdapter)
            .build(new FrameLayoutBuilder()
                .style(Style.MATCH)
                .build(root));

        return root;
    }

    public void setListTitle(String title) {
        listTitle.setText(title);
    }
}
