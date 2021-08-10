package com.github.jasvir.jcv.ui.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.github.jasvir.jcv.constants.Const.COORDINATES
import com.github.jasvir.jcv.constants.Const.IMG_FILE
import com.github.jasvir.jcv.data.data_classes.Faces
import com.github.jasvir.jcv.data.data_classes.Result
import com.github.jasvir.jcv.databinding.FragmentImageDialogBinding
import com.github.jasvir.jcv.extention.BitmapExtention.flipIMage


class FragmentDialog : DialogFragment(), LifecycleObserver {
    private lateinit var binding: FragmentImageDialogBinding
    private var filePath: String? = null
    private var coordinates: Result? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = FragmentImageDialogBinding.inflate(LayoutInflater.from(context))
        requireActivity().lifecycle.addObserver(this)
        return AlertDialog.Builder(requireActivity())
            .setView(binding.root)
            .create()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreated() {
        requireActivity().lifecycle.removeObserver(this)
        filePath = arguments?.getString(IMG_FILE)!!
        coordinates = arguments?.getParcelable(COORDINATES)!!
        detectFace(BitmapFactory.decodeFile(filePath).flipIMage(), coordinates!!.faces)
    }


    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window!!.setLayout(width, height)
        }
    }

    private fun detectFace(image: Bitmap, faces: List<Faces>) {
        val paint = Paint()
        paint.strokeWidth = 6F
        paint.color = Color.RED
        paint.style = Paint.Style.STROKE
        val tempBitmap = Bitmap.createBitmap(image.width, image.height, Bitmap.Config.RGB_565)
        val canvas = Canvas(tempBitmap)
        canvas.drawBitmap(image, 0F, 0F, null)

        for (i in faces.indices) {
            val thisFace: Faces = faces.get(i)
            val x1: Float = thisFace.coordinates.xmin.toFloat()
            val y1: Float = thisFace.coordinates.ymin.toFloat()
            val x2: Float = thisFace.coordinates.xmax.toFloat()
            val y2: Float = thisFace.coordinates.ymax.toFloat()
            canvas.drawRoundRect(RectF(x1, y1, x2, y2), 0F, 0F, paint)
        }

        binding.ivDisplayImg.setImageDrawable(BitmapDrawable(resources, tempBitmap))
    }


    companion object {
        fun newInstance(bundle: Bundle): FragmentDialog {
            val fragmentDialog = FragmentDialog()
            fragmentDialog.arguments = bundle
            return fragmentDialog
        }
    }


}
