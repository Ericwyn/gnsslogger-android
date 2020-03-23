package com.example.gnssloggerbutterflying.Settingfragments;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.location.GnssMeasurementsEvent;
import android.location.GnssStatus;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.gnssloggerbutterflying.GnssLocationListener;
import com.example.gnssloggerbutterflying.R;
import com.example.gnssloggerbutterflying.RinexFileLogger.RinexO_Header;

import java.util.Objects;

public class Fragment_Setting extends Fragment implements GnssLocationListener {

    public final static String TAG = "Fragment_Setting";


    private TextView mtv_approLat, mtv_approLong, mtv_approHeight, mtv_approX, mtv_approY, mtv_approZ;

    private RadioGroup mrg_SelectVersion;

    private TextView mtv_FileName, mtv_MarkerName, mtv_MarkerNumber, mtv_ObsName, mtv_ObsAgencyName, mtv_RecNumber, mtv_RecType, mtv_RecVersion, mtv_AntType, mtv_AntDeltaH, mtv_AntDeltaN, mtv_AntDeltaE, mtv_AntName;

    private Button mbt_FileName, mbt_MarkerName, mbt_MarkerNumber, mbt_ObsName, mbt_ObsAgencyName, mbt_RecNumber, mbt_RecType, mbt_RecVersion, mbt_AntType, mbt_AntDeltaH, mbt_AntDeltaN, mbt_AntDeltaE, mbt_AntName;


    private CheckBox mcb_ScreenOn;

    private boolean isScreenOn = true;

    private Location mLocation;

    private String Version;

    private RinexO_Header mRinexO_header;

    private void updateFragmentSetting() {
        mRinexO_header = new RinexO_Header();
        if (mtv_AntDeltaE.getText() != null)
            mRinexO_header.setAntDeltaE( mtv_AntDeltaE.getText().toString());
        if (mtv_AntDeltaH.getText() != null)
            mRinexO_header.setAntDeltaH( mtv_AntDeltaH.getText().toString());
        if (mtv_AntDeltaN.getText() != null)
            mRinexO_header.setAntDeltaN( mtv_AntDeltaN.getText().toString());
        if (mtv_FileName.getText() != null)
            mRinexO_header.setFileName( mtv_FileName.getText().toString());
        if (mtv_AntName.getText() != null)
            mRinexO_header.setAntName( mtv_AntName.getText().toString());
        if (mtv_AntType.getText() != null)
            mRinexO_header.setAntType( mtv_AntType.getText().toString());

        if (mtv_approX.getText() != null)
            mRinexO_header.setApproX(mtv_approX.getText().toString());
        if (mtv_approY.getText() != null)
            mRinexO_header.setApproY( mtv_approY.getText().toString());
        if (mtv_approZ.getText() != null)
            mRinexO_header.setApproZ(mtv_approZ.getText().toString());

        if (mtv_MarkerName.getText() != null)
            mRinexO_header.setMarkerName( mtv_MarkerName.getText().toString());
        if (mtv_MarkerNumber.getText() != null)
            mRinexO_header.setMarkerNumber( mtv_MarkerNumber.getText().toString());
        if (mtv_ObsName.getText() != null)
            mRinexO_header.setObserverName( mtv_ObsName.getText().toString());
        if (mtv_ObsAgencyName.getText() != null)
            mRinexO_header.setObsAgencyName( mtv_ObsAgencyName.getText().toString());
        if (Version != null)
            mRinexO_header.setVersion(Version);
        else
        {
            mRinexO_header.setVersion("3.03");
        }
        if (mtv_RecNumber.getText() != null)
            mRinexO_header.setRecNumber( mtv_RecNumber.getText().toString());
        if (mtv_RecType.getText() != null)
            mRinexO_header.setRecType( mtv_RecType.getText().toString());
        if (mtv_RecVersion.getText() != null)
            mRinexO_header.setRecVersion( mtv_RecVersion.getText().toString());


    }

    public RinexO_Header getRinexO_Header() {
        updateFragmentSetting();
        return mRinexO_header;
    }

    public boolean getScreenOn() {
        return isScreenOn;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_setting, container, false);


        /*
        第一个卡片
         */
        mtv_approLat = v.findViewById(R.id.tv_appro_poxLat);
        mtv_approLong = v.findViewById(R.id.tv_appro_poxLong);
        mtv_approHeight = v.findViewById(R.id.tv_appro_poxH);
        mtv_approX = v.findViewById(R.id.tv_appro_poxX);
        mtv_approY = v.findViewById(R.id.tv_appro_poxY);
        mtv_approZ = v.findViewById(R.id.tv_appro_poxZ);

        /*
        第二个卡片
         */
        mrg_SelectVersion = v.findViewById(R.id.rg_select_version);
        mtv_FileName = v.findViewById(R.id.tv_filename);
        mbt_FileName = v.findViewById(R.id.bt_filename);
        mcb_ScreenOn = v.findViewById(R.id.cb_screen_on);

        /*
        第三个卡片
         */
        mtv_MarkerName = v.findViewById(R.id.tv_markername);
        mtv_MarkerNumber = v.findViewById(R.id.tv_markernumber);
        mbt_MarkerName = v.findViewById(R.id.bt_markername);
        mbt_MarkerNumber = v.findViewById(R.id.bt_markernumber);

        mtv_ObsName = v.findViewById(R.id.tv_obsname);
        //获取手机型号
        mtv_ObsName.setText(android.os.Build.MODEL);

        mbt_ObsName = v.findViewById(R.id.bt_obsname);
        mtv_ObsAgencyName = v.findViewById(R.id.tv_agencyname);
        mbt_ObsAgencyName = v.findViewById(R.id.bt_agencyname);

        mtv_RecNumber = v.findViewById(R.id.tv_recnumber);
        mbt_RecNumber = v.findViewById(R.id.bt_recnumber);
        mtv_RecType = v.findViewById(R.id.tv_rectype);
        //获取手机厂商
        mtv_RecType.setText(android.os.Build.BRAND);

        mbt_RecType = v.findViewById(R.id.bt_rectype);
        mtv_RecVersion = v.findViewById(R.id.tv_recversion);
        //获取手机型号
        mtv_RecVersion.setText(android.os.Build.MODEL);

        mbt_RecVersion = v.findViewById(R.id.bt_recversion);

        mtv_AntType = v.findViewById(R.id.tv_anttype);
        //手机型号
        mtv_AntType.setText(android.os.Build.MODEL);

        mbt_AntType = v.findViewById(R.id.bt_anttype);
        mtv_AntName = v.findViewById(R.id.tv_antname);
        mbt_AntName = v.findViewById(R.id.bt_antname);

        mtv_AntDeltaE = v.findViewById(R.id.tv_antdeltaE);
        mtv_AntDeltaH = v.findViewById(R.id.tv_antdeltaH);
        mtv_AntDeltaN = v.findViewById(R.id.tv_antdeltaN);
        mbt_AntDeltaE = v.findViewById(R.id.bt_antdeltaE);
        mbt_AntDeltaH = v.findViewById(R.id.bt_antdeltaH);
        mbt_AntDeltaN = v.findViewById(R.id.bt_antdeltaN);

        return v;
    }


    /**
     *从Fragment_GnssStatus 中获取纬度，经度，和高度，并将Fragment_setting 中的text view 设置值
     * @param lat
     * @param lon
     * @param height
     */
    public void setLatLongAltFromGnssStatus(double lat ,double lon,double height)
    {
        mtv_approLat.setText(String.valueOf(lat));

        mtv_approLong.setText(String.valueOf(lon));

        mtv_approHeight.setText(String.valueOf(height));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        mbt_FileName.setOnClickListener(new onClick());

        mbt_RecType.setOnClickListener(new onClick());
        mbt_RecNumber.setOnClickListener(new onClick());
        mbt_RecVersion.setOnClickListener(new onClick());

        mbt_AntType.setOnClickListener(new onClick());
        mbt_AntDeltaE.setOnClickListener(new onClick());
        mbt_AntDeltaH.setOnClickListener(new onClick());
        mbt_AntDeltaN.setOnClickListener(new onClick());
        mbt_AntName.setOnClickListener(new onClick());
        mbt_MarkerNumber.setOnClickListener(new onClick());
        mbt_MarkerName.setOnClickListener(new onClick());
        mbt_ObsName.setOnClickListener(new onClick());
        mbt_ObsAgencyName.setOnClickListener(new onClick());


        mrg_SelectVersion.setOnCheckedChangeListener(new onCheckChange());

        mcb_ScreenOn.setOnCheckedChangeListener(new onCheckBoxChange());

        updateFragmentSetting();

        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onGnssFirstFix(int ttffMillis) {

    }

    @Override
    public void onSatelliteStatusChanged(GnssStatus status) {

    }

    @Override
    public void onGnssStarted() {

    }

    @Override
    public void onGnssStopped() {

    }

    @Override
    public void onGnssMeasurementsReceived(GnssMeasurementsEvent event) {

    }

    @Override
    public void onLocationChanged(Location location) {
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

    @Override
    public void onNmeaMessage(String message, long timestamp) {

    }

    private class onClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.bt_filename:
                    AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));

                    View view = LayoutInflater.from(Objects.requireNonNull(getActivity())).inflate(R.layout.alert_4chars_filename, null);
                    final EditText met_FileName = view.findViewById(R.id.et_filename);

                    builder.setMessage("Please input a new File Name(max 4 characters)").setView(view).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mtv_FileName.setText(met_FileName.getText());

                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();
                    break;
                //markername max 60chars
                case R.id.bt_markername:
                    AlertDialog.Builder builder_markername = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));

                    View view_markername = LayoutInflater.from(Objects.requireNonNull(getActivity())).inflate(R.layout.alert_60chars, null);
                    final EditText met_MarkerName = view_markername.findViewById(R.id.et_60chars);

                    builder_markername.setMessage("Please input a new Marker Name(max 60 characters)").setView(view_markername).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mtv_MarkerName.setText(met_MarkerName.getText());
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();
                    break;
                //markernumber max 60chars
                case R.id.bt_markernumber:
                    AlertDialog.Builder builder_markernumber = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));

                    View view_markernumber = LayoutInflater.from(Objects.requireNonNull(getActivity())).inflate(R.layout.alert_60chars, null);
                    final EditText met_MarkerNumber = view_markernumber.findViewById(R.id.et_60chars);

                    builder_markernumber.setMessage("Please input a new Marker Number(max 60 characters)").setView(view_markernumber).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mtv_MarkerNumber.setText(met_MarkerNumber.getText());
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();
                    break;
                //observer name max 20chars
                case R.id.bt_obsname:
                    AlertDialog.Builder builder_obsname = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));

                    View view_obsname = LayoutInflater.from(Objects.requireNonNull(getActivity())).inflate(R.layout.alert_20chars, null);
                    final EditText met_obsname = view_obsname.findViewById(R.id.et_20chars);

                    builder_obsname.setMessage("Please input a new Observer Name(max 20 characters)").setView(view_obsname).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mtv_ObsName.setText(met_obsname.getText());
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();
                    break;
                //observer agency name max 20chars
                case R.id.bt_agencyname:
                    AlertDialog.Builder builder_agencyname = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));

                    View view_agencyname = LayoutInflater.from(Objects.requireNonNull(getActivity())).inflate(R.layout.alert_20chars, null);
                    final EditText met_agencyname = view_agencyname.findViewById(R.id.et_40chars);

                    builder_agencyname.setMessage("Please input a new Observer Agency Name(max 40 characters)").setView(view_agencyname).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mtv_ObsAgencyName.setText(met_agencyname.getText());
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();
                    break;
                //receiver number max 20chars
                case R.id.bt_recnumber:
                    AlertDialog.Builder builder_recnumber = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));

                    View view_recnumber = LayoutInflater.from(Objects.requireNonNull(getActivity())).inflate(R.layout.alert_40chars, null);
                    final EditText met_recnumber = view_recnumber.findViewById(R.id.et_20chars);

                    builder_recnumber.setMessage("Please input a new Receiver Name(max 20 characters)").setView(view_recnumber).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mtv_RecNumber.setText(met_recnumber.getText());
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();
                    break;
                //receiver type max 20chars
                case R.id.bt_rectype:
                    AlertDialog.Builder builder_rectype = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));

                    View view_rectype = LayoutInflater.from(Objects.requireNonNull(getActivity())).inflate(R.layout.alert_20chars, null);
                    final EditText met_rectype = view_rectype.findViewById(R.id.et_20chars);

                    builder_rectype.setMessage("Please input a new Receiver Type(max 20 characters)").setView(view_rectype).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mtv_RecType.setText(met_rectype.getText());
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();
                    break;
                //receiver type max 20chars
                case R.id.bt_recversion:
                    AlertDialog.Builder builder_recversion = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));

                    View view_recversion = LayoutInflater.from(Objects.requireNonNull(getActivity())).inflate(R.layout.alert_20chars, null);
                    final EditText met_recversion = view_recversion.findViewById(R.id.et_20chars);

                    builder_recversion.setMessage("Please input a new Receiver Version(max 20 characters)").setView(view_recversion).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mtv_RecVersion.setText(met_recversion.getText());
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();
                    break;
                //ant name max 20chars
                case R.id.bt_antname:
                    AlertDialog.Builder builder_antname = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));

                    View view_antname = LayoutInflater.from(Objects.requireNonNull(getActivity())).inflate(R.layout.alert_20chars, null);
                    final EditText met_antname = view_antname.findViewById(R.id.et_20chars);

                    builder_antname.setMessage("Please input a new Antenna Number(max 20 characters)").setView(view_antname).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mtv_AntName.setText(met_antname.getText());
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();
                    break;
                //ant name max 20chars
                case R.id.bt_anttype:
                    AlertDialog.Builder builder_anttype = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));

                    View view_anttype = LayoutInflater.from(Objects.requireNonNull(getActivity())).inflate(R.layout.alert_20chars, null);
                    final EditText met_anttype = view_anttype.findViewById(R.id.et_20chars);

                    builder_anttype.setMessage("Please input a new Antenna Type(max 20 characters)").setView(view_anttype).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mtv_AntType.setText(met_anttype.getText());
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();
                    break;
                //ant delta E max 4number
                case R.id.bt_antdeltaE:
                    AlertDialog.Builder builder_antdeltaE = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));

                    View view_antdeltaE = LayoutInflater.from(Objects.requireNonNull(getActivity())).inflate(R.layout.alert_4decimalnumber, null);
                    final EditText met_antdeltaE1 = view_antdeltaE.findViewById(R.id.et_4number1);
                    final EditText met_antdeltaE2 = view_antdeltaE.findViewById(R.id.et_4number2);

                    builder_antdeltaE.setMessage("Please input a new Antenna Eccentricity East(meters)(max 20 characters)").setView(view_antdeltaE).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mtv_AntDeltaE.setText(met_antdeltaE1.getText() + "." + met_antdeltaE2.getText());
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();
                    break;
                //ant delta N max 4number
                case R.id.bt_antdeltaN:
                    AlertDialog.Builder builder_antdeltaN = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));

                    View view_antdeltaN = LayoutInflater.from(Objects.requireNonNull(getActivity())).inflate(R.layout.alert_4decimalnumber, null);
                    final EditText met_antdeltaN1 = view_antdeltaN.findViewById(R.id.et_4number1);
                    final EditText met_antdeltaN2 = view_antdeltaN.findViewById(R.id.et_4number2);

                    builder_antdeltaN.setMessage("Please input a new Antenna Eccentricity North(meters)(max 20 characters)").setView(view_antdeltaN).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mtv_AntDeltaN.setText(met_antdeltaN1.getText() + "." + met_antdeltaN2.getText());
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();
                    break;
                //ant delta N max 4number
                case R.id.bt_antdeltaH:
                    AlertDialog.Builder builder_antdeltaH = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));

                    View view_antdeltaH = LayoutInflater.from(Objects.requireNonNull(getActivity())).inflate(R.layout.alert_4decimalnumber, null);
                    final EditText met_antdeltaH1 = view_antdeltaH.findViewById(R.id.et_4number1);
                    final EditText met_antdeltaH2 = view_antdeltaH.findViewById(R.id.et_4number2);

                    builder_antdeltaH.setMessage("Please input a new Antenna Height(meters)(max 20 characters)").setView(view_antdeltaH).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mtv_AntDeltaH.setText(met_antdeltaH1.getText() + "." + met_antdeltaH2.getText());
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();
                    break;


            }
            updateFragmentSetting();
        }
    }

    private class onCheckChange implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            RadioButton radioButton = group.findViewById(checkedId);
            Version =  radioButton.getText().toString();
            updateFragmentSetting();
        }

    }

    private class onCheckBoxChange implements CheckBox.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                isScreenOn = true;
            } else {
                isScreenOn = false;
            }

        }
    }
}
