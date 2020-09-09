package com.ramzan.dicegainz.ui.lifts

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.ramzan.dicegainz.R
import com.ramzan.dicegainz.database.LiftDatabase
import com.ramzan.dicegainz.dummy.DummyContent

/**
 * A fragment representing a list of Lifts.
 */
class LiftsPageFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val application = requireNotNull(this.activity).application

        // Create an instance of the ViewModel Factory.
        val dataSource = LiftDatabase.getInstance(application).liftDatabaseDao
        val viewModelFactory = LiftsViewModelFactory(dataSource, application)

        // Get a reference to the ViewModel associated with this fragment.
        val liftsViewModel =
            ViewModelProvider(
                this, viewModelFactory).get(LiftsViewModel::class.java)


        val view = inflater.inflate(R.layout.fragment_lift_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = LinearLayoutManager(context)
                adapter = LiftRecyclerAdapter(DummyContent.ITEMS)
            }
        }
        return view
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            LiftsPageFragment()
    }
}