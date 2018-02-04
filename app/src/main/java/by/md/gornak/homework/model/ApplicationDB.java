package by.md.gornak.homework.model;


public class ApplicationDB {
    private String appPackage;
    private boolean isFavourite;
    private int frequency;

    public ApplicationDB(String appPackage, boolean isFavourite, int frequency) {
        this.appPackage = appPackage;
        this.isFavourite = isFavourite;
        this.frequency = frequency;
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
}
