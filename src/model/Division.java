package model;

public class Division {
    int divId;
    String divName;
    int countryId;

    public Division(int divId, String divName, int countryId) {
        this.divId = divId;
        this.divName = divName;
        this.countryId = countryId;
    }

    public int getDivId() {
        return divId;
    }

    public void setDivId(int divId) {
        this.divId = divId;
    }

    public String getDivName() {
        return divName;
    }

    public void setDivName(String divName) {
        this.divName = divName;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    @Override
    public String toString() {
        return divName;
    }
}
