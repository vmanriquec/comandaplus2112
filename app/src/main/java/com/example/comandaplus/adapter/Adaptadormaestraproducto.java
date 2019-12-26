package com.example.comandaplus.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.cocosw.bottomsheet.BottomSheet;
import com.example.comandaplus.R;
import com.example.comandaplus.Realm.Crudetallepedido;
import com.example.comandaplus.Realm.Detallepedidorealm;
import com.example.comandaplus.modelo.Detallepedido;
import com.example.comandaplus.modelo.Productos;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;
import jp.wasabeef.picasso.transformations.CropSquareTransformation;


public class Adaptadormaestraproducto extends RecyclerView.Adapter<Adaptadormaestraproducto.AdaptadorViewHolder> {
    @BindView(R.id.plus_button1)
    Button plusButton1;
    @BindView(R.id.menos_button1)
    Button menosButton1;
    @BindView(R.id.cremas)
    Button cremas;
    //Realm realm = Realm.getDefaultInstance();
    private Context mainContext;
    String foto;
    SharedPreferences prefs;
    String FileName = "myfile";
    private List<Productos> items;
    ArrayList<Detallepedido> detallepedido = new ArrayList<>();
    Detallepedido objdetallepedido;

    private Detallepedidorealm detallepedidorealm;
    public Adaptadormaestraproducto(List<Productos> items, Context contexto) {
        this.mainContext = contexto;
        this.items = items;
        prefs = contexto.getApplicationContext().getSharedPreferences(FileName, Context.MODE_PRIVATE);
        String idalmacenactiv = prefs.getString("idalmacenactivo", "");
}


    static class AdaptadorViewHolder extends RecyclerView.ViewHolder {
        protected TextView productonombre;
        protected TextView idproducto;
        protected TextView productoprecio;
        protected TextView productoingredientes, stockp;
        protected ImageView productoimagen;
        protected TextView cantidadpedida;
        protected TextView cantidadtarjeta;
        protected CheckBox michek;
        protected LinearLayout masmenos;
        protected Button mas,meno, botonok,cremas;
        protected Button menos;

        public AdaptadorViewHolder(View v) {
            super(v);
            this.productonombre = (TextView) v.findViewById(R.id.productonombrep);
            this.productoprecio = (TextView) v.findViewById(R.id.productopreciop);
            this.idproducto = (TextView) v.findViewById(R.id.idproductop);
            this.cantidadpedida = (TextView) v.findViewById(R.id.cantidadpedida);
            this.productoingredientes = (TextView) v.findViewById(R.id.productoingredientesp);
            this.productoimagen = (ImageView) v.findViewById(R.id.productoimagenp);
            this.stockp = (TextView) v.findViewById(R.id.stockp);
            this.mas = (Button) v.findViewById(R.id.menos_button1);
            this.meno = (Button) v.findViewById(R.id.plus_button1);
            this.cremas=(Button)v.findViewById(R.id.cremas);
this.cantidadtarjeta=(TextView) v.findViewById(R.id.cantidadtarjeta);

        }
    }

    @Override
    public AdaptadorViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.tarjetaproducto, viewGroup, false);
        return new AdaptadorViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final AdaptadorViewHolder viewHolder, final int position) {
        final Productos item = items.get(position);
        viewHolder.itemView.setTag(item);

        viewHolder.productonombre.setText(item.getNombreproducto());
        viewHolder.productoingredientes.setText(item.getIngredientes());
        viewHolder.productoprecio.setText("S/. " + String.valueOf(item.getPrecventa()));
        viewHolder.idproducto.setText(String.valueOf(item.getIdproducto()));

        // aqui traigo el stock  viewHolder.stockp.setText("Stock: "+ String.valueOf(item.getEstadoproducto()));

        //viewHolder.michek.setVisibility(View.GONE);
        //viewHolder.cantidadpedida.setVisibility(View.GONE);

        /*asignar imagen desde url*/
        foto = item.getDescripcion().toString();

        Picasso.with(mainContext.getApplicationContext()).load(foto).transform(new CropSquareTransformation()).resize(200, 200)
                .into(viewHolder.productoimagen);

        viewHolder.productoimagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Toast ImageToast = new Toast(mainContext.getApplicationContext());
                LinearLayout toastLayout = new LinearLayout(mainContext.getApplicationContext());
                toastLayout.setOrientation(LinearLayout.VERTICAL);

                ImageView image = new ImageView(mainContext.getApplicationContext());
                TextView text = new TextView(mainContext.getApplicationContext());
                foto = item.getDescripcion().toString();

                Picasso.with(mainContext.getApplicationContext()).load(foto).transform(new CropSquareTransformation())
                        .resize(350, 350)
                        .into(image);
                text.setText(item.getIngredientes());
                text.setTextColor(Color.RED);
                text.setBackgroundColor(Color.WHITE);
                text.setGravity(12);
                toastLayout.addView(image);
                toastLayout.addView(text);
                ImageToast.setView(toastLayout);
                ImageToast.setGravity(Gravity.TOP | Gravity.LEFT, 140, 140);
                ImageToast.setDuration(Toast.LENGTH_LONG);
                ImageToast.show();


                ImageToast.getView().setPadding(20, 100, 20, 20);


            }
        });
        /*si esta check activo para aumentar cantidad*/
        prefs = mainContext.getApplicationContext().getSharedPreferences(FileName, Context.MODE_PRIVATE);
        String idalmacenactiv = prefs.getString("idalmacenactivo", "");
        int i = Integer.parseInt("1");



viewHolder.cremas.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {


        new AlertDialog.Builder(v.getContext())
                .setTitle("Nuke planet Jupiter?")
                .setMessage("Note that nuking planet Jupiter will destroy everything in there.")
                .setPositiveButton("Nuke", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("MainActivity", "Sending atomic bombs to Jupiter");
                    }
                })
                .setNegativeButton("Abort", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("MainActivity", "Aborting mission...");
                    }
                })
                .show();

        xxx();
    }
});

        viewHolder.meno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Animation myAnim = AnimationUtils.loadAnimation(mainContext, R.anim.milshake);
                final Animation creciente = AnimationUtils.loadAnimation(mainContext, R.anim.degrandeachico);
viewHolder.meno.setAnimation(myAnim);
viewHolder.cantidadpedida.setAnimation(creciente);
                //MediaPlayer mp = MediaPlayer.create(mainContext, R.raw.mariomas);
                //mp.start();
viewHolder.meno.startAnimation(myAnim);
viewHolder.cantidadpedida.startAnimation(creciente);

                int c= Integer.parseInt(viewHolder.cantidadpedida.getText().toString());
                c=c+1;

                viewHolder.cantidadpedida.setText(Integer.toString(c));
                double cc= Double.parseDouble(viewHolder.cantidadtarjeta.getText().toString());
                viewHolder.cantidadtarjeta.setText(Double.toString(cc+item.getPrecventa()));
                int idp=Integer.parseInt(viewHolder.idproducto.getText().toString());

                Intent intent = new Intent("custom-message");
                //            intent.putExtra("quantity",Integer.parseInt(quantity.getText().toString()));
                intent.putExtra("mas",Double.toString(item.getPrecventa()));

                LocalBroadcastManager.getInstance(mainContext).sendBroadcast(intent);

adicionarnuevoproductoadetallerealm( 1,"sss",viewHolder.productonombre.getText().toString(),Double.parseDouble(item.getPrecventa().toString()),Integer.parseInt(viewHolder.idproducto.getText().toString()), Integer.parseInt(viewHolder.cantidadpedida.getText().toString()),2);
Crudetallepedido.getAllDetallepedidorealm();
                // if (verificarsiexiste(idp)){
                    //   Toast.makeText(getApplicationContext(),idp,Toast.LENGTH_SHORT).show();
                   // realmgrbarenbasedatosa(idp,t,cantped,pr,idp,foto);


                //}else{
                  //  realmgrbarenbasedatosdetalle( idp, idp,c,item.getPrecventa(),item.getNombreproducto(),1);

                //}
            }
        });



        viewHolder.mas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Animation myAnim = AnimationUtils.loadAnimation(mainContext, R.anim.milshake);
                final Animation decreciente = AnimationUtils.loadAnimation(mainContext, R.anim.dechicoagrande);
                viewHolder.mas.setAnimation(myAnim);
                viewHolder.cantidadpedida.setAnimation(decreciente);
                //MediaPlayer mp = MediaPlayer.create(mainContext, R.raw.mariomenos);
                //mp.start();
                viewHolder.mas.startAnimation(myAnim);



                int d= Integer.parseInt(viewHolder.cantidadpedida.getText().toString());
                if (d==0) {


                }else{
                    d=d-1;

                    viewHolder.cantidadpedida.setText(Integer.toString(d));

                    double cc= Double.parseDouble(viewHolder.cantidadtarjeta.getText().toString());
                   // TextView f= (TextView) mainContext.getApplicationContext. (R.id.totalsoles);
                    //f.setText(viewHolder.cantidadtarjeta.getText());

                    viewHolder.cantidadtarjeta.setText(Double.toString(cc-item.getPrecventa()));

                    Intent intent = new Intent("custom-message");
                    //            intent.putExtra("quantity",Integer.parseInt(quantity.getText().toString()));
                    intent.putExtra("menos",Double.toString(item.getPrecventa()));
                    LocalBroadcastManager.getInstance(mainContext).sendBroadcast(intent);
     }
            disminuirproductoadetallerealm(1,"sss",viewHolder.productonombre.getText().toString(),Double.parseDouble(item.getPrecventa().toString()),Integer.parseInt(viewHolder.idproducto.getText().toString()), Integer.parseInt(viewHolder.cantidadpedida.getText().toString()),2);
            }
        });

    }


    private void adicionarnuevoproductoadetallerealm(Integer almacen,String rutainagen,String nombreproducto,Double preciovtaproducto,Integer idproducto,Integer cantidad,Integer idpedido) {
        detallepedidorealm=new Detallepedidorealm();
        detallepedidorealm.setIdalmacenrealm(almacen);
        detallepedidorealm.setImagenrealm(rutainagen);
        detallepedidorealm.setNombreproductorealm(nombreproducto);
        detallepedidorealm.setPrecventarealm(preciovtaproducto);
        detallepedidorealm.setIdproductorealm(idproducto);
        detallepedidorealm.setCantidadrealm(cantidad);
        detallepedidorealm.setIdpedido(idpedido);
        detallepedidorealm.setSubtotal(preciovtaproducto+cantidad);
        Crudetallepedido.addDetallepedidorealm(detallepedidorealm);
    }


    private void disminuirproductoadetallerealm(Integer almacen,String rutainagen,String nombreproducto,Double preciovtaproducto,Integer idproducto,Integer cantidad,Integer idpedido) {
        detallepedidorealm=new Detallepedidorealm();
        detallepedidorealm.setIdalmacenrealm(almacen);
        detallepedidorealm.setImagenrealm(rutainagen);
        detallepedidorealm.setNombreproductorealm(nombreproducto);
        detallepedidorealm.setPrecventarealm(preciovtaproducto);
        detallepedidorealm.setIdproductorealm(idproducto);
        detallepedidorealm.setCantidadrealm(cantidad);
        detallepedidorealm.setIdpedido(idpedido);
        detallepedidorealm.setSubtotal(preciovtaproducto+cantidad);
        Crudetallepedido.Disminuirdetalle(detallepedidorealm);
    }

    private void xxx() {


    }


    @Override
    public int getItemCount() {
        return items.size();
    }
}

