package com.github.jasvir.jcv.extention

import android.app.Activity
import android.app.Dialog
import android.content.Context
import com.github.jasvir.jcv.ui.dialogs.ProgressDialog

object ProgressExtention {
    fun Activity.showPogressDialog(): Dialog {
        return ProgressDialog.progressDialog(this)
    }


    fun Activity.dismiss() {
        return ProgressDialog.dismiss()
    }

}
