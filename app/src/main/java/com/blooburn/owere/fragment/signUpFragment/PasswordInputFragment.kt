package com.blooburn.owere.fragment.signUpFragment

import android.os.Bundle
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import com.blooburn.owere.R
import com.blooburn.owere.databinding.PasswordInputFragmentBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class PasswordInputFragment : Fragment(R.layout.password_input_fragment) {

    private var binding: PasswordInputFragmentBinding? = null
    private var password: String? = null
    private var email: String? = null
    private var customerOrDesigner: String? = null

    private lateinit var auth: FirebaseAuth    //파이어베이스 인증 사용

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth    //파이어베이스 인증 인스턴스 초기화

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        var nextFragment = MyNameInputFragment()    //다음 프래그먼트 정의

        val passwordInputFragmentBinding = PasswordInputFragmentBinding.bind(view)
        binding = passwordInputFragmentBinding //바인딩 초기화


        //이전 프래그먼트에서 전송한 이메일과 고객선택 받아서 초기화
        setFragmentResultListener("email") { key, bundle ->
            bundle.getString("email")?.let {
                email = it
                binding?.trasmitTest?.text = it
            }
            bundle.getString("customerOrDesigner")?.let {
                customerOrDesigner = it
                binding?.trasmitTest2?.text = it
            }
        }

        initPasswordEditText()//패스워드 입력 예외처리(비어있는경우)
        initSignUpButton()  //버튼 누른 후 진행 메소드


    }

    private fun initSignUpButton() {


        //가입하기 버튼 리스너
        binding?.passwordSignupButton!!.setOnClickListener {

            if(auth.currentUser!=null){
                Toast.makeText(context, "기존 아이디를 로그아웃합니다.", Toast.LENGTH_SHORT).show()
                auth.signOut()
            }
            binding?.let {
                password = it.passwordEditText!!.text.toString()    //패스워드 텍스트 값 초기화
            }
            var email = email.toString()
            var password = password.toString()

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(context, "회원가입에 성공했습니다.", Toast.LENGTH_SHORT).show()

                        //나머지는 firebase가 알아서 해줌.

                        //가입후 로그인 실행
                        auth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(requireActivity()) { task ->  //task가 완료가 되었는지 리스너 장착
                                if (task.isSuccessful) {//로그인이 성공이라면
                                    handleSuccessLogin()    //DB 업로드 및 다음프래그먼트로 이동
                                } else {
                                    Toast.makeText(
                                        context,
                                        "로그인에 실패했습니다. 이메일 또는 비밀번호를 확인해주세요.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                    } else {
                        Toast.makeText(
                            context,
                            "이미 가입한 이메일이거나, 회원가입에 실패했습니다.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }


    }


    //로그인 한 후
    private fun handleSuccessLogin() {
        if (auth.currentUser == null) {   //currentUser는 null이 가능하므로 그럴경우 실패
            Toast.makeText(context, "로그인에 실패했습니다.", Toast.LENGTH_SHORT).show()
            return
        }
        //성공했다면 유저id가져오기
        //userid가져오기 //null일경우를 대비해서 orEmpty
        val userId = auth.currentUser?.uid.orEmpty()


        //DB사용

        //reference에서 Users라는 child를 선택
        val currentUserDB = Firebase.database.reference.child("Users").child(userId)
        val user = mutableMapOf<String, Any>()
        user["userId"] = userId
        currentUserDB.updateChildren(user)      //제일 상위에 DB 안에Users라는 List가 생기고 그안에
        // userId라는 항목으로 오브젝트가 생기고 그안에 user가 저장 유저는 userId를 가지고잇음
        //Users라는 키도 따로 빼둠.


        //다음 프래그먼트로 부착
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.signUpFragmentContainer, MyNameInputFragment())
            .commit()

    }

    // 입력칸이 비어있는 경우 예외처리
    private fun initPasswordEditText() {
        val passwordEditText = binding?.passwordEditText
        val passwordEditTextCheck = binding?.checkPasswordEditText
        val signUpButton = binding?.passwordSignupButton
        signUpButton!!.isEnabled = false
        signUpButton!!.visibility = INVISIBLE

        passwordEditTextCheck?.addTextChangedListener {
            //둘다 채워졌는지 여부
            val enable = passwordEditText!!.text.isNotEmpty() && passwordEditTextCheck!!.text.isNotEmpty()
            //동일한 비밀번호 여부
            val check = passwordEditText!!.text == passwordEditTextCheck!!.text
            if (enable || check) {
                signUpButton!!.isEnabled = true
                signUpButton!!.visibility = VISIBLE
            } else {

                signUpButton!!.isEnabled = false
                signUpButton!!.visibility = INVISIBLE
            }


        }


    }
}
