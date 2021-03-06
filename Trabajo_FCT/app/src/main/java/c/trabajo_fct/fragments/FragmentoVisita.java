package c.trabajo_fct.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import c.trabajo_fct.DividerItemDecoration;
import c.trabajo_fct.R;
import c.trabajo_fct.activities.MainActivity;
import c.trabajo_fct.adapters.VisitasAdapter;
import c.trabajo_fct.bdd.DAO;
import c.trabajo_fct.interfaces.Callback_MainActivity;
import c.trabajo_fct.interfaces.FragmentAllowMultideletion;
import c.trabajo_fct.interfaces.GestionFabDesdeFragmento;
import c.trabajo_fct.interfaces.OnAdapterItemClick;
import c.trabajo_fct.interfaces.OnAdapterItemLongClick;
import c.trabajo_fct.modelos.Alumno;
import c.trabajo_fct.modelos.Visita;

/**
 * Created by Cristina on 27/02/2016.
 */
public class FragmentoVisita extends Fragment implements GestionFabDesdeFragmento, FragmentAllowMultideletion{

    public final static String ORDEN_ASCENDENTE = "Asc";
    public final static String ORDEN_DESCENDENTE = "Desc";

    private Callback_MainActivity listener;
    private OnAdapterItemLongClick listenerLongClick;
    private RecyclerView lstVisitas;
    private VisitasAdapter adaptador;
    private DAO gestor;
    private static Alumno alumno;
    private BroadcastReceiver receptor;
    private LocalBroadcastManager gestorLocal;
    private IntentFilter filtro;

    public static FragmentoVisita newInstance(Alumno a) {
        alumno = a;
        return new FragmentoVisita();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_visita, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        gestor = new DAO(getContext());
        lstVisitas = (RecyclerView) getView().findViewById(R.id.lstVisitas);
        if (alumno == null)
            adaptador = new VisitasAdapter((ArrayList<Visita>)gestor.selectProximasVisitasYRealizadasAyer(new Date(new Date().getTime() - TimeUnit.DAYS.toMillis(1)).getTime()));
        else
            adaptador = new VisitasAdapter((ArrayList<Visita>)gestor.selectAllVisitasDeAlumno(alumno.getId()));
        adaptador.setEmptyView(getView().findViewById(R.id.lblNoHayVisitas));
        adaptador.setOnItemClickListener((OnAdapterItemClick) getActivity());
        adaptador.setListenerLongClick((OnAdapterItemLongClick) getActivity());
        lstVisitas.setAdapter(adaptador);
        lstVisitas.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        lstVisitas.setItemAnimator(new DefaultItemAnimator());
        lstVisitas.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));

        receptor = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                adaptador.reloadData(context);
            }
        };

        filtro = new IntentFilter(MainActivity.ALUMNO_ELIMINADO_REFRESCAR_VISITAS_ACTION);
        gestorLocal = LocalBroadcastManager.getInstance(getContext());
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onFabPressed() {
        listener.cargarFragmentoSecundario(MainActivity.FRAGMENTO_INSERT_UPDATE_VISITA, null);
    }

    @Override
    public void onResume() {
        gestorLocal.registerReceiver(receptor, filtro);
        super.onResume();
    }

    @Override
    public void onPause() {
        gestorLocal.unregisterReceiver(receptor);
        super.onPause();
    }

    @Override
    public void setFabImage() {
        listener.setFabImage(R.drawable.ic_event);
    }

    public void setListener(Callback_MainActivity listener) {
        this.listener = listener;
    }

    @Override
    public Callback_MainActivity getListener() {
        return listener;
    }

    @Override
    public void onAttach(Context context) {
        listener = (Callback_MainActivity) context;
        listenerLongClick = (OnAdapterItemLongClick) context;
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        disableMultideletion();
        listenerLongClick = null;
        listener = null;
        super.onDetach();
    }

    public VisitasAdapter getAdaptador() {
        return adaptador;
    }

    @Override
    public void desactivarMultideletion() {
        disableMultideletion();
    }

    private void disableMultideletion(){
        listenerLongClick.desactivarMultiseleccion();
        adaptador.clearAllSelections();
        adaptador.disableMultiDeletionMode();
    }
}
