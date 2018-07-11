package com.example.bsk.mail;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.ByteString;

public class MailGunService {
    public final static String MAILGUN_API_USERNAME = "api";

    private final HttpUrl mailgunApiUrl;
    private final String authHeaderValue;

    public MailGunService(HttpUrl mailgunbaseUrl, String mailgunDomain, String mailgunApiKey) {
        this.mailgunApiUrl = HttpUrl.parse(mailgunbaseUrl.toString() + "/" + mailgunDomain);
        this.authHeaderValue = buildAuthHeader(mailgunApiKey);
    }

    public boolean sendMail(MailgunEmail email) throws IOException {
        Request request = new Request.Builder()
                .url(mailgunApiUrl.toString() + "/messages")
                .post(email.toMultipartBody())
                .addHeader("Authentication", authHeaderValue)
                .build();

        OkHttpClient okHttpClient = new OkHttpClient();
        Response response = okHttpClient.newCall(request).execute();
        return response.isSuccessful();
    }

    private static String buildAuthHeader(String mailgunApiKey) {
        String authString = String.format("%s:%s", MAILGUN_API_USERNAME, mailgunApiKey);
        ByteString authData = ByteString.encodeUtf8(authString);
        return "Basic " + authData.base64();
    }
}