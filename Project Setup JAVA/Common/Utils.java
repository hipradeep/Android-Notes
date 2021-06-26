package com.lmwdelivery.common;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.Vibrator;
import android.provider.Settings;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.lmwdelivery.R;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.MODE_PRIVATE;

@SuppressWarnings("ALL")
public class Utils {
    public static final int DIALOG_NETWORK_ERROR = 10;
    public static final int DIALOG_NO_CONNECTION = 100;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    public static final String FILE_NAME = "file_lang"; // preference file name
    public static final String KEY_LANG = "key_lang"; // preference key
    public static String selection = "";
    public static String selection1 = "";
    static private ProgressDialog mProgressDialog;
    static private AlertDialog mErrorDialog;
    public static int SPLASHTIMEOUT = 500;

    public static String getEmail(Context context) {
        AccountManager manager = AccountManager.get(context);
        Account[] accounts = manager.getAccountsByType("com.google");
        if (accounts != null && accounts.length > 0)
            return accounts[0].name;
        else
            return null;
    }

    public static String getCurrenttime() {

        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTime(new Date());
        calendar.add(Calendar.HOUR, 0);

        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");//HH:mm
        String time = null;
        try {
            time = sdf.format(calendar.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return time;
    }

    public static void showAlertDialog(Context ctx, String title, String msg) {
        new AlertDialog.Builder(ctx)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setIcon(R.mipmap.ic_launcher)
                .show();
    }

    public static String changeDateFormat(int day, int month, int year) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, day);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.YEAR, year);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        return dateFormat.format(c.getTime());
    }

    public static String getDateTime(String dateToCon) {
        String dateStr = "";
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
            Date date = dateFormat.parse(dateToCon);//You will get date object relative to server/client timezone wherever it is parsed
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd, HH:mm:ss"); //If you need time just put specific format for time like 'HH:mm:ss'
            dateStr = formatter.format(date);
        } catch (Exception e) {
        }
        return dateStr;
    }

    public static boolean isNetworkConnectedMainThred(Context ctx) {

        ConnectivityManager cm = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null)
            return false;
        else
            return true;
    }

    public static void languages(Context context) {
        Locale locale = new Locale(getLangCode(context));
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }

    private static String getLangCode(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(FILE_NAME, MODE_PRIVATE);
        String langCode = preferences.getString(KEY_LANG, "en");
        // save english 'en' as the default language
        return langCode;
    }

    public static void openBrowser(Context context, String url) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        context.startActivity(i);

    }

    public static boolean validateAadharNumber(String aadharNumber) {
        Pattern aadharPattern = Pattern.compile("\\d{12}");
        boolean isValidAadhar = aadharPattern.matcher(aadharNumber).matches();
        if (isValidAadhar) {
            isValidAadhar = VerhoeffAlgorithm.validateVerhoeff(aadharNumber);
        }
        return isValidAadhar;
    }

    public static boolean checkAndRequestPermissions(Context context) {

        boolean isgiven = false;
        int location = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
        int crosslocation = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION);
        int callphone = ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE);
        int camerapermisson = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA);
        int writeextenalpermisison = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readexternalpermission = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE);

        List<String> listPermissionsNeeded = new ArrayList<>();

        if (callphone != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CALL_PHONE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            try {
                ActivityCompat.requestPermissions((Activity) context, listPermissionsNeeded.toArray
                        (new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);


            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            isgiven = true;
        }
        return isgiven;
    }

    public static boolean checkAndRequestPermissionsforImageShare(Context context) {

        boolean isgiven = false;
        int location = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
        int crosslocation = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION);
        int callphone = ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE);
        int camerapermisson = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA);
        int writeextenalpermisison = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readexternalpermission = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE);

        List<String> listPermissionsNeeded = new ArrayList<>();

        if (writeextenalpermisison != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (readexternalpermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            try {
                ActivityCompat.requestPermissions((Activity) context, listPermissionsNeeded.toArray
                        (new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);


            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            isgiven = true;
        }
        return isgiven;
    }

    public static String getDate() {

        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTime(new Date());
        calendar.add(Calendar.HOUR, 0);

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");//hh:mm a
        String time = null;
        try {
            time = sdf.format(calendar.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return time;
    }

    public static String getTripeTime() {

        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTime(new Date());
        calendar.add(Calendar.HOUR, 0);

        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aaa");//hh:mm a
        String time = null;
        try {
            time = sdf.format(calendar.getTime());
            time = time.replace("pm", "PM").replace("am", "AM");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return time;
    }

    public static String getEncoded64ImageStringFromBitmap(Context c, Bitmap bitmap) {

        String imgString = "";

        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
            byte[] byteFormat = stream.toByteArray();
            // get the base 64 string
            imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);
        } catch (NullPointerException e) {
            e.printStackTrace();
            Toast.makeText(c, " This Image Cant be loaded ", Toast.LENGTH_SHORT).show();
        }
        return imgString;
    }

    public static String DateFormater(String date) {
        SimpleDateFormat sdf;
        sdf = new SimpleDateFormat("dd/MM/yyyy");  //format of the date which you send as parameter(if the date is like 08-Aug-2016 then use dd-MMM-yyyy)
        String s = "";
        try {
            Date dt = sdf.parse(date);
            sdf = new SimpleDateFormat("yyyy-MM-dd");
            s = sdf.format(dt);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return s;
    }


    public static void rateapp(Context context) {
        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + context.getPackageName())));
    }

    public static boolean isNetworkAvailable(Context context) {
        final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();
        if (activeNetworkInfo != null) { // connected to the internet
            if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                // connected to wifi
                return true;
            } else if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                // connected to the mobile provider's data plan
                return true;
            }
        }
        showMessage(context, "No Connection Found!");
        return false;
    }

    public static String getAndroidDeviceId(Context context) {

        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static void openWhatsAppConversationUsingUri(Context context, String numberWithCountryCode, String message) {

        Uri uri = Uri.parse("https://api.whatsapp.com/send?phone=" + numberWithCountryCode + "&text=" + message);

        Intent sendIntent = new Intent(Intent.ACTION_VIEW, uri);

        context.startActivity(sendIntent);
    }

    public static void goAnotherActivity(Activity a, Class c) {

        a.startActivity(new Intent(a, c));
//        a.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        // a.finish();

    }

    public static void goAnotherActivitytofrom(Activity a, Class c) {

        a.startActivity(new Intent(a, c));
//        a.overridePendingTransition(R.anim.to_middle, R.anim.from_middle);
        a.finish();

    }

    public static void showMessage(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    //    getDtaeFromTimeMillis
    public static String getDateFromDATETIME(String date) {
//Mon Nov 25 07:00:02 GMT+05:30 2019
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy", Locale.ENGLISH);

        Date myDate = null;
        try {
            myDate = dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat timeFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        return timeFormat.format(myDate);
    }

    //    getDtaeFromTimeMillis
    public static String getTimeFromDATETIME(String time) {
//Mon Nov 25 07:00:02 GMT+05:30 2019

        String convertedTime = "";
        convertedTime = time.substring(11, 16);

        return convertedTime;
    }

    public static String changeDateFormatSlash(int day, int month, int year) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, day);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.YEAR, year);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        return dateFormat.format(c.getTime());
    }

    public static boolean validatePan(String panNo) {

        Pattern pattern = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]");
        Matcher matcher = pattern.matcher(panNo);

        return matcher.matches();
    }


    public static boolean isEmailAddress(String text) {
        String pattern = "^[_a-z0-9-]+(\\.[_a-z0-9-]+)*@[a-z0-9-]+(\\.[a-z0-9-]+)*(\\.[a-z]{2,4})$";
        return text.matches(pattern);
    }

    // fragment.isAdded() checks in FragmentManager's backstack;
    // this function checks in FragmentManager's maintain list
    //
    // sometimes we have fragment managed by FM, but in its backstack;
    // this function helps find such fragments
    //
    // HINT: currently has no use but maybe will if we have some
    // fragment backstack issues in future
    public static boolean isFragmentAdded(Fragment frg, FragmentManager fm) {
        if (frg == null || frg.getClass() == null
                || frg.getClass().getName() == null)
            return false;

        if (fm.getFragments() == null)
            return false;

        for (Fragment f : fm.getFragments()) {
            if (f != null && f.getClass() != null
                    && f.getClass().getName() != null)
                if (f.getClass().getName().equals(frg.getClass().getName())
                        || f.getTag() != null
                        && f.getTag().equals(frg.getClass().getName()))
                    return true;
        }
        return false;
    }

    public static void startVibration(final Context context) {
        AsyncTask<Integer, Void, Void> vibration = new AsyncTask<Integer, Void, Void>() {

            @Override
            protected Void doInBackground(Integer... params) {
                Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                if (v != null)
                    v.vibrate(params[0]);
                return null;
            }
        };

        // Vibrate for 2000 milliseconds
        AsyncTaskExecutionHelper.executeParallel(vibration, 2000);
    }

    public static void playDefaultNotificationSound(Context context) {
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(context, notification);
        r.play();
    }

    public static void blinkLED(Context context) {
        NotificationManager notifMgr = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notif = new Notification();
        notif.ledARGB = Color.argb(255, 0, 255, 0);
        notif.flags |= Notification.FLAG_SHOW_LIGHTS;
        notif.ledOnMS = 300;
        notif.ledOffMS = 200;
        notifMgr.notify(999, notif);
    }


    public static void hideSoftKeyboard(Activity activity) {
        if (activity != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity
                    .getSystemService(Activity.INPUT_METHOD_SERVICE);
            View v = activity.getCurrentFocus();
            if (v != null) {
                IBinder binder = activity.getCurrentFocus()
                        .getWindowToken();
                if (binder != null) {
                    inputMethodManager.hideSoftInputFromWindow(binder, 0);
                }
            }
        }
    }

    public static Dialog createSimpleDialog1(Context context, String title,
                                             String msg, String btnLabel1, final Method method1) {
        String chars = title;
        SpannableString str = new SpannableString(chars);

        str.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.colorPrimaryDark)), 0,
                chars.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(str);
        builder.setMessage(msg);

        builder.setPositiveButton(btnLabel1,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        if (method1 != null)
                            method1.execute();
                    }
                });

        Dialog d = builder.show();
        int dividerId = d.getContext().getResources()
                .getIdentifier("android:id/titleDivider", null, null);
        View divider = d.findViewById(dividerId);
        try {
            divider.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
        } catch (Exception e) {
        }

        return d;

    }

    public static boolean isEmailValid(String email) {
        boolean isValid = false;
        String expression = "^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    public interface Method {
        void execute();
    }

    public static Bitmap getCompressedBitmap(String imagePath) {

        float maxHeight = 350.0f;
        float maxWidth = 500.0f;
        Bitmap scaledBitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(imagePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;
        float imgRatio = (float) actualWidth / (float) actualHeight;
        float maxRatio = maxWidth / maxHeight;

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;
            }
        }

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);
        options.inJustDecodeBounds = false;
        options.inDither = false;
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
            bmp = BitmapFactory.decodeFile(imagePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        assert scaledBitmap != null;
        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

        ExifInterface exif = null;
        try {
            exif = new ExifInterface(imagePath);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
            } else if (orientation == 3) {
                matrix.postRotate(180);
            } else if (orientation == 8) {
                matrix.postRotate(270);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 85, out);

        byte[] byteArray = out.toByteArray();

        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;

        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }
        return inSampleSize;
    }

    public static int getAge(String dobString) {

        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            date = sdf.parse(dobString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (date == null) return 0;

        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.setTime(date);

        int year = dob.get(Calendar.YEAR);
        int month = dob.get(Calendar.MONTH);
        int day = dob.get(Calendar.DAY_OF_MONTH);

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }


        return age;
    }

    private static String changeDateFormat(String date) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a", Locale.ENGLISH);

        Date myDate = null;
        try {
            myDate = dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat timeFormat = new SimpleDateFormat("dd MMM, yy", Locale.ENGLISH);
        return timeFormat.format(myDate);
    }
}