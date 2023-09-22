package elfak.mosis.helponroad.Fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import elfak.mosis.helponroad.Adapters.RangListRadnjaAdapter
import elfak.mosis.helponroad.Adapters.RangListUsersAdapter
import elfak.mosis.helponroad.R
import elfak.mosis.helponroad.ViewModel.UsersViewModel
import elfak.mosis.helponroad.databinding.FragmentRanglistBinding
import elfak.mosis.helponroad.databinding.FragmentTableBinding

class TableFragment : Fragment() {
    private var _binding: FragmentTableBinding?=null
    private val binding get() = _binding!!
    private val vm: UsersViewModel by  activityViewModels()
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_table, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPreferences = requireActivity().getSharedPreferences("Aplikacija", Context.MODE_PRIVATE)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager=LinearLayoutManager(requireContext())
        if(sharedPreferences.getBoolean("strana",false))
        {
            val adapter=RangListRadnjaAdapter(vm.radnje)
            recyclerView.adapter=adapter
        }
        else if(!sharedPreferences.getBoolean("strana",false))
        {
            val adapter= RangListUsersAdapter(vm.users)
            recyclerView.adapter=adapter
        }


    }

}