package com.argora.app.main

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import com.argora.app.R
import com.argora.app.data.NewHolding
import com.argora.app.data.StockMatch
import com.argora.app.databinding.DialogAddHoldingBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddHoldingBottomSheet : BottomSheetDialogFragment() {

    private var _binding: DialogAddHoldingBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels {
        MainViewModel.MainViewModelFactory(requireActivity().application)
    }

    private var selectedStock: StockMatch? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = DialogAddHoldingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAutoCompleteAdapter()
        setupListeners()
        setupObservers()
    }

    private fun setupAutoCompleteAdapter() {
        val adapter = ArrayAdapter<StockMatch>(
            requireContext(),
            R.layout.dropdown_menu_item,
            mutableListOf()
        )
        binding.actvStockSearch.setAdapter(adapter)
    }

    private fun setupListeners() {
        binding.actvStockSearch.doOnTextChanged { text, _, _, _ ->
            text?.toString()?.let { query ->
                if (query.length >= 2) {
                    viewModel.searchStock(query)
                } else {
                    binding.actvStockSearch.setAdapter(ArrayAdapter(requireContext(), R.layout.dropdown_menu_item, emptyList<StockMatch>()))
                }
            }
            selectedStock = null
        }

        binding.actvStockSearch.setOnItemClickListener { _, _, position, _ ->
            val adapter = binding.actvStockSearch.adapter as ArrayAdapter<StockMatch>
            selectedStock = adapter.getItem(position)
        }

        binding.etPurchaseDate.setOnClickListener {
            showDatePicker()
        }

        binding.btnSave.setOnClickListener {
            saveHolding()
        }
    }

    private fun setupObservers() {
        viewModel.searchResults.observe(viewLifecycleOwner) { matches ->
            val adapter = ArrayAdapter(requireContext(), R.layout.dropdown_menu_item, matches)
            binding.actvStockSearch.setAdapter(adapter)
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()

        // Use default DatePickerDialog without any custom theme
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance().apply {
                    set(year, month, dayOfMonth)
                }
                val format = SimpleDateFormat("yyyy-MM-dd", Locale.US)
                binding.etPurchaseDate.setText(format.format(selectedDate.time))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.show()
    }

    private fun saveHolding() {
        if (selectedStock == null) {
            Toast.makeText(context, "Please select a stock from the search results", Toast.LENGTH_SHORT).show()
            return
        }

        val quantity = binding.etQuantity.text.toString().toDoubleOrNull()
        val avgPrice = binding.etAvgBuyPrice.text.toString().toDoubleOrNull()
        val date = binding.etPurchaseDate.text.toString()

        if (quantity == null || avgPrice == null || date.isEmpty()) {
            Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val newHolding = NewHolding(
            name = selectedStock!!.name,
            ticker = selectedStock!!.symbol,
            quantity = quantity,
            avgBuyPrice = avgPrice,
            purchaseDate = date
        )

        viewModel.addHolding(newHolding)
        dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}