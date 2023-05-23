package com.epam.mjc.nio;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;

public class FileReader {

    public Profile getDataFromFile(File file) {
        Map<String, String> profileData = readProfileData(file);
        return createProfileFromData(profileData);
    }

    private Map<String, String> readProfileData(File file) {
        Map<String, String> profileData = new HashMap<>();
        try {
            // Создаем канал для чтения файла
            FileChannel channel = FileChannel.open(file.toPath(), StandardOpenOption.READ);
            // Создаем буфер для хранения данных
            ByteBuffer buffer = ByteBuffer.allocate((int) channel.size());
            // Читаем данные из канала в буфер
            channel.read(buffer);
            // Переводим буфер в режим чтения
            buffer.flip();
            // Создаем декодер для преобразования байтов в символы
            CharsetDecoder decoder = StandardCharsets.UTF_8.newDecoder();
            // Декодируем данные из буфера в символьный поток
            CharBuffer charBuffer = decoder.decode(buffer);
            // Закрываем канал
            channel.close();
            // Разбиваем символьный поток на строки по символу перевода строки
            String[] lines = charBuffer.toString().split("\n");
            // Обрабатываем каждую строку
            for (String line : lines) {
                // Разбиваем строку на ключ и значение по символу двоеточия
                String[] keyValue = line.split(":");
                if (keyValue.length == 2) {
                    String key = keyValue[0].trim();
                    String value = keyValue[1].trim();
                    profileData.put(key, value);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return profileData;
    }

    private Profile createProfileFromData(Map<String, String> profileData) {
        String name = profileData.get("Name");
        Integer age = Integer.parseInt(profileData.get("Age"));
        String email = profileData.get("Email");
        Long phone = Long.parseLong(profileData.get("Phone"));
        return new Profile(name, age, email, phone);
    }
}
