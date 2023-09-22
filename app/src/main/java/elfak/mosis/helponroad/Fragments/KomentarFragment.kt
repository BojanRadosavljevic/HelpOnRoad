package elfak.mosis.helponroad.Fragments

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.timepicker.TimeFormat
import com.google.type.DateTime
import elfak.mosis.helponroad.Model.Komentar
import elfak.mosis.helponroad.R
import elfak.mosis.helponroad.ViewModel.UsersViewModel
import elfak.mosis.helponroad.databinding.FragmentKomentarBinding
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle


class KomentarFragment : DialogFragment() {
    private var _binding: FragmentKomentarBinding? = null
    private val binding get() = _binding!!
    private val vm: UsersViewModel by  activityViewModels()
    lateinit var dial:Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onStart() {
        super.onStart()

        dial= dialog!!
        if (dial != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dial!!.window?.setLayout(width, height)
        }
        val listView: ListView = binding.KomentariListView
        val userStrings = vm.komentariradnje.map { "${it.user} :  ${it.komentar}   -   ${it.datumIVreme}" }
        if (userStrings.isNullOrEmpty()) {
            Log.d("aaa", "UserStrings is null or empty")
        } else {
            val arrayAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, userStrings)
            listView.adapter = arrayAdapter}
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentKomentarBinding.inflate(inflater, container, false)
        (requireActivity() as AppCompatActivity).supportActionBar?.show()
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.Dodaj.setOnClickListener{
            if(binding.Komentar.text.toString().trim().isNotEmpty())
            {
                val kom=Komentar(
                    vm.izabranaRadnja?.id+vm.user.userName+ LocalTime.now().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM)).toString(),
                    vm.user.userName,
                    vm.izabranaRadnja?.id!!,
                    binding.Komentar.text.toString().trim(),
                    LocalDate.now().toString()
                )
                vm.addKomentar(kom)
                vm.povecajPoene(true)
                dial.dismiss();
            }
        }

    }
}