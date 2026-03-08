package com.helloworld;

import eydsh.winter.WinterApp;
import eydsh.winter.annotations.Winter;

@Winter
public class WinterApplication {
    static void main() {
        WinterApp winterApp = new WinterApp();

        winterApp.run(WinterApplication.class);
    }
}
