package com.wallet.movilehack.interfaces;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.wallet.movilehack.R;
import com.wallet.movilehack.preferences.Usuario;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private View headerView;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private FragmentTransaction ft;
    public static AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        carregarReferencias();
        try {
            ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.main, Fragment.instantiate(this, "com.wallet.movilehack.fragments.CarteiraFragment"));
            ft.commit();
        } catch (Exception e) {
        }


        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);

        FuncoesActivitys.menuNavigationItem(navigationView, toolbar, this, FuncoesActivitys.MENU_CARTEIRA, headerView);
    }

    @Override

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_CAMERA_REQUEST_CODE) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();

            } else {

                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();

            }

        }}

    public void carregarReferencias() {
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        headerView = navigationView.getHeaderView(0);

        Button btnSejaContaProfissional = (Button) headerView.findViewById(R.id.b_seja_conta_profissional);
        btnSejaContaProfissional.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Usuario.getTipoConta(MainActivity.this).equals("PRO"))
                    Usuario.setTipoConta(MainActivity.this, "P");
                else
                    Usuario.setTipoConta(MainActivity.this, "PRO");

                FragmentTransaction ft = ((AppCompatActivity) MainActivity.this).getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.main, Fragment.instantiate(MainActivity.this, "com.wallet.movilehack.fragments.CarteiraFragment"));
                ft.commit();

            }
        });
    }

    @Override
    public void onBackPressed() {
        if (Usuario.getTela(this) == FuncoesActivitys.MENU_CARTEIRA)
            super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }
}
