package com.github.xe11.rvrside.search.di

import android.content.Context
import com.github.xe11.rvrside.core.di.ApplicationComponent
import com.github.xe11.rvrside.core.di.ScreenScope
import com.github.xe11.rvrside.search.ui.SearchActivity
import dagger.BindsInstance
import dagger.Component
import dagger.Component.Factory

@Component(
    dependencies = [
        ApplicationComponent::class,
    ],
)
@ScreenScope
internal interface SearchScreenComponent {

    fun inject(activity: SearchActivity)

    @Factory
    interface ComponentFactory {

        fun create(
            @BindsInstance context: Context,
            appComponent: ApplicationComponent,
        ): SearchScreenComponent
    }

    companion object {

        fun get(context: Context): SearchScreenComponent {
            val applicationComponent = ApplicationComponent.formContext(context)

            return DaggerSearchScreenComponent.factory()
                .create(
                    context = context,
                    appComponent = applicationComponent
                )
        }
    }
}
