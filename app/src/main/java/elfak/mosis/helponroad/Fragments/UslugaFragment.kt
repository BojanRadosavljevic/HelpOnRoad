package elfak.mosis.helponroad.Fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.set
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import elfak.mosis.helponroad.Model.Usluga
import elfak.mosis.helponroad.R
import elfak.mosis.helponroad.Utils.Constants
import elfak.mosis.helponroad.ViewModel.UsersViewModel
import elfak.mosis.helponroad.databinding.FragmentRadnjaBinding
import elfak.mosis.helponroad.databinding.FragmentUslugaBinding


class UslugaFragment : Fragment() {
    private var _binding: FragmentUslugaBinding?=null
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
        _binding = FragmentUslugaBinding.inflate(inflater, container, false)
        (requireActivity() as AppCompatActivity).supportActionBar?.show()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPreferences = requireActivity().getSharedPreferences("Aplikacija", Context.MODE_PRIVATE)

        for(u in vm.usluge)
            if(u.ime==Constants.VULKANIZERSKE_USLUGE)
            {
                binding.VULKANIZERSKEUSLUGE.isChecked=true
                binding.VULKANIZERSKEUSLUGECena.setText(u.cena.toString())
            }
            else if(u.ime==Constants.ELEKTRONSKE_USLUGE)
            {
                binding.ELEKTRONSKEUSLUGE.isChecked=true
                binding.ELEKTRONSKEUSLUGECena.setText(u.cena.toString())
            }
            else if(u.ime==Constants.MEHANICKE_USLUGE)
            {
                binding.MEHANICKEUSLUGE.isChecked=true
                binding.MEHANICKEUSLUGECena.setText(u.cena.toString())
            }
            else if(u.ime==Constants.USLUGE_SLEPANJA)
            {
                binding.USLUGESLEPANJA.isChecked=true
                binding.USLUGESLEPANJACena.setText(u.cena.toString())
            }
        binding.UslugaCancel.setOnClickListener {
            findNavController().navigate(R.id.action_uslugaFragment_to_radnjaFragment)
        }
        binding.UslugaDodaj.setOnClickListener {
            vm.usluge=ArrayList<Usluga>()
            if(binding.VULKANIZERSKEUSLUGE.isChecked)
            {
                val u=Usluga(vm.shop.id+Constants.VULKANIZERSKE_USLUGE,Constants.VULKANIZERSKE_USLUGE,vm.shop.id,binding.VULKANIZERSKEUSLUGECena.text.toString().toInt())
                vm.usluge.add(u)
            }
            if(binding.ELEKTRONSKEUSLUGE.isChecked)
            {
                val u=Usluga(vm.shop.id+Constants.ELEKTRONSKE_USLUGE,Constants.ELEKTRONSKE_USLUGE,vm.shop.id,binding.ELEKTRONSKEUSLUGECena.text.toString().toInt())
                vm.usluge.add(u)
            }
            if(binding.MEHANICKEUSLUGE.isChecked)
            {
                val u=Usluga(vm.shop.id+Constants.MEHANICKE_USLUGE,Constants.MEHANICKE_USLUGE,vm.shop.id,binding.MEHANICKEUSLUGECena.text.toString().toInt())
                vm.usluge.add(u)
            }
            if(binding.USLUGESLEPANJA.isChecked)
            {
                val u=Usluga(vm.shop.id+Constants.USLUGE_SLEPANJA,Constants.USLUGE_SLEPANJA,vm.shop.id,binding.USLUGESLEPANJACena.text.toString().toInt())
                vm.usluge.add(u)
            }
            vm.addUslugeRadnji(vm.shop.id)
            findNavController().navigate(R.id.action_uslugaFragment_to_radnjaFragment)
        }
    }
}