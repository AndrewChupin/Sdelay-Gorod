package com.makecity.client.presentation.notification

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import com.makecity.client.R
import com.makecity.client.app.AppInjector
import com.makecity.client.presentation.lists.Notification
import com.makecity.client.presentation.lists.NotificationAdapter
import com.makecity.core.extenstion.calculateDiffs
import com.makecity.core.presentation.screen.ToolbarConfig
import com.makecity.core.presentation.screen.ToolbarScreen
import com.makecity.core.presentation.view.StatementFragment
import kotlinx.android.synthetic.main.fragment_notification.*
import kotlinx.android.synthetic.main.toolbar.*


typealias NotificationStatement = StatementFragment<NotificationReducer, NotificationViewState, NotificationAction>


class NotificationFragment : NotificationStatement(), ToolbarScreen {

	companion object {
		fun newInstance() = NotificationFragment()
	}

	override val layoutId: Int = R.layout.fragment_notification

	override fun onInject() = AppInjector.inject(this)

	override fun getToolbar(): Toolbar = toolbar

	override fun onViewCreatedBeforeRender(savedInstanceState: Bundle?) {
		setupToolbarWith(requireActivity(), ToolbarConfig(
			title = getString(R.string.notifications),
			isDisplayHomeButton = false
		))

		notification_recycler.setHasFixedSize(false)
		notification_recycler.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
		notification_recycler.layoutManager = LinearLayoutManager(context)

		val adapter = NotificationAdapter {}
		notification_recycler.adapter = adapter


		adapter.calculateDiffs(List(20) {
			Notification("Велосипедисты-энтузиасты построили на этом месте памп-трек - грунтовую велосипедную закольцованную трассу, представляющую собой чередование ям, кочек и контруклонов.", "от Sdelaigorod.ru", "11 Мар 2018, 21:42")
		})
	}

	override fun render(state: NotificationViewState) {

	}
}