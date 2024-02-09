package com.private_lbs.taskmaster.S3.controller;

import com.private_lbs.taskmaster.redis.service.RedisPubService;
import com.private_lbs.taskmaster.S3.data.dto.EventNotification;
import com.private_lbs.taskmaster.S3.data.dto.EventRecord;
import com.private_lbs.taskmaster.S3.data.vo.OriginUrl;
import com.private_lbs.taskmaster.S3.service.S3Service;
import com.private_lbs.taskmaster.member.domain.Member;
import com.private_lbs.taskmaster.member.service.MemberService;
import com.private_lbs.taskmaster.request.domain.Request;
import com.private_lbs.taskmaster.request.service.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/S3")
@RequiredArgsConstructor
public class S3Controller {

    private final RedisPubService redisPubService;
    private final RequestService requestService;
    private final MemberService memberService;
    private final S3Service s3Service;

    // 클라이언트로부터 AWS S3 사전 서명된 URL 생성 요청을 처리
    @GetMapping("/generate-url")
    public ResponseEntity<String> generatePresignedUrl(@RequestParam String filename){

        Member member = memberService.getCurrentMember();
        Request request = requestService.save(member);

        long durationMillis = 100000 * 60;

        OriginUrl originUrl = s3Service.makeOriginUrl(filename, member.getId(), request.getId());
        URL presignedUrl = s3Service.generatePresignedUrl(originUrl.getFileKey(), durationMillis);

        requestService.setUrls(request, originUrl, presignedUrl);
        return ResponseEntity.ok(presignedUrl.toString());
    }



    // AWS S3에서 파일 업로드 이벤트 수신 및 AI 서버가 SUB 중인 채널로 전송
    @PostMapping("/endpoint")
    public ResponseEntity<Void> receiveFileUrl(@RequestBody EventNotification notification){
        for (EventRecord record : notification.getRecords()) {
            redisPubService.sendMessage(OriginUrl.makeUrlFromEventRecord(record));
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }



    @GetMapping("/generate-url")
    public ResponseEntity<?> generateUrl(@RequestParam String filename, @RequestParam Long filesize) {
        // 여기서 Member ID와 Request ID는 예시로 사용된 값입니다.
        // 실제 애플리케이션에서는 인증된 사용자의 ID와 요청 ID를 얻는 로직이 필요할 수 있습니다.
        Member member = memberService.getCurrentMember();
        Request request = requestService.save(member);
        Map<String, Object> response = s3Service.createMultipartUploadUrls(filename, filesize, member.getId(), request.getId());
        return ResponseEntity.ok(response);
    }

    // 업로드 완료 요청 처리
    @PostMapping("/complete-upload")
    public ResponseEntity<?> completeUpload(@RequestBody CompleteUploadRequest completeUploadRequest) {
        // 업로드 완료 처리 로직 구현
        // 예: S3에 CompleteMultipartUpload 요청 보내기
        return ResponseEntity.ok().build();
    }




}
