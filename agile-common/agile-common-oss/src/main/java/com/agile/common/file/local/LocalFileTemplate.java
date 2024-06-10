package com.agile.common.file.local;

import cn.hutool.core.io.FileUtil;
import com.agile.common.file.core.FileProperties;
import com.agile.common.file.core.FileTemplate;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Local file read mode.
 *
 * @author Huang Z.Y.
 */
@RequiredArgsConstructor
public class LocalFileTemplate implements FileTemplate {

    private final FileProperties properties;

    @Override
    public void createBucket(String bucketName) {
        FileUtil.mkdir(properties.getLocal().getBasePath() + FileUtil.FILE_SEPARATOR + bucketName);
    }

    @Override
    public List<Bucket> getAllBuckets() {
        return Arrays.stream(FileUtil.ls(properties.getLocal().getBasePath()))
                .filter(FileUtil::isDirectory)
                .map(dir -> new Bucket(dir.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public void removeBucket(String bucketName) {
        FileUtil.del(properties.getLocal().getBasePath() + FileUtil.FILE_SEPARATOR + bucketName);
    }

    @Override
    public void putObject(String bucketName, String objectName, InputStream stream, String contextType) {
        // Created when a Bucket does not exist
        String dir = properties.getLocal().getBasePath() + FileUtil.FILE_SEPARATOR + bucketName;
        if (!FileUtil.isDirectory(properties.getLocal().getBasePath() + FileUtil.FILE_SEPARATOR + bucketName)) {
            createBucket(bucketName);
        }

        // Write file
        File file = FileUtil.file(dir + FileUtil.FILE_SEPARATOR + objectName);
        FileUtil.writeFromStream(stream, file);
    }

    @Override
    @SneakyThrows
    public S3Object getObject(String bucketName, String objectName) {
        String dir = properties.getLocal().getBasePath() + FileUtil.FILE_SEPARATOR + bucketName;
        S3Object s3Object = new S3Object();
        s3Object.setObjectContent(FileUtil.getInputStream(dir + FileUtil.FILE_SEPARATOR + objectName));
        return s3Object;
    }

    @Override
    public void removeObject(String bucketName, String objectName) throws Exception {
        String dir = properties.getLocal().getBasePath() + FileUtil.FILE_SEPARATOR + bucketName;
        FileUtil.del(dir + FileUtil.FILE_SEPARATOR + objectName);
    }

    @Override
    public void putObject(String bucketName, String objectName, InputStream stream) throws Exception {
        putObject(bucketName, objectName, stream, null);
    }

    @Override
    public List<S3ObjectSummary> getAllObjectsByPrefix(String bucketName, String prefix, boolean recursive) {
        String dir = properties.getLocal().getBasePath() + FileUtil.FILE_SEPARATOR + bucketName;

        return Arrays.stream(FileUtil.ls(dir)).filter(file -> file.getName().startsWith(prefix)).map(file -> {
            S3ObjectSummary summary = new S3ObjectSummary();
            summary.setKey(file.getName());
            return summary;
        }).collect(Collectors.toList());
    }

}
    