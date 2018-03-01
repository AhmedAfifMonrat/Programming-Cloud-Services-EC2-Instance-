package com.aws.munrat;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.List;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder;
import com.amazonaws.services.ec2.model.AuthorizeSecurityGroupIngressRequest;
import com.amazonaws.services.ec2.model.CreateKeyPairRequest;
import com.amazonaws.services.ec2.model.CreateKeyPairResult;
import com.amazonaws.services.ec2.model.CreateSecurityGroupRequest;
import com.amazonaws.services.ec2.model.CreateSecurityGroupResult;
import com.amazonaws.services.ec2.model.IpPermission;
import com.amazonaws.services.ec2.model.KeyPair;
import com.amazonaws.services.ec2.model.RunInstancesRequest;
import com.amazonaws.services.ec2.model.RunInstancesResult;

public class RunningInstance {
	
	 private static CreateKeyPairResult createKeyPairResult;
	 private static String privateKey;
	 private static RunInstancesResult result;
	 

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
        
        //*********************CREATING SECURITY GROUPS*************************//
        try {
            CreateSecurityGroupRequest securityGroupRequest = new CreateSecurityGroupRequest(
                    "MonratSecurity", "My Security Group");
            CreateSecurityGroupResult result = ec2
                    .createSecurityGroup(securityGroupRequest);
            System.out.println(String.format("Security group created: [%s]",
                    result.getGroupId()));
        } catch (AmazonServiceException ase) {
            // Likely this means that the group is already created, so ignore.
            System.out.println(ase.getMessage());
        }

        String ipAddr = "0.0.0.0/0";
        // Get the IP of the current host, so that we can limit the Security Group
        // by default to the ip range associated with your subnet.
        try {
            InetAddress addr = InetAddress.getLocalHost();

            // Get IP Address
            ipAddr = addr.getHostAddress()+"/10";
        } catch (UnknownHostException e) {
        }

        // Create a range that you would like to populate.
        List<String> ipRanges = Collections.singletonList(ipAddr);

        // Open up port 23 for TCP traffic to the associated IP from above (e.g. ssh traffic).
        IpPermission ipPermission = new IpPermission()
                .withIpProtocol("tcp")
                .withFromPort(new Integer(22))
                .withToPort(new Integer(22))
                .withIpRanges(ipRanges);

        List<IpPermission> ipPermissions = Collections.singletonList(ipPermission);
        try {
            // Authorize the ports to the used.
        	AuthorizeSecurityGroupIngressRequest authorizeSecurityGroupIngressRequest =
            new AuthorizeSecurityGroupIngressRequest();
            authorizeSecurityGroupIngressRequest.withGroupName("MonratSecurity").withIpPermissions(ipPermission);
        	ec2.authorizeSecurityGroupIngress(authorizeSecurityGroupIngressRequest);
            System.out.println(String.format("Ingress port authroized: [%s]",ipPermissions.toString()));
        } catch (AmazonServiceException ase) {
            // Ignore because this likely means the zone has already been authorized.
            System.out.println(ase.getMessage());
        }
        
 //creating key
        
        CreateKeyPairRequest createKeyPairRequest = new CreateKeyPairRequest();
        createKeyPairRequest.withKeyName("keyEc2Monrat");
        createKeyPairResult = ec2.createKeyPair(createKeyPairRequest);
        
        KeyPair keyPair = new KeyPair();

        keyPair = createKeyPairResult.getKeyPair();

        privateKey = keyPair.getKeyMaterial();
        
      //Create an instance in Frankfurt
        
        RunInstancesRequest runInstancesRequest = new RunInstancesRequest();

         		runInstancesRequest.withImageId("ami-97e953f8")
         		                   .withInstanceType("t2.micro")
         		                   .withMinCount(1)
         		                   .withMaxCount(1)
         		                   .withKeyName("keyEc2Monrat")
         		                   .withSecurityGroups("MonratSecurity");
         		
         		
         	result = ec2.runInstances(runInstancesRequest);
         	
        

	}

}
