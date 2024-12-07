package com.easit.tailor.helper

sealed class ConnectionStatus{
    object Available: ConnectionStatus()
    object UnAvailable: ConnectionStatus()
}