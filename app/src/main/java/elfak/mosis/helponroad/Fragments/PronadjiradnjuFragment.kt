package elfak.mosis.helponroad.Fragments

import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import elfak.mosis.helponroad.Model.Kvar
import elfak.mosis.helponroad.Model.Radnja
import elfak.mosis.helponroad.Model.Usluga
import elfak.mosis.helponroad.R
import elfak.mosis.helponroad.Utils.Constants
import elfak.mosis.helponroad.ViewModel.UsersViewModel
import elfak.mosis.helponroad.databinding.FragmentPronadjiradnjuBinding
import java.lang.Math.*

class PronadjiradnjuFragment : Fragment() {
    private var _binding: FragmentPronadjiradnjuBinding?=null
    private val binding get() = _binding!!
    private val vm: UsersViewModel by  activityViewModels()
    lateinit var pomru:MutableMap<Radnja,ArrayList<Usluga>>
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location
    private  var lok:LatLng?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentPronadjiradnjuBinding.inflate(inflater, container, false)
        (requireActivity() as AppCompatActivity).supportActionBar?.show()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.Cancel.setOnClickListener {
            findNavController().navigate(R.id.action_pronadjiradnjuFragment_to_userFragment)
        }
        binding.Potrazi.setOnClickListener {
            filtritajStranu()
        }
    }

    private fun filtritajStranu() {
        var t=0
        var dist=0
        vm.filtraneUslugeRadnje=mutableMapOf()
        pomru=vm.uslugeradnje
        if(binding.VULKANIZERSKEUSLUGE.isChecked)
        {
            val pomru2:MutableMap<Radnja,ArrayList<Usluga>>?=mutableMapOf()
            for (ru in pomru)
                for (u in ru.value)
                    if (u.ime == Constants.VULKANIZERSKE_USLUGE)
                        pomru2?.put(ru.key, ru.value)
            pomru= pomru2!!
        }
        else t++
        if(binding.MEHANICKEUSLUGE.isChecked)
        {
            val pomru2:MutableMap<Radnja,ArrayList<Usluga>>?=mutableMapOf()
            for (ru in pomru)
                for(u in ru.value)
                    if(u.ime==Constants.MEHANICKE_USLUGE)
                        pomru2?.put(ru.key,ru.value)
            pomru= pomru2!!
        }
        else t++
        if(binding.ELEKTRONSKEUSLUGE.isChecked)
        {
            val pomru2:MutableMap<Radnja,ArrayList<Usluga>>?=mutableMapOf()
            for (ru in pomru)
                for(u in ru.value)
                    if(u.ime==Constants.ELEKTRONSKE_USLUGE)
                        pomru2?.put(ru.key,ru.value)
            pomru= pomru2!!
        }
        else t++
        if(binding.USLUGESLEPANJA.isChecked)
        {
            val pomru2:MutableMap<Radnja,ArrayList<Usluga>>?=mutableMapOf()
            for (ru in pomru)
                for(u in ru.value)
                    if(u.ime==Constants.USLUGE_SLEPANJA)
                        pomru2?.put(ru.key,ru.value)
            pomru= pomru2!!
        }
        else t++
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        if (ActivityCompat.checkSelfPermission(
                requireActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                requireActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
            fusedLocationClient.lastLocation.addOnCompleteListener(requireActivity()) {
                val location: Location? = it.result
                if (location != null) {
                    if(t==4)
                        pomru=vm.uslugeradnje
                    for (ru in pomru)
                        if(!binding.Distanca.text.toString().trim().isEmpty()){
                            if(getDistance(location.latitude, location.longitude, ru.key.latitude, ru.key.longitude) < binding.Distanca.text.toString().trim().toInt())
                            {
                                vm.filtraneUslugeRadnje?.put(ru.key,ru.value)
                                Log.d("aaa",ru.key.Ime)
                            }
                        }else {
                            vm.filtraneUslugeRadnje?.put(ru.key,ru.value)}

                        }
                else  {
                    Toast.makeText(requireActivity(), "Please turn on location", Toast.LENGTH_LONG).show()
                }
                findNavController().navigate(R.id.action_pronadjiradnjuFragment_to_prikaziradnjeFragment)
            }


    }
    fun getDistance(currentLat: Double, currentLon: Double, deviceLat: Double, deviceLon: Double): Double {
        val earthRadius = 6371000.0

        val currentLatRad = Math.toRadians(currentLat)
        val deviceLatRad = Math.toRadians(deviceLat)
        val deltaLat = Math.toRadians(deviceLat - currentLat)
        val deltaLon = Math.toRadians(deviceLon - currentLon)

        val a = sin(deltaLat / 2) * sin(deltaLat / 2) +
                cos(currentLatRad) * cos(deviceLatRad) *
                sin(deltaLon / 2) * sin(deltaLon / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return earthRadius * c
    }

}