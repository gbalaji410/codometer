package com.walmartlabs.codometer.manager;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class CodometerCCMConfig {
    
    @Value("${jenkins.url:http://localhost:8080}")
    private String jenkinsUrl;
    
    @Value("${codometer.jenkins.jobName:CodeCoverageJob}")
    private String codometerJenkinsJobName;

    public String getJenkinsUrl() {
        return jenkinsUrl;
    }

    public void setJenkinsUrl(String jenkinsUrl) {
        this.jenkinsUrl = jenkinsUrl;
    }

    public String getCodometerJenkinsJobName() {
        return codometerJenkinsJobName;
    }

    public void setCodometerJenkinsJobName(String codometerJenkinsJobName) {
        this.codometerJenkinsJobName = codometerJenkinsJobName;
    }
}
