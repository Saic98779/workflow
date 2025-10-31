//package com.metaverse.workflow.encryption;
//
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.reactive.function.client.WebClient;
//
//@RestController
//public class AppAController {
//
//    private final encryptService secureService;
//    private final WebClient webClient;
//
//    public AppAController(encryptService secureService) {
//        this.secureService = secureService;
//        this.webClient = WebClient.builder()
//                .baseUrl("http://localhost:8081") // App B
//                .build();
//    }

//    @GetMapping("/send-to-b")
//    public String sendToB(@RequestBody CentralRampData centralRampData) throws Exception {
//
//
//        String securePayload = secureService.encryptAndSign(centralRampData);
//
//        String response = webClient.get() .uri(uriBuilder ->
//                        uriBuilder.path("/receive") .queryParam("data", securePayload) .build())
//                .retrieve() .bodyToMono(String.class) .block();
//
//        return "App B Response: " +securePayload;
//    }
//}


