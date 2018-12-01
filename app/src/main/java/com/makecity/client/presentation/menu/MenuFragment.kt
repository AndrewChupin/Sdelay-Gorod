package com.makecity.client.presentation.menu

import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.View
import com.makecity.client.R
import com.makecity.client.app.AppInjector
import com.makecity.core.extenstion.isVisible
import com.makecity.core.presentation.screen.ToolbarConfig
import com.makecity.core.presentation.screen.ToolbarScreen
import com.makecity.core.presentation.state.PrimaryViewState
import com.makecity.core.presentation.view.StatementFragment
import com.makecity.core.utils.Symbols.EMPTY
import com.makecity.core.utils.image.CommonImageRules
import com.makecity.core.utils.image.ImageManager
import kotlinx.android.synthetic.main.fragment_menu.*
import ru.tinkoff.decoro.MaskImpl
import ru.tinkoff.decoro.slots.PredefinedSlots
import javax.inject.Inject


typealias MenuStatement = StatementFragment<MenuReducer, MenuViewState, MenuAction>


class MenuFragment: MenuStatement(), ToolbarScreen {

	companion object {
		fun newInstance() = MenuFragment()
	}

	@Inject
	lateinit var imageManager: ImageManager

	private var mask = MaskImpl(PredefinedSlots.RUS_PHONE_NUMBER, true)

	override val layoutId: Int = R.layout.fragment_menu

	override fun onInject() = AppInjector.inject(this)

	override fun getToolbar(): Toolbar = toolbar

	override fun onViewCreatedBeforeRender(savedInstanceState: Bundle?) {
		setupToolbarWith(requireActivity(), ToolbarConfig(
			title = EMPTY,
			isDisplayHomeButton = true
		))

		menu_profile_cell clickReduce MenuAction.ShowProfile

		context?.let {
			/*menu_container_main.addView(MenuView(it, R.string.settings, R.drawable.ic_settings_gray_24dp) {
				reducer.reduce(MenuAction.ItemSelected(MenuType.SETTINGS))
			})*/
			/*menu_container_main.addView(MenuView(it, R.string.notifications, R.drawable.ic_notifications_gray_24dp) {
				reducer.reduce(MenuAction.ItemSelected(MenuType.NOTIFICATIONS))
			})*/
			menu_container_main.addView(MenuView(it, R.string.partners, R.drawable.ic_group_gray_24dp) {
				reducer.reduce(MenuAction.ItemSelected(MenuType.PARTNERS))
			})
			menu_container_main.addView(MenuView(it, R.string.support_project, R.drawable.ic_credit_card_gray_24dp) {
				reducer.reduce(MenuAction.ItemSelected(MenuType.SUPPORT))
			})
			/*menu_container_main.addView(MenuView(it, R.string.archive_problems, R.drawable.ic_archive_gray_24dp) {
				reducer.reduce(MenuAction.ItemSelected(MenuType.ARCHIVE))
			})*/
			/*menu_container_main.addView(MenuView(it, R.string.help, R.drawable.ic_help_outline_gray_24dp) {
				reducer.reduce(MenuAction.ItemSelected(MenuType.HELP))
			})*/
			menu_container_main.addView(MenuView(it, R.string.about_project, R.drawable.ic_info_gray_24dp) {
				reducer.reduce(MenuAction.ItemSelected(MenuType.ABOUT_PROJECT))
			})
		}
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		reducer.reduce(MenuAction.RefreshProfileData)
	}

	override fun render(state: MenuViewState) {
		when (state.screenState) {
			is PrimaryViewState.Data -> {
				menu_profile_loader.isVisible = false
				menu_profile_container.isVisible = true
				when (state.profile) {
					null -> {
						menu_profile_image.setImageResource(R.drawable.placeholder_face)
						menu_profile_action.setText(R.string.add_profile)
						menu_profile_name.setText(R.string.guest)
					}
					else -> {
						val profile = state.profile
						imageManager.apply(CommonImageRules(
							menu_profile_image,
							profile.photo,
							placeholder = R.drawable.placeholder_face,
							withCircle = true
						))
						val name = if (profile.firstName.isEmpty() && profile.lastName.isEmpty()) {
							mask.insertFront(profile.phone)
							mask.toString()
						} else {
							"${profile.firstName} ${profile.lastName}"
						}

						menu_profile_name.text = name
						menu_profile_action.setText(R.string.show_profile)
					}
				}
			}
			is PrimaryViewState.Loading -> {
				menu_profile_loader.isVisible = true
				menu_profile_container.isVisible = false
			}
		}
	}
}