package com.mobilerp.pathwaysstudio.mobilerp;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.BeepManager;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.camera.CameraSettings;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class BarcodeScanner extends AppCompatActivity {

    final Context context = this;
    DecoratedBarcodeView barcodeView;
    BeepManager beepManager;
    String lastText;
    CameraSettings settings;
    APIServer apiServer;
    TextView tvBarcode, tvBarcodeValue, tvPrice, tvTotal;
    EditText etName, etPrice, etTotal;
    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            if (result.getText() == null || result.getText().equals(lastText)) {
                return;
            }

            lastText = result.getText();
            barcodeView.setStatusText(lastText);
            beepManager.playBeepSoundAndVibrate();
            findLastScannedProduct(result.getText());
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scanner);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Camera settings
        settings = new CameraSettings();
        settings.setFocusMode(CameraSettings.FocusMode.MACRO);

        //Barcode settings
        barcodeView = (DecoratedBarcodeView) findViewById(R.id.barcodePreview);
        barcodeView.getBarcodeView().setCameraSettings(settings);
        barcodeView.decodeContinuous(callback);

        beepManager = new BeepManager(this);

        //Server init
        apiServer = new APIServer(context);

        // Init elements to display items
        tvBarcode = (TextView) findViewById(R.id.tvBarcode);
        tvBarcodeValue = (TextView) findViewById(R.id.tvBarcodeValue);
        tvPrice = (TextView) findViewById(R.id.tvPrice);
        tvTotal = (TextView) findViewById(R.id.tvTotal);

        etName = (EditText) findViewById(R.id.etName);
        etPrice = (EditText) findViewById(R.id.etPrice);
        etTotal = (EditText) findViewById(R.id.etTotal);

        tvBarcode.setText(R.string.item_barcode);
        tvPrice.setText(R.string.item_price);
        tvTotal.setText(R.string.item_total);

        etName.setEnabled(false);
        etPrice.setEnabled(false);
        etTotal.setEnabled(false);

    }

    private void findLastScannedProduct(final String barcode) {
        tvBarcodeValue.setText(barcode);
        apiServer.getResponse(Request.Method.GET, APIServer.BASE_URL + APIServer.FIND_PRODUCT + barcode, null, new VolleyCallback() {
            @Override
            public void onSuccessResponse(JSONObject result) {
                try {
                    JSONArray _itms = result.getJSONArray("mobilerp");
                    JSONObject _itm = _itms.getJSONObject(0);
                    Toast.makeText(context, _itm.getString("name"), Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    Toast.makeText(context, R.string._404_not_found, Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse response = error.networkResponse;
                if (response.statusCode == 404) {
                    Toast.makeText(context, R.string._404_not_found, Toast.LENGTH_LONG).show();
                    etName.setText(R.string.new_item);
                    etName.setEnabled(true);
                    etPrice.setEnabled(true);
                    etTotal.setEnabled(true);
                } else {
                    apiServer.genericErrors(response.statusCode);
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        barcodeView.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        barcodeView.pause();
    }

    public void pause(View view) {
        barcodeView.pause();
    }

    public void resume(View view) {
        barcodeView.resume();
    }

    public void triggerScan(View view) {
        barcodeView.decodeSingle(callback);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }


}