package com.mvrc.viewapp.testUtils

import com.google.firebase.Timestamp
import com.mvrc.viewapp.data.model.ViewPost
import com.mvrc.viewapp.data.model.ViewTopic
import com.mvrc.viewapp.data.model.ViewUser
import okhttp3.internal.immutableListOf

object Stubs {
    val user = ViewUser(
        id = "user123",
        email = "fake@example.com",
        username = "Fake User",
        profession = "Developer",
        photoUrl = "",
        topics = immutableListOf("Music"),
        following = emptyList(),
        followers = emptyList(),
        createdAt = Timestamp.now()
    )

    val post1 = ViewPost(
        authorId = "author123",
        authorName = "Johnny Fresh",
        authorPhotoUrl = "",
        id = "post1",
        title = "The Healing Power of Music: How Melodies Can Soothe Your Soul",
        subtitle = "Exploring the profound impact of music on our emotional well-being",
        body = """
        Music has been an integral part of human culture since ancient times, serving not only as entertainment but also as a powerful tool for expression and healing. From tribal chants to symphonies, the melodies that resonate within us have the ability to evoke a wide range of emotions and transcend language barriers.
        Scientific research has shed light on the therapeutic effects of music, revealing its ability to reduce stress, alleviate pain, and improve mood. Whether it's the calming notes of a classical piece or the energizing rhythm of a lively tune, music has the remarkable power to uplift our spirits and bring solace to our troubled minds.
        In times of hardship and adversity, music serves as a beacon of hope, providing comfort and strength to those in need. It has the uncanny ability to evoke memories, transport us to distant places, and connect us with others on a profound level.
        Moreover, music therapy has emerged as a recognized form of treatment for various mental health conditions, including depression, anxiety, and PTSD. Trained therapists use music interventions to address emotional, cognitive, and social needs, empowering individuals to navigate their inner struggles and find inner peace.
        As we navigate the complexities of life, let us not underestimate the transformative power of music. Whether it's a simple melody or a symphonic masterpiece, the healing touch of music has the potential to soothe our souls and uplift our spirits, reminding us of the beauty and resilience of the human spirit.
    """,
        imageUrl = "",
        createdAt = Timestamp.now(),
        topic = ViewTopic(
            topicName = "Music"
        ),
        readTime = "7 minutes",
        bookmarkedBy = emptyList()
    )
}