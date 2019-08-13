package com.asiainfo.common.pre.cs;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author guyu
 *
 * wave file
 */
public class WaveFile {

    public static final int WAVE_FORMAT_PCM = 1;

    /**
     * File Header
     * 12字节
     */
    class RiffHeader {
        final int len = 4+4+4;
        byte[] riffHeader;

        /**
         * 构造函数
         */
        RiffHeader() {
            riffHeader = new byte[len];
            // 设置初始值
            riff("RIFF");
            rifftype("WAVE");
        }

        /**
         * 设置riff,4字节
         * @param s
         */
        void riff(String s) {
            System.arraycopy(s.getBytes(), 0, riffHeader, 0, 4);
        }

        /**
         * 设置文件大小,4字节
         * @param fs
         */
        void filesize(int fs) {
            System.arraycopy(ConvtUtil.intToBit32(fs), 0, riffHeader, 4, 4);
        }

        /**
         * 设置rifftype,4字节
         * @param s
         */
        void rifftype(String s) {
            System.arraycopy(s.getBytes(), 0, riffHeader, 8, 4);
        }
    }

    /**
     * 块信息
     * 8字节
     */
    class Chunk {
        final int len = 4+4;
        byte[] chunk;

        /**
         * 构造函数
         */
        Chunk() {
            chunk = new byte[len];
        }

        /**
         * 设置chunk id,4字节
         * @param s
         */
        void chunk_id(String s) {
            System.arraycopy(s.getBytes(), 0, chunk, 0, 4);
        }

        /**
         * 设置块大小,4字节
         * @param
         */
        void chunksize(int cs) {
            System.arraycopy(ConvtUtil.intToBit32(cs), 0, chunk, 4, 4);
        }
    }

    /**
     * Wave Format
     * 14字节
     */
    class WaveFormat {
        final int len = 2+2+4+4+2;
        byte[] wavFmt;

        /**
         * 构造函数
         */
        WaveFormat() {
            wavFmt = new byte[len];

            // 设置初始值
            formatTag(WAVE_FORMAT_PCM);
        }

        /**
         * 设置格式标签,2字节
         * @param ft
         */
        void formatTag(int ft) {
            System.arraycopy(ConvtUtil.intToBit16(ft), 0, wavFmt, 0, 2);
        }

        /**
         * 设置通道号,2字节
         * @param chnl
         */
        void channels(int chnl) {
            System.arraycopy(ConvtUtil.intToBit16(chnl), 0, wavFmt, 2, 2);
        }

        /**
         * 设置采样率,4字节
         * @param rate
         */
        void samplesPreSec(int rate) {
            System.arraycopy(ConvtUtil.intToBit32(rate), 0, wavFmt, 4, 4);
        }

        /**
         * 设置每秒字节数,4字节
         * @param rate
         */
        void avgBytesPreSec(int rate) {
            System.arraycopy(ConvtUtil.intToBit32(rate), 0, wavFmt, 8, 4);
        }

        /**
         * 块对齐数,2字节
         * @param ba
         */
        void blockAlign(int ba) {
            System.arraycopy(ConvtUtil.intToBit16(ba), 0, wavFmt, 12, 2);
        }
    }

    /**
     * pcm wave format
     * sizeof WaveFormat + 2字节
     */
    class PCMWAVEFORMAT {
        WaveFormat wf;
        byte[] bitsPerSample;

        /**
         * 构造函数
         */
        PCMWAVEFORMAT() {
            wf = new WaveFormat();
            bitsPerSample = null;
        }

        /**
         * 设置每次采样的bit数
         * @param bps
         */
        void bitsPerSample(int bps) {
            bitsPerSample = ConvtUtil.intToBit16(bps);
        }
    }

    // 字节流结构体定义
    RiffHeader header;
    Chunk ch_format;
    PCMWAVEFORMAT format;
    Chunk ch_data;
    // data follows here,after ch_data
    byte[] data;

    // 临时参量,用于计算length
    int _samplespersec;
    int _channels;
    int _chunksize;

    /**
     * 无参构造函数
     */
    private WaveFile() {
        super();

        // 初始化
        header = new RiffHeader();
        ch_format = new Chunk();
        format = new PCMWAVEFORMAT();
        ch_data = new Chunk();
        data = null;

        ch_format.chunk_id("fmt ");
        ch_format.chunksize(16);
        ch_data.chunk_id("data");
    }

    /**
     * 构造函数
     * @param channels
     * @param samplerate
     * @param datasize
     * @param totalsamples
     */
    public WaveFile(int channels,
                    int samplerate,
                    int datasize,
                    int totalsamples) {
        this();

        // 根据参数设置
        format.wf.channels(channels);
        format.wf.samplesPreSec(samplerate);

        int ba = datasize * channels / 8;
        format.wf.blockAlign(ba);
        format.wf.avgBytesPreSec(samplerate * ba);
        format.bitsPerSample(datasize);

        int cs = totalsamples * ba;
        ch_data.chunksize(cs);
        header.filesize(cs + 36);

        // 为临时参量赋值
        _samplespersec = samplerate;
        _channels = channels;
        _chunksize = cs;
    }

    /**
     * 构造函数
     * @param samplerate
     * @param totalsamples
     */
    public WaveFile(int samplerate, int totalsamples) {
        this(1, samplerate, 8, totalsamples);
    }

    /**
     * Return wave length in secs.
     * @return
     */
    public float length() {
        float len = (_chunksize / this._channels) / (float)_samplespersec;

        return len;
    }

    /**
     * 从输入流中读取wav数据
     * @param is
     * @throws IOException
     */
    public static WaveFile read(InputStream is) throws IOException {
        WaveFile oWf = new WaveFile();

        is.read(oWf.header.riffHeader);
        is.read(oWf.ch_format.chunk);
        is.read(oWf.format.wf.wavFmt);
        is.read(oWf.format.bitsPerSample);
        is.read(oWf.ch_data.chunk);

        byte[] bs = new byte[4];
        System.arraycopy(oWf.ch_data.chunk, 4, bs, 0, 4);
        oWf.data = new byte[ConvtUtil.bit32ToInt(bs)];
        is.read(oWf.data);

        return oWf;
    }

    /**
     * 写文件头
     * @param os
     * @throws IOException
     */
    public void writeHeader(OutputStream os) throws IOException {
        os.write(this.header.riffHeader);
        os.write(this.ch_format.chunk);
        os.write(this.format.wf.wavFmt);
        os.write(this.format.bitsPerSample);
        os.write(this.ch_data.chunk);
    }

    /**
     * 写入音频数据
     * @param os
     * @param data
     * @throws IOException
     */
    public void writeData(OutputStream os, byte[] data) throws IOException {
        os.write(data);
    }

    /**
     * 写入音频数据
     * @param os
     * @param data
     * @param off
     * @param len
     * @throws IOException
     */
    public void writeData(OutputStream os, byte[] data, int off, int len) throws IOException {
        os.write(data, off, len);
    }

    /**
     * 将wav数据写入到输入流
     * @param os		output stream
     * @param data		raw pcm data
     * @param off		data offset
     * @param len		length
     * @param writeHeader	是否写入文件头
     * @throws IOException
     */
    public void write(OutputStream os, byte[] data, int off, int len, boolean writeHeader) throws IOException {
        this.data = data;

        // 写入文件头
        if (writeHeader) {
            writeHeader(os);
        }
        os.write(this.data, off, len);
    }

}
