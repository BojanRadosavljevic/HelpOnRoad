package elfak.mosis.helponroad.ViewModel

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Tasks.await
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import android.util.Log
import androidx.core.net.toUri
import com.google.firebase.storage.FirebaseStorage
import elfak.mosis.helponroad.Model.*

class UsersViewModel :ViewModel() {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private var databaseUser: DatabaseReference? = null
    private val storageRef = FirebaseStorage.getInstance().getReference()
    lateinit var user: User
    lateinit var shop: Radnja
    lateinit var userovKvar: Kvar
    var Logged = false
    lateinit var usluge: ArrayList<Usluga>
    lateinit var radnje: ArrayList<Radnja>
    lateinit var users: ArrayList<User>
    lateinit var uslugeradnje: MutableMap<Radnja, ArrayList<Usluga>>
    lateinit var kvaroviusera: MutableMap<User, Kvar>
    lateinit var filtraneUslugeRadnje: MutableMap<Radnja, ArrayList<Usluga>>
    lateinit var filtriraniKvaroviUsera: MutableMap<User,Kvar>
    lateinit var izabranaRadnja: Radnja
    lateinit var komentariradnje: ArrayList<Komentar>
    lateinit var izabranKvarUser: User
    lateinit var izabranKvarKvar: Kvar


    fun LoginUser(email: String, password: String) {
        databaseUser =
            FirebaseDatabase.getInstance("https://helponroad-8c6fe-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("user")
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener() {
            if (it.isSuccessful)
                databaseUser!!.child(it.result.user?.uid.toString()).get()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val dataSnapshot = task.result
                            if (dataSnapshot.exists() && dataSnapshot.child("password")
                                    .getValue(String::class.java) == password
                            ) {
                                getUserInformation(
                                    dataSnapshot.child("id").getValue(String::class.java)!!
                                )
                                Logged = true
                            }
                        }

                    }
        }

    }

    fun LoginShop(email: String, password: String) {
        databaseUser =
            FirebaseDatabase.getInstance("https://helponroad-8c6fe-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("shop")
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener() {
            if (it.isSuccessful)
                databaseUser!!.child(it.result.user?.uid.toString()).get()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val dataSnapshot = task.result
                            if (dataSnapshot.exists() && dataSnapshot.child("password")
                                    .getValue(String::class.java) == password
                            ) {
                                getShopInformation(
                                    dataSnapshot.child("id").getValue(String::class.java)!!
                                )
                                Log.d(
                                    "aaa",
                                    dataSnapshot.child("username").getValue(String::class.java)!!
                                )
                                Logged = true
                            }
                        }

                    }
        }
    }


    fun createNewUser(userC: User) {
        firebaseAuth.createUserWithEmailAndPassword(userC.email, userC.password)
            .addOnCompleteListener() {
                if (it.isSuccessful) {

                    userC.id = it.result.user?.uid.toString()

                    SaveUserInformation(userC)
                    Logged = true
                }
            }

    }

    @SuppressLint("SuspiciousIndentation")
    fun createNewShop(shopC: Radnja) {
        firebaseAuth.createUserWithEmailAndPassword(shopC.email, shopC.password)
            .addOnCompleteListener() {
                if (it.isSuccessful) {
                    shopC.id = it.result.user?.uid.toString()

                    SaveShopInformation(shopC)
                    Logged = true
                }
            }
    }

    fun SaveUserInformation(userC: User) {

        databaseUser =
            FirebaseDatabase.getInstance("https://helponroad-8c6fe-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("user")
        databaseUser!!.child(userC.id).setValue(userC).addOnSuccessListener {
            Log.d("aaa", "uspesno dodat user")
            user = userC
        }
    }

    fun SaveShopInformation(shopC: Radnja) {
        databaseUser =
            FirebaseDatabase.getInstance("https://helponroad-8c6fe-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("shop")
        databaseUser!!.child(shopC.id).setValue(shopC).addOnSuccessListener {
            Log.d("aaa", "uspesno dodat user")
            shop = shopC
        }
    }

    fun getUserInformation(userId: String) {
        databaseUser =
            FirebaseDatabase.getInstance("https://helponroad-8c6fe-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("user")
        databaseUser!!.child(userId).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val dataSnapshot = task.result
                if (dataSnapshot.exists()) {
                    user = User(
                        dataSnapshot.child("id").getValue(String::class.java)!!,
                        dataSnapshot.child("userName").getValue(String::class.java)!!,
                        dataSnapshot.child("brojTelefona").getValue(String::class.java)!!,
                        dataSnapshot.child("brojPoena").getValue(Int::class.java)!!,
                        dataSnapshot.child("email").getValue(String::class.java)!!,
                        dataSnapshot.child("password").getValue(String::class.java)!!,
                        dataSnapshot.child("strana").getValue(Boolean::class.java)!!
                    )
                }
            }
        }
    }

    fun getShopInformation(userUid: String) {
        databaseUser =
            FirebaseDatabase.getInstance("https://helponroad-8c6fe-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("shop")
        databaseUser!!.child(userUid).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val dataSnapshot = task.result
                if (dataSnapshot.exists()) {
                    shop = Radnja(
                        dataSnapshot.child("id").getValue(String::class.java)!!,
                        dataSnapshot.child("ime").getValue(String::class.java)!!,
                        dataSnapshot.child("username").getValue(String::class.java)!!,
                        dataSnapshot.child("brojTelefona").getValue(String::class.java)!!,
                        dataSnapshot.child("email").getValue(String::class.java)!!,
                        dataSnapshot.child("password").getValue(String::class.java)!!,
                        dataSnapshot.child("brojPoena").getValue(Int::class.java)!!,
                        dataSnapshot.child("latitude").getValue(Double::class.java)!!,
                        dataSnapshot.child("longitude").getValue(Double::class.java)!!,
                        dataSnapshot.child("strana").getValue(Boolean::class.java)!!
                    )
                }
            }
        }
    }

    fun changeUserInformation(userId: String) {
        databaseUser =
            FirebaseDatabase.getInstance("https://helponroad-8c6fe-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("user")
        databaseUser!!.child(userId).setValue(user).addOnSuccessListener {
            Log.d("aaa", "uspesno dodat user")
        }
    }

    fun changeRadnjaInformation(shopId: String) {
        databaseUser =
            FirebaseDatabase.getInstance("https://helponroad-8c6fe-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("shop")
        databaseUser!!.child(shopId).setValue(shop).addOnSuccessListener {
            Log.d("aaa", "uspesno dodat user")
        }
    }

    fun deleteUserAccount(userId: String) {
        databaseUser =
            FirebaseDatabase.getInstance("https://helponroad-8c6fe-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("user")
        var pomdatabaseUser=FirebaseDatabase.getInstance("https://helponroad-8c6fe-default-rtdb.europe-west1.firebasedatabase.app/")
            .getReference("kvar")
        pomdatabaseUser!!.child(userId).removeValue().addOnSuccessListener {
            val fileRef = storageRef.child(userId).delete()
        }
        databaseUser!!.child(userId).removeValue().addOnSuccessListener {
            Log.d("aaa", "uspesno izbrisan user")
        }
    }

    fun deleteShopAccount(shopId: String) {
        databaseUser =
            FirebaseDatabase.getInstance("https://helponroad-8c6fe-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("shop")
        databaseUser!!.child(shopId).removeValue().addOnSuccessListener {
            var databaseUser2 =
                FirebaseDatabase.getInstance("https://helponroad-8c6fe-default-rtdb.europe-west1.firebasedatabase.app/")
                    .getReference("usluga")
            databaseUser2!!.child(shopId).removeValue().addOnSuccessListener {
                Log.d("aaa", "izbrisana dodata usluga")
            }
        }
    }

    fun addUslugeRadnji(shopId: String) {
        deleteUslugeRadni(shopId)
        databaseUser =
            FirebaseDatabase.getInstance("https://helponroad-8c6fe-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("usluga")
        for (u in usluge)
            databaseUser!!.child(shopId).child(u.id).setValue(u).addOnSuccessListener {
                Log.d("aaa", "uspesno dodata usluga")
            }
    }

    fun getUslugeRadnji(shopId: String) {
        usluge = ArrayList<Usluga>()
        databaseUser =
            FirebaseDatabase.getInstance("https://helponroad-8c6fe-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("usluga")
        databaseUser!!.child(shopId).get().addOnCompleteListener {
            if (it.isSuccessful) {
                val Snapshot = it.result
                if (Snapshot.exists()) {
                    for (u in Snapshot.children) {
                        val us = Usluga(
                            u.child("id").getValue(String::class.java)!!,
                            u.child("ime").getValue(String::class.java)!!,
                            u.child("radnja").getValue(String::class.java)!!,
                            u.child("cena").getValue(Int::class.java)!!
                        )
                        usluge.add(us)
                    }
                }
            }
        }
    }

    fun deleteUslugeRadni(shopId: String) {
        databaseUser =
            FirebaseDatabase.getInstance("https://helponroad-8c6fe-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("usluga")
        databaseUser!!.child(shopId).removeValue().addOnSuccessListener {
            Log.d("aaa", "izbrisana dodata usluga")
        }
    }

    fun getTop10Users() {
        users = ArrayList<User>()
        kvaroviusera= mutableMapOf()
        databaseUser = FirebaseDatabase.getInstance("https://helponroad-8c6fe-default-rtdb.europe-west1.firebasedatabase.app/").getReference("user")
        databaseUser!!.get().addOnCompleteListener {
            if (it.isSuccessful) {
                val Snapshot = it.result
                if (Snapshot.exists()) {
                    for (u in Snapshot.children) {
                        val us = User(
                            u.child("id").getValue(String::class.java)!!,
                            u.child("userName").getValue(String::class.java)!!,
                            u.child("brojTelefona").getValue(String::class.java)!!,
                            u.child("brojPoena").getValue(Int::class.java)!!,
                            u.child("email").getValue(String::class.java)!!,
                            u.child("password").getValue(String::class.java)!!,
                            u.child("strana").getValue(Boolean::class.java)!!
                        )
                        users.add(us)
                        var pom: DatabaseReference? =FirebaseDatabase.getInstance("https://helponroad-8c6fe-default-rtdb.europe-west1.firebasedatabase.app/").getReference("kvar")
                        pom!!.child(us.id).get().addOnSuccessListener {
                                if (it.exists()) {
                                    var uk = Kvar(
                                        it.child("id").getValue(String::class.java)!!,
                                        it.child("user").getValue(String::class.java)!!,
                                        it.child("slika").getValue(String::class.java)!!,
                                        it.child("opis").getValue(String::class.java)!!,
                                        it.child("latitude").getValue(Double::class.java)!!,
                                        it.child("longitude").getValue(Double::class.java)!!,
                                        it.child("status").getValue(String::class.java)!!
                                    )
                                    kvaroviusera.put(us,uk)
                                }
                            }
                        }
                    }
                }
                users.sortByDescending { it.brojPoena }
            }
    }

    fun getAllRadnja() {
        filtraneUslugeRadnje = mutableMapOf()
        radnje = ArrayList<Radnja>()
        databaseUser =
            FirebaseDatabase.getInstance("https://helponroad-8c6fe-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("shop")
        databaseUser!!.get().addOnCompleteListener {
            if (it.isSuccessful) {
                val Snapshot = it.result
                if (Snapshot.exists()) {
                    for (u in Snapshot.children) {
                        val us = Radnja(
                            u.child("id").getValue(String::class.java)!!,
                            u.child("ime").getValue(String::class.java)!!,
                            u.child("username").getValue(String::class.java)!!,
                            u.child("brojTelefona").getValue(String::class.java)!!,
                            u.child("email").getValue(String::class.java)!!,
                            u.child("password").getValue(String::class.java)!!,
                            u.child("brojPoena").getValue(Int::class.java)!!,
                            u.child("latitude").getValue(Double::class.java)!!,
                            u.child("longitude").getValue(Double::class.java)!!,
                            u.child("strana").getValue(Boolean::class.java)!!
                        )
                        radnje.add(us)
                    }
                }
                radnje.sortByDescending { it.brojPoena }
                getUslugeSvihRadnji()
            }
        }

    }

    fun getUslugeSvihRadnji() {
        uslugeradnje = mutableMapOf()
        databaseUser =
            FirebaseDatabase.getInstance("https://helponroad-8c6fe-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("usluga")
        for (r in radnje) {
            databaseUser!!.child(r.id).get().addOnCompleteListener {
                if (it.isSuccessful) {
                    val Snapshot = it.result
                    if (Snapshot.exists()) {
                        var usl = ArrayList<Usluga>()

                        for (u in Snapshot.children) {
                            val us = Usluga(
                                u.child("id").getValue(String::class.java)!!,
                                u.child("ime").getValue(String::class.java)!!,
                                u.child("radnja").getValue(String::class.java)!!,
                                u.child("cena").getValue(Int::class.java)!!
                            )
                            usl.add(us)
                        }
                        uslugeradnje.put(r, usl)
                    }
                }
            }
        }
    }

    fun getKvarUsera() {
        val fileRef = storageRef.child(user.id)
        databaseUser =
            FirebaseDatabase.getInstance("https://helponroad-8c6fe-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("kvar")
        databaseUser!!.child(user.id).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val dataSnapshot = task.result
                if (dataSnapshot.exists()) {
                    userovKvar = Kvar(
                        dataSnapshot.child("id").getValue(String::class.java)!!,
                        dataSnapshot.child("user").getValue(String::class.java)!!,
                        dataSnapshot.child("slika").getValue(String::class.java)!!,
                        dataSnapshot.child("opis").getValue(String::class.java)!!,
                        dataSnapshot.child("latitude").getValue(Double::class.java)!!,
                        dataSnapshot.child("longitude").getValue(Double::class.java)!!,
                        dataSnapshot.child("status").getValue(String::class.java)!!
                    )
                } else userovKvar = Kvar("", "", "", "", 0.0, 0.0, "")
            }
        }
    }


    fun deleteKvar() {
        val fileRef = storageRef.child(user.id).delete()
        databaseUser =
            FirebaseDatabase.getInstance("https://helponroad-8c6fe-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("kvar")
        databaseUser!!.child(user.id).removeValue().addOnSuccessListener {
            userovKvar = Kvar("", "", "", "", 0.0, 0.0, "")
        }
    }

    fun addKomentar(kom: Komentar) {
        databaseUser =
            FirebaseDatabase.getInstance("https://helponroad-8c6fe-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("komentar")
        databaseUser!!.child(kom.radnja).child(kom.id).setValue(kom).addOnSuccessListener {
            Log.d("aaa", "uspesno dodat komentar")
        }
    }

    fun getKomentareRadnje() {
        komentariradnje = arrayListOf()
        databaseUser =
            FirebaseDatabase.getInstance("https://helponroad-8c6fe-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("komentar")
        databaseUser!!.child(izabranaRadnja?.id!!).get().addOnSuccessListener {
            val Snapshot = it
            if (Snapshot.exists()) {
                for (u in Snapshot.children) {
                    val us = Komentar(
                        u.child("id").getValue(String::class.java)!!,
                        u.child("user").getValue(String::class.java)!!,
                        u.child("radnja").getValue(String::class.java)!!,
                        u.child("komentar").getValue(String::class.java)!!,
                        u.child("datumIVreme").getValue(String::class.java)!!,
                    )
                    komentariradnje.add(us)
                }
            }
        }
    }

    fun povecajPoene(komentar: Boolean) {
        if (komentar) {
            user.brojPoena += 1
            databaseUser =
                FirebaseDatabase.getInstance("https://helponroad-8c6fe-default-rtdb.europe-west1.firebasedatabase.app/")
                    .getReference("user")
            databaseUser!!.child(user.id).setValue(user).addOnSuccessListener {
                izabranaRadnja?.brojPoena = izabranaRadnja?.brojPoena!! + 1
                var pom: DatabaseReference? =
                    FirebaseDatabase.getInstance("https://helponroad-8c6fe-default-rtdb.europe-west1.firebasedatabase.app/")
                        .getReference("shop")
                pom!!.child(izabranaRadnja?.id!!).setValue(izabranaRadnja).addOnSuccessListener {}
            }
        } else {
            shop.brojPoena += 5
            databaseUser =
                FirebaseDatabase.getInstance("https://helponroad-8c6fe-default-rtdb.europe-west1.firebasedatabase.app/")
                    .getReference("shop")
            databaseUser!!.child(shop.id).setValue(shop).addOnSuccessListener {
                izabranKvarUser.brojPoena = izabranKvarUser.brojPoena + 3
                var pom: DatabaseReference? =
                    FirebaseDatabase.getInstance("https://helponroad-8c6fe-default-rtdb.europe-west1.firebasedatabase.app/")
                        .getReference("user")
                pom!!.child(izabranKvarUser.id).setValue(izabranKvarUser).addOnSuccessListener {
                    izabranKvarKvar = Kvar("", "", "", "", 0.0, 0.0, "")
                    izabranKvarUser = User("", "", "", 0, "", "", false)
                }
            }
        }
    }
    fun changeKvar(kv:Kvar)
    {
        databaseUser = FirebaseDatabase.getInstance("https://helponroad-8c6fe-default-rtdb.europe-west1.firebasedatabase.app/").getReference("kvar")
        databaseUser!!.child(kv.id).setValue(kv).addOnSuccessListener {

        }
    }
    fun resiKvar(flag:Boolean){
        val fileRef = storageRef.child(izabranKvarUser.id).delete()
        databaseUser =
            FirebaseDatabase.getInstance("https://helponroad-8c6fe-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("kvar")
        databaseUser!!.child(izabranKvarUser.id).removeValue().addOnSuccessListener {

            povecajPoene(flag)
        }

    }
}
