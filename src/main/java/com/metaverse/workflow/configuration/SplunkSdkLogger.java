package com.metaverse.workflow.configuration;

import okhttp3.*;

import javax.net.ssl.*;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.time.Duration;

public class SplunkSdkLogger {

    private static final String HEC_URL =
            "https://195.250.30.208:8088/services/collector/raw";

    private static final String TOKEN =
            "5609e7b9-78ef-4487-be90-b08372129a0b";

    private static final String CHANNEL =
            "6f1c9d9e-2f8a-4c2e-9d9b-6b5c9d7f4a12";

    private static final OkHttpClient CLIENT = buildUnsafeClient();

    private static OkHttpClient buildUnsafeClient() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public void checkClientTrusted(X509Certificate[] c, String a) {}
                        public void checkServerTrusted(X509Certificate[] c, String a) {}
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[0];
                        }
                    }
            };

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new SecureRandom());

            return new OkHttpClient.Builder()
                    .sslSocketFactory(
                            sslContext.getSocketFactory(),
                            (X509TrustManager) trustAllCerts[0]
                    )
                    .hostnameVerifier((h, s) -> true)
                    .connectTimeout(Duration.ofSeconds(5))
                    .readTimeout(Duration.ofSeconds(5))
                    .build();

        } catch (Exception e) {
            throw new RuntimeException("Failed to create unsafe OkHttp client", e);
        }
    }

    public static void send(String message) {
        try {
            Request request = new Request.Builder()
                    .url(HEC_URL)
                    .addHeader("Authorization", "Splunk " + TOKEN)
                    .addHeader("X-Splunk-Request-Channel", CHANNEL)
                    .post(RequestBody.create(
                            message,
                            MediaType.parse("text/plain")
                    ))
                    .build();

            try (Response response = CLIENT.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    System.err.println("Splunk HEC failed: " + response.code());
                }
            }
        } catch (Exception e) {
            // IMPORTANT: print once while testing
            e.printStackTrace();
        }
    }
}
