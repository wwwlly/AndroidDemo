package com.kemp.demo.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import com.kemp.annotations.Description
import com.kemp.demo.R
import com.kemp.demo.base.BaseActivity
import com.kemp.demo.fragment.FragmentA
import com.kemp.demo.fragment.FragmentB
import com.kemp.demo.utils.DebugLog
import kotlinx.android.synthetic.main.activity_fragment.*

/**
 * 缓存的fragment会随着activity执行onResume、onPause
 *
 */
@Description("Fragment声明周期、懒加载")
class FragmentDemo : BaseActivity() {

    private val tabNames = arrayOf("tabA", "tabB", "tabC", "tabD", "tabE", "tabF", "tabG", "tabI")

    companion object {
        fun logd(msg: String?) {
            DebugLog.d(msg)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment)

//        initTabLayout()
        initViewPager()
        tabLayout.setupWithViewPager(viewPager)
    }

    private fun initTabLayout() {
        for (name in tabNames) {
            val tab = tabLayout.newTab().setText(name)
            tabLayout.addTab(tab)
        }
    }

    private fun initViewPager() {
        //设置缓存页数 即预加载
//        viewPager.offscreenPageLimit = 2
        logd("缓存页数：${viewPager.offscreenPageLimit}")
        viewPager.adapter = object : FragmentPagerAdapter(supportFragmentManager) {
            override fun getCount(): Int {
                return tabNames.size
            }

            override fun getItem(position: Int): Fragment {
                return if (tabNames.size > 1 && position == 1) {
                    FragmentB.getInstance(position)
                } else {
                    FragmentA.getInstance(position)
                }
            }

            override fun getPageTitle(position: Int): CharSequence? {
                return tabNames[position]
            }
        }
    }
}