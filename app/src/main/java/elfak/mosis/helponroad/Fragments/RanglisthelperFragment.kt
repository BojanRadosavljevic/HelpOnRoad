package elfak.mosis.helponroad.Fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import elfak.mosis.helponroad.R
import elfak.mosis.helponroad.databinding.FragmentRanglistBinding
import elfak.mosis.helponroad.databinding.FragmentRanglisthelperBinding


class RanglisthelperFragment : Fragment() {
    private var _binding: FragmentRanglisthelperBinding?=null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRanglisthelperBinding.inflate(inflater, container, false)
        (requireActivity() as AppCompatActivity).supportActionBar?.show()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPreferences = requireActivity().getSharedPreferences("Aplikacija", Context.MODE_PRIVATE)
        if(sharedPreferences.getBoolean("strana",false)==true)
        {
            binding.idText.text="1. na rang listi dobija popust od 20%, 2. i 3. na rang listi dobijaju popust od 15%,4. i 5. na rang listi dobijaju popust od 10%,od 6. do 10. na rang listi dobijaju popust od 5%,"
        }
        else if(sharedPreferences.getBoolean("strana",false)==false)
        {
            binding.idText.text="1. na rang listi dobija popust od 20%, 2. i 3. na rang listi dobijaju popust od 15%,4. i 5. na rang listi dobijaju popust od 10%,od 6. do 10. na rang listi dobijaju popust od 5%,"
        }
    }
}