package util;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.webkit.MimeTypeMap;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.StringDef;
import androidx.appcompat.app.AlertDialog;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import modules.Dashboard;

public class Global {
    public static boolean validField(@NonNull List<TextView> input) {
        List<TextView> passwords = new ArrayList<>();
        for (TextView textView : input) {
            if (textView.getText().toString().isEmpty()) {
                return showTextError(textView, Constant.EMPTY_FIELD_MESSAGE);
            }

            if (textView.getInputType() == 33) {
                Pattern pat = Pattern.compile(Constant.REGEX);
                if (!pat.matcher(textView.getText().toString()).matches()) {
                    return showTextError(textView, Constant.INVALID_EMAIL_PATTERN_MESSAGE);
                }
            }

            if (textView.getInputType() == 129) {
                if (textView.getText().toString().length() < 6) {
                    return showTextError(textView, Constant.INVALID_PASSWORD_LENGTH_MESSAGE);
                }
                passwords.add(textView);
            }
        }
        for (int i = 0; i < passwords.size(); i++) {
            if (!passwords.get(0).getText().toString().equals(passwords.get(i).getText().toString())) {
                return showTextError(passwords.get(i), Constant.PASSWORD_DOES_NOT_MATCH_MESSAGE);

            }
        }

        return true;
    }
    public static boolean validPhone(String phone) {
        if (phone.isEmpty()) {
            return false;
        }
        if (!phone.matches("[0-9]+")){
            return false;
        }
        if (phone.length() != 13) {
            return false;
        }
        if (!(phone.charAt(0) == '+' && phone.charAt(1) == '9' && phone.charAt(2) == '6' && phone.charAt(3) == '2')) {
            return false;
        }
        if (!(phone.charAt(4) == '7' && (phone.charAt(5) == '7' || phone.charAt(5) == '8' || phone.charAt(5) == '9')))
        {
            return false;
        }
        return true;
    }
    public static boolean showTextError(TextView textView, String message) {
        textView.setError(message);
        textView.requestFocus();
        return false;
    }

    public static double distFrom(
            double lat1, double lng1, double lat2, double lng2)
    {
        double earthRadius = 3958.75;
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double dist = earthRadius * c;

        return dist ;
    }
    public static String getFileExtension(Uri uri , ContentResolver contentResolver) {
        ContentResolver cr = contentResolver;
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    public static  AlertDialog.Builder dialogYesNo(Context context,String title ,  String message , boolean flag , DialogInterface positiveButton, DialogInterface negativeButton ){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setCancelable(flag);

        builder.setPositiveButton("Ok", (DialogInterface.OnClickListener) positiveButton);
        builder.setNegativeButton("Cancel" , (DialogInterface.OnClickListener) negativeButton);
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle(title);
        alert.show();
        return builder;
    }
    public static AlertDialog.Builder dialogYesNo(Context context,String title ,  String message , boolean flag , DialogInterface.OnClickListener positiveButton ){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setCancelable(flag);

        builder.setPositiveButton("Ok",  positiveButton);
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle(title);
        alert.show();
        return builder;
    }
    public synchronized static DatabaseReference getUser(String id){
        return Constant.users.child(id);
    }

    public static String getNullString(String title) {
        if(title == null){
            return  "" ;
        }else {
            return title;
        }
    }

    public static String getPlaceImageNotFound(String s) {
        if(s == null || s.isEmpty()){
            return Constant.NO_PLACE_IMAGE;
        }else{
            return s;
        }

    }
    public static String getUserImageNotFound(String s) {
        if(s == null || s.isEmpty()){
            return Constant.NO_USER_IMAGE;
        }else{
            return s;
        }

    }



    public static  DatabaseReference getPlace(String id){
        return Constant.places.child(id);
    }


    public static void updateDashboard(int updateRequest) {
        Constant.dashboard.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()) {
                    Dashboard dashboard = snapshot.getValue(Dashboard.class);
                    switch (updateRequest){
                        case Constant.INCREASE_USER:
                            dashboard.increaseUsers();
                            break;
                        case Constant.INCREASE_COMMENT:
                            dashboard.increaseComments();
                            break;
                        case Constant.INCREASE_PLACE:
                            dashboard.increasePlacesCount();
                            break;
                        case Constant.INCREASE_NAVIGATION:
                            dashboard.increaseNavigationsCount();
                            break;
                        case Constant.INCREASE_NEARBY:
                            dashboard.increaseNearbyCount();
                            break;
                        case Constant.DECREASE_PLACE :
                            dashboard.decreasePlacesCount();
                            break;
                        case Constant.DECREASE_COMMENT:
                            dashboard.decreaseComments();
                            break;
                        case Constant.DECREASE_NEARBY:
                            dashboard.decreaseNearbyCount();
                            break;
                        case Constant.DECREASE_NAVIGATION:
                            dashboard.decreaseNavigationsCount();
                            break;
                        case Constant.DECREASE_USER:
                            dashboard.decreaseUsers();
                            break;
                    }
                    Constant.dashboard.setValue(dashboard);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public static String getNullCity(String city) {
        if(city == null){
            return "Cannot find a city ";
        }
        return city;
    }
}
