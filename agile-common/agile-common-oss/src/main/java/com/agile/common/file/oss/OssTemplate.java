package com.agile.common.file.oss;

import com.agile.common.file.core.FileProperties;
import com.agile.common.file.core.FileTemplate;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import lombok.AllArgsConstructor;

import java.io.InputStream;
import java.util.List;

/**
 * aws-s3 General storage operation supports all cloud storage compatible with s3 protocol.
 * (Ali Cloud OSS, Tencent Cloud COS, Qiniuyun, Jingdong Cloud, minio, etc)
 *
 * @author Huang Z.Y.
 */
@AllArgsConstructor
public class OssTemplate implements FileTemplate {

    private final FileProperties fileProperties;

    @Override
    public void createBucket(String bucketName) {

    }

    @Override
    public List<Bucket> getAllBuckets() {
        return null;
    }

    @Override
    public void removeBucket(String bucketName) {

    }

    @Override
    public void putObject(String bucketName, String objectName, InputStream stream, String contextType) throws Exception {

    }

    @Override
    public void putObject(String bucketName, String objectName, InputStream stream) throws Exception {

    }

    @Override
    public S3Object getObject(String bucketName, String objectName) {
        return null;
    }

    @Override
    public void removeObject(String bucketName, String objectName) throws Exception {

    }

    @Override
    public List<S3ObjectSummary> getAllObjectsByPrefix(String bucketName, String prefix, boolean recursive) {
        return null;
    }

}
    