package com.mvrc.viewapp.core

/**
 * Object containing various constants used throughout the application.
 */
object Constants {

    // Firestore Collection References
    const val USERS_COLLECTION = "users"
    const val POSTS_COLLECTION = "posts"

    // Firestore User fields
    const val USERS_ID_FIELD = "id"
    const val USERS_USERNAME_FIELD = "username"
    const val USERS_EMAIL_FIELD = "email"
    const val USERS_PROFESSION_FIELD = "profession"
    const val USERS_PROFESSION_DEFAULT = "Hi! I am a new contributor!"
    const val USERS_PHOTO_URL_FIELD = "photo_url"
    const val USERS_CREATED_AT_FIELD = "created_at"
    const val USERS_TOPICS_FIELD = "topics"
    const val USERS_FOLLOWING_FIELD = "following"
    const val USERS_FOLLOWERS_FIELD = "followers"

    // Firestore Post fields
    const val POSTS_AUTHOR_ID_FIELD = "author_id"
    const val POSTS_AUTHOR_NAME_FIELD = "author_name"
    const val POSTS_AUTHOR_PHOTO_URL_FIELD = "author_photo_url"
    const val POSTS_ID_FIELD = "id"
    const val POSTS_TITLE_FIELD = "title"
    const val POSTS_SUBTITLE_FIELD = "subtitle"
    const val POSTS_BODY_FIELD = "body"
    const val POSTS_IMAGE_URL_FIELD = "image_url"
    const val POSTS_CREATED_AT_FIELD = "created_at"
    const val POSTS_TOPIC_FIELD = "topic"
    const val POSTS_READ_TIME_FIELD = "read_time"
    const val POSTS_BOOKMARKED_BY_FIELD = "bookmarked_by"

    const val NEWEST_FILTER = "Newest"

    // Limit number of posts retrieved from Firebase
    const val FOR_YOU_POSTS_LIMIT_RATE: Long = 2
    const val FOLLOWING_POSTS_LIMIT_RATE: Long = 3
    const val BOOKMARKED_POSTS_LIMIT_RATE: Long = 4
    const val SEARCHED_POSTS_LIMIT_RATE: Long = 6
    const val USER_POSTS_LIMIT_RATE: Long = 4
    const val MORE_POSTS_LIMIT_RATE: Long = 2

    // Min number of words in Post creation
    const val MIN_TITLE_WORDS = 3
    const val MIN_SUBTITLE_WORDS = 3
    const val MIN_BODY_WORDS = 20

    // Limit number of users retrieved from Firebase
    const val NUM_OF_MOST_FOLLOWED_USERS_TO_DISPLAY = 20

    // Named injections
    const val SIGN_IN_REQUEST = "signInRequest"
    const val SIGN_UP_REQUEST = "signUpRequest"

    // Screens
    const val AUTH_SCREEN = "Authentication"
    const val SIGN_IN_SCREEN = "Sign in"
    const val FORGOT_PASSWORD_SCREEN = "Forgot password"
    const val EMAIL_SENT_SCREEN = "Email sent"
    const val SIGN_UP_SCREEN = "Sign up"
    const val TOPIC_SELECTION_SCREEN = "Topic selection"
    const val USER_SELECTION_SCREEN = "User selection"
    const val LANDING_SCREEN = "Landing"
    const val LANDING_NAV_GRAPH = "Landing Nav Graph"
    const val HOME_SCREEN = "Home"
    const val SEARCH_SCREEN = "Search"
    const val ADD_POST_SCREEN = "Add Post"
    const val BOOKMARKS_SCREEN = "Bookmark"
    const val SETTINGS_SCREEN = "Settings"
    const val POST_SCREEN = "Post"
    const val USER_SCREEN = "User"
    const val EDIT_PROFILE_SCREEN = "Edit Profile"
    const val POST_ARGUMENT = "postId"
    const val USER_ARGUMENT = "userId"
    const val COMING_FROM_USER_SCREEN_ARGUMENT = "comingFromUserScreen"

    const val DEBUG_TAG = "Debug"
}

/**
 * Enum class representing different options for signing in.
 */
enum class SignInOption {
    Google,
    Email
}