package component;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.example.yallatour.R;

import util.Constant;

public class PickImageDialog extends AlertDialog.Builder {
    private final LinearLayout pickFromCamera;
    private final LinearLayout pickFromGallery;
    AlertDialog alertDialog ;
    public PickImageDialog(@NonNull Context context , boolean flag) {
        super(context);

        this.setCancelable(flag);
        this.setTitle("Pick a profile image");
        final View dialogView = LayoutInflater.from(context).inflate(R.layout.pick_image_dialog_layout, null);
        this.setView(dialogView);
        alertDialog = this.show();
        pickFromCamera  = dialogView.findViewById(R.id.pickFromCamera);
       pickFromGallery = dialogView.findViewById(R.id.pickFromGallery);
    }

    public LinearLayout getPickFromCamera() {
        return pickFromCamera;
    }

    public LinearLayout getPickFromGallery() {
        return pickFromGallery;
    }
    public void dismiss(){
        alertDialog.dismiss();
    }
}


