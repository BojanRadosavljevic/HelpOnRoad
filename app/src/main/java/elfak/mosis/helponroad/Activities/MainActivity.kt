package elfak.mosis.helponroad.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import elfak.mosis.helponroad.Fragments.HomeFragment
import elfak.mosis.helponroad.Fragments.LoginFragment
import elfak.mosis.helponroad.Fragments.RadnjaFragment
import elfak.mosis.helponroad.Fragments.UserFragment
import elfak.mosis.helponroad.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }
    override fun onBackPressed() {
        val currentFragment = getCurrentFragment()
        if (currentFragment is UserFragment || currentFragment is RadnjaFragment) {
        }
            else if(currentFragment is HomeFragment)
                finish()
            else {
            super.onBackPressed() // Allow normal back button behavior for other fragments
        }
    }
    private fun getCurrentFragment(): Fragment? {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as? NavHostFragment
        return navHostFragment?.childFragmentManager?.fragments?.get(0)
    }
}