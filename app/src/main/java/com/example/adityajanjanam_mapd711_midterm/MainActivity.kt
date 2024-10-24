package com.example.adityajanjanam_mapd711_midterm

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPref = getSharedPreferences("BloodSugarConverter", MODE_PRIVATE)

        // Initialize UI elements
        val nameEditText: EditText = findViewById(R.id.editTextName)
        val ageEditText: EditText = findViewById(R.id.editTextAge)
        val genderMale: RadioButton = findViewById(R.id.radioMale)
        val bloodSugarEditText: EditText = findViewById(R.id.editTextBloodSugar)
        val unitSpinner: Spinner = findViewById(R.id.spinnerUnit)
        val exerciseSpinner: Spinner = findViewById(R.id.spinnerExercise)
        val convertButton: Button = findViewById(R.id.buttonConvert)

        convertButton.setOnClickListener {
            // Gather input data
            val name = nameEditText.text.toString().trim()
            val ageInput = ageEditText.text.toString().trim()
            val bloodSugarInput = bloodSugarEditText.text.toString().trim()

            // Validate inputs
            if (!validateInputs(name, ageInput, bloodSugarInput, nameEditText, ageEditText, bloodSugarEditText)) return@setOnClickListener

            // Parse age and blood sugar input safely
            val age = ageInput.toIntOrNull() ?: run {
                ageEditText.error = "Invalid age value"
                return@setOnClickListener
            }

            val bloodSugar = bloodSugarInput.toDoubleOrNull() ?: run {
                bloodSugarEditText.error = "Invalid blood sugar value"
                return@setOnClickListener
            }

            // Get spinner selections safely
            val unit = unitSpinner.selectedItem?.toString() ?: "mmol/l" // Default value
            val exercise = exerciseSpinner.selectedItem?.toString() ?: "None" // Default value

            // Convert blood sugar value based on the unit
            val convertedValue = when (unit) {
                "mmol/l" -> bloodSugar * 18
                else -> bloodSugar / 18
            }

            // Store the data in SharedPreferences
            storeDataInPreferences(name, age, genderMale.isChecked, convertedValue, exercise)

            // Start the ResultActivity
            startActivity(Intent(this, ResultActivity::class.java))
        }
    }

    private fun validateInputs(
        name: String,
        ageInput: String,
        bloodSugarInput: String,
        nameEditText: EditText,
        ageEditText: EditText,
        bloodSugarEditText: EditText
    ): Boolean {
        var isValid = true

        if (name.isEmpty()) {
            nameEditText.error = "Please enter your name"
            isValid = false
        }

        if (ageInput.isEmpty()) {
            ageEditText.error = "Please enter your age"
            isValid = false
        } else if (ageInput.toIntOrNull() == null) {
            ageEditText.error = "Please enter a valid age"
            isValid = false
        }

        if (bloodSugarInput.isEmpty()) {
            bloodSugarEditText.error = "Please enter a blood sugar value"
            isValid = false
        } else if (bloodSugarInput.toDoubleOrNull() == null) {
            bloodSugarEditText.error = "Please enter a valid blood sugar value"
            isValid = false
        }

        return isValid
    }

    private fun storeDataInPreferences(name: String, age: Int, isMale: Boolean, bloodSugar: Double, exercise: String) {
        val editor = sharedPref.edit()
        editor.putString("name", name)
        editor.putInt("age", age)  // Store age as Int
        editor.putString("gender", if (isMale) "Male" else "Female")
        editor.putFloat("bloodSugar", bloodSugar.toFloat())
        editor.putString("exercise", exercise)
        editor.putBoolean("loadWebsite", true) // Save website preference if needed
        editor.apply()
    }
}
