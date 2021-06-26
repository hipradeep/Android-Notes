package com.lmwdelivery.constants;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.IBinder;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import com.google.gson.JsonObject;
import com.lmwdelivery.R;
import com.lmwdelivery.app.PreferencesManager;
import com.lmwdelivery.common.DialogUtil;
import com.lmwdelivery.common.LoggerUtil;
import com.lmwdelivery.common.NetworkConnectionChecker;
import com.lmwdelivery.common.NetworkUtils;
import com.lmwdelivery.common.Utils;
import com.lmwdelivery.retrofit.ApiServices;
import com.lmwdelivery.retrofit.MvpView;
import com.lmwdelivery.retrofit.ServiceGenerator;

import java.util.Locale;
import java.util.Random;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.lmwdelivery.app.AppConfig.PAYLOAD_BUNDLE;


@SuppressLint("Registered")
public class BaseActivity extends AppCompatActivity implements
        NetworkConnectionChecker.OnConnectivityChangedListener, View.OnClickListener, MvpView {
    private ProgressDialog mProgressDialog;
    private static final String TAG = "BaseActivity";
    protected static final int PHONE_STATE_PERMISSION_REQUEST_CODE = 12;
    public Activity context;
    public ApiServices apiServices, createServiceUtilityV2;
    public PreferencesManager preferencesManager;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = this;
        apiServices = ServiceGenerator.createService(ApiServices.class);
        createServiceUtilityV2 = ServiceGenerator.createServiceUtilityV2(ApiServices.class);
        PreferencesManager.initializeInstance(context);
    }

    public String getColoredSpanned(String text, String color) {
        return "<font color=" + color + ">" + text + "</font>";
    }


    public void languageselection(String langtype) {
        Locale locale = new Locale(langtype);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
    }


    public JsonObject encryptBody(JsonObject param) {
        JsonObject body = new JsonObject();
        try {

            LoggerUtil.logItem(body);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return body;
    }


    private void getlogout(Class<?> activity) {


    }

//    @Override
//    protected void attachBaseContext(Context newBase) {
//        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
//    }

    public void setLangRecreate(String langval, boolean bool) {
        Configuration config = getBaseContext().getResources().getConfiguration();
        Locale locale = new Locale(langval);
        Locale.setDefault(locale);
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        if (bool)
            recreate();
    }
    public void setScaleOnButton(View view){
        Animation scaleAnim = AnimationUtils.loadAnimation(context, R.anim.scale_anim);
        view.startAnimation(scaleAnim);
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public void showLoading() {
        mProgressDialog = DialogUtil.showLoadingDialog(BaseActivity.this, "Base Activity");
        mProgressDialog.setCancelable(false);
    }

    @Override
    public void hideLoading() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.cancel();
        }
    }

    @Override
    public void openActivityOnTokenExpire() {

    }

    @Override
    public void onError(int resId) {

    }

    @Override
    public void onError(String message) {

    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void showMessage(int resId) {

    }

    @Override
    public boolean isNetworkConnected() {
        return false;
    }

    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    @Override
    public void connectivityChanged(boolean availableNow) {

    }

    public void createInfoDialog(Context context, String title,
                                 String msg) {
        androidx.appcompat.app.AlertDialog.Builder builder1 = new androidx.appcompat.app.AlertDialog.Builder(context);
        builder1.setTitle(title);
        String msgs = getColoredSpanned(msg, "#1b4576");
        builder1.setMessage(Html.fromHtml(msgs));
        builder1.setCancelable(true);

        builder1.setNegativeButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        androidx.appcompat.app.AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public String generatePin() {
        Random random = new Random();
        @SuppressLint("DefaultLocale") String randomPIN = String.format("%04d", random.nextInt(10000));

        return randomPIN;
    }

    @Override
    public void sendUnUsedEPin(String epinNo, String from) {
    }

    @Override
    public void getvehicledata(String vehiclename, String vehicleid) {

    }

    @Override
    public void getAccpetRejectBooking(String id, String bookid, String action, String remark, String reason) {

    }

    public void hideSoftKeyboard(Activity activity) {
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        InputMethodManager inputMethodManager = (InputMethodManager) activity
                .getSystemService(Activity.INPUT_METHOD_SERVICE);
        View v = activity.getCurrentFocus();
        if (v != null) {
            IBinder binder = activity.getCurrentFocus().getWindowToken();
            if (binder != null) {
                if (inputMethodManager != null) {
                    inputMethodManager.hideSoftInputFromWindow(binder, 0);
                }
            }
        }
    }

    public void goToActivity(Activity activity, Class<?> classActivity, Bundle bundle) {
        Utils.hideSoftKeyboard(activity);
        Intent intent = new Intent(activity, classActivity);
        activity.overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
        if (bundle != null)
            intent.putExtra(PAYLOAD_BUNDLE, bundle);
        activity.startActivity(intent);
    }

    public void goToActivityWithFinish(Activity activity, Class<?> classActivity, Bundle bundle) {
        Intent intent = new Intent(context, classActivity);
        activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        if (bundle != null)
            intent.putExtra(PAYLOAD_BUNDLE, bundle);
        Utils.hideSoftKeyboard(activity);
        activity.startActivity(intent);
        activity.finish();
    }

    public void goToActivityWithFinishFlipAnimation(Activity activity, Class<?> classActivity, Bundle bundle) {
        Intent intent = new Intent(context, classActivity);
        activity.overridePendingTransition(R.anim.from_middle, R.anim.to_middle);
        if (bundle != null)
            intent.putExtra(PAYLOAD_BUNDLE, bundle);
        Utils.hideSoftKeyboard(activity);
        activity.startActivity(intent);
        activity.finish();
    }

    public static void finishActivity(Activity activity) {
        Utils.hideSoftKeyboard(activity);
        activity.finish();
    }

    protected static final int PERMISSION_REQUEST_AUDIO_VIDEO = 15;

    public void hasAudioVideoStateContactPermission() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED)
            requestAudioVideoStateContactPermission();
    }

    public void requestAudioVideoStateContactPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE)
                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)
                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)
                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO)
                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            Utils.createSimpleDialog1(context, getString(R.string.alert_text), getString(R.string.permission_camera_rationale11), getString(R.string.reqst_permission), new Utils.Method() {
                @Override
                public void execute() {
                    ActivityCompat.requestPermissions(BaseActivity.this, new String[]{
                            Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA}, PERMISSION_REQUEST_AUDIO_VIDEO);
                }
            });

        } else {
            ActivityCompat.requestPermissions(this, new String[]{
                            Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.CALL_PHONE,
                            Manifest.permission.READ_CONTACTS,
                            Manifest.permission.RECORD_AUDIO,
                            Manifest.permission.CAMERA},
                    PERMISSION_REQUEST_AUDIO_VIDEO);
        }
    }

    protected static final int STORAGE_PERMISSION_REQUEST_CODE = 16;

    public void hasStoragePermission() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(context, Manifest.permission.PACKAGE_USAGE_STATS) != PackageManager.PERMISSION_GRANTED
        )
            requestStoragePermission();
    }

    public void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.PACKAGE_USAGE_STATS)) {
            Utils.createSimpleDialog1(context, getString(R.string.alert_text), getString(R.string.permission_camera_rationale11), getString(R.string.reqst_permission), new Utils.Method() {
                @Override
                public void execute() {
                    ActivityCompat.requestPermissions(BaseActivity.this, new String[]{
                                    Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.PACKAGE_USAGE_STATS},
                            STORAGE_PERMISSION_REQUEST_CODE);
                }
            });

        } else {
            ActivityCompat.requestPermissions(this, new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.PACKAGE_USAGE_STATS
                    },
                    STORAGE_PERMISSION_REQUEST_CODE);
        }
    }

    public void shareDataText(String shareBody, String shareSubject) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
//        sharingIntent.setPackage("com.facebook.katana");
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, shareSubject);
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        startActivity(sharingIntent);
    }

    public void SharingToSocialMedia(String application, String shareBody) {

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, shareBody);

        boolean installed = checkAppInstall(application);
        if (installed) {
            intent.setPackage(application);
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(),
                    "Install application first", Toast.LENGTH_LONG).show();
        }
    }

    private boolean checkAppInstall(String uri) {
        PackageManager pm = getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
        }

        return false;
    }

    public void logoutDialog1(Activity context, Class<?> activity) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setTitle("Logout");
        builder1.setMessage("Do you really want to logout?");
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                "Yes",
                (dialog, id) -> {
                    if (NetworkUtils.getConnectivityStatus(context) != 0)
//                        getLogout(activity);
//                    else
                        showMessage(R.string.alert_internet);
                    dialog.cancel();
                });

        builder1.setNegativeButton("No", (dialog, id) -> dialog.cancel());

        AlertDialog alert11 = builder1.create();
        alert11.setOnShowListener(arg0 -> {
            alert11.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.red));
            alert11.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        });
        alert11.show();
    }
    public void logoutDialog(Activity context, Class<?> activity) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setTitle("Logout");
        builder1.setMessage("Do you really want to logout?");
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                "Yes",
                (dialog, id) -> {
                    if (NetworkUtils.getConnectivityStatus(context) != 0) {
                        PreferencesManager.getInstance(context).clear();
                        PreferencesManager.getInstance(context).setIsFirstTimeLaunch(false);
                        goToActivityWithFinish(context, activity, null);
                    } else
                        showMessage(R.string.alert_internet);
                    dialog.cancel();
                });

        builder1.setNegativeButton("No", (dialog, id) -> dialog.cancel());

        AlertDialog alert11 = builder1.create();
        alert11.setOnShowListener(arg0 -> {
            alert11.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.red));
            alert11.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        });
        alert11.show();
    }

//    public void getLogout(Class<?> activity) {
//        showLoading();
////        {"DeviceId":"dsfsd"}
//        JsonObject parent = new JsonObject();
//        parent.addProperty("user_id", PreferencesManager.getInstance(context).getUserid());
//        LoggerUtil.logItem(parent);
//        Call<ResponseErrMsg> call = apiServices.getLogout(parent);
//        call.enqueue(new Callback<ResponseErrMsg>() {
//            @Override
//            public void onResponse(Call<ResponseErrMsg> call, Response<ResponseErrMsg> response) {
//                hideLoading();
//                LoggerUtil.logItem(response.body());
//                if (!response.body().isError()) {
//                    int defaultUiMode = PreferencesManager.getInstance(context).getUiMode();
//                    PreferencesManager.getInstance(context).clear();
//                    PreferencesManager.getInstance(context).setIsFirstTimeLaunch(false);
//                    PreferencesManager.getInstance(context).setUiMode(defaultUiMode);
//                    finishAffinity();
//                    goToActivityWithFinish(context, activity, null);
//                    showMessage(response.body().getMsg());
//                } else {
//                    goToActivityWithFinish(context, activity, null);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseErrMsg> call, Throwable t) {
//                hideKeyboard();
//                hideLoading();
//            }
//        });
//    }
}