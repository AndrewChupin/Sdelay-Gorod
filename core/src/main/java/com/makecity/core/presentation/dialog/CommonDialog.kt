package com.makecity.core.presentation.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DialogTwoWaysData(
	val message: String,
	val positiveButton: String,
	val negativeButton: String
) : Parcelable


interface DialogTwoWaysDelegate {
	fun onPositiveClick(dialog: DialogInterface) {}
	fun onNegativeClick(dialog: DialogInterface) {}
}


class TwoWaysDialog : DialogFragment() {

	companion object {
		const val ARGUMENT_DIALOG_TWO_WAY = "ARGUMENT_DIALOG_TWO_WAY"

		fun newInstance(data: DialogTwoWaysData) = TwoWaysDialog()
			.apply {
				val args = Bundle()
				args.putParcelable(ARGUMENT_DIALOG_TWO_WAY, data)
				arguments = args
			}
	}

	override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = arguments
		?.getParcelable<DialogTwoWaysData>(ARGUMENT_DIALOG_TWO_WAY)
		?.run {
			AlertDialog.Builder(requireContext())
				.setCancelable(true)
				.setMessage(message)
				.setNegativeButton(negativeButton) { dialog, _ ->
					val target = targetFragment
					if (target is DialogTwoWaysDelegate) {
						target.onNegativeClick(dialog)
					}
				}
				.setPositiveButton(positiveButton) { dialog, _ ->
					val target = targetFragment
					if (target is DialogTwoWaysDelegate) {
						target.onPositiveClick(dialog)
					}
				}
				.create()
		} ?: throw IllegalArgumentException("arguments dialog must keep DialogTwoWaysData")

}