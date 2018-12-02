package com.makecity.client.presentation.edit_profile

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.View
import com.makecity.client.R
import com.makecity.client.app.AppConst
import com.makecity.client.app.AppInjector
import com.makecity.client.utils.PathParser
import com.makecity.client.utils.saver.BitmapSaverRequest
import com.makecity.core.presentation.screen.ToolbarConfig
import com.makecity.core.presentation.screen.ToolbarScreen
import com.makecity.core.presentation.state.PrimaryViewState
import com.makecity.core.presentation.view.StatementFragment
import com.makecity.core.utils.Symbols.EMPTY
import com.makecity.core.utils.image.CommonImageRules
import com.makecity.core.utils.image.ImageManager
import com.makecity.core.utils.saver.FileSaver
import kotlinx.android.synthetic.main.fragment_edit_profile.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.view_titled_edit_text.view.*
import ru.tinkoff.decoro.MaskImpl
import ru.tinkoff.decoro.slots.PredefinedSlots
import ru.tinkoff.decoro.watchers.MaskFormatWatcher
import java.io.File
import javax.inject.Inject


typealias EditProfileStatement = StatementFragment<EditProfileReducer, EditProfileViewState, EditProfileAction>


class EditProfileFragment : EditProfileStatement(), ToolbarScreen {

	companion object {
		fun newInstance() = EditProfileFragment()
	}

	@Inject
	lateinit var imageManager: ImageManager
	@Inject
	lateinit var fileSaver: FileSaver

	override val layoutId: Int = R.layout.fragment_edit_profile
	private lateinit var loadingDialog: ProgressDialog

	override fun onInject() = AppInjector.inject(this)

	override fun getToolbar(): Toolbar = toolbar

	override fun onViewCreatedBeforeRender(savedInstanceState: Bundle?) {
		setupToolbarWith(requireActivity(), ToolbarConfig(
			title = getString(R.string.title_edit_profile),
			isDisplayHomeButton = true
		))

		loadingDialog = ProgressDialog(requireContext())
		loadingDialog.setMessage(getString(R.string.updating_profile))
		loadingDialog.setCancelable(false)
		edit_profile_phone.titled_view_field.isEnabled = false

		edit_profile_change_photo clickReduce EditProfileAction.PickPhoto

		val mask = MaskImpl(PredefinedSlots.RUS_PHONE_NUMBER, true)
		mask.isHideHardcodedHead = true
		val watcher = MaskFormatWatcher(mask)
		watcher.installOn(edit_profile_phone.titled_view_field)

		edit_profile_save_changes.clickReduce {
			EditProfileAction.SaveChanges(
				sex = getGender(edit_profile_sex_group.checkedRadioButtonId),
				firstName = edit_profile_name.titled_view_field.text.toString(),
				secondName = edit_profile_family.titled_view_field.text.toString(),
				street = edit_profile_street.titled_view_field.text.toString(),
				house = edit_profile_house.titled_view_field.text.toString()
			)
		}
	}


	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		reducer.reduce(EditProfileAction.LoadProfile)
	}

	override fun onDestroyView() {
		super.onDestroyView()
		loadingDialog.dismiss()
	}

	override fun onScreenResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onScreenResult(requestCode, resultCode, data)
		if (data != null && data.data != null) {
			var file = File(PathParser.parseMedia(requireActivity().application.contentResolver, data.data))

			if (file.length() > AppConst.MAX_IMAGE_SIZE) {
				val newFile = imageManager.crateNewPictureFile()
				newFile.createNewFile()
				file = file.copyTo(newFile, overwrite = true)
				fileSaver.save(BitmapSaverRequest(file))
			}

			reducer.reduce(EditProfileAction.ChangePhoto(file.absolutePath))
		}
	}

	@SuppressLint("SetTextI18n")
	override fun render(state: EditProfileViewState) {

		when (state.screenState) {
			is PrimaryViewState.Loading -> loadingDialog.show()
			else -> loadingDialog.hide()
		}

		state.profile?.apply {
			edit_profile_name.titled_view_field.setText(firstName)
			edit_profile_family.titled_view_field.setText(lastName)
			edit_profile_phone.titled_view_field.setText(phone)
			edit_profile_street.titled_view_field.setText(street)
			edit_profile_house.titled_view_field.setText(house)
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
		else -> EMPTY
	}
}