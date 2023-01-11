package com.konbini.internet

import android.content.Context
import android.net.ConnectivityManager
import android.net.LinkProperties
import android.net.Network
import android.net.NetworkCapabilities
import android.util.Log
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*


class Internet(private val context: Context): InternetService {

    companion object {
        const val TAG = "Internet"
    }

    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    override fun observe(): Flow<InternetService.Status> {
        return callbackFlow {
            val callback = object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    launch {
                        send(InternetService.Status.Available)
                        Log.e(TAG, InternetService.Status.Available.name)
                    }
                }

                override fun onLosing(network: Network, maxMsToLive: Int) {
                    super.onLosing(network, maxMsToLive)
                    launch {
                        send(InternetService.Status.Losing)
                        Log.e(TAG, InternetService.Status.Losing.name)
                    }
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    launch {
                        send(InternetService.Status.Lost)
                        Log.e(TAG, InternetService.Status.Lost.name)
                    }
                }

                override fun onUnavailable() {
                    super.onUnavailable()
                    launch {
                        send(InternetService.Status.Unavailable)
                        Log.e(TAG, InternetService.Status.Unavailable.name)
                    }
                }

                override fun onCapabilitiesChanged(
                    network: Network,
                    networkCapabilities: NetworkCapabilities
                ) {
                    super.onCapabilitiesChanged(network, networkCapabilities)
                    launch {
                        send(InternetService.Status.CapabilitiesChanged)
                        Log.e(TAG, InternetService.Status.CapabilitiesChanged.name)
                    }
                }

                override fun onLinkPropertiesChanged(
                    network: Network,
                    linkProperties: LinkProperties
                ) {
                    super.onLinkPropertiesChanged(network, linkProperties)
                    launch {
                        send(InternetService.Status.LinkPropertiesChanged)
                        Log.e(TAG, InternetService.Status.LinkPropertiesChanged.name)
                    }
                }

                override fun onBlockedStatusChanged(network: Network, blocked: Boolean) {
                    super.onBlockedStatusChanged(network, blocked)
                    launch {
                        send(InternetService.Status.BlockedStatusChanged)
                        Log.e(TAG, InternetService.Status.BlockedStatusChanged.name)
                    }
                }
            }
            connectivityManager.registerDefaultNetworkCallback(callback)
            awaitClose {
                connectivityManager.unregisterNetworkCallback(callback)
            }
        }.distinctUntilChanged()
    }
}