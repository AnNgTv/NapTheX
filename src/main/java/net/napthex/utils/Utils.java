package net.napthex.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.napthex.NapTheX;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.util.*;
import java.util.stream.Collectors;

public class Utils {
    public static String randomMD5() {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            String s = String.valueOf(System.currentTimeMillis() + FNum.randomInt(0, 999999));
            md.update(s.getBytes("UTF-8"));
            byte[] digest = md.digest();
            BigInteger bigInt = new BigInteger(1, digest);
            StringBuilder sb = new StringBuilder(bigInt.toString(16));
            while (sb.length() < 32) {
                sb.insert(0, "0");
            }
            return sb.toString();
        } catch (Exception e) {
            return "";
        }
    }

    public static JsonObject fetchJsonResponse(String urlStr) {
        try {
            CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setConnectTimeout(60000);
            conn.setReadTimeout(60000);

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String response = reader.lines().collect(Collectors.joining());
            reader.close();
            conn.disconnect();

            return new JsonParser().parse(response).getAsJsonObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void dispatchCommand(Player player, String command) {
        Task.sync(() -> {
            String type = command.replaceAll("(?ium)^(player:|op:|console:|)(.*)$", "$1").replace(":", "").toLowerCase();
            String cmd = command.replaceAll("(?ium)^(player:|op:|console:|)(.*)$", "$2").replaceAll("(?ium)([{]Player[}])", player.getName());
            
            switch (type) {
                case "op":
                    boolean isOp = player.isOp();
                    try {
                        player.setOp(true);
                        player.performCommand(cmd);
                    } finally {
                        player.setOp(isOp);
                    }
                    break;
                case "player":
                case "":
                    player.performCommand(cmd);
                    break;
                case "console":
                default:
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
                    break;
            }
        });
    }

    public static LinkedHashMap<Integer, String> sortByComparator(Map<String, Integer> unsortMap, final boolean order, int limit) {
        List<Map.Entry<String, Integer>> list = new LinkedList<>(unsortMap.entrySet());
        list.sort((o1, o2) -> {
            if (order) {
                return o1.getValue().compareTo(o2.getValue());
            } else {
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        LinkedHashMap<Integer, String> sortedMap = new LinkedHashMap<>();
        int i = 1;
        for (Map.Entry<String, Integer> entry : list) {
            if (i > limit) break;
            sortedMap.put(i, entry.getKey());
            i++;
        }
        return sortedMap;
    }
}
