package com.makecity.client.presentation.edit_profile

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.Toolbar
import com.makecity.client.R
import com.makecity.client.app.AppInjector
import com.makecity.core.presentation.screen.ToolbarConfig
import com.makecity.core.presentation.screen.ToolbarScreen
import com.makecity.core.presentation.state.PrimaryViewState
import com.makecity.core.presentation.view.StatementFragment
import com.makecity.core.utils.image.CommonImageRules
import com.makecity.core.utils.image.ImageManager
import kotlinx.android.synthetic.main.fragment_edit_profile.*
import kotlinx.android.synthetic.main.toolbar.*
import ru.tinkoff.decoro.MaskImpl
import ru.tinkoff.decoro.slots.PredefinedSlots
import ru.tinkoff.decoro.watchers.MaskFormatWatcher
import javax.inject.Inject


typealias EditProfileStatement = StatementFragment<EditProfileReducer, EditProfileViewState, EditProfileAction>


class EditProfileFragment : EditProfileStatement(), ToolbarScreen {

	companion object {
		fun newInstance() = EditProfileFragment()
	}

	@Inject
	lateinit var imageManager: ImageManager

	override val layoutId: Int = R.layout.fragment_edit_profile
	private lateinit var loadingDialog: ProgressDialog

	override fun onInject() = AppInjector.inject(this)

	override fun getToolbar(): Toolbar = toolbar

	override fun onViewCreatedBeforeRender(savedInstanceState: Bundle?) {
		setupToolbarWith(requireActivity(), ToolbarConfig(
			title = getString(R.string.title_edit_profile),
			isDisplayHomeButton = true
		))

		edit_profile_change_photo clickReduce EditProfileAction.PickPhoto

		val mask = MaskImpl(PredefinedSlots.RUS_PHONE_NUMBER, true)
		mask.isHideHardcodedHead = true
		val watcher = MaskFormatWatcher(mask)
		watcher.installOn(edit_profile_phone)

		edit_profile_save_changes.clickReduce {
			EditProfileAction.SaveChanges(
				sex = getGender(edit_profile_sex_group.checkedRadioButtonId),
				name = edit_profile_name.text.toString(),
				address = edit_profile_address.text.toString()
			)
		}
	}

	override fun onScreenResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onScreenResult(requestCode, resultCode, data)
		data?.dataString?.let {
			val rules = CommonImageRules(edit_profile_photo, it, R.drawable.placeholder_face, true)
			imageManager.apply(rules)
			reducer.reduce(EditProfileAction.ChangePhoto(it))
		}
	}

	@SuppressLint("SetTextI18n")
	override fun render(state: EditProfileViewState) {

		when (state.screenState) {
			is PrimaryViewState.Loading -> loadingDialog = ProgressDialog.show(
				context, getString(R.string.updating_profile), null, false, false
			)
			else -> loadingDialog.dismiss()
		}

		state.profile?.apply {
			edit_profile_name.setText("$firstName $lastName")
			edit_profile_phone.setText(phone)
			edit_profile_address.setText("$street $house")
			val rules = CommonImageRules(edit_profile_photo, photo, R.drawable.placeholder_face, true)
			imageManager.apply(rules)
			when (sex) {
				"male" -> edit_profile_sex_group.check(R.id.edit_profile_sex_male)
				"female" -> edit_profile_sex_group.check(R.id.edit_profile_sex_female)
			}
		}
	}

	private fun getGender(id: Int): String = when (id) {
		R.id.edit_profile_sex_male -> "male"
		R.id.edit_profile_sex_female -> "female"
		else -> throw IllegalArgumentException("Sex with id $id not support")
	}
}