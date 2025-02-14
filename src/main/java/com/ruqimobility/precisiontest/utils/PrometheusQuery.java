package com.ruqimobility.precisiontest.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruqimobility.precisiontest.constants.PrometheusConstant;
import com.ruqimobility.precisiontest.entity.response.InterfaceOfPromeResp;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.web.servlet.view.script.ScriptTemplateViewResolver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;


public class PrometheusQuery {
    private CloseableHttpClient httpClient;
    public PrometheusQuery(){
        this.httpClient = HttpClients.createDefault();
    }

    public String execute(String promQL) throws IOException {
        if(promQL==null){
            return null;
        }
        promQL = URLEncoder.encode(promQL);
        HttpGet httpGet = new HttpGet(PrometheusConstant.PROM_URL + "?query=" + promQL);
        String auth = String.format("%s:%s", PrometheusConstant.USERNAME, PrometheusConstant.PASSWORD);
        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.UTF_8));
        String authHeader = "Basic " + new String(encodedAuth);
        httpGet.setHeader(HttpHeaders.AUTHORIZATION, authHeader);

        HttpResponse response = httpClient.execute(httpGet);
        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        String line;
        StringBuilder responseContent = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            responseContent.append(line);
        }
        reader.close();

        httpClient.close();
        return responseContent.toString();

    }

    public List<String> rangeQueryMid(String hql,String start,String end,int step) throws IOException {
        List<String> res = new ArrayList<>();
        if(StringUtils.isEmpty(hql)||StringUtils.isEmpty(start)||StringUtils.isEmpty(end)){
            return res;
        }
        String hqlEncode = URLEncoder.encode(hql);
        String request = String.format(PrometheusConstant.TEST_PROM_RANGE_URL, hqlEncode, start, end,step);
        HttpGet httpGet = new HttpGet(request);
        String auth = String.format("%s:%s", PrometheusConstant.TEST_USERNAME, PrometheusConstant.TEST_PASSWORD);
        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.UTF_8));
        String authHeader = "Basic " + new String(encodedAuth);
        httpGet.setHeader(HttpHeaders.AUTHORIZATION, authHeader);
        HttpResponse response = httpClient.execute(httpGet);
        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        String line;
        StringBuilder responseContent = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            responseContent.append(line);
        }
        reader.close();
        String promRes = String.valueOf(responseContent);
        //如果超出打点限制
        if(promRes.contains("maximum")){
            step += 1;
            if(step<10){
                rangeQueryMid(hql,start,end,step);
            }
        }else {
            JSONObject jsonObject = JSONObject.parseObject(promRes);
            if (jsonObject.containsKey("data") && promRes.contains("uri")) {
                JSONObject data = jsonObject.getJSONObject("data");
                JSONArray result = data.getJSONArray("result");
                result.stream().forEach(t -> {
                    JSONObject t1 = (JSONObject) t;
                    String url = t1.getJSONObject("metric").getString("uri");
                    res.add(url);
                });
            }
        }
        return res;
    }

    public List<String> rangeQuery(String hql,String start,String end,int step) throws IOException {
        List<String> res = new ArrayList<>();
        if(StringUtils.isEmpty(hql)||StringUtils.isEmpty(start)||StringUtils.isEmpty(end)){
            return res;
        }
        String hqlEncode = URLEncoder.encode(hql);
        String request = String.format(PrometheusConstant.TEST_PROM_RANGE_URL, hqlEncode, start, end, step);
        HttpGet httpGet = new HttpGet(request);
        String auth = String.format("%s:%s", PrometheusConstant.TEST_USERNAME, PrometheusConstant.TEST_PASSWORD);
        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.UTF_8));
        String authHeader = "Basic " + new String(encodedAuth);
        httpGet.setHeader(HttpHeaders.AUTHORIZATION, authHeader);
        HttpResponse response = httpClient.execute(httpGet);
        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        String line;
        StringBuilder responseContent = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            responseContent.append(line);
        }
        reader.close();
        String promRes = String.valueOf(responseContent);
        //如果超出打点限制
        if(promRes.contains("maximum")){
            step += 1;
            if(step<10){
                rangeQuery(hql,start,end,step);
            }
        }else {
            JSONObject jsonObject = JSONObject.parseObject(promRes);
            if (jsonObject.containsKey("data") && promRes.contains("url")) {
                JSONObject data = jsonObject.getJSONObject("data");
                JSONArray result = data.getJSONArray("result");
                result.stream().forEach(t -> {
                    JSONObject t1 = (JSONObject) t;
                    String url = t1.getJSONObject("metric").getString("url");
                    res.add(url);
                });
            }
        }
        return res;

    }

    public static void main(String[] args) throws IOException {
//        String hql = "sum by (url)(url_in_status_total{application=~\"travel-open-proxy|travel-open-proxy-estimate-price\",url=~\"/etravel/gd/etravel/order/event/notify\"}>0)";
        String start = "2020-05-20T00:10:51.781Z";
        String end = "2024-05-21T20:10:51.781Z";
        String hql = "sum by (uri)({uri =~ \"/qiying/activity/product/query/list\"}>0)";
        PrometheusQuery prometheusQuery = new PrometheusQuery();
        List<String> strings = prometheusQuery.rangeQueryMid(hql, start, end,1);
        for (String s:
             strings) {
            System.out.println(s);
        }
    }
    /**
     * @isMultiModule true 本身存在多模块情况，false 不存在多模块情况
     * */
    public InterfaceOfPromeResp extractField(String res,Boolean isMultiModule) throws IOException {
        InterfaceOfPromeResp interfaceOfPromeResp = new InterfaceOfPromeResp();
        // 创建 ObjectMapper 对象
        ObjectMapper objectMapper = new ObjectMapper();
        // 将字符串转换为 JSON 格式
        JsonNode rootNode = objectMapper.readTree(res);
        JsonNode resultNode = rootNode.get("data").get("result");
        //查询结果为空,则返回空对象。
        if(resultNode.size()==0){
            return interfaceOfPromeResp;
        }
        List<String> appNames = new ArrayList<>();
        List<String> dayCounts = new ArrayList<>();
        Set<String> calledHostSet = new HashSet<>();
        int frontGateway = 0;
        for (JsonNode resultItem : resultNode) {
            JsonNode metricNode = resultItem.get("metric");
            String appName = metricNode.get("application").asText();
            JsonNode valueNode = resultItem.get("value");
            if (valueNode != null && valueNode.isArray() && valueNode.size() > 1) {
                int anInt = valueNode.get(1).asInt();
                dayCounts.add(String.valueOf(anInt));
            }
            String calledHost = metricNode.get("calledHost").asText();
            calledHostSet.add(calledHost);
            appNames.add(appName);
            //判断上游有无gateway
            if(appName.contains("gateway")){
                frontGateway=1;
            }
        }
        if(isMultiModule || (!isMultiModule&&calledHostSet.size() == 1)){
            String joinApps = String.join(";", appNames);
            interfaceOfPromeResp.setFrontApps(joinApps);
            interfaceOfPromeResp.setFrontGateway(frontGateway);
            interfaceOfPromeResp.setDayCount(String.join(";", dayCounts));
        }
        return interfaceOfPromeResp;
    }

}
