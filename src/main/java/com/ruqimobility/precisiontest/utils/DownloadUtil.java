package com.ruqimobility.precisiontest.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;

import com.ruqimobility.precisiontest.entity.dto.DownloadJarDto;
import com.ruqimobility.precisiontest.service.impl.CallGraphServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.BufferedSink;
import okio.Okio;
import okio.Sink;

/**
 * 文件下载工具类（单例模式）
 */
@Component
public class DownloadUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(CallGraphServiceImpl.class);

    @Autowired
    private OkHttpClient okHttpClient;

    public interface OnDownloadListener{

        /**
         * 下载成功之后的文件
         */
        void onDownloadSuccess(DownloadJarDto dto);

        /**
         * 下载进度
         */
        void onDownloading(int progress);

        /**
         * 下载异常信息
         */

        void onDownloadFailed(DownloadJarDto dto,Exception e);
    }

    public void downloadByAsync(final DownloadJarDto dto, final String destFileDir, final String destFileName,
                                final OnDownloadListener listener) {
        Request request = new Request.Builder()
                .url(dto.getUrl())
                .build();
        //异步请求
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // 下载失败监听回调
                listener.onDownloadFailed(dto, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[4096];
                int len = 0;
                FileOutputStream fos = null;

                // 储存下载文件的目录
                File dir = new File(destFileDir);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File file = new File(dir, destFileName);

                try {
                    is = response.body().byteStream();
                    fos = new FileOutputStream(file);
                    int size = 0;
                    long total = response.body().contentLength();
                    int tempProcess = 0;
                    while ((size = is.read(buf)) != -1) {
                        len += size;
                        fos.write(buf, 0, size);
                        int process = (int) Math.floor(((double) len / total) * 100);
                        // 控制台打印文件下载的百分比情况
                        if(process % 10 == 0) {
                            if(tempProcess != process) {
                                listener.onDownloading(process);
                                tempProcess = process;
                            }
                        }
                    }

                    fos.flush();
                    // 下载完成
                    listener.onDownloadSuccess(dto);
                } catch (Exception e) {
                    LOGGER.error("error:{}", e);
                    listener.onDownloadFailed(dto, e);
                } finally {
                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }

            }
        });
    }
}