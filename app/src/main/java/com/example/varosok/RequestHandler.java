package com.example.varosok;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class RequestHandler {
    private RequestHandler(){}
    private static HttpURLConnection setupConnection(String url) throws IOException {
        URL urlobj = new URL(url);
        HttpURLConnection connection = (HttpURLConnection)  urlobj.openConnection();
        connection.setRequestProperty("Accept","application/json");
        connection.setConnectTimeout(10000);
        connection.setReadTimeout(10000);
        return connection;
    }
    private static Response getResponse(HttpURLConnection connection) throws IOException {
        int responseCode = connection.getResponseCode();
        InputStream inputStream;
        if (responseCode < 400) {
            inputStream = connection.getInputStream();
        } else {
            inputStream = connection.getErrorStream();
        }
        StringBuilder content = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line = reader.readLine();
        while (line != null) {
            content.append(line);
            line = reader.readLine();
        }

        reader.close();
        inputStream.close();

        return new Response(responseCode, content.toString());
    }
    public static Response get(String url) throws IOException {
        //connection létrehozása
        HttpURLConnection connection = setupConnection(url);
        //connection típusának beállítása
        connection.setRequestMethod("GET");
        //response visszaadása
        return getResponse(connection);
    }
    public static Response post(String url, String requestBody) throws IOException {
        //connection létrehozása
        HttpURLConnection connection = setupConnection(url);
        //connection típusának beállítása
        connection.setRequestMethod("POST");
        //requestBody hozzáadása
        addRequestBody(connection, requestBody);
        //response visszaadása
        return getResponse(connection);
    }

    //put metódus létrehozása a PUT kéréshez
    public static Response put(String url, String requestBody) throws IOException {
        //connection létrehozása
        HttpURLConnection connection = setupConnection(url);
        //connection típusának beállítása
        connection.setRequestMethod("PUT");
        //requestBody hozzáadása
        addRequestBody(connection, requestBody);
        //response visszaadása
        return getResponse(connection);
    }

    //delete metódus létrehozása a DELETE kéréshez
    public static Response delete(String url) throws IOException {
        //connection létrehozása
        HttpURLConnection connection = setupConnection(url);
        //connection típusának beállítása
        connection.setRequestMethod("DELETE");
        //response visszaadása
        return getResponse(connection);
    }
    private static void addRequestBody(HttpURLConnection connection, String requestBody) throws IOException {
        connection.setRequestProperty("Content-Type", "application/json");
        //outputStream létrehozása
        connection.setDoOutput(true);
        OutputStream outputStream = connection.getOutputStream();
        //írás a outputStream-be
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
        //requestBody írása a writer-be
        writer.write(requestBody);
        //véglegesítés
        writer.flush();
        //bezárás
        writer.close();
        outputStream.close();
    }
}


