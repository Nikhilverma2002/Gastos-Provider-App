package com.cu.gastosmerchant1.registration;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cu.gastosmerchant1.R;
import com.cu.gastosmerchant1.details.Account_Information;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import pl.droidsonroids.gif.GifImageView;

public class BasicDetails extends AppCompatActivity {

    //TODO PHOTO GALLERY PLUS FILES LEFT

    private static final int REQUEST_LOCATION = 1;
    private GifImageView loader,loader_one;
    private TextView add_text;
    private boolean b=false;
    public ActivityResultLauncher<Intent> resultLauncher;
    private String ownerName, phoneNumber, emailAddress, shopName, shopAddress, shopCategory, location, latitude, longitude;
    private String shopImageUri;
    private Uri imageUri;
    private ImageView shopImage;
    private CardView imageCard;
    private Spinner categoryDropDown, locationDropDown, stateDropDown;
    private ArrayList<String> locations = new ArrayList<>();
    private ArrayList<String> categories = new ArrayList<>();
    private ArrayList<String> states = new ArrayList<>();
    private long creationTimeStamp;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final int CAMERA_REQUEST = 1888;
    Button photoPickerButton;
    LocationManager locationManager;

    Button basicBtn;
    ImageView back;
    EditText OwnerName, PhoneNumber, EmailAddress, ShopName, ShopAddress, shopArea;
    ImageView shopLocationAdd;
    TextView selectCategory;
    TextView selectLocation;
    TextView selectState;

    ArrayList<State> statesArrayList = new ArrayList<>();

    String emailPattern = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$";
    String url_city = "https://raw.githubusercontent.com/zimmy9537/Indian-States-And-Districts/master/states-and-districts.json";
    String city_json;

    String pin;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_details);

        //permissions
//        ActivityCompat.requestPermissions(this,
//                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        loader=findViewById(R.id.loader);
        add_text=findViewById(R.id.add_text);
        basicBtn = findViewById(R.id.basicBtn);
        back = findViewById(R.id.prev);
        imageCard = findViewById(R.id.cardView);
        photoPickerButton = findViewById(R.id.photoPickerbutton);
        shopArea = findViewById(R.id.area);
        OwnerName = findViewById(R.id.ownername);
        PhoneNumber = findViewById(R.id.phnumber);
        ShopAddress = findViewById(R.id.shopaddresss);
        ShopName = findViewById(R.id.shopname);
        EmailAddress = findViewById(R.id.emailadress);
        selectCategory = findViewById(R.id.selectcat);
        selectLocation = findViewById(R.id.selectcity);
        selectState = findViewById(R.id.selectstate);
        shopImage = findViewById(R.id.shopImage);
        shopLocationAdd = findViewById(R.id.imageView3);
        categoryDropDown = findViewById(R.id.category_spinner);
        locationDropDown = findViewById(R.id.city_spinner);
        locationDropDown.setEnabled(false);
        stateDropDown = findViewById(R.id.state_spinner);
        fillCategories(categories);
        longitude = "";
        latitude = "";
        mAuth = FirebaseAuth.getInstance();

        getCityData();

        String number = mAuth.getCurrentUser().getPhoneNumber();

        PhoneNumber.setText(number.substring(3));


        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(BasicDetails.this, R.layout.support_simple_spinner_dropdown_item, categories) {
            @Override
            public int getCount() {
                return super.getCount();
            }
        };

        categoryAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        categoryDropDown.setAdapter(categoryAdapter);
        categoryDropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectCategory.setText(adapterView.getItemAtPosition(i).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //do nothing
            }
        });


        pin = getIntent().getStringExtra("pin");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //firebase init
//        firebaseAuth=FirebaseAuth.getInstance();
//        firebaseDatabase=FirebaseDatabase.getInstance();
//        myRef =firebaseDatabase.getReference();

        shopLocationAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
              
                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
                {
                    ActivityCompat.requestPermissions(BasicDetails.this,
                      new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
                    Log.d("TAG", "onClick: if wala");
                    loader.setVisibility(View.VISIBLE);
                    shopLocationAdd.setVisibility(View.GONE);
                    add_text.setVisibility(View.GONE);
                    OnGps();
                } else {

                   if(b==false)
                   {
                       ActivityCompat.requestPermissions(BasicDetails.this,
                               new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
                       Log.d("TAG", "onClick: else wala");
                       loader.setVisibility(View.VISIBLE);
                       shopLocationAdd.setVisibility(View.GONE);
                       add_text.setVisibility(View.GONE);
                       getLocation();
                   }
                   else
                   {

                   }

                }
            }
        });

        resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    if (result.getData() != null) {
                        imageUri = result.getData().getData();
                        shopImage.setImageURI(imageUri);
                        shopImageUri = imageUri.toString();
                        findViewById(R.id.shopcover).setVisibility(View.INVISIBLE);
                    }
                }
            }
        });

        photoPickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(BasicDetails.this);
                dialog.setContentView(R.layout.dialog_picture_capture);
                Button camera = (Button) dialog.findViewById(R.id.camera);
                Button storage = (Button) dialog.findViewById(R.id.storage);
                storage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        resultLauncher.launch(intent);
                        dialog.dismiss();
                    }
                });

                camera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dexterCalling(BasicDetails.this);
                        dialog.dismiss();


//                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                        if (ContextCompat.checkSelfPermission(BasicDetails.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
//                            ImagePicker.with(BasicDetails.this)
//                                    .cameraOnly()
//                                    .saveDir(getExternalFilesDir(Environment.DIRECTORY_DCIM))
//                                    .start(CAMERA_REQUEST);
//
//                        } else {
//                            ActivityCompat.requestPermissions(
//                                    BasicDetails.this,
//                                    new String[]{Manifest.permission.CAMERA},
//                                    PERMISSION_REQUEST_CODE
//                            );
//                        }
                        dialog.dismiss();
                    }
                });

                Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        basicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                emailAddress = EmailAddress.getText().toString().trim();

                if (emailAddress.isEmpty()) {
                    Toast.makeText(BasicDetails.this, "email address can not be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!emailAddress.matches(emailPattern)) {
                    Toast.makeText(BasicDetails.this, "Enter valid email id", Toast.LENGTH_SHORT).show();
                    return;
                }

                shopName = ShopName.getText().toString().trim();
                shopAddress = ShopAddress.getText().toString().trim();
                location = selectLocation.getText().toString();
                shopCategory = selectCategory.getText().toString();

                if (shopCategory.equals("Select Category")) {
                    Toast.makeText(BasicDetails.this, "Please select correct Category", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (viewsEmpty()) {
                    Toast.makeText(BasicDetails.this, "Views are empty", Toast.LENGTH_SHORT).show();
                    //todo give proper name
                    return;
                }


                Intent i = new Intent(BasicDetails.this, AddQR.class);

                Date date = new Date();
                creationTimeStamp = date.getTime();

                if (selectState.getText().toString().equals("Select Shop State")) {
                    Toast.makeText(BasicDetails.this, "Select Shop State", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (location.equals("Select Shop City")) {
                    Toast.makeText(BasicDetails.this, "Select Shop City", Toast.LENGTH_SHORT).show();
                    return;
                }

                i.putExtra("accountInformation", shareAccountInformation());
                i.putExtra("shopName", shopName);
                i.putExtra("shopAddress", shopAddress);
                i.putExtra("shopCategory", shopCategory);
                i.putExtra("latitude", latitude);
                i.putExtra("longitude", longitude);
                i.putExtra("location", location);
                i.putExtra("shopImageUri", shopImageUri);
                i.putExtra("creationTimeStamp", creationTimeStamp);
                i.putExtra("state", selectState.getText().toString());
                i.putExtra("area", shopArea.getText().toString());
                startActivity(i);
            }
        });
    }

    private void OnGps() {
        loader.setVisibility(View.GONE);
        shopLocationAdd.setVisibility(View.VISIBLE);
        add_text.setVisibility(View.VISIBLE);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));

            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void getLocation()

    {

        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
            loader.setVisibility(View.GONE);
            shopLocationAdd.setVisibility(View.VISIBLE);
            add_text.setVisibility(View.VISIBLE);
            shopLocationAdd.setImageResource(R.drawable.ic_group_5471_add_tick);
            findViewById(R.id.add_text).setVisibility(View.INVISIBLE);
            b=true;
        } else {

            LocationRequest locationRequest = new LocationRequest();
            locationRequest.setInterval(10000);
            locationRequest.setFastestInterval(3000);
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            LocationServices.getFusedLocationProviderClient(this)
                    .requestLocationUpdates(locationRequest, new LocationCallback() {
                        @Override
                        public void onLocationResult(@NonNull LocationResult locationResult) {
                            super.onLocationResult(locationResult);
                            LocationServices.getFusedLocationProviderClient(BasicDetails.this)
                                    .removeLocationUpdates(this);
                            if (locationResult != null && locationResult.getLocations().size() > 0) {
                                int latestLocationIndex = locationResult.getLocations().size() - 1;
                                latitude = String.valueOf(locationResult.getLocations().get(latestLocationIndex).getLatitude());
                                longitude = String.valueOf(locationResult.getLocations().get(latestLocationIndex).getLongitude());
                                Log.v(BasicDetails.class.getSimpleName(), "latitude:- " + latitude + ", longitude:- " + longitude);
                                loader.setVisibility(View.GONE);
                                shopLocationAdd.setVisibility(View.VISIBLE);
                                add_text.setVisibility(View.VISIBLE);
                                shopLocationAdd.setImageResource(R.drawable.ic_group_5471_add_tick);
                                findViewById(R.id.add_text).setVisibility(View.INVISIBLE);
                                b=true;
                            }
                        }
                    }, Looper.getMainLooper());
        }
    }

    private Account_Information shareAccountInformation() {
        ownerName = OwnerName.getText().toString().trim();
        emailAddress = EmailAddress.getText().toString().trim();
        //todo need to work on phone number size
        phoneNumber = PhoneNumber.getText().toString().trim();
        Account_Information account_information = new Account_Information(emailAddress, ownerName, phoneNumber, pin);
        return account_information;
    }

    boolean viewsEmpty() {
        if (OwnerName.getText().toString().isEmpty() ||
                PhoneNumber.getText().toString().isEmpty() ||
                ShopAddress.getText().toString().isEmpty() ||
                ShopName.getText().toString().isEmpty() ||
                shopArea.getText().toString().isEmpty() ||
                EmailAddress.getText().toString().isEmpty() ||
                shopImage.getDrawable() == null ||
                (longitude.equals("") && latitude.equals(""))) {
            return true;
        }
        return false;
    }

    private ArrayList<String> fillCategories(ArrayList<String> arr) {
        arr.add("Select Category");
        arr.add("Food & Beverages");
        arr.add("Fashion");
        arr.add("Salon & spa");
        arr.add("Stores");
        arr.add("Medical");
        arr.add("others");
        return arr;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            imageUri = data.getData();
            shopImage.setImageURI(imageUri);
            shopImageUri = imageUri.toString();
            findViewById(R.id.shopcover).setVisibility(View.INVISIBLE);
        }
    }

    void getCityData() {
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url_city, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                city_json = response;

                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                GetStates getStates = gson.fromJson(city_json, GetStates.class);

                statesArrayList = new ArrayList<>();
                statesArrayList = getStates.getStates();

                State state0 = new State();
                ArrayList<String> state0arrayList=new ArrayList<>();
                state0arrayList.add("Select Shop City");
                state0.setState("Select Shop State");
                state0.setDistricts(state0arrayList);

                statesArrayList.add(0, state0);

                states = new ArrayList<>();
                for (State state : statesArrayList) {
                    states.add(state.getState());
                    Log.v("state", state.getState());
                }

                //state
                ArrayAdapter<String> stateAdapter = new ArrayAdapter<String>(BasicDetails.this, R.layout.support_simple_spinner_dropdown_item, states);
                stateDropDown.setAdapter(stateAdapter);
                stateDropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        selectState.setText(adapterView.getItemAtPosition(i).toString());
                        locationDropDown.setEnabled(true);
                        String stateString = adapterView.getItemAtPosition(i).toString();
                        for (State state : statesArrayList) {
                            if (state.getState().equals(stateString)) {
                                locations = new ArrayList<>();
                                locations = state.getDistricts();
                                locations.add(0, "Select Shop City");
                            }
                        }

                        ArrayAdapter<String> locationAdapter = new ArrayAdapter<String>(BasicDetails.this, R.layout.support_simple_spinner_dropdown_item, locations);
                        locationAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                        locationDropDown.setAdapter(locationAdapter);
                        locationDropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                selectLocation.setText(adapterView.getItemAtPosition(i).toString());
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });


                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        //do nothing
                    }
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("errorResponse", error.getMessage());
            }
        });

        queue.add(stringRequest);

    }
    void dexterCalling(Activity activity)//for granting RunTime permission
    {
        //    Log.d("chekingTemp", "dexterCalling: "+temp);
        Dexter.withActivity(this)

                .withPermissions(Manifest.permission.CAMERA,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.READ_EXTERNAL_STORAGE)

                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {

                        ImagePicker.with(BasicDetails.this)
                                .cameraOnly()
                                .saveDir(getExternalFilesDir(Environment.DIRECTORY_DCIM))
                                .start(CAMERA_REQUEST);
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {

                        permissionToken.continuePermissionRequest();
                    }
                }).withErrorListener(new PermissionRequestErrorListener() {

            @Override
            public void onError(DexterError error) {

                Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show();
            }
        })

                .onSameThread().check();
    }
}



