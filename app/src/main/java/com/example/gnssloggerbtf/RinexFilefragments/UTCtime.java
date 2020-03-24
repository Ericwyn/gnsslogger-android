package com.example.gnssloggerbtf.RinexFilefragments;

import android.annotation.SuppressLint;
import android.location.GnssClock;

public class UTCtime {

    private   int year;
    private  int month;
    private  int day;
    private  int hour;
    private  int minute;
    private  double sec;
    private  String UTCtime;
    public UTCtime(GnssClock gnssClock)
    {
        getUTCTimes(gnssClock);
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getYear() {
        return year;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getMonth() {
        return month;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getDay() {
        return day;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getHour() {
        return hour;
    }

    public void setMinute(int min) {
        this.minute = min;
    }

    public int getMinute() {
        return minute;
    }

    public void setSecond(double sec) {
        this.sec = sec;
    }

    public double getSecond() {
        return sec;
    }


    public void setUTCtime(String UTCtime) {
        this.UTCtime = UTCtime;
    }

    public String getUTCtime() {
        return UTCtime;
    }



    /**
     * 这个是获取UTC时间的方法
     * @param gnssClock
     * @return time_args 专门存储utc时间的类
     */
    @SuppressLint("DefaultLocale")
    private void getUTCTimes(GnssClock gnssClock) {
        //存放时间

        //闰秒,年，月，日，时，分，秒
        int LeapSecond, year = 0, month = 0, hour = 0, day = 0, min = 0;
        double second =0;
        //
        long   TimeNanos, FullBiasNanos;
        double BiasNanos,UTCtimeNanos, GPStimeNanos;
        //utc秒，Gps秒，周内秒
        double UTCtimeSec, GPStimeSec, TowSec=0;
        //最后得到的是秒，看总共有多少天
        int  dayNum;
        //剩余的小天
        double minday = 0;

        String UTCtime = new String(" ");
        //判断能不能获取到BiasNanos，FullBiasNanos
        boolean fail = false;
        FullBiasNanos = 0;
        BiasNanos = 0;
        //获取gnss接收机内部硬件的时钟值  ???
        TimeNanos = gnssClock.getTimeNanos();


        if (gnssClock.hasFullBiasNanos()) {
            //获取gps接收机内部硬件时钟与gps时的差
            FullBiasNanos=gnssClock.getFullBiasNanos();      //这个针对非GPS星座有问题，因为非GPS星座用的时间系统不一样

        }
        else
            fail=true;

        if (gnssClock.hasBiasNanos()) {

            BiasNanos =  gnssClock.getBiasNanos();
        }
        else
            fail=true;

        if (!fail) {
            if (gnssClock.hasLeapSecond()) {
                LeapSecond = gnssClock.getLeapSecond();
                UTCtimeNanos = TimeNanos - (FullBiasNanos + BiasNanos) - LeapSecond * 1000000000;
                UTCtimeSec = UTCtimeNanos / 1000000000.0;
            } else {
                GPStimeNanos = TimeNanos - (FullBiasNanos + BiasNanos);
                GPStimeSec = GPStimeNanos / 1000000000.0;
                //这里的18指的是跳秒总共18s
                UTCtimeSec = GPStimeSec - 18;
            }
            //周内秒
            TowSec = UTCtimeSec % (3600 * 24 * 7);
            /**
             * 时间转换问题
             */

            //从一月6日0时算起，前面有6天，先加上
            UTCtimeSec = UTCtimeSec + 6* 24 * 60 * 60;
            //总共有多少天
            dayNum = (int) Math.floor(UTCtimeSec / (3600.0 * 24));
            //剩余的小天
            minday=(UTCtimeSec / (3600.0 * 24)-dayNum);



            for(int i=1980;;i++)
            {
                if ((i % 4 == 0 && i % 100 != 0) || i % 400 == 0)
                {
                    dayNum=dayNum-366;
                }
                else
                {
                    dayNum=dayNum-365;
                }
                if (dayNum<365)
                {
                    year=i+1;
                    break;
                }
            }
            //Log.d("年", String.valueOf(year));
            for(int i=1;;i++)
            {
                if (i == 1 || i == 3 || i == 5 || i == 7 || i == 8 || i == 10 || i == 12)
                {
                    dayNum=dayNum-31;
                }
                else if (i == 4 || i == 6 || i == 9 || i == 11)
                    dayNum=dayNum-30;
                else
                {
                    if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0)
                    {
                        dayNum=dayNum-29;
                        if(dayNum<29)
                        {
                            month=i+1;
                            break;
                        }

                    }
                    else
                    {dayNum=dayNum-28;
                        if(dayNum<28)
                        {
                            month=i+1;
                            break;
                        }}

                }
                if(dayNum<30)
                {
                    month=i+1;
                    break;
                }
            }
//            Log.d("月", String.valueOf(month));
//            Log.d("daynum", String.valueOf(dayNum));
            day= (int) Math.floor(dayNum);

            hour= (int) Math.floor(minday*24);
            minday=minday*24-hour;
            min=(int) Math.floor(minday*60);
            minday=minday*60-min;
            second=minday*60;
            UTCtime = (year + "-" + month + "-" + day + " " + hour + ":" + min + ":" + String.format("%.2f",second));
        } else
            UTCtimeSec = -1;
        this.setYear(year);
        this.setMonth(month);
        this.setDay(day);
        this.setHour(hour);
        this.setMinute(min);
        this.setSecond(second);
        this.setUTCtime(UTCtime);
    }
}
