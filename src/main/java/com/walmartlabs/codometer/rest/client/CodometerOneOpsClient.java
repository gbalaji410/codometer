package com.walmartlabs.codometer.rest.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.walmartlabs.codometer.model.Computes;

public class CodometerOneOpsClient {

	private static final String ONEOPS_URL = "https://oneops.prod.walmart.com/{organization}/assemblies/{assembly}/operations/environments/{environment}/platforms/{activePlatformName}/components/compute/instances?instances_state=all";
	
	private static final String GET_PLATFORM_SERVICE_URL = "https://oneops.prod.walmart.com/{organization}/assemblies/{assembly}/transition/environments/{environment}/platforms.json";
	
	private static final String ORG_NAME = "{organization}";
	
	private static final String ASSEMBLY_NAME = "{assembly}";
	
	private static final String ENV_NAME = "{environment}";
	
	private static final String PLATFORM_NAME = "{activePlatformName}";
	
	private static final String TOMCAT_PACK = "tomcat";

        public List<String> getPlatformList(String orgName, String assemblyName, String envName, String token)
                throws ClientProtocolException, IOException {
        
            List<String> platformList = new ArrayList<String>();

            HttpClient client = HttpClientBuilder.create().build();
    
            String url = GET_PLATFORM_SERVICE_URL.replace(ORG_NAME, orgName).replace(ASSEMBLY_NAME, assemblyName)
                            .replace(ENV_NAME, envName);
    
            HttpGet req = new HttpGet(url);
            
            String authtoken = "Basic " + token;
    
            // add request header
            req.addHeader("Content-Type", "application/json");
            req.addHeader("Accept", "application/json");
            req.addHeader("Authorization",authtoken);
    
            HttpResponse response = client.execute(req);
    
            System.out.println("Response Code : " + response.getStatusLine().getStatusCode());
    
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
    
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                    result.append(line);
            }
            
            platformList = getPlatformsByPackFromResult(result.toString(), TOMCAT_PACK);
            
            return platformList;
        }
	
	public Computes getComputeList(String orgName, String assemblyName, String envName, String platform, String token)
			throws ClientProtocolException, IOException {

		HttpClient client = HttpClientBuilder.create().build();
		
		String url = ONEOPS_URL.replace(ORG_NAME, orgName).replace(ASSEMBLY_NAME, assemblyName)
		        .replace(ENV_NAME, envName).replace(PLATFORM_NAME, platform);
		
		HttpGet req = new HttpGet(url);
		
		String authtoken = "Basic " + token;

		// add request header
		req.addHeader("Content-Type", "application/json");
		req.addHeader("Accept", "application/json");
		req.addHeader("Authorization",authtoken);

		HttpResponse response = client.execute(req);

		System.out.println("Response Code : " + response.getStatusLine().getStatusCode());

		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
		
		Computes computes = new Computes();
		computes.setComputes(getComputesFromResult(result.toString()));
		
		return computes;
	}

	public static List<String> getComputesFromResult(String jsonData) {

		List<String> computes = new ArrayList<String>();

		JSONParser jp = new JSONParser();
		try {
			Object obj = jp.parse(jsonData);
			if (obj != null) {
				if (obj instanceof JSONArray) {
					JSONArray array = (JSONArray) obj;
					Iterator<?> it = array.iterator();
					while (it.hasNext()) {
						Object o = it.next();
						JSONObject server = (JSONObject) o;
						JSONObject ciAttributes = (JSONObject)server.get("ciAttributes");
						computes.add(ciAttributes.get("public_ip").toString());
					}

				} else {
					return null;
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return computes;
	}
	
        public static List<String> getPlatformsByPackFromResult(String jsonData, String packName) {

            List<String> platforms = new ArrayList<String>();

            JSONParser jp = new JSONParser();
            try {
                    Object obj = jp.parse(jsonData);
                    if (obj != null) {
                            if (obj instanceof JSONArray) {
                                    JSONArray array = (JSONArray) obj;
                                    Iterator<?> it = array.iterator();
                                    while (it.hasNext()) {
                                            Object o = it.next();
                                            JSONObject server = (JSONObject) o;
                                            JSONObject ciAttributes = (JSONObject)server.get("ciAttributes");
                                            
                                            String platformPackName = ciAttributes.get("pack").toString();
                                            if (packName.equals(platformPackName)) {
                                                platforms.add(server.get("ciName").toString());
                                            }
                                    }

                            } else {
                                    return null;
                            }

                    }
            } catch (Exception e) {
                    e.printStackTrace();
            }
            return platforms;
    }
	
}
