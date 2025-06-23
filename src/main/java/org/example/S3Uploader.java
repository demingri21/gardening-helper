package org.example;

import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.nio.file.Paths;

public class S3Uploader
{

    private final S3Client s3;
    private static final String bucketName = "garden-plant-images";

    public S3Uploader()
    {
        this.s3 = S3Client.builder().region(Region.US_EAST_1).credentialsProvider(ProfileCredentialsProvider.create()).build();
    }

    public void uploadFile(String localFilePath, String keyNameInBucket)
    {
        PutObjectRequest putRequest = PutObjectRequest.builder().bucket(bucketName).key(keyNameInBucket).build();
        s3.putObject(putRequest, RequestBody.fromFile(Paths.get(localFilePath)));
        System.out.println("Upload complete: " + keyNameInBucket);
    }
}
