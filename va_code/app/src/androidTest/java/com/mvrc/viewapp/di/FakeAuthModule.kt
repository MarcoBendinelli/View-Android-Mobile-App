package com.mvrc.viewapp.di

import android.content.Context
import com.google.android.gms.auth.api.identity.Identity
import com.mvrc.viewapp.R
import com.mvrc.viewapp.domain.use_case.validation.ValidateEmail
import com.mvrc.viewapp.domain.use_case.validation.ValidatePassword
import com.mvrc.viewapp.domain.use_case.validation.ValidateRepeatedPassword
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AuthModule::class]
)
object FakeAuthModule {

    /**
     * Provides the Google Identity Toolkit sign-in client for one-tap sign-in.
     *
     * @param context The application context.
     * @return The Google Identity Toolkit sign-in client.
     */
    @Provides
    @Singleton
    fun provideOneTapClient(
        @ApplicationContext
        context: Context
    ) = Identity.getSignInClient(context)

    /**
     * Provides an instance of [ValidateEmail] for validating email addresses.
     *
     * @param context The application context.
     * @return The email validation instance.
     */
    @Provides
    @Singleton
    fun provideValidateEmail(@ApplicationContext context: Context) = ValidateEmail(
        emailNotBlankMessage = context.resources.getString(R.string.email_not_blank),
        invalidEmailMessage = context.resources.getString(R.string.invalid_email_address)
    )

    /**
     * Provides an instance of [ValidatePassword] for validating passwords.
     *
     * @param context The application context.
     * @return The password validation instance.
     */
    @Provides
    @Singleton
    fun provideValidatePassword(@ApplicationContext context: Context) = ValidatePassword(
        atLeastCharactersMessage = context.resources.getString(R.string.password_at_least),
        atLeastDigitOrNumberMessage = context.resources.getString(R.string.password_letter_digit)
    )

    /**
     * Provides an instance of [ValidateRepeatedPassword] for validating repeated passwords.
     *
     * @param context The application context.
     * @return The repeated password validation instance.
     */
    @Provides
    @Singleton
    fun provideValidateRepeatedPassword(@ApplicationContext context: Context) =
        ValidateRepeatedPassword(
            passwordsDoNotMatchMessage = context.resources.getString(R.string.passwords_not_match)
        )
}