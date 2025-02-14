package com.ruqimobility.precisiontest.utils;

/**
 * @author jeff
 * @title ElasticsearchClient
 * @description
 * @create 2024/5/23 16:43
 **/

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.*;


import java.io.IOException;
import java.util.Objects;

public class ElasticsearchClient {
    private static final String HOSTNAME = "10.2.1.3";

    private static final int PORT = 9200;
    private static final String USERNAME = "read_ro";
    private static final String PASSWORD = "aZd9kW1Xju";

    public RestHighLevelClient get(int retry) throws IOException {
        // 设置验证信息，填写账号及密码
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(USERNAME, PASSWORD));
        // 初始化 RestClient, hostName 和 port 填写集群的内网 VIP 地址与端口
        RestClientBuilder builder = RestClient.builder(new HttpHost(HOSTNAME, PORT, "http"));
        // 设置认证信息
        builder.setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider));
        try {
            RestHighLevelClient newClient = new RestHighLevelClient(builder);
            if (newClient.ping(RequestOptions.DEFAULT)) {
                return newClient;
            } else {
                newClient.close();
                if(retry > 0){
                    return get(retry - 1);
                }
            }
        } catch (IOException e) {
            if (retry > 0) {
                return get(retry - 1);
            }
        }
        throw new IOException("connect es error !");
    }

}
