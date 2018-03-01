package com.aws.munrat;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder;
import com.amazonaws.services.ec2.model.DescribeRegionsResult;
import com.amazonaws.services.ec2.model.Region;

public class ListingRegionNames {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
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
            .withRegion("us-west-2")
            .build();
        
        try {
        	DescribeRegionsResult regions_response = ec2.describeRegions();

        	for(Region region : regions_response.getRegions()) {
        	    System.out.printf(
        	        "Found region %s " +
        	        "with endpoint %s \n",
        	        region.getRegionName(),
        	        region.getEndpoint());
        }
        }
        	catch (AmazonServiceException ase) {
                // Likely this means that the group is already created, so ignore.
                System.out.println(ase.getMessage());
            }
        

	}

}
