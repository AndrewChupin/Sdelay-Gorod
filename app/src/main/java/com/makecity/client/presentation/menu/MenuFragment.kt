package com.makecity.client.presentation.menu

import android.os.Bundle
import android.support.v7.widget.Toolbar
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.makecity.client.R
import com.makecity.client.app.AppInjector
import com.makecity.core.presentation.screen.ToolbarConfig
import com.makecity.core.presentation.screen.ToolbarScreen
import com.makecity.core.presentation.view.StatementFragment
import kotlinx.android.synthetic.main.fragment_menu.*
import kotlinx.android.synthetic.main.toolbar.*


typealias MenuStatement = StatementFragment<MenuReducer, MenuViewState, MenuAction>


class MenuFragment: MenuStatement(), ToolbarScreen {

	companion object {
		fun newInstance() = MenuFragment()
	}

	override val layoutId: Int = R.layout.fragment_menu

	override fun onInject() = AppInjector.inject(this)

	override fun getToolbar(): Toolbar = toolbar

	override fun onViewCreatedBeforeRender(savedInstanceState: Bundle?) {
		setupToolbarWith(requireActivity(), ToolbarConfig(
			title = getString(R.string.app_name),
			isDisplayHomeButton = true
		))

		Glide.with(menu_profile_image)
			.load(R.drawable.placeholder_place)
			.apply(RequestOptions.circleCropTransform())
			.into(menu_profile_image)

		menu_add_account.setOnClickListener {
			reducer.reduce(MenuAction.ItemSelected(MenuType.ADD_ACCOUNT))
		}

		context?.let {
			menu_container_main.addView(MenuView(it, R.string.settings, R.drawable.ic_settings_gray_24dp) {
				reducer.reduce(MenuAction.ItemSelected(MenuType.SETTINGS))
			})
			menu_container_main.addView(MenuView(it, R.string.notifications, R.drawable.ic_notifications_gray_24dp) {
				reducer.reduce(MenuAction.ItemSelected(MenuType.NOTIFICATIONS))
			})
			menu_container_main.addView(MenuView(it, R.string.partners, R.drawable.ic_group_gray_24dp) {
				reducer.reduce(MenuAction.ItemSelected(MenuType.PARTNERS))
			})
			menu_container_main.addView(MenuView(it, R.string.support_project, R.drawable.ic_credit_card_gray_24dp) {
				reducer.reduce(MenuAction.ItemSelected(MenuType.SUPPORT))
			})
			menu_container_main.addView(MenuView(it, R.string.archive_problems, R.drawable.ic_archive_gray_24dp) {
				reducer.reduce(MenuAction.ItemSelected(MenuType.ARCHIVE))
			})
			menu_container_main.addView(MenuView(it, R.string.help, R.drawable.ic_help_outline_gray_24dp) {
				reducer.reduce(MenuAction.ItemSelected(MenuType.HELP))
			})
			menu_container_main.addView(MenuView(it, R.string.about_project, R.drawable.ic_info_gray_24dp) {
				reducer.reduce(MenuAction.ItemSelected(MenuType.ABOUT_PROJECT))
			})
		}

		menu_profile_cell.setOnClickListener {
			reducer.reduce(MenuAction.ShowProfile)
		}
	}

	override fun render(state: MenuViewState) {

	}
}