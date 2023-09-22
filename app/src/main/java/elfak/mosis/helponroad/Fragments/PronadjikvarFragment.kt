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
import elfak.mosis.helponroad.Model.User
import elfak.mosis.helponroad.Model.Usluga
import elfak.mosis.helponroad.R
import elfak.mosis.helponroad.ViewModel.UsersViewModel
import elfak.mosis.helponroad.databinding.FragmentPronadjikvarBinding
import elfak.mosis.helponroad.databinding.FragmentPronadjiradnjuBinding


class PronadjikvarFragment : Fragment() {
    private var _binding: FragmentPronadjikvarBinding? = null
    private val binding get() = _binding!!
    private val vm: UsersViewModel by activityViewModels()
    lateinit var pomru: MutableMap<User, Kvar>
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location
    private var lok: LatLng? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentPronadjikvarBinding.inflate(inflater, container, false)
        (requireActivity() as AppCompatActivity).supportActionBar?.show()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.Cancel.setOnClickListener {
            findNavController().navigate(R.id.action_pronadjikvarFragment_to_radnjaFragment)
        }
        binding.Potrazi.setOnClickListener {
            filtrirajStranu()
        }
    }

    fun filtrirajStranu() {
        vm.filtriraniKvaroviUsera = mutableMapOf()
        pomru = vm.kvaroviusera
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
                    for (ru in pomru)
                        if (!binding.Distanca.text.toString().trim().isEmpty()) {
                            if (getDistance(location.latitude, location.longitude, ru.value.latitude, ru.value.longitude) < binding.Distanca.text.toString().trim().toInt()) {
                                vm.filtriraniKvaroviUsera.put(ru.key, ru.value)
                                Log.d("aaa", ru.key.userName)
                            }
                        } else {
                            vm.filtriraniKvaroviUsera.put(ru.key, ru.value)
                        }

                } else {
                    Toast.makeText(requireActivity(), "Please turn on location", Toast.LENGTH_LONG)
                        .show()
                }
                findNavController().navigate(R.id.action_pronadjikvarFragment_to_prikazikvaroveFragment)
            }
    }
    fun getDistance(currentLat: Double, currentLon: Double, deviceLat: Double, deviceLon: Double): Double {
        val earthRadius = 6371000.0

        val currentLatRad = Math.toRadians(currentLat)
        val deviceLatRad = Math.toRadians(deviceLat)
        val deltaLat = Math.toRadians(deviceLat - currentLat)
        val deltaLon = Math.toRadians(deviceLon - currentLon)

        val a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
                Math.cos(currentLatRad) * Math.cos(deviceLatRad) *
                Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))

        return earthRadius * c
    }
}