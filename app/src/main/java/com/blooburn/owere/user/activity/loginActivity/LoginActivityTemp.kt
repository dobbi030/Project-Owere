package com.blooburn.owere.user.activity.loginActivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.blooburn.owere.R
import com.blooburn.owere.user.activity.main.UserMainActivity
import com.blooburn.owere.user.item.ChatListItem
import com.blooburn.owere.user.item.UserEntity
import com.blooburn.owere.util.ApplicationForShared
import com.blooburn.owere.util.databaseInstance
//import com.blooburn.owere.util.myId
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


//임시 로그인 회원가입 액티비티
class LoginActivityTemp : AppCompatActivity() {


    private lateinit var auth : FirebaseAuth    //파이어베이스 인증 사용




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_temp)

        auth = Firebase.auth




        initLoginButton()   //로그인버튼 설정
        initSignUpButton()  //회원가입 버튼 설정
        initEmailAndPasswordEditText()  //이메일 패스워드 텍스트 설정


    }



    private fun initLoginButton() {
        val loginButton = findViewById<Button>(R.id.loginButton)
        loginButton.setOnClickListener {
            val email = getInputEmail()
            val password =  getInputPassword()

            //이메일과 패스워드 파라미터로 auth에 있는 signin기능 사용 가능
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->  //task가 완료가 되었는지 리스너 장착
                    if(task.isSuccessful){//로그인이 성공이라면
                        handleSuccessLogin()    //로그인 액티비티 종료
                    }else{
                        Toast.makeText(this, "로그인에 실패했습니다. 이메일 또는 비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show()
                    }
                }
            //나머지는 firebase가 알아서 해줌.

        }
    }
    //회원가입
    private fun initSignUpButton() {
        val signUpButton = findViewById<Button>(R.id.signUpButton)
        signUpButton.setOnClickListener {
            val email = getInputEmail()
            val password =  getInputPassword()

            //signUp기능 사용
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) {task ->
                    if(task.isSuccessful){
                        Toast.makeText(this, "회원가입에 성공했습니다. 로그인 버튼을 눌러 로그인해주세요.", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(this, "이미 가입한 이메일이거나, 회원가입에 실패했습니다.", Toast.LENGTH_SHORT).show()
                    }
                }

        }
    }
    //비었을 때 예외처리
    private fun initEmailAndPasswordEditText(){
        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val signUpButton = findViewById<Button>(R.id.signUpButton)

        emailEditText.addTextChangedListener {
            val enable = emailEditText.text.isNotEmpty() && passwordEditText.text.isNotEmpty()  //비어있지않을 때만 true값 반환
            loginButton.isEnabled = enable
            signUpButton.isEnabled = enable

        }
        passwordEditText.addTextChangedListener {
            val enable = emailEditText.text.isNotEmpty() && passwordEditText.text.isNotEmpty()
            loginButton.isEnabled = enable
            signUpButton.isEnabled = enable

        }
    }


    private fun getInputEmail() : String{
        return findViewById<EditText>(R.id.emailEditText).text.toString()
    }
    private fun getInputPassword() : String{
        return findViewById<EditText>(R.id.passwordEditText).text.toString()
    }



    //로그인 한 후
    private fun handleSuccessLogin(){
        if(auth.currentUser == null){   //currentUser는 null이 가능하므로 그럴경우 실패
            Toast.makeText(this, "로그인에 실패했습니다.", Toast.LENGTH_SHORT).show()
            return
        }
        //성공했다면 유저id가져오기
        val userId = auth.currentUser?.uid.orEmpty()    //userid가져오기 //null일경우를 대비해서 orEmpty
        //reference에서 Users라는 child를 선택
        val currentUserDB = Firebase.database.reference.child("Users").child(userId)
        val user = mutableMapOf<String, Any>()
        user["userId"] = userId
        currentUserDB.updateChildren(user)      //제일 상위에 DB 안에Users라는 List가 생기고 그안에
        // userId라는 항목으로 오브젝트가 생기고 그안에 user가 저장 유저는 userId를 가지고잇음
        //Users라는 키도 따로 빼둠.

        // 패키지 변수에 유저 아이디 저장
//        myId = userId



//        var userEntity : UserEntity?
//        var userName : String?= null
//        currentUserDB.addListenerForSingleValueEvent(object : ValueEventListener{
//            override fun onDataChange(snapshot: DataSnapshot) {
//                userEntity = snapshot.getValue(UserEntity::class.java)
//                userName = userEntity!!.myName
//
//
//
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//
//            }
//
//        })

//        //로그인 정보 저장
//        ApplicationForShared.prefs.apply {
//            setShared("userId",userId)
//            setShared("userName", userName!!)
//        }


        val intent = Intent(this, UserMainActivity::class.java) //유저 메인페이지로 이동.
        startActivity(intent)
        finish()

    }

}



