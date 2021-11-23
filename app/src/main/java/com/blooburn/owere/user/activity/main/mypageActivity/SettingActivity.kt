package com.blooburn.owere.user.activity.main.mypageActivity

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.blooburn.owere.R
import com.blooburn.owere.user.activity.main.mypageActivity.SettingActivity.Companion.firstActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth


class SettingActivity : AppCompatActivity() {
    // MemberQuitDialog에서 SettingActivity를 종료시키기 위해 Activity를 전역변수로 선언
    companion object {
        private const val TAG = "MainActivityForUnKnownUser"
        var firstActivity: Activity? = null
    }

    val auth = FirebaseAuth.getInstance()

    var googleSignInClient: GoogleSignInClient? = null

    private val logOutButton by lazy {
        findViewById<TextView>(R.id.setting_logout)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        if (auth.currentUser == null) {
            this.finish()
        }

        val gso: GoogleSignInOptions =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        logOutButton.setOnClickListener {
            firstActivity = this
            val dialog = MemeberQuitDialog(this, auth, googleSignInClient)
            dialog.myDiag(this, auth)
        }
    }
}

//커스텀다이얼로그 (로그아웃 하실 건가요?)
class MemeberQuitDialog(
    context: Context,
    auth: FirebaseAuth,
    googleSignInClient: GoogleSignInClient?
) {
    private val dialog = Dialog(context)

    private val mgoogleSignInClient: GoogleSignInClient? = googleSignInClient


    fun myDiag(context: Context, auth: FirebaseAuth) {
        // 보여질 화면 결정
        dialog.setContentView(R.layout.logout_dialog_layout)

        //Dialog 크기 설정
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        //영역 밖 터치 처리
        dialog.setCanceledOnTouchOutside(true)
        //취소 가능
        dialog.setCancelable(true)

        val yesButton = dialog.findViewById<TextView>(R.id.logout_yes)
        val noButton = dialog.findViewById<TextView>(R.id.logout_no)

        yesButton.setOnClickListener {
            auth.signOut()
            mgoogleSignInClient?.signOut()

            //프래그먼트 삭제
            dialog.dismiss()

            // 액티비티 종료 -> 로그인 화면으로 돌아감
            firstActivity?.finish()
        }

        dialog.show()
    }
}