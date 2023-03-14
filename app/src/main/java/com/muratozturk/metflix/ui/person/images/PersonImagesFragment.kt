package com.muratozturk.metflix.ui.person.images

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.muratozturk.metflix.R
import com.muratozturk.metflix.common.*
import com.muratozturk.metflix.databinding.FragmentPersonImagesBinding
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import www.sanju.motiontoast.MotionToastStyle

@AndroidEntryPoint
class PersonImagesFragment : Fragment(R.layout.fragment_person_images) {
    private val viewModel: PersonImagesViewModel by viewModels()
    private val binding by viewBinding(FragmentPersonImagesBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        collectData()
    }

    fun initUI() {
        with(binding) {
            with(viewModel) {

            }
        }
    }

    private fun onClick(id: Int) {

    }

    fun collectData() {
        with(viewModel) {
            with(binding) {

                lifecycleScope.launchWhenCreated {
                    images.collectLatest { response ->
                        when (response) {
                            is Resource.Loading -> {
                                imagesRecycler.gone()
                            }
                            is Resource.Error -> {
                                imagesRecycler.gone()

                                requireActivity().showToast(
                                    getString(com.muratozturk.metflix.R.string.error),
                                    response.throwable.localizedMessage ?: "Error",
                                    MotionToastStyle.ERROR
                                )

                            }
                            is Resource.Success -> {
                                imagesRecycler.visible()

                                val imagesAdapter =
                                    PersonImagesAdapter(response.data)
                                imagesRecycler.adapter = imagesAdapter

                            }
                        }

                    }
                }


            }
        }
    }

    companion object {
        fun createBundle(id: Int) =
            PersonImagesFragment().apply {
                arguments = Bundle().apply {
                    putInt(Constants.Arguments.ID, id)
                }
            }
    }
}