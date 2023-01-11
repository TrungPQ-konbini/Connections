package com.konbini.connections

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.konbini.connections.databinding.FragmentFirstBinding
import com.konbini.internet.Internet
import com.konbini.internet.InternetService
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val internet = Internet(requireContext())
        viewLifecycleOwner.lifecycleScope.launch {
            launch {
                internet.observe().collect { status ->
                    when (status) {
                        InternetService.Status.CapabilitiesChanged,
                        InternetService.Status.LinkPropertiesChanged,
                        InternetService.Status.BlockedStatusChanged-> {
                            internet.observe().collect()
                        }
                        else -> {}
                    }
                    binding.textviewFirst.text = status.name
                }
            }
        }

        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}