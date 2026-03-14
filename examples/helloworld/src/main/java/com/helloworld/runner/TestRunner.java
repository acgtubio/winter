package com.helloworld.runner;

import com.helloworld.service.RandomService;
import com.helloworld.service.Validator;
import eydsh.winter.annotations.Inject;
import eydsh.winter.annotations.Injectable;
import eydsh.winter.common.WinterRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Injectable
public class TestRunner implements WinterRunner {
    private final Logger LOGGER = LoggerFactory.getLogger(TestRunner.class);
    private final RandomService service;
    private final Validator validator;

    @Inject
    public TestRunner(
            RandomService service,
            Validator validator
    ) {
        this.service = service;
        this.validator = validator;
    }

    @Override
    public void run() {
        String result = this.service.sayHello();
        boolean validatorResult = this.validator.validate();

        LOGGER.info(result);
        LOGGER.info("Validator result: {}", validatorResult);
    }
}
