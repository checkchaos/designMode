/*
 * Created on Jul 19, 2006
 *
 * Copyright (c) 2006 Asiainfo Technologies(China),Inc.
 */
package com.asiainfo.common.pre.cs;

/**
 * @author guyu
 *
 * Adaptive Differential Pulse Code Modulation
 *
 * Description: Routines to convert 12 bit linear samples to the
 * Dialogic or Oki ADPCM coding format.
 * I copied the algorithms out of the book "PC Telephony - The
 * complete guide to designing, building and programming systems
 * using Dialogic and Related Hardware" by Bob Edgar. pg 272-276.
 */
public class Adpcm {

    /**
     * adpcm encode/decode status
     */
    public static class adpcm_status {
        int next_flag;
        int last_signal;
        int ss_index;
        int zero_count;	// counter of consecutive zero samples

        /**
         * 构造函数
         */
        public adpcm_status() {
            init();
        }

        /**
         * 初始化状态
         */
        void init() {
            next_flag = 0;
            last_signal = 0;
            ss_index = 0;
            zero_count = 0;
        }
    }

    // M(L(n)) values, step size index shift table, indsft[8]
    public static final short[/*8*/] M = { -1, -1, -1, -1, 2, 4, 6, 8 };

    /* calculated step sizes, search table, stpsz[i]=floor[16*(11/10)^i] */
    static final short[/*49*/] step_size = { 16, 17, 19, 21, 23, 25, 28, 31, 34, 37, 41,
            45, 50, 55, 60, 66, 73, 80, 88, 97, 107, 118, 130, 143, 157, 173,
            190, 209, 230, 253, 279, 307, 337, 371, 408, 449, 494, 544, 598, 658,
            724, 796, 876, 963, 1060, 1166, 1282, 1411, 1552 };

    /* nibble to bit map */
    static final int[/*8*/][/*3*/] nbl2bit = { {0,0,0}, {0,0,1}, {0,1,0}, {0,1,1}, {1,0,0},
            {1,0,1}, {1,1,0}, {1,1,1}
    };

    /* sign table */
    static final int signs[/*2*/] = { 1, -1 };

    /**
     * 私有构造函数
     */
    private Adpcm() {
        super();
    }

    /**
     * 获取short的高位字节
     * @param sv
     * @return
     */
    public static final byte hi_byte (short sv) {
        return (byte)( (sv >> 8) & 0x00FF );
    }

    /**
     * 获取short的低位字节
     * @param sv
     * @return
     */
    public static final byte lo_byte (short sv) {
        return (byte)( sv & 0x00FF);
    }

    /**
     * 状态初始化,Initialze the data used by the coder.
     * @param stat
     */
    public static void adpcm_init(adpcm_status stat) {
        stat.init();
        return;
    }

    /**
     * Decode Linear to ADPCM
     * @param encoded
     * @param stat
     * @return
     */
    public static short adpcm_decode( byte encoded, adpcm_status stat ) {
        int sign, E, SS, diff;

        // System.out.println("encoded ==> " + encoded);
        sign = signs[encoded >> 3];
        encoded &= 0x07;

        SS = step_size[stat.ss_index];
        E = SS*nbl2bit[encoded][0] + SS/2*nbl2bit[encoded][1] + SS/4*nbl2bit[encoded][2] + SS/8/*SS>>3*/;
        if (0 != ((encoded >> 1) & SS & 0x1))
            E++;
        diff = sign * E;

        if ((stat.next_flag & 0x01) != 0)
            stat.last_signal -= 8;
        else if ((stat.next_flag & 0x02) != 0)
            stat.last_signal += 8;

        stat.last_signal += diff;

        /*
         *  Clip the values to +/- 2^11 (supposed to be 12 bits)
         */
        if( stat.last_signal > 2047 ) stat.last_signal = 2047;
        if( stat.last_signal < -2047 ) stat.last_signal = -2047;

        stat.next_flag = 0;

        boolean autoReturn = true;
        if (autoReturn) {
            if (0 != encoded)
                stat.zero_count = 0;
            else if (++stat.zero_count == 24) {
                stat.zero_count = 0;
                if (stat.last_signal > 0)
                    stat.next_flag = 0x01;
                else if (stat.last_signal < 0)
                    stat.next_flag = 0x02;
            }
        }

        stat.ss_index += step_adjust( encoded );
        if( stat.ss_index < 0 ) stat.ss_index = 0;
        if( stat.ss_index > 48 ) stat.ss_index = 48;

        // System.out.println("sample ==> " + stat.last_signal);
        return (short)( stat.last_signal );
    }

    /**
     * adjust the step for use on the next sample.
     * @param code
     * @return
     */
    public static short step_adjust ( byte code ) {
        return M[code & 0x07];
    }
}
