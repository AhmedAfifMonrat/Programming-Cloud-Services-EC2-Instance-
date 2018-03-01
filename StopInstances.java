package com.aws.munrat;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder;
import com.amazonaws.services.ec2.model.StopInstancesRequest;


public class StopInstances {
	
	private static String instance_id= "i-0cbfa93d31b75a9dc";

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
        
        try {

            StopInstancesRequest request = new StopInstancesRequest().withInstanceIds(instance_id);
            ec2.stopInstances(request);
            System.out.println("Instance Stopping........");
        }
        catch (AmazonServiceException e)
        {
        	System.out.println(e.getMessage());
        }
        
        }

	}


