package com.app.partyzone.seller.ui.screen.home

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.PostAdd
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import coil.compose.AsyncImage
import com.app.partyzone.core.domain.SellerPost
import com.app.partyzone.design_system.composable.PzChip
import com.app.partyzone.design_system.composable.PzIconButton
import com.app.partyzone.design_system.theme.Theme
import com.app.partyzone.design_system.theme.brush
import com.app.partyzone.seller.R
import com.app.partyzone.seller.ui.composable.ErrorView
import com.app.partyzone.seller.ui.navigation.Screen
import com.app.partyzone.seller.ui.util.ComposableLifecycle
import com.app.partyzone.seller.ui.util.EventHandler

@Composable
fun HomeScreen(homeViewModel: HomeViewModel = hiltViewModel()) {
    val state by homeViewModel.state.collectAsState()
    val posts by homeViewModel.posts.collectAsState()

    ComposableLifecycle { _, event ->
        if (event == Lifecycle.Event.ON_RESUME)
            homeViewModel.hasNotifications()
    }

    EventHandler(effects = homeViewModel.effect) { effect, navController ->
        when (effect) {

            is HomeEffect.NavigateToNotification -> {
                navController.navigate(Screen.Notification)
            }

            else -> {}
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Theme.colors.primary)
    ) {
        AnimatedVisibility(visible = state.isLoading) {
            Column(
                Modifier
                    .fillMaxSize()
                    .align(Alignment.Center),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally),
                    color = Theme.colors.contentPrimary,
                )
            }
        }
        AnimatedVisibility(visible = state.isLoading.not() && state.error != null) {
            ErrorView(
                error = state.error,
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentHeight()
                    .align(Alignment.Center),
                onClickRetry = homeViewModel::onClickRetry
            )
        }
        AnimatedVisibility(visible = state.isLoading.not() && state.error.isNullOrEmpty()) {
            HomeContent(
                name = state.sellerState.name,
                onClickNotification = homeViewModel::onClickNotification,
                hasNotifications = state.hasNotifications,
                posts = posts,
                onCreatePost = {}
            )
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
private fun HomeContent(
    name: String,
    hasNotifications: Boolean,
    posts: List<SellerPost>,
    onCreatePost: () -> Unit,
    onClickNotification: () -> Unit,
) {
    Scaffold(floatingActionButton = {
        FloatingActionButton(
            onClick = onCreatePost,
            containerColor = Theme.colors.secondary,
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.PostAdd,
                contentDescription = "Create Post",
                tint = Theme.colors.primary
            )
        }
    }) { p ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Theme.colors.primary)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.welcome),
                        color = Theme.colors.contentPrimary,
                        style = Theme.typography.titleLarge
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = com.app.partyzone.design_system.R.drawable.user_icon),
                            contentDescription = stringResource(R.string.profile_icon),
                            modifier = Modifier
                                .size(24.dp)
                                .graphicsLayer(alpha = 0.99f)
                                .drawWithCache {
                                    onDrawWithContent {
                                        drawContent()
                                        drawRect(brush, blendMode = BlendMode.SrcAtop)
                                    }
                                }
                        )
                        Text(
                            text = name,
                            color = Theme.colors.contentSecondary,
                            style = Theme.typography.title
                        )
                    }
                }
                PzIconButton(
                    painter = painterResource(id = com.app.partyzone.design_system.R.drawable.notification_icon),
                    onClick = onClickNotification,
                    tint = if (hasNotifications) Color(0xFFFB0160) else Theme.colors.contentPrimary,
                    hasNotifications = hasNotifications
                ) {
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
            Spacer(modifier = Modifier.height(24.dp))
            Spacer(modifier = Modifier.width(16.dp))

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(posts) { post ->
                    PostItem(post)
                }
            }
        }
    }
}

@Composable
fun PostItem(post: SellerPost) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Post Images
            LazyRow {
                items(post.images) { imageUrl ->
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .size(200.dp)
                            .padding(4.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )
                }
            }

            // Caption
            Text(
                text = post.description,
                style = Theme.typography.body,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            // Likes/Comments
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {

                IconButton(onClick = { /* Handle comment */ }) {
                    Icon(
                        imageVector = Icons.Filled.Comment,
                        contentDescription = "Comment",
                        tint = Theme.colors.contentPrimary
                    )
                }
            }
        }
    }
}