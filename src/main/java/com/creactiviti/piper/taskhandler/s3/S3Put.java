package com.creactiviti.piper.taskhandler.s3;

import java.nio.file.Paths;

import org.springframework.stereotype.Component;

import com.creactiviti.piper.core.task.Task;
import com.creactiviti.piper.core.task.TaskHandler;

import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest.Builder;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

/**
 * @author Arik Cohen
 * @since Feb, 11 2020
 */
@Component("s3/put")
class S3Put implements TaskHandler<PutObjectResponse> {

  @Override
  public PutObjectResponse handle (Task aTask) throws Exception {
    
    S3Client s3 = S3Client.builder().build();
    
    String bucketName;
    String key;
    
    String uri = aTask.getString("uri");
    
    if(uri != null) {
      AmazonS3URI s3Uri = new AmazonS3URI(uri);
      bucketName = s3Uri.getBucket();
      key = s3Uri.getKey();
    }
    else {
      bucketName = aTask.getRequiredString("bucketName");
      key = aTask.getRequiredString("key");
    }
    
    Builder putObjectRequestBuilder = PutObjectRequest.builder()
                                                      .bucket(bucketName)
                                                      .key(key);
    
    if(aTask.getString("acl")!=null) {
      putObjectRequestBuilder.acl(ObjectCannedACL.fromValue(aTask.getString("acl")));
    }
    
    PutObjectResponse response = s3.putObject(
      putObjectRequestBuilder.build(),
      Paths.get(aTask.getRequiredString("filepath"))
    );
    
    return response;
  }

}
