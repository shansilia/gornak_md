package by.md.gornak.homework.util;


import android.content.Context;
import android.content.pm.PackageManager;

import java.util.Comparator;

import by.md.gornak.homework.R;
import by.md.gornak.homework.model.ApplicationDB;

public class Sorting {

    public static final String DATE = "0";
    public static final String AZ = "1";
    public static final String ZA = "2";
    public static final String START = "3";
    public static final String NONE = "4";


    public static Comparator getComparable(Context context) {
        String type = Settings.getStringValue(context, R.string.pref_key_sorting);
        if (DATE.equals(type)) {
            return new DateComparator(context);
        } else if (AZ.equals(type)) {
            return new AComparator(context, true);
        } else if (ZA.equals(type)) {
            return new AComparator(context, false);
        } else if (START.equals(type)) {
            return new StartComparator();
        }
        return new NoneComparator();
    }

    static class DateComparator implements Comparator<ApplicationDB> {

        private Context mContext;

        public DateComparator(Context context) {
            mContext = context;
        }

        @Override
        public int compare(ApplicationDB a, ApplicationDB b) {
            try {
                long first = mContext
                        .getPackageManager()
                        .getPackageInfo(a.getAppPackage(), 0)
                        .firstInstallTime;
                long second = mContext
                        .getPackageManager()
                        .getPackageInfo(b.getAppPackage(), 0)
                        .firstInstallTime;

                if(second > first) {
                    return 1;
                } else if(first > second) {
                    return -1;
                } else {
                    return 0;
                }
            } catch (PackageManager.NameNotFoundException e) {
                return 0;
            }
        }
    }

    static class AComparator implements Comparator<ApplicationDB> {
        boolean isAZ;
        private Context mContext;

        public AComparator(Context context, boolean isAZ) {
            this.isAZ = isAZ;
            mContext = context;
        }

        @Override
        public int compare(ApplicationDB a, ApplicationDB b) {
            int revert = (isAZ ? 1 : -1);
            return a.getInfo().loadLabel(mContext.getPackageManager()).toString()
                    .compareTo(b.getInfo().loadLabel(mContext.getPackageManager()).toString()) * revert;
        }
    }

    static class NoneComparator implements Comparator<ApplicationDB> {

        @Override
        public int compare(ApplicationDB a, ApplicationDB b) {
            return 1;
        }
    }

    static class StartComparator implements Comparator<ApplicationDB> {

        @Override
        public int compare(ApplicationDB a, ApplicationDB b) {
            return b.getFrequency() - a.getFrequency();
        }
    }
}
