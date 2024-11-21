package com.example.triplogger.view

import android.content.Intent
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
    private lateinit var viewType: String
    private lateinit var labelText: String

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i(TAG, "onCreate entered")
        super.onCreate(savedInstanceState)
        arguments?.let {
            imageResourceId = it.getInt(ARG_IMAGE)
            viewType = it.getString(ARG_VIEW_TYPE).toString()
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
            Log.i(TAG, "$labelText Image clicked!")
            openViewActivity()
        }
        tvViewLabel.setOnClickListener {
            Log.i(TAG, "$labelText Text clicked!")
            openViewActivity()
        }
        return view
    }

    private fun openViewActivity() {
        val context = requireContext()
        val activityClass = when(viewType){
            "list_view" -> ListViewActivity::class.java
            "map_view" -> MapViewActivity::class.java
            else -> MapViewActivity::class.java

        }
        val intent = Intent(context, activityClass)
        context.startActivity(intent)
    }

    companion object {
        private const val ARG_IMAGE = "imageResourceId"
        private const val ARG_LABEL = "labelText"
        private const val ARG_VIEW_TYPE = "viewType"

        @JvmStatic
        fun newInstance(imageResourceId: Int, viewType: String, labelText: String) =
            ViewIconFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_IMAGE, imageResourceId)
                    putString(ARG_VIEW_TYPE, viewType)
                    putString(ARG_LABEL, labelText)
                }
            }
    }
}
