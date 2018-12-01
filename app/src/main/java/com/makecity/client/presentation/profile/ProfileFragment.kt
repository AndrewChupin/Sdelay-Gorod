package com.makecity.client.presentation.profile

import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.View
import com.makecity.client.R
import com.makecity.client.app.AppInjector
import com.makecity.core.extenstion.checkNotEmpty
import com.makecity.core.presentation.screen.ToolbarConfig
import com.makecity.core.presentation.screen.ToolbarScreen
import com.makecity.core.presentation.view.StatementFragment
import com.makecity.core.utils.image.CommonImageRules
import com.makecity.core.utils.image.ImageManager
import kotlinx.android.synthetic.main.fragment_profile.*
import ru.tinkoff.decoro.MaskImpl
import ru.tinkoff.decoro.slots.PredefinedSlots
import javax.inject.Inject

typealias ProfileStatement = StatementFragment<ProfileReducer, ProfileViewState, ProfileAction>


class ProfileFragment : ProfileStatement(), ToolbarScreen {

	companion object {
		fun newInstance() = ProfileFragment()

		private const val MALE = "male"
		private const val FEMALE = "female"
	}

	override val layoutId: Int = R.layout.fragment_profile
	private var mask = MaskImpl(PredefinedSlots.RUS_PHONE_NUMBER, true)

	@Inject
	lateinit var imageManager: ImageManager

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
				profile_name.text = getString(R.string.name_undefined)
			} else {
				profile_name.text = getString(R.string.name_format, firstName, lastName)
			}

			if (street.isEmpty()) {
				profile_address.text = getString(R.string.undefined_symbol)
			} else {
				val address = "$street $house"
				profile_address.text = address
			}

			profile_gender.text = when (sex) {
				MALE -> getString(R.string.male)
				FEMALE -> getString(R.string.female)
				else -> getString(R.string.undefined_symbol)
			}

			photo.checkNotEmpty {
				imageManager.apply(CommonImageRules(
					image = profile_image,
					url = it,
					placeholder = R.drawable.face_placeholder_bg
				))
			}

			profile_city.text = state.geoPoint?.title
		}
	}
}