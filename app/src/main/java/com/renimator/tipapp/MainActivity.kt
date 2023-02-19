package com.renimator.tipapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import com.renimator.tipapp.databinding.ActivityMainBinding
import java.text.NumberFormat
import kotlin.math.ceil

class MainActivity : AppCompatActivity() {

    private val VISIBLE = "visible"
    private val GONE = "gone"

    private var groupState = "gone"

    lateinit private var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.calculateButton.setOnClickListener {
            calculateTip2()
        }

        binding.splitSwitch.setOnClickListener {
            if(groupState == GONE) visibilityChange(View.VISIBLE, VISIBLE)
            else visibilityChange(View.GONE, GONE)
        }
    }

    private fun visibilityChange(visibility: Int, state: String) {
        binding.groupSize.visibility = visibility
        groupState = state
    }

    /**
     *
     *     standart function for tip calculation
     *
     */
    private fun calculateTip() {
        val stringInTextField = binding.costOfService.text.toString()
        val cost = stringInTextField.toDouble()
        val selectedId = binding.tipOptions.checkedRadioButtonId

        val tipPercentage = when(selectedId) {
            R.id.option_twenty_percent -> 0.20
            R.id.option_eighteen_percent -> 0.18
            else -> 0.15
        }

        var tip = cost * tipPercentage
        if(binding.roundUpSwitch.isChecked) {
            tip = ceil(tip)
        }

        val formattedTip = NumberFormat.getCurrencyInstance().format(tip)
        binding.tipResult.text = getString(R.string.tip_amount, formattedTip)
    }

    /**


        function for showing tip for service +++++ how much money should spend each person in a group
            including counted tip  (need further code refactoring)

     */

    private fun calculateTip2() {
        val stringInTextField = binding.costOfService.text.toString()
        val cost = stringInTextField.toDouble()

        val tipPercentage = when(binding.tipOptions.checkedRadioButtonId) {
            R.id.option_twenty_percent -> 0.20
            R.id.option_eighteen_percent -> 0.18
            else -> 0.15
        }

        var tip = cost * tipPercentage

        if(binding.roundUpSwitch.isChecked) {
            val formattedTip = NumberFormat.getCurrencyInstance().format(ceil(tip))
            binding.tipResult.text = getString(R.string.tip_amount, formattedTip)
        } else {
            val formattedTip = NumberFormat.getCurrencyInstance().format(tip)
            binding.tipResult.text = getString(R.string.tip_amount, formattedTip)
        }


        Log.d("CheckData", "${binding.groupSize.text.toString() != null}")
        Log.d("CheckData", binding.groupSize.text.toString())
        if(groupState == VISIBLE && !binding.groupSize.text.toString().equals("")) {
            val groupSize: Double = binding.groupSize.text.toString().toDouble()
                Log.d("CheckData", groupSize.toString())
                val personalExpenses = NumberFormat.getCurrencyInstance().format((tip + cost) / groupSize)
                binding.costPerPerson.text = getString(R.string.cost_per_person, personalExpenses)
        } else {
            binding.costPerPerson.text = ""
            Toast.makeText(this, "Fill in the group size field", Toast.LENGTH_SHORT).show()
        }
    }
}