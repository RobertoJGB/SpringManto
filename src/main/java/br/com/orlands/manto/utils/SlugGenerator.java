package br.com.orlands.manto.utils;

import java.text.Normalizer;
import java.util.Locale;
import java.util.Random;

public class SlugGenerator {

    public static String toSlugRandomCode(String input) {
        String slug = Normalizer.normalize(input, Normalizer.Form.NFD)
            .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
            .toLowerCase(Locale.ROOT)
            .replaceAll("[^a-z0-9\\s-]", "")  // remove caracteres especiais
            .replaceAll("\\s+", "-")          // espaços viram '-'
            .replaceAll("-{2,}", "-")         // múltiplos '-' viram um só
            .replaceAll("^-|-$", "");         // remove '-' iniciais ou finais

        String randomCode = generateRandomCode(6); // código de 6 caracteres

        return slug + "-" + randomCode;
    }

    private static String generateRandomCode(int length) {
        String chars = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }
}

