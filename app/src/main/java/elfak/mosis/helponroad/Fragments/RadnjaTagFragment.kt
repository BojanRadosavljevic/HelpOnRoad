package elfak.mosis.helponroad.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import elfak.mosis.helponroad.Model.Radnja
import elfak.mosis.helponroad.R
import elfak.mosis.helponroad.ViewModel.UsersViewModel
import elfak.mosis.helponroad.databinding.FragmentRadnjaTagBinding


class RadnjaTagFragment : DialogFragment() {
    private var _binding: FragmentRadnjaTagBinding?=null
    private val binding get() = _binding!!
    private val vm: UsersViewModel by  activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRadnjaTagBinding.inflate(inflater, container, false)
        (requireActivity() as AppCompatActivity).supportActionBar?.show()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm.getKomentareRadnje()
        binding.Ime.setText(vm.izabranaRadnja.Ime)
        binding.Vlasnik.setText(vm.izabranaRadnja.Username)
        binding.BrojTelefona.setText(vm.izabranaRadnja.BrojTelefona)
        binding.Email.setText(vm.izabranaRadnja.email)
        binding.BrojPoena.setText(vm.izabranaRadnja.brojPoena.toString())
        binding.Latitude.setText(vm.izabranaRadnja.latitude.toString())
        binding.Longitude.setText(vm.izabranaRadnja.longitude.toString())


        binding.Usluge.setOnClickListener {
            val dialogFragment=UslugetagFragment()
            dialogFragment.show(parentFragmentManager,"UslugetagFragment")
        }
        binding.Komentari.setOnClickListener {
            val dialogFragment=KomentarFragment()
            dialogFragment.show(parentFragmentManager,"KomentarFragment")
        }


    }
}