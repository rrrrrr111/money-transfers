package ru.roman.app;

import com.google.inject.AbstractModule;
import ru.roman.app.account.MoneyTransferService;
import ru.roman.app.account.MoneyTransferServiceImpl;

/**
 * Guice config
 */
public class GuiceConfig extends AbstractModule {

    @Override
    protected void configure() {
        bind(MoneyTransferService.class).to(MoneyTransferServiceImpl.class);

    }
}