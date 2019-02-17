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
import android.widget.Toast;

import com.wallet.movilehack.R;
import com.wallet.movilehack.conexao.Configuracao;
import com.wallet.movilehack.interfaces.FuncoesActivitys;
import com.wallet.movilehack.preferences.DadosCartao;
import com.wallet.movilehack.preferences.Usuario;

import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class AddCartaoFragment extends Fragment implements NavigationView.OnNavigationItemSelectedListener {

    private final static int KEY_POST_1 = 1;
    private final static int KEY_POST_2 = 2;
    private final static int KEY_POST_3 = 3;
    private final static int KEY_POST_4 = 4;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private View view;
    private View headerView;
    private ImageView ivProcessDialog;
    private ProgressBar pbProcessDialog;
    private View vAboutDialog;
    private Button btnOkDialog;
    private AlertDialog dialog;
    private AsyncTask task;
    private String cardToken;


    // TODO: Rename and change types and number of parameters
    public static AddCartaoFragment newInstance(String param1, String param2) {
        AddCartaoFragment fragment = new AddCartaoFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public AddCartaoFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add_cartao, container, false);
        return view;
    }

    private void carregarReferencias() {
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        navigationView = (NavigationView) getActivity().findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        headerView = navigationView.getHeaderView(0);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        carregarReferencias();
        verificacao();

        FuncoesActivitys.menuNavigationItem(navigationView, toolbar, getContext(), FuncoesActivitys.MENU_ADD_CARTAO, headerView);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }


    private void verificacao() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        final View v = LayoutInflater.from(getContext()).inflate(R.layout.layout_add_cartao, null);
        vAboutDialog = v.findViewById(R.id.v_about_button);
        btnOkDialog = v.findViewById(R.id.btn_ok);

        btnOkDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
                dialog.dismiss();
                transacao(KEY_POST_1);

            }
        });

        builder.setView(v);
        builder.setCancelable(false);
        dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();

    }

    private void transacao(final int key) {
        if(key == KEY_POST_1) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            final View v = LayoutInflater.from(getContext()).inflate(R.layout.layout_realizando_transacao, null);
            builder.setView(v);
            builder.setCancelable(false);
            dialog = builder.create();
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.show();
        }
        task = new AsyncTask<Integer, String, String>() {
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
            protected String doInBackground(Integer... c) {
                etapaConcluida = false;
                String response = "";
                stringResponse = "-";
                int keyPost = c[0];
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

                    switch (keyPost) {
                        case KEY_POST_1:
                            formBuilder.add("holder_name", "ZOOP SQUAD QA FACA NA CAVEIRA");
                            formBuilder.add("expiration_month", "09");
                            formBuilder.add("expiration_year", "2020");
                            formBuilder.add("security_code", "654");
                            formBuilder.add("card_number", "4539003370725497");
                            formBody = formBuilder.build();
                            request = new Request.Builder()
                                    .url(Configuracao.urlObterToken)
                                    .post(formBody)
                                    .addHeader("content-type", "application/json")
                                    .addHeader("Authorization", "Basic enBrX3Rlc3Rfb2dtaTNUSm5WMzNVRGxqZE40bjhhUml0Og==")

                                    // .addHeader("x-access-token", Usuario.getToken(getContext()))
                                    .addHeader("cache-control", "no-cache")
                                    .build();
                            break;
                        case KEY_POST_2:
                            formBuilder.add("buyer_id", Usuario.getId(getContext()));
                            formBuilder.add("card_token", cardToken);
                            formBody = formBuilder.build();
                            request = new Request.Builder()
                                    .url(Configuracao.urlAssociarCartaoGrupo)
                                    .post(formBody)
                                    .addHeader("content-type", "application/x-www-form-urlencoded")
                                    .addHeader("cache-control", "no-cache")
                                    .build();
                            break;

                        case KEY_POST_3:
                            formBuilder.add("buyer_id", Usuario.getId(getContext()));
                            formBuilder.add("amount", "180");
                            formBody = formBuilder.build();
                            request = new Request.Builder()
                                    .url(Configuracao.urlFazerTransacaoCartao)
                                    .post(formBody)
                                    .addHeader("content-type", "application/x-www-form-urlencoded")
                                    .addHeader("cache-control", "no-cache")
                                    .build();
                            break;
                        case KEY_POST_4:
                            formBuilder.add("buyer_id", Usuario.getId(getContext()));
                            formBuilder.add("amount", "180");
                            formBody = formBuilder.build();
                            request = new Request.Builder()
                                    .url(Configuracao.urlDebitoParaWallet)
                                    .post(formBody)
                                    .addHeader("content-type", "application/x-www-form-urlencoded")
                                    .addHeader("cache-control", "no-cache")
                                    .build();
                            break;
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

                Log.i("AddCartaoFragment", " values = " + values[0]);
                Log.i("AddCartaoFragment", " etapaConcluida= " + etapaConcluida);
                Log.i("AddCartaoFragment", " KEY_POST= " +key);
                if (etapaConcluida) {
                    switch (key) {
                        case KEY_POST_1:
                            cardToken = DadosCartao.get(getContext(), "id", values[0]);
                            transacao(KEY_POST_2);
                            break;
                        case KEY_POST_2:
                            transacao(KEY_POST_3);
                            break;
                        case KEY_POST_3:
                            transacao(KEY_POST_4);
                            break;
                        case KEY_POST_4:
                            dialog.cancel();
                            dialog.dismiss();

                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            final View v = LayoutInflater.from(getContext()).inflate(R.layout.layout_add_valor, null);
                            vAboutDialog = v.findViewById(R.id.v_about_button);
                            btnOkDialog = v.findViewById(R.id.btn_ok);

                            btnOkDialog.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.cancel();
                                    dialog.dismiss();
                                    FragmentTransaction ft = ((AppCompatActivity) getContext()).getSupportFragmentManager().beginTransaction();
                                    ft.replace(R.id.main, Fragment.instantiate(getContext(), "com.wallet.movilehack.fragments.CarteiraFragment"));
                                    ft.commit();
                                }
                            });

                            builder.setView(v);
                            builder.setCancelable(false);
                            dialog = builder.create();
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.show();
                            break;
                    }
                } else {
                    dialog.cancel();
                    dialog.dismiss();
                    Toast.makeText(getContext(), "Um erro ocorreu", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            protected void onCancelled() {
                super.onCancelled();
            }

        }.execute(key);
    }
}


