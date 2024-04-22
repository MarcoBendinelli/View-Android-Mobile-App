package com.mvrc.viewapp.data.repository

import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.mvrc.viewapp.core.Constants.USERS_COLLECTION
import com.mvrc.viewapp.data.cache.ViewUserCache
import com.mvrc.viewapp.data.model.Response.Failure
import com.mvrc.viewapp.data.model.Response.Success
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * This test suite covers various scenarios for authentication operations
 * implemented in the [AuthRepositoryImpl] class.
 */
class AuthRepositoryImplTest {

    private lateinit var authRepository: AuthRepositoryImpl
    private val mockAuth: FirebaseAuth = mockk()
    private val mockOneTapClient: SignInClient = mockk()
    private val mockSignInRequest: BeginSignInRequest = mockk()
    private val mockSignUpRequest: BeginSignInRequest = mockk()
    private val mockFirestore: FirebaseFirestore = mockk()
    private val mockAuthCredential: AuthCredential = mockk()
    private val mockAuthResult: AuthResult = mockk()
    private val mockFirebaseUser: FirebaseUser = mockk {
        every { uid } returns "code"
        every { displayName } returns "Mario Pino"
        every { email } returns "mario@pino.com"
        every { photoUrl?.toString() } returns "https://example.com/marione.jpg"
    }
    private val email = "test@test.com"
    private val password = "password"
    private val viewUserCache = ViewUserCache()

    @Before
    fun setUp() {
        authRepository = AuthRepositoryImpl(
            mockAuth,
            mockOneTapClient,
            mockSignInRequest,
            mockSignUpRequest,
            mockFirestore,
            viewUserCache
        )
    }

    /**
     * Validates the behavior of [AuthRepositoryImpl.oneTapSignInWithGoogle]
     * when the sign-in with one-tap is successful.
     */
    @Test
    fun oneTapSignInWithGoogle_signIn_returnsSuccess() =
        runBlocking {
            // Set up
            val mockBeginSignInResult: BeginSignInResult = mockk()
            every {
                mockOneTapClient.beginSignIn(mockSignInRequest)
            } returns Tasks.forResult(mockBeginSignInResult)

            // Execute
            val result = authRepository.oneTapSignInWithGoogle()

            // Verify
            assertTrue(result is Success)
            assertEquals(mockBeginSignInResult, (result as Success).data)
        }

    /**
     * Validates the behavior of [AuthRepositoryImpl.oneTapSignInWithGoogle]
     * when sign-in with one-tap fails, but sign-up with one-tap is successful.
     */
    @Test
    fun oneTapSignInWithGoogle_signInException_signUp_returnsSuccess() =
        runBlocking {
            // Set up
            val mockBeginSignUpResult: BeginSignInResult = mockk()
            every {
                mockOneTapClient.beginSignIn(mockSignInRequest)
            }.throws(Exception())
            every {
                mockOneTapClient.beginSignIn(mockSignUpRequest)
            } returns Tasks.forResult(mockBeginSignUpResult)

            // Execute
            val result = authRepository.oneTapSignInWithGoogle()

            // Verify
            assertTrue(result is Success)
            assertEquals(mockBeginSignUpResult, (result as Success).data)
        }

    /**
     * Validates the behavior of [AuthRepositoryImpl.oneTapSignInWithGoogle]
     * when both sign-in and sign-up with one-tap fail.
     */
    @Test
    fun oneTapSignInWithGoogle_signInException_signUpException_returnsFailure() =
        runBlocking {
            // Set up
            every {
                mockOneTapClient.beginSignIn(mockSignInRequest)
            }.throws(Exception())
            every {
                mockOneTapClient.beginSignIn(mockSignUpRequest)
            }.throws(Exception())

            // Execute
            val result = authRepository.oneTapSignInWithGoogle()

            // Verify
            assertTrue(result is Failure)
        }

    /**
     * Validates the behavior of [AuthRepositoryImpl.firebaseSignInWithGoogle]
     * when the sign-in with Google credentials is successful.
     */
    @Test
    fun firebaseSignInWithGoogle_success_returnsSuccess() = runBlocking {
        // Set up
        every { mockAuth.signInWithCredential(mockAuthCredential) } returns Tasks.forResult(
            mockAuthResult
        )
        every { mockAuthResult.additionalUserInfo?.isNewUser } returns false

        // Execute
        val result = authRepository.firebaseSignInWithGoogle(mockAuthCredential)

        // Verify
        assertTrue(result is Success)
        assertEquals(true, (result as Success).data)
    }

    /**
     * Validates the behavior of [AuthRepositoryImpl.firebaseSignInWithGoogle]
     * when the sign-in with Google credentials is successful for a new user_profile.
     */
    @Test
    fun firebaseSignInWithGoogle_newUser_returnsSuccess() = runBlocking {
        // Set up
        every { mockAuth.currentUser } returns mockFirebaseUser
        every {
            mockFirestore.collection(USERS_COLLECTION).document(any()).set(any())
        } returns Tasks.forResult(null)
        every { mockAuth.signInWithCredential(mockAuthCredential) } returns Tasks.forResult(
            mockAuthResult
        )
        every { mockAuthResult.additionalUserInfo?.isNewUser } returns true

        // Execute
        val result = authRepository.firebaseSignInWithGoogle(mockAuthCredential)

        // Verify
        assertTrue(result is Success)
        assertEquals(true, authRepository.isFirstGoogleLoginStateFlow.value)
    }

    /**
     * Validates the behavior of [AuthRepositoryImpl.firebaseSignInWithGoogle]
     * when an exception occurs during the Google sign-in process.
     */
    @Test
    fun firebaseSignInWithGoogle_exception_returnsFailure() = runBlocking {
        // Set up
        every { mockAuth.signInWithCredential(mockAuthCredential) }.throws(Exception())

        // Execute
        val result = authRepository.firebaseSignInWithGoogle(mockAuthCredential)

        // Verify
        assertTrue(result is Failure)
    }

    /**
     * Validates the behavior of [AuthRepositoryImpl.firebaseSignUpWithEmailAndPassword]
     * when the sign-up with email and password in Firebase is successful,
     * resulting in a [Success] response.
     */
//    @Test
//    fun firebaseSignUpWithEmailAndPassword_success_returnsSuccess() = runBlocking {
//        val mockCollection = mockk<CollectionReference>()
//        val mockDocument = mockk<DocumentReference>()
//        val mockSetFunction = mockk<Task<Void>>()
//
//        // Set up
//        every { mockAuth.createUserWithEmailAndPassword(email, password) } returns Tasks.forResult(
//            null
//        )
//        every { mockAuth.currentUser }.returns(mockFirebaseUser)
//        every { mockFirestore.collection(any()) } returns mockCollection
//        every { mockCollection.document(any()) } returns mockDocument
//        every { mockDocument.set(any<Map<String, Any>>()) } returns mockSetFunction
//        coEvery { mockSetFunction.await() } returns mockk()
//
//        // Execute
//        val result = authRepository.firebaseSignUpWithEmailAndPassword(email, password)
//
//        // Verify
//        assertTrue(result is Success)
//        assertEquals(true, (result as Success).data)
//    }

    /**
     * Validates the behavior of [AuthRepositoryImpl.firebaseSignUpWithEmailAndPassword]
     * when an exception occurs during the sign-up with email and password in Firebase,
     * resulting in a [Failure] response.
     */
    @Test
    fun firebaseSignUpWithEmailAndPassword_exception_returnsFailure() = runBlocking {
        // Set up
        every {
            mockAuth.createUserWithEmailAndPassword(email, password)
        } throws Exception()

        // Execute
        val result = authRepository.firebaseSignUpWithEmailAndPassword(email, password)

        // Verify
        assertTrue(result is Failure)
    }

    /**
     * Validates the behavior of [AuthRepositoryImpl.firebaseSignInWithEmailAndPassword]
     * when the sign-in with email and password in Firebase is successful,
     * resulting in a [Success] response.
     */
    @Test
    fun firebaseSignInWithEmailAndPassword_success_returnsSuccess() = runBlocking {
        // Set up
        every { mockAuth.signInWithEmailAndPassword(email, password) } returns Tasks.forResult(null)

        // Execute
        val result = authRepository.firebaseSignInWithEmailAndPassword(email, password)

        // Verify
        assertTrue(result is Success)
        assertEquals(true, (result as Success).data)
    }

    /**
     * Validates the behavior of [AuthRepositoryImpl.firebaseSignInWithEmailAndPassword]
     * when an exception occurs during the sign-in with email and password in Firebase,
     * resulting in a [Failure] response.
     */
    @Test
    fun firebaseSignInWithEmailAndPassword_exception_returnsFailure() = runBlocking {
        // Set up
        every { mockAuth.signInWithEmailAndPassword(email, password) } throws Exception()

        // Execute
        val result = authRepository.firebaseSignInWithEmailAndPassword(email, password)

        // Verify
        assertTrue(result is Failure)
    }

    /**
     * Validates the behavior of [AuthRepositoryImpl.sendPasswordResetEmail]
     * when sending a password reset email in Firebase is successful,
     * resulting in a [Success] response.
     */
    @Test
    fun sendPasswordResetEmail_success_returnsSuccess() = runBlocking {
        // Set up
        every { mockAuth.sendPasswordResetEmail(email) } returns Tasks.forResult(null)

        // Execute
        val result = authRepository.sendPasswordResetEmail(email)

        // Verify
        assertTrue(result is Success)
        assertEquals(true, (result as Success).data)
    }

    /**
     * Validates the behavior of [AuthRepositoryImpl.sendPasswordResetEmail]
     * when an exception occurs during the password reset email sending in Firebase,
     * resulting in a [Failure] response.
     */
    @Test
    fun sendPasswordResetEmail_exception_returnsFailure() = runBlocking {
        // Set up
        every { mockAuth.sendPasswordResetEmail(email) } throws Exception()

        // Execute
        val result = authRepository.sendPasswordResetEmail(email)

        // Verify
        assertTrue(result is Failure)
    }

    /**
     * Validates the behavior of [AuthRepositoryImpl.getAuthState]
     * when the current user_profile in Firebase is null,
     * resulting in a true value for the authentication state.
     */
    @Test
    fun getAuthState_userIsNull_returnsTrue() = runBlocking {
        // Set up
        every { mockAuth.currentUser } returns null

        // Execute
        val result = authRepository.getAuthState(MainScope()).value

        // Verify
        assertTrue(result)
    }

    /**
     * Validates the behavior of [AuthRepositoryImpl.getAuthState]
     * when the current user_profile in Firebase is not null,
     * resulting in a false value for the authentication state.
     */
    @Test
    fun getAuthState_userIsNotNull_returnsFalse() = runBlocking {
        // Set up
        every { mockAuth.currentUser } returns mockFirebaseUser
        every { mockAuth.addAuthStateListener(any()) } returns Unit
        every { mockAuth.removeAuthStateListener(any()) } returns Unit

        // Execute
        val result = authRepository.getAuthState(MainScope()).value

        // Verify
        assertFalse(result)
    }

    /**
     * Validates the behavior of [AuthRepositoryImpl.signOut]
     * when signing out from both Firebase and Google's One Tap API is successful,
     * resulting in a [Success] response.
     */
    @Test
    fun signOut_success_returnsSuccess() = runBlocking {
        // Set up
        every { mockOneTapClient.signOut() } returns Tasks.forResult(null)
        every { mockAuth.signOut() } returns Unit

        // Execute
        val result = authRepository.signOut()

        // Verify
        assertTrue(result is Success)
        assertEquals(true, (result as Success).data)
    }

    /**
     * Validates the behavior of [AuthRepositoryImpl.signOut]
     * when signing out from both Firebase and Google's One Tap API is successful,
     * resulting in a [Failure] response.
     */
    @Test
    fun signOut_exception_returnsFailure() = runBlocking {
        // Set up
        every { mockOneTapClient.signOut() } throws Exception()

        // Execute
        val result = authRepository.signOut()

        // Verify
        assertTrue(result is Failure)
    }

    /**
     * Validates the behavior of [AuthRepositoryImpl.fromFirebaseException]
     * when the exception message indicates invalid login credentials.
     */
    @Test
    fun fromFirebaseException_withInvalidLoginCredentials_returnsInvalidCredentialsMessage() {
        // Set up
        val exceptionMessage = "Error [INVALID_LOGIN_CREDENTIALS] Something went wrong."

        // Execute
        val result = AuthRepositoryImpl.fromFirebaseException(Exception(exceptionMessage))

        // Verify
        assertEquals("Invalid credentials.", result)
    }

    /**
     * Validates the behavior of [AuthRepositoryImpl.fromFirebaseException]
     * when the exception message is not recognized.
     */
    @Test
    fun fromFirebaseException_withException_returnsDefault() {
        // Set up
        val exceptionMessage = "Error"

        // Execute
        val result = AuthRepositoryImpl.fromFirebaseException(Exception(exceptionMessage))

        // Verify
        assertEquals("Error", result)
    }
}