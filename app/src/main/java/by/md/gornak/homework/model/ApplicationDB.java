package by.md.gornak.homework.model;


import android.content.pm.ResolveInfo;

public class ApplicationDB {

    private ResolveInfo info;
    private String appPackage;
    private boolean isFavourite = false;
    private int frequency;
    private boolean isDesktop;
    private int position;
    private String type;
    private String additionaly;
    private byte[] image;

    public enum TYPE {APP, PHONE}

    public ApplicationDB(ResolveInfo info) {
        this.info = info;
        this.appPackage = info.activityInfo.applicationInfo.packageName;
        this.frequency = 0;
        this.isDesktop = false;
        this.position = -1;
        type = TYPE.APP.toString();
    }

    public ApplicationDB(String appPackage, boolean isFavourite, int frequency, boolean isDesktop, int position, String type, String additionaly, byte[] image) {
        this.appPackage = appPackage;
        this.isFavourite = isFavourite;
        this.frequency = frequency;
        this.isDesktop = isDesktop;
        this.position = position;
        this.type = type;
        this.additionaly = additionaly;
        this.image = image;
    }

    public ApplicationDB(ResolveInfo info, String appPackage, int frequency,
                         boolean isDesktop, int position, String type) {
        this.info = info;
        this.appPackage = appPackage;
        this.frequency = frequency;
        this.isDesktop = isDesktop;
        this.position = position;
        this.type = type;
        this.additionaly = null;
        this.image = new byte[]{};
    }

    public ApplicationDB(String type, String appPackage, int position, String additionaly, byte[] image) {
        this.info = null;
        this.appPackage = appPackage;
        this.frequency = 0;
        this.isDesktop = true;
        this.position = position;
        this.type = type;
        this.additionaly = additionaly;
        this.image = image;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ApplicationDB that = (ApplicationDB) o;

        return appPackage.equals(that.appPackage);
    }

    @Override
    public int hashCode() {
        return appPackage.hashCode();
    }

    public String getAppPackage() {
        return appPackage;
    }

    public void setAppPackage(String appPackage) {
        this.appPackage = appPackage;
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public boolean isDesktop() {
        return isDesktop;
    }

    public void setDesktop(boolean desktop) {
        isDesktop = desktop;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int pos) {
        this.position = pos;
    }

    public ResolveInfo getInfo() {
        return info;
    }

    public void setInfo(ResolveInfo info) {
        this.info = info;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAdditionaly() {
        return additionaly;
    }

    public void setAdditionaly(String additionaly) {
        this.additionaly = additionaly;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
