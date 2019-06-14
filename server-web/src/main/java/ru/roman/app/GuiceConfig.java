package ru.roman.app;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Scopes;
import ru.roman.app.account.MoneyTransferService;
import ru.roman.app.account.MoneyTransferServiceImpl;

/**
 * Guice config
 */
public class GuiceConfig extends AbstractModule {

    public static final Injector INJECTOR_INSTANCE = Guice.createInjector(new GuiceConfig());

    @Override
    protected void configure() {

        bind(MoneyTransferService.class).to(MoneyTransferServiceImpl.class).in(Scopes.SINGLETON);
    }
}