package component;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.example.meshwar.R;

public class YesNoDialog extends AlertDialog.Builder{
    View dialogView;
    AlertDialog alert ;


    private TextView txtPositiveButtonText , txtNegativeButtonText , txtMessage;
    public YesNoDialog(Context context,String title ,  String message , boolean flag ) {
        super(context);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_yes_no_dialog, null);
        txtPositiveButtonText = dialogView.findViewById(R.id.txtPositiveButtonText);
        txtNegativeButtonText = dialogView.findViewById(R.id.txtNegativeButtonText);
        txtMessage = dialogView.findViewById(R.id.txtMessage);

        this.setView(dialogView);

        builder.setCancelable(flag);
setTxtMessage(message);

alert = this.show();
        //Setting the title manually
        alert.setTitle(title);

    }
    public void close(){
        alert.dismiss();
    }

    public void setTxtPositiveButtonText(String txtPositiveButtonText) {
        this.txtPositiveButtonText.setText( txtPositiveButtonText);
    }

    public void setTxtNegativeButtonText(String txtNegativeButtonText) {
        this.txtNegativeButtonText.setText( txtNegativeButtonText);
    }

    public void setTxtMessage(String txtMessage) {
        this.txtMessage.setText(txtMessage);
    }
    public void setPositiveButtonClickListener( View.OnClickListener positiveButton){
        txtPositiveButtonText.setOnClickListener(positiveButton);
    }
    public void setNegativeButtonClickListener( View.OnClickListener negativeButton){
        txtNegativeButtonText.setOnClickListener(negativeButton);
    }
}
