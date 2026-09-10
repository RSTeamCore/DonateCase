package net.ritasister.dc.plugin.loader;

import net.ritasister.dc.DonateCasePaperPlugin;
import net.ritasister.dc.api.handler.Handler;
import net.ritasister.dc.api.handler.LoadHandlers;

import java.util.List;

public final class DCLoaderHandlers implements LoadHandlers<DonateCasePaperPlugin> {

    private final List<Handler<?>> handlers;

    public DCLoaderHandlers(List<Handler<?>> handlers) {
        this.handlers = handlers;
    }

    @Override
    public void loadHandler(DonateCasePaperPlugin plugin) {
        handlers.forEach(Handler::handle);
    }
}
