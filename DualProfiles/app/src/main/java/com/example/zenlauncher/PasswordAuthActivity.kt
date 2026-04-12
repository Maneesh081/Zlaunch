package com.example.zenlauncher

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.example.zenlauncher.util.PreferenceHelper

class PasswordAuthActivity : AppCompatActivity() {

    companion object {
        const val RESULT_AUTH_SUCCESS = 1001
        const val RESULT_AUTH_FAILED = 1002
    }

    private enum class Mode { SET_PASSWORD, CONFIRM_PASSWORD, ENTER_PASSWORD }

    private lateinit var textTitle: TextView
    private lateinit var textSubtitle: TextView
    private lateinit var editPassword: TextInputEditText
    private lateinit var layoutConfirm: TextInputLayout
    private lateinit var editConfirm: TextInputEditText
    private lateinit var textError: TextView
    private lateinit var btnSubmit: Button
    private lateinit var btnCancel: Button

    private lateinit var mode: Mode
    private var tempPassword = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password_auth)

        textTitle = findViewById(R.id.textTitle)
        textSubtitle = findViewById(R.id.textSubtitle)
        editPassword = findViewById(R.id.editPassword)
        layoutConfirm = findViewById(R.id.layoutConfirm)
        editConfirm = findViewById(R.id.editConfirm)
        textError = findViewById(R.id.textError)
        btnSubmit = findViewById(R.id.btnSubmit)
        btnCancel = findViewById(R.id.btnCancel)

        val passwordSet = PreferenceHelper.isSwitchPasswordSet(this)

        if (!passwordSet) {
            // First time: set password
            mode = Mode.SET_PASSWORD
            textTitle.text = "Set Password"
            textSubtitle.text = "Create a password to switch to default launcher"
            btnSubmit.text = "Next"
        } else {
            // Password already set: enter to authenticate
            mode = Mode.ENTER_PASSWORD
            textTitle.text = "Enter Password"
            textSubtitle.text = "Password required to switch to default launcher"
            btnSubmit.text = "Unlock"
        }

        btnSubmit.setOnClickListener { handleSubmit() }
        btnCancel.setOnClickListener { finish() }
    }

    private fun handleSubmit() {
        val pwd = editPassword.text?.toString()?.trim() ?: ""

        if (pwd.isEmpty()) {
            showError("Password cannot be empty")
            return
        }

        when (mode) {
            Mode.SET_PASSWORD -> {
                // Save temp password and show confirm field
                tempPassword = pwd
                mode = Mode.CONFIRM_PASSWORD
                layoutConfirm.visibility = View.VISIBLE
                textTitle.text = "Confirm Password"
                textSubtitle.text = "Re-enter your password to confirm"
                btnSubmit.text = "Set Password"
                textError.visibility = View.GONE
                editPassword.hint = "Password"
            }

            Mode.CONFIRM_PASSWORD -> {
                val confirm = editConfirm.text?.toString()?.trim() ?: ""
                if (confirm != tempPassword) {
                    showError("Passwords do not match")
                    return
                }
                // Password confirmed - save it
                PreferenceHelper.setSwitchPassword(this, tempPassword)
                Toast.makeText(this, "Password set!", Toast.LENGTH_SHORT).show()
                switchToDefaultLauncher()
            }

            Mode.ENTER_PASSWORD -> {
                val stored = PreferenceHelper.getSwitchPassword(this)
                if (pwd != stored) {
                    showError("Incorrect password")
                    return
                }
                // Authenticated
                Toast.makeText(this, "Authenticated", Toast.LENGTH_SHORT).show()
                switchToDefaultLauncher()
            }
        }
    }

    private fun showError(msg: String) {
        textError.text = msg
        textError.visibility = View.VISIBLE
        textError.setTextColor(getColor(R.color.text_secondary))
    }

    private fun switchToDefaultLauncher() {
        // Launch the default home picker intent
        val homeIntent = Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_HOME)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(homeIntent)
        setResult(RESULT_AUTH_SUCCESS)
        finish()
    }
}
