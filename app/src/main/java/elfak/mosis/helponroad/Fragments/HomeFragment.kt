package elfak.mosis.helponroad.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import elfak.mosis.helponroad.R
import elfak.mosis.helponroad.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {
    private var _binding:FragmentHomeBinding?=null
    private val binding get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        (requireActivity() as AppCompatActivity).supportActionBar?.show()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.HomeLoginButton.setOnClickListener{
            findNavController().navigate(R.id.action_homeFragment3_to_loginFragment)
        }
        binding.HomeRegisterButton.setOnClickListener{
            findNavController().navigate(R.id.action_homeFragment3_to_registerFragment)
        }
    }


}