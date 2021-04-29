package com.cwl.arccoroutine.customview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import by.kirich1409.viewbindingdelegate.viewBinding
import com.cwl.arccoroutine.R
import com.cwl.arccoroutine.databinding.ActivityLayoutManagerTestBinding
import com.cwl.arccoroutine.databinding.ItemTextBinding
import com.cwl.common.widget.recyclerview.multitype2.QuickAdapter2
import com.cwl.customviewbox.LayoutManager.StackLayoutManager

class LayoutManagerTestActivity : AppCompatActivity() {
    private val vb by viewBinding(ActivityLayoutManagerTestBinding::bind,R.id.root)

    private val texts = arrayListOf<String>("A", "B", "C","D","E","F","G","H","I","J","K","L","N","P","Q","R","S","T","U","V","W","X","Y","Z")
    private val adapter by lazy{
        QuickAdapter2<ItemTextBinding,String>(ItemTextBinding::inflate, texts).apply {
            register { commonViewHolder2, s, i ->
                with(commonViewHolder2.viewBinding) {
                    root.text = s
                }
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_layout_manager_test)
        with(vb){
            rvList.layoutManager= StackLayoutManager()
            rvList.adapter=adapter
        }
    }
}