package br.com.oversight.sefisca.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;

import org.primefaces.model.UploadedFile;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AccessControlList;
import com.amazonaws.services.s3.model.GroupGrantee;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.Permission;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UtilAmazon {
    public void uploadArquivo(UploadedFile file) {
        String bucketName = "sefisca";
        BasicAWSCredentials awsCreds = new BasicAWSCredentials("AKIAVV5HEO7UZF33LVHW",
                "v+F7idVZ5KYMlsIGVHG2clmnzp+7nHIzJB6X+nH6");
        AmazonS3 s3 = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .withRegion("sa-east-1").build();
        AccessControlList acl = new AccessControlList();
        acl.grantPermission(GroupGrantee.AllUsers, Permission.Read);
        try {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            String keyName = String.valueOf(timestamp.getTime()).concat("-").concat(file.getFileName());
            S3Object s3Object = new S3Object();
            ObjectMetadata omd = new ObjectMetadata();
            omd.setContentType(file.getContentType());
            omd.setContentLength(file.getSize());
            omd.setHeader("filename", file.getFileName());
            InputStream input = file.getInputstream();
            s3Object.setObjectContent(input);
            PutObjectResult uploaded = s3
                    .putObject(new PutObjectRequest(bucketName, keyName, input, omd).withAccessControlList(acl));
            System.out.println("Uploaded successfully! " + uploaded.getETag());
            s3Object.close();

        } catch (AmazonServiceException ase) {
            System.out
                    .println("Caught an AmazonServiceException, which means your request made it to Amazon S3, but was "
                            + "rejected with an error response for some reason.");
            System.out.println("Error Message:  " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:  " + ase.getErrorCode());
            System.out.println("Error Type:    " + ase.getErrorType());
            System.out.println("Request ID:    " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            System.out.println(
                    "Caught an AmazonClientException, which means the client encountered an internal error while "
                            + "trying to communicate with S3, such as not being able to access the network.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
