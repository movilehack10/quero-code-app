package com.wallet.movilehack.conexao;

public class Configuracao {


    public static final String urlRealizarCompra = "http://138.197.170.34:3004";

    public static final String urlCriarUsuario = "http://138.197.170.34:3004";
    public static final String urlObterSaldo = "http://138.197.170.34:3004/buyer-info";
    //TOKENINZAR Post
    public static final String urlObterToken = "https://api.zoop.ws/v1/marketplaces/3249465a7753536b62545a6a684b0000/cards/tokens";

    //Associar Cartao Grupo
    public static final String urlAssociarCartaoGrupo = "http://138.197.170.34:3004/associate-card-customer";

    //Fazer transação de cartão nao presente
    public static final String urlFazerTransacaoCartao = "http://138.197.170.34:3004/one-click-pay";


    //debito para sua wallet
    public static final String urlDebitoParaWallet = "http://138.197.170.34:3004/p2p-seller_master-buyer";

}
