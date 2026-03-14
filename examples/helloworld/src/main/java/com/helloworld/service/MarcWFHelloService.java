package com.helloworld.service;

import eydsh.winter.annotations.Injectable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Injectable
public class MarcWFHelloService {
    private final Logger LOGGER = LoggerFactory.getLogger(MarcWFHelloService.class);

    public MarcWFHelloService() {

    }

    public void sayHelloToMarcFromLogger() {
        LOGGER.info("YOOOOOO what's up marc");
    }
}
