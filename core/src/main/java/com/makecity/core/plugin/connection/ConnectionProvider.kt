package com.makecity.core.plugin.connection

import io.reactivex.Observable
import io.reactivex.Single


/**
 * Interface wrapper for observe connection state
 * @author Andrew Chupin
 */
interface ConnectionProvider {
    /**
     * Checking current connection state
     */
    fun isConnected(): Single<Boolean>

    /**
     * Observe changes connection state
     */
    fun observeConnectionState(): Observable<Boolean>
}
