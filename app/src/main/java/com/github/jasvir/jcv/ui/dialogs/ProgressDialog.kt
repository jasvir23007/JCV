package com.github.jasvir.jcv.ui.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import com.github.jasvir.jcv.R

class ProgressDialog {

    companion object {
        var dialog:Dialog? = null

        fun progressDialog(context: Context): Dialog {
            dialog = Dialog(context)
            val inflate = LayoutInflater.from(context).inflate(R.layout.progress_dialog, null)
            dialog?.setContentView(inflate)
            dialog?.setCancelable(false)
            dialog?.window!!.setBackgroundDrawable(
                ColorDrawable(Color.TRANSPARENT)
            )
            return dialog!!
        }


//        fun show(context: Context) {
//            dialog = progressDialog(context)
//            dialog?.show()
//        }

        fun dismiss() {
            dialog?.dismiss()
        }

    }


}