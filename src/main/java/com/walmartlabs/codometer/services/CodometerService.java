package com.walmartlabs.codometer.services;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.walmartlabs.codometer.manager.JenkinsManager;
import com.walmartlabs.codometer.model.Computes;
import com.walmartlabs.codometer.rest.client.CodometerOneOpsClient;

@Path("/codometerservice")
@Produces({ "application/json", "application/xml" })
@Service(value="codometerService")
public class CodometerService {
	
    @Autowired
    private JenkinsManager jenkinsManager;
    
	@GET
	public Response getCoverageData() {
		
		ResponseBuilder builder = Response.ok("Hello");
		builder = Response.status(Status.OK);
		return builder.build();
	}
	
	
	@GET
	@Path("computes")
	@Produces(value="application/json")
	public Response getOneOpsComputeDetails(@QueryParam(value = "orgName") String orgName,
			@QueryParam(value = "assemblyName") String assemblyName,
			@QueryParam(value = "envName") String envName,
			@QueryParam(value = "platform") String platform,
			@QueryParam(value = "token") String token) {
		
		CodometerOneOpsClient client = new CodometerOneOpsClient();
		Computes computes = null;
		
		try {
			computes = client.getComputeList(orgName, assemblyName, envName, platform, token);
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).build();
		}
		
		ResponseBuilder builder = Response.ok(computes);
		return builder.build();
	}

        @POST
        @Path("/triggerBuild")
        @Produces(value="application/json")
        //@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED})
        public Response triggerBuild(@QueryParam(value = "gitUrl") String gitUrl,
                @QueryParam(value = "branchName") String branchName,
                @QueryParam(value = "hosts") String hosts) {
            try {
                jenkinsManager.triggerBuild(gitUrl, branchName, hosts);
            } catch (Exception e) {
                    e.printStackTrace();
                    return Response.status(Status.BAD_REQUEST).build();
            }
            
            ResponseBuilder builder = Response.ok();
            return builder.build();
        }
	
        @POST
        @Path("/triggerBuildOneOps")
        @Produces(value="application/json")
        //@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED})
        public Response triggerBuildOneOps(@QueryParam(value = "gitUrl") String gitUrl,
                @QueryParam(value = "branchName") String branchName,
                @QueryParam(value = "orgName") String orgName,
    			@QueryParam(value = "assemblyName") String assemblyName,
    			@QueryParam(value = "envName") String envName,
    			@QueryParam(value = "platform") String platform,
    			@QueryParam(value = "token") String token) {
            try {
            	Computes computes = null;
        		
        		CodometerOneOpsClient client = new CodometerOneOpsClient();

        		computes = client.getComputeList(orgName, assemblyName, envName, platform, token);
        		
                jenkinsManager.triggerBuild(gitUrl, branchName, StringUtils.join(computes.getComputes(), ","));
            } catch (Exception e) {
                    e.printStackTrace();
                    return Response.status(Status.BAD_REQUEST).build();
            }
            
            ResponseBuilder builder = Response.ok();
            return builder.build();
        }
}
