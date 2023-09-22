package elfak.mosis.helponroad.Fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import elfak.mosis.helponroad.Model.User
import elfak.mosis.helponroad.R
import elfak.mosis.helponroad.ViewModel.UsersViewModel
import elfak.mosis.helponroad.databinding.FragmentLoginBinding
import elfak.mosis.helponroad.databinding.FragmentUserBinding

class UserFragment : Fragment() {
    private var _binding: FragmentUserBinding?=null
    private val binding get() = _binding!!
    private val vm: UsersViewModel by  activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserBinding.inflate(inflater, container, false)
        (requireActivity() as AppCompatActivity).supportActionBar?.show()
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPreferences = requireActivity().getSharedPreferences("Aplikacija", Context.MODE_PRIVATE)
        binding.UserUserName.hint=vm.user.userName
        binding.UserNumber.hint=vm.user.brojTelefona
        vm.getAllRadnja()
        vm.getKvarUsera()
        binding.LogoutUser.setOnClickListener {
            vm.user= User("","","",0,"","",false)
            sharedPreferences.edit().putBoolean("isLoggedIn", false).apply()
            sharedPreferences.edit().putBoolean("strana",false).apply()
            vm.Logged=false
            findNavController().navigate(R.id.action_userFragment_to_homeFragment3)
        }
        binding.UserChangeInfo.setOnClickListener{
            val f=validateInfo(binding.UserUserName.text.toString().trim(),binding.UserNumber.text.toString().trim())
            if(f){
                vm.user.userName = binding.UserUserName.text.toString().trim()
                vm.user.brojTelefona = binding.UserNumber.text.toString().trim()
                vm.changeUserInformation(vm.user.id)
                binding.UserUserName.text.clear()
                binding.UserNumber.text.clear()
                binding.UserUserName.hint=vm.user.userName
                binding.UserNumber.hint=vm.user.brojTelefona
            }
        }
        binding.UserDeleteAccount.setOnClickListener{
            vm.deleteUserAccount(vm.user.id)
            vm.user= User("","","",0,"","",false)
            sharedPreferences.edit().putBoolean("isLoggedIn", false).apply()
            sharedPreferences.edit().putBoolean("strana",false).apply()
            vm.Logged=false
            findNavController().navigate(R.id.action_userFragment_to_homeFragment3)
        }
        binding.UserRangList.setOnClickListener{
            findNavController().navigate(R.id.action_userFragment_to_ranglistFragment)
        }
        binding.UserPrijaviKvar.setOnClickListener{
            findNavController().navigate(R.id.action_userFragment_to_prijavikvarFragment)
        }
        binding.UserPronadjiRadnju.setOnClickListener{
            findNavController().navigate(R.id.action_userFragment_to_pronadjiradnjuFragment)
        }

    }
    fun validateInfo(userName:String,brojTelefona:String):Boolean{
        if(userName.isEmpty())
            return false
        if(brojTelefona.isEmpty())
            return false
        return true
    }

}