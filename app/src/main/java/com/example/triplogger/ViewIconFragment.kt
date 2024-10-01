package com.example.triplogger.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.triplogger.R

private const val TAG = "ViewIconFragment"
class ViewIconFragment : Fragment() {

    private var imageResourceId: Int = 0
    private lateinit var labelText: String

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i(TAG, "onCreate entered")
        super.onCreate(savedInstanceState)
        arguments?.let {
            imageResourceId = it.getInt(ARG_IMAGE)
            labelText = it.getString(ARG_LABEL).toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i(TAG, "onCreateView entered")
        val view = inflater.inflate(R.layout.fragment_view_icon, container, false)
        val ivViewIcon: ImageView = view.findViewById(R.id.ivViewIcon)
        val tvViewLabel: TextView = view.findViewById(R.id.tvViewLabel)
        ivViewIcon.setImageResource(imageResourceId)
        tvViewLabel.text = labelText
        ivViewIcon.setOnClickListener {
            Log.i("ViewFragment", "$labelText Image clicked!")
        }
        return view
    }

    companion object {
        private const val ARG_IMAGE = "imageResourceId"
        private const val ARG_LABEL = "labelText"

        @JvmStatic
        fun newInstance(imageResourceId: Int, labelText: String) =
            ViewIconFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_IMAGE, imageResourceId)
                    putString(ARG_LABEL, labelText)
                }
            }
    }
}
