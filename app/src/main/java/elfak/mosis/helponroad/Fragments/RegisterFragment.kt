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
import com.google.firebase.auth.FirebaseAuth
import elfak.mosis.helponroad.Model.Radnja
import elfak.mosis.helponroad.Model.User
import elfak.mosis.helponroad.R
import elfak.mosis.helponroad.databinding.FragmentRegisterBinding
import elfak.mosis.helponroad.ViewModel.UsersViewModel

class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding?=null
    private val binding get() = _binding!!
    private val vm:UsersViewModel by  activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        (requireActivity() as AppCompatActivity).supportActionBar?.show()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPreferences = requireActivity().getSharedPreferences("Aplikacija", Context.MODE_PRIVATE)
        binding.RegisterOKButton.setOnClickListener{
            if(binding.RegisterShopOrUser.isChecked==false) {
                var user = User(
                    "",
                    binding.RegisterUserName.text.toString().trim(),
                    binding.RegisterNumber.text.toString().trim(),
                    10,
                    binding.RegisterEmail.text.toString().trim(),
                    binding.RegisterPassword.text.toString().trim(),
                    binding.RegisterShopOrUser.isChecked
                )

                var f = validateDataUser(user)
                if(f==true)
                {

                    vm.createNewUser(user)
                    if(vm.Logged==true) {
                        sharedPreferences.edit().putBoolean("isLoggedIn", true).apply()
                        sharedPreferences.edit().putBoolean("strana", vm.user.strana).apply()
                        findNavController().navigate(R.id.action_registerFragment_to_userFragment)
                    }

                }

            }
            else
            {
                var shop = Radnja(
                    "",
                    binding.RegisterNameShop.text.toString().trim(),
                    binding.RegisterUserName.text.toString().trim(),
                    binding.RegisterNumber.text.toString().trim(),
                    binding.RegisterEmail.text.toString().trim(),
                    binding.RegisterPassword.text.toString().trim(),
                    10,
                    0.0,
                    0.0,
                    binding.RegisterShopOrUser.isChecked
                )
                var p = validateDataShop(shop)
                if(p==true)
                {
                    vm.createNewShop(shop)
                    if(vm.Logged==true) {
                        sharedPreferences.edit().putBoolean("isLoggedIn", true).apply()
                        sharedPreferences.edit().putBoolean("strana", true).apply()
                        findNavController().navigate(R.id.action_registerFragment_to_radnjaFragment)
                    }
                }
            }

        }
        binding.RegisterHaveAccout.setOnClickListener{
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
    }

    fun validateDataUser(user:User): Boolean {
        if(user.userName.isEmpty())
            return false
        if(user.brojTelefona.isEmpty())
            return false
        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(user.email).matches())
            return false
        if(user.password.length<=4)
            return false
        return true
    }
    fun validateDataShop(shop:Radnja): Boolean {
        if(shop.Username.isEmpty())
            return false
        if(shop.Ime.isEmpty())
            return false
        if(shop.BrojTelefona.isEmpty())
            return false
        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(shop.email).matches())
            return false
        if(shop.password.length<=4)
            return false
        return true
    }

}