package com.makecity.client.presentation.profile

import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.View
import com.makecity.client.R
import com.makecity.client.app.AppInjector
import com.makecity.core.presentation.screen.ToolbarConfig
import com.makecity.core.presentation.screen.ToolbarScreen
import com.makecity.core.presentation.view.StatementFragment
import kotlinx.android.synthetic.main.fragment_profile.*
import ru.tinkoff.decoro.MaskImpl
import ru.tinkoff.decoro.slots.PredefinedSlots
import ru.tinkoff.decoro.watchers.MaskFormatWatcher

typealias ProfileStatement = StatementFragment<ProfileReducer, ProfileViewState, ProfileAction>


class ProfileFragment : ProfileStatement(), ToolbarScreen {

	companion object {
		fun newInstance() = ProfileFragment()
	}

	override val layoutId: Int = R.layout.fragment_profile
	private var mask = MaskImpl(PredefinedSlots.RUS_PHONE_NUMBER, true)

	override fun onInject() = AppInjector.inject(this)

	override fun getToolbar(): Toolbar = toolbar

	override fun onViewCreatedBeforeRender(savedInstanceState: Bundle?) {
		setupToolbarWith(requireActivity(), ToolbarConfig(
			title = getString(R.string.profile),
			isDisplayHomeButton = true
		))

		profile_edit_profile_button clickReduce ProfileAction.ShowEditProfile
		profile_logout clickReduce ProfileAction.Logout
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		reducer.reduce(ProfileAction.GetProfileData)
	}

	override fun render(state: ProfileViewState) {
		state.profile?.apply {
			profile_phone.text = mask.run {
				insertFront(phone)
				toString()
			}

			if (firstName.isEmpty() && lastName.isEmpty()) {
				profile_name.text = getString(R.string.undefined)
			} else {
				profile_name.text = getString(R.string.name_format, firstName, lastName)
			}
		}
	}
}