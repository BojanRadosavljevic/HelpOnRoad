package elfak.mosis.helponroad.Fragments

import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import elfak.mosis.helponroad.Model.Radnja
import elfak.mosis.helponroad.R
import elfak.mosis.helponroad.ViewModel.UsersViewModel
import elfak.mosis.helponroad.databinding.FragmentPromenilokacijuBinding
import elfak.mosis.helponroad.databinding.FragmentRadnjaBinding


class PromenilokacijuFragment : Fragment() , OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener{
    private var _binding: FragmentPromenilokacijuBinding?=null
    private val binding get() = _binding!!
    private val vm: UsersViewModel by  activityViewModels()
    private var map: GoogleMap? = null
    private var isMapReady = false
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location
    private  var radnjaMap: MutableMap<Marker?, Radnja> = mutableMapOf()
    private  var lok:LatLng?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentPromenilokacijuBinding.inflate(inflater, container, false)
        (requireActivity() as AppCompatActivity).supportActionBar?.show()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync { mMap ->
            map = mMap
            mMap.mapType = GoogleMap.MAP_TYPE_NORMAL

            mMap.clear()
            mMap.uiSettings.isZoomControlsEnabled = true
            mMap.uiSettings.isCompassEnabled = true

            if (ActivityCompat.checkSelfPermission(
                    requireActivity(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                )
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    1001
                )
                return@getMapAsync
            }
            mMap.isMyLocationEnabled = true
            mMap.setOnInfoWindowClickListener(this)
            mMap.setOnMapClickListener { it->
                lok=LatLng(it.latitude,it.longitude)
                map?.clear()


                    val marker = map?.addMarker(
                        MarkerOptions().position(lok!!).title(vm.shop.Ime).icon(
                            BitmapDescriptorFactory.fromResource(R.drawable.icons8_marker_64))
                    )

            }
            fusedLocationClient.lastLocation.addOnCompleteListener { location ->
                if (location.result != null) {
                    lastLocation = location.result
                    val currentLatLong = LatLng(location.result.latitude, location.result.longitude)
                    val googlePlex = CameraPosition.builder()
                        .target(currentLatLong)
                        .zoom(15f)
                        .bearing(0f)
                        .tilt(0f)
                        .build()

                    mMap.animateCamera(
                        CameraUpdateFactory.newCameraPosition(googlePlex),
                        1000,
                        null
                    )
                    if(vm.shop!=null)
                        setUpMarkers()
                }
                Toast.makeText(requireActivity(), "Please turn on location", Toast.LENGTH_LONG).show()
            }
        }
        binding.Dodaj.setOnClickListener {
            if(lok!=null)
            {
                vm.shop.latitude= lok!!.latitude
                vm.shop.longitude= lok!!.longitude
                vm.changeRadnjaInformation(vm.shop.id)
                findNavController().navigate(R.id.action_promenilokacijuFragment_to_radnjaFragment)
            }
        }
        binding.Odustani.setOnClickListener {
            findNavController().navigate(R.id.action_promenilokacijuFragment_to_radnjaFragment)
        }
    }
    private fun setUpMarkers()
    {
        map?.clear()
        if(vm.shop.latitude!=0.0&&vm.shop.longitude!=0.0)
        {

                val marker = map?.addMarker(
                    MarkerOptions().position(LatLng(vm.shop.latitude, vm.shop.longitude)).title(vm.shop.Ime).icon(
                        BitmapDescriptorFactory.fromResource(R.drawable.icons8_marker_64))
                )
                radnjaMap[marker] = vm.shop
                marker?.tag = vm.shop
            }
    }
        private fun setMyLocationOverlay() {
            if (map != null) {
                try {
                    map?.isMyLocationEnabled = true
                    fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                        if (location != null) {
                            val latLng = LatLng(location.latitude, location.longitude)
                            map?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                        }
                    }
                } catch (e: SecurityException) {
                    e.printStackTrace()
                }
            }
        }
        private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
                if (isGranted) {
                    setMyLocationOverlay()
                }
            }
        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }

        override fun onMapReady(googleMap: GoogleMap) {
            Toast.makeText(this.activity, "Map is ready.", Toast.LENGTH_SHORT).show()
            map = googleMap
            isMapReady = true

        }

    override fun onInfoWindowClick(p0: Marker) {
        TODO("Not yet implemented")
    }

}