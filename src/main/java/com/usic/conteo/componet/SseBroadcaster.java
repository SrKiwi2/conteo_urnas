package com.usic.conteo.componet;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Component
public class SseBroadcaster {
    private final List<SseEmitter> clients = new CopyOnWriteArrayList<>();

    public SseEmitter subscribe() {
        SseEmitter emitter = new SseEmitter(0L); // sin timeout
        clients.add(emitter);

        emitter.onTimeout(() -> clients.remove(emitter));
        emitter.onCompletion(() -> clients.remove(emitter));
        emitter.onError(e -> clients.remove(emitter));

        // opcional: saludo inicial
        try { emitter.send(SseEmitter.event().name("hello").data("connected")); } catch (Exception ignored) {}
        return emitter;
    }

    public void sendEvent(String name, Object data) {
        List<SseEmitter> dead = new ArrayList<>();
        for (SseEmitter em : clients) {
            try {
                em.send(SseEmitter.event().name(name).data(data));
            } catch (Exception e) {
                dead.add(em);
            }
        }
        clients.removeAll(dead);
    }
}
