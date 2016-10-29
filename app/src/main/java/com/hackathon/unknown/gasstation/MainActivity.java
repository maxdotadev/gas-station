package com.hackathon.unknown.gasstation;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.hackathon.unknown.gasstation.network.PlacesService;
import com.hackathon.unknown.gasstation.network.RetrofitHelper;
import com.hackathon.unknown.gasstation.network.model.GooglePlace;
import com.hackathon.unknown.gasstation.network.model.PlacesResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.hackathon.unknown.gasstation.R.id.map;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener, View.OnClickListener {
    private static final int LOCATION_DIFF = 500;
    private static final String SEARCH_TYPE_GAS_STATION = "gas_station";
    private static final String SEARCH_TYPE_HOSPITAL = "hospital";
    private static final String SEARCH_TYPE_SCHOOL = "school";
    private static final String SEARCH_TYPE_RESTAURANT = "restaurant";
    private static final String SEARCH_TYPE_POST_OFFICE = "post_station";
    private static final String SEARCH_TYPE_BUS_STATION = "bus_station";
    private static final String SEARCH_TYPE_BANK = "bank";

    private Handler mHandler;
    private PlacesResult mResult;
    private PlacesService mPlacesService;
    private MapFragment mMapFragment;
    private GoogleMap mGoogleMap;
    private Location mCurrentLocation;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private String mSearchType = SEARCH_TYPE_SCHOOL;


    RelativeLayout relativeLayout;
    Button btnGetDirections;
    Marker mMarker;
    ProgressDialog progressDialog;
    Polyline mPolyline;
    String path;
    //            +"&destination="+"&language=vi";
    String ss = null;
    ArrayList<DirectionStep> arrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();
        getPreviousLocation();

        mMapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(map);
        mMapFragment.getMapAsync(this);


        relativeLayout = (RelativeLayout) findViewById(R.id.layoutDirections);
        relativeLayout.setVisibility(View.GONE);

        btnGetDirections = (Button) findViewById(R.id.buttonGetDirections);
        btnGetDirections.setOnClickListener(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Thông Báo");
        progressDialog.setMessage("Đang tải Map....");
        progressDialog.show();
    }

    private void getPreviousLocation() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();

            createLocationRequest();
        }
    }

    private void initData() {
        mHandler = new Handler();
        mPlacesService = RetrofitHelper.getInstance().getPlacesService();
    }

    public void retrieveNearbyPlaces() {
        Call<PlacesResult> call = mPlacesService.getNearbyPlaces(mCurrentLocation.getLatitude() + "," + mCurrentLocation.getLongitude(),
                "distance", mSearchType, "AIzaSyBOwSkOFPCw7zf0R-6wGnrcAWNH2oP_RKM");
        call.enqueue(new Callback<PlacesResult>() {
            @Override
            public void onResponse(Call<PlacesResult> call, Response<PlacesResult> response) {
                mResult = response.body();
                mGoogleMap.clear();
                Toast.makeText(MainActivity.this, "Tìm thấy " + mResult.getResults().size() + " địa chỉ gần bạn", Toast.LENGTH_SHORT).show();
                for (int i = 0; i < mResult.getResults().size(); i++) {
                    final GooglePlace place = mResult.getResults().get(i);
                    final Marker marker = mGoogleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(place.getGeometry().getLocation().lat,
                                    place.getGeometry().getLocation().lng))
                            .title(place.getName())
                            .visible(false));
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            final Bitmap bitmap = getBitmapFromURL(place.getIcon());
                            if (bitmap != null) {
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        marker.setIcon(BitmapDescriptorFactory.fromBitmap(bitmap));
                                        marker.setVisible(true);
                                    }
                                });
                            }
                        }
                    }).start();
                    marker.setTag(i);
                    place.updateDistance(mCurrentLocation);
                    Log.e("result", place.getPlaceId() + " at " + place.getVicinity());
                }
            }

            @Override
            public void onFailure(Call<PlacesResult> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Fail due to unknown reason", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                final GooglePlace place = mResult.getResults().get((Integer) marker.getTag());

                Snackbar snackbar = Snackbar.make(findViewById(R.id.activity_main), "", Snackbar.LENGTH_LONG);
// Get the Snackbar's layout view
                Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();
// Hide the text
                TextView textView = (TextView) layout.findViewById(android.support.design.R.id.snackbar_text);
                textView.setVisibility(View.INVISIBLE);

// Inflate our custom view
                View snackView = getLayoutInflater().inflate(R.layout.snackbar_layout, null);
// Configure the view
                TextView txtTitle = (TextView) snackView.findViewById(R.id.textViewTitle);
                txtTitle.setText(place.getName());

                TextView txtAddress = (TextView) snackView.findViewById(R.id.textViewAddress);
                txtAddress.setText(place.getVicinity());

                Button btnDirecton = (Button) snackView.findViewById(R.id.buttonDirection);
                btnDirecton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mPolyline != null) {
                            mPolyline.remove();
                        }
                        if (mMarker != null) {
                            mMarker.remove();
                        }
                        path = "http://maps.googleapis.com/maps/api/directions/json?origin=" + mCurrentLocation.getLatitude() + "," + mCurrentLocation.getLongitude() + "&destination=" + place.getGeometry().getLocation().lat + "," + place.getGeometry().getLocation().lng + "&language=vi";
                        arrayList = new ArrayList<>();
                        getDirections();
                        progressDialog = new ProgressDialog(MainActivity.this);
                        progressDialog.setTitle("Thông Báo");
                        progressDialog.setMessage("Đang thực hiện....");
                        progressDialog.show();
                    }
                });

// Add the view to the Snackbar's layout
                layout.addView(snackView, 0);
// Show the Snackbar
                snackbar.show();

                return true;
            }
        });

        mGoogleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                progressDialog.dismiss();
            }
        });
        processLocationChanges();
    }

    private void processLocationChanges() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mGoogleMap.setMyLocationEnabled(true);

        if (mCurrentLocation != null) {
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()),
                    16));
            retrieveNearbyPlaces();
        }
    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//             TODO: Consider calling
//                ActivityCompat#requestPermissions
//             here to request the missing permissions, and then overriding
//               public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                                                      int[] grantResults)
//             to handle the case where the user grants the permission. See the documentation
//             for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);

        if (mCurrentLocation == null || location.distanceTo(mCurrentLocation) > LOCATION_DIFF) {
            mCurrentLocation = location;
            processLocationChanges();
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private Bitmap getBitmapFromURL(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location.distanceTo(mCurrentLocation) > LOCATION_DIFF) {
            mCurrentLocation = location;
            processLocationChanges();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonDirections:
                arrayList = new ArrayList<>();
                getDirections();
                progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("Thông Báo");
                progressDialog.setMessage("Đang thực hiện....");
                progressDialog.show();
                break;
            case R.id.buttonGetDirections:
                getDetailDirections();
                break;
            default:
                break;
        }
    }

    private void getDetailDirections() {
        Intent intent = new Intent(this, ListDirection.class);
        Bundle bundle = new Bundle();
        MarkerOptions markerOptions;
        bundle.putString("data", ss);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void getDirections() {
        AsyncTask<String, String, String> asyncTask = new AsyncTask<String, String, String>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected String doInBackground(String... params) {
                try {
                    URL url = new URL(params[0]);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);

                    connection.connect();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder builder = new StringBuilder();

                    StringBuffer sb = new StringBuffer();
                    String line = "";
                    while ((line = reader.readLine()) != null) {
                        sb.append(line).append("\n");
                    }
                    reader.close();

                    return sb.toString();

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onProgressUpdate(String... values) {
                super.onProgressUpdate(values);

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressDialog.dismiss();
                relativeLayout.setVisibility(View.VISIBLE);
                ss = s;
                parseJSon(s);
                PolylineOptions lineOption = new PolylineOptions();
                for (int i = 0; i < arrayList.size(); i++) {
                    lineOption.add(arrayList.get(i).getLatLngStart());
                    lineOption.add(arrayList.get(i).getLatLngEnd());
                }

                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(arrayList.get(0).getLatLngStart(), 15));
                MarkerOptions options = new MarkerOptions();
                options.position(arrayList.get(0).getLatLngStart());
                options.title("Bắt đầu");
                options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                mMarker = mGoogleMap.addMarker(options);

                options.position(arrayList.get(arrayList.size() - 1).getLatLngEnd());
                options.title("Điểm đến");

                options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                mMarker=mGoogleMap.addMarker(options);
                lineOption.width(5);
                lineOption.color(Color.BLUE);
                mPolyline = mGoogleMap.addPolyline(lineOption);
            }
        };
        asyncTask.execute(new String[]{path});
    }

    private void parseJSon(String s) {
        try {

            JSONObject jsonObject = new JSONObject(s);
            JSONArray array = jsonObject.getJSONArray("routes").getJSONObject(0).getJSONArray("legs").getJSONObject(0).getJSONArray("steps");
            for (int i = 0; i < array.length(); i++) {

                DirectionStep step = new DirectionStep();
                step.setLatLngStart(new LatLng(array.getJSONObject(i).getJSONObject("start_location").getDouble("lat"),
                        array.getJSONObject(i).getJSONObject("start_location").getDouble("lng")));
                step.setLatLngEnd(new LatLng(array.getJSONObject(i).getJSONObject("end_location").getDouble("lat"),
                        array.getJSONObject(i).getJSONObject("end_location").getDouble("lng")));
                arrayList.add(step);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
