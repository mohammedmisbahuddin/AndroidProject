package tk.a4thcoordinate.gprsdemo;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.test.mock.MockPackageManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.IntentIntegrator;
import com.google.zxing.integration.IntentResult;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView latitudeView,longitudeView;
    private static final int REQUEST_CODE_PERMISSION = 2;
    String mPermission = Manifest.permission.ACCESS_FINE_LOCATION;
    FloatingActionButton scanBtn;
    EditText passId;
    Button submitBtn;
    private Integer passIdContent;
    // GPSTracker class
    GPSTracker gps;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            if (ActivityCompat.checkSelfPermission(this, mPermission)
                    != MockPackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{mPermission},
                        REQUEST_CODE_PERMISSION);

                // If any permission above not allowed by user, this condition will
                //execute every time, else your else part will work
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        gps = new GPSTracker(MainActivity.this);

        // check if GPS enabled
        if(gps.canGetLocation()){

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            latitudeView = (TextView) findViewById(R.id.latitudeView);
            latitudeView.setText(latitudeView.getText()+" "+latitude+" ");
            longitudeView = (TextView) findViewById(R.id.longitudeView);
            longitudeView.setText(longitudeView.getText()+" "+longitude+" ");

            // \n is for new line

        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }

        //scanning <code></code>

        scanBtn = (FloatingActionButton) findViewById(R.id.scanBtn);
        scanBtn.setOnClickListener(this);

        //custom pass id entry
        passId = (EditText) findViewById(R.id.passIdTxt);
        submitBtn = (Button) findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.scanBtn){
            IntentIntegrator scanIntegrator = new IntentIntegrator(this);
            scanIntegrator.initiateScan();
        }
        if (view.getId()==R.id.submitBtn){
            if (passId.getText().toString().matches("")){
                Toast toast = Toast.makeText(getApplicationContext(),
                        "No scan data received!", Toast.LENGTH_SHORT);
                toast.show();
            }
            else    {
                passIdContent = Integer.parseInt(passId.getText().toString());
                Toast toast = Toast.makeText(getApplicationContext(),
                        "CONTENT : "+passIdContent, Toast.LENGTH_SHORT);
                toast.show();
            }

        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult == null) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();

        }
        else{
            String scanContent = scanningResult.getContents();
            //String scanFormat = scanningResult.getFormatName();
            //passIdContent = Integer.parseInt(scanContent);
            passId.setText(scanContent);
            Toast toast = Toast.makeText(getApplicationContext(),
                    "CONTENT : "+scanContent, Toast.LENGTH_SHORT);
            toast.show();

        }
    }
}