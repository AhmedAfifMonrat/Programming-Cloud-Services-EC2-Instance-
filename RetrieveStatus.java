package com.aws.munrat;

import java.util.List;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder;

import com.amazonaws.services.ec2.model.DescribeInstanceStatusRequest;
import com.amazonaws.services.ec2.model.DescribeInstanceStatusResult;


public class RetrieveStatus {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/*
         * The ProfileCredentialsProvider will return your [default]
         * credential profile by reading from the credentials file located at
         * (C:\\Users\\ahmed\\.aws\\credentials).
         */
		AWSCredentials credentials = null;
        try {
            credentials = new ProfileCredentialsProvider("default").getCredentials();
        } catch (Exception e) {
            throw new AmazonClientException(
                    "Cannot load the credentials from the credential profiles file. " +
                    "Please make sure that your credentials file is at the correct " +
                    "location (C:\\Users\\ahmed\\.aws\\credentials), and is in valid format.",
                    e);
        }
     // Create the AmazonEC2Client object so we can call various APIs.
        AmazonEC2 ec2 = AmazonEC2ClientBuilder.standard()
            .withCredentials(new AWSStaticCredentialsProvider(credentials))
            .withRegion("eu-central-1")
            .build();
        
     // Retrieving Running Instances Status
        try {
        DescribeInstanceStatusRequest describeInstanceRequest = new DescribeInstanceStatusRequest().withIncludeAllInstances(true);
        DescribeInstanceStatusResult describeInstanceResult = ec2.describeInstanceStatus(describeInstanceRequest);
        List<com.amazonaws.services.ec2.model.InstanceStatus> state = describeInstanceResult.getInstanceStatuses();
        int i=0;
      
        while (state.size() > i) {
        	
          if(state.get(i).getInstanceState().getName().equals("running")) {
             System.out.println("id-"+state.get(i).getInstanceId()+"\n");
                System.out.println("state-"+state.get(i).getInstanceState()+"\n");
                System.out.println("zone-"+state.get(i).getAvailabilityZone()+"\n");
                 System.out.println("system status-"+state.get(i).getSystemStatus()+"\n");
            }
            i++;
          }
        }
        catch (AmazonServiceException e)
        {
        	System.out.println(e.getMessage());
        }
        

	}

}
