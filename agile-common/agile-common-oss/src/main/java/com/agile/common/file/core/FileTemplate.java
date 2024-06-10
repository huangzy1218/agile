package com.agile.common.file.core;

import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import org.springframework.beans.factory.InitializingBean;

import java.io.InputStream;
import java.util.List;

/**
 * File manipulation template.
 *
 * @author Huang Z.Y.
 */
public interface FileTemplate extends InitializingBean {

    /**
     * Create bucket.
     *
     * @param bucketName Bucket name
     */
    void createBucket(String bucketName);

    /**
     * Get all bucket.
     */
    List<Bucket> getAllBuckets();

    /**
     * @param bucketName Bucket name.
     */
    void removeBucket(String bucketName);

    /**
     * Upload file.
     *
     * @param bucketName  Bucket name
     * @param objectName  File name
     * @param stream      File stream
     * @param contextType File type
     */
    void putObject(String bucketName, String objectName, InputStream stream, String contextType) throws Exception;

    /**
     * Upload file.
     *
     * @param bucketName Bucket name
     * @param objectName File name
     * @param stream     File stream
     */
    void putObject(String bucketName, String objectName, InputStream stream) throws Exception;

    /**
     * Get file.
     *
     * @param bucketName Bucket name
     * @param objectName File name
     * @return Binary stream API Documentation
     */
    S3Object getObject(String bucketName, String objectName);

    void removeObject(String bucketName, String objectName) throws Exception;

    @Override
    default void afterPropertiesSet() throws Exception {
    }

    /**
     * Query files by file prefix.
     *
     * @param bucketName Bucket name
     * @param prefix     Prefix
     * @param recursive  Whether to query recursively
     * @return S3ObjectSummary List
     * @see <a href="http://docs.aws.amazon.com/goto/WebAPI/s3-2006-03-01/ListObjects">AWS
     * API Documentation</a>
     */
    List<S3ObjectSummary> getAllObjectsByPrefix(String bucketName, String prefix, boolean recursive);

}
    