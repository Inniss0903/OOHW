package services;

import java.util.List;

public class ServicesTest {

    public static void main(String[] args) {
        AppPreference appPreference = new AppPreference();
        appPreference.changeInitStation(false);
        appPreference.export2XML();
    }
}
