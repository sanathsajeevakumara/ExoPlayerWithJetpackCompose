package com.sanath.simplevideoplayerwithjetpackcompose.presenter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FileOpen
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.media3.ui.PlayerView
import com.sanath.simplevideoplayerwithjetpackcompose.ui.theme.SimpleVideoPlayerWithJetpackComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SimpleVideoPlayerWithJetpackComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val viewModel = hiltViewModel<MainViewModel>()
                    val videoItem by viewModel.videoItems.collectAsState()
                    val selectVideoLauncher = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.GetContent(),
                        onResult = { uri ->
                            uri?.let {
                                viewModel.addVideoUri(uri)
                            }
                        }
                    )
                    var lifiecycle by remember {
                        mutableStateOf(Lifecycle.Event.ON_CREATE)
                    }
                    val lifecycleOwner = LocalLifecycleOwner.current
                    DisposableEffect(key1 = lifecycleOwner) {
                        val observer = LifecycleEventObserver { _, event ->
                            lifiecycle = event
                        }
                        lifecycleOwner.lifecycle.addObserver(observer)

                        onDispose {
                            lifecycleOwner.lifecycle.removeObserver(observer)
                        }
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        AndroidView(
                            factory = { context ->
                                PlayerView(context).also { playerView ->
                                    playerView.player = viewModel.player
                                }
                            },
                            update = {
                                when(lifiecycle) {
                                    Lifecycle.Event.ON_PAUSE -> {
                                        it.onPause()
                                        it.player?.pause()
                                    }
                                    Lifecycle.Event.ON_RESUME -> {
                                        it.onResume()
                                    }
                                    else -> Unit
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(16 / 9f)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        IconButton(onClick = {
                            selectVideoLauncher.launch("video/*")
                        }) {
                            Icon(
                                imageVector = Icons.Default.FileOpen,
                                contentDescription = "Select Videos"
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            items(videoItem) { item ->
                                Text(
                                    text = item.name,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            viewModel.playVideo(item.contentUri)
                                        }
                                        .padding(16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}