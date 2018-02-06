package by.md.gornak.homework.util;


import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import by.md.gornak.homework.R;
import by.md.gornak.homework.db.DBService;
import by.md.gornak.homework.model.ApplicationDB;

public class Sorting {

    private static final String DATE = "0";
    private static final String AZ = "1";
    private static final String ZA = "2";
    private static final String START = "3";
    private static final String NONE = "4";

    protected static Context mContext;

    public static Comparator getComparable(Context context) {
        mContext = context;
        String type = Settings.getStringValue(context, R.string.pref_key_sorting);
        if (DATE.equals(type)) {
            return new DateComparator();
        } else if (AZ.equals(type)) {
            return new AComparator(true);
        } else if (ZA.equals(type)) {
            return new AComparator(false);
        } else if (START.equals(type)) {
            DBService dbService = new DBService(mContext);
            return new StartComparator(dbService.readAll());

        }
        return new NoneComparator();
    }

    static class DateComparator implements Comparator<ResolveInfo> {

        @Override
        public int compare(ResolveInfo a, ResolveInfo b) {
            try {
                long first = mContext
                        .getPackageManager()
                        .getPackageInfo(a.activityInfo.applicationInfo.packageName, 0)
                        .firstInstallTime;
                long second = mContext
                        .getPackageManager()
                        .getPackageInfo(b.activityInfo.applicationInfo.packageName, 0)
                        .firstInstallTime;

                return (int) (second - first);
            } catch (PackageManager.NameNotFoundException e) {
                return 0;
            }
        }
    }

    static class AComparator implements Comparator<ResolveInfo> {
        boolean isAZ;

        public AComparator(boolean isAZ) {
            this.isAZ = isAZ;
        }

        @Override
        public int compare(ResolveInfo a, ResolveInfo b) {
            int revert = (isAZ ? 1 : -1);
            return a.loadLabel(mContext.getPackageManager()).toString()
                    .compareTo(b.loadLabel(mContext.getPackageManager()).toString()) * revert;
        }
    }

    static class NoneComparator implements Comparator<ResolveInfo> {

        @Override
        public int compare(ResolveInfo a, ResolveInfo b) {
            return 1;
        }
    }

    static class StartComparator implements Comparator<ResolveInfo> {

        Map<String, ApplicationDB> mData;

        StartComparator(Map<String, ApplicationDB> data) {
            mData = data;
        }

        @Override
        public int compare(ResolveInfo a, ResolveInfo b) {
            ApplicationDB first = mData.get(a.activityInfo.applicationInfo.packageName);
            ApplicationDB second = mData.get(b.activityInfo.applicationInfo.packageName);
            int firstCount = first == null ? 0 : first.getFrequency();
            int secondCount = second == null ? 0 : second.getFrequency();
            return secondCount - firstCount;
        }
    }
}
