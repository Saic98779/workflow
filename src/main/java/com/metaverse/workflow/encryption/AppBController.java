//package com.metaverse.workflow.encryption;
//
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//public class AppBController {
//
//    private final SecureServiceA secureService;
//
//    public AppBController(SecureServiceA secureService) {
//        this.secureService = secureService;
//    }
//
//    @GetMapping("/receive")
//    public String receive(@RequestParam String data) {
//        try {
//            System.out.println(data);
//
//            Employee decryptAndVerify = secureService.decryptAndVerify(data);
//            String securePayload = secureService.encryptAndSign(decryptAndVerify);
//
//
//            System.out.println(decryptAndVerify);
//
//            return securePayload;
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "Error: " + e.getMessage();
//        }
//    }
//}
//
