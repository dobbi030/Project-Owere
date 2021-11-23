package com.blooburn.owere.user.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.blooburn.owere.R
import com.blooburn.owere.designer.activity.main.DesignerMainActivity
import com.blooburn.owere.user.activity.main.UserMainActivity
import com.blooburn.owere.user.activity.signUpActivity.SignUpActivity1_choice
import com.blooburn.owere.util.databaseInstance
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase


/*
* 1. 이 화면에서는 무조건 현재 로그인이 되어 있지 않은 사용자로 간주
* 2. 로그인 혹은 가입하는 계정의 이전 로그인 기록을 찾음
* 3. 이전 로그인 기록이 있다면 해당 계정에 맞는 페이지로,
* 4. 없다면 가입 페이지로 전환
*/


class MainActivityForUnknownUser : AppCompatActivity() {

    var uids: DatabaseReference = databaseInstance.getReference("Uids");

    private val googleSignInButton: SignInButton by lazy { findViewById(R.id.sign_in_button) }

    // Google Login result
    companion object {
        private const val TAG = "MainActivityForUnKnownUser"
        private const val RC_SIGN_IN = 9001
    }

    // Google Api Client
    private lateinit var googleSignInClient: GoogleSignInClient

    // Firebase Auth
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_for_unknown_user)

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.firebase_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Initialize Firebase Auth
        auth = Firebase.auth

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


    private fun updateUI(account: FirebaseUser?) {
        Log.w(TAG, "updateUI (1)")
        if (account != null) {
            // Uids 참조, 자식 노드의 키 값과 현재 유저의 uid 비교
            uids.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var agreement: Boolean = false
                    snapshot.children.forEach {
                        // 로그인이 되어있다면 다음 액티비티 실행 (= uid와 키 값 일치)
                        if (it.key == account.uid) {
                            agreement = true
                            val mode = it.value
                            // 고객 로그인
                            if (mode == "User") {
                                val intent: Intent = Intent(
                                    this@MainActivityForUnknownUser,
                                    UserMainActivity::class.java
                                )
                                startActivity(intent)
                                // 디자이너 로그인
                            } else if (mode == "Designer") {
                                val intent: Intent = Intent(
                                    this@MainActivityForUnknownUser,
                                    DesignerMainActivity::class.java
                                )
                                startActivity(intent)
                            }
                        }
                    }
                    if (!agreement) {
                        // uid와 일치하는 키 값 없음 (= 처음 로그인 하는 유저)
                        val currentUserUid: DatabaseReference = uids.child(account.uid)
                        currentUserUid.setValue("None")
                        val intent: Intent =
                            Intent(
                                this@MainActivityForUnknownUser,
                                SignUpActivity1_choice::class.java
                            )
                        startActivity(intent)
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
//            finish()
        } else {
            Log.d(TAG, "Google Sign In is failed: FirebaseUser is null")
        }
    }
}



