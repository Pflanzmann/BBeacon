package com.bbeacon.dagger2_injection.setup;

import android.app.Application;

import com.bbeacon.BBeaconApplication;
import com.bbeacon.dagger2_injection.modules.android.ActivityBuilderModule;
import com.bbeacon.dagger2_injection.bindings.BackendBindings;
import com.bbeacon.dagger2_injection.modules.android.FragmentBuilderModule;
import com.bbeacon.dagger2_injection.bindings.ManagerBindings;
import com.bbeacon.dagger2_injection.bindings.StorageBindings;
import com.bbeacon.dagger2_injection.modules.UtilityModule;
import com.bbeacon.dagger2_injection.modules.android.ViewModelFactoryModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;


@Singleton
@Component(
        modules = {
                AndroidSupportInjectionModule.class,

                ActivityBuilderModule.class,
                FragmentBuilderModule.class,
                ViewModelFactoryModule.class,

                BackendBindings.class,
                StorageBindings.class,
                ManagerBindings.class,

                UtilityModule.class
        }
)
public interface AppComponent extends AndroidInjector<BBeaconApplication> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }

}
