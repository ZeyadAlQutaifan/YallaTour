package component;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.example.yallatour.R;

public class InputPasswordDialog extends AlertDialog.Builder {
    String inputText ;
    EditText editText ;
    Button btn ;
    AlertDialog alertDialog ;
    public InputPasswordDialog(@NonNull Context context ,  String title , String message , boolean flag ) {
        super(context);

        this.setCancelable(flag);
        this.setTitle(title);
        final View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_input_password, null);
        this.setView(dialogView);
        alertDialog = this.show();
       btn = dialogView.findViewById(R.id.btnSubmit);
        editText = dialogView.findViewById(R.id.etPassword) ;
        TextView txtMessage = dialogView.findViewById(R.id.txtMessage);
        txtMessage.setText(message);

    }
    public String getInputText(){
        return inputText;
    }
    public Button getButton(){
        return btn;
    }
}
