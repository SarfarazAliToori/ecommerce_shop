package com.acclivousbyte.shopee.view.fragments.filter

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.acclivousbyte.shopee.R
import com.acclivousbyte.shopee.databinding.FragmentFilterBinding
import com.acclivousbyte.shopee.extensions.setupProgressDialog
import com.acclivousbyte.shopee.models.brands.*
import com.acclivousbyte.shopee.utils.AppUtils
import com.acclivousbyte.shopee.utils.MaterialDialogHelper
import com.acclivousbyte.shopee.view.fragments.base.MainMVVMNavigationFragment
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class FilterFragment :
    MainMVVMNavigationFragment<FilterViewModel>(FilterViewModel::class), clickListener {

    private lateinit var binding: FragmentFilterBinding
    private val filterViewModel by viewModel<FilterViewModel>()
    private val dialogHelper by inject<MaterialDialogHelper>()
    private lateinit var expandableListViewAdapter: FilterExpandableListViewAdapter
    private lateinit var headerList: MutableList<String>
    private lateinit var childList: HashMap<String, List<String>>
    private lateinit var headerIcons: MutableList<Int>

    private lateinit var categoryChildList: MutableList<String>
    private lateinit var manufacturerChildList: MutableList<String>
    private lateinit var makeChildList: MutableList<String>
    private lateinit var modelsChildList: MutableList<String>
    private lateinit var yearsChildList: MutableList<String>

    private lateinit var categoryMutableList: MutableList<BrandsCategory>
    private lateinit var manufacturerMutableList: MutableList<Manufacturer>
    private lateinit var makeMutableList: MutableList<BrandsDetails>
    private lateinit var modelMutableList: MutableList<BrandsModel>
    private lateinit var yearsMutableList: MutableList<BrandsYear>
    private lateinit var expandableList: ExpandableListView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFilterBinding.inflate(inflater, container, false)


        headerList = ArrayList<String>()
        childList = HashMap()
        headerIcons = ArrayList<Int>()

        categoryChildList = mutableListOf()
        manufacturerChildList= mutableListOf()
        makeChildList = mutableListOf()
        modelsChildList = mutableListOf()
        yearsChildList = mutableListOf()

        headerList.add("Categories")
        headerList.add("Manufacturers")
        headerList.add("Make")
        headerList.add("Model")
        headerList.add("Years")

        getListData()
        setupFilter()

        binding.fragmentFilterBackTv.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.fragmentFilterDone.setOnClickListener {
            AppUtils.isFromFilter = true
            findNavController().popBackStack()
        }

        filterViewModel.filterData()
        setupProgressDialog(filterViewModel.showHideProgressDialog, dialogHelper)

        return  binding.root
    }

    private fun setupFilter() {
        expandableListViewAdapter = FilterExpandableListViewAdapter(
            requireContext(),
            headerList,
            childList,
            headerIcons,
            this
        )

        expandableList = binding.fragmentFilterExpendableList
        expandableList.setAdapter(expandableListViewAdapter)

    }

    private fun getListData() {

        categoryMutableList = mutableListOf()
        manufacturerMutableList = mutableListOf()
        makeMutableList = mutableListOf()
        modelMutableList = mutableListOf()
        yearsMutableList = mutableListOf()

        observe(filterViewModel.filterListMutableLiveData) {
            if (!it.consumed) it.consume()?.let { brandsData ->


                brandsData.categories.forEach {
                    categoryMutableList.add(it)
                }
                categoryMutableList.forEach {
                    categoryChildList.add(it.name + "//${it.id}")
                    AppUtils.category_id = it.id
                }


                brandsData.Manufacturers.forEach {
                    manufacturerMutableList.add(it)
                }
                manufacturerMutableList.forEach {
                    manufacturerChildList.add(it.name + "//${it.id}")
                }

                brandsData.brands.forEach {
                    makeMutableList.add(it)
                }
                makeMutableList.forEach {
                    makeChildList.add(it.name + "//${it.id}")
                }

                brandsData.models.forEach {
                    modelMutableList.add(it)
                }
                modelMutableList.forEach {
                    modelsChildList.add(it.name + "//${it.id}")
                }

                brandsData.years.forEach {
                    yearsMutableList.add(it)
                }
                yearsMutableList.forEach {
                    yearsChildList.add(it.year.toString() + "//${it.id}")
                    AppUtils.year_id = it.id
                }

//
//                brandsData.years.forEach {
//                    yearsChildList.add(it.year.toString())
//                }

            }
        }

//        childList[headerList[0]] = categoryChildList
//        childList[headerList[1]] = manufacturerChildList
//        childList[headerList[2]] = makeChildList
//        childList[headerList[3]] = modelsChildList
//        childList[headerList[4]] = yearsChildList

        loadListOfChild()


    }

    private fun loadListOfChild() {
        childList[headerList[0]] = categoryChildList
        childList[headerList[1]] = manufacturerChildList
        childList[headerList[2]] = makeChildList
        childList[headerList[3]] = modelsChildList
        childList[headerList[4]] = yearsChildList
    }


    override fun onChildClick(childPosition: Int, groupPosition: Int, childId: Int) {
        Log.i("ad", "childPositon == $childPosition:: groupPosition ==$groupPosition::  childId == $childId")

//        AppUtils.filterCheck = true
//        AppUtils.checkPosition = childPosition
        /**
         * Query params for Filters in Get Products API:
            query, category_id, manufacturer_id, brand_id, model_id, year_id, page, per_page
         */

        if (groupPosition == 0 || groupPosition == 1 || groupPosition == 2 || groupPosition == 3 || groupPosition == 4) {
            AppUtils.checkPosition = childPosition
        }

        if (groupPosition == 0) {
            manufacturerChildList.clear()
            val manufacturerList = manufacturerMutableList.filter {
                childId == it.categories_ids[0]
            }
            manufacturerList.forEach {
                manufacturerChildList.add(it.name + "//${it.id}")
            }

            expandableList.collapseGroup(1)
            expandableList.expandGroup(1, true)
            loadListOfChild()
        }


        if (groupPosition == 2) {
            modelsChildList.clear()
            val modelList = modelMutableList.filter {
                childId == it.brands_id
            }
            modelList.forEach {
                modelsChildList.add(it.name + "//${it.id}")
            }
            expandableList.collapseGroup(3)
            expandableList.expandGroup(3, true)
            loadListOfChild()
        }

        if (groupPosition == 3) {
            yearsChildList.clear()
            val yearList = yearsMutableList.filter {
                childId == it.models_id
            }
            yearList.forEach {
                yearsChildList.add(it.year.toString() + "//${it.id}")
            }
            expandableList.collapseGroup(4)
            expandableList.expandGroup(4, true)
            loadListOfChild()
        }

        // get id's for Query product filter
        if (groupPosition == 1) {
            AppUtils.manufacturer_id = childId
            Log.i("aa:", "manufacturer_id : ${AppUtils.manufacturer_id}")
        }

        if (groupPosition == 3) {
            AppUtils.brand_id = childId
            Log.i("aa:", "MakeId : ${AppUtils.brand_id}")
        }

        if (groupPosition == 4) {
            AppUtils.model_id = childId
            Log.i("aa:", "ModelId : ${AppUtils.model_id}")
        }


    }


}