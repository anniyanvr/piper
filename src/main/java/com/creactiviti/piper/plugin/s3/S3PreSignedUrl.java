package com.creactiviti.piper.plugin.s3;

import java.time.Duration;

import org.springframework.stereotype.Component;

import com.creactiviti.piper.core.task.Task;
import com.creactiviti.piper.core.task.TaskHandler;

import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

/**
 * Presign an S3 Object action (GET default) so that it can be executed at a 
 * later time without requiring additional signing or authentication.
 * 
 * @author Arik Cohen
 * @since Feb, 10 2020
 */
@Component("s3/presigned-url")
class S3PreSignedUrl implements TaskHandler<String> {

  @Override
  public String handle (Task aTask) throws Exception {
    
    S3Presigner presigner = S3Presigner.create();

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
    
    PresignedGetObjectRequest request =
        presigner.presignGetObject(z -> z.signatureDuration(Duration.ofSeconds(aTask.getInteger("duration", 60)))
                .getObjectRequest(por -> por.bucket(bucketName).key(key)));
    
    return request.url().toString();
  }

}
