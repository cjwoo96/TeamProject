package com.private_lbs.taskmaster.S3.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.private_lbs.taskmaster.member.domain.Member;
import com.private_lbs.taskmaster.redis.service.RedisPubService;
import com.private_lbs.taskmaster.S3.data.dto.EventRecord;
import com.private_lbs.taskmaster.S3.data.vo.OriginUrl;
import com.private_lbs.taskmaster.request.domain.Request;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class S3Service {

    private RedisPubService redisPubService;
    private final AmazonS3Client amazonS3Client;
    private final S3Service s3Service;

    // AWS S3 버킷 정보와 리전을 설정
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    @Value("${cloud.aws.s3.region.static}")
    private String region;

    // AWS S3에서 파일에 대한 사전 서명된 URL을 생성하고 반환
    // 파일 이름, 사용자 ID, 요청 ID, 그리고 유효 기간을 인자로 받음
    public URL generatePresignedUrl(String fileKey, long durationMillis){
        Date expiration = new Date(System.currentTimeMillis() + durationMillis);
        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucket, fileKey)
                .withMethod(HttpMethod.PUT)
                .withExpiration(expiration);

        return amazonS3Client.generatePresignedUrl(request);
    }

    public Map<String, Object> createMultipartUploadUrls(String filename, Long fileSize, Long memberId, Long requestId) {

        OriginUrl originUrl = s3Service.makeOriginUrl(filename, memberId, requestId);
        String uploadId = initiateMultipartUpload(originUrl.toString());

        long partSize = 5 * 1024 * 1024; // 5MB
        int partCount = (int) Math.ceil((double) fileSize / partSize);
        List<String> presignedUrls = new ArrayList<>();

        for (int partNumber = 1; partNumber <= partCount; partNumber++) {
            GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucketName, keyPrefix)
                    .withMethod(HttpMethod.PUT)
                    .withExpiration(new Date(new Date().getTime() + 1000 * 60 * 60)) // URL 유효 시간 설정
                    .withUploadId(uploadId)
                    .withPartNumber(partNumber);
            URL url = amazonS3.generatePresignedUrl(request);
            presignedUrls.add(url.toString());
        }

        Map<String, Object> response = new HashMap<>();
        response.put("presignedUrls", presignedUrls);
        response.put("uploadId", uploadId);
        return response;
    }
    private String initiateMultipartUpload(String keyPrefix) {
        InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest(bucketName, keyPrefix);
        InitiateMultipartUploadResult result = amazonS3.initiateMultipartUpload(request);
        return result.getUploadId();
    }


    public OriginUrl makeOriginUrl(String fileName, long userId, long requestId) {
        String fileKey = userId + "/" + requestId + "/" + fileName;
        return OriginUrl.of(bucket, fileKey);
    }

    public void save(List<EventRecord> records) {
        for (EventRecord record : records) {
            redisPubService.sendMessage(OriginUrl.makeUrlFromEventRecord(record));
        }
    }
}
