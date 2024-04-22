package com.mvrc.viewapp.di

import android.app.Application
import android.content.Context
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.mvrc.viewapp.R
import com.mvrc.viewapp.core.Constants
import com.mvrc.viewapp.domain.use_case.validation.ValidateEmail
import com.mvrc.viewapp.domain.use_case.validation.ValidatePassword
import com.mvrc.viewapp.domain.use_case.validation.ValidateRepeatedPassword
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

/**
 * Dagger Hilt module for providing dependencies related to authentication within the ViewModel component.
 * This module includes methods for providing the Google Sign-In client, sign-in and sign-up requests,
 * Google Sign-In options, and instances of classes responsible for validating email, password, and repeated password.
 *
 * @see [AuthModule.provideOneTapClient]
 * @see [AuthModule.provideSignInRequest]
 * @see [AuthModule.provideSignUpRequest]
 * @see [AuthModule.provideGoogleSignInOptions]
 * @see [AuthModule.provideValidateEmail]
 * @see [AuthModule.provideValidatePassword]
 * @see [AuthModule.provideValidateRepeatedPassword]
 */
@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

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
     * Provides the sign-in request for one-tap sign-in.
     *
     * @param app The application instance.
     * @return The sign-in request.
     */
    @Provides
    @Singleton
    @Named(Constants.SIGN_IN_REQUEST)
    fun provideSignInRequest(
        app: Application
    ) = BeginSignInRequest.builder()
        .setGoogleIdTokenRequestOptions(
            BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                .setServerClientId(app.getString(R.string.web_client_id))
                .setFilterByAuthorizedAccounts(true)
                .build()
        )
        .setAutoSelectEnabled(true)
        .build()

    /**
     * Provides the sign-up request for one-tap sign-in.
     *
     * @param app The application instance.
     * @return The sign-up request.
     */
    @Provides
    @Singleton
    @Named(Constants.SIGN_UP_REQUEST)
    fun provideSignUpRequest(
        app: Application
    ) = BeginSignInRequest.builder()
        .setGoogleIdTokenRequestOptions(
            BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                .setServerClientId(app.getString(R.string.web_client_id))
                .setFilterByAuthorizedAccounts(false)
                .build()
        )
        .build()

    /**
     * Provides the Google Sign-In options.
     *
     * @param app The application instance.
     * @return The Google Sign-In options.
     */
    @Provides
    @Singleton
    fun provideGoogleSignInOptions(
        app: Application
    ) = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(app.getString(R.string.web_client_id))
        .requestEmail()
        .build()

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