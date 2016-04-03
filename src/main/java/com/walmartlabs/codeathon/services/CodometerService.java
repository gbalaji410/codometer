package com.walmartlabs.codeathon.services;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.springframework.stereotype.Service;

@Path("/codometer")
@Produces({ "application/json", "application/xml" })
@Service(value="codometerService")
public class CodometerService {
	
	@GET
	public Response getCoverageData() {
		
		ResponseBuilder builder = Response.ok("Hello");
		builder = Response.status(Status.OK);
		return builder.build();
	}
}
