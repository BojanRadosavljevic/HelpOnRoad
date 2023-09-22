package elfak.mosis.helponroad.Fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import elfak.mosis.helponroad.Model.User
import elfak.mosis.helponroad.R
import elfak.mosis.helponroad.ViewModel.UsersViewModel
import elfak.mosis.helponroad.databinding.FragmentLoginBinding
import elfak.mosis.helponroad.databinding.FragmentRegisterBinding


class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding?=null
    private val binding get() = _binding!!
    private val vm: UsersViewModel by  activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        (requireActivity() as AppCompatActivity).supportActionBar?.show()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPreferences = requireActivity().getSharedPreferences("Aplikacija", Context.MODE_PRIVATE)
        binding.LoginOKButton.setOnClickListener{
            if(binding.LoginShopOrUser.isChecked==false) {
                val email = binding.LoginEmail.text.toString().trim()
                val password = binding.LoginPassword.text.toString().trim()
                val f=validateDataUser(email,password)
                if(f==true)
                {
                    Log.d("aaa","usao")
                    vm.LoginUser(email,password)

                    if(vm.Logged==true) {
                        sharedPreferences.edit().putBoolean("isLoggedIn", true).apply()
                        sharedPreferences.edit().putBoolean("strana", false).apply()
                        findNavController().navigate(R.id.action_loginFragment_to_userFragment)
                    }


                }

            }
            else
            {
                val email = binding.LoginEmail.text.toString().trim()
                val password = binding.LoginPassword.text.toString().trim()
                val f=validateDataUser(email,password)
                if(f)
                {
                    vm.LoginShop(email,password)
                    if(vm.Logged==true) {
                        sharedPreferences.edit().putBoolean("isLoggedIn", true).apply()
                        sharedPreferences.edit().putBoolean("strana",true).apply()
                        binding.LoginEmail.setText(vm.shop.Ime)
                        findNavController().navigate(R.id.action_loginFragment_to_radnjaFragment)
                    }


                }

            }

        }
        binding.LoginNotHaveAccout.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }
    fun validateDataUser(email:String,password:String): Boolean {
        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
            return false
        if(password.length<=4)
            return false
        return true
    }
}