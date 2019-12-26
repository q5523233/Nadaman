package com.sm.nadaman.common.ecg;

/**
 * 处理每一帧
 */

public class Ecg2DataFrame {
    int state;
    public int dataLength, dataSum, dataIndex, type;
    public int[] data;

    public Ecg2DataFrame() {
        state = 0;
        data = new int[15];
    }



    //
    public int appendByte(byte b, byte[] values) {

        int value = b & 0xFF;
        char checkok = 0;
        switch (state) {
            //StateSuccess
            case 0://头部
                if (value == 0xFA) {
                    state = 3;
                }
                break;
            case 3: //length
                dataLength = value - 1;
                if (dataLength < 1) {
                    state = 0;
                } else {
                    data = new int[dataLength];
                    dataSum = 0;
                    dataIndex = 0;
                    state++;
                }
                break;
            case 4://DataType 数据类型
                type = value;
                state++;
                dataSum += value;
//                if (type!=18){
//                    Log.e("--", "appendByte: ");
//                }
                break;
            case 5://真实的数据内容
                if (dataIndex < dataLength) {
//                  CommonUtils.log("开始  dataIndex====="+dataIndex);
                    data[dataIndex] = value;
                    dataIndex++;
                    dataSum += value;
                    if (dataIndex == dataLength) {
                        state++;
                    }
//                    CommonUtils.log("结束 dataIndex====="+dataIndex);
                }
                break;
            case 6://校验位
                if (dataSum % 256 == (value & 0xFF)) {
                    state = 0;
                    checkok = 1;
                } else {
                    checkok = 1;
                    state = 0;
                }

                break;
        }
        return checkok;
    }


}
