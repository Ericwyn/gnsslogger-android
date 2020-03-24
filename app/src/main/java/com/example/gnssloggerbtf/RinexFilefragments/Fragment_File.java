package com.example.gnssloggerbtf.RinexFilefragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.location.GnssMeasurementsEvent;
import android.location.GnssNavigationMessage;
import android.location.Location;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adam.gpsstatus.GpsStatusProxy;
import com.example.gnssloggerbtf.GnssMeasurementListener;
import com.example.gnssloggerbtf.GnssNavigationMessageListener;
import com.example.gnssloggerbtf.R;
import com.example.gnssloggerbtf.RinexFileLogger.RinexOBodyFile;
import com.example.gnssloggerbtf.RinexFileLogger.RinexO_Header;
import com.example.gnssloggerbtf.RinexFilefragments.Constellations.GpsGalileoBdsGlonassQzssConstellation;
import com.example.gnssloggerbtf.RinexFilefragments.Constellations.GpsTime;
import com.example.gnssloggerbtf.RinexFilefragments.Nav.GpsNavigationConv;
import com.example.gnssloggerbtf.Settingfragments.Fragment_Setting;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Fragment_File extends Fragment implements GnssMeasurementListener, GnssNavigationMessageListener {

    private final static String TAG = "Fragment_File";


    private final static String InterDic = "GNSSLOGGER";

    private Button bt_Start, bt_End, bt_Open, bt_Share, bt_delete, bt_rename;

    private boolean isStart = false;
    private boolean isEnd = false;


    private Context mContext;

    private GpsNavigationConv mGpsNavigationConv;


    private Chronometer timer;


    public static String FormatMiss(int miss) {
        String hh = miss / 3600 > 9 ? miss / 3600 + "" : "0" + miss / 3600;
        String mm = (miss % 3600) / 60 > 9 ? (miss % 3600) / 60 + "" : "0" + (miss % 3600) / 60;
        String ss = (miss % 3600) % 60 > 9 ? (miss % 3600) % 60 + "" : "0" + (miss % 3600) % 60;
        return hh + ":" + mm + ":" + ss;
    }


    //获取GNSS原始观测数据
    private GpsGalileoBdsGlonassQzssConstellation mGpsGalileoBdsGlonassQzssConstellation = new GpsGalileoBdsGlonassQzssConstellation();


    //recyclerview
    private RecyclerView mFileListRecyclerView;

    private List<MyFile> mMyFileList = new ArrayList<>();

    private MyFileAdapter mMyFileAdapter;


    //当前选中的文件
    private MyFile currentFile;

    //头文件信息，从fragment_setting 中获得


    //GNSS信号
    private GpsStatusProxy proxy;


    /**
     * 将数据存入外部存储，成文件，这个一直出错，所以没用到
     *
     * @param content 数据
     */
    private void savetoExternalStorage(String content, String filename) {

        BufferedWriter currentFileWriter = null;
        try {
            File dir = new File(Environment.getExternalStorageDirectory(), "skypan");
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File currentFile = new File(dir, filename);
            if (!currentFile.exists()) {
                currentFile.createNewFile();
            }

            String currentFilePath = currentFile.getAbsolutePath();
            currentFileWriter = new BufferedWriter(new FileWriter(currentFile));
            currentFileWriter.write(content);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            try {
                currentFileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 将数据存入内部存储，成文件
     *
     * @param content  数据
     * @param filename 文件名
     */
    private void savetoIntertalStorage(String content, String filename) {
        FileOutputStream fileOutputStream = null;

//        File textsDir = new File(mContext.getFilesDir().getAbsolutePath() + File.separator + "gnss1.0");
//        if(!textsDir.exists()){
//            textsDir.mkdir();
//        }
        // File file=new File(textsDir,filename);
        try {
            fileOutputStream = Objects.requireNonNull(getActivity()).openFileOutput(filename, Context.MODE_PRIVATE);
            // fileOutputStream=new FileOutputStream(file);
            fileOutputStream.write(content.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                assert fileOutputStream != null;
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 用file writer 写入文件
     */
    private void savetoIntertalStorageUseFW(String content, String filename) {
        FileWriter fileWriter = null;
        try {
//            fileOutputStream = Objects.requireNonNull(getActivity()).openFileOutput(filename, Context.MODE_PRIVATE);
//
//            fileOutputStream.write(content.getBytes());
            fileWriter = new FileWriter(filename);
            //fileWriter=getActivity().openFileOutput(filename, Context.MODE_PRIVATE);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
//                assert fileOutputStream != null;
//                fileOutputStream.close();
                assert fileWriter != null;
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void savetoIntertalStorageUseBuff(String content, String filename) {

        BufferedOutputStream buff = null;
        File textsDir = new File(mContext.getFilesDir().getAbsolutePath() + File.separator + InterDic);
        if (!textsDir.exists()) {
            textsDir.mkdir();
        }
        File file = new File(textsDir, filename);
        try {
            buff = new BufferedOutputStream(new FileOutputStream(file));
            buff.write(content.getBytes());
            buff.flush();
            buff.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                buff.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private RinexO_Header getHeaderInfoFromFragmentSetting() {
        RinexO_Header mRinexO_header = new RinexO_Header();
        try {
            assert getFragmentManager() != null;
            //通过标签找fragment_setting ,标签的设置在mainactivity里
            Fragment_Setting fragment_setting = (Fragment_Setting) getFragmentManager().findFragmentByTag(1 + "");
            assert fragment_setting != null;
            mRinexO_header = fragment_setting.getRinexO_Header();
        } catch (Exception e) {
            Log.d(TAG, "getHeaderInfoFromFragmentSetting出错");
        }
        return mRinexO_header;
    }


    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_file, container, false);

        //解决分享文件时遇到的问题
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();


        bt_Start = v.findViewById(R.id.bt_start);
        bt_End = v.findViewById(R.id.bt_end);

        bt_Share = v.findViewById(R.id.bt_share);
        bt_Open = v.findViewById(R.id.bt_open);
        bt_delete = v.findViewById(R.id.bt_delete);
        bt_rename = v.findViewById(R.id.bt_rename);

        timer = v.findViewById(R.id.timer);
        timer.setText("0:00:00");
        //设置mContext
        mContext = getContext();
        mGpsNavigationConv = new GpsNavigationConv(mContext);


        //recyclerview的设置
        LinearLayoutManager mLinearLayout = new LinearLayoutManager(getContext());
        mLinearLayout.setOrientation(RecyclerView.VERTICAL);

        mFileListRecyclerView = v.findViewById(R.id.rv_file_list);

        mMyFileAdapter = new MyFileAdapter();

        mFileListRecyclerView.setAdapter(mMyFileAdapter);

        mFileListRecyclerView.setFocusable(false);
        mFileListRecyclerView.setFocusableInTouchMode(false);
        mFileListRecyclerView.setLayoutManager(mLinearLayout);
        mFileListRecyclerView.setNestedScrollingEnabled(false);
        //添加Android自带的分割线
        mFileListRecyclerView.addItemDecoration(new DividerItemDecoration(Objects.requireNonNull(getContext()), DividerItemDecoration.VERTICAL));


        mMyFileAdapter.setItemClickListener(new OnRecyclerViewClickListener() {
            @Override
            public void onItemClickListener(View view) {
                int position = mFileListRecyclerView.getChildAdapterPosition(view);
                mMyFileAdapter.setCurrentPosition(position);
                currentFile = mMyFileList.get(position);

                Toastutil.showmsg(getContext(), "当前选中文件" + currentFile.getFileName());
            }

            @Override
            public void onItemLongClickListener(View view) {

            }
        });

        proxy = GpsStatusProxy.getInstance(getContext());
        proxy.register();

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        bt_Start.setOnClickListener(new onclick());
        bt_End.setOnClickListener(new onclick());
        bt_Open.setOnClickListener(new onclick());
        bt_Share.setOnClickListener(new onclick());
        bt_delete.setOnClickListener(new onclick());
        bt_rename.setOnClickListener(new onclick());


    }


    @Override
    public void onGnssMeasurementsReceived(GnssMeasurementsEvent eventArgs) {


        if (isStart && !isEnd) {
            mGpsGalileoBdsGlonassQzssConstellation.updateMeasurements(eventArgs);

        }
    }

    @Override
    public void onLocationChanged(Location location) {

        try {
            proxy.notifyLocation(location);
        } catch (Exception e) {
            Log.d(TAG, "还未获得定位结果");
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

    private int miss = 0;

    @Override
    public void onGnssNavigationMessageReceived(GnssNavigationMessage event) {

        if (isStart && !isEnd) {
            //卫星号
            int svid = event.getSvid();
            //原始数据
            byte[] rawData = event.getData();
            //
            int messageId = event.getMessageId();
            //
            int submessageId = event.getSubmessageId();
            int type = event.getType();

            mGpsNavigationConv.onGpsNavMessageReported(svid, type, submessageId, rawData);

        }

    }

    @Override
    public void onGnssNavigationMessageStatusChanged(int status) {

    }

    private class onclick implements View.OnClickListener {

        @SuppressLint({"DefaultLocale", "SetTextI18n"})
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.bt_start:

                    //将其清零，表示下一次记录开始.
                    mGpsGalileoBdsGlonassQzssConstellation.deleteepochMeasurementList();


                    bt_Start.setEnabled(false);
                    bt_End.setEnabled(true);
                    // 将计时器清零
                    timer.setBase(SystemClock.elapsedRealtime());
                    //开始计时
                    timer.start();
                    timer.setText("0:00:00");
                    miss = 0;


                    timer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                        @Override
                        public void onChronometerTick(Chronometer chronometer) {
                            miss = miss + 1;
                            chronometer.setText(FormatMiss(miss));
                        }
                    });
                    isStart = true;
                    isEnd = false;


                    Toastutil.showmsg(getContext(), "Start Logging");
                    break;
                case R.id.bt_end:


                    timer.stop();

                    bt_End.setEnabled(false);
                    bt_Start.setEnabled(true);
                    isStart = false;
                    isEnd = true;

                    //获取头文件信息
                    RinexO_Header mRinexO_Header = getHeaderInfoFromFragmentSetting();


                    if (mRinexO_Header != null) {
                        if (mGpsGalileoBdsGlonassQzssConstellation.getEpochMeasurementList().size() > 0) {
                            if (mRinexO_Header.getVersion().equals("3.03")) {
                                StringBuilder stringBuilderHeader = mRinexO_Header.HeaderConvertRinexO3();
                                //Log.d(TAG, mRinexO_Header.getFileName());

                                //获取主体文件信息
                                RinexOBodyFile rinexOBodyFile = new RinexOBodyFile(mGpsGalileoBdsGlonassQzssConstellation.getEpochMeasurementList());

                                StringBuilder stringBuilderBody = rinexOBodyFile.getRinex3bodyString();

                                //获取观测开始时间
                                GpsTime firstOBSTime = mGpsGalileoBdsGlonassQzssConstellation.getEpochMeasurementList().get(0).getEpochTime();

                                //添加TIME OF FIRST OBS
                                stringBuilderHeader.append(String.format("%6d%6d%6d%6d%6d%13.7f     GPS         TIME OF FIRST OBS", firstOBSTime.getYear(), firstOBSTime.getMonth(), firstOBSTime.getDay(), firstOBSTime.getHour(), firstOBSTime.getMinute(), firstOBSTime.getSecond()));

                                stringBuilderHeader.append("\n");

                                //添加观测文件的头文件结束标志
                                stringBuilderHeader.append("                                                            END OF HEADER");
                                stringBuilderHeader.append("\n");
                                //将头文件和主体文件合并

                                long stee = System.currentTimeMillis();
                                stringBuilderHeader.append(stringBuilderBody);
                                long edee = System.currentTimeMillis();
                                Log.d("字符串拼接用时", edee - stee + "ms");


                                String filename = mRinexO_Header.getFileName() + firstOBSTime.getDOY() + firstOBSTime.getHourLabel(firstOBSTime.getHour()) + String.format("%.0f", firstOBSTime.getWeeksec()) + "." + firstOBSTime.getYearSimplify() + "o";


                                //向内部存储中写入文件
                                long st = System.currentTimeMillis();
                                savetoIntertalStorageUseBuff(stringBuilderHeader.toString(), filename);
                                long ed = System.currentTimeMillis();
                                Log.d("文件用时", ed - st + "-------");
                                //savetoIntertalStorageUseFW(stringBuilderHeader.toString(), filename);
                                //output(filename, stringBuilderHeader.toString());
                                @SuppressLint("DefaultLocale") MyFile myFile = new MyFile(filename, String.format("%.2f MB", stringBuilderHeader.length() / (1024.0 * 1024.0)), firstOBSTime.getGpsTimeStringForFileDate(), "3.03", "O");
                                mMyFileList.add(myFile);
                                mMyFileAdapter.notifyDataSetChanged();
                            }
                            if (mRinexO_Header.getVersion().equals("2.11")) {
                                StringBuilder stringBuilderHeader = mRinexO_Header.HeaderConvertRinexO2();
                                //Log.d(TAG, mRinexO_Header.getFileName());

                                //获取主体文件信息
                                RinexOBodyFile rinexOBodyFile = new RinexOBodyFile(mGpsGalileoBdsGlonassQzssConstellation.getEpochMeasurementList());

                                StringBuilder stringBuilderBody = rinexOBodyFile.getRinex2bodyString();

                                //获取观测开始时间
                                GpsTime firstOBSTime = mGpsGalileoBdsGlonassQzssConstellation.getEpochMeasurementList().get(0).getEpochTime();

                                //添加TIME OF FIRST OBS
                                stringBuilderHeader.append(String.format("%6d%6d%6d%6d%6d%13.7f     GPS         TIME OF FIRST OBS", firstOBSTime.getYear(), firstOBSTime.getMonth(), firstOBSTime.getDay(), firstOBSTime.getHour(), firstOBSTime.getMinute(), firstOBSTime.getSecond()));

                                stringBuilderHeader.append("\n");

                                //添加观测文件的头文件结束标志
                                stringBuilderHeader.append("                                                            END OF HEADER");
                                stringBuilderHeader.append("\n");
                                //将头文件和主体文件合并
                                stringBuilderHeader.append(stringBuilderBody);


                                String filename = mRinexO_Header.getFileName() + firstOBSTime.getDOY() + firstOBSTime.getHourLabel(firstOBSTime.getHour()) + String.format("%.0f", firstOBSTime.getWeeksec()) + "." + firstOBSTime.getYearSimplify() + "o";

                                //向内部存储中写入文件
                                savetoIntertalStorageUseBuff(stringBuilderHeader.toString(), filename);

                                @SuppressLint("DefaultLocale") MyFile myFile = new MyFile(filename, String.format("%.2f MB", stringBuilderHeader.length() / (1024.0 * 1024.0)), firstOBSTime.getGpsTimeStringForFileDate(), "2.11", "O");
                                mMyFileList.add(myFile);
                                mMyFileAdapter.notifyDataSetChanged();

                            }
                        } else {
                            Toastutil.showmsg(mContext, "观测时间太短了！！！");
                        }

                    } else {
                        Toastutil.showmsg(mContext, "请前往设置输出文件内容信息");
                    }

                    if (mRinexO_Header != null) {
                        //对于导航文件的时间，可以和观测开始时间一致便可。
                        //获取观测开始时间
                        if (mGpsGalileoBdsGlonassQzssConstellation.getEpochMeasurementList().size() > 0) {

                            try {
                                GpsTime firstNavTime = mGpsGalileoBdsGlonassQzssConstellation.getEpochMeasurementList().get(0).getEpochTime();
                                //输出导航文件，现在只有3.03版本
                                if (mRinexO_Header.getVersion().equals("3.03")) {
                                    StringBuilder stringBuilder = mGpsNavigationConv.getNavgationMessageToRinex3Body();
                                    StringBuilder stringBuilder1 = mGpsNavigationConv.getNavigationMessageToRinex3Header();
                                    stringBuilder1.append(stringBuilder);
                                    String filename = mRinexO_Header.getFileName() + firstNavTime.getDOY() + firstNavTime.getHourLabel(firstNavTime.getHour()) + String.format("%.0f", firstNavTime.getWeeksec()) + "." + firstNavTime.getYearSimplify() + "n";
                                    savetoIntertalStorageUseBuff(stringBuilder1.toString(), filename);
                                    MyFile myFile = new MyFile(filename, String.format("%.2f MB", stringBuilder1.length() / (1024.0 * 1024.0)), firstNavTime.getGpsTimeStringForFileDate(), "3.03", "N");
                                    mMyFileList.add(myFile);
                                    mMyFileAdapter.notifyDataSetChanged();
                                }
                                if (mRinexO_Header.getVersion().equals("2.11")) {
                                    StringBuilder stringBuilder = mGpsNavigationConv.getNavgationMessageToRinex2Body();
                                    StringBuilder stringBuilder1 = mGpsNavigationConv.getNavigationMessageToRinex2Header();
                                    stringBuilder1.append(stringBuilder);
                                    String filename = mRinexO_Header.getFileName() + firstNavTime.getDOY() + firstNavTime.getHourLabel(firstNavTime.getHour()) + String.format("%.0f", firstNavTime.getWeeksec()) + "." + firstNavTime.getYearSimplify() + "n";
                                    savetoIntertalStorageUseBuff(stringBuilder1.toString(), filename);
                                    MyFile myFile = new MyFile(filename, String.format("%.2f MB", stringBuilder1.length() / (1024.0 * 1024.0)), firstNavTime.getGpsTimeStringForFileDate(), "2.11", "N");
                                    mMyFileList.add(myFile);
                                    mMyFileAdapter.notifyDataSetChanged();
                                }
                            } catch (Exception e) {
                                Log.d("File", "输出导航文件出错");
                            }
                        } else {
                            Toastutil.showmsg(mContext, "观测时间太短了！！！");
                        }

                    } else {
                        Toastutil.showmsg(mContext, "请前往设置输出文件内容信息");
                    }
                    //将其清零，表示下一次记录开始.
                    mGpsGalileoBdsGlonassQzssConstellation.deleteepochMeasurementList();

                    //每次出现新的文件，都需要清零  初始化。。。
                    mMyFileAdapter.setCurrentPosition(-1);
                    currentFile=null;

                    Toastutil.showmsg(getContext(), "End Logging Open Convert");
                    break;
                case R.id.bt_open:
                    try {
                        if (currentFile != null) {
                            String filename = currentFile.getFileName();


                            File myfile = new File(mContext.getFilesDir() + "/" + InterDic + "/", filename);
                            OpenFileUtil.openFile(mContext, myfile);
                        }
                    }
                    catch (Exception e) {
                        Log.d("File", "未选中文件");
                        Toastutil.showmsg(mContext, "未选中文件");
                    }
                    break;
                case R.id.bt_delete:

                    if (currentFile != null) {
                        AlertDialog.Builder builder_delete = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));

                        builder_delete.setMessage("Delete or not delete, that's the question！").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                try {
                                    if (currentFile != null) {
                                        String filename = currentFile.getFileName();
                                        File myfile = new File(mContext.getFilesDir() + "/" + InterDic + "/", filename);
                                        myfile.delete();

                                        int position = mMyFileAdapter.getCurrentPosition();
                                        mMyFileList.remove(position);
                                        mMyFileAdapter.notifyDataSetChanged();

                                        //每次删除  需要重新清零   初始化
                                        mMyFileAdapter.setCurrentPosition(-1);
                                        currentFile=null;

                                    }
                                } catch (Exception e) {
                                    Toastutil.showmsg(mContext, "未选中文件");
                                }


                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
                    } else {
                        Toastutil.showmsg(mContext, "未选中文件");
                    }
                    break;
                case R.id.bt_share:

                    try {
                        if (currentFile != null) {
                            String filename = currentFile.getFileName();
                            File myfile = new File(mContext.getFilesDir() + "/" + InterDic + "/", filename);
                            ShareFileUtil.shareFile(mContext, myfile);

                        }

                    } catch (Exception e) {
                        Log.d("File", "未选中文件");
                        Toastutil.showmsg(mContext, "未选中文件");
                    }
                    break;
                case R.id.bt_rename:
                    if (currentFile != null) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));

                        View view = LayoutInflater.from(Objects.requireNonNull(getActivity())).inflate(R.layout.alert_20chars, null);
                        final EditText met_FileName = view.findViewById(R.id.et_20chars);

                        builder.setMessage("Please input a new File Name(max 10 characters)").setView(view).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    if (currentFile != null) {
                                        File myfile = new File(mContext.getFilesDir() + "/" + InterDic + "/", currentFile.getFileName());
                                        String newname = met_FileName.getText().toString();
                                        if (!newname.equals("")) {
                                            File newfile = new File(mContext.getFilesDir() + "/" + InterDic + "/", newname);
                                            myfile.renameTo(newfile);

                                            int position = mMyFileAdapter.getCurrentPosition();
                                            mMyFileList.get(position).setFileName(newname);
                                            mMyFileAdapter.notifyDataSetChanged();
                                        }
                                    }
                                }
                                catch (Exception e)
                                {
                                    Toastutil.showmsg(mContext, "未选中文件");
                                }

                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();

                    } else {
                        Toastutil.showmsg(mContext, "未选中文件");
                    }

                    break;

            }
        }
    }


    private class MyFileAdapter extends RecyclerView.Adapter<MyFileAdapter.ViewHolder> {

        private OnRecyclerViewClickListener listener;

        public void setItemClickListener(OnRecyclerViewClickListener itemClickListener) {
            listener = itemClickListener;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_file, parent, false);


            return new ViewHolder(v);
        }

        private int currentPosition = -1;

        public int getCurrentPosition() {
            return currentPosition;
        }

        public void setCurrentPosition(int currentPosition) {
            this.currentPosition = currentPosition;
        }

        //向列表中写入数据
        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
            MyFile myFile = mMyFileList.get(position);
            holder.getMyFileName().setText(myFile.getFileName());
            holder.getMyFileDate().setText(myFile.getFileDate());
            holder.getMyFileSize().setText(myFile.getFileSize());
            holder.getMyFileVersion().setText(myFile.getFileVersion());
            holder.getMyFileType().setText(myFile.getFileType());

            //接口回调
            if (listener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onItemClickListener(v);
                        notifyDataSetChanged();

                    }
                });
            }
            //如果下标和传回来的下标相等 那么确定是点击的条目 把背景设置一下颜色
            if (position == getCurrentPosition()) {
                holder.itemView.setBackgroundColor(Color.rgb(254, 251, 239));
            } else {
                //            否则的话就全白色初始化背景
                holder.itemView.setBackgroundColor(Color.WHITE);
            }

        }


        @Override
        public int getItemCount() {
            return mMyFileList.size();
        }


        class ViewHolder extends RecyclerView.ViewHolder {
            TextView myFileName;
            ImageView myFileImage;
            TextView myFileSize;
            TextView myFileDate;
            TextView myFileVersion;
            TextView myFileType;

            ViewHolder(View itemView) {
                super(itemView);
                myFileName = itemView.findViewById(R.id.myFileName);
                myFileImage = itemView.findViewById(R.id.myFileImage);
                myFileSize = itemView.findViewById(R.id.fileSize);
                myFileDate = itemView.findViewById(R.id.fileDate);
                myFileVersion = itemView.findViewById(R.id.fileVersion);
                myFileType = itemView.findViewById(R.id.fileType);
            }

            public TextView getMyFileType() {
                return myFileType;
            }

            public TextView getMyFileDate() {
                return myFileDate;
            }

            public TextView getMyFileName() {
                return myFileName;
            }

            public TextView getMyFileSize() {
                return myFileSize;
            }

            public TextView getMyFileVersion() {
                return myFileVersion;
            }
        }


    }


}

