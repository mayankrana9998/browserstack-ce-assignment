package com.browserstack.assignment;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class RapidTranslator {

    private static final String API_KEY = "836ee8c138mshea6436cb10d3b65p16a066jsn9b74e9b59a00";
    private static final String API_HOST = "google-translate113.p.rapidapi.com";

    public static String translateToEnglish(String spanishText) {
        try {
            URL url = new URL("https://google-translate113.p.rapidapi.com/api/v1/translator/json");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("X-RapidAPI-Key", API_KEY);
            conn.setRequestProperty("X-RapidAPI-Host", API_HOST);
            conn.setDoOutput(true);

            // ✅ Wrap text inside the JSON body under "json.title"
            JSONObject json = new JSONObject();
            json.put("title", spanishText);

            JSONObject body = new JSONObject();
            body.put("from", "es");
            body.put("to", "en");
            body.put("json", json);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(body.toString().getBytes());
                os.flush();
            }

            Scanner scanner = new Scanner(conn.getInputStream());
            String response = scanner.useDelimiter("\\A").next();
            scanner.close();

            // ✅ Parse nested translated JSON
            JSONObject responseObject = new JSONObject(response);
            JSONObject trans = responseObject.getJSONObject("trans");
            return trans.getString("title");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ RapidAPI translation failed: " + e.getMessage());
            return "(Translation failed)";
        }
    }
}
