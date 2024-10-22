package com.example.builday365.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
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
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.builday365.R;
import com.example.builday365.databinding.FragmentMapsBinding;
import com.example.builday365.viewmodel.MapsViewModel;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class MapsFragment extends Fragment implements GoogleMap.OnMapLongClickListener,
        GoogleMap.OnCameraMoveStartedListener,
        ActivityCompat.OnRequestPermissionsResultCallback {
    private static final String TAG = "MapsFragment";
    private static final int LOCATION_PERMISSION_CODE = 100;
    private static final int BACKGROUND_PERMISSION_CODE = 200;
    String[] LOCATION_PERMISSIONS = {
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
    };

    private MapsViewModel mMapsViewModel;
    private FragmentMapsBinding mBinding;
    private GoogleMap mGoogleMap;
    private Marker mHomeMarker;
    private Marker mLastLocationMarker;
    private MarkerOptions mLastLocationMarkerOption;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "[onCreateView]");
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_maps, container, false);
        mBinding.setLifecycleOwner(this);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "[onViewCreated]");
        super.onViewCreated(view, savedInstanceState);
        mMapsViewModel = new ViewModelProvider(this).get(MapsViewModel.class);
        mMapsViewModel.setFocused(true);
        mBinding.setMapsViewModel(mMapsViewModel);
        mBinding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMapsViewModel.onFabClick();
            }
        });
        observe();

        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleMap != null) {
            setLastLocation();
        }
    }

    private MapsFragmentListener mapsFragmentListener;

    public interface MapsFragmentListener{
        void onInputASent(CharSequence input);
    }

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

            setLastLocation();
        }
    };

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
                        mMapsViewModel.setHomeLocation(latLng);
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
    public void onCameraMoveStarted(int i) {
        if (mMapsViewModel == null) return;
        if (i == REASON_GESTURE) {
            mBinding.fab.setImageResource(R.drawable.btn_move_currentlocation2);
            mMapsViewModel.setFocused(false);
        }
    }

    @Override
    public void onRequestPermissionsResult(int permsRequestCode, @NonNull String[] permissions, @NonNull int[] grandResults) {
        super.onRequestPermissionsResult(permsRequestCode, permissions, grandResults);
        Log.d(TAG, "[onRequestPermissionsResult]" + permsRequestCode);

        if (permsRequestCode == LOCATION_PERMISSION_CODE && grandResults.length == LOCATION_PERMISSIONS.length) {
            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "[onRequestPermissionsResult] permission is denied");
                    return;
                }
            }
            setLastLocation();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    permissionDialog();
                }
            }
        }
    }

    private void observe() {
        if (mMapsViewModel == null) return;
        mMapsViewModel.getFocused().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                Log.d(TAG, "[onChanged] focused " + aBoolean);
                if (aBoolean == true) {
                    LatLng data = mMapsViewModel.getLastLocation().getValue();
                    if (data == null) return;
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(data, 15);
                    mGoogleMap.animateCamera(cameraUpdate);
                    mBinding.fab.setImageResource(R.drawable.btn_move_currentlocation1);
                }
            }
        });

        mMapsViewModel.getLastLocation().observe(getViewLifecycleOwner(), new Observer<LatLng>() {
            @Override
            public void onChanged(LatLng latLng) {
                if (latLng == null) return;
                Log.d(TAG, "[onChanged] lastLocation " + latLng);
                setLastLocationMarker(latLng);
            }
        });

        mMapsViewModel.getHomeLocation().observe(getViewLifecycleOwner(), new Observer<LatLng>() {
            @Override
            public void onChanged(LatLng latLng) {
                if (latLng == null) return;
                Log.d(TAG, "[onChanged] homeLocation " + latLng);
                setHomeLocationMarker(latLng);
            }
        });
    }

    private void setLastLocation() {
        Log.d(TAG, "[setLastLocation]");
        if (checkPermission() == false) return;

        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        Location lastLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (lastLocation == null) {
            Log.d(TAG, "[setLastLocation] the last location info is not exist");
            return;
        }
        double lastLatitude = lastLocation.getLatitude();
        double lastLongtitude  = lastLocation.getLongitude();

        LatLng lastLocationLatLng = new LatLng(lastLatitude, lastLongtitude);
        mMapsViewModel.setLastLocation(lastLocationLatLng);
    }

    private void setLastLocationMarker(LatLng location) {
        if (location == null) return;

        if (mLastLocationMarker == null) {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(location);

            int catList[] = {
                    R.drawable.builday_icon_cat,
                    R.drawable.builday_logo_cat,
                    R.drawable.builday_current_marker1,
                    R.drawable.builday_current_marker2
            };
            Random random = new Random();
            int catId = 0;
            catId = catList[random.nextInt(4)];

            Log.d(TAG, "[setLastLocationMarker] catId=" + catId);
            BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(catId);
            Bitmap catMarker = Bitmap.createScaledBitmap(bitmapdraw.getBitmap(), 300, 300, false);

            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(catMarker));
            mLastLocationMarker = mGoogleMap.addMarker(markerOptions);
            mLastLocationMarkerOption = markerOptions;
        } else {
            mLastLocationMarker.remove();
            mLastLocationMarkerOption.position(location);
            mLastLocationMarker = mGoogleMap.addMarker(mLastLocationMarkerOption);
        }

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(location, 15);
        mGoogleMap.animateCamera(cameraUpdate);
        //mGoogleMap.moveCamera(cameraUpdate);
    }

    private void setHomeLocationMarker(LatLng location) {
        if (mHomeMarker != null) {
            mHomeMarker.remove();
        }
        Log.d(TAG, "[setHomeLocationMarker] " + location.toString());
        View markerView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_marker, null);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(location);
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

    private String getAddress(double latitude, double longitude) {
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

    private boolean checkPermission() {
        Log.d(TAG, "[checkPermission]");
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                return true;
            }
        }
        requestPermissions(LOCATION_PERMISSIONS, LOCATION_PERMISSION_CODE);
        return false;
    }

    private void permissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("백그라운드 위치 권한 설정");
        builder.setMessage("백그라운드 위치 권한을 위해 항상 허용으로 설정해주세요.");

        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        requestPermissions(new String[]{android.Manifest.permission.ACCESS_BACKGROUND_LOCATION},BACKGROUND_PERMISSION_CODE);
                        break;
                }
            }
        };
        builder.setPositiveButton("네", listener);
        builder.setNegativeButton("아니오", null);

        builder.show();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof MapsFragmentListener){
            mapsFragmentListener = (MapsFragmentListener)context;
        } else{
            throw new RuntimeException(context.toString() + " must implement FragmentListner");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mapsFragmentListener = null;
    }
}