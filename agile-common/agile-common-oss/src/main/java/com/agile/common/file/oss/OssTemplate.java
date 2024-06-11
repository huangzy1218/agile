package com.agile.common.file.oss;

import com.agile.common.file.core.FileProperties;
import com.agile.common.file.core.FileTemplate;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import lombok.Cleanup;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

/**
 * aws-s3 General storage operation supports all cloud storage compatible with s3 protocol.
 * (Ali Cloud OSS, Tencent Cloud COS, Qiniuyun, Jingdong Cloud, minio, etc)
 *
 * @author Huang Z.Y.
 */
@RequiredArgsConstructor
public class OssTemplate implements FileTemplate {

    private final FileProperties properties;

    private AmazonS3 amazonS3;

    @Override
    @SneakyThrows
    public void createBucket(String bucketName) {
        if (!amazonS3.doesBucketExistV2(bucketName)) {
            amazonS3.createBucket((bucketName));
        }
    }

    @Override
    public List<Bucket> getAllBuckets() {
        return amazonS3.listBuckets();
    }

    @SneakyThrows
    public Optional<Bucket> getBucket(String bucketName) {
        return amazonS3.listBuckets().stream().filter(b -> b.getName().equals(bucketName)).findFirst();
    }

    @Override
    @SneakyThrows
    public void removeBucket(String bucketName) {
        amazonS3.deleteBucket(bucketName);
    }

    @Override
    public void putObject(String bucketName, String objectName, InputStream stream, String contextType) throws Exception {
        putObject(bucketName, objectName, stream, stream.available(), contextType);
    }

    public PutObjectResult putObject(String bucketName, String objectName, InputStream stream, long size,
                                     String contextType) throws Exception {
        // String fileName = getFileName(objectName);
        byte[] bytes = IOUtils.toByteArray(stream);
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(size);
        objectMetadata.setContentType(contextType);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        // Upload
        return amazonS3.putObject(bucketName, objectName, byteArrayInputStream, objectMetadata);
    }

    @Override
    public void putObject(String bucketName, String objectName, InputStream stream) throws Exception {
        putObject(bucketName, objectName, stream, stream.available(), "application/octet-stream");
    }

    @Override
    public S3Object getObject(String bucketName, String objectName) {
        return amazonS3.getObject(bucketName, objectName);
    }

    @Override
    public void removeObject(String bucketName, String objectName) throws Exception {
        amazonS3.deleteObject(bucketName, objectName);
    }

    @Override
    public List<S3ObjectSummary> getAllObjectsByPrefix(String bucketName, String prefix, boolean recursive) {
        ObjectListing objectListing = amazonS3.listObjects(bucketName, prefix);
        return new ArrayList<>(objectListing.getObjectSummaries());
    }

    @Override
    public void afterPropertiesSet() {
        ClientConfiguration clientConfiguration = new ClientConfiguration();
        clientConfiguration.setMaxConnections(properties.getOss().getMaxConnections());

        AwsClientBuilder.EndpointConfiguration endpointConfiguration = new AwsClientBuilder.EndpointConfiguration(
                properties.getOss().getEndpoint(), properties.getOss().getRegion());
        AWSCredentials awsCredentials = new BasicAWSCredentials(properties.getOss().getAccessKey(),
                properties.getOss().getSecretKey());
        AWSCredentialsProvider awsCredentialsProvider = new AWSStaticCredentialsProvider(awsCredentials);
        this.amazonS3 = AmazonS3Client.builder()
                .withEndpointConfiguration(endpointConfiguration)
                .withClientConfiguration(clientConfiguration)
                .withCredentials(awsCredentialsProvider)
                .disableChunkedEncoding()
                .build();
    }

    /**
     * Get the file external link.
     *
     * @param bucketName Bucket name
     * @param objectName File name
     * @param expires    Expire time <=7
     * @return url
     * @see AmazonS3#generatePresignedUrl(String bucketName, String key, Date expiration)
     */
    @SneakyThrows
    public String getObjectURL(String bucketName, String objectName, Integer expires) {
        Date date = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, expires);
        URL url = amazonS3.generatePresignedUrl(bucketName, objectName, calendar.getTime());
        return url.toString();
    }

    /**
     * Get file information.
     *
     * @param bucketName Bucket name
     * @param objectName File name
     * @see <a href="http://docs.aws.amazon.com/goto/WebAPI/s3-2006-03-01/GetObject">AWS
     * API Documentation</a>
     */
    public S3Object getObjectInfo(String bucketName, String objectName) throws Exception {
        @Cleanup
        S3Object object = amazonS3.getObject(bucketName, objectName);
        return object;
    }

}
    