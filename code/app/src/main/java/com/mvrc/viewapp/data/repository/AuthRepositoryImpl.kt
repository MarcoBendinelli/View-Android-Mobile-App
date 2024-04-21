package com.mvrc.viewapp.data.repository

import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.Timestamp
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mvrc.viewapp.core.Constants.SIGN_IN_REQUEST
import com.mvrc.viewapp.core.Constants.SIGN_UP_REQUEST
import com.mvrc.viewapp.core.Constants.USERS_COLLECTION
import com.mvrc.viewapp.core.Constants.USERS_PROFESSION_DEFAULT
import com.mvrc.viewapp.data.cache.ViewUserCache
import com.mvrc.viewapp.data.model.Response
import com.mvrc.viewapp.data.model.Response.Failure
import com.mvrc.viewapp.data.model.Response.Success
import com.mvrc.viewapp.data.model.ViewUser
import com.mvrc.viewapp.domain.repository.AuthRepository
import com.mvrc.viewapp.domain.repository.OneTapSignInResponse
import com.mvrc.viewapp.domain.repository.SignInWithGoogleResponse
import com.mvrc.viewapp.domain.repository.SignOutResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Named

/**
 * Implementation of [AuthRepository] for handling authentication operations.
 *
 * @property auth Firebase authentication instance.
 * @property oneTapClient Google Sign-In client for one-tap sign-in.
 * @property signInRequest Configuration for one-tap sign-in.
 * @property signUpRequest Configuration for one-tap sign-up.
 * @property firestore Firebase Firestore database instance.
 * @property viewUserCache The [ViewUserCache] to save the user_profile data.
 */
class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private var oneTapClient: SignInClient,
    @Named(SIGN_IN_REQUEST)
    private var signInRequest: BeginSignInRequest,
    @Named(SIGN_UP_REQUEST)
    private var signUpRequest: BeginSignInRequest,
    private val firestore: FirebaseFirestore,
    private val viewUserCache: ViewUserCache
) : AuthRepository {

    private val _isFirstGoogleLogin = MutableStateFlow(true)
    override val isFirstGoogleLoginStateFlow: StateFlow<Boolean> = _isFirstGoogleLogin.asStateFlow()

    override suspend fun oneTapSignInWithGoogle(): OneTapSignInResponse {
        return try {
            val signInResult = oneTapClient.beginSignIn(signInRequest).await()
            Success(signInResult)
        } catch (e: Exception) {
            try {
                val signUpResult = oneTapClient.beginSignIn(signUpRequest).await()
                Success(signUpResult)
            } catch (e: Exception) {
                Failure(e)
            }
        }
    }

    override suspend fun firebaseSignInWithGoogle(
        googleCredential: AuthCredential
    ): SignInWithGoogleResponse {
        return try {
            val authResult = auth.signInWithCredential(googleCredential).await()
            val isNewUser = authResult.additionalUserInfo?.isNewUser ?: false
            if (isNewUser) {
                val response = addUserToFirestore()
                _isFirstGoogleLogin.value = true
                return response
            } else {
                _isFirstGoogleLogin.value = false
            }
            Success(true)
        } catch (e: Exception) {
            Failure(e)
        }
    }

    /**
     * Adds the current authenticated user_profile to Firestore.
     *
     * The function retrieves the current authenticated user_profile, converts it to a custom User model
     * and stores the user_profile information in the Firestore database.
     *
     * @return [Response] indicating the success or failure of the operation.
     *   - If the operation succeeds, [Success] is returned with a `true` value.
     *   - If an exception occurs during the operation, [Failure] is returned with the exception.
     */
    private suspend fun addUserToFirestore() = runCatching {
        auth.currentUser?.apply {
            val viewUser = ViewUser(
                id = this.uid,
                email = this.email!!,
                username = this.displayName ?: this.email!!,
                profession = USERS_PROFESSION_DEFAULT,
                photoUrl = if (this.photoUrl != null) this.photoUrl.toString() else "",
                topics = emptyList(),
                following = emptyList(),
                followers = emptyList(),
                createdAt = Timestamp.now()
            )
            firestore.collection(USERS_COLLECTION).document(uid).set(viewUser.toMap()).await()
        }
        Success(true)
    }.getOrElse { e ->
        Failure(Exception(e.message))
    }

    override suspend fun firebaseSignUpWithEmailAndPassword(
        email: String, password: String
    ) = try {
        auth.createUserWithEmailAndPassword(email, password).await()
        addUserToFirestore()
    } catch (e: Exception) {
        Failure(e)
    }

    override suspend fun firebaseSignInWithEmailAndPassword(
        email: String, password: String
    ) = try {
        auth.signInWithEmailAndPassword(email, password).await()
        Success(true)
    } catch (e: Exception) {
        Failure(e)
    }

    override suspend fun sendPasswordResetEmail(email: String) = try {
        auth.sendPasswordResetEmail(email).await()
        Success(true)
    } catch (e: Exception) {
        Failure(e)
    }

    override fun getAuthState(viewModelScope: CoroutineScope) = callbackFlow {
        val authStateListener = FirebaseAuth.AuthStateListener { auth ->
            launch {
                updateCacheOnUserChange(auth)
            }
            trySend(auth.currentUser == null)
        }
        auth.addAuthStateListener(authStateListener)
        awaitClose {
            auth.removeAuthStateListener(authStateListener)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), auth.currentUser == null)

    override suspend fun signOut(): SignOutResponse {
        return try {
            oneTapClient.signOut().await()
            auth.signOut()
            Success(true)
        } catch (e: Exception) {
            Failure(e)
        }
    }

    companion object {
        fun fromFirebaseException(exception: Exception): String {
            val exceptionCode: String = try {
                exception.message.toString().split('[', ']')[1].filter { !it.isWhitespace() }
            } catch (e: Exception) {
                "Default"
            }
            return when (exceptionCode) {
                "INVALID_LOGIN_CREDENTIALS" -> "Invalid credentials."
                else -> exception.message.toString()
            }
        }
    }

    /**
     * Updates the cache when the user_profile changes.
     *
     * It retrieves the user_profile information from Firestore based on the provided [firebaseUser].
     * If the [firebaseUser] is null, an empty [ViewUser] is returned.
     * If the user_profile document exists in Firestore, a [ViewUser] is created from the Firestore snapshot.
     * If the user_profile document doesn't exist, a new [ViewUser] with default values is created.
     *
     * @param firebaseUser The Firebase user_profile whose information needs to be cached.
     */
    private suspend fun updateCacheOnUserChange(firebaseUser: FirebaseAuth?) {
        lateinit var user: ViewUser

        // If the Firebase user_profile is null, return an empty user_profile.
        if (firebaseUser == null) {
            user = ViewUser.empty()
        } else if (firebaseUser.currentUser == null) {
            user = ViewUser.empty()
        } else {
            // Retrieve the user_profile document reference from Firestore.
            val userDocRef = firestore
                .collection(USERS_COLLECTION)
                .document(firebaseUser.currentUser!!.uid)

            val docSnapshot = userDocRef.get().await()

            // If the user_profile document exists, create a [ViewUser] from the snapshot.
            user = if (docSnapshot.exists()) {
                ViewUser.fromFirestore(docSnapshot)
            } else {
                // If the user_profile document doesn't exist, create a new [ViewUser] with default values.
                ViewUser(
                    id = firebaseUser.uid!!,
                    email = firebaseUser.currentUser!!.email!!,
                    username = firebaseUser.currentUser!!.displayName
                        ?: firebaseUser.currentUser!!.email!!,
                    profession = "",
                    photoUrl = if (firebaseUser.currentUser!!.photoUrl != null) firebaseUser.currentUser!!.photoUrl.toString() else "",
                    topics = emptyList(),
                    following = emptyList(),
                    followers = emptyList(),
                    createdAt = Timestamp.now()
                )
            }
        }
        // Cache the user_profile data and return the user_profile.
        viewUserCache.write(value = user)
    }
}
