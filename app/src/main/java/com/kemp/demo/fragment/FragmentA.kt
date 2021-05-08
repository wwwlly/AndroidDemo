package com.kemp.demo.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kemp.demo.R
import com.kemp.demo.activity.FragmentDemo
import kotlinx.android.synthetic.main.fragment_a.*

class FragmentA : BaseFragment() {

    private val index by lazy {
        arguments?.getInt("index")
    }

    private val TAG by lazy {
        "FragmentA index: $index "
    }

    companion object {

        fun getInstance(index: Int): FragmentA {
            val fragment = FragmentA()
            val bundle = Bundle()
            bundle.putString("name", "FragmentA")
            bundle.putInt("index", index)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FragmentDemo.logd("$TAG onCreate")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        FragmentDemo.logd( "$TAG onCreateView")
        return inflater.inflate(R.layout.fragment_a, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        FragmentDemo.logd("$TAG onViewCreated")
        tv_name.text = TAG
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        FragmentDemo.logd("$TAG onActivityCreated")
    }

    override fun onStart() {
        super.onStart()
        FragmentDemo.logd("$TAG onStart")
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        FragmentDemo.logd("$TAG setUserVisibleHint isVisibleToUser: $isVisibleToUser")
    }

    override fun onResume() {
        super.onResume()
        FragmentDemo.logd("$TAG onResume")
    }

    override fun onPause() {
        super.onPause()
        FragmentDemo.logd("$TAG onPause")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        FragmentDemo.logd("$TAG onDestroyView")
    }

    override fun onStop() {
        super.onStop()
        FragmentDemo.logd("$TAG onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        FragmentDemo.logd("$TAG onDestroy")
    }
}