// Generated by Dagger (https://dagger.dev).
package com.pkapps.punchclock.di;

import android.app.Application;
import com.pkapps.punchclock.feature_time_tracking.data.local.PunchClockDatabase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
@DaggerGenerated
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava"
})
public final class AppModule_ProvidePunchClockDatabaseFactory implements Factory<PunchClockDatabase> {
  private final Provider<Application> applicationProvider;

  public AppModule_ProvidePunchClockDatabaseFactory(Provider<Application> applicationProvider) {
    this.applicationProvider = applicationProvider;
  }

  @Override
  public PunchClockDatabase get() {
    return providePunchClockDatabase(applicationProvider.get());
  }

  public static AppModule_ProvidePunchClockDatabaseFactory create(
      Provider<Application> applicationProvider) {
    return new AppModule_ProvidePunchClockDatabaseFactory(applicationProvider);
  }

  public static PunchClockDatabase providePunchClockDatabase(Application application) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.providePunchClockDatabase(application));
  }
}
