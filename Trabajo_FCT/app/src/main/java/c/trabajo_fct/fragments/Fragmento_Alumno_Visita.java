package c.trabajo_fct.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import c.trabajo_fct.R;
import c.trabajo_fct.adapters.CachedFragmentPagerAdapter;
import c.trabajo_fct.interfaces.Callback_MainActivity;
import c.trabajo_fct.interfaces.GestionFabDesdeFragmento;
import c.trabajo_fct.interfaces.OnAdapterItemLongClick;
import c.trabajo_fct.modelos.Alumno;

/**
 * Created by Cristina on 29/02/2016.
 */
public class Fragmento_Alumno_Visita extends Fragment implements GestionFabDesdeFragmento{

    private Callback_MainActivity listener;
    private PaginasAdapter mAdaptador;
    private ViewPager viewPager;
    private static Alumno alumno;

    public static Fragmento_Alumno_Visita newInstance(Alumno a) {
        alumno = a;
        return new Fragmento_Alumno_Visita();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragmento_alumno_visita, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        setupViewPager();
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        listener = (Callback_MainActivity) context;
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        listener = null;
        super.onDetach();
    }

    private void setupViewPager() {
        TabLayout tabLayout = (TabLayout) getView().findViewById(R.id.tabL);
        viewPager = (ViewPager) getView().findViewById(R.id.viewPager);
        ArrayList<String> titulosPaginas = new ArrayList<>();
        titulosPaginas.add("Detalles Alumno");
        titulosPaginas.add("Visitas Alumno");
        mAdaptador = new PaginasAdapter(getChildFragmentManager(), titulosPaginas);
        viewPager.setAdapter(mAdaptador);
        viewPager.setOffscreenPageLimit(1);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                GestionFabDesdeFragmento fragmento = (GestionFabDesdeFragmento) mAdaptador.getFragment(viewPager.getCurrentItem());
                if (fragmento != null) {
                    if (fragmento.getListener() == null)
                        fragmento.setListener(listener);

                    fragmento.setFabImage();
                    setActivityMultiDeletionAdapter();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setActivityMultiDeletionAdapter() {
        Fragment fragmento = mAdaptador.getFragment(viewPager.getCurrentItem());
        if (fragmento != null)
            if (fragmento instanceof  FragmentoVisita)
                ((OnAdapterItemLongClick)getActivity()).setAdapterAllowMultiDeletion(((FragmentoVisita) fragmento).getAdaptador());
    }

    public PaginasAdapter getAdaptador() {
        return mAdaptador;
    }

    public ViewPager getViewPager() {
        return viewPager;
    }

    @Override
    public void onFabPressed() {
        GestionFabDesdeFragmento fragmento = (GestionFabDesdeFragmento) mAdaptador.getFragment(viewPager.getCurrentItem());
        if (fragmento != null)
            fragmento.onFabPressed();
    }

    @Override
    public void setFabImage() {
        GestionFabDesdeFragmento fragmento = (GestionFabDesdeFragmento) mAdaptador.getFragment(viewPager.getCurrentItem());
        if (fragmento != null)
            fragmento.setFabImage();
    }

    @Override
    public void setListener(Callback_MainActivity listener) {

    }

    @Override
    public Callback_MainActivity getListener() {
        return null;
    }

    public class PaginasAdapter extends CachedFragmentPagerAdapter {

        private final ArrayList<String> mTitulos;

        public PaginasAdapter(FragmentManager fm, ArrayList<String> titulos) {
            super(fm);
            this.mTitulos = titulos;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    Fragmento_Detalle_Alumno a = Fragmento_Detalle_Alumno.newInstance(alumno);
                    a.setListener(listener);
                    return a;
                case 1:
                    FragmentoVisita v = FragmentoVisita.newInstance(alumno);
                    v.setListener(listener);
                    return v;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return mTitulos.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitulos.get(position);
        }
    }
}
