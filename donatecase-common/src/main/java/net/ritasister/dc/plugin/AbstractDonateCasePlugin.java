package net.ritasister.dc.plugin;

import net.ritasister.dc.DonateCaseApiProvider;
import net.ritasister.dc.api.ApiRegistrationUtil;
import net.ritasister.dc.api.DonateCase;
import net.ritasister.dc.api.logging.PluginLogger;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractDonateCasePlugin implements DonateCasePlugin {

    private DonateCaseApiProvider apiProvider;

    public final void load() {
        //Register WGRP API
        this.apiProvider = new DonateCaseApiProvider(this);
        this.apiProvider.ensureApiWasLoadedByPlugin();
        ApiRegistrationUtil.registerProvider(apiProvider);
        registerApiOnPlatform(this.apiProvider);
    }

    public final void unLoad() {
        // unregister api
        ApiRegistrationUtil.unregisterProvider();
    }

    @Override
    public final DonateCaseApiProvider getApiProvider() {
        return this.apiProvider;
    }

    @Override
    public @NotNull PluginLogger getLogger() {
        return getBootstrap().getPluginLogger();
    }

    protected abstract void registerApiOnPlatform(DonateCase api);

}
