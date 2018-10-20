package com.makecity.client.presentation.edit_profile

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.Toolbar
import com.bumptech.glide.Glide
import com.makecity.client.R
import com.makecity.client.app.AppInjector
import com.makecity.core.presentation.screen.ToolbarConfig
import com.makecity.core.presentation.screen.ToolbarScreen
import com.makecity.core.presentation.view.StatementFragment
import com.makecity.core.utils.image.ImageManager
import kotlinx.android.synthetic.main.fragment_edit_profile.*
import kotlinx.android.synthetic.main.toolbar.*
import java.io.File
import javax.inject.Inject

typealias EditProfileStatement = StatementFragment<EditProfileReducer, EditProfileViewState, EditProfileAction>


class EditProfileFragment : EditProfileStatement(), ToolbarScreen {

	companion object {
		private const val PHOTO_REQ_CODE = 533
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
	}

	override fun onScreenResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onScreenResult(requestCode, resultCode, data)
		if (data != null) {
			Glide.with(edit_profile_photo)
				.load(File(data.data.path))
				.into(edit_profile_photo)
			//imageManager.apply(CommonImageRules(edit_profile_photo, data.dataString, R.drawable.placeholder_face, true))
		}
	}

	override fun render(state: EditProfileViewState) {

	}
}