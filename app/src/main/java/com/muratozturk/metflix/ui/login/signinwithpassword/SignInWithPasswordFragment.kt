package com.muratozturk.metflix.ui.login.signinwithpassword

import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.muratozturk.metflix.R
import com.muratozturk.metflix.common.LoadingScreen
import com.muratozturk.metflix.common.Resource
import com.muratozturk.metflix.common.changeFocusedInputTint
import com.muratozturk.metflix.common.showToast
import com.muratozturk.metflix.databinding.FragmentSignInWithPasswordBinding
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import www.sanju.motiontoast.MotionToastStyle

@AndroidEntryPoint
class SignInWithPasswordFragment : Fragment(R.layout.fragment_sign_in_with_password) {
    private val binding by viewBinding(FragmentSignInWithPasswordBinding::bind)
    private val viewModel: SignInWithPasswordViewModel by viewModels()
    private var isPasswordShowing: Boolean = false
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()
        collectData()
    }

    private fun initUI() {
        with(binding) {
            with(viewModel) {
                backButton.setOnClickListener {
                    findNavController().popBackStack()
                }
                signUp.setOnClickListener {
                    val action =
                        SignInWithPasswordFragmentDirections.actionSignInWithPasswordFragmentToSignUpFragment()
                    findNavController().navigate(action)
                }
                showHidePassword.setOnClickListener {
                    if (isPasswordShowing) {
                        showHidePassword.setImageResource(R.drawable.show)
                        passwordEditText.transformationMethod = PasswordTransformationMethod()
                        isPasswordShowing = false
                    } else {
                        showHidePassword.setImageResource(R.drawable.hide)
                        passwordEditText.transformationMethod = null
                        isPasswordShowing = true
                    }
                }
                passwordEditText.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
                    passwordEditText.changeFocusedInputTint(hasFocus)
                    if (hasFocus) {
                        showHidePassword.setColorFilter(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.text_color
                            )
                        )
                    } else {
                        if (passwordEditText.text.toString().isEmpty()) {
                            showHidePassword.setColorFilter(
                                ContextCompat.getColor(
                                    requireContext(),
                                    R.color.inactive_input
                                )
                            )
                        }
                    }
                }
                emailEditText.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
                    emailEditText.changeFocusedInputTint(hasFocus)
                }
                signIn.setOnClickListener {
                    signIn(emailEditText.text.toString(), passwordEditText.text.toString())

                }
            }
        }
    }

    private fun collectData() {
        with(viewModel) {
            viewLifecycleOwner.lifecycleScope.launchWhenCreated {
                user.collect { response ->
                    when (response) {
                        is Resource.Loading -> {
                            LoadingScreen.displayLoading(requireContext(), false)
                        }
                        is Resource.Error -> {
                            LoadingScreen.hideLoading()
                            requireActivity().showToast(
                                getString(R.string.error),
                                getString(R.string.email_or_password_is_not_valid),
                                MotionToastStyle.ERROR
                            )
                            Log.e("Response", response.throwable.localizedMessage ?: "Error")

                        }
                        is Resource.Success -> {
                            LoadingScreen.hideLoading()
                            val action =
                                SignInWithPasswordFragmentDirections.actionSignInWithPasswordFragmentToHomeFragment()
                            findNavController().navigate(action)
                        }
                        else -> {}
                    }
                }

            }


        }
    }
}