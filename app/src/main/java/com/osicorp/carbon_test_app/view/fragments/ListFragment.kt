package com.osicorp.carbon_test_app.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.osicorp.carbon_test_app.R
import com.osicorp.carbon_test_app.databinding.FragmentListBinding
import com.osicorp.carbon_test_app.interfaces.ListListener
import com.osicorp.carbon_test_app.view.fragments.list.PostAdapter
import com.osicorp.carbon_test_app.view_model.ListViewModel
import com.osicorp.carbon_test_app.view_model.PostViewModelFactory

class ListFragment: BaseFragment(), ListListener {


    private var binding: FragmentListBinding?=null
    private  lateinit var viewModel: ListViewModel
    private lateinit var controller: MediaController
    private var reachedBottom = false

    companion object{
        private const val TAG = "ListFragment"
        fun getInstance():ListFragment = ListFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView: started")
        val view = inflater.inflate(R.layout.fragment_list, container, false)
        viewModel = ViewModelProvider(this,
            PostViewModelFactory(activity?.application!!))
            .get(ListViewModel::class.java)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        controller = MediaController(activity)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentListBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadFirst()
        viewModel.postData.observe(
            viewLifecycleOwner,
            Observer { posts ->
                run {
                    for (p in posts) Log.d(TAG, "onViewCreated: ${p.TimeStamp}")
                }
            })
    }

    override fun initialiseWidgets(view: View) {
        with(binding!!){
            val layoutManager = LinearLayoutManager(
                activity, RecyclerView.VERTICAL,
                false
            )
            val  adapter = PostAdapter(this@ListFragment)
            list.layoutManager = layoutManager
            list.adapter = adapter
            viewModel.postData.observe(
                viewLifecycleOwner,
                Observer { posts -> adapter.setList(posts) })

            list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    reachedBottom = !recyclerView.canScrollVertically(1)
                    if (reachedBottom) {

                        viewModel.loadMorePosts()
                    }
                }
            })
        }
    }


    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    override fun showSelectedPostWithId(id: String) {
        //listener?.expandPost(viewModel.getPostWithId(id))
        val bundle = bundleOf("postId" to id)
        findNavController().navigate(R.id.action_listFragment_to_detailsFragment, bundle)
    }

    override fun getMediaController(): MediaController {
        return controller
    }
}