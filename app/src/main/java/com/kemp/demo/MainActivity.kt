package com.kemp.demo

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.kemp.demo.adapter.MainAdapter
import com.kemp.demo.adapter.MainItemData
import java.text.Collator
import java.util.*

class MainActivity : AppCompatActivity(), MainAdapter.OnItemClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var path: String? = intent.getStringExtra("com.kemp.demo.Path")

        if (path == null) {
            path = ""
        }

        val mainAdapter = MainAdapter(this, getData(path))
        mainAdapter.onItemClickListener = this
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = mainAdapter
        }
    }

    private fun getData(prefix: String): List<MainItemData> {
        val myData = ArrayList<MainItemData>()

        val mainIntent = Intent(Intent.ACTION_MAIN, null)
        mainIntent.addCategory("com.kemp.demo.MAIN_ACTIVITY")

        val pm = packageManager
        val list = pm.queryIntentActivities(mainIntent, 0) ?: return myData

        val prefixPath: Array<String>?
        var prefixWithSlash = prefix

        if (prefix == "") {
            prefixPath = null
        } else {
            prefixPath = prefix.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            prefixWithSlash = "$prefix/"
        }

        val len = list.size

        val entries = HashMap<String, Boolean>()

        for (i in 0 until len) {
            val info = list[i]
            val labelSeq = info.loadLabel(pm)
            val label = labelSeq?.toString() ?: info.activityInfo.name

            if (prefixWithSlash.length == 0 || label.startsWith(prefixWithSlash)) {

                val labelPath = label.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

                val nextLabel = if (prefixPath == null) labelPath[0] else labelPath[prefixPath.size]

                if (prefixPath?.size ?: 0 == labelPath.size - 1) {
                    addItem(myData, nextLabel, activityIntent(
                            info.activityInfo.applicationInfo.packageName,
                            info.activityInfo.name))
                } else {
                    if (entries[nextLabel] == null) {
                        addItem(myData, nextLabel, browseIntent(if (prefix == "") nextLabel else "$prefix/$nextLabel"))
                        entries[nextLabel] = true
                    }
                }
            }
        }

        Collections.sort(myData, sDisplayNameComparator)

        return myData
    }

    private val sDisplayNameComparator = object : Comparator<MainItemData> {
        private val collator = Collator.getInstance()

        override fun compare(map1: MainItemData, map2: MainItemData): Int {
            return collator.compare(map1.title, map2.title)
        }
    }

    private fun activityIntent(pkg: String, componentName: String): Intent {
        val result = Intent()
        result.setClassName(pkg, componentName)
        return result
    }

    private fun browseIntent(path: String): Intent {
        val result = Intent()
        result.setClass(this, MainActivity::class.java)
        result.putExtra("com.kemp.demo.Path", path)
        return result
    }

    private fun addItem(data: MutableList<MainItemData>, name: String, intent: Intent) {
        val temp = MainItemData(name, intent)
        data.add(temp)
    }

    override fun onItemClick(mainItemData: MainItemData) {
        startActivity(mainItemData.intent)
    }
}