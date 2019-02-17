package com.wallet.movilehack.interfaces;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.ColorRes;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.wallet.movilehack.R;
import com.wallet.movilehack.preferences.Usuario;

import de.hdodenhof.circleimageview.CircleImageView;

public class FuncoesActivitys {

    public final static int MENU_CARTEIRA = 1;
    public final static int MENU_PERFIL = 2;
    public final static int MENU_PERFIL_COMERCIAL = 3;
    public final static int MENU_MAPA = 4;
    public final static int MENU_PROMOCOES = 5;
    public final static int MENU_ANALITICA = 6;
    public final static int MENU_ADD_CARTAO = 7;


    public static void menuNavigationItem(final NavigationView navigationView, Toolbar toolbar, final Context context, int telaMenu, View headerView) {
        final DrawerLayout drawerLayout = (DrawerLayout) ((AppCompatActivity) context).findViewById(R.id.drawer);
        Menu menu = navigationView.getMenu();
        navigationView.getMenu().clear();
        navigationView.inflateMenu(R.menu.menu_navigation_main);

     /*   android:id="@+id/nav_carteira"
        android:id="@+id/nav_perfil"
        android:id="@+id/nav_perfil_comercial"
        android:id="@+id/nav_mapa"
        android:id="@+id/nav_promocoes"
        android:id="@+id/nav_analitica"*/

        MenuItem menuItemCarteira = menu.findItem(R.id.nav_carteira);
        MenuItem menuItemPerfil = menu.findItem(R.id.nav_perfil);
        MenuItem menuItemPerfilComercial = menu.findItem(R.id.nav_perfil_comercial);
        MenuItem menuItemMapa = menu.findItem(R.id.nav_mapa);
        MenuItem menuItemPromocoes = menu.findItem(R.id.nav_promocoes);
        MenuItem menuItemAnalitica = menu.findItem(R.id.nav_analitica);
        MenuItem menuItemAddCarde = menu.findItem(R.id.nav_add_cartao);

        if (Usuario.getTipoConta(context).equals("P")) {
            menuItemPerfilComercial.setVisible(false);
            menuItemAnalitica.setVisible(false);
            menuItemPromocoes.setVisible(false);
            menuItemMapa.setVisible(true);
            menuItemPerfil.setVisible(true);
        } else {
            menuItemPerfilComercial.setVisible(true);
            menuItemAnalitica.setVisible(true);
            menuItemPromocoes.setVisible(true);
            menuItemMapa.setVisible(false);
            menuItemPerfil.setVisible(false);
        }

        if (telaMenu == MENU_CARTEIRA)
            setTextColorForMenuItem(menuItemCarteira, R.color.green_50, context);

        if (telaMenu == MENU_PERFIL)
            setTextColorForMenuItem(menuItemPerfil, R.color.green_50, context);

        if (telaMenu == MENU_PERFIL_COMERCIAL)
            setTextColorForMenuItem(menuItemPerfilComercial, R.color.green_50, context);

        if (telaMenu == MENU_ANALITICA)
            setTextColorForMenuItem(menuItemAnalitica, R.color.green_50, context);

        if (telaMenu == MENU_MAPA)
            setTextColorForMenuItem(menuItemMapa, R.color.green_50, context);

        if (telaMenu == MENU_PROMOCOES)
            setTextColorForMenuItem(menuItemPromocoes, R.color.green_50, context);

        if (telaMenu == MENU_ADD_CARTAO)
            setTextColorForMenuItem(menuItemAddCarde, R.color.green_50, context);

        TextView txtNomeUsuario = (TextView) headerView.findViewById(R.id.txt_usuario_perfil);
        TextView txtTipoConta = (TextView) headerView.findViewById(R.id.txt_tipo_conta);

        CircleImageView imbContaUsuario = (CircleImageView)  headerView.findViewById(R.id.imb_foto_perfil);
        CircleImageView imbContaUsuarioProfissional = (CircleImageView)  headerView.findViewById(R.id.imb_foto_perfil_profissional);

        Button btnSejaContaProfissional = (Button) headerView.findViewById(R.id.b_seja_conta_profissional);
        if(Usuario.getTipoConta(context).equals("PRO")){
            txtTipoConta.setText("Conta Profissional");
            imbContaUsuario.setVisibility(View.GONE);
            imbContaUsuarioProfissional.setVisibility(View.VISIBLE);
            btnSejaContaProfissional.setText("Mudar para conta pessoal");
        }else {
            btnSejaContaProfissional.setText("Mudar para conta profissional");
            txtTipoConta.setText("Conta Pessoal");
            imbContaUsuario.setVisibility(View.VISIBLE);
            imbContaUsuarioProfissional.setVisibility(View.GONE);
        }


        final String[] fragments = {
                "com.wallet.movilehack.fragments.CarteiraFragment",// [0]
                "com.wallet.movilehack.fragments.PerfilFragment",// [1]
                "com.wallet.movilehack.fragments.PerfilComercialFragment",// [2]
                "com.wallet.movilehack.fragments.AnaliticaFragment",// [3]
                "com.wallet.movilehack.fragments.MapaFragment",// [4]
                "com.wallet.movilehack.fragments.PromocoesFragment",// [5]
                "com.wallet.movilehack.fragments.AddCartaoFragment"// [6]
        };


        Usuario.setTela(context, telaMenu);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                drawerLayout.closeDrawers();
                Intent intent;
                FragmentTransaction ft = null;

                switch (menuItem.getItemId()) {
                    case R.id.nav_carteira:
                        ft = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.main, Fragment.instantiate(context, fragments[0]));
                        ft.commit();
                        break;

                    case R.id.nav_perfil:
                        Toast.makeText(context, "Em desenvolvimento...", Toast.LENGTH_SHORT).show();
                      //  ft = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
                        //ft.replace(R.id.main, Fragment.instantiate(context, fragments[1]));
                       // ft.commit();
                        break;

                    case R.id.nav_perfil_comercial:
                        Toast.makeText(context, "Em desenvolvimento...", Toast.LENGTH_SHORT).show();
                      //  ft = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
                        //ft.replace(R.id.main, Fragment.instantiate(context, fragments[2]));
                       // ft.commit();
                        break;

                    case R.id.nav_analitica:
                        Toast.makeText(context, "Em desenvolvimento...", Toast.LENGTH_SHORT).show();
                   //     ft = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
                   //     ft.replace(R.id.main, Fragment.instantiate(context, fragments[3]));
                    //    ft.commit();
                        break;

                    case R.id.nav_mapa:
                        Toast.makeText(context, "Em desenvolvimento...", Toast.LENGTH_SHORT).show();
                       // ft = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
                       // ft.replace(R.id.main, Fragment.instantiate(context, fragments[4]));
                      //  ft.commit();
                        break;

                    case R.id.nav_add_cartao:
                        ft = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.main, Fragment.instantiate(context, fragments[6]));
                        ft.commit();
                        break;

                    case R.id.nav_promocoes:
                        Toast.makeText(context, "Em desenvolvimento...", Toast.LENGTH_SHORT).show();
                       // ft = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
                        //ft.replace(R.id.main, Fragment.instantiate(context, fragments[5]));
                        //ft.commit();
                        break;
                     /*   AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Credencias");
                        builder.setMessage("Deseja reenviar suas credencias de acesso ?");
                        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        builder.show();*/
                    default:;
                }
                return true;
            }
        });

        // Initializing Drawer Layout and ActionBarToggle
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle((AppCompatActivity) context, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank

                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessay or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    private static void setTextColorForMenuItem(MenuItem menuItem, @ColorRes int color, Context context) {
      /*  try {
            SpannableString spanString = new SpannableString(menuItem.getTitle().toString());
            spanString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, color)), 0, spanString.length(), 0);
            menuItem.setTitle(spanString);
        } catch (Exception e) {
        }*/
    }

}
