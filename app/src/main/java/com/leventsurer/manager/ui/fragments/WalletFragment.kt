package com.leventsurer.manager.ui.fragments

import android.os.Bundle
import android.os.Environment
import android.provider.SyncStateContract
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.leventsurer.manager.MainActivity
import com.leventsurer.manager.R
import com.leventsurer.manager.data.model.FinancialEventModel
import com.leventsurer.manager.data.model.Resource
import com.leventsurer.manager.data.model.UserModel
import com.leventsurer.manager.databinding.FragmentWalletBinding
import com.leventsurer.manager.tools.adapters.DuesPaymentStatusAdapter
import com.leventsurer.manager.tools.adapters.FinancialEventsDetailAdapter
import com.leventsurer.manager.tools.helpers.HeaderHelper
import com.leventsurer.manager.viewModels.AuthViewModel
import com.leventsurer.manager.viewModels.DatabaseViewModel
import com.leventsurer.manager.viewModels.SharedPreferencesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.apache.poi.ss.usermodel.*
import org.apache.poi.ss.util.CellUtil.createCell
import org.apache.poi.xssf.usermodel.IndexedColorMap
import org.apache.poi.xssf.usermodel.XSSFColor
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

@AndroidEntryPoint
class WalletFragment : Fragment() {
    private var _binding: FragmentWalletBinding? = null
    private val binding: FragmentWalletBinding get() = _binding!!
    private val databaseViewModel by viewModels<DatabaseViewModel>()
    private val authViewModel by viewModels<AuthViewModel>()
    private val sharedPreferencesViewModel by viewModels<SharedPreferencesViewModel>()
    //Adapter list
    private var duesPaymentStatusAdapterList = ArrayList<UserModel>()
    private var financialEventAdapterList = ArrayList<FinancialEventModel>()
    //Adapters
    private lateinit var duesPaymentStatusAdapter : DuesPaymentStatusAdapter
    private lateinit var financialEventsAdapter:FinancialEventsDetailAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWalletBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        setupDuesPaymentStatusAdapter()
        setupFinancialEventsDetailAdapter()
        getUsers()
        getFinancialEvents()
        getApartmentBudget()
        observeApartmentData()
        onClickListener()
    }

    private fun onClickListener() {
        binding.apply {
            btnCreatePdfFile.setOnClickListener {
                fun createSheetHeader(cellStyle: CellStyle, sheet: Sheet) {
                    //setHeaderStyle is a custom function written below to add header style

                    //Create sheet first row
                    val row = sheet.createRow(0)

                    //Header list
                    val HEADER_LIST = listOf("column_1", "column_2", "column_3")

                    //Loop to populate each column of header row
                    for ((index, value) in HEADER_LIST.withIndex()) {

                        val columnWidth = (15 * 500)

                        //index represents the column number
                        sheet.setColumnWidth(index, columnWidth)

                        //Create cell
                        val cell = row.createCell(index)

                        //value represents the header value from HEADER_LIST
                        cell?.setCellValue(value)

                        //Apply style to cell
                        cell.cellStyle = cellStyle
                    }
                }
                fun getHeaderStyle(workbook: Workbook): CellStyle {

                    //Cell style for header row
                    val cellStyle: CellStyle = workbook.createCellStyle()

                    //Apply cell color
                    val colorMap: IndexedColorMap = (workbook as XSSFWorkbook).stylesSource.indexedColors
                    var color = XSSFColor(IndexedColors.RED, colorMap).indexed
                    cellStyle.fillForegroundColor = color
                    cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND)

                    //Apply font style on cell text
                    val whiteFont = workbook.createFont()
                    color = XSSFColor(IndexedColors.WHITE, colorMap).indexed
                    whiteFont.color = color
                    whiteFont.bold = true
                    cellStyle.setFont(whiteFont)


                    return cellStyle
                }
                fun addData(rowIndex: Int, sheet: Sheet) {

                    //Create row based on row index
                    val row = sheet.createRow(rowIndex)

                    //Add data to each cell
                    createCell(row, 0, "value 1") //Column 1
                    createCell(row, 1, "value 2") //Column 2
                    createCell(row, 2, "value 3") //Column 3
                }
                fun createWorkbook(): Workbook {
                    // Creating excel workbook
                    val workbook = XSSFWorkbook()

                    //Creating first sheet inside workbook
                    //Constants.SHEET_NAME is a string value of sheet name
                    val sheet: Sheet = workbook.createSheet("SheetName")

                    //Create Header Cell Style
                    val cellStyle = getHeaderStyle(workbook)

                    //Creating sheet header row
                    createSheetHeader(cellStyle, sheet)

                    //Adding data to the sheet
                    addData(0, sheet)

                    return workbook
                }


                fun createExcel(workbook: Workbook) {

                    //Get App Director, APP_DIRECTORY_NAME is a string
                    val appDirectory =  File( Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "Manager")//requireContext().getExternalFilesDir(Constants.APP_DIRECTORY_NAME)

                    //Check App Directory whether it exists or not, create if not.
                    if (appDirectory != null && !appDirectory.exists()) {
                        appDirectory.mkdirs()
                    }

                    //Create excel file with extension .xlsx
                    val excelFile = File(appDirectory,"Kasa.xlsx")

                    //Write workbook to file using FileOutputStream
                    try {
                        val fileOut = FileOutputStream(excelFile)
                        workbook.write(fileOut)
                        fileOut.close()
                        Toast.makeText(requireContext(),"Belge Kaydedildi",Toast.LENGTH_SHORT).show()
                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }

                val myWorkBook = createWorkbook()
                createExcel(myWorkBook)
            }
        }
    }

    private fun getApartmentBudget() {
        databaseViewModel.getApartmentInfo()

    }

    private fun observeApartmentData() {
        databaseViewModel.apartmentLiveData.observe(viewLifecycleOwner){
            when(it){
                is Resource.Loading ->{}
                is Resource.Failure ->{}
                is Resource.Success ->{
                    binding.twCurrentBudget.text = it.result.budget.toString()
                }
                else->{}
            }
        }
    }

    private fun getUsers() {
        databaseViewModel.getAllApartmentUsers()
        observeUsers()
    }

    private fun getFinancialEvents(){
        databaseViewModel.getFinancialEvents()
        observeFinancialEvents()
    }

    private fun observeFinancialEvents() {
        viewLifecycleOwner.lifecycleScope.launch {
            databaseViewModel.financialEventsFlow.collect {
                when (it) {
                    is Resource.Failure -> {
                        Toast.makeText(context, it.exception.message, Toast.LENGTH_LONG).show()

                    }
                    is Resource.Loading -> {
                    }
                    is Resource.Success -> {
                        financialEventAdapterList = it.result
                        financialEventsAdapter.list = financialEventAdapterList
                    }
                    else -> {

                    }
                }
            }

        }
    }

    private fun observeUsers() {
        viewLifecycleOwner.lifecycleScope.launch {
            databaseViewModel.users.collect {
                when (it) {
                    is Resource.Failure -> {
                        Toast.makeText(context, it.exception.message, Toast.LENGTH_LONG).show()

                    }
                    is Resource.Loading -> {
                    }
                    is Resource.Success -> {
                        duesPaymentStatusAdapterList = it.result
                        duesPaymentStatusAdapter.list = duesPaymentStatusAdapterList
                    }
                    else -> {

                    }
                }
            }

        }
    }

    //Aidat ödeme durumunun listeleneceği adapterın kurulumunu yapar
    private fun setupDuesPaymentStatusAdapter() {
        binding.rwDuesPaymentStatus.layoutManager = LinearLayoutManager(requireContext())
        duesPaymentStatusAdapter = DuesPaymentStatusAdapter()
        binding.rwDuesPaymentStatus.adapter = duesPaymentStatusAdapter
    }
    private fun setupFinancialEventsDetailAdapter(){
        binding.rwFinancialEvents.layoutManager = LinearLayoutManager(requireContext())
        financialEventsAdapter = FinancialEventsDetailAdapter()
        binding.rwFinancialEvents.adapter = financialEventsAdapter
    }


    private fun setupUi() {
        HeaderHelper.customHeader(
            binding.customHeader,
            title = "Cüzdan",
            startIconVisibility = true,
            endIconVisibility = true,
            startIcon = R.drawable.ic_baseline_sensor_door_24,
            endIcon = R.drawable.ic_baseline_settings_24,
            startIconClick = {
                authViewModel.logout()
                sharedPreferencesViewModel.clearSharedPref()
                val action = WalletFragmentDirections.actionWalletFragmentToLoginFragment()
                findNavController().navigate(action)
            },
            endIconClick = {
                val action = WalletFragmentDirections.actionWalletFragmentToSettingsFragmet()
                findNavController().navigate(action)
            },
        )
        (requireActivity() as MainActivity).showBottomNavigation()
    }

}