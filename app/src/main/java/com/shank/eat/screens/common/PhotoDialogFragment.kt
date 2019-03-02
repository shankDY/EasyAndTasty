package com.shank.eat.screens.common

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.util.Log
import com.shank.eat.R
import kotlinx.android.synthetic.main.fragment_photo_dialog.view.*

class PhotoDialogFragment : DialogFragment() {
    private var listener: OnFragmentInteractionListener? = null
    private lateinit var mPicture: PictureHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPicture = PictureHelper(this)
        Log.d(TAG,"Create")

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = activity!!.layoutInflater.inflate(R.layout.fragment_photo_dialog,null)
        view.buttonPhoto.setOnClickListener {
            mPicture.takeCameraPicture()
        }
        view.buttonGallery.setOnClickListener {
            mPicture.getPicture()
        }
        //показываем диалог
        return AlertDialog.Builder(context!!)
            .setView(view)
            .setTitle(R.string.addPicture)
            .create()
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as OnFragmentInteractionListener
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == mPicture.CAMERA_TAKE_PICTURE_CODE) {
            if (resultCode == RESULT_OK) {
               listener?.onFragmentInteraction(mPicture.imageUri!!)
                dismiss()
            }
        }else if (requestCode == mPicture.GET_PICTURE_CODE) {

            if (resultCode == RESULT_OK) {
                listener?.onFragmentInteraction(data?.data!!)
                dismiss()
            }
        }
    }

    companion object {
        const val TAG = "PhotoDialogFragment"
    }
}
