package util;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

public class Constant {
    public static final String TAG_V = "TAG_V" ;
    public static final String WEATHER_KEY = "900e61e649b5797a2a5094c6ca29dfae";
    public static final String WEATHER_URI = "https://api.openweathermap.org/data/2.5/weather?";
    public static  String LATITUDE_BLOCK = "lat={#}";
    public static final String AND = "&";
   public static String LONGITUDE_BLOCK = "lon={#}";
   public static String KEY_BLOCK = "appid={#}";

    public static final String REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\."+ "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z" + "A-Z]{2,7}$";
    public static double LONGITUDE ;
    public static double LATITUDE ;

    public static final String INVALID_PASSWORD_LENGTH_MESSAGE = "password should be more than 6 characters";
    public static final String PASSWORD_DOES_NOT_MATCH_MESSAGE= "Password does not match! ";
    public static final String INVALID_EMAIL_PATTERN_MESSAGE= "Please insert a valid email";
    public static final String EMPTY_FIELD_MESSAGE = "This field is required!";
    public static  FirebaseAuth AUTH = FirebaseAuth.getInstance();
    public static  FirebaseUser USER;
    public static final FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
    public static final FirebaseDatabase DATABASE = FirebaseDatabase.getInstance();
    public static boolean isAdmin = false ;
    public static final String LOREN_EXAMPLE = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Amet risus nullam eget felis eget nunc lobortis mattis. Placerat duis ultricies lacus sed turpis tincidunt id. Sit amet cursus sit amet dictum. Quam elementum pulvinar etiam non quam. Elementum sagittis vitae et leo duis ut diam quam nulla. Amet risus nullam eget felis eget nunc. Sed vulputate odio ut enim blandit volutpat maecenas volutpat. Congue eu consequat ac felis donec et. Accumsan in nisl nisi scelerisque eu ultrices vitae auctor eu.";
public static final int ALERT_TYPE_YES_NO = 1001;
public static final int ALERT_TYPE_YES = 1002;

}
