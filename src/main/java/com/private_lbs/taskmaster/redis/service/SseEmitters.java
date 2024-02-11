package com.private_lbs.taskmaster.redis.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


@Service
@Slf4j
public class SseEmitters {
    // SseEmitter 객체를 저장하기 위한 스레드 안전 리스트. 동시 수정 예외를 피하기 위해 CopyOnWriteArrayList 사용.
    private final List<SseEmitter> emitters=new CopyOnWriteArrayList<>();

    // SSe를 통해 연결되어 있는 모든 클라이언트에 메세지를 보냄.
    public void sendMessage(String message){
        //리스트에 있는 모든 SSeEmitter에 대해 메시지를 보내기 위해 반복.
        emitters.forEach(emitter->{
            try{
                // 클라이언트에게 "message" 라는 이름의 SSe 이벤트 전송. 이 이벤트에는 전송 데이터 포함.
                emitter.send(SseEmitter.event()
                        .name("message")
                        .data(message));
                log.info("Message send to client:{}",message);
            } catch (Exception e) {
                log.error("Error sending SSE: {}", e.getMessage());
                emitters.remove(emitter);
            }
        });
    }
}
