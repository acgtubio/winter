package com.helloworld.service;

import eydsh.winter.annotations.Inject;
import eydsh.winter.annotations.Injectable;

@Injectable
public class RandomService {
    MarcWFHelloService marcHelloService;

    @Inject
    public RandomService(MarcWFHelloService marcHelloService) {
        this.marcHelloService = marcHelloService;
    }

    public String sayHello() {
        this.marcHelloService.sayHelloToMarcFromLogger();

        return "Hello";
    }
}
