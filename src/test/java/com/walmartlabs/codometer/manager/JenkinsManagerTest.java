package com.walmartlabs.codometer.manager;

import org.testng.annotations.Test;


public class JenkinsManagerTest {

    @Test
    public void testJenkinsManager() {
        JenkinsManager jenkinsManager = new JenkinsManager();
        
        CodometerCCMConfig config = new CodometerCCMConfig();
        config.setCodometerJenkinsJobName("CodeCoverageJob");
        config.setJenkinsUrl("http://localhost:8080");
        jenkinsManager.setConfig(config);;
        
        String gitUrl = "https://gecgithub01.walmart.com/FOCI/foci-services.git"; 
        String branchName = "dev";
        String hosts = "localhost:6300";
        jenkinsManager.triggerBuild(gitUrl, branchName, hosts);
    }
}
