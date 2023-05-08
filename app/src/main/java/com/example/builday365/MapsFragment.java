package com.example.builday365;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class MapsFragment extends Fragment implements GoogleMap.OnMapLongClickListener, GoogleMap.OnCameraMoveStartedListener,
        ActivityCompat.OnRequestPermissionsResultCallback {
    private static final String TAG = "MapsFragment";

    String[] REQUIRED_PERMISSIONS = {
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
    };
    private static final int PERMISSIONS_REQUEST_CODE = 100;

    private GoogleMap mGoogleMap;
    private LatLng mHomeLocation;
    private LatLng mLastLocation;
    private Marker mHomeMarker;
    FloatingActionButton mFab;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            Log.d(TAG, "[onMapReady]");
            mGoogleMap = googleMap;
            mGoogleMap.getUiSettings().setZoomGesturesEnabled(true);
            mGoogleMap.setOnMapLongClickListener(MapsFragment.this);
            mGoogleMap.setOnCameraMoveStartedListener(MapsFragment.this);

            mLastLocation = getLastLocation();
            if (mLastLocation == null) {
                Log.d(TAG, "[onMapReady] request permission");
                ActivityCompat.requestPermissions(getActivity(), REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE);
                return;
            }
            setLastLocationMarker(mLastLocation);
            setHomeLocationMarker();
            setFabListener();
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "[onCreateView]");
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "[onViewCreated]");
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    @Override
    public void onMapLongClick(@NonNull LatLng latLng) {
        Log.d(TAG, "[onMapLongClick] " + latLng.toString());

        String address = getAddress(latLng.latitude, latLng.longitude);
        if (address.isEmpty()) {
            Log.d(TAG, "[onMapLongClick] not found address");
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Home");
        builder.setMessage("Is your home here?\n" + address);
        builder.setCancelable(true);
        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mHomeLocation = latLng;
                        setHomeLocationMarker();
                    }
                });

        builder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Nothing
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    @Override
    public void onRequestPermissionsResult(int permsRequestCode, @NonNull String[] permissions, @NonNull int[] grandResults) {
        super.onRequestPermissionsResult(permsRequestCode, permissions, grandResults);
        Log.d(TAG, "[onRequestPermissionsResult]");

        if (permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {
            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "[onRequestPermissionsResult] permission is denied");
                    return;
                }
            }
            LatLng lastLatLng = getLastLocation();
            if (lastLatLng != null) {
                setLastLocationMarker(lastLatLng);
            }
        }
    }
    private void setLastLocationMarker(LatLng location) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(location);

        int catList[] = {
                R.drawable.builday_icon_cat,
                R.drawable.builday_logo_cat,
                R.drawable.builday_current_marker1,
                R.drawable.builday_current_marker2
        };
        Random random = new Random();
        int catId = catList[random.nextInt(4)];

        Log.d(TAG, "[setLastLocationMarker] catId=" + catId);
        BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(catId);
        Bitmap catMarker = Bitmap.createScaledBitmap(bitmapdraw.getBitmap(), 300, 300, false);
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(catMarker));
        mGoogleMap.addMarker(markerOptions);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(location, 15);
        mGoogleMap.animateCamera(cameraUpdate);
        //mGoogleMap.moveCamera(cameraUpdate);
    }

    private void setHomeLocationMarker() {
        if (mHomeLocation == null) {
            Log.d(TAG, "[setHomeLocationMarker] there is no home location");
            return;
        }
        if (mHomeMarker != null) {
            mHomeMarker.remove();
        }
        Log.d(TAG, "[setHomeLocationMarker] " + mHomeLocation.toString());
        View markerView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_marker, null);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(mHomeLocation);
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(getActivity(), markerView)));
        mHomeMarker = mGoogleMap.addMarker(markerOptions);
    }

    private Bitmap createDrawableFromView(Context context, View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }

    private LatLng getLastLocation() {
        Log.d(TAG, "[getLastLocation]");
        // permission check
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "[getLastLocation] permission denied");
            return null;
        }
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        Location lastLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (lastLocation == null) {
            Log.d(TAG, "[getLastLocation] the last location info is not exist");
            return null;
        }
        double lastLatitude = lastLocation.getLatitude();
        double lastLongtitude  = lastLocation.getLongitude();

        LatLng lastLocationLatLng = new LatLng(lastLatitude, lastLongtitude);
        return lastLocationLatLng;
    }

    public String getAddress( double latitude, double longitude) {
        Log.d(TAG, "[getAddress]");
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        List<Address> addresses = null;

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 7);
        } catch (IOException ioException) {
            Toast.makeText(getActivity(), "Cannot use geocoder service", Toast.LENGTH_LONG).show();
        } catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(getActivity(), "Invalid location", Toast.LENGTH_LONG).show();
        }

        if (addresses.isEmpty()) {
            Toast.makeText(getActivity(), "Cannot find the address", Toast.LENGTH_LONG).show();
            return "";
        }
        Address address = addresses.get(0);
        return address.getAddressLine(0).toString()+"\n";
    }

    private void setFabListener() {
        mFab = getActivity().findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFab.setImageResource(R.drawable.builday_icon_currentlocation1);
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(mLastLocation, 15);
                mGoogleMap.animateCamera(cameraUpdate);
            }
        });
    }

    @Override
    public void onCameraMoveStarted(int i) {
        if (mFab == null) {
            Log.w(TAG, "[onCameraMoveStarted] invalide operation");
            return;
        }
        Log.d(TAG, "[onCameraMoveStarted] " + i);
        if (i == REASON_GESTURE) {
            mFab.setImageResource(R.drawable.builday_icon_currentlocation2);
        }
    }
}