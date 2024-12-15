package com.github.xe11.rvrside.details.di

import android.content.Context
import com.github.xe11.rvrside.core.di.ApplicationComponent
import com.github.xe11.rvrside.core.di.ScreenScope
import com.github.xe11.rvrside.details.ui.MovieDetailsFragment
import dagger.BindsInstance
import dagger.Component
import dagger.Component.Factory

@Component(
    dependencies = [
        ApplicationComponent::class,
    ],
)
@ScreenScope
internal interface DetailsScreenComponent {

    fun inject(fragment: MovieDetailsFragment)

    @Factory
    interface ComponentFactory {

        fun create(
            @BindsInstance context: Context,
            appComponent: ApplicationComponent,
        ): DetailsScreenComponent
    }

    companion object {

        fun get(context: Context): DetailsScreenComponent {
            val applicationComponent = ApplicationComponent.formContext(context)

            return DaggerDetailsScreenComponent.factory()
                .create(
                    context = context,
                    appComponent = applicationComponent
                )
        }
    }
}
