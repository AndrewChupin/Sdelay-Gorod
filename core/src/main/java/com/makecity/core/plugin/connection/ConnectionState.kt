package com.makecity.core.plugin.connection


sealed class ConnectionState {
    object Unknown: ConnectionState()
    object Connect: ConnectionState()
    object Disconnect: ConnectionState()
}
