package utils;

import model.Date;

public class DateHandler {
    public static String[] getYears() {
        String[] years = new String[56]; // 1970-2025
        for (int i = 0; i < 56; i++) {
            years[i] = String.valueOf(1970 + i);
        }
        return years;
    }

    public static String[] getMonths() {
        String[] months = new String[12];
        for (int i = 0; i < 12; i++) {
            months[i] = String.valueOf(i + 1);
        }
        return months;
    }

    public static String[] getDays(int month, int year) {
        int daysInMonth = Date.getDaysInMonth(month, year);
        String[] days = new String[daysInMonth];
        for (int i = 0; i < daysInMonth; i++) {
            days[i] = String.valueOf(i + 1);
        }
        return days;
    }

    public static boolean validateDate(int day, int month, int year) {
        Date date = new Date(day, month, year);
        return date.isValid();
    }
}