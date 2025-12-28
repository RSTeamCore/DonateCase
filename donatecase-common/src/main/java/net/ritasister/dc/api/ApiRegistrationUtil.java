package net.ritasister.dc.api;

import net.ritasister.dc.DonateCaseApiProvider;

import java.lang.reflect.Method;

public final class ApiRegistrationUtil {

    private static final Method REGISTER;
    private static final Method UNREGISTER;

    private ApiRegistrationUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    static {
        try {
            REGISTER = DonateCaseProvider.class.getDeclaredMethod("register", DonateCase.class);
            REGISTER.setAccessible(true);

            UNREGISTER = DonateCaseProvider.class.getDeclaredMethod("unregister");
            UNREGISTER.setAccessible(true);
        } catch (NoSuchMethodException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static void registerProvider(DonateCaseApiProvider donateCaseApiProvider) {
        try {
            REGISTER.invoke(null, donateCaseApiProvider);
        } catch (Exception e) {
            e.fillInStackTrace();
        }
    }

    public static void unregisterProvider() {
        try {
            UNREGISTER.invoke(null);
        } catch (Exception e) {
            e.fillInStackTrace();
        }
    }

}
