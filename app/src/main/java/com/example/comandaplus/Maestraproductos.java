package com.example.comandaplus;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.comandaplus.Realm.CrudUsuarios;
import com.example.comandaplus.Realm.Crudetallepedido;
import com.example.comandaplus.Realm.Detallepedidorealm;
import com.example.comandaplus.Realm.UsuariosRealm;
import com.example.comandaplus.adapter.Adaptadormaestraproducto;
import com.example.comandaplus.modelo.Productos;
import com.example.comandaplus.modelo.SectionDataModel;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import io.realm.Realm;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Usuario on 10/03/2018.
 */

public class Maestraproductos extends Fragment implements View.OnClickListener, RecyclerView.OnItemTouchListener {
    ArrayList<SectionDataModel> allSampleData;
    TextView totalsoles;
    @BindView(R.id.maestraproductos)
    RecyclerView maestraproductos;
    @BindView(R.id.imageButton)
    ImageButton imageButton;
    Unbinder unbinder;
    private Detallepedidorealm detallepedidorealm;
    private View view;
    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lManager;
    ArrayList<Productos> people = new ArrayList<>();
    private String[] strArrData = {"No Suggestions"};
    private Realm realm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.layoutmaestrasproduc, container, false);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mMessageReceiver, new IntentFilter("custom-message"));

        recycler = (RecyclerView) view.findViewById(R.id.maestraproductos);
        totalsoles = (TextView) view.findViewById(R.id.totalsoles);
        int numberOfColumns = 6;
        recycler.setHasFixedSize(true);
        lManager = new LinearLayoutManager(getActivity());
        recycler.setLayoutManager(lManager);
        realm = Realm.getDefaultInstance();

        new traerproductosporidalmacenidfamilia().execute("1", "3");
        ImageView toto = view.findViewById(R.id.imagendefacebook);

        UsuariosRealm u = CrudUsuarios.getUsuariosRealmByidusuario(0);


        String imgUrl = "https://graph.facebook.com/" + u.getIdfacebook() + "/picture?type=large";
        Log.d("dato", "eeeso" + u.getIdfacebook());
        Picasso.with(getApplicationContext()).load(imgUrl).transform(new CropCircleTransformation()).resize(50, 50)
                .into(toto);


        unbinder = ButterKnife.bind(this, view);
        return view;
    }
    public void onDestroy() {
        Crudetallepedido.deleteAllDetallepedidorealm();

        super.onDestroy();
    }


    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String ItemNamemas = intent.getStringExtra("mas");
            String ItemNamemenos = intent.getStringExtra("menos");

            Double totalsoles2 = Double.parseDouble(totalsoles.getText().toString());

            if (ItemNamemas != null) {
                Double nuevovalormas = Double.valueOf(ItemNamemas);
                totalsoles.setText(String.valueOf(Double.parseDouble(totalsoles.getText().toString()) + nuevovalormas));
            }
            if (ItemNamemenos != null) {
                Double nuevovalormenos = Double.valueOf(ItemNamemenos);
                if (totalsoles2 - nuevovalormenos > 0) {
                    totalsoles.setText(String.valueOf(Double.parseDouble(totalsoles.getText().toString()) - nuevovalormenos));
                } else if (totalsoles2 - nuevovalormenos == 0) {
                    totalsoles.setText("0");
                }
            }
            //configview();
        }
    };

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    @Override
    public void onClick(View v) {

    }


    @Override
    public void onDestroyView() {


        super.onDestroyView();
        unbinder.unbind();
    }



    BottomSheetBehavior bottomSheetBehavior;

    @OnClick(R.id.imageButton)
    public void onClick() {
        String oo="";String l;
        List<Detallepedidorealm> ee=Crudetallepedido.getAllDetallepedidorealm();

for (int w=0;w<ee.size();w++){


    if(ee.get(w).getCantidadrealm()==0){
        oo="aun no hay ningun pedido :)";
    }else {
         l =  "         " + ee.get(w).getCantidadrealm() +"   "+ee.get(w).getNombreproductorealm()+ "\r\n";
        oo = oo + l;
    }
       }

       /* Log.d("shett","si entro al boton imagen "+oo);
        new BottomSheet.Builder(getActivity(), R.style.BottomSheet_Dialog)
                .title("Pedido de la mesa....")
                .icon(R.drawable.burger)
                .sheet(1, "oo")

                .show();
*/
       xxx();

    }

    private void showCustomDialog() {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = view.findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.my_dialog, viewGroup, false);

        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        //finally creating the alert dialog and displaying it
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private class traerproductosporidalmacenidfamilia extends AsyncTask<String, String, String> {

        HttpURLConnection conne;
        URL url = null;
        ArrayList<Productos> listaalmaceno = new ArrayList<Productos>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected String doInBackground(String... params) {

            try {
                url = new URL("https://sodapop.pe/sugest/apitraerproductosporfamiliaalmacen.php");
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return e.toString();
            }
            try {
                conne = (HttpURLConnection) url.openConnection();
                conne.setReadTimeout(15000);
                conne.setConnectTimeout(10000);
                conne.setRequestMethod("POST");
                conne.setDoInput(true);
                conne.setDoOutput(true);

                // Append parameters to URL


                Uri.Builder builder = new Uri.Builder()

                        .appendQueryParameter("idalmacen", params[0])
                        .appendQueryParameter("idfamilia", params[1]);

                String query = builder.build().getEncodedQuery();

                // Open connection for sending data
                OutputStream os = conne.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conne.connect();

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return e1.toString();
            }
            try {
                int response_code = conne.getResponseCode();
                if (response_code == HttpURLConnection.HTTP_OK) {
                    InputStream input = conne.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);

                    }
                    return (

                            result.toString()


                    );

                } else {
                    return ("Connection error");
                }
            } catch (IOException e) {
                e.printStackTrace();

                return e.toString();
            } finally {
                conne.disconnect();
            }
        }


        @Override
        protected void onPostExecute(String result) {

            people.clear();


            ArrayList<String> dataList = new ArrayList<String>();
            Productos meso;
            if (result.equals("no rows")) {
                Toast.makeText(Maestraproductos.this.getActivity(), "no existen datos a mostrar", Toast.LENGTH_LONG).show();

            } else {

                try {


                    JSONArray jArray = new JSONArray(result);


                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject json_data = jArray.optJSONObject(i);

                        meso = new Productos(json_data.getInt("idproducto"), json_data.getString("nombreproducto"), json_data.getString("estadoproducto"), json_data.getString("ingredientes"), json_data.getDouble(("precventa")), json_data.getString("descripcion"));
                        people.add(meso);

                    }
                    strArrData = dataList.toArray(new String[dataList.size()]);


                    adapter = new Adaptadormaestraproducto(people, getActivity().getApplicationContext());
                    recycler.setLayoutManager(new GridLayoutManager(Maestraproductos.this.getActivity(), 1));

                    recycler.setAdapter(adapter);


                } catch (JSONException e) {

                }

            }

        }

    }
private void xxx() {
      Dialog   dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.my_dialog);
        dialog.show();


        TextView tv_message = (TextView) dialog.findViewById(R.id.txtenviar);

        tv_message.setText("enviaaaaaar");

        TextView etalle = (TextView) dialog.findViewById(R.id.txtdetalle);
    etalle.setText("detaleeeee");
    Button bt_yes = (Button) dialog.findViewById(R.id.buttonOk);
        bt_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });

    }
}
