package com.helloworld.service;

import eydsh.winter.annotations.Injectable;

@Injectable
public class Validator {
    public Validator () {

    }

    public boolean validate() {
        System.out.println("validated");

        return true;
    }
}
