package homeproject.example.com.myhomeproject.screens.btm_navigation_screens.search

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import homeproject.example.com.myhomeproject.R
import homeproject.example.com.myhomeproject.screens.common.BaseFragment


class SearchFragment : BaseFragment() {

    private lateinit var viewModel: SearchViewModel

    override fun provideYourFragmentView(inflater: LayoutInflater, parent: ViewGroup?,
                                         savedInstanceState: Bundle?): View {
        Log.d(TAG, "onCreate")
        return inflater.inflate(R.layout.search_fragment, parent, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SearchViewModel::class.java)
        // TODO: Use the ViewModel
    }

    companion object {
        const val TAG = "SearchFragment"
    }
}
