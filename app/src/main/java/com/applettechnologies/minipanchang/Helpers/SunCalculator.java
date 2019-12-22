package com.applettechnologies.minipanchang.Helpers;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class SunCalculator {
    public static final double ZENITH_ASTRONOMICAL = 108.0d;
    public static final double ZENITH_CIVIL = 96.0d;
    public static final double ZENITH_NAUTICAL = 102.0d;
    public static final double ZENITH_OFFICIAL = 90.83333333333333d;

    /*90.83333333333333*/
    public enum CalculationType {
        Sunrise,
        Sunset
    }

    public static double sunrise(Calendar calendar, LatLonPoint loc, double zenith) {
        return sunriseOrSunset(calendar, loc, zenith, CalculationType.Sunrise);
    }

    public static double sunset(Calendar calendar, LatLonPoint loc, double zenith) {
        return sunriseOrSunset(calendar, loc, zenith, CalculationType.Sunset);
    }

    public static double dayLength(double rise, double set) {
        if (set >= 0.0d && rise >= 0.0d) {
            double dayLength = set - rise;
            if (dayLength < 0.0d) {
                return dayLength + 24.0d;
            }
            return dayLength;
        } else if (set < -1.5d || rise < -1.5d) {
            return 0.0d;
        } else {
            return 24.0d;
        }
    }

    public static double sunriseOrSunset(Calendar calendar, LatLonPoint loc, double zenith, CalculationType calculation) {
        if (calendar instanceof GregorianCalendar) {
            return sunriseOrSunset((GregorianCalendar) calendar, loc, zenith, calendar.getTimeZone(), calculation);
        }
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTimeZone(calendar.getTimeZone());
        cal.setTimeInMillis(calendar.getTimeInMillis());
        return sunriseOrSunset(cal, loc, zenith, cal.getTimeZone(), calculation);
    }

    private static double sunriseOrSunset(GregorianCalendar calendarDate, LatLonPoint loc, double zenith, TimeZone timeZone, CalculationType calculation) {
        double t;
        int N = calendarDate.get(6);
        double lngHour = loc.getLongitude() / 15.0d;
        if (calculation == CalculationType.Sunrise) {
            t = ((double) N) + ((6.0d - lngHour) / 24.0d);
        } else {
            t = ((double) N) + ((18.0d - lngHour) / 24.0d);
        }
        double M = (0.9856d * t) - 3.289d;
        double L = mod((((1.916d * Math.sin(Math.toRadians(M))) + M) + (0.02d * Math.sin(Math.toRadians(2.0d * M)))) + 282.634d, 360.0d);
        double RA = mod(Math.toDegrees(Math.atan(0.91764d * Math.tan(Math.toRadians(L)))), 360.0d);
        RA = (RA + ((Math.floor(L / 90.0d) * 90.0d) - (Math.floor(RA / 90.0d) * 90.0d))) / 15.0d;
        double sinDec = 0.39782d * Math.sin(Math.toRadians(L));
        double cosH = (Math.cos(Math.toRadians(zenith)) - (Math.sin(Math.toRadians(loc.getLatitude())) * sinDec)) / (Math.cos(Math.toRadians(loc.getLatitude())) * Math.cos(Math.asin(sinDec)));
        if (cosH > 1.0d) {
            return -2.0d;
        }
        if (cosH < -1.0d) {
            return -1.0d;
        }
        double H;
        if (calculation == CalculationType.Sunrise) {
            H = 360.0d - Math.toDegrees(Math.acos(cosH));
        } else {
            H = Math.toDegrees(Math.acos(cosH));
        }
        double UT = mod(((((H / 15.0d) + RA) - (0.06571d * t)) - 6.622d) - lngHour, 24.0d);
        int UThour = (int) UT;
        Date time = new Date(calendarDate.get(1) - 1900, calendarDate.get(2), calendarDate.get(5), UThour, (int) ((UT - ((double) UThour)) * 60.0d), 0);
        double localT = UT + (((double) timeZone.getOffset(time.getTime())) / 3600000.0d);
        if (localT < 0.0d) {
            time.setTime(time.getTime() + 86400000);
            localT = UT + (((double) timeZone.getOffset(time.getTime())) / 3600000.0d);
        } else if (localT >= 24.0d) {
            time.setTime(time.getTime() - 86400000);
            localT = UT + (((double) timeZone.getOffset(time.getTime())) / 3600000.0d);
        }
        return mod(localT, 24.0d);
    }

    private static double mod(double x, double y) {
        double result = x % y;
        if (result < 0.0d) {
            return result + y;
        }
        return result;
    }

}
