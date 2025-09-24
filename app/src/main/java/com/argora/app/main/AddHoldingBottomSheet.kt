package com.argora.app.main

import android.R
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
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

        val adapter =
            ArrayAdapter<StockMatch>(requireContext(), R.layout.simple_dropdown_item_1line)
        binding.actvStockSearch.setAdapter(adapter)

        setupListeners(adapter)
        setupObservers(adapter)
    }

    private fun setupListeners(adapter: ArrayAdapter<StockMatch>) {
        binding.actvStockSearch.doOnTextChanged { text, _, _, _ ->
            viewModel.searchStock(text.toString())
            selectedStock = null // Clear selection when text changes
        }

        binding.actvStockSearch.setOnItemClickListener { parent, _, position, _ ->
            selectedStock = parent.getItemAtPosition(position) as StockMatch
        }

        binding.etPurchaseDate.setOnClickListener {
            showDatePicker()
        }

        binding.btnSave.setOnClickListener {
            saveHolding()
        }
    }

    private fun setupObservers(adapter: ArrayAdapter<StockMatch>) {
        viewModel.searchResults.observe(viewLifecycleOwner) { matches ->
            adapter.clear()
            adapter.addAll(matches)
            adapter.notifyDataSetChanged()
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, month, dayOfMonth)
                val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
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
        dismiss() // Close the dialog
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}