package com.asiainfo.web.crm.pre.cs;

import com.ai.common.util.FtpUtil;
import com.ailk.common.data.IData;
import com.ailk.common.data.IDataset;
import com.ailk.common.data.impl.DataMap;
import com.ailk.common.data.impl.Pagination;
import com.asiainfo.common.pre.cs.Adpcm;
import com.asiainfo.common.pre.cs.WaveFile;
import com.asiainfo.utils.constants.ParamOutConst;
import com.asiainfo.utils.srvcaller.CenterClient;
import com.asiainfo.utils.web.saas.Page;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.tapestry.IRequestCycle;

import javax.servlet.ServletOutputStream;
import java.io.*;
import java.net.URLDecoder;

@SuppressWarnings("all")
public abstract class CtTele extends Page {
    private static transient Logger log = Logger.getLogger(CtTele.class);
    private static String REPLACE_STR = "/";

    public abstract void setCtTeles(IDataset ctTeles);

    public abstract void setCtTeleCount(long ctTeleCount);

    public abstract void setCtTele(IData ctTele);

    public void init(IRequestCycle cycle) throws Exception {
    }

    public void downRecord(IRequestCycle cycle) throws Exception {
        playRecord(cycle);
    }

    public void playRecord(IRequestCycle cycle) throws Exception {
        DataMap dataMap = new DataMap();
        if (StringUtils.isBlank(this.getData().getString("REC_FILENAME"))) {
            new Exception("============================>>>>> file path fail");
        }
        ServletOutputStream out = null;
        InputStream inputStream = null;
        BufferedInputStream bis = null;
        ByteArrayInputStream bs = null;
        ByteArrayOutputStream outBaos = null;
        FtpUtil ftpUtil = null;
        ByteArrayOutputStream baos = null;
        String workCode = this.getData().getString("WORK_CODE");
        String[] filenames = StringUtils.split(URLDecoder.decode(StringUtils.trim(this.getData().getString("REC_FILENAME"))), "\\");
        if (filenames == null || filenames.length < 6) {
            new Exception("============================>>>>> file path fail or length less zero");
        }
        //"/20190709/cti工号/1108560.V3";
        String fileName = REPLACE_STR + filenames[3] + REPLACE_STR + workCode + REPLACE_STR;
        try {
            log.error("========================= workCode " + workCode + "   fileDir " + fileName);
            ftpUtil = new FtpUtil("DOWN_PLAY_RECORD_PATH");
            log.error("---------------------------->>>>>" + ftpUtil.getBsFtpPath().getRemotePath());
            baos = new ByteArrayOutputStream();
            log.error("======================= down file start");
            changeWorkDir(ftpUtil, ftpUtil.getBsFtpPath().getRemotePath() + fileName);
            ftpUtil.download(filenames[5], baos);
            log.error("==================== down file finish");
            byte[] tempBytes = baos.toByteArray();
            //将v3格式转换成WAV
            bs = new ByteArrayInputStream(tempBytes);
            outBaos = new ByteArrayOutputStream();
            convert(6000, tempBytes.length, bs, outBaos);

            out = getResponse().getOutputStream();
            getResponse().setContentType("application/x-msdownload");
            getResponse().setHeader("Content-Disposition", "attachment;filename=" + fileName + ".wav");
            out.write(outBaos.toByteArray());
            out.flush();
        } catch (Exception e) {
            log.error("playing ftp record err --------------->：" + e);
            new Exception("playing ftp record err --------------->", e);
        } finally {
            try {
                if (ftpUtil != null) {
                    ftpUtil.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
                if (bis != null) {
                    bis.close();
                }
                if (bs != null) {
                    bs.close();
                }
                if (outBaos != null) {
                    outBaos.close();
                }
                if (out != null) {
                    out.close();
                }
                if (baos != null) {
                    baos.close();
                }
            } catch (Exception e) {
                log.error("Exception", e);
            }
        }
    }

    //切换ftp目录
    private void changeWorkDir(FtpUtil ftp, String dir) throws Exception {
        String currDir = ftp.getCurrentWorkingDirectory();
        if (!currDir.equals(dir)) {
            ftp.changeWorkingDirectory(dir);
            log.error("change dir sucess , old ---- " + currDir + "   now   --------" + dir);
        }
    }

    private void convert(int sampleRate, int totalBytes, InputStream is, OutputStream os) throws IOException {
        WaveFile wave = new WaveFile(1, sampleRate, 16, totalBytes * 2);
        wave.writeHeader(os);
        decode(2, 65536, is, os);
    }

    private void decode(int sample_size, int buffer_size, InputStream is, OutputStream os) throws IOException {
        short[] buffer12 = new short[buffer_size];
        byte[] adpcm = new byte[buffer_size / 2];
        Adpcm.adpcm_status coder_stat = new Adpcm.adpcm_status();
        Adpcm.adpcm_init(coder_stat);
        int bytesRead;
        while ((bytesRead = is.read(adpcm)) > 0) {
            int j = 0;
            for (int i = 0; i < bytesRead; i++) {
                buffer12[j++] = Adpcm.adpcm_decode((byte) ((adpcm[i] >> 4) & 0x0F), coder_stat);
                buffer12[j++] = Adpcm.adpcm_decode((byte) (adpcm[i] & 0x0F), coder_stat);
            }
            write12(os, sample_size, buffer12, bytesRead * 2);
        }
    }

    private void write12(OutputStream os, int sample_size, short[] buffer12, int buffer_offset) throws IOException {
        if (0 == (sample_size >> 1)) {
            byte[] buffer8 = new byte[buffer_offset];
            for (int i = 0; i < buffer_offset; i++) {
                buffer12[i] /= 16;    // buffer12[i] >> 4
                buffer8[i] = (byte) (buffer12[i] + 0x80);
            }
            os.write(buffer8);
        } else {
            byte[] buffer16 = new byte[buffer_offset * 2];
            for (int i = 0; i < buffer_offset; i++) {
                buffer12[i] *= 16;    // buffer12[i] << 4
                buffer16[i * 2] = Adpcm.lo_byte(buffer12[i]);    // 低字节在前
                buffer16[i * 2 + 1] = Adpcm.hi_byte(buffer12[i]);    // 高字节在后
            }
            os.write(buffer16);
        }
    }

    public void queryCtTeles(IRequestCycle cycle) throws Exception {
        DataMap dataMap = new DataMap();
        if (StringUtils.isNotBlank(this.getData().getString("OP_ID"))) {
            dataMap.put("OP_ID", this.getData().getString("OP_ID"));
        }
        if (StringUtils.isNotBlank(this.getData().getString("START_TIME"))) {
            dataMap.put("START_TIME", this.getData().getString("START_TIME"));
        }
        if (StringUtils.isNotBlank(this.getData().getString("END_TIME"))) {
            dataMap.put("END_TIME", this.getData().getString("END_TIME"));
        }
        if (StringUtils.isNotBlank(this.getData().getString("PLATFORM_NO"))) {
            dataMap.put("PLATFORM_NO", this.getData().getString("PLATFORM_NO"));
        }
        if (StringUtils.isNotBlank(this.getData().getString("CALLER_NO"))) {
            dataMap.put("CALLER_NO", this.getData().getString("CALLER_NO"));
        }
        if (StringUtils.isNotBlank(this.getData().getString("CALLED_NO"))) {
            dataMap.put("CALLED_NO", this.getData().getString("CALLED_NO"));
        }
        queryCtTeles(dataMap);
    }

    private void queryCtTeles(IData param) {
        Pagination pagination = getPagination("pageNavbar");
        int currentPage = pagination.getCurrent();
        int pageSize = pagination.getPageSize();
        int startIndex = (Integer.valueOf(currentPage) - 1) * (Integer.valueOf(pageSize)) + 1;
        int endIndex = (Integer.valueOf(currentPage)) * (Integer.valueOf(pageSize));
        param.put("START_INDEX", startIndex);
        param.put("END_INDEX", endIndex);
        IData resultsMap = CenterClient.call("pre_IBsPrePhoneCSV_queryCtTeles", param);
        IDataset results = resultsMap.getDataset(ParamOutConst.resultList);
        int count = 0;
        if (null != resultsMap) {
            count = resultsMap.getInt(ParamOutConst.totalCount);
        }
        setCtTeles(results);
        setCtTeleCount(count);
    }

}
