package com.example.darren.permissionssample

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.example.darren.permissionssample.ui.theme.PermissionsSampleTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.shouldShowRationale

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PermissionsSampleTheme {
                val permissionsState = rememberMultiplePermissionsState(
                    permissions = listOf(
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.CAMERA
                    )
                )

                val lifecycleOwner = LocalLifecycleOwner.current
                DisposableEffect(
                    key1 = lifecycleOwner,
                    effect = {
                        val observer = LifecycleEventObserver {
                            _, event ->
                            if (event == Lifecycle.Event.ON_START){
                                permissionsState.launchMultiplePermissionRequest()
                            }
                        }
                        lifecycleOwner.lifecycle.addObserver(observer)
                        onDispose {
                            lifecycleOwner.lifecycle.removeObserver(observer)
                        }
                    }
                )

                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    permissionsState.permissions.forEach {
                        perm ->
                        when(perm.permission){
                            Manifest.permission.CAMERA ->{
                                when{
                                    perm.status.isGranted ->{
                                        Text(text = "Camera permission accepted")
                                    }
                                    perm.status.shouldShowRationale ->{
                                        Text(text = "Camera permission is needed")
                                    }
                                    !perm.status.isGranted && !perm.status.shouldShowRationale ->{
                                        Text(text = "Camera permission is denied")
                                    }
                                }
                            }
                            Manifest.permission.RECORD_AUDIO ->{
                                when{
                                    perm.status.isGranted ->{
                                        Text(text = "Record audio permission accepted")
                                    }
                                    perm.status.shouldShowRationale ->{
                                        Text(text = "Record audio permission is needed")
                                    }
                                    !perm.status.isGranted && !perm.status.shouldShowRationale ->{
                                        Text(text = "Record audio permission is denied")
                                    }
                                }
                            }
                        }
                    }

                }


            }
        }
    }
}

