package com.wallet.movilehack.fragments;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.wallet.movilehack.R;
import com.wallet.movilehack.interfaces.FuncoesActivitys;
import com.wallet.movilehack.preferences.Usuario;


public class PagarFragment extends Fragment implements NavigationView.OnNavigationItemSelectedListener {

    private OnFragmentInteractionListener mListener;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private View view;
    private View headerView;
    private Button btnCriarQRCode;
    private TextView txtTitleSaldo;
    private TextView txtSaldo;


    // TODO: Rename and change types and number of parameters
    public static PagarFragment newInstance(String param1, String param2) {
        PagarFragment fragment = new PagarFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_pagar, container, false);
        return view;
    }

    private void carregarReferencias() {
        btnCriarQRCode = (Button) view.findViewById(R.id.b_criar_qr_code);
        txtTitleSaldo = (TextView) view.findViewById(R.id.txtTitleSaldo);
        txtSaldo = (TextView) view.findViewById(R.id.txtSaldo);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        navigationView = (NavigationView) getActivity().findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        headerView = navigationView.getHeaderView(0);
    }

    private void carregarInformacao() {


    }

    private void listener() {
        btnCriarQRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        carregarReferencias();
        listener();
        FuncoesActivitys.menuNavigationItem(navigationView, toolbar, getContext(), FuncoesActivitys.MENU_CARTEIRA, headerView);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
