package com.example.gnssloggerbtf;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.GnssMeasurementsEvent;
import android.location.GnssNavigationMessage;
import android.location.GnssStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.OnNmeaMessageListener;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.gnssloggerbtf.GnssStatusfragment.Fragment_GnssStatus;
import com.example.gnssloggerbtf.Settingfragments.FragmentD;
import com.example.gnssloggerbtf.Settingfragments.Fragment_Setting;
import com.example.gnssloggerbtf.RinexFilefragments.Fragment_File;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import java.util.ArrayList;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.N)
public class MainActivity extends AppCompatActivity {

    private static final String CURRENT_FRAGMENT = "STATE_FRAGMENT_SHOW";

    private FragmentManager fragmentManager = getSupportFragmentManager();

    private Fragment currentFragment = new Fragment();
    private List<Fragment> fragments = new ArrayList<>();

    private int currentIndex = 0;

    private Fragment_GnssStatus mFragmentGnssStatus;
    private Fragment_Setting mFragmentSetting;
    private Fragment_File mFragment_file;
    private FragmentD fragmentD;


    private List<Fragment> mFragmentList = new ArrayList<>();
    //定位相关
    private Location mLocation;
    private LocationManager mLocationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        if (savedInstanceState != null) {
            // “内存重启”时调用

            //获取“内存重启”时保存的索引下标
            currentIndex = savedInstanceState.getInt(CURRENT_FRAGMENT, 0);

            //注意，添加顺序要跟下面添加的顺序一样！！！！
            fragments.removeAll(fragments);
            fragments.add(fragmentManager.findFragmentByTag(0 + ""));
            fragments.add(fragmentManager.findFragmentByTag(1 + ""));
            fragments.add(fragmentManager.findFragmentByTag(2 + ""));
            fragments.add(fragmentManager.findFragmentByTag(3 + ""));
            //恢复fragment页面
            restoreFragment();

        } else {      //正常启动时调用

            mFragmentGnssStatus = new Fragment_GnssStatus();
            mFragmentSetting = new Fragment_Setting();
            mFragment_file = new Fragment_File();
            fragmentD = new FragmentD();
            fragments.add(mFragmentGnssStatus);
            fragments.add(mFragmentSetting);
            fragments.add(mFragment_file);
            fragments.add(fragmentD);
            //showFragment();
        }
        init();
        BottomBar bottomBar = findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(new onTabSelect());


    }

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    /**
     * 在对sd卡进行读写操作之前调用这个方法 * Checks if the app has permission to write to device storage * If the app does not has permission then the user will be prompted to grant permissions
     */
    public static void verifyStoragePermissions(Activity activity) {    // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {        // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        }
    }
    //初始化
    private void init() {
        verifyStoragePermissions(MainActivity.this);
        ignoreBatteryOptimization(MainActivity.this);
//        if(Build.MANUFACTURER.equals("Xiaomi")) {
//            Intent intent = new Intent();
//            intent.setAction("miui.intent.action.OP_AUTO_START");
//            intent.addCategory(Intent.CATEGORY_DEFAULT);
//            startActivity(intent);
//        }

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //判断GNSS是否启动
        assert mLocationManager != null;
        if (!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            Toast.makeText(MainActivity.this, "请开启GNSS导航...", Toast.LENGTH_SHORT).show();

            // 返回开启GPS导航设置界面
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            MainActivity.this.startActivityForResult(intent, 0);
            return;
        }
        //看权限是否开启
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        // 为获取地理位置信息时设置查询条件
        String bestProvider = mLocationManager.getBestProvider(getCriteria(), true);

        // 获取位置信息

        // 如果不设置查询要求，getLastKnownLocation方法传人的参数为LocationManager.GPS_PROVIDER

        assert bestProvider != null;
        mLocation = mLocationManager.getLastKnownLocation(bestProvider);

        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, mLocationListener);


        mLocationManager.registerGnssMeasurementsCallback(gnssMeasurementsEventListener);
//
        mLocationManager.registerGnssNavigationMessageCallback(gnssNavigationMessageListener);
        mLocationManager.registerGnssStatusCallback(mGnssStatus);
        mLocationManager.addNmeaListener(mNmeaMessageListener);

    }

    /**
     * *  对定位的要求
     *
     * @return
     */
    private static Criteria getCriteria() {

        Criteria criteria = new Criteria();

        // 设置定位精确度 Criteria.ACCURACY_COARSE比较粗略，Criteria.ACCURACY_FINE则比较精细

        criteria.setAccuracy(Criteria.ACCURACY_FINE);

        // 设置是否要求速度

        criteria.setSpeedRequired(true);

        // 设置是否允许运营商收费

        criteria.setCostAllowed(false);

        // 设置是否需要方位信息

        criteria.setBearingRequired(true);

        // 设置是否需要海拔信息

        criteria.setAltitudeRequired(true);

        // 设置对电源的需求

        criteria.setPowerRequirement(Criteria.POWER_HIGH);

        return criteria;

    }

    private LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            //Log.d("haha", "onLocationChanged");
            mLocation = location;
            mFragmentGnssStatus.onLocationChanged(mLocation);
            mFragment_file.onLocationChanged(mLocation);

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };
    private GnssStatus.Callback mGnssStatus = new GnssStatus.Callback() {
        @Override
        public void onStarted() {

            super.onStarted();
        }

        @Override
        public void onStopped() {
            super.onStopped();
        }

        @Override
        public void onFirstFix(int ttffMillis) {
            //super.onFirstFix(ttffMillis);
            mFragmentGnssStatus.onGnssFirstFix(ttffMillis);
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onSatelliteStatusChanged(GnssStatus status) {
            mFragmentGnssStatus.onSatelliteStatusChanged(status);
            super.onSatelliteStatusChanged(status);
        }
    };
    private OnNmeaMessageListener mNmeaMessageListener = new OnNmeaMessageListener() {
        @Override
        public void onNmeaMessage(String message, long timestamp) {

            mFragmentGnssStatus.onNmeaMessage(message, timestamp);
        }
    };

    private GnssMeasurementsEvent.Callback gnssMeasurementsEventListener = new GnssMeasurementsEvent.Callback() {
        @Override
        public void onGnssMeasurementsReceived(GnssMeasurementsEvent eventArgs) {
            mFragment_file.onGnssMeasurementsReceived(eventArgs);
        }

        @Override
        public void onStatusChanged(int status) {
            super.onStatusChanged(status);
        }
    };
    private GnssNavigationMessage .Callback gnssNavigationMessageListener=new GnssNavigationMessage.Callback() {
        @Override
        public void onGnssNavigationMessageReceived(GnssNavigationMessage event) {
            mFragment_file.onGnssNavigationMessageReceived(event);
        }

        @Override
        public void onStatusChanged(int status) {
            super.onStatusChanged(status);
        }
    };


    private class onTabSelect implements OnTabSelectListener {
        //FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        @Override
        public void onTabSelected(int tabId) {
            switch (tabId) {
                case R.id.tab_monitor:
                    currentIndex = 0;
                    break;

                case R.id.tab_help:

                    currentIndex = 3;
                    break;

                case R.id.tab_setting:

                    currentIndex = 1;
                    break;

                case R.id.tab_rinex:

                    currentIndex = 2;
                    break;


            }
            showFragment();

        }
    }

    /**
     * 使用show() hide()切换页面
     * 显示fragment
     */
    private void showFragment() {

        FragmentTransaction transaction = fragmentManager.beginTransaction();

        //如果之前没有添加过
        if (!fragments.get(currentIndex).isAdded()) {
            transaction
                    .hide(currentFragment)
                    .add(R.id.contentContainer, fragments.get(currentIndex), "" + currentIndex);  //第三个参数为添加当前的fragment时绑定一个tag

        } else {
            transaction
                    .hide(currentFragment)
                    .show(fragments.get(currentIndex));
        }

        currentFragment = fragments.get(currentIndex);

        transaction.commit();

        if (currentFragment != mFragmentSetting && mFragmentSetting != null) {
            Log.d("screen", String.valueOf(mFragmentSetting.getScreenOn()));
        }

    }

    /**
     * 恢复fragment
     */
    private void restoreFragment() {


        FragmentTransaction mBeginTreansaction = fragmentManager.beginTransaction();

        for (int i = 0; i < fragments.size(); i++) {

            if (i == currentIndex) {
                mBeginTreansaction.show(fragments.get(i));
            } else {
                mBeginTreansaction.hide(fragments.get(i));
            }

        }

        mBeginTreansaction.commit();

        //把当前显示的fragment记录下来
        currentFragment = fragments.get(currentIndex);

    }


    /**
     * 忽略电池优化
     */
    private void ignoreBatteryOptimization(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
            boolean hasIgnored = powerManager.isIgnoringBatteryOptimizations(activity.getPackageName());            //  判断当前APP是否有加入电池优化的白名单，如果没有，弹出加入电池优化的白名单的设置对话框。            if (!hasIgnored) {                Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);                intent.setData(Uri.parse("package:" + activity.getPackageName()));                startActivity(intent);            }        }    }

            //  判断当前APP是否有加入电池优化的白名单，如果没有，弹出加入电池优化的白名单的设置对话框。
            if (!hasIgnored) {
                @SuppressLint("BatteryLife") Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + activity.getPackageName()));
                startActivity(intent);
            }
        }


    }
}
