package com.example.builday365.view

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback
import androidx.core.content.ContextCompat
//import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.builday365.R
//import com.example.builday365.databinding.FragmentMapsBinding
import com.example.builday365.viewmodel.MapsViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnCameraMoveStartedListener
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException
import java.util.Locale
import java.util.Random

class MapsFragment : Fragment(), OnMapLongClickListener, OnCameraMoveStartedListener,
    OnRequestPermissionsResultCallback {
    var LOCATION_PERMISSIONS = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
    private var mMapsViewModel: MapsViewModel? = null
//    private var mBinding: FragmentMapsBinding? = null
    private var mGoogleMap: GoogleMap? = null
    private var mHomeMarker: Marker? = null
    private var mLastLocationMarker: Marker? = null
    private var mLastLocationMarkerOption: MarkerOptions? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "[onCreateView]")
//        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_maps, container, false)
//        mBinding.setLifecycleOwner(this)
//        return mBinding.getRoot()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "[onViewCreated]")
        super.onViewCreated(view, savedInstanceState)
        mMapsViewModel = ViewModelProvider(this).get(MapsViewModel::class.java)
        mMapsViewModel!!.setFocused(true)
//        mBinding!!.mapsViewModel = mMapsViewModel
//        mBinding!!.fab.setOnClickListener { mMapsViewModel!!.onFabClick() }
        observe()
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    override fun onResume() {
        super.onResume()
        if (mGoogleMap != null) {
            setLastLocation()
        }
    }

    private var mapsFragmentListener: MapsFragmentListener? = null

    interface MapsFragmentListener {
        fun onInputASent(input: CharSequence)
    }

    private val callback = OnMapReadyCallback { googleMap ->

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        Log.d(TAG, "[onMapReady]")
        mGoogleMap = googleMap
        mGoogleMap!!.uiSettings.isZoomGesturesEnabled = true
        mGoogleMap!!.setOnMapLongClickListener(this@MapsFragment)
        mGoogleMap!!.setOnCameraMoveStartedListener(this@MapsFragment)
        setLastLocation()
    }

    override fun onMapLongClick(latLng: LatLng) {
        Log.d(TAG, "[onMapLongClick] $latLng")
        val address = getAddress(latLng.latitude, latLng.longitude)
        if (address.isEmpty()) {
            Log.d(TAG, "[onMapLongClick] not found address")
            return
        }
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("Add Home")
        builder.setMessage("Is your home here?\n$address")
        builder.setCancelable(true)
        builder.setPositiveButton(
            "Yes"
        ) { dialog, id -> mMapsViewModel!!.setHomeLocation(latLng) }
        builder.setNegativeButton(
            "No"
        ) { dialog, id ->
            // Nothing
        }
        val dialog = builder.create()
        dialog.show()
    }

    override fun onCameraMoveStarted(i: Int) {
        if (mMapsViewModel == null) return
        if (i == OnCameraMoveStartedListener.REASON_GESTURE) {
//            mBinding!!.fab.setImageResource(R.drawable.btn_move_currentlocation2)
            mMapsViewModel!!.setFocused(false)
        }
    }

    override fun onRequestPermissionsResult(
        permsRequestCode: Int,
        permissions: Array<String>,
        grandResults: IntArray
    ) {

        super.onRequestPermissionsResult(permsRequestCode, permissions, grandResults)
        Log.d(TAG, "[onRequestPermissionsResult]$permsRequestCode")
        if (permsRequestCode == LOCATION_PERMISSION_CODE && grandResults.size == LOCATION_PERMISSIONS.size) {
            for (result in grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "[onRequestPermissionsResult] permission is denied")
                    return
                }
            }
            setLastLocation()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (ContextCompat.checkSelfPermission(
                        requireActivity(),
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    permissionDialog()
                }
            }
        }
    }

    private fun observe() {
        if (mMapsViewModel == null) return
        mMapsViewModel!!.focused.observe(viewLifecycleOwner, object : Observer<Boolean?> {
            override fun onChanged(t: Boolean?) {
                Log.d(TAG, "[onChanged] focused $t")
                if (t == true) {
                    val data = mMapsViewModel!!.lastLocation.value ?: return
                    val cameraUpdate = CameraUpdateFactory.newLatLngZoom(data, 15f)
                    mGoogleMap!!.animateCamera(cameraUpdate)
//                    mBinding!!.fab.setImageResource(R.drawable.btn_move_currentlocation1)
                }
            }
        })
        mMapsViewModel!!.lastLocation.observe(viewLifecycleOwner, object : Observer<LatLng?> {




            override fun onChanged(t: LatLng?) {
                if (t == null) return
                Log.d(TAG, "[onChanged] lastLocation $t")
                setLastLocationMarker(t)
            }
        })
        mMapsViewModel!!.homeLocation.observe(viewLifecycleOwner, object : Observer<LatLng?> {
            override fun onChanged(t: LatLng?) {
                if (t == null) return
                Log.d(TAG, "[onChanged] homeLocation $t")
                setHomeLocationMarker(t)
            }
        })
    }

    private fun setLastLocation() {
        Log.d(TAG, "[setLastLocation]")
        if (checkPermission() == false) return
        val locationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val lastLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        if (lastLocation == null) {
            Log.d(TAG, "[setLastLocation] the last location info is not exist")
            return
        }
        val lastLatitude = lastLocation.latitude
        val lastLongtitude = lastLocation.longitude
        val lastLocationLatLng = LatLng(lastLatitude, lastLongtitude)
        mMapsViewModel!!.setLastLocation(lastLocationLatLng)
    }

    private fun setLastLocationMarker(location: LatLng?) {
        if (location == null) return
        if (mLastLocationMarker == null) {
            val markerOptions = MarkerOptions()
            markerOptions.position(location)
            val catList = intArrayOf(
                R.drawable.builday_icon_cat,
                R.drawable.builday_logo_cat,
                R.drawable.builday_current_marker1,
                R.drawable.builday_current_marker2
            )
            val random = Random()
            var catId = 0
            catId = catList[random.nextInt(4)]
            Log.d(TAG, "[setLastLocationMarker] catId=$catId")
            val bitmapdraw = resources.getDrawable(catId) as BitmapDrawable
            val catMarker = Bitmap.createScaledBitmap(bitmapdraw.bitmap, 300, 300, false)
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(catMarker))
            mLastLocationMarker = mGoogleMap!!.addMarker(markerOptions)
            mLastLocationMarkerOption = markerOptions
        } else {
            mLastLocationMarker!!.remove()
            mLastLocationMarkerOption!!.position(location)
            mLastLocationMarker = mGoogleMap!!.addMarker(mLastLocationMarkerOption!!)
        }
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(location, 15f)
        mGoogleMap!!.animateCamera(cameraUpdate)
        //mGoogleMap.moveCamera(cameraUpdate);
    }

    private fun setHomeLocationMarker(location: LatLng) {
        if (mHomeMarker != null) {
            mHomeMarker!!.remove()
        }
        Log.d(TAG, "[setHomeLocationMarker] $location")
        val markerView = LayoutInflater.from(activity).inflate(R.layout.layout_marker, null)
        val markerOptions = MarkerOptions()
        markerOptions.position(location)
        markerOptions.icon(
            BitmapDescriptorFactory.fromBitmap(
                createDrawableFromView(
                    activity,
                    markerView
                )
            )
        )
        mHomeMarker = mGoogleMap!!.addMarker(markerOptions)
    }

    private fun createDrawableFromView(context: Context?, view: View): Bitmap {
        val displayMetrics = DisplayMetrics()
        (context as Activity?)!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
        view.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels)
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels)
        view.buildDrawingCache()
        val bitmap =
            Bitmap.createBitmap(view.measuredWidth, view.measuredHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    private fun getAddress(latitude: Double, longitude: Double): String {
        Log.d(TAG, "[getAddress]")
        val geocoder = Geocoder(requireActivity(), Locale.getDefault())
        var addresses: List<Address>? = null
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 7)
        } catch (ioException: IOException) {
            Toast.makeText(activity, "Cannot use geocoder service", Toast.LENGTH_LONG).show()
        } catch (illegalArgumentException: IllegalArgumentException) {
            Toast.makeText(activity, "Invalid location", Toast.LENGTH_LONG).show()
        }
        if (addresses!!.isEmpty()) {
            Toast.makeText(activity, "Cannot find the address", Toast.LENGTH_LONG).show()
            return ""
        }
        val address = addresses[0]
        return """
            ${address.getAddressLine(0)}
            
            """.trimIndent()
    }

    private fun checkPermission(): Boolean {
        Log.d(TAG, "[checkPermission]")
        if (ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            if (ContextCompat.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                return true
            }
        }
        requestPermissions(LOCATION_PERMISSIONS, LOCATION_PERMISSION_CODE)
        return false
    }

    private fun permissionDialog() {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("백그라운드 위치 권한 설정")
        builder.setMessage("백그라운드 위치 권한을 위해 항상 허용으로 설정해주세요.")
        val listener = DialogInterface.OnClickListener { dialog, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> requestPermissions(
                    arrayOf(
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION
                    ), BACKGROUND_PERMISSION_CODE
                )
            }
        }
        builder.setPositiveButton("네", listener)
        builder.setNegativeButton("아니오", null)
        builder.show()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mapsFragmentListener = if (context is MapsFragmentListener) {
            context
        } else {
            throw RuntimeException("$context must implement FragmentListner")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mapsFragmentListener = null
    }

    companion object {
        private const val TAG = "MapsFragment"
        private const val LOCATION_PERMISSION_CODE = 100
        private const val BACKGROUND_PERMISSION_CODE = 200
    }
}