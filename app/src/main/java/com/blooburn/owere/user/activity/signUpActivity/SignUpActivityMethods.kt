package com.blooburn.owere.user.activity.signUpActivity

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.blooburn.owere.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class SignUpActivityMethods : AppCompatActivity() {

    private val googleSignInButton: SignInButton by lazy { findViewById(R.id.sign_in_button_google) }

    // Google Login result
    companion object {
        private const val TAG = "GoogleActivity"
        private const val RC_SIGN_IN = 9001
    }

    // Google Api Client
    private lateinit var googleSignInClient: GoogleSignInClient

    // Firebase Auth
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_methods)

// Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.firebase_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Initialize Firebase Auth
        auth = Firebase.auth

        if(auth.currentUser!=null){
            Toast.makeText(this, "기존 아이디를 로그아웃합니다.", Toast.LENGTH_SHORT).show()
            auth.signOut()
        }

        googleSignInButton.setOnClickListener {

            signIn()
        }
    }


    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }


    // 로그인 한 구글 ID로 firebase 사용자 인증 정보 반환,
    // 해당 정보로 firebase 인증
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    updateUI(null)
                }
            }
    }


//은선님의 실수
    //액티비티 시작되었을 때

//    override fun onStart() {
//        super.onStart()
//
    //액티비티 생성되자마자 구글로그인이 되어있는 상황이라서 상호님이 바로 다음액티비티로 넘어가게 되는 상황발생
//        val currentUser = auth.currentUser
//        updateUI(currentUser)
//
//    }

    // onStart말고 onRestart로 옮김으로서 로그인계정 선택 이후에 updateUI함수가 호출되도록 변경
    override fun onRestart() {
        super.onRestart()
        // Check if user is signed in (non-null) and update UI accordingly.

        val currentUser = auth.currentUser
        updateUI(currentUser)
    }


    private fun updateUI(account: FirebaseUser?) {
        //로그인이 되어있다면 다음 액티비티 실행

        if (account != null) {

            val intent: Intent = Intent(this, SignUpActivity1_choice::class.java)
            startActivity(intent)
//        finish()
        } else {
            Log.d(TAG, "Google Sign In is failed: FirebaseUser is null")
        }
    }

    
}



