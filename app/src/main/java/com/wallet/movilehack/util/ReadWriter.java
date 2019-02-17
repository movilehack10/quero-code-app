package com.wallet.movilehack.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.math.BigDecimal;

public class ReadWriter {
    public static final String APP_PREFS = "APP_PREFS";
    public static final String KEY_USUARIO_JSON  = "APP_USUARIO_JSON";
    public static final String KEY_USUARIO_TELA  = "APP_USUARIO_JSON";
    public static final String KEY_USUARIO_TOKEN  = "KEY_USUARIO_TOKEN";
    public static final String KEY_USUARIO_ID  = "KEY_USUARIO_ID";
    public static final String KEY_USUARIO_SALDO  = "KEY_USUARIO_SALDO";
    public static final String KEY_USUARIO_TIPO_CONTA  = "KEY_USUARIO_TIPO_CONTA";

    public static final String KEY_DADOS_CARTAO_NUMERO  = "KEY_DADOS_CARTAO_NUMERO";
    public static final String KEY_DADOS_SECURITY_CODE  = "KEY_DADOS_SECURITY_CODE";

    public static void grava(String chave, String valor, Context ctx) {
        if (ctx != null) {
            SharedPreferences prefs = ctx.getSharedPreferences(APP_PREFS,
                    Context.MODE_PRIVATE);

            SharedPreferences.Editor edit = prefs.edit();
            edit.putString(chave, valor);
            edit.commit();
        }
    }

    public static void grava(String chave, int valor, Context ctx) {
        if (ctx != null) {
            SharedPreferences prefs = ctx.getSharedPreferences(APP_PREFS,
                    Context.MODE_PRIVATE);

            SharedPreferences.Editor edit = prefs.edit();
            edit.putInt(chave, valor);
            edit.commit();
        }
    }

    public static void grava(String chave, long valor, Context ctx) {
        if (ctx != null) {
            SharedPreferences prefs = ctx.getSharedPreferences(APP_PREFS,
                    Context.MODE_PRIVATE);

            SharedPreferences.Editor edit = prefs.edit();
            edit.putLong(chave, valor);
            edit.commit();
        }
    }

    public static void gravaBigDecimal(String chave, BigDecimal valor,
                                       Context ctx) {
        if (ctx != null) {
            SharedPreferences prefs = ctx.getSharedPreferences(APP_PREFS,
                    Context.MODE_PRIVATE);

            SharedPreferences.Editor edit = prefs.edit();
            if (valor != null)
                edit.putString(chave, valor.toString());
            else
                edit.putString(chave, null);
            edit.commit();
        }
    }

    public static String ler(String chave, Context ctx) {
        String ret = null;
        if (ctx != null) {
            SharedPreferences prefs = ctx.getSharedPreferences(APP_PREFS,
                    Context.MODE_PRIVATE);
            ret = prefs.getString(chave, null);
        }
        return ret;
    }

    public static String ler(String chave, Context ctx, String def) {
        String ret = null;
        if (ctx != null) {
            SharedPreferences prefs = ctx.getSharedPreferences(APP_PREFS,
                    Context.MODE_PRIVATE);
            ret = prefs.getString(chave, def);
        }
        return ret;
    }


    public static int lerInt(String chave, Context ctx) {
        int ret = 0;
        if (ctx != null) {
            SharedPreferences prefs = ctx.getSharedPreferences(APP_PREFS,
                    Context.MODE_PRIVATE);
            ret = prefs.getInt(chave, 0);
        }
        return ret;
    }


    public static int lerInt(String chave, Context ctx, int defValue) {
        int ret = 0;
        if (ctx != null) {
            SharedPreferences prefs = ctx.getSharedPreferences(APP_PREFS,
                    Context.MODE_PRIVATE);
            ret = prefs.getInt(chave, defValue);
        }
        return ret;
    }


    public static long lerLong(String chave, Context ctx, long defValue) {
        long ret = 0;
        if (ctx != null) {
            SharedPreferences prefs = ctx.getSharedPreferences(APP_PREFS,
                    Context.MODE_PRIVATE);
            ret = prefs.getLong(chave, defValue);
        }
        return ret;
    }

    public static long lerLong(String chave, Context ctx) {
        long ret = 0;
        if (ctx != null) {
            SharedPreferences prefs = ctx.getSharedPreferences(APP_PREFS,
                    Context.MODE_PRIVATE);
            try {
                ret = prefs.getLong(chave, 0);
            } catch (Exception e) {
                ret = 0;
            }
        }
        return ret;
    }


    public static long lerLong(String chave, Context ctx, int defValue) {
        long ret = 0;
        if (ctx != null) {
            SharedPreferences prefs = ctx.getSharedPreferences(APP_PREFS,
                    Context.MODE_PRIVATE);
            ret = prefs.getLong(chave, defValue);
        }
        return ret;
    }

    public static BigDecimal lerBigDecimal(String chave, Context ctx) {
        BigDecimal ret = new BigDecimal("0");
        if (ctx != null) {
            SharedPreferences prefs = ctx.getSharedPreferences(APP_PREFS,
                    Context.MODE_PRIVATE);
            String obs = prefs.getString(chave, null);
            if (obs != null)
                return new BigDecimal(obs);
        }
        return ret;
    }

    public static void gravaBoolean(String chave, boolean valor, Context context) {
        if (context != null) {
            SharedPreferences prefs = context.getSharedPreferences(APP_PREFS,
                    Context.MODE_PRIVATE);

            SharedPreferences.Editor edit = prefs.edit();
            edit.putBoolean(chave, valor);
            edit.commit();
        }

    }

    public static boolean lerBoolean(String chave, Context context) {
        boolean ret = false;
        if (context != null) {
            SharedPreferences prefs = context.getSharedPreferences(APP_PREFS,
                    Context.MODE_PRIVATE);
            ret = prefs.getBoolean(chave, false);
        }

        return ret;
    }

    public static boolean lerBoolean(String chave, Context context, boolean def) {
        boolean ret = def;
        if (context != null) {
            SharedPreferences prefs = context.getSharedPreferences(APP_PREFS,
                    Context.MODE_PRIVATE);
            ret = prefs.getBoolean(chave, def);
        }

        return ret;
    }
}
