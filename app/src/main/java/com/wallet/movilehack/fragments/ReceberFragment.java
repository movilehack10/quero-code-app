package com.wallet.movilehack.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothClass;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.TextView;
import android.widget.Toast;

import com.blikoon.qrcodescanner.QrCodeActivity;
import com.wallet.movilehack.R;
import com.wallet.movilehack.conexao.Configuracao;
import com.wallet.movilehack.interfaces.FuncoesActivitys;
import com.wallet.movilehack.preferences.Usuario;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class ReceberFragment extends Fragment implements NavigationView.OnNavigationItemSelectedListener {
    private static final int REQUEST_CODE_QR_SCAN = 101;
    private OnFragmentInteractionListener mListener;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private View view;
    private View headerView;
    private Button btnLerQR;
    private EditText edtValor;
    private ImageView ivProcessDialog;
    private TextView tvMsgDialog;
    private ProgressBar pbProcessDialog;
    private View vAboutDialog;
    private Button btnOkDialog;
    private AlertDialog dialog;




    // TODO: Rename and change types and number of parameters
    public static ReceberFragment newInstance(String param1, String param2) {
        ReceberFragment fragment = new ReceberFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_receber, container, false);
        return view;
    }

    private void carregarReferencias() {
        edtValor = (EditText) view.findViewById(R.id.edtValor);
        btnLerQR = (Button) view.findViewById(R.id.b_ler_qr);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        navigationView = (NavigationView) getActivity().findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        headerView = navigationView.getHeaderView(0);
    }

    private void lerQrCode() {
        Intent i = new Intent(getContext(), QrCodeActivity.class);
        getActivity().startActivityForResult(i, REQUEST_CODE_QR_SCAN);
    }

    private void listener() {
        btnLerQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(edtValor.getText().toString()))
                    lerQrCode();
                else
                    Toast.makeText(getContext(), "Campo valor não pode ficar em branco", Toast.LENGTH_SHORT).show();
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


    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != Activity.RESULT_OK) {
            Log.d("ReceberFragment", "COULD NOT GET A GOOD RESULT.");
            if (data == null)
                return;
            //Getting the passed result
            String result = data.getStringExtra("com.blikoon.qrcodescanner.error_decoding_image");
            if (result != null) {
                AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                alertDialog.setTitle("Scan Error");
                alertDialog.setMessage("QR Code could not be scanned");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
            return;

        }
        if (requestCode == REQUEST_CODE_QR_SCAN) {
            if (data == null)
                return;
            //Getting the passed result
            String result = data.getStringExtra("com.blikoon.qrcodescanner.got_qr_scan_relult");
            Log.i("ReceberFragment", "result ="+result);
            realizarPagamento(result, edtValor.getText().toString());

        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void realizarPagamento(String id, final String valor) {

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

        changeViewDialog(R.drawable.qrcode, "Realizando pagamento...", ProgressBar.VISIBLE, Button.GONE, false);


        new AsyncTask<String, String, String>() {
            private boolean etapaConcluida;
            private String stringResponse;
            private int paramentro = 0;
            private int codigo = 0;
            private boolean erroTimeOut;
            private AlertDialog alert;
            private Context context;


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }


            @Override
            protected String doInBackground(String... c) {
                etapaConcluida = false;
                String response = "";
                stringResponse = "-";
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

                   /* request = new Request.Builder()
                            .url(Configuracao.urlRealizarCompra)
                            .get()
                            .addHeader("content-type", "application/x-www-form-urlencoded")
                            .addHeader("cache-control", "no-cache")
                            .build();*/

                    formBuilder.add("id", "");
                    formBuilder.add("valeu", valor);
                    formBody = formBuilder.build();
                    request = new Request.Builder()
                            .url(Configuracao.urlRealizarCompra)
                            .post(formBody)
                            .addHeader("content-type", "application/x-www-form-urlencoded")
                            .addHeader("cache-control", "no-cache")
                            .build();
                    analisarRequest(request, client);
                } catch (Exception e) {
                    erroTimeOut = true;
                    e.printStackTrace();
                    Log.i("", "erro = " + e);
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

          //      if (TextUtils.isEmpty(values[0]))
          //          changeViewDialog(R.drawable.close_red, "Um erro ocorreu", ProgressBar.GONE, Button.VISIBLE, true);
           //     else {
             //       if (etapaConcluida)
                        changeViewDialog(R.drawable.close, "Pagamento realizada com sucesso!", ProgressBar.GONE, Button.VISIBLE, false);

              //      else
                //        changeViewDialog(R.drawable.close_red, "Um erro ocorreu", ProgressBar.GONE, Button.VISIBLE, true);

             //   }
            }

            @Override
            protected void onCancelled() {
                super.onCancelled();
            }

        }.execute(id);
    }

    public void changeViewDialog(int resImageView, String text, int visibleProgressBar,
                                 int visibleButtonOK, boolean isErro) {
        ivProcessDialog.setImageResource(resImageView);
        tvMsgDialog.setText(text);
        btnOkDialog.setVisibility(visibleButtonOK);
        btnOkDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = ((AppCompatActivity) getContext()).getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.main, Fragment.instantiate(getContext(), "com.wallet.movilehack.fragments.CarteiraFragment"));
                ft.commit();
            }
        });
        pbProcessDialog.setVisibility(visibleProgressBar);

        vAboutDialog.setVisibility(visibleButtonOK);

        if (isErro)
            tvMsgDialog.setTextColor(getResources().getColor(R.color.red));
        else
            tvMsgDialog.setTextColor(getResources().getColor(R.color.green));

    }

}
