package com.example.floatingwindowapp;

import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;

/**
 * Holder for the Android {@link Application} context object used by Crudolib.  This should be
 * used only by infrastructure components which offer process global singletons that would
 * otherwise need to access the root context (Analytics, Network stack, etc).
 * <p />
 * Do not use this class as a replacement for specialized contexts like fragment, activity,
 * service, etc!
 * <p />
 * Testing code using this requires either PowerMock or manually calling set with
 * a Robolectric context instead.  Note however that it is also possible to organize infra
 * code such that you can test the core infra independent of its configuration inside Crudo.
 * See {@code CrudoNetInstance} for an example of this clear configuration separation, with
 * {@code CrudoNet} being the main infra entry point that can be easily unit tested
 * independent of the Crudo app context.
 */
public class AppContext {

    private static volatile Application sApplication;
    private static volatile Context sContext;

    private AppContext() {
    }

    public static void set(Application context) {
        if (context == null) {
            throw new IllegalArgumentException("context cannot be null");
        }
        sApplication = context;
        sContext = new ContextWrapper(sApplication) {
            @Override
            public Context getApplicationContext() {
                return this;
            }
        };
    }

    /*
     * Return the app context needed in some places.
     * In case it will be used in the early start up when getApplicationContext() returns null,
     * A ContextWrapper that wrapped the App Context is returned
     */
    public static Context get() {
        Context context = sContext;
        if (context == null) {
            throw new IllegalStateException("AppContext.set has not been invoked");
        }
        return context;
    }

    // Return the application instance
    public static Application getApplication() {
        Application application = sApplication;
        if (application == null) {
            throw new IllegalStateException("AppContext.set has not been invoked");
        }
        return application;
    }
}
