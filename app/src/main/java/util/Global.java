package util;

import android.content.ContentResolver;
import android.net.Uri;
import android.webkit.MimeTypeMap;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

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

        return dist;
    }
    public static String getFileExtension(Uri uri , ContentResolver contentResolver) {
        ContentResolver cr = contentResolver;
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }
}
