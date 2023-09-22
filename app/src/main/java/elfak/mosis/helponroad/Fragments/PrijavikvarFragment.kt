package elfak.mosis.helponroad.Fragments

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.Debug.getLocation
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.net.toUri
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import elfak.mosis.helponroad.Model.Kvar
import elfak.mosis.helponroad.R
import elfak.mosis.helponroad.ViewModel.UsersViewModel
import elfak.mosis.helponroad.databinding.FragmentPrijavikvarBinding
import java.io.File
import java.io.FileNotFoundException
import java.io.InputStream



class PrijavikvarFragment : Fragment() {
    private var _binding: FragmentPrijavikvarBinding?=null
    private val binding get() = _binding!!
    private val vm: UsersViewModel by  activityViewModels()
    private var selectedImageUri: Uri? = null
    private lateinit var progressDialog: ProgressDialog
    val storageRef= FirebaseStorage.getInstance().getReference()
    private var map: GoogleMap? = null
    private var isMapReady = false
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location
    private  var lok:LatLng?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        progressDialog = ProgressDialog(requireContext())
        progressDialog.setMessage("Registrovanje: 0 %")
        progressDialog.setCancelable(false)
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
        progressDialog.progress = 0
        progressDialog.max = 100
    }
    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentPrijavikvarBinding.inflate(inflater, container, false)
        (requireActivity() as AppCompatActivity).supportActionBar?.show()
        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            selectedImageUri = data.data!!
            try
            {
                val imageStream: InputStream? = requireActivity().contentResolver.openInputStream(selectedImageUri!!)
                val selectedImageBitmap = BitmapFactory.decodeStream(imageStream)
                binding.AddImageKvar.setImageBitmap(selectedImageBitmap)

            }
            catch(e: FileNotFoundException)
            {
                e.printStackTrace();
            }
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(vm.userovKvar.id!="") {
            getSliku()
            binding.Status.setText(vm.userovKvar.status)
            binding.DodajOpisEdit.setText(vm.userovKvar.opis)
            binding.Latitude.text = vm.userovKvar.latitude.toString()
            binding.Longitude.text = vm.userovKvar.longitude.toString()
        }
        binding.idOdustani.setOnClickListener {
            findNavController().navigate(R.id.action_prijavikvarFragment_to_userFragment)
        }
        binding.idIzbrisiKvar.setOnClickListener {
            vm.deleteKvar()
            findNavController().navigate(R.id.action_prijavikvarFragment_to_userFragment)
        }
        binding.AddImageKvar.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST)
        }
        binding.idDodaj.setOnClickListener {
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
                        lok = LatLng(location.latitude, location.longitude)
                        if(validateData(binding.DodajOpisEdit.text.toString().trim())==true){
                            vm.userovKvar= Kvar(vm.user.id,vm.user.userName,selectedImageUri.toString(),binding.DodajOpisEdit.text.toString().trim(), lok!!.latitude,lok!!.longitude,"postavljeno")
                            addKvar(vm.userovKvar)
                            findNavController().navigate(R.id.action_prijavikvarFragment_to_userFragment)
                    }
                }
                else  {
                    Toast.makeText(requireActivity(), "Please turn on location", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    fun validateData(opis:String):Boolean{
        if(opis.length<10)
            return false
        if(selectedImageUri.toString().isEmpty())
            return false
        return true
    }
    fun addKvar(kvar:Kvar){
        var databaseUser: DatabaseReference? = null

        progressDialog.show()
        val fileRef=storageRef.child(kvar.id)
        databaseUser= FirebaseDatabase.getInstance("https://helponroad-8c6fe-default-rtdb.europe-west1.firebasedatabase.app/").getReference("kvar")
        fileRef.putFile(kvar.slika.toUri()).addOnSuccessListener {
            databaseUser!!.child(kvar.id).setValue(kvar)
        } .addOnProgressListener { taskSnapshot ->
            val percent = ((100 * taskSnapshot.bytesTransferred) / taskSnapshot.totalByteCount).toInt()
            progressDialog.progress = percent
            progressDialog.setMessage("Prijavljivanje kvara: $percent %")
        }.addOnCompleteListener { task ->
            progressDialog.dismiss()
        }
    }
    fun getSliku() {
        val fileRef = storageRef.child(vm.userovKvar.id)
        val local= File.createTempFile("tempImage","jpg")
        fileRef.getFile(local).addOnSuccessListener{
            val bitmap=BitmapFactory.decodeFile(local.absolutePath)
            binding.AddImageKvar.setImageBitmap(bitmap)
        }

    }
}