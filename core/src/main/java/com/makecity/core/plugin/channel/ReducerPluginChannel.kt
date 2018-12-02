package com.makecity.core.plugin.channel


interface ReducerPluginChannel<M : Message> {
	var channel: ((M) -> Unit)?
}