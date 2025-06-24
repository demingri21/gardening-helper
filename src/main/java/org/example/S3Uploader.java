package org.example;

import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;

import java.io.IOException;
import java.nio.file.*;

public class S3Uploader
{
    private final S3Client s3;
    private static final String bucketName = "garden-plant-images";
    private static final String keyNameInBucket = "flower-data";
    public S3Uploader(String key, String secretKey)
    {
        AwsBasicCredentials creds = AwsBasicCredentials.create(key, secretKey);
        this.s3 = S3Client.builder().region(Region.US_EAST_1).credentialsProvider(StaticCredentialsProvider.create(creds)).build();
    }

    public void uploadFile(String localFilePath)
    {
        PutObjectRequest putRequest = PutObjectRequest.builder().bucket(bucketName).key(keyNameInBucket).build();
        s3.putObject(putRequest, RequestBody.fromFile(Paths.get(localFilePath)));
        System.out.println("Upload complete: " + keyNameInBucket);
    }

    public void downloadFile(String localFilePath) throws IOException
    {
        GetObjectRequest getRequest = GetObjectRequest.builder().bucket(bucketName).key(keyNameInBucket).build();
        ResponseInputStream<GetObjectResponse> s3Object = s3.getObject(getRequest);
        Path destination = Paths.get(localFilePath);
        Files.copy(s3Object, destination, StandardCopyOption.REPLACE_EXISTING);
    }
}
