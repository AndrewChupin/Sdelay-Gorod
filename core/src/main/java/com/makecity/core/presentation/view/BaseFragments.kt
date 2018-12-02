package com.makecity.core.presentation.view

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.support.annotation.CallSuper
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.makecity.core.presentation.navigation.FragmentConsumer
import com.makecity.core.presentation.navigation.FragmentDelegate
import com.makecity.core.presentation.state.ViewState
import com.makecity.core.presentation.viewmodel.ActionView
import com.makecity.core.presentation.viewmodel.BaseReducer
import com.makecity.core.presentation.viewmodel.StatementReducer
import kotlinx.android.synthetic.*
import javax.inject.Inject


abstract class BaseFragment: Fragment(), FragmentDelegate {

    abstract val layoutId: Int

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
        = inflater.inflate(layoutId, container, false)

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (activity is FragmentConsumer) {
            (activity as FragmentConsumer).fragmentelegate = this
        }
    }

    @CallSuper
    override fun onDestroyView() {
        super.onDestroyView()
        if (activity is FragmentConsumer) {
            (activity as FragmentConsumer).fragmentelegate = null
        }
        clearFindViewByIdCache()
    }

    override fun onBackClick(): Boolean {
        return false
    }

    override fun onScreenResult(requestCode: Int, resultCode: Int, data: Intent?) {}

    protected fun showMessage(message: String, contextView: View? = view) {
        contextView?.let {
            Snackbar.make(it, message, Snackbar.LENGTH_LONG).show()
        }
    }

    protected inline fun <reified Data: Parcelable> getArgument(key: String): Data {
        arguments?.let {
            if (it.containsKey(key)) {
                return it.getParcelable(key)
            }
        }
        throw IllegalStateException("Not found data with key $key")
    }

    protected inline fun <Data> LiveData<Data>.observeThis(crossinline closure: (Data) -> Unit) {
        observe(this@BaseFragment, Observer {
            it?.let(closure)
        })
    }
}



abstract class ReducibleViewFragment<Reducer: BaseReducer<AG>, AG: ActionView>: BaseFragment(), ReducibleView<Reducer, AG>, InjectableView {

    @Inject
    override lateinit var reducer: Reducer

    @CallSuper
    override fun onAttach(context: Context?) {
        onInject()
        super.onAttach(context)
    }

    infix fun View?.clickReduce(action: AG) {
        this?.setOnClickListener {
            reducer.reduce(action)
        }
    }

    infix fun View?.clickReduce(action: () -> AG) {
        this?.setOnClickListener {
            reducer.reduce(action())
        }
    }
}


abstract class StatementFragment<Reducer: StatementReducer<State, AG>, State: ViewState, AG: ActionView>
    : ReducibleViewFragment<Reducer, AG>(), RenderableView<State> {

    private var isInstanceSaved: Boolean = false

    abstract fun onViewCreatedBeforeRender(savedInstanceState: Bundle?)

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        reducer.reduceBeforeReady()

        onViewCreatedBeforeRender(savedInstanceState)
        reducer.viewState.observeThis {
            if (!isInstanceSaved) render(it)
        }

        reducer.reduceAfterReady()
    }

    @CallSuper
    override fun onResume() {
        super.onResume()
        isInstanceSaved = false
    }

    @CallSuper
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        isInstanceSaved = true
    }

    @CallSuper
    override fun onDestroyView() {
        reducer.viewState.removeObservers(this)
        super.onDestroyView()
    }
}
