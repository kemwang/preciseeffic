package com.ruqimobility.precisiontest.utils;

import org.mountcloud.graphql.GraphqlClient;
import org.mountcloud.graphql.request.mutation.GraphqlMutation;
import org.mountcloud.graphql.request.query.GraphqlQuery;
import org.mountcloud.graphql.response.GraphqlResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GraphQLClientUtil {

    private static String Cookie = "pamirs_uc_session_id=c3cf72d35db24bba9";
    private static String ServerUrl = "http://localhost:88";
    private static String CONTENT_TYPE = "application/json";
    public static GraphqlResponse GraphqlQueryClient(GraphqlQuery query) throws IOException {

        //创建客户端
        GraphqlClient graphqlClient = GraphqlClient.buildGraphqlClient(ServerUrl);

        //用map来进行传递
        Map<String,String> httpHeaders = new HashMap<>();
        httpHeaders.put("Content-Type",CONTENT_TYPE);
        httpHeaders.put("Cookie",Cookie);
        //设置http请求的头
        graphqlClient.setHttpHeaders(httpHeaders);

        //执行查询
        GraphqlResponse graphqlResponse = graphqlClient.doQuery(query);
        return graphqlResponse;
    }

    public static GraphqlResponse GraphqlMutationClient(GraphqlMutation mutation) throws IOException {

        //创建客户端
        GraphqlClient graphqlClient = GraphqlClient.buildGraphqlClient(ServerUrl);

        //用map来进行传递
        Map<String,String> httpHeaders = new HashMap<>();
        httpHeaders.put("Content-Type",CONTENT_TYPE);
        httpHeaders.put("Cookie",Cookie);
        //设置http请求的头
        graphqlClient.setHttpHeaders(httpHeaders);

        //执行查询
        GraphqlResponse graphqlResponse = graphqlClient.doMutation(mutation);
        return graphqlResponse;
    }
}
