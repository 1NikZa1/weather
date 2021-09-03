package com.nikza;

import com.google.gson.*;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.Optional;
import java.util.Scanner;

public class Main {

    private static final String TOKEN = "&appid=56d34c9219f692b8ae5b2cb6616f0a95";
    private static final String LINK = "https://api.openweathermap.org/data/2.5/weather?units=metric&id=";
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Scanner SCANNER = new Scanner(System.in);

    private static int getCityCode() throws FileNotFoundException {
        System.out.println("Введите город: ");
        String str = SCANNER.nextLine();

        City[] list = GSON.fromJson(new FileReader("src/cityList.json"), City[].class);

        Optional<City> optional = Arrays.stream(list)
                .filter(v -> v.getName().equals(str)).findFirst();

        int a = 0;
        if (optional.isPresent())
            a = optional.get().getId();
        return a;
    }

    public static void main(String[] args) throws IOException {

        URL url = new URL(LINK + getCityCode() + TOKEN);

        URLConnection request = url.openConnection();
        request.connect();
        JsonElement root;

        try {
            root = JsonParser.parseReader(new InputStreamReader((InputStream) request.getContent()));
        } catch (IOException ex) {
            System.out.println("Город не найден");
            return;
        }

        JsonObject rootobj = root.getAsJsonObject();
        JsonElement zipcode = rootobj.get("main");
        Temp temp = GSON.fromJson(zipcode, Temp.class);
        System.out.println("Сейчас: " + temp.getTemp() + "\n" +
                "Ощущается как: " + temp.getFeels_like() + "\n" +
                "Минимальная за день: " + temp.getTemp_min() + "\n" +
                "Максимальная за день: " + temp.getTemp_max() + "\n" +
                "Влажность: " + temp.getHumidity() + "\n" +
                "Давление: " + temp.getPressure() + "\n"+
                zipcode);

    }
}
