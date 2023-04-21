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
import com.google.firebase.firestore.FieldValue
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
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.exp

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
    private lateinit var duesPaymentStatusAdapter: DuesPaymentStatusAdapter
    private lateinit var financialEventsAdapter: FinancialEventsDetailAdapter
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
                val myWorkBook = createWorkbook()
                createExcel(myWorkBook)
            }
        }
    }

    private fun createSheetHeader(cellStyle: CellStyle, sheet: Sheet) {
        //setHeaderStyle is a custom function written below to add header style

        //Create sheet first row
        val row = sheet.createRow(0)

        //Header list
        val HEADER_LIST = listOf("column_1", "column_2", "column_3","column_4","column_5")

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

    private fun getHeaderStyle(workbook: Workbook): CellStyle {

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

    private fun createWorkbook(): Workbook {
        // Creating excel workbook
        val workbook = XSSFWorkbook()

        //Creating first sheet inside workbook
        //Constants.SHEET_NAME is a string value of sheet name
        val date = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val currentDate = date.format(Date()).replace("/",".").split(" ")[0]
        val sheet: Sheet = workbook.createSheet(currentDate)

        //Create Header Cell Style
        val cellStyle = getHeaderStyle(workbook)

        //Creating sheet header row
        createSheetHeader(cellStyle, sheet)

        //Sütun Başlıklarının Dosyaya Eklenmesi
        val row = sheet.createRow(0)
        createCell(row, 0, "Gelir ADI") //Column 1
        createCell(row, 1, "Gelir Tutarı")//Column 2
        createCell(row, 2, "Gider Adı") //Column 3
        createCell(row, 3, "Gider Tutarı") //Column 4
        createCell(row, 4, "Aidat Ödemeyenler")//Column 5
        //Gelir Gider Listelerini Oluşturulması
        val expenseList = ArrayList<FinancialEventModel>()
        val incomeList = ArrayList<FinancialEventModel>()
        val paymentStatusList = ArrayList<UserModel>()
        for (financialEvent in financialEventAdapterList) {
            if (financialEvent.isExpense) {
                expenseList.add(financialEvent)
            } else {
                incomeList.add(financialEvent)
            }
        }
        for(user in duesPaymentStatusAdapterList){
            if(!user.duesPaymentStatus){
                paymentStatusList.add(user)
            }
        }
        //En uzun listeye göre sayılı satır oluşturulması
        val rowList = ArrayList<Row>()
        if(expenseList.size>=incomeList.size && expenseList.size >= paymentStatusList.size){
            Log.e("kontrol","expense list en büyük")
            for (i in 0..expenseList.size+1){
                rowList.add(sheet.createRow(i+1))
            }
        }else if(incomeList.size >= expenseList.size && incomeList.size >= paymentStatusList.size){
            Log.e("kontrol","income list en büyük")
            for (i in 0.. expenseList.size+1){
                rowList.add(sheet.createRow(i+1))
            }
        }else if(paymentStatusList.size >= expenseList.size && paymentStatusList.size >= incomeList.size){
            Log.e("kontrol","paymentStatusList list en büyük")
            for (i in 0.. paymentStatusList.size+1){
                rowList.add(sheet.createRow(i+1))
            }
        }

        var incomeRowCount = 0
        var expenseRowCount = 0
        var paymentStatusCount = 0
        Log.e("kontrol","rowList length:${rowList.size}")
        Log.e("kontrol","rowList :${rowList}")
        //Gelir Ve Giderlerin Ait Olduğu Satıra Yazdırılması
        for (i in 0 until incomeList.size) {
            Log.e("kontrol","i değeri:$i")
            createCell(rowList[incomeRowCount], 0, incomeList[i].eventName)
            createCell(rowList[incomeRowCount], 1, incomeList[i].amount.toString())
            incomeRowCount += 1
        }
        for (i in 0 until expenseList.size) {
            createCell(rowList[expenseRowCount], 2, expenseList[i].eventName)
            createCell(rowList[expenseRowCount], 3, expenseList[i].amount.toString())
            expenseRowCount += 1
        }
        for (i in 0 until paymentStatusList.size) {
            createCell(rowList[paymentStatusCount], 4, paymentStatusList[i].fullName)
            paymentStatusCount += 1
        }
        return workbook
    }

    private fun createExcel(workbook: Workbook) {
        //Get App Director, APP_DIRECTORY_NAME is a string
        val appDirectory = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),
            "Manager"
        )//requireContext().getExternalFilesDir("kasa"),"Manager")
        //Check App Directory whether it exists or not, create if not.
        if (!appDirectory.exists()) {
            appDirectory.mkdirs()
        }
        //Create excel file with extension .xlsx
        val date = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val currentDate = date.format(Date()).replace("/",".").split(" ")[0]
        val excelFile = File(appDirectory, "${currentDate}gelirGiderKontrol.xlsx")
        //Write workbook to file using FileOutputStream
        try {
            val fileOut = FileOutputStream(excelFile)
            workbook.write(fileOut)
            fileOut.close()
            Toast.makeText(requireContext(), "Belge Kaydedildi", Toast.LENGTH_SHORT).show()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun getApartmentBudget() {
        databaseViewModel.getApartmentInfo()
    }

    private fun observeApartmentData() {
        databaseViewModel.apartmentLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {}
                is Resource.Failure -> {}
                is Resource.Success -> {
                    binding.twCurrentBudget.text = it.result.budget.toString()
                }
                else -> {}
            }
        }
    }

    private fun getUsers() {
        databaseViewModel.getAllApartmentUsers()
        observeUsers()
    }

    private fun getFinancialEvents() {
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

    private fun setupFinancialEventsDetailAdapter() {
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