package com.udacity.project4.authentication

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.udacity.project4.databinding.ActivityAuthenticationBinding
import com.udacity.project4.locationreminders.RemindersActivity

/**
 * This class should be the starting point of the app, It asks the users to sign in / register, and redirects the
 * signed in users to the RemindersActivity.
 */
class AuthenticationActivity : AppCompatActivity() {
    lateinit var binding: ActivityAuthenticationBinding
    private lateinit var viewModel: AuthenticationViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthenticationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(AuthenticationViewModel::class.java)
//         TODO: Implement the create account and sign in using FirebaseUI, use sign in using email and sign in using Google

//          TODO: If the user was authenticated, send him to RemindersActivity

//          TODO: a bonus is to customize the sign in flow to look nice using :
        //https://github.com/firebase/FirebaseUI-Android/blob/master/auth/README.md#custom-layout

        if (FirebaseAuth.getInstance().currentUser!=null)
            startActivity(Intent(this, RemindersActivity::class.java))


        viewModel.authentication.observe(this) { state ->
            if (state != null) {
                Toast.makeText(this, "", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, RemindersActivity::class.java))
            } else
                Toast.makeText(this, "", Toast.LENGTH_SHORT).show()

        }

        binding.button.setOnClickListener { launchSignInFlow() }

    }

    private fun launchSignInFlow() {
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(), AuthUI.IdpConfig.GoogleBuilder().build()
        )

        val firebaseIntent = AuthUI.getInstance()
            .createSignInIntentBuilder().setAvailableProviders(providers)
            .build()

        activityResultLauncher.launch(
            firebaseIntent
        )


//        startActivityForResult(
//            AuthUI.getInstance()
//                .createSignInIntentBuilder().setAvailableProviders(providers)
//                .build(), SIGN_IN_REQUEST_CODE
//        )
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == SIGN_IN_REQUEST_CODE) {
//            val response = IdpResponse.fromResultIntent(data)
//            if (resultCode == Activity.RESULT_OK) {
//                // Successfully signed in user.
//                Log.i(
//                    ContentValues.TAG,
//                    "Successfully signed in user " +
//                            "${FirebaseAuth.getInstance().currentUser?.displayName}!"
//                )
//            } else {
//                // Sign in failed. If response is null the user canceled the sign-in flow using
//                // the back button. Otherwise check response.getError().getErrorCode() and handle
//                // the error.
//                Log.i(ContentValues.TAG, "Sign in unsuccessful ${response?.error?.errorCode}")
//            }
//        }
//    }

    private val activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

            val data: Intent? = result.data
            val response = IdpResponse.fromResultIntent(data)
            if (result.resultCode == Activity.RESULT_OK) {
                // Successfully signed in user.
                startActivity(Intent(this, RemindersActivity::class.java))
                Log.i(
                    ContentValues.TAG,
                    "Successfully signed in user " +
                            "${FirebaseAuth.getInstance().currentUser?.displayName}!"
                )
            } else {
                // Sign in failed. If response is null the user canceled the sign-in flow using
                // the back button. Otherwise check response.getError().getErrorCode() and handle
                // the error.
                Log.i(ContentValues.TAG, "Sign in unsuccessful ${response?.error?.errorCode}")
            }

        }

    enum class AuthenticationState {
        AUTHENTICATED, UNAUTHENTICATED
    }
}
