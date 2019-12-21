package com.example.comandaplus.Realm;

import android.util.Log;

import com.example.comandaplus.modelo.Pedido;

import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class Crudpedido {




    private final static int calculateIndex(){
        Realm realm = Realm.getDefaultInstance();
        Number currentIdNum = realm.where(PedidoRealm.class).max("idpedido");
        int nextId;
        if(currentIdNum == null){
            nextId = 0;
        }else {
            nextId = currentIdNum.intValue()+1;
        }
        return nextId;
    }


    public final static void addPedidorealm(final Detallepedidorealm Detallepedidorealm){
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction(){
            @Override
            public void execute(Realm realm){
                int index = Crudpedido.calculateIndex();
                PedidoRealm pedidoRealm = realm.createObject(PedidoRealm.class, index);
                pedidoRealm.setEstadopedido(pedidoRealm.getEstadopedido());
                pedidoRealm.setFechapedido(pedidoRealm.getFechapedido());
                pedidoRealm.setIdalmacen(pedidoRealm.getIdalmacen());
                pedidoRealm.setDescripcionpedido(pedidoRealm.getDescripcionpedido());
                pedidoRealm.setIdcliente(pedidoRealm.getIdcliente());
                pedidoRealm.setIdmesa(pedidoRealm.getIdmesa());
                pedidoRealm.setIdusuario(pedidoRealm.getIdusuario());
                pedidoRealm.setIdfacebook(pedidoRealm.getIdfacebook());
pedidoRealm.setTotalpedido(pedidoRealm.getTotalpedido());
                  }
        });
    }
    public final static List<PedidoRealm> getAllPedidorealm(){
        Realm realm = Realm.getDefaultInstance();
        RealmResults<PedidoRealm> PedidoRealm = realm.where(PedidoRealm.class).findAll();
        for(PedidoRealm Pedidorealm: PedidoRealm){
            Log.d("TAG", "idpedido: " + Pedidorealm.getIdpedido() );


        }
        return PedidoRealm;
    }


    public final static PedidoRealm getPedidorealmByIdpedido(int idpedido){
        Realm realm = Realm.getDefaultInstance();
        PedidoRealm PedidoRealm = realm.where(PedidoRealm.class).equalTo("idpedido", idpedido).findFirst();
        if(PedidoRealm != null){
            Log.d("TAG", "idmesadepedidoatrare: " + PedidoRealm.getIdmesa() );
        }
        return PedidoRealm;
    }

    public final static void Actualizartotalpedido(int idpedido, Double totalpedido){

        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        PedidoRealm pedidoRealm = realm.where(PedidoRealm.class).equalTo("idpedido", idpedido).findFirst();
        pedidoRealm.setTotalpedido(totalpedido);
 realm.insertOrUpdate(pedidoRealm);
        realm.commitTransaction();
        Log.d("TAG", "se actualiuzo total pedido a: " + Double.toString(totalpedido ));
    }
    public final static void Actualizardescripciondepedido(int idpedido, String descripcionpedido){

        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        PedidoRealm pedidoRealm = realm.where(PedidoRealm.class).equalTo("descripcionpedido", descripcionpedido).findFirst();
        pedidoRealm.setDescripcionpedido(descripcionpedido);
        realm.insertOrUpdate(pedidoRealm);
        realm.commitTransaction();
        Log.d("TAG", "se actualizo descripcion de pedido: " + descripcionpedido );
    }

    public final static void eliminarpedidoydetallesdepedidoporidpedido(int idpedido){
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();

        realm.executeTransaction(realm1 -> {
            RealmResults<Detallepedidorealm> result = realm1.where(Detallepedidorealm.class).equalTo("idpedido",idpedido).findAll();
            result.deleteAllFromRealm();
        });
        Log.d("TAG", "se elimino todo el detalle de pedido con id : " + String.valueOf(idpedido) );


        PedidoRealm PedidoRealm = realm.where(PedidoRealm.class).equalTo("idpedido", idpedido).findFirst();
        PedidoRealm.deleteFromRealm();
        realm.commitTransaction();
        Log.d("TAG", "se elimino pedido con id : " + String.valueOf(idpedido) );
    }
}
