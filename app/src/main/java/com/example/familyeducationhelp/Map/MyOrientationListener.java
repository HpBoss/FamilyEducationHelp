package com.example.familyeducationhelp.Map;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/*
 * @Comment :
 * @Created :2019/10/13 12:30
 * @Name : HP
 */
public class MyOrientationListener implements SensorEventListener {
 
    private SensorManager mSensorManager;
    private Context mContext;
    private Sensor accelerometer; // 加速度传感器
    private Sensor magnetic; // 地磁场传感器
    private OnOrientationListener mOnOrientationListener;
    private float[] accelerometerValues = new float[3];
    private float[] magneticFieldValues = new float[3];

    public void setmOnOrientationListener(OnOrientationListener mOnOrientationListener) {
        this.mOnOrientationListener = mOnOrientationListener;
    }

    public MyOrientationListener(Context context) {
        this.mContext = context;
    }
 
    public void star() {
        mSensorManager = (SensorManager) mContext
                .getSystemService(Context.SENSOR_SERVICE);
        if (mSensorManager != null) {
            // 初始化加速度传感器
            accelerometer = mSensorManager
                    .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            // 初始化地磁场传感器
            magnetic = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        }

        if (accelerometer != null && null != magnetic) {//注册两种监听
            mSensorManager.registerListener(this,
                    accelerometer, Sensor.TYPE_ACCELEROMETER);
            mSensorManager.registerListener(this, magnetic,
                    Sensor.TYPE_MAGNETIC_FIELD);
        }
    }
 
    public void stop() {//停止监听
        //停止定位
        mSensorManager.unregisterListener(this);
    }
 
    @Override
    public void onSensorChanged(SensorEvent event) {//传感器接收到不同的变化信息，收集、反应数据
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            accelerometerValues = event.values;
//            Log.d("加速度:","accelerometerValues");
        }
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            magneticFieldValues = event.values;
//            Log.d("磁场:","magneticFieldValues");
        }
        calculateOrientation();//获取到具体的方向度数
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {//重写精度变化方法

    }
    private void calculateOrientation() {
        float[] values = new float[3];
        float[] R = new float[9];
        SensorManager.getRotationMatrix(R, null, accelerometerValues,
                magneticFieldValues);
        SensorManager.getOrientation(R, values);
        values[0] = (float) Math.toDegrees(values[0]);
        if (mOnOrientationListener != null) {
            mOnOrientationListener.onOrientationChanged(values[0]);//实现接口
        }
    }
 
    public interface OnOrientationListener {//接口
        void onOrientationChanged(float x);
    }
}
