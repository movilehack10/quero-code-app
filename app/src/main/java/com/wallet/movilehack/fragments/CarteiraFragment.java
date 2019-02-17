package com.wallet.movilehack.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.QuickContactBadge;
import android.widget.TableRow;
import android.widget.TextView;

import com.wallet.movilehack.R;
import com.wallet.movilehack.conexao.Configuracao;
import com.wallet.movilehack.interfaces.FuncoesActivitys;
import com.wallet.movilehack.preferences.Usuario;

import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class CarteiraFragment extends Fragment implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private NavigationView navigationView;
    private View view;
    private View headerView;
    private Button btnPagar;
    private Button btnReceber;
    private Button btnAddCartao;
    private TextView txtTitleSaldo;
    private TextView txtSaldo;
    private Button btnLerQR;
    private EditText edtValor;
    private ImageView ivProcessDialog;
    private TextView tvMsgDialog;
    private ProgressBar pbProcessDialog;
    private View vAboutDialog;
    private Button btnOkDialog;
    private AlertDialog dialog;
    private AsyncTask task;


    // TODO: Rename and change types and number of parameters
    public static CarteiraFragment newInstance(String param1, String param2) {
        CarteiraFragment fragment = new CarteiraFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public CarteiraFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_carteira, container, false);
        return view;
    }

    private void carregarReferencias() {
        btnPagar = (Button) view.findViewById(R.id.b_pagar);
        btnReceber = (Button) view.findViewById(R.id.b_receber);
        btnAddCartao = (Button) view.findViewById(R.id.b_home_add_cartao);
        txtTitleSaldo = (TextView) view.findViewById(R.id.txtTitleSaldo);
        txtSaldo = (TextView) view.findViewById(R.id.txtSaldo);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        navigationView = (NavigationView) getActivity().findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        headerView = navigationView.getHeaderView(0);
    }

    private void carregarInformacoes() {
        if (TextUtils.isEmpty(Usuario.getId(getContext())))
            Usuario.setTipoConta(getContext(), "P");

        if (Usuario.getTipoConta(getContext()).equals("PRO")) {
            txtSaldo.setText(Usuario.getSaldo(getContext()));
            txtTitleSaldo.setText("Fluxo de Caixa:");
            btnPagar.setVisibility(View.GONE);
        } else {
            txtSaldo.setText(Usuario.getSaldo(getContext()));
            txtTitleSaldo.setText("Saldo disponível:");
            btnPagar.setVisibility(View.VISIBLE);
        }


    }

    private void listener() {
        btnAddCartao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = ((AppCompatActivity) getContext()).getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.main, Fragment.instantiate(getContext(), "com.wallet.movilehack.fragments.AddCartaoFragment"));
                ft.commit();
            }
        });

        btnPagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = ((AppCompatActivity) getContext()).getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.main, Fragment.instantiate(getContext(), "com.wallet.movilehack.fragments.PagarFragment"));
                ft.commit();
            }
        });

        btnReceber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = ((AppCompatActivity) getContext()).getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.main, Fragment.instantiate(getContext(), "com.wallet.movilehack.fragments.ReceberFragment"));
                ft.commit();
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        carregarReferencias();
        listener();
        carregarInformacoes();

        if (Usuario.getTipoConta(getContext()).equals("PRO"))
            btnPagar.setVisibility(View.GONE);
        else
            btnPagar.setVisibility(View.VISIBLE);

        if(TextUtils.isEmpty(Usuario.getId(getContext())))
        transacao(true);

        transacao(false);

        FuncoesActivitys.menuNavigationItem(navigationView, toolbar, getContext(), FuncoesActivitys.MENU_CARTEIRA, headerView);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }


    private void verificarUsuario() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        final View v = LayoutInflater.from(getContext()).inflate(R.layout.layout_recebendo_pagamento, null);
        ivProcessDialog = v.findViewById(R.id.iv_process);
        tvMsgDialog = v.findViewById(R.id.tv_msg);
        pbProcessDialog = v.findViewById(R.id.pb_process);
        vAboutDialog = v.findViewById(R.id.v_about_button);
        btnOkDialog = v.findViewById(R.id.btn_ok);
        //tvData = v.findViewById(R.id.tv_data);

        btnOkDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
                dialog.dismiss();
            }
        });

        builder.setView(v);
        //builder.setTitle(getString(R.string.register_device));

        builder.setCancelable(false);
        dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();

        //changeViewDialog(R.drawable.qrcode, "Realizando pagamento...", ProgressBar.VISIBLE, Button.GONE, false);

    }

    private void transacao(final boolean isUser) {
        task = new AsyncTask<Boolean, String, String>() {
            private boolean etapaConcluida;
            private String stringResponse;
            private int paramentro = 0;
            private int codigo = 0;
            private boolean erroTimeOut;
            private AlertDialog alert;
            private Context context;
            private boolean isIdUser;


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }


            @Override
            protected String doInBackground(Boolean... c) {
                etapaConcluida = false;
                String response = "";
                stringResponse = "-";
                boolean isIdUser = c[0];
                try {
                    OkHttpClient.Builder b = new OkHttpClient.Builder();
                    b.connectTimeout(30, TimeUnit.SECONDS); // connect timeout
                    b.readTimeout(30, TimeUnit.SECONDS);
                    b.writeTimeout(30, TimeUnit.SECONDS);
                    b.retryOnConnectionFailure(false);
                    OkHttpClient client = b.build();

                    FormBody.Builder formBuilder = new FormBody.Builder();
                    Request request = null;
                    RequestBody formBody = null;

                    if (isIdUser) {
                        request = new Request.Builder()
                                .url(Configuracao.urlCriarUsuario)
                                .get()
                                .addHeader("cache-control", "no-cache")
                                .build();
                    } else {
                        formBuilder.add("buyer_id", Usuario.getId(getContext()));
                        formBody = formBuilder.build();
                        request = new Request.Builder()
                                .url(Configuracao.urlObterSaldo)
                                .post(formBody)
                                .addHeader("content-type", "application/x-www-form-urlencoded")
                                // .addHeader("x-access-token", Usuario.getToken(getContext()))
                                .addHeader("cache-control", "no-cache")
                                .build();
                    }
                    analisarRequest(request, client);
                } catch (Exception e) {
                    erroTimeOut = true;
                    e.printStackTrace();
                    Log.i("CarteiraFragment", "erro = " + e);
                }
                publishProgress(stringResponse);
                return "";
            }

            public void analisarRequest(Request request, OkHttpClient client) throws Exception {
                if (request != null) {
                    Response resp = client.newCall(request).execute();
                    this.stringResponse = resp.body().string();
                    this.codigo = resp.code();
                    this.etapaConcluida = resp.isSuccessful();
                }
            }

            @Override
            protected void onProgressUpdate(String... values) {
                super.onProgressUpdate(values);

                Log.i("CarteiraFragment", " values = " + values[0]);
                Log.i("CarteiraFragment", " etapaConcluida= " + etapaConcluida);
                if (etapaConcluida) {
                    if (isUser)
                        Usuario.setId(getContext(), values[0]);
                    else
                        Usuario.setSaldo(getContext(), values[0]);
                    txtSaldo.setText(Usuario.getSaldo(getContext()));
                }
            }

            @Override
            protected void onCancelled() {
                super.onCancelled();
            }

        }.execute(isUser);
    }

    public void changeViewDialog(int resImageView, String text, int visibleProgressBar,
                                 int visibleButtonOK, boolean isErro) {
        ivProcessDialog.setImageResource(resImageView);
        tvMsgDialog.setText(text);
        btnOkDialog.setVisibility(visibleButtonOK);
        pbProcessDialog.setVisibility(visibleProgressBar);

        vAboutDialog.setVisibility(visibleButtonOK);

        if (isErro)
            tvMsgDialog.setTextColor(getResources().getColor(R.color.red));
        else
            tvMsgDialog.setTextColor(getResources().getColor(R.color.green));

    }

}
