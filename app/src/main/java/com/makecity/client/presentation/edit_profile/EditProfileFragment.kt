package com.makecity.client.presentation.edit_profile

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.Toolbar
import com.makecity.client.R
import com.makecity.client.app.AppInjector
import com.makecity.core.presentation.screen.ToolbarConfig
import com.makecity.core.presentation.screen.ToolbarScreen
import com.makecity.core.presentation.view.StatementFragment
import com.makecity.core.utils.image.CommonImageRules
import com.makecity.core.utils.image.ImageManager
import kotlinx.android.synthetic.main.fragment_edit_profile.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject
import ru.tinkoff.decoro.slots.PredefinedSlots
import ru.tinkoff.decoro.MaskImpl
import ru.tinkoff.decoro.watchers.MaskFormatWatcher


typealias EditProfileStatement = StatementFragment<EditProfileReducer, EditProfileViewState, EditProfileAction>


class EditProfileFragment : EditProfileStatement(), ToolbarScreen {

	companion object {
		fun newInstance() = EditProfileFragment()
	}

	@Inject
	lateinit var imageManager: ImageManager

	override val layoutId: Int = R.layout.fragment_edit_profile

	override fun onInject() = AppInjector.inject(this)

	override fun getToolbar(): Toolbar = toolbar

	override fun onViewCreatedBeforeRender(savedInstanceState: Bundle?) {
		setupToolbarWith(requireActivity(), ToolbarConfig(
			title = getString(R.string.title_edit_profile),
			isDisplayHomeButton = true
		))

		edit_profile_change_photo.setOnClickListener {
			reducer.reduce(EditProfileAction.PickPhoto)
		}

		val mask = MaskImpl(PredefinedSlots.RUS_PHONE_NUMBER, true)
		mask.isHideHardcodedHead = true
		val watcher = MaskFormatWatcher(mask)
		watcher.installOn(edit_profile_phone)
		edit_profile_phone.setText("9995554433")
	}

	override fun onScreenResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onScreenResult(requestCode, resultCode, data)
		if (data != null) {
			val rules = CommonImageRules(edit_profile_photo, data.dataString, R.drawable.placeholder_face, true)
			imageManager.apply(rules)
		}
	}

	override fun render(state: EditProfileViewState) {

	}
}