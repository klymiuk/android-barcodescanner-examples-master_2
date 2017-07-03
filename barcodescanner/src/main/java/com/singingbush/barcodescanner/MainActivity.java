package com.singingbush.barcodescanner;

import android.app.Activity;
import android.content.Intent;
import android.app.Fragment;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.zxing.integration.android.IntentIntegrator;

import net.sourceforge.zbar.Symbol;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();
    public static String LOG_TAG = "my_log";
    TextView txtVidpavnik, txtDocument, txtPereviznik, txtAuto, txtPricep, txtVodiy, txtResult2, txtZavant, txtRozvant, product, beer, sort, diameter, length, volume, quantity, diameterGroup, dischargeHeights, width, height, coefficient;
    String testsort;
    String txtProduct, txtBeer, txtSort, txtDiameter, txtLength, txtVolume, txtQuantity, txtDiameterGroup, txtDischargeHeights, txtWidth, txtHeight, txtCoefficient;

    ArrayAdapter<String> adapterGrid;
    GridView gvMain;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.v(TAG, "starting " + TAG);

        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void adjustGridView() {
        gvMain.setNumColumns(12);
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    private class ParseTask extends AsyncTask<String, Void, String> {

        // читаємо номер бирки з текстЕдіт
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";
        byte[] data = null;

        //        String parammetrs = "number=165581&serie=14";
        @Override
        protected String doInBackground(String... params) {


            // получаем данные с внешнего ресурса
            String n = params[0];

            // строк
            // а запроса для елок
            //String parammetrs = "number="+ n +"&serie=14";
            String parammetrs = "q=" + n + "&t=0";
            try {
                URL url = new URL("http://www.ukrforest.com/mod/check/_check.php");

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
                urlConnection.setRequestProperty("Content-Length", "" + Integer.toString(parammetrs.getBytes().length));


                OutputStream os = urlConnection.getOutputStream();
                data = parammetrs.getBytes("UTF-8");
                os.write(data);
                data = null;


                urlConnection.connect();


                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                resultJson = buffer.toString();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return resultJson;
        }


        @Override
        protected void onPostExecute(String strJson) {
            super.onPostExecute(strJson);

            ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
            progressBar.setVisibility(progressBar.INVISIBLE);
            // выводим целиком полученную json-строку


//            Log.d(LOG_TAG, strJson);

            //ListView



            //parsing JSON Responce
            try {
                JSONObject obj = new JSONObject(strJson);
                JSONArray x = (JSONArray) obj.get("x");
                JSONObject t = (JSONObject) obj.get("t");
                String t2 =  t.getString("t2");
                String t1 = t.getString("t1");
                String[] parseT1 = t1.split("\n");
                String[] parseT2 = t2.split("\n");
                String result = parseT2[3];
                String navant = parseT2[0];
                String rozvant = parseT2[1];
                String vidpavnik = parseT1[0];
                String document = parseT1[1];
                String auto = parseT1[3];
                String pricep = parseT1[4];
                String vodiy = parseT1[5];

                String[] parseResult = result.split(" ");

                String poroda = "";
                String L = "";
                String V = "";
                String prod = "";
                String D = "";
                String sort = "";
                for (int j = 0; j<parseResult.length;j++) {

                    Log.d(LOG_TAG, parseResult[j]);

                        Log.d(LOG_TAG, Integer.toString(j));

                }


                txtVidpavnik = (TextView) findViewById(R.id.txtVidpravnik);
                //txtVidpavnik.setText(vidpavnik);

                txtDocument = (TextView) findViewById(R.id.txtDocument);
                //txtDocument.setText(document);

                txtAuto = (TextView) findViewById(R.id.txtAuto);
                //txtAuto.setText(auto);

                txtPricep = (TextView) findViewById(R.id.txtPricep);
                //txtPricep.setText(pricep);

                txtVodiy =(TextView) findViewById(R.id.txtVodiy);
                //txtVodiy.setText(vodiy);



                txtResult2 = (TextView) findViewById(R.id.txtResult2);

                txtZavant = (TextView) findViewById(R.id.txtZavant);
                  //  txtZavant.setText(navant);

                txtRozvant = (TextView) findViewById(R.id.txtRozvant);
                  //  txtRozvant.setText(rozvant);


                for (int y = 0; y < parseResult.length; y++) {




                       if (parseResult[y].equals("Сорт")) {

                           poroda = parseResult[y-2];

                                for (int p = 0; p< (y-2); p++ ) {

                                    prod = prod + parseResult[p] + " " ;

                                    sort = parseResult[y+1];
                                }
                         }

                       if (parseResult[y].equals("D")) {


                           D = parseResult[y + 1];                       }

                       if (parseResult[y].equals("L")) {

                           L = parseResult[y + 1];

                       }

                        if (parseResult[y].equals("V")) {

                            V = parseResult[y + 1];

                        }



                       txtResult2.setText("Продукція = " + prod+ " Порода = " + poroda + " Сорт = " + sort + " D = " + D + " см." + " L =" + L + "  м." + " V = " + V + " куб.м");

                }


                //txtResult2.setText("Довжина =" + parseResult[11] + parseResult[12]+ " " + "Об'єм=" + parseResult[14] + parseResult[15]+ "Test " + sort2);
                //String[] parset2 = t2.toString();
                show(x);



                String txtSubordination, txtPartner, txtRegion, txtBreed, txtHeight;
                if (obj.length() > 6) {

                    txtSubordination = obj.getString("subordination");
                    txtPartner = obj.getString("partner");
                    txtRegion = obj.getString("region");
                    txtBreed = obj.getString("breed");

                } else {












                    txtSubordination = "Error";
                    txtPartner = "Error";
                    txtRegion = "Error";
                    txtBreed = "Error";

                }





            } catch (JSONException e) {


            }


            JSONObject dataJsonObj = null;
            String secondName = "";

        }
    }

    //function for showing result
    public void show(JSONArray arrstr) {
        ArrayList<JSONArray> str = new ArrayList<JSONArray>();
        ArrayList<String> grd = new ArrayList<String>();
        JSONArray tmp;
        try {
            for (int i = 0; i < arrstr.length(); i++) {
                tmp = arrstr.getJSONArray(i);
                for (int j = 0; j < tmp.length(); j++) {


                    grd.add(tmp.getString(j));
                }
            }


            adapterGrid = new ArrayAdapter<String>(this, R.layout.item, R.id.tvText, grd);
            gvMain = (GridView) findViewById(R.id.gvMain);
            gvMain.setAdapter(adapterGrid);
            adjustGridView();


        } catch (Exception e) {

        }

    }
//    @Override // todo
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {


            case ZBarScannerActivity.REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    showResultAsToast(intent, "Zbar");
                }
                break;

        }
    }

    private void showResultAsToast(final Intent result, final String provider) {
        String barcode = result.getStringExtra("SCAN_RESULT");
        String type = result.getStringExtra("SCAN_RESULT_FORMAT");
        Log.d(TAG, String.format("Barcode: '%s' type: %s from %s", barcode, type, provider));
        Toast.makeText(this, String.format("Бирка номер : %s ", barcode), Toast.LENGTH_SHORT).show();


        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(progressBar.VISIBLE);


        new ParseTask().execute(barcode);

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment implements View.OnClickListener {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);


            Button scanZbarButton = (Button) rootView.findViewById(R.id.scan_button_zbar);
            scanZbarButton.setOnClickListener(this);


            return rootView;
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {

                case R.id.scan_button_zbar:
                    scanWithZBar();
                    break;


            }
        }

        private void scanWithZBar() {
            Intent zbarIntent = new Intent(getActivity(), ZBarScannerActivity.class);
            zbarIntent.putExtra(ZBarScannerActivity.SCAN_MODES, new int[]{Symbol.QRCODE, Symbol.EAN13, Symbol.UPCE, Symbol.UPCA, Symbol.CODE128});
            getActivity().startActivityForResult(zbarIntent, ZBarScannerActivity.REQUEST_CODE);
        }

        // scan with Zebra Crossing
        private void startZXing() {
            IntentIntegrator integrator = new IntentIntegrator(getActivity());
            // possible barcode types are:
            // "UPC_A", "UPC_E", "EAN_8", "EAN_13", "CODE_39",
            // "CODE_93", "CODE_128", "ITF", "RSS_14", "RSS_EXPANDED"
            Collection<String> BARCODE_TYPES = Collections.unmodifiableCollection(Arrays.asList("UPC_A", "UPC_E", "EAN_8", "EAN_13"));
            integrator.initiateScan(BARCODE_TYPES);
        }

    }

}
