package com.kengz.sevenminworkoutapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.kengz.sevenminworkoutapp.databinding.ActivityBmiBinding
import java.math.BigDecimal
import java.math.RoundingMode

class BmiActivity : AppCompatActivity() {

    companion object {
        private const val METRIC_UNITS_VIEW = "METRIC_UNITS_VIEW"
        private const val US_UNITS_VIEW = "US_UNITS_VIEW"
    }

    private lateinit var binding: ActivityBmiBinding
    private var currentViewVisibleView: String = METRIC_UNITS_VIEW;

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityBmiBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarBmiActivity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "CALCULATE BMI"

        binding.toolbarBmiActivity.setNavigationOnClickListener {
            onBackPressed()
        }

        makeVisibleMetricView()

        binding.rgUnits.setOnCheckedChangeListener { _, checkedId: Int ->
            if (checkedId == R.id.rbMetricUnits) {
                makeVisibleMetricView()
            } else {
                makeVisibleUsMetricView()
            }
        }

        binding.btnCalculateUnits.setOnClickListener {
            calculateUnits()
        }
    }

    private fun makeVisibleMetricView() {
        currentViewVisibleView = METRIC_UNITS_VIEW
        binding.tilMetricUnitHeight.visibility = View.VISIBLE
        binding.tilMetricUnitWeight.visibility = View.VISIBLE
        binding.tilUsUnitHeightFeet.visibility = View.GONE
        binding.tilUsUnitHeightInch.visibility = View.GONE
        binding.tilUsUnitWeight.visibility = View.GONE

        binding.etMetricUnitHeight.text?.clear()
        binding.etMetricUnitWeight.text?.clear()
        binding.llDiplayBMIResult.visibility = View.INVISIBLE
    }

    private fun makeVisibleUsMetricView() {
        currentViewVisibleView = US_UNITS_VIEW
        binding.tilMetricUnitHeight.visibility = View.GONE
        binding.tilMetricUnitWeight.visibility = View.GONE
        binding.tilUsUnitHeightFeet.visibility = View.VISIBLE
        binding.tilUsUnitHeightInch.visibility = View.VISIBLE
        binding.tilUsUnitWeight.visibility = View.VISIBLE

        binding.etUsUnitHeightFeet.text?.clear()
        binding.etUsUnitHeightInch.text?.clear()
        binding.etUsUnitWeight.text?.clear()
        binding.llDiplayBMIResult.visibility = View.INVISIBLE
    }

    private fun displayBMIResult(bmi: Float) {
        val bmiLabel: String
        val bmiDescription: String

        if (bmi.compareTo(15f) <= 0) {
            bmiLabel = "Very severely underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat more!"
        } else if (bmi.compareTo(15f) > 0 && bmi.compareTo(16f) <= 0
        ) {
            bmiLabel = "Severely underweight"
            bmiDescription = "Oops!You really need to take better care of yourself! Eat more!"
        } else if (bmi.compareTo(16f) > 0 && bmi.compareTo(18.5f) <= 0
        ) {
            bmiLabel = "Underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat more!"
        } else if (bmi.compareTo(18.5f) > 0 && bmi.compareTo(25f) <= 0
        ) {
            bmiLabel = "Normal"
            bmiDescription = "Congratulations! You are in a good shape!"
        } else if (bmi.compareTo(25f) > 0 && bmi.compareTo(
                30f
            ) <= 0
        ) {
            bmiLabel = "Overweight"
            bmiDescription = "Oops! You really need to take care of your yourself! Workout maybe!"
        } else if (bmi.compareTo(30f) > 0 && bmi.compareTo(35f) <= 0
        ) {
            bmiLabel = "Obese Class | (Moderately obese)"
            bmiDescription = "Oops! You really need to take care of your yourself! Workout maybe!"
        } else if (bmi.compareTo(35f) > 0 && bmi.compareTo(40f) <= 0
        ) {
            bmiLabel = "Obese Class || (Severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        } else {
            bmiLabel = "Obese Class ||| (Very Severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        }

        val bmiValue = BigDecimal(bmi.toDouble()).setScale(2, RoundingMode.HALF_EVEN).toString()

        binding.llDiplayBMIResult.visibility = View.VISIBLE
        binding.tvBMIValue.text = bmiValue
        binding.tvBMIType.text = bmiLabel
        binding.tvBMIDescription.text = bmiDescription
    }

    private fun validateMetricUnits(): Boolean {
        var isValid = true
        if (binding.etMetricUnitWeight.text.toString().isEmpty()) {
            isValid = false
        } else if (binding.etMetricUnitHeight.text.toString().isEmpty()) {
            isValid = false
        }
        return isValid
    }

    private fun calculateUnits() {
        if (currentViewVisibleView == METRIC_UNITS_VIEW) {
            if (validateMetricUnits()) {
                val heightVal: Float = binding.etMetricUnitHeight.text.toString().toFloat() / 100
                val weightVal: Float = binding.etMetricUnitWeight.text.toString().toFloat()
                val bmi = weightVal / (heightVal * heightVal)
                displayBMIResult(bmi)
            } else {
                Toast.makeText(this@BmiActivity, "Please enter valid values", Toast.LENGTH_SHORT)
                    .show()
            }
        } else {
            if (validateUsUnits()) {
                val heightValFeet: Float = binding.etUsUnitHeightFeet.text.toString().toFloat()
                val heightValInch: Float = binding.etUsUnitHeightInch.text.toString().toFloat()
                val weightVal: Float = binding.etUsUnitWeight.text.toString().toFloat()
                val heightVal = heightValFeet + heightValInch * 12
                val bmi = 703 * (weightVal / (heightVal * heightVal))
                displayBMIResult(bmi)
            } else {
                Toast.makeText(this@BmiActivity, "Please enter valid values", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun validateUsUnits(): Boolean {
        var isValid = true

        when {
            binding.etUsUnitWeight.text.toString().isEmpty() -> {
                isValid = false
            }
            binding.etUsUnitHeightFeet.text.toString().isEmpty() -> {
                isValid = false
            }
            binding.etUsUnitHeightInch.text.toString().isEmpty() -> {
                isValid = false
            }
        }
        return isValid
    }
}
