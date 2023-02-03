package com.acclivousbyte.shopee.view.fragments.filter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.acclivousbyte.shopee.R
import com.acclivousbyte.shopee.utils.AppUtils
import okio.utf8Size

class FilterExpandableListViewAdapter(
    private val context: Context,
    private val headerTitleList: MutableList<String>,
    private val childTitleList: HashMap<String, List<String>>,
    private val headerIcons: MutableList<Int>,
    private val listener: clickListener
): BaseExpandableListAdapter() {

    var isChecked: Boolean = true


    override fun getGroupCount(): Int {
        return headerTitleList.size
    }

    override fun getChildrenCount(p0: Int): Int {
        return this.childTitleList[headerTitleList[p0]]!!.size
    }

    override fun getGroup(p0: Int): Any {
        return headerTitleList[p0]
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        return childTitleList[headerTitleList[groupPosition]]!![childPosition]
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    override fun getGroupView(groupPosition: Int,
                              childPosition: Boolean,
                              p2: View?, p3: ViewGroup?
    ): View? {

        var convertView = p2
        val headerTitle = getGroup(groupPosition) as String

        if (convertView == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = inflater.inflate(R.layout.filter_group_items, null)
        }

        val headerTv = convertView?.findViewById<TextView>(R.id.filter_group_header_title)
        if (headerTv != null) {
            headerTv.text = headerTitle
        }

//        val headerIv = convertView?.findViewById<ImageView>(R.id.filter_group_header_icon)
//        val dd = headerIcons[groupPosition]
//        headerIv?.setImageResource(dd)

        return convertView
    }

    override fun getChildView(groupPosition: Int,
                              childPosition: Int,
                              p2: Boolean,
                              p3: View?,
                              p4: ViewGroup?
    ): View? {
        var convertView = p3
        val childTitle = getChild(groupPosition, childPosition) as String


        if (convertView == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = inflater.inflate(R.layout.filter_child_items, null)
        }

        // title and id split
        val splitTitle = childTitle.split("//")
        val title = splitTitle[0]
        val id = splitTitle[1]

        val childTitleTv = convertView?.findViewById<TextView>(R.id.filter_child_title_tv)

        if (childTitleTv != null) {
            childTitleTv.text = title
        }

        val childLayout = convertView?.findViewById<LinearLayout>(R.id.filter_child_layout)
        val check = convertView?.findViewById<CheckBox>(R.id.fragment_filter_child_check_box)


        if (AppUtils.checkPosition == childPosition) {
            if (check?.isChecked != true) {
                check?.isChecked = true
            }
        } else {
            if (check?.isChecked == true) {
                check.isChecked = false
            }
        }


        childLayout?.setOnClickListener {
            isChecked = false
            listener.onChildClick(childPosition, groupPosition, id.toInt())

//            if (check?.isChecked != true) {
//                check?.isChecked = AppUtils.filterCheck
////                check?.isChecked = true
//            } else {
//                check.isChecked = true
//            }


        }


        return convertView
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }



}


interface clickListener {
    fun onChildClick(childPosition: Int, groupPosition: Int, childId: Int)
}