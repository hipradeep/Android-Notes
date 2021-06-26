package com.lmwdelivery.constants;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Build;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;


import com.lmwdelivery.app.PreferencesManager;

import java.lang.ref.WeakReference;

/**
 * Created by surya on 29/3/18.
 */

public class NightModeHelper {
    /**
     * init on day mode
     **/
    private static int sUiNightMode = AppCompatDelegate.MODE_NIGHT_NO;

    private static WeakReference<Activity> mActivity;
    //  private SharedPrefHelper mPrefs;
    private static NightModeHelper instance;


    /**
     * Default behaviour is to automatically save the setting and restore it.
     */
    public NightModeHelper(Activity activity, int defaultUiMode) {
        setConfigMode();
        //mPrefs = SharedPrefHelper.getInstance(new WeakReference<Context>(activity));
    }

    /**
     * @param activity
     */
    public NightModeHelper(Activity activity) {
        //  mPrefs = SharedPrefHelper.getInstance(new WeakReference<Context>(activity));
    }

    /**
     * @return
     */
    public static NightModeHelper getInstance() {
        return instance == null ? instance = new NightModeHelper(null) : instance;
    }

    /**
     * @param activity
     * @return
     */
    public static NightModeHelper getInstance(Activity activity) {
        mActivity = new WeakReference<Activity>(activity);
        return instance == null ? instance = new NightModeHelper(activity) : instance;
    }

    /**
     * @param activity
     * @param defaultUiMode
     * @return
     */
    public static NightModeHelper getInstance(Activity activity, int defaultUiMode) {
        mActivity = new WeakReference<Activity>(activity);
        return instance == null ? instance = new NightModeHelper(activity, defaultUiMode) : instance;
    }

    /**
     * @param uiNightMode
     */
    private void updateConfig(int uiNightMode) {
        if (mActivity == null)
            return;

        Activity activity = mActivity.get();
        if (activity == null) {
            throw new IllegalStateException("Activity went away?");
        }
        Configuration newConfig = new Configuration(activity.getResources().getConfiguration());
        newConfig.uiMode &= ~Configuration.UI_MODE_NIGHT_MASK;
        newConfig.uiMode |= uiNightMode;
        if (Build.VERSION.SDK_INT > 16)
            activity.createConfigurationContext(newConfig);
        else
            activity.getResources().updateConfiguration(newConfig, null);
        sUiNightMode = uiNightMode;
        PreferencesManager.getInstance(activity).setUiMode(sUiNightMode);
//        if (mPrefs != null) {
//            mPrefs.setValue(SharedPrefHelper.SharedPrefKeysEnum.NIGHT_MODE, sUiNightMode);
//        }
    }

    /**
     * get ui night mode
     */
    public static int getUiNightMode() {
        return sUiNightMode;
    }

    /**
     * toggle night mode
     */
    public void toggle() {
        if (sUiNightMode == AppCompatDelegate.MODE_NIGHT_YES) {
            notNight();
            return;
        }
        night();
    }

    /**
     * light mode
     */
    public void notNight() {
        if (mActivity == null)
            return;

        updateConfig(AppCompatDelegate.MODE_NIGHT_NO);
        mActivity.get().recreate();
    }

    /**
     * night mode
     */
    public void night() {
        if (mActivity == null)
            return;

        updateConfig(AppCompatDelegate.MODE_NIGHT_YES);
        mActivity.get().recreate();
    }

    /**
     *
     */
    public void setNightModeLocal() {
        if (mActivity == null ||
                mActivity.get() == null)
            return;
        ((AppCompatActivity) mActivity.get()).getDelegate().setLocalNightMode(sUiNightMode);
    }

    /**
     *
     */
    public void setConfigurationMode() {
        setConfigMode();
    }

    public int getConfigMode() {
        return sUiNightMode;
    }

    /**
     *
     */
    public void setConfigMode() {
//        if (mPrefs == null)
//            return;
        Activity activity = mActivity.get();
        int defaultUiMode = PreferencesManager.getInstance(activity).getUiMode();
        sUiNightMode = defaultUiMode;
        AppCompatDelegate.setDefaultNightMode(sUiNightMode);
    }

    /**
     * @return
     */
    public boolean isNightMode() {
        return sUiNightMode == AppCompatDelegate.MODE_NIGHT_YES;
    }
}