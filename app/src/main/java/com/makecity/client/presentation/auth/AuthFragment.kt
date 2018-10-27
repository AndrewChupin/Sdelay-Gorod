package com.makecity.client.presentation.auth

import android.os.Bundle
import android.support.v4.view.ViewCompat
import android.support.v7.widget.Toolbar
import android.text.InputFilter
import android.util.Log
import com.makecity.client.R
import com.makecity.client.app.AppConst
import com.makecity.client.app.AppInjector
import com.makecity.client.data.auth.AuthType
import com.makecity.client.utils.PhoneParser
import com.makecity.core.extenstion.addOnTextChangeListener
import com.makecity.core.extenstion.fromHtml
import com.makecity.core.extenstion.withArguments
import com.makecity.core.presentation.screen.KeyboardScreen
import com.makecity.core.presentation.screen.ToolbarConfig
import com.makecity.core.presentation.screen.ToolbarScreen
import com.makecity.core.presentation.view.StatementFragment
import com.makecity.core.utils.ScreenUtils
import com.makecity.core.utils.Symbols.EMPTY
import kotlinx.android.synthetic.main.fragment_auth.*
import ru.tinkoff.decoro.MaskImpl
import ru.tinkoff.decoro.slots.PredefinedSlots
import ru.tinkoff.decoro.watchers.FormatWatcher
import ru.tinkoff.decoro.watchers.MaskFormatWatcher

typealias AuthStatement = StatementFragment<AuthReducer, AuthViewState, AuthAction>


class AuthFragment : AuthStatement(), ToolbarScreen, KeyboardScreen {

	companion object {
		private const val ARGUMENT_AUTH_DATA = "ARGUMENT_AUTH_DATA"
		fun newInstance(authData: AuthData) = AuthFragment().withArguments {
			putParcelable(ARGUMENT_AUTH_DATA, authData)
		}
	}


	private lateinit var formatWatcher: FormatWatcher
	override val layoutId: Int = R.layout.fragment_auth

	override fun onInject() = AppInjector.inject(this, getArgument(ARGUMENT_AUTH_DATA))

	override fun getToolbar(): Toolbar = auth_toolbar

	override fun onViewCreatedBeforeRender(savedInstanceState: Bundle?) {
		// Toolbar
		setupToolbarWith(requireActivity(), ToolbarConfig(
			title = EMPTY,
			isDisplayHomeButton = true
		))

		ViewCompat.setElevation(auth_input_container, ScreenUtils.convertDpToPixel(8f))

		// Keyboard and focus
		auth_input_field.addOnTextChangeListener { text ->
			Log.d("Logos", "Content ${text.toString()}")
			text?.let {
				if (reducer.state.authType == AuthType.PHONE) {
					val phoneUnformatted = formatWatcher.mask.toUnformattedString()
					Log.d("Logos", "Phone $phoneUnformatted")
					Log.d("Logos", "Phone1 ${PhoneParser.parse(phoneUnformatted)}")
					reducer.reduce(AuthAction.ResearchContent(PhoneParser.parse(phoneUnformatted)))
				} else {
					reducer.reduce(AuthAction.ResearchContent(it.toString()))
				}
			}
		}

		auth_input_field.requestFocus()
		showKeyboard()


	}

	override fun onDestroyView() {
		super.onDestroyView()
		trySetupContentSize(false)
		hideKeyboard()
	}

	override fun render(state: AuthViewState) {
		updateAuthContent(state.authType)
		//auth_next_button.isVisible = state.isNextVisible
	}

	private fun updateAuthContent(authType: AuthType) = when (authType) {
		AuthType.PHONE -> {
			auth_input_field.setHint(R.string.input_phone)

			val mask = MaskImpl(PredefinedSlots.RUS_PHONE_NUMBER, true)
			mask.isHideHardcodedHead = true
			formatWatcher = MaskFormatWatcher(mask)
			formatWatcher.installOn(auth_input_field)

			auth_info_title.text = getString(R.string.auth_phone_title)
			auth_info_description.text = getString(R.string.auth_phone_description)

			auth_next_button clickReduce AuthAction.ShowNextStep
		}
		AuthType.SMS -> {
			auth_input_field.setHint(R.string.input_code)
			auth_input_field.filters = arrayOf(InputFilter.LengthFilter(AppConst.SMS_CODE_LENGTH))

			auth_info_title.text = getString(R.string.auth_sms_title, "+79042797431").fromHtml
			auth_info_description.text = getString(R.string.auth_sms_description)

			auth_next_button clickReduce AuthAction.ShowNextStep
		}
		AuthType.PASSWORD -> {
			auth_input_field.setHint(R.string.input_password)
			auth_input_field.filters = arrayOf()

			auth_info_title.text = getString(R.string.auth_phone_title)
			auth_info_description.text = getString(R.string.auth_phone_description)

			auth_next_button.setOnClickListener {
				reducer.reduce(AuthAction.CheckPassword(auth_input_field.text.toString()))
			}
		}
		AuthType.CREATE_PASSWORD -> {
			auth_input_field.setHint(R.string.input_password)
			auth_input_field.filters = arrayOf()

			auth_info_title.text = getString(R.string.auth_phone_title)
			auth_info_description.text = getString(R.string.auth_phone_description)

			auth_next_button.setOnClickListener {
				reducer.reduce(AuthAction.CreatePassword(auth_input_field.text.toString()))
			}
		}
	}
}