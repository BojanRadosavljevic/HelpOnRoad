package elfak.mosis.helponroad.Fragments

import android.content.pm.PackageManager
import android.graphics.BitmapFactory
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
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.storage.FirebaseStorage
import elfak.mosis.helponroad.Model.Kvar
import elfak.mosis.helponroad.R
import elfak.mosis.helponroad.ViewModel.UsersViewModel
import elfak.mosis.helponroad.databinding.FragmentKvartagBinding
import elfak.mosis.helponroad.databinding.FragmentRadnjaTagBinding
import java.io.File

class KvartagFragment : DialogFragment() {
    private var _binding: FragmentKvartagBinding?=null
    private val binding get() = _binding!!
    private val vm: UsersViewModel by  activityViewModels()
    val storageRef= FirebaseStorage.getInstance().getReference()
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
        _binding = FragmentKvartagBinding.inflate(inflater, container, false)
        (requireActivity() as AppCompatActivity).supportActionBar?.show()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getSliku()
        binding.Vlasnik.setText(vm.izabranKvarUser.userName)
        binding.BrojTelefona.setText(vm.izabranKvarUser.brojTelefona)
        binding.Opis.setText(vm.izabranKvarKvar.opis)
        binding.Latitude.setText(vm.izabranKvarKvar.latitude.toString())
        binding.Longitude.setText(vm.izabranKvarKvar.longitude.toString())
        binding.Status.setText(vm.izabranKvarKvar.status)

        binding.Odustani.setOnClickListener {
            if(vm.izabranKvarKvar.status!="postavljeno") {
                vm.changeKvar(Kvar(
                    vm.izabranKvarKvar.id,
                    vm.izabranKvarKvar.user,
                    vm.izabranKvarKvar.slika,
                    vm.izabranKvarKvar.opis,
                    vm.izabranKvarKvar.latitude,
                    vm.izabranKvarKvar.longitude,
                    "postavljeno"
                ))
            }
            dismiss()
        }
        binding.Resi.setOnClickListener {
                filtrirajStranu()
        }
    }
    fun getSliku() {
        val fileRef = storageRef.child(vm.izabranKvarUser.id)
        val local= File.createTempFile("tempImage","jpg")
        fileRef.getFile(local).addOnSuccessListener{
            val bitmap= BitmapFactory.decodeFile(local.absolutePath)
            binding.Image.setImageBitmap(bitmap)
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
    fun filtrirajStranu() {
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
                            if (getDistance(location.latitude, location.longitude, vm.izabranKvarKvar.latitude, vm.izabranKvarKvar.longitude) < 10) {
                                vm.resiKvar(false)
                                dismiss()
                            }
                    else{
                        if(vm.izabranKvarKvar.status!="zakazan") {
                                vm.changeKvar(Kvar(
                                    vm.izabranKvarKvar.id,
                                    vm.izabranKvarKvar.user,
                                    vm.izabranKvarKvar.slika,
                                    vm.izabranKvarKvar.opis,
                                    vm.izabranKvarKvar.latitude,
                                    vm.izabranKvarKvar.longitude,
                                    "zakazan"
                                ))
                                dismiss()
                            }
                    }
                } else {
                    Toast.makeText(requireActivity(), "Please turn on location", Toast.LENGTH_LONG)
                        .show()
                }
            }
    }

}