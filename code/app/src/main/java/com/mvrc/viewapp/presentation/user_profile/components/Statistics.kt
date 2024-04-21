package com.mvrc.viewapp.presentation.user_profile.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.mvrc.viewapp.R
import com.mvrc.viewapp.core.ScreenUtils.Companion.rh
import com.mvrc.viewapp.core.ScreenUtils.Companion.rw
import com.mvrc.viewapp.core.Utils.abbreviateNumber
import com.mvrc.viewapp.presentation.theme.UiConstants.openSansSemiBold

/**
 * [Composable] to display user statistics such as number of followers, following, and posts.
 *
 * @param numOfFollowers The number of followers.
 * @param numOfFollowing The number of following.
 * @param numOfPosts The number of posts.
 */
@Composable
fun Statistics(numOfFollowers: Int, numOfFollowing: Int, numOfPosts: Int) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(
            space = 40.rw(),
            alignment = Alignment.CenterHorizontally
        )
    ) {
        ColumnStat(topStat = numOfFollowers, bottomStat = stringResource(id = R.string.followers))
        ColumnStat(topStat = numOfFollowing, bottomStat = stringResource(id = R.string.following))
        ColumnStat(topStat = numOfPosts, bottomStat = stringResource(id = R.string.posts))
    }
}

/**
 * Column of statistics with a numeric value and a label.
 *
 * @param topStat The numeric value to be displayed.
 * @param bottomStat The label for the statistic.
 */
@Composable
fun ColumnStat(topStat: Int, bottomStat: String) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = abbreviateNumber(topStat),
            style = MaterialTheme.typography.headlineSmall.copy(
                fontFamily = openSansSemiBold,
                color = MaterialTheme.colorScheme.secondary
            )
        )

        Spacer(modifier = Modifier.height(10.rh()))

        Text(
            text = bottomStat,
            style = MaterialTheme.typography.titleSmall
        )
    }
}