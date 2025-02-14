package com.ruqimobility.precisiontest.utils;

import com.plexpt.chatgpt.ChatGPT;
import com.plexpt.chatgpt.entity.chat.ChatChoice;
import com.plexpt.chatgpt.entity.chat.ChatCompletion;
import com.plexpt.chatgpt.entity.chat.ChatCompletionResponse;
import com.plexpt.chatgpt.entity.chat.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lj
 * @title AIUtil
 * @description
 * @create 2023/12/12 15:56
 **/
public class AIUtil {
    public static ChatGPT chatGPT;
    public static String prompt = "请你扮演IT专家，阅读以下springboot代码：\n%s\n;片段二、%s\n.请你根据以上的接口方法参数，转换成接口对应的JSON格式请求参数，直接输出JSON内容，不要其他的解释、说明!";
    static {
        chatGPT = ChatGPT.builder()
                .apiKey("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")
                .timeout(900)
                .apiHost("https://service-bbdwydru-1302250574.usw.apigw.tencentcs.com/") //代理地址
                .build()
                .init();
            }

    public static String askGPT(String reqPrompt){
        Message reqMessage = Message.of(reqPrompt);
        List<Message> messages = new ArrayList<>();
        messages.add(reqMessage);
        ChatCompletion chatCompletion = ChatCompletion.builder()
                .model("gpt-3.5-turbo-16k-0613")
                .messages(messages)
                .temperature(0.1)
                .build();
        ChatCompletionResponse response = chatGPT.chatCompletion(chatCompletion);
        ChatChoice choice = response.getChoices().get(0);
        Message resMessage = choice.getMessage();
        return resMessage.getContent();
    }

    public static void main(String[] args) {
        AIUtil.askGPT("请你扮演IT专家，阅读以下springboot代码：/**\n" +
                " * 过滤广告组配置\n" +
                " *\n" +
                " * @param appAdConfigGroupList 广告组配置\n" +
                " * @param appAdConfigType      广告类型(AppAdConfigTypeEnum)\n" +
                " * @param module               模块(AdModuleEnum)\n" +
                " * @param clientType           客户端(AdPlatformEnum)\n" +
                " */\n" +
                "public AppAdConfigGroup filerAppAdConfigGroup(List<AppAdConfigGroup> appAdConfigGroupList, Integer appAdConfigType, String module, String clientType){\n" +
                "    return appAdConfigGroupList.stream().filter(p -> appAdConfigType == p.getAppAdConfigType().intValue()).filter(p -> Arrays.stream(p.getClientTypeList().split(\",\")).anyMatch(t -> t.equals(String.valueOf(clientType)))).filter(p -> Arrays.stream(p.getModuleList().split(\",\")).anyMatch(t -> t.equals(String.valueOf(module)))).findFirst().orElse(null);\n" +
                "}" +
                "请你根据以上的接口方法参数，转换成接口对应的JSON格式请求参数，直接输出JSON内容，不要其他的解释、说明");
    }
}
