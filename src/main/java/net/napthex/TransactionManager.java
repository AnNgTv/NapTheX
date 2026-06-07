package net.napthex;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Comparator;
import java.util.stream.Collectors;
import net.napthex.utils.Transaction;

public class TransactionManager {
    public static List<Transaction> forDaily(List<Transaction> list, Date date) {
        return list.stream().filter(t -> isSameDay(t.getDate(), date))
                .sorted(Comparator.comparingInt(Transaction::getAmount).reversed())
                .collect(Collectors.toList());
    }

    public static List<Transaction> forWeekly(List<Transaction> list, Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        Date start = cal.getTime();
        return list.stream().filter(t -> t.getDate().after(start))
                .sorted(Comparator.comparingInt(Transaction::getAmount).reversed())
                .collect(Collectors.toList());
    }

    public static List<Transaction> forMonthly(List<Transaction> list, Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        Date start = cal.getTime();
        return list.stream().filter(t -> t.getDate().after(start))
                .sorted(Comparator.comparingInt(Transaction::getAmount).reversed())
                .collect(Collectors.toList());
    }

    public static List<Transaction> forYear(List<Transaction> list, Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_YEAR, 1);
        Date start = cal.getTime();
        return list.stream().filter(t -> t.getDate().after(start))
                .collect(Collectors.toList());
    }

    public static List<Transaction> forPlayer(List<Transaction> list, String name) {
        return list.stream().filter(t -> t.getPlayerName().equalsIgnoreCase(name)).collect(Collectors.toList());
    }

    private static boolean isSameDay(Date d1, Date d2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(d1);
        cal2.setTime(d2);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
               cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }
}
