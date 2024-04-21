package com.mvrc.viewapp.presentation.components

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged

/**
 * [Composable] for handling lazy list scrolling and triggers the [onLoadMore] callback when the last visible item
 * is close to the end of the list, with debouncing to avoid rapid multiple calls.
 *
 * @param lazyListState The LazyListState of the LazyColumn or LazyRow.
 * @param buffer The buffer to determine when to trigger the [onLoadMore] callback. Defaults to 1.
 * @param debounceTime The time in milliseconds to wait after the last emission before allowing the flow to proceed.
 * @param onLoadMore The callback to be invoked when more items need to be loaded.
 */
@OptIn(FlowPreview::class)
@Composable
fun LazyListScrollHandler(
    lazyListState: LazyListState,
    buffer: Int = 1,
    debounceTime: Long = 200, // Debounce time in milliseconds
    onLoadMore: () -> Unit
) {
    // Use derivedStateOf to automatically recompose when the calculated value changes.
    val loadMore = remember {
        derivedStateOf {
            val layoutInfo = lazyListState.layoutInfo
            // Get the total number of items in the list.
            val totalItems = layoutInfo.totalItemsCount

            // Calculates the index of the last visible item.
            // If there are visible items, it retrieves the index of the last visible item and adds 1.
            // If there are no visible items, it defaults to 0.
            val lastVisibleItemIndex = (layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0)

            // Check if the last visible item is close to the end of the list based on the buffer.
            lastVisibleItemIndex >= (totalItems - buffer)
        }
    }

    // LaunchedEffect to start a coroutine when loadMore value changes.
    LaunchedEffect(loadMore) {
        snapshotFlow { loadMore.value }
            .debounce(debounceTime) // Debounce the flow
            .distinctUntilChanged()
            .collect {
                // Invoke the callback when more items need to be loaded.
                if (loadMore.value) {
                    onLoadMore()
                }
            }
    }
}