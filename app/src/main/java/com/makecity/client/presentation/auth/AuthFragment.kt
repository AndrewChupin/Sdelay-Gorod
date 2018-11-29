package com.makecity.client.presentation.auth

import android.animation.Animator
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewCompat
import android.support.v7.widget.Toolbar
import android.text.InputFilter
import android.text.TextWatcher
import android.view.View
import com.makecity.client.R
import com.makecity.client.app.AppConst
import com.makecity.client.app.AppInjector
import com.makecity.client.data.auth.AuthType
import com.makecity.client.presentation.aimation.SimpleAnimatorListener
import com.makecity.client.presentation.aimation.incorrect.IncorrectAnimator
import com.makecity.client.utils.PhoneParser
import com.makecity.core.extenstion.addOnTextChangeListener
import com.makecity.core.extenstion.fromHtml
import com.makecity.core.extenstion.isVisible
import com.makecity.core.extenstion.withArguments
import com.makecity.core.presentation.screen.KeyboardScreen
import com.makecity.core.presentation.screen.ToolbarConfig
import com.makecity.core.presentation.screen.ToolbarScreen
import com.makecity.core.presentation.state.PrimaryViewState
import com.makecity.core.presentation.view.StatementFragment
import com.makecity.core.utils.ScreenUtils
import com.makecity.core.utils.Symbols.EMPTY
import kotlinx.android.synthetic.main.fragment_auth.*
import ru.tinkoff.decoro.MaskImpl
import ru.tinkoff.decoro.slots.PredefinedSlots
import ru.tinkoff.decoro.watchers.FormatWatcher
import ru.tinkoff.decoro.watchers.MaskFormatWatcher
import android.text.InputType
import com.makecity.core.plugin.channel.DefaultMessage


typealias AuthStatement = StatementFragment<AuthReducer, AuthViewState, AuthAction>


class AuthFragment : AuthStatement(), ToolbarScreen, KeyboardScreen, SimpleAnimatorListener {

	companion object {
		private const val ARGUMENT_AUTH_DATA = "ARGUMENT_AUTH_DATA"
		fun newInstance(authData: AuthData) = AuthFragment().withArguments {
			putParcelable(ARGUMENT_AUTH_DATA, authData)
		}
	}

	private var textObserver: TextWatcher? = null
	private var formatWatcher: FormatWatcher? = null

	override val layoutId: Int = R.layout.fragment_auth
	private var mask = MaskImpl(PredefinedSlots.RUS_PHONE_NUMBER, true)


	override fun onInject() = AppInjector.inject(this, getArgument(ARGUMENT_AUTH_DATA))

	override fun getToolbar(): Toolbar = auth_toolbar

	override fun onViewCreatedBeforeRender(savedInstanceState: Bundle?) {
		// Toolbar
		setupToolbarWith(requireActivity(), ToolbarConfig(
			title = EMPTY,
			isDisplayHomeButton = true
		))

		ViewCompat.setElevation(auth_input_container, ScreenUtils.convertDpToPixel(8f))

		auth_retry_button clickReduce AuthAction.RefreshSms

		// Keyboard and focus
		textObserver = auth_input_field.addOnTextChangeListener { text ->
			if (auth_input_error.isVisible) {
				auth_input_error.isVisible = false
			}

			text?.let {
				if (reducer.state.authType == AuthType.PHONE) {
					val phoneUnformatted = formatWatcher?.mask?.toUnformattedString() ?: EMPTY
					reducer.reduce(AuthAction.ResearchContent(PhoneParser.parse(phoneUnformatted)))
				} else {
					reducer.reduce(AuthAction.ResearchContent(it.toString()))
				}
			}
		}

		reducer.channel = {
			when (it) {
				DefaultMessage.ClearData -> {
					auth_input_field.text = null
				}
			}
		}

		auth_input_field.requestFocus()
		showKeyboard()
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		updateAuthContent(reducer.state.authType)
	}

	override fun onDestroyView() {
		super.onDestroyView()

		hideKeyboard()
		reducer.channel = null
		IncorrectAnimator.destroy() // TODO LATE


		textObserver?.let {
			auth_input_field.removeTextChangedListener(it)
		}
		formatWatcher?.removeFromTextView()
	}

	override fun onBackClick(): Boolean {
		reducer.reduce(AuthAction.BackClick)
		return true
	}

	override fun render(state: AuthViewState) {
		if (state.authType == AuthType.SMS) {
			checkTimer(state.blockingSeconds)
			mask.insertFront(state.authPhone)
			auth_info_title.text = getString(R.string.auth_sms_title, mask.toString()).fromHtml
		}

		if (state.authType == AuthType.CREATE_PASSWORD) {
			when (state.isPassExist) {
				true -> auth_input_field.setHint(R.string.repeat_password)
				false -> auth_input_field.setHint(R.string.make_password)
			}
		}

		if (state.isResetContent) {
			auth_input_field.setText(EMPTY)

			if (!state.isPassExist) {
				IncorrectAnimator.add(view = auth_input_field, animatorListener = this)
			}
		}

		when (state.screenState) {
			PrimaryViewState.Data -> {
				updateState(withInfo = true, inputEnable = true)
			}
			PrimaryViewState.Loading -> {
				updateState(true)
				hideKeyboard()
			}
			PrimaryViewState.Success -> {
				updateState(withInfo = true, inputEnable = true, nextButtonVisible = true)
			}
		}
	}

	override fun onAnimationStart(animation: Animator) {
		super.onAnimationStart(animation)
		auth_input_field.isEnabled = false
		auth_input_error.isVisible = true
		auth_input_field.setHintTextColor(ContextCompat.getColor(
			requireContext(),
			R.color.colorDangerous
		))
	}

	override fun onAnimationEnd(animation: Animator) {
		super.onAnimationEnd(animation)
		auth_input_field.isEnabled = true
		auth_input_field.setHintTextColor(ContextCompat.getColor(
			requireContext(),
			R.color.text_dark_description
		))
	}

	private fun checkTimer(time: Int) = when {
		time <= 0 -> {
			auth_info_description.isVisible = false
			auth_retry_button.isVisible = true
		}
		time > 0 -> {
			auth_info_description.isVisible = true
			auth_retry_button.isVisible = false
			auth_info_description.text = getString(R.string.auth_sms_description, time)
		}
		else -> Unit
	}

	private fun updateState(
		withProgress: Boolean = false,
		withInfo: Boolean = false,
		inputEnable: Boolean = false,
		nextButtonVisible: Boolean = false
	) {
		auth_progress.isVisible = withProgress
		auth_info_container.isVisible = withInfo
		auth_input_field.isEnabled = inputEnable
		if (nextButtonVisible) auth_next_button.show() else auth_next_button.hide()
	}

	private fun updateAuthContent(authType: AuthType) = when (authType) {
		AuthType.PHONE -> {
			auth_input_field.setHint(R.string.input_phone)

			mask.isHideHardcodedHead = true
			formatWatcher = MaskFormatWatcher(mask)
			formatWatcher?.installOn(auth_input_field)

			auth_info_title.text = getString(R.string.auth_phone_title)
			auth_info_description.text = getString(R.string.auth_phone_description)

			auth_next_button clickReduce AuthAction.ShowNextStep
		}
		AuthType.SMS -> {
			auth_input_field.setHint(R.string.input_code)
			auth_input_field.filters = arrayOf(InputFilter.LengthFilter(AppConst.SMS_CODE_LENGTH))

			auth_info_title.text = getString(R.string.auth_sms_title, EMPTY).fromHtml
			auth_info_description.text = getString(R.string.auth_sms_description, AppConst.SECURE_RETRY_TIMEOUT)

			auth_next_button clickReduce AuthAction.ShowNextStep
		}
		AuthType.PASSWORD -> {
			auth_input_field.setHint(R.string.input_password)
			auth_input_field.filters = arrayOf()
			auth_input_field.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD

			auth_info_title.text = getString(R.string.auth_phone_title)
			auth_info_description.text = getString(R.string.auth_phone_description)

			auth_next_button clickReduce {
				AuthAction.CheckPassword(auth_input_field.text.toString())
			}
		}
		AuthType.CREATE_PASSWORD -> {
			auth_input_field.setHint(R.string.input_password)
			auth_input_field.filters = arrayOf()
			auth_input_field.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD

			auth_info_title.text = getString(R.string.auth_phone_title)
			auth_info_description.text = getString(R.string.auth_phone_description)

			auth_next_button clickReduce  {
				AuthAction.CreatePassword(auth_input_field.text.toString())
			}
		}
	}
}