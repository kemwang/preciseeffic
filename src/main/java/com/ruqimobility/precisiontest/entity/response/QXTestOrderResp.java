package com.ruqimobility.precisiontest.entity.response;

import lombok.Data;

import java.util.List;

@Data
public class QXTestOrderResp {
    private Long id;
    private String code;
    private String status;
    private String type;
    private List<Long> testerIds;
    private Long createdBy;
    private Long tapdWorkspaceId;
    private List<String> tapdStoryIds;
    private List<String> tapdBugIds;
    private String operationType;
    private List<FrontendTestApplication> frontendTestApplications;
    private List<TestApplication> testApplications;

    @Data
    public class TestApplication{
        private Long gitlabProjectId;
        private String nacosServiceName;
        private String developBranch;
        private String deployBranch;
        private Boolean existRelyApplication;
        private Boolean existCloudConfig;
        private Boolean existSql;
    }

    @Data
    public class FrontendTestApplication {
        private String applicationName;
        private List<Long> developerIds;
        private List<Long> reviewerIds;
    }
}
