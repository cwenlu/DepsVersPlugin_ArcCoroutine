package com.cwl.arccoroutine.customview

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.ArrayAdapter
import android.widget.SimpleAdapter
import by.kirich1409.viewbindingdelegate.viewBinding
import com.cwl.arccoroutine.R
import com.cwl.arccoroutine.databinding.ActivitySimpleUseDragHelper1Binding
import com.cwl.arccoroutine.databinding.ItemHotArticlesBinding
import com.cwl.common.exts.dp2px
import com.cwl.common.widget.recyclerview.divider
import com.cwl.common.widget.recyclerview.multitype2.QuickAdapter2
import com.cwl.common.widget.recyclerview.setupLayoutManager

class SimpleUseDragHelper1Activity : AppCompatActivity() {
    private val viewBinding by viewBinding(ActivitySimpleUseDragHelper1Binding::bind,R.id.root)
    private val adapter by lazy { QuickAdapter2<ItemHotArticlesBinding,String>(ItemHotArticlesBinding::inflate,arrayListOf("a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p")) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple_use_drag_helper1)
        with(viewBinding){
//            lv.adapter=ArrayAdapter<String>(this@SimpleUseDragHelper1Activity,android.R.layout.simple_list_item_1,
//                arrayListOf("a","b","c","d","e","f","g","h","i","j","k","l","m"))
//            root.setScrollableView(lv)

            rv.setupLayoutManager()
            rv.divider(12, Color.GRAY,true)
            adapter.register { commonViewHolder2, data, i ->
                (commonViewHolder2.viewBinding).run {
                    tvTitle.text=data
                }
            }
            rv.adapter=adapter
            root.setScrollableView(rv)
            root.setPeekHeight(dp2px(47f))
        }
    }
}