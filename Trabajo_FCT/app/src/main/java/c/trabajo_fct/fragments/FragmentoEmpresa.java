package c.trabajo_fct.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import c.trabajo_fct.DividerItemDecoration;
import c.trabajo_fct.R;
import c.trabajo_fct.activities.MainActivity;
import c.trabajo_fct.adapters.EmpresasAdapter;
import c.trabajo_fct.bdd.DAO;
import c.trabajo_fct.interfaces.Callback_MainActivity;
import c.trabajo_fct.interfaces.FragmentAllowMultideletion;
import c.trabajo_fct.interfaces.GestionFabDesdeFragmento;
import c.trabajo_fct.interfaces.OnAdapterItemClick;
import c.trabajo_fct.interfaces.OnAdapterItemLongClick;

/**
 * Created by Cristina on 27/02/2016.
 */
public class FragmentoEmpresa extends Fragment implements GestionFabDesdeFragmento, FragmentAllowMultideletion {

    private Callback_MainActivity listener;
    private OnAdapterItemLongClick listenerLongClick;
    private RecyclerView lstEmpresas;
    private EmpresasAdapter adaptador;
    private DAO gestor;

    public FragmentoEmpresa() {
    }

    public static FragmentoEmpresa newInstance() {
        FragmentoEmpresa fragment = new FragmentoEmpresa();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_empresa, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        gestor = new DAO(getContext());

        lstEmpresas = (RecyclerView) getView().findViewById(R.id.lstEmpresas);
        adaptador = new EmpresasAdapter(gestor.selectAllEmpresa());
        adaptador.setEmptyView(getView().findViewById(R.id.lblNoHayEmpresas));
        adaptador.setOnItemClickListener((OnAdapterItemClick) getActivity());
        adaptador.setListenerLongClick((OnAdapterItemLongClick) getActivity());
        lstEmpresas.setAdapter(adaptador);
        lstEmpresas.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        lstEmpresas.setItemAnimator(new DefaultItemAnimator());
        lstEmpresas.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    public void onFabPressed() {
        if (listener != null)
            listener.cargarFragmentoSecundario(MainActivity.FRAGMENTO_INSERT_UPDATE_EMPRESA, null);
        else
            Toast.makeText(getContext(), "listener null", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setFabImage() {
        if (listener != null)
            listener.setFabImage(R.drawable.ic_store);
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

    @Override
    public void desactivarMultideletion() {
        disableMultideletion();
    }

    private void disableMultideletion(){
        listenerLongClick.desactivarMultiseleccion();
        adaptador.clearAllSelections();
        adaptador.disableMultiDeletionMode();
    }

    public EmpresasAdapter getAdaptador() {
        return adaptador;
    }
}
