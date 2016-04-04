package com.walmartlabs.codometer.manager;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
public class JenkinsManager {

    private RestTemplate rest;
    
    @Autowired
    private CodometerCCMConfig config;
    
    public JenkinsManager() {
        init();
    }
    
    @PostConstruct
    public void init() {
        rest = new RestTemplate();
        
        HttpMessageConverter<?> formHttpMessageConverter = new FormHttpMessageConverter();
        HttpMessageConverter<?> stringHttpMessageConverternew = new StringHttpMessageConverter();
        List<HttpMessageConverter<?>> converters = new ArrayList<HttpMessageConverter<?>>();
        converters.add(formHttpMessageConverter);
        converters.add(stringHttpMessageConverternew);
        rest.setMessageConverters(converters);        
    }
    
    public void triggerBuild(String gitUrl, String branchName, String hosts) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add(Constants.REQUEST_PARAM_PROJECT_GIT_URL, gitUrl);
        map.add(Constants.REQUEST_PARAM_PROJECT_GIT_BRANCH, branchName);
        map.add(Constants.REQUEST_PARAM_CODE_COVERAGE_HOSTS, hosts);
        
        String url = config.getJenkinsUrl() + "/job/" + config.getCodometerJenkinsJobName() + "/buildWithParameters";
        String result = rest.postForObject(url, map, String.class);
        System.out.println(result);        
    }

    public CodometerCCMConfig getConfig() {
        return config;
    }

    public void setConfig(CodometerCCMConfig config) {
        this.config = config;
    }
}
