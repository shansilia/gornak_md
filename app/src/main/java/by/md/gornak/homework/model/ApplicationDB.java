package by.md.gornak.homework.model;


public class ApplicationDB {
    private String appPackage;
    private boolean isFavourite;
    private int frequency;
    private boolean isDesktop;
    private int position;

    public ApplicationDB(String appPackage, boolean isFavourite, int frequency,
                         boolean isDesktop, int position) {
        this.appPackage = appPackage;
        this.isFavourite = isFavourite;
        this.frequency = frequency;
        this.isDesktop = isDesktop;
        this.position = position;
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
}
