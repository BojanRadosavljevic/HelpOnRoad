package elfak.mosis.helponroad.Fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import elfak.mosis.helponroad.R
import elfak.mosis.helponroad.ViewModel.UsersViewModel
import elfak.mosis.helponroad.databinding.FragmentRadnjaBinding
import elfak.mosis.helponroad.databinding.FragmentRanglistBinding


class RanglistFragment : Fragment() {
    private var _binding: FragmentRanglistBinding?=null
    private val binding get() = _binding!!
    private val vm: UsersViewModel by  activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
            _binding = FragmentRanglistBinding.inflate(inflater, container, false)
            (requireActivity() as AppCompatActivity).supportActionBar?.show()
            return binding.root
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            val sharedPreferences = requireActivity().getSharedPreferences("Aplikacija", Context.MODE_PRIVATE)
            if(sharedPreferences.getBoolean("strana",false)==true)
            {
                binding.MyName.text=vm.shop.Ime
                binding.MyPoints.text=vm.shop.brojPoena.toString()
                binding.Help.setOnClickListener {
                    findNavController().navigate(R.id.action_ranglistFragment_to_ranglisthelperFragment)
                }
                val listView: ListView = binding.rangListaListView
                val userStrings = vm.users.map { "${it.userName} - Points: ${it.brojPoena}" }
                if (userStrings.isNullOrEmpty()) {
                    Log.d("RangListaFragment", "UserStrings is null or empty")
                } else {
                    val arrayAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, userStrings)
                    listView.adapter = arrayAdapter
                    Log.d("RangListaFragment", "ArrayAdapter set")
                }
            }
            else if(sharedPreferences.getBoolean("strana",false)==false)
            {
                binding.MyName.text=vm.user.userName
                binding.MyPoints.text=vm.user.brojPoena.toString()
                binding.Help.setOnClickListener {
                    findNavController().navigate(R.id.action_ranglistFragment_to_ranglisthelperFragment)
                }
                val listView: ListView = binding.rangListaListView
                val userStrings = vm.radnje.map { "${it.Ime}           -           Points: ${it.brojPoena}" }
                if (userStrings.isNullOrEmpty()) {
                    Log.d("RangListaFragment", "UserStrings is null or empty")
                } else {
                    val arrayAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, userStrings)
                    listView.adapter = arrayAdapter
                    Log.d("RangListaFragment", "ArrayAdapter set")
                }
            }

        }
}