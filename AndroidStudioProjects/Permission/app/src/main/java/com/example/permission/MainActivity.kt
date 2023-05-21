package com.example.permission

import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.permission.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.*

class MainActivity : AppCompatActivity() {
    val binding by lazy { ActivityMainBinding.inflate(layoutInflater)}
    lateinit var permLauncher: ActivityResultLauncher<String>
    lateinit var location:String
    val stuDB: StudentDB by lazy { StudentDB.getInstance(this) }

    fun addStudents(view: View) {
        for (i in 0..4) {
            val stu = Student(0, "fName$i", "lName", (0..100).random(), "A+")
            stuDB.studentDAO().insert(stu)
        }
    }
    fun findStudent(view: View){
        GlobalScope.launch(Dispatchers.IO) {
            val stuList :List<String> = stuDB.studentDAO().searchStudent(90)
            Log.d("IISE",stuList.toString())
        }
    }
    override fun onSaveInstanceState(outState: Bundle) {
        outState.run {
            putString("location", location)
        }
        Log.d("IISE", "onSave called!")
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        location = savedInstanceState.getString("location").toString()
        binding.textView.text = savedInstanceState.getString("location")
    }

    fun loadPreference(view: View) {
        val sharedPref = getPreferences(Context.MODE_PRIVATE) ?: return
        Snackbar.make(
            binding.root,
            "my Code was ${sharedPref.getInt("IISECode", 0)} and my Grade was ${
                sharedPref.getString(
                    "IISEGrade",
                    "F"
                )
            }",
            Snackbar.LENGTH_LONG
        ).show()
    }

    fun savePreference(view: View){
        val sharedPref = getPreferences(Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putInt("IISECode", binding.editTextNumber.text.toString().toInt() )
            putString("IISEGrade",binding.editTextTextPersonName.text.toString())
            apply()
        }
        Toast.makeText(this,"preference saved",Toast.LENGTH_SHORT).show()
        binding.editTextNumber.text.clear()
        binding.editTextTextPersonName.text.clear()
    }

    fun saveFile(view: View){
        val filename = "myfile"
        //val file = File(filesDir, filename)
        val file = File(getExternalFilesDir(null), filename)
        Log.d("IISE","$file")
        val writer = BufferedWriter(FileWriter(file))
        val sharedPref = getPreferences(Context.MODE_PRIVATE) ?: return

        Toast.makeText(this,"File saved!",Toast.LENGTH_SHORT).show()
        writer.append("my Code was ${sharedPref.getInt("IISECode", 0)}")
        writer.append("my Grade was ${sharedPref.getString("IISEGrade", "A+")}")
        writer.close()
    }

    fun loadFile(view: View){
        val filename = "myfile"
        //val file = File(filesDir, filename)
        val file = File(getExternalFilesDir(null), filename)
        val files: Array<String> = fileList()
        for (i in files)
            Log.d("IISE", i)
        val reader = BufferedReader(FileReader(file))
        reader.readLines().forEach{
            Log.d("IISE",it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        Log.d("IISE", "External Storage Mounted: " +
                "${Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED}")
        val externalStorageVolumes: Array<out File> = ContextCompat.getExternalFilesDirs(applicationContext, null)
        for (ext in externalStorageVolumes) Log.d("IISE","ext device: $ext")
        val primaryExternalStorage = externalStorageVolumes[0]
        Log.d("IISE", "Primary ext storage: $primaryExternalStorage")

        permLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    Log.d(
                        "IISE",
                        "Now, permission granted by the user"
                    )
                }
                else{
                    Log.d("IISE","permission request denied. next time, we need to explain WHY.")
                }
            }

        binding.button.setOnClickListener() {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    "android.permission.ACCESS_FINE_LOCATION"
                ) ==
                        PERMISSION_GRANTED -> {
                    val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                        .toString()
                    Toast.makeText(this, location, Toast.LENGTH_LONG).show()
                    binding.textView.text = location
                }
            }
        }
        when {
            ContextCompat.checkSelfPermission (
                this, "android.permission.ACCESS_FINE_LOCATION"
            ) == PERMISSION_GRANTED -> {
//                val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
//                Log.d(
//                    "IISE",
//                    "${locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)}"
//                )
            }
            shouldShowRequestPermissionRationale("android.permission.ACCESS_FINE_LOCATION") -> {
                Log.d("IISE", "without this? you cannot use our app :D")
            }
            else -> {
                Log.d("IISE", "request permission")
                permLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }

        /*val perm = ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_NETWORK_STATE")
        if (perm == PERMISSION_GRANTED) {
            Log.d("IISE", "Permission granted!")
            val connMgr = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo: NetworkInfo? = connMgr.activeNetworkInfo
            Log.d("IISE", "Connected: ${networkInfo?.isConnected}")
        }*/

        /*val perm = ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION")
        if (perm == PERMISSION_GRANTED) {
            val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
            Log.d("IISE","${locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)}")
        }
        else{
            val permRationale = shouldShowRequestPermissionRationale("android.permission.ACCESS_FINE_LOCATION")
            Log.d("IISE","$permRationale")
        }*/

    }
}