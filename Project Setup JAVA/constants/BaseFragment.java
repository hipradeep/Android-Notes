package com.lmwdelivery.constants;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.lmwdelivery.R;
import com.lmwdelivery.common.DialogUtil;
import com.lmwdelivery.common.Utils;
import com.lmwdelivery.retrofit.ApiServices;
import com.lmwdelivery.retrofit.MvpView;
import com.lmwdelivery.retrofit.ServiceGenerator;

import java.util.Random;

import static com.lmwdelivery.app.AppConfig.PAYLOAD_BUNDLE;


public abstract class BaseFragment extends Fragment implements MvpView {
    // Toolbar toolbar;
    protected static final int ASK_SEND_SMS_PERMISSION_REQUEST_CODE = 14;
    private static final String TAG = "BaseFragment";
    private ProgressDialog mProgressDialog;
    protected final Gson gson = new Gson();
    //protected Entity mEntity;
    protected String latitude = "0", longitude = "0", lastActiveTime;
    public Activity context;
    public ApiServices apiServices, createServiceUtilityV2, apiServicesMarket, apiServicesNews;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        apiServices = ServiceGenerator.createService(ApiServices.class);
        apiServicesMarket = ServiceGenerator.createMarketFile(ApiServices.class);
        apiServicesNews = ServiceGenerator.createNewsFile(ApiServices.class);

        createServiceUtilityV2 = ServiceGenerator.createServiceUtilityV2(ApiServices.class);
    }

    public void showAlert(String msg, int color, int icon) {
//        Alerter.create(context)
//                .setText(msg)
//                .setTextAppearance(R.style.alertTextColor)
//                .setBackgroundColorRes(color)
//                .setIcon(icon)
//                .show();
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public String getColoredSpanned(String text, String color) {
        return "<font color=" + color + ">" + text + "</font>";
    }

    @Override
    public void getAccpetRejectBooking(String id, String bookid, String action, String remark, String reason) {

    }

    @Override
    public void getvehicledata(String vehiclename, String vehicleid) {

    }

    @Override
    public void sendUnUsedEPin(String epinNo, String from) {
    }

    @Override
    public void openActivityOnTokenExpire() {

    }

    @Override
    public void onError(int resId) {

    }

    @Override
    public void onError(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
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

    @Override
    public void hideKeyboard() {
        Utils.hideSoftKeyboard(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    public String generatePin() {
        Random random = new Random();
        @SuppressLint("DefaultLocale") String randomPIN = String.format("%04d", random.nextInt(10000));
        return randomPIN;
    }

    public void showLoading() {
        //hideLoading();
        mProgressDialog = DialogUtil.showLoadingDialog(getActivity(), TAG);
        mProgressDialog.setCancelable(false);
    }

    public void hideLoading() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.cancel();
        }
    }

    public void createInfoDialog(Context context, String title,
                                 String msg) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setTitle(title);
        builder1.setMessage(msg);
        builder1.setCancelable(true);

        builder1.setNegativeButton(
                "OK",
                (dialog, id) -> dialog.cancel());

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public void goToActivity(Class<?> classActivity, Bundle bundle) {
        Utils.hideSoftKeyboard(getActivity());
        Intent intent = new Intent(getContext(), classActivity);
        if (bundle != null)
            intent.putExtra(PAYLOAD_BUNDLE, bundle);
        getActivity().startActivity(intent);
    }

    public void goToActivityWithFinish(Class<?> classActivity, Bundle bundle) {
        Intent intent = new Intent(getContext(), classActivity);
        if (bundle != null)
            intent.putExtra(PAYLOAD_BUNDLE, bundle);
        Utils.hideSoftKeyboard(getActivity());
        getActivity().startActivity(intent);
        getActivity().finish();
    }

    public void checkSMSPermission() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED)
            requestSMSPermission();
    }

    public void requestSMSPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.SEND_SMS)
                || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_SMS)
                || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.RECEIVE_SMS)) {
            Utils.createSimpleDialog1(getActivity(), getString(R.string.alert_text), getString(R.string.permission_camera_rationale11), getString(R.string.reqst_permission), () -> ActivityCompat.requestPermissions(getActivity(), new String[]{
                            Manifest.permission.SEND_SMS,
                            Manifest.permission.READ_SMS,
                            Manifest.permission.RECEIVE_SMS},
                    ASK_SEND_SMS_PERMISSION_REQUEST_CODE));
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                            Manifest.permission.SEND_SMS,
                            Manifest.permission.READ_SMS,
                            Manifest.permission.RECEIVE_SMS
                    },
                    ASK_SEND_SMS_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void shareDataText(String shareBody, String shareSubject) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, shareSubject);
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.app_name)));
    }
}
