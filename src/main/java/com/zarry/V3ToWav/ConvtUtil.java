/*
 * Created on Jul 22, 2005
 *
 * Copyright (c) 2005 Asiainfo Technologies(China),Inc.
 */
package com.asiainfo.common.pre.cs;

import java.util.HashMap;

/**
 * @author guyu
 * <p>
 * 字节数组转换成整数
 */
public final class ConvtUtil {

    /**
     * 单字节转换为整数
     *
     * @param b
     * @return
     */
    public static int bit8ToInt(byte b) {
        int integer = 0;
        integer |= (b & 0xFF);

        return integer;
    }

    /**
     * 整数转换成单字节
     *
     * @param integer
     * @return
     */
    public static byte intToBit8(int integer) {
        return (byte) (integer & 0x000000FF);
    }

    /**
     * 双字节转换成整数
     *
     * @param bytes
     * @return
     */
    public static int bit16ToInt(byte[] bytes) {
        int i1 = bit8ToInt(bytes[0]);
        int i2 = bit8ToInt(bytes[1]);

        return i1 | i2 << 8;
    }

    /**
     * 整数转换成双字节
     *
     * @param integer
     * @return
     */
    public static byte[] intToBit16(int integer) {
        byte[] bs = new byte[2];

        bs[0] = intToBit8(integer);
        bs[1] = intToBit8(integer >> 8);

        return bs;
    }

    /**
     * 三字节转换成整数
     *
     * @param bytes
     * @return
     */
    public static int bit24ToInt(byte[] bytes) {
        int i1 = bit8ToInt(bytes[0]);
        int i2 = bit8ToInt(bytes[1]);
        int i3 = bit8ToInt(bytes[2]);

        return i1 | i2 << 8 | i3 << 16;
    }

    /**
     * 四字节转换成整数
     *
     * @param bytes
     * @return
     */
    public static int bit32ToInt(byte[] bytes) {
        int i1 = bit8ToInt(bytes[0]);
        int i2 = bit8ToInt(bytes[1]);
        int i3 = bit8ToInt(bytes[2]);
        int i4 = bit8ToInt(bytes[4]);

        return i1 | i2 << 8 | i3 << 16 | i4 << 24;
    }

    /**
     * 整数转换成四字节
     *
     * @param integer
     * @return
     */
    public static byte[] intToBit32(int integer) {
        byte[] bs = new byte[4];

        bs[0] = intToBit8(integer);
        bs[1] = intToBit8(integer >> 8);
        bs[2] = intToBit8(integer >> 16);
        bs[3] = intToBit8(integer >> 24);

        return bs;
    }

    /**
     * 交换双字节
     *
     * @param a
     * @return
     */
    public static int swapBytes16(short a) {
        return ((a >> 8) & 0xFF) + ((a << 8) & 0xFF00);
    }

    public static boolean equal(byte[] bs, int len, String signature) {
        byte[] sigs = signature.getBytes();
        if (bs.length < len || sigs.length != len)
            return false;

        for (int i = 0; i < len; i++) {
            if (bs[i] != sigs[i])
                return false;
        }
        return true;
    }

    /**
     * @param para
     * @param sql
     * @param map
     * @return
     * @throws Exception
     * @version: 1.0
     * @description: 处理包含In的sql
     * @author:lujun3
     * @date:2012-6-26 上午10:42:18
     */
    public static StringBuffer dealWithIn(Object[] para, StringBuffer sql, HashMap<String, Object> map, String sqlStr) throws Exception {
        StringBuffer sqlBuffer = null;
        if (sqlStr != null) {
            sqlBuffer = new StringBuffer(sqlStr);
        } else {
            sqlBuffer = sql;
        }
        StringBuffer myMapStr = new StringBuffer();
        sqlBuffer.append(" ( ");
        if (para != null) {
            for (int index = 0; index < para.length; index++) {
                if (index < para.length - 1) {
                    sqlBuffer.append(" :param_in").append(index).append(" ,");
                } else {
                    sqlBuffer.append(" :param_in").append(index).append(" )");
                }
                myMapStr.append("param_in").append(index);
                map.put(myMapStr.toString(), para[index]);
                myMapStr.delete(0, myMapStr.length());
            }
        } else {
            throw new Exception("dealWithIn 参数：Object[] para 不能为空");
        }
        return sqlBuffer;
    }

    public static void main(String[] args) {
        Object obj = "";
        Object[] para = null;
        HashMap<String, Object> map = new HashMap<String, Object>();
        try {
            System.out.println(ConvtUtil.dealWithIn(para, null, map, "select * from ww where s in "));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
