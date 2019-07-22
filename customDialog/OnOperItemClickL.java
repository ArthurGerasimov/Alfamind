package id.meteor.alfamind.customDialog;

import android.view.View;
import android.widget.AdapterView;

public interface OnOperItemClickL {
    void onOperItemClick(AdapterView<?> parent, View view, int position, long id);

    void onCancel();
}
