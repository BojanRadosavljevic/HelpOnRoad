package elfak.mosis.helponroad.Fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import elfak.mosis.helponroad.Model.Radnja
import elfak.mosis.helponroad.Model.User
import elfak.mosis.helponroad.R
import elfak.mosis.helponroad.ViewModel.UsersViewModel
import elfak.mosis.helponroad.databinding.FragmentRadnjaBinding
import elfak.mosis.helponroad.databinding.FragmentUserBinding


class RadnjaFragment : Fragment() {
    private var _binding: FragmentRadnjaBinding?=null
    private val binding get() = _binding!!
    private val vm: UsersViewModel by  activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRadnjaBinding.inflate(inflater, container, false)
        (requireActivity() as AppCompatActivity).supportActionBar?.show()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPreferences = requireActivity().getSharedPreferences("Aplikacija", Context.MODE_PRIVATE)
        binding.RadnjaIme.hint=vm.shop.Ime
        binding.RadnjaUserName.hint=vm.shop.Username
        binding.RadnjaNumber.hint=vm.shop.BrojTelefona
        vm.getUslugeRadnji(vm.shop.id)
        vm.getTop10Users()
        binding.LogoutRadnja.setOnClickListener {
            vm.shop= Radnja("","","","","","",0,0.0,0.0,true)
            sharedPreferences.edit().putBoolean("isLoggedIn", false).apply()
            sharedPreferences.edit().putBoolean("strana",false).apply()
            vm.Logged=false
            findNavController().navigate(R.id.action_radnjaFragment_to_homeFragment3)
        }
        binding.RadnjaChangeInfo.setOnClickListener{
            val f=validateInfo(binding.RadnjaIme.text.toString().trim(),binding.RadnjaUserName.text.toString().trim(),binding.RadnjaNumber.text.toString().trim())
            if(f){
                vm.shop.Ime=binding.RadnjaIme.text.toString().trim()
                vm.shop.Username = binding.RadnjaUserName.text.toString().trim()
                vm.shop.BrojTelefona = binding.RadnjaNumber.text.toString().trim()
                vm.changeRadnjaInformation(vm.shop.id)
                binding.RadnjaIme.text.clear()
                binding.RadnjaUserName.text.clear()
                binding.RadnjaNumber.text.clear()
                binding.RadnjaIme.hint=vm.shop.Ime
                binding.RadnjaUserName.hint=vm.shop.Username
                binding.RadnjaNumber.hint=vm.shop.BrojTelefona
            }
        }
        binding.RadnjaDeleteAccount.setOnClickListener {
            vm.deleteShopAccount(vm.shop.id)
            vm.shop= Radnja("","","","","","",0,0.0,0.0,true)
            sharedPreferences.edit().putBoolean("isLoggedIn", false).apply()
            sharedPreferences.edit().putBoolean("strana",false).apply()
            vm.Logged=false
            findNavController().navigate(R.id.action_radnjaFragment_to_homeFragment3)
        }
        binding.RadnjaDodajUslugu.setOnClickListener {
            findNavController().navigate(R.id.action_radnjaFragment_to_uslugaFragment)
        }
        binding.RadnjaAddLocation.setOnClickListener {
            findNavController().navigate(R.id.action_radnjaFragment_to_promenilokacijuFragment)
        }
        binding.RadnjaIzadjiNaTeren.setOnClickListener {
            findNavController().navigate(R.id.action_radnjaFragment_to_pronadjikvarFragment)
        }
        binding.RadnjaRangList.setOnClickListener{
            findNavController().navigate(R.id.action_radnjaFragment_to_ranglistFragment)
        }

    }
    fun validateInfo(ime:String,userName:String,brojTelefona:String):Boolean{
        if(ime.isEmpty())
            return false
        if(userName.isEmpty())
            return false
        if(brojTelefona.isEmpty())
            return false
        return true
    }
}