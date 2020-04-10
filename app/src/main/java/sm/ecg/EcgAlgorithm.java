package sm.ecg;

import android.util.Log;


import com.sm.nadaman.common.event.MsgEvent;

import org.greenrobot.eventbus.EventBus;

import static com.sm.nadaman.common.ConstantKt.ACTION_GET_HR;


/**
 * 动态库使用类
 */

public class EcgAlgorithm {

    private int hrException = 3;
    private MsgEvent msgEvent;

    public EcgAlgorithm() {

    }

    public void changeMode(int i) {
        nativeFilterSelect(i, 1);
    }

    public int lvbo(short s) {
        int t = nativeEcgWrite(s);
        getHeartHR();
//        hrException = nativeGetArrType();
        if (hrException == 11) {

        }
        Log.e("kankan", "  心率的值为：" + String.valueOf(HR) + "失常类型" + hrException + "  传进来的数值为：" + s);
        return t;
    }

    int HR;
    int testHR;

    public void getHeartHR() {
        HR = nativeGetHR();
        Log.e("kankan", "心率的值为：" + HR);

        if (HR > 0) {
            testHR = HR;
        } else {
            HR = 0;
        }
        msgEvent = new MsgEvent(ACTION_GET_HR, HR + "");
        EventBus.getDefault().post(msgEvent);
    }

    public int getHeartException() {
//        hrException = nativeGetArrType() % 256;
        return hrException;
    }

    static {
        System.loadLibrary("ecgAlgorithm");
    }

    public native void nativeCreateEcgDataManage();

    public native byte nativeEcgAnaGetLoginStatu();

    public native String nativeEcgAnaGetMd5Code();

    public native void nativeEcgAnaLoginCode(byte[] code);

    public int nativeEcgWrite(short data) {
        short[] out = new short[12];
        nativeEcgWrite(new short[]{data}, out, (short) 0);
        return out[0];
    }

    /**
     * @param data 输入值
     * @param out  输出值
     * @param lead 0 单导 1 12导
     * @return
     */
    public native void nativeEcgWrite(short[] data, short[] out, short lead);// 过滤后的心电值

    public native int[]  nativeGetArrType();

    public native int nativeGetHR();

    public native int nativeGetAnaHR();//获取平均心率 最后10s

    public native void nativeFilterSelect(int mode, int trap);

    public native void nativeAnalysisWave();//静态诊断分析

}