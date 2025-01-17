package com.github.martinyes.penguinapp.util;

import com.github.martinyes.penguinapp.server.Server;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class AppUtils {

    public static boolean isIPv4Address(String input) {
        String[] parts = input.split("\\.");
        if (parts.length != 4) {
            return false;
        }

        for (String part : parts) {
            try {
                int num = Integer.parseInt(part);
                if (num < 0 || num > 255) {
                    return false;
                }
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return true;
    }

    public static boolean performHttpPing(Server server) throws IOException {
        try {
            long startTime = System.currentTimeMillis();
            URL url = new URL(server.getAddress());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("HEAD");
            int responseCode = connection.getResponseCode();
            long endTime = System.currentTimeMillis();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                server.setResponseTime(endTime - startTime);
                return true;
            } else {
                server.setResponseTime(-1L);
                return false;
            }
        } catch (IOException e) {
            server.setResponseTime(-1L);
            throw e;
        }
    }

    public static boolean performSystemPing(Server server) throws IOException, InterruptedException {
        try {
            boolean isWindows = System.getProperty("os.name").toLowerCase().contains("win");

            long startTime = System.currentTimeMillis();
            ProcessBuilder processBuilder = new ProcessBuilder("ping",
                    isWindows ? "-n" : "-c", "1", server.getAddress());
            Process proc = processBuilder.start();
            boolean finished = proc.waitFor(200, TimeUnit.MILLISECONDS);
            long endTime = System.currentTimeMillis();

            if (finished) {
                server.setResponseTime(endTime - startTime);
                return true;
            } else {
                server.setResponseTime(-1L);
                return false;
            }
        } catch (IOException | InterruptedException e) {
            server.setResponseTime(-1L);
            throw e;
        }
    }
}