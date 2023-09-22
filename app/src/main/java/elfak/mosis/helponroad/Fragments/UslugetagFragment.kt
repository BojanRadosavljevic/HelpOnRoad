package elfak.mosis.helponroad.Fragments

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import elfak.mosis.helponroad.R
import elfak.mosis.helponroad.ViewModel.UsersViewModel
import elfak.mosis.helponroad.databinding.FragmentUslugetagBinding


class UslugetagFragment : DialogFragment() {
    private var _binding: FragmentUslugetagBinding? = null
    private val binding get() = _binding!!
    private val vm: UsersViewModel by  activityViewModels()
    lateinit var dial: Dialog

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
        val userStrings = vm.uslugeradnje.getValue(vm.izabranaRadnja).map { "${it.ime} ..........  ${it.cena}" }
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
        _binding = FragmentUslugetagBinding.inflate(inflater, container, false)
        (requireActivity() as AppCompatActivity).supportActionBar?.show()
        return binding.root
    }


}