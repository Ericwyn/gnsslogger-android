package com.example.gnssloggerbutterflying.GnssStatusfragment;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.GnssMeasurementsEvent;
import android.location.GnssStatus;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gnssloggerbutterflying.GnssLocationListener;
import com.example.gnssloggerbutterflying.R;
import com.example.gnssloggerbutterflying.Settingfragments.Fragment_Setting;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static android.util.TypedValue.COMPLEX_UNIT_DIP;
import static android.util.TypedValue.COMPLEX_UNIT_PX;
import static com.example.gnssloggerbutterflying.GnssStatusfragment.CarrierFreqUtils.CF_UNKNOWN;

public class Fragment_GnssStatus extends Fragment implements GnssLocationListener {


    public final static String TAG = "GnssStatus_Fragment";


    private Drawable mFlagUsa, mFlagRussia, mFlagJapan, mFlagChina, mFlagIndia, mFlagEU, mFlagICAO;


    private TextView mLatitudeView, mLongitudeView, mFixTimeView, mTTFFView, mAltitudeView,
            mAltitudeMslView, mHorVertAccuracyLabelView, mHorVertAccuracyView,
            mSpeedView, mSpeedAccuracyView, mBearingView, mBearingAccuracyView, mNumSats,
            mPdopLabelView, mPdopView, mHvdopLabelView, mHvdopView;
    private TableRow mSpeedBearingAccuracyRow;

    private Location mLocation;

    private List<SatelliteStatus> mSatelliteStatusList = new ArrayList<>();

    private SatelliteStatusAdapter mGnssAdapter;

    private RecyclerView mGnssStatusList;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_gnssstatus, container, false);

        mLatitudeView = v.findViewById(R.id.latitude);
        mLongitudeView = v.findViewById(R.id.longitude);
        mFixTimeView = v.findViewById(R.id.fix_time);
        mTTFFView = v.findViewById(R.id.ttff);
        mAltitudeView = v.findViewById(R.id.altitude);
        mAltitudeMslView = v.findViewById(R.id.altitude_msl);
        mHorVertAccuracyLabelView = v.findViewById(R.id.hor_vert_accuracy_label);
        mHorVertAccuracyView = v.findViewById(R.id.hor_vert_accuracy);
        mSpeedView = v.findViewById(R.id.speed);
        mSpeedAccuracyView = v.findViewById(R.id.speed_acc);
        mBearingView = v.findViewById(R.id.bearing);
        mBearingAccuracyView = v.findViewById(R.id.bearing_acc);
        mNumSats = v.findViewById(R.id.num_sats);
        mPdopLabelView = v.findViewById(R.id.pdop_label);
        mPdopView = v.findViewById(R.id.pdop);
        mHvdopLabelView = v.findViewById(R.id.hvdop_label);
        mHvdopView = v.findViewById(R.id.hvdop);

        mSpeedBearingAccuracyRow = v.findViewById(R.id.speed_bearing_acc_row);


        mFlagUsa = getResources().getDrawable(R.drawable.ic_flag_usa);
        mFlagRussia = getResources().getDrawable(R.drawable.ic_flag_russia);
        mFlagJapan = getResources().getDrawable(R.drawable.ic_flag_japan);
        mFlagChina = getResources().getDrawable(R.drawable.ic_flag_china);
        mFlagIndia = getResources().getDrawable(R.drawable.ic_flag_india);
        mFlagEU = getResources().getDrawable(R.drawable.ic_flag_european_union);
        mFlagICAO = getResources().getDrawable(R.drawable.ic_flag_icao);

        // GNSS,设置列表
        LinearLayoutManager mLinearLayoutmGnss = new LinearLayoutManager(getContext());
        mLinearLayoutmGnss.setOrientation(RecyclerView.VERTICAL);

        mGnssStatusList = v.findViewById(R.id.gnss_status_list);
        mGnssAdapter = new SatelliteStatusAdapter();
        mGnssStatusList.setAdapter(mGnssAdapter);
        mGnssStatusList.setFocusable(false);
        mGnssStatusList.setFocusableInTouchMode(false);
        mGnssStatusList.setLayoutManager(mLinearLayoutmGnss);
        mGnssStatusList.setNestedScrollingEnabled(false);


        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    @Override
    public void onLocationChanged(Location location) {

        mLocation = location;

        mLatitudeView.setText(String.valueOf(String.format("%.7f", mLocation.getLatitude())) + "°");
        mLongitudeView.setText(String.valueOf(String.format("%.7f", mLocation.getLongitude())) + "°");
        Log.d(TAG, mLocation.getLatitude() + "du");

        if (location.hasAltitude()) {
            mAltitudeView.setText(String.valueOf(String.format("%.2f", mLocation.getAltitude())) + "m");
        } else {
            mAltitudeView.setText("未知");
        }
        if (location.hasSpeed()) {
            mSpeedView.setText(String.valueOf(String.format("%.2f", mLocation.getSpeed())) + "m/s");
        } else {
            mSpeedView.setText("未知");
        }
        if (location.hasBearing()) {
            mBearingView.setText(String.valueOf(String.format("%.2f", mLocation.getBearing())) + "°");
        } else {
            mBearingView.setText("未知");
        }


        if (SatelliteUtils.isVerticalAccuracySupported(location)) {
            mHorVertAccuracyView.setText(String.format("%.2f", mLocation.getAccuracy()) + "/" + String.format("%.2f", mLocation.getVerticalAccuracyMeters()) + "m");
        } else {
            if (location.hasAccuracy()) {
                mHorVertAccuracyView.setText(String.format("%.2f", mLocation.getAccuracy()) + "m");
            } else {
                mHorVertAccuracyView.setText("未知");
            }
        }

        if (SatelliteUtils.isSpeedAndBearingAccuracySupported()) {
            mSpeedBearingAccuracyRow.setVisibility(View.VISIBLE);
            if (location.hasSpeedAccuracy()) {
                mSpeedAccuracyView.setText(String.format("%.1f", mLocation.getSpeedAccuracyMetersPerSecond()) + "m/s");
            } else {
                mSpeedAccuracyView.setText("未知");
            }
            if (location.hasBearingAccuracy()) {
                mBearingAccuracyView.setText(String.format("%.2f", mLocation.getBearingAccuracyDegrees()) + "°");
            } else {
                mBearingAccuracyView.setText("未知");
            }
        } else {
            mSpeedBearingAccuracyRow.setVisibility(View.GONE);
        }
        updateFixTime();

        updateFragmentSetting();


    }

    private void updateFragmentSetting() {
        assert getFragmentManager() != null;
        //通过标签找fragment_setting ,标签的设置在mainactivity里
        Fragment_Setting fragment_setting = (Fragment_Setting) getFragmentManager().findFragmentByTag(1+"");

        if (mLocation != null && mLocation.hasAltitude()) {

            try {
                assert fragment_setting != null;
                fragment_setting.setLatLongAltFromGnssStatus(mLocation.getLatitude(), mLocation.getLongitude(), mLocation.getAltitude());

            } catch (Exception e) {
                Log.d(TAG, "fragment_setting.setLatLongAltFromGnssStatus出错");
            }
        } else {
            assert fragment_setting != null;
            fragment_setting.setLatLongAltFromGnssStatus(0.0000, 0.0000, 0.0000);

        }
    }

//    @Override
//    public void onOrientationChanged(double orientation, double tilt) {
//
//    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    /**
     * 这个方法用来读取NMEA数据，对于整个程序来说，可以不要。感觉它被淘汰了。
     */
    @Override
    public void onNmeaMessage(String message, long timestamp) {

        final int ALTITUDE_INDEX = 9;
        final int PDOP_INDEX = 15;
        final int HDOP_INDEX = 16;
        final int VDOP_INDEX = 17;
        String[] tokens = message.split(",");
        if (message.startsWith("$GPGGA") || message.startsWith("$GNGNS") || message.startsWith("$GNGGA")) {


            String altitudeMsl = tokens[ALTITUDE_INDEX];

            if (!altitudeMsl.equals("")) {
                mAltitudeMslView.setText(String.format("%.2f", Double.valueOf(altitudeMsl)) + "m");
            }


        }
//        else
//        {
//            mAltitudeMslView.setText(" ");
//        }
        if (message.startsWith("$GNGSA") || message.startsWith("$GPGSA")) {
            mPdopView.setVisibility(View.VISIBLE);
            mHvdopView.setVisibility(View.VISIBLE);
            mPdopLabelView.setVisibility(View.VISIBLE);
            mHvdopLabelView.setVisibility(View.VISIBLE);

            String pdop, hdop, vdop;
            pdop = tokens[PDOP_INDEX];
            hdop = tokens[HDOP_INDEX];
            vdop = tokens[VDOP_INDEX];
            if (!pdop.equals(""))
                mPdopView.setText(String.format("%.1f", Double.valueOf(pdop)));
            else
                mPdopView.setText("未知");
            if (!hdop.equals("") && !vdop.equals(""))
                mHvdopView.setText(String.format("%.1f", Double.valueOf(hdop)) + "/" + vdop.substring(0, 3));
            else
                mHvdopView.setText("未知");
        }


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

    private void updateFixTime() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("yy-MM-dd HH:mm:ss");//设置日期格式
        mFixTimeView.setText(df.format(new Date()));// new Date()为获取当前系统时间

    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onGnssFirstFix(int ttffMillis) {

        if (ttffMillis == 0) {
            mTTFFView.setText("未知");
        } else {
            mTTFFView.setText(TimeUnit.MILLISECONDS.toSeconds(ttffMillis) + " sec");
        }

    }

    /**
     * 当卫星状态改变时，需要设置List<SatelliteStatus> mSatelliteStatusList = new ArrayList<>();往这个列表中添加东西。
     *
     * @param status
     */

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onSatelliteStatusChanged(GnssStatus status) {

        mSatelliteStatusList.clear();
        final int length = status.getSatelliteCount();
        int sat_used = 0;
        int sat_view = 0;

        for (int i = 0; i < length; i++) {

            SatelliteStatus satelliteStatus = new SatelliteStatus();
            satelliteStatus.setSvid(status.getSvid(i));
            satelliteStatus.setElevationDegrees(status.getElevationDegrees(i));
            satelliteStatus.setAzimuthDegrees(status.getAzimuthDegrees(i));

            if (status.getCn0DbHz(i) != 0.0f) {
                sat_view++;
                satelliteStatus.setCn0DbHz(status.getCn0DbHz(i));
            }

            ConstellationType type = SatelliteUtils.getGnssConstellationType(status.getConstellationType(i));

            satelliteStatus.setConstellationType(type);

            if (status.hasCarrierFrequencyHz(i)) {
                satelliteStatus.setCarrierFrequencyHz(status.getCarrierFrequencyHz(i));
                satelliteStatus.setHasCarrierFrequency(true);
            }
            if (status.hasAlmanacData(i)) {
                satelliteStatus.setHasAlmanac(true);
            }
            if (status.hasEphemerisData(i)) {
                satelliteStatus.setHasEphemeris(true);
            }
            if (status.usedInFix(i)) {
                satelliteStatus.setUsedInFix(true);
                sat_used++;
            }
            mSatelliteStatusList.add(satelliteStatus);


            mGnssAdapter.notifyDataSetChanged();

        }
        mNumSats.setText(sat_used + "/" + sat_view + "/" + length);
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

    private class SatelliteStatusAdapter extends RecyclerView.Adapter<SatelliteStatusAdapter.ViewHolder> {


        class ViewHolder extends RecyclerView.ViewHolder {
            private final TextView svId;
            private final TextView gnssFlagHeader;
            private final ImageView gnssFlag;
            private final LinearLayout gnssFlagLayout;
            private final TextView carrierFrequency;
            private final TextView signal;
            private final TextView elevation;
            private final TextView azimuth;
            private final TextView statusFlags;

            ViewHolder(View v) {
                super(v);
                svId = v.findViewById(R.id.sv_id);
                gnssFlagHeader = v.findViewById(R.id.gnss_flag_header);
                gnssFlag = v.findViewById(R.id.gnss_flag);
                gnssFlagLayout = v.findViewById(R.id.gnss_flag_layout);
                carrierFrequency = v.findViewById(R.id.carrier_frequency);
                signal = v.findViewById(R.id.signal);
                elevation = v.findViewById(R.id.elevation);
                azimuth = v.findViewById(R.id.azimuth);
                statusFlags = v.findViewById(R.id.status_flags);
            }

            public TextView getSvId() {
                return svId;
            }

            public TextView getFlagHeader() {
                return gnssFlagHeader;
            }

            public ImageView getFlag() {
                return gnssFlag;
            }

            public LinearLayout getFlagLayout() {
                return gnssFlagLayout;
            }

            public TextView getCarrierFrequency() {
                return carrierFrequency;
            }

            public TextView getSignal() {
                return signal;
            }

            public TextView getElevation() {
                return elevation;
            }

            public TextView getAzimuth() {
                return azimuth;
            }

            public TextView getStatusFlags() {
                return statusFlags;
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.status_row_item, viewGroup, false);
            return new ViewHolder(v);
        }

        @Override
        public int getItemCount() {
            // Add 1 for header row
            return mSatelliteStatusList.size() + 1;

        }

        //设置数据列表里面的数据
        @SuppressLint({"SetTextI18n", "DefaultLocale"})
        public void onBindViewHolder(@NonNull ViewHolder v, final int position) {
            //第一行开头
            if (position == 0) {

                // Show the header field for the GNSS flag and hide the ImageView
                v.getFlagHeader().setVisibility(View.VISIBLE);
                v.getFlag().setVisibility(View.GONE);
                v.getFlagLayout().setVisibility(View.GONE);

                // Populate the header fields
                v.getSvId().setText("ID");
                v.getSvId().setTypeface(v.getSvId().getTypeface(), Typeface.BOLD);
                //系统为GNSS

                v.getFlagHeader().setText("GNSS");

                //如果手机型号能接收到频率,频率那一列就可见
                if (SatelliteUtils.isGnssCarrierFrequenciesSupported()) {
                    v.getCarrierFrequency().setVisibility(View.VISIBLE);
                    v.getCarrierFrequency().setText("CF");
                    v.getCarrierFrequency().setTypeface(v.getCarrierFrequency().getTypeface(), Typeface.BOLD);
                } else {
                    v.getCarrierFrequency().setVisibility(View.GONE);
                }

                v.getSignal().setText("Cno");

                v.getSignal().setTypeface(v.getSignal().getTypeface(), Typeface.BOLD);


                v.getElevation().setText("Elev");
                v.getElevation().setTypeface(v.getElevation().getTypeface(), Typeface.BOLD);
                v.getAzimuth().setText("Azim");
                v.getAzimuth().setTypeface(v.getAzimuth().getTypeface(), Typeface.BOLD);
                //标识号
                v.getStatusFlags().setText("Flags");
                v.getStatusFlags().setTypeface(v.getStatusFlags().getTypeface(), Typeface.BOLD);
            } else {
                // There is a header at 0, so the first data row will be at position - 1, etc.
                int dataRow = position - 1;

                List<SatelliteStatus> sats = null;


                sats = mSatelliteStatusList;


                // Show the row field for the GNSS flag mImage and hide the header
                v.getFlagHeader().setVisibility(View.GONE);
                v.getFlag().setVisibility(View.VISIBLE);
                v.getFlagLayout().setVisibility(View.VISIBLE);

                // Populate status data for this row
                assert sats != null;
                v.getSvId().setText(Integer.toString(sats.get(dataRow).getSvid()));
                v.getFlag().setScaleType(ImageView.ScaleType.FIT_START);


                ConstellationType type = sats.get(dataRow).getConstellationType();
                switch (type) {
                    case GPS:
                        v.getFlag().setVisibility(View.VISIBLE);
                        v.getFlag().setImageDrawable(mFlagUsa);
                        break;
                    case GLONASS:
                        v.getFlag().setVisibility(View.VISIBLE);
                        v.getFlag().setImageDrawable(mFlagRussia);
                        break;
                    case QZSS:
                        v.getFlag().setVisibility(View.VISIBLE);
                        v.getFlag().setImageDrawable(mFlagJapan);
                        break;
                    case BEIDOU:
                        v.getFlag().setVisibility(View.VISIBLE);
                        v.getFlag().setImageDrawable(mFlagChina);
                        break;
                    case GALILEO:
                        v.getFlag().setVisibility(View.VISIBLE);
                        v.getFlag().setImageDrawable(mFlagEU);
                        break;
                    case IRNSS:
                        v.getFlag().setVisibility(View.VISIBLE);
                        v.getFlag().setImageDrawable(mFlagIndia);
                        break;
                    case SBAS:
                        //setSbasFlag(sats.get(dataRow), v.getFlag());
                        break;
                    case UNKNOWN:
                        v.getFlag().setVisibility(View.INVISIBLE);
                        break;
                }
                if (SatelliteUtils.isGnssCarrierFrequenciesSupported()) {
                    if (sats.get(dataRow).isHasCarrierFrequency()) {
                        String carrierLabel = CarrierFreqUtils.getCarrierFrequencyLabel(sats.get(dataRow));
                        if (!carrierLabel.equals(CF_UNKNOWN)) {
                            // Make sure it's the normal text size (in case it's previously been
                            // resized to show raw number).  Use another TextView for default text size.
                            v.getCarrierFrequency().setTextSize(COMPLEX_UNIT_PX, v.getSvId().getTextSize());
                            // Show label such as "L1"
                            v.getCarrierFrequency().setText(carrierLabel);
                        } else {
                            // Shrink the size so we can show raw number
                            v.getCarrierFrequency().setTextSize(COMPLEX_UNIT_DIP, 10);
                            // Show raw number for carrier frequency - Convert Hz to MHz
                            float carrierMhz = sats.get(dataRow).getCarrierFrequencyHz() / 1000000.00f;
                            v.getCarrierFrequency().setText(String.format("%.3f", carrierMhz));
                        }
                    } else {
                        v.getCarrierFrequency().setText("");
                    }
                } else {
                    v.getCarrierFrequency().setVisibility(View.GONE);
                }
                if (String.valueOf(sats.get(dataRow).getCn0DbHz()) != null) {
                    v.getSignal().setText(String.format("%.1f", sats.get(dataRow).getCn0DbHz()));
                } else {
                    v.getSignal().setText("");
                }

                if (String.valueOf(sats.get(dataRow).getElevationDegrees()) != null) {
                    v.getElevation().setText(String.format("%.1f", sats.get(dataRow).getElevationDegrees()) + "°");
                } else {
                    v.getElevation().setText("");
                }

                if (String.valueOf(sats.get(dataRow).getAzimuthDegrees()) != null) {
                    v.getAzimuth().setText(String.format("%.1f", sats.get(dataRow).getAzimuthDegrees()) + "°");
                } else {
                    v.getAzimuth().setText("");
                }

                char[] flags = new char[3];
                flags[0] = !sats.get(dataRow).isHasAlmanac() ? ' ' : 'A';
                flags[1] = !sats.get(dataRow).isHasEphemeris() ? ' ' : 'E';
                flags[2] = !sats.get(dataRow).isUsedInFix() ? ' ' : 'U';
                v.getStatusFlags().setText(new String(flags));
            }
        }


    }

}



