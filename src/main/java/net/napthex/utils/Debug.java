package net.napthex.utils;

import net.napthex.NapTheX;
import net.napthex.api.Card;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Debug {
    public static void write(String msg) {
        msg = msg.trim();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
        File file = new File(NapTheX.getInstance().getDataFolder(), "debug.txt");
        if (!file.exists()) {
            file.getParentFile().mkdirs();
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
            bw.append("[").append(sdf.format(date)).append("]").append(msg);
            bw.newLine();
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void write(String msg, Card card) {
        String fullMsg = String.format("%s (Player: %s, Type: %s, Price: %d, Serial: %s, Pin: %s, TransID: %s, InternalID: %s)",
                msg.trim(), card.player(), card.cardType(), card.cardPrice(), card.SERIAL(), card.PIN(), card.transID(), card.internalID());
        write(fullMsg);
        NapTheX.getInstance().getLogger().warning(fullMsg);
    }
}
