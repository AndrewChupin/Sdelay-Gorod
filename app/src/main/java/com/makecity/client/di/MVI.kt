package com.makecity.client.di

import com.makecity.client.data.comments.Comment


interface IState
interface IEffect
interface IAction

interface Reducer<E : IEffect, S : IState> {
	fun reduce(effect: E, state: S): S
}

typealias Actor<Action, E> = (Action, Emitter<E>) -> Unit
typealias Emitter<E> = (E) -> Unit

abstract class Dispatcher<A : IAction, E : IEffect, S : IState> {

	abstract var store: Store<S>
	abstract var reducer: Reducer<E, S>

	private fun dispatch(action: A) {
		val executor = specify(action)
		executor(action) {
			store.state = reducer.reduce(it, store.state)
		}
	}

	abstract fun specify(action: A): Actor<A, E>
}


abstract class FeedDispatcher(
	override var reducer: Reducer<Effect, State> = ReducerImpl(),
	override var store: Store<State>
) : Dispatcher<Action, Effect, State>() {


	fun clickActor(action: Action.Click, emitter: Emitter<Effect>) {

	}
}


class ReducerImpl : Reducer<Effect, State> {
	override fun reduce(effect: Effect, state: State): State = when (effect) {
		is Effect.DataSuccess -> state.copy(comments = effect.comments)
	}
}


interface Provider<T> {
	fun provide(): T
}

interface Store<State> {
	var state: State
}

data class State(
	val comments: List<Comment> = emptyList()
) : IState


sealed class Action : IAction {
	object Click : Action()
}

sealed class Effect : IEffect {
	data class DataSuccess(
		val comments: List<Comment>
	) : Effect()
}

