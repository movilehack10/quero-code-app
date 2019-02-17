package com.wallet.movilehack.preferences;

import android.content.Context;
import android.util.Log;

import com.wallet.movilehack.interfaces.FuncoesActivitys;
import com.wallet.movilehack.util.ReadWriter;

import org.json.JSONArray;
import org.json.JSONObject;

public class Usuario {


    public static String getUsuarioJson(Context context) {
        Log.i("USUARIO", "Usuario -> json= " + ReadWriter.ler(ReadWriter.KEY_USUARIO_JSON, context, ""));
        return ReadWriter.ler(ReadWriter.KEY_USUARIO_JSON, context, "");
    }

    public static void setUsuarioJson(Context context, String usuarioJson) {
        ReadWriter.grava(ReadWriter.KEY_USUARIO_JSON, usuarioJson, context);
    }


    public static int getTela(Context context) {
        return ReadWriter.lerInt(ReadWriter.KEY_USUARIO_TELA, context, FuncoesActivitys.MENU_ADD_CARTAO);
    }

    public static void setTela(Context context, int id) {
        ReadWriter.grava(ReadWriter.KEY_USUARIO_TELA, id, context);
    }


    public static String getId(Context context) {
        return ReadWriter.ler(ReadWriter.KEY_USUARIO_ID, context, "");
    }

    public static void setId(Context context, String id) {
        ReadWriter.grava(ReadWriter.KEY_USUARIO_ID, id, context);
    }

    public static String getToken(Context context) {
        return ReadWriter.ler(ReadWriter.KEY_USUARIO_ID, context, "");
    }

    public static void setToken(Context context, String token) {
        ReadWriter.grava(ReadWriter.KEY_USUARIO_TOKEN, token, context);
    }


    public static String getTipoConta(Context context) {
        return ReadWriter.ler(ReadWriter.KEY_USUARIO_TIPO_CONTA, context, "");
    }

    public static void setTipoConta(Context context, String tipoConta) {
        ReadWriter.grava(ReadWriter.KEY_USUARIO_TIPO_CONTA, tipoConta, context);
    }


    public static String getSaldo(Context context) {
        return ReadWriter.ler(ReadWriter.KEY_USUARIO_SALDO, context, "R$ -,--");
    }




   /* public static boolean getFirst(Context context) {
        boolean value = false;
        try {
            JSONObject jsonBody = new JSONObject(getUsuarioJson(context));
            value = jsonBody.getBoolean("first");
            Log.i("DADOS", "USUARIO -> first = " + value);
        } catch (Exception e) {
        }
        return value;
    }*/


    public static void setSaldo(Context context, String json) {
        String value = "";
        try {
            JSONObject jsonBody = new JSONObject(json);
            value = jsonBody.getString("current_balance");
            Log.i("Usuario", "USUARIO -> current_balance = " + value);
        } catch (Exception e) {
        }
        ReadWriter.grava(ReadWriter.KEY_USUARIO_SALDO, value, context);
    }
}
