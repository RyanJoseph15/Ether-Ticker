package com.minisoftwareandgames.ryan.etherticker;

import android.os.AsyncTask;
import android.util.Log;

import com.minisoftwareandgames.ryan.etherticker.objects.URLandViews;
import com.minisoftwareandgames.ryan.etherticker.objects.Widget;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by ryan on 8/14/15.
 */
public class JSONAsyncTask extends AsyncTask<URLandViews, Void, JSONObject> {

	@Override
	protected void onPreExecute() {
		super.onPreExecute();

	}

	@Override
	protected JSONObject doInBackground(URLandViews... urlAndviews) {
		try {
			//------------------>>
			HttpGet httppost = new HttpGet(urlAndviews[0].URL);
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response = httpclient.execute(httppost);

			// StatusLine stat = response.getStatusLine();
			int status = response.getStatusLine().getStatusCode();

			if (status == 200) {
				HttpEntity entity = response.getEntity();
				String data = EntityUtils.toString(entity);


				JSONObject jsono = new JSONObject(data);
//                    Log.d("etherticker", "response: " + jsono.toString());
                    /* get options from settings */
//                    SharedPreferences prefs = urlAndviews[0].context.getSharedPreferences("EtherTicker", Context.MODE_PRIVATE);
//                    String exchange = prefs.getString("exchange", "Gatecoin");
				Widget widget = new SQLiteHelper(urlAndviews[0].context).getWidget(urlAndviews[0].widgetId);
				String exchange = widget.exchange;
				String currency = widget.currency;
				JSONArray array = null;
				JSONObject object = null;
				String high = null;
				String low = null;
				String price = null;
//				Log.d("etherticker", exchange + " " + currency);
				switch (exchange) {
					case "Gatecoin":
						array = jsono.getJSONArray("tickers");
//                            String currency = prefs.getString("currency", "BTC");
                            /* update views */
						JSONObject ETHBTC = array.getJSONObject(3);
						JSONObject BTCEUR = array.getJSONObject(0);
						JSONObject BTCHKD = array.getJSONObject(1);
						JSONObject BTCUSD = array.getJSONObject(2);
						JSONObject from = ETHBTC;
						JSONObject to = BTCUSD;
						boolean convert = true;
//						Log.d("etherticker", "async -> currency: " + currency);
						switch (currency) {
							case "BTC":
								convert = false;
								low = ETHBTC.getString("low");
								high = ETHBTC.getString("high");
								price = ETHBTC.getString("open");
								break;
							case "HKD":
								to = BTCHKD;
								break;
							case "EUR":
								to = BTCEUR;
								break;
							case "USD":
								to = BTCUSD;
								break;
							default:
								Log.i("switch (currency)", "null");
								break;
						}
						if (convert) {
							low = convert(OPERATION.MULT, from.getString("low"), to.getString("low"));
							high = convert(OPERATION.MULT, from.getString("high"), to.getString("high"));
							price = convert(OPERATION.MULT, from.getString("open"), to.getString("open"));
						}
//                            Log.d("etherticker", "crunched numbers -> low: " + low + ", high: " + high + ", open: " + price + ", currency: " + currency + ", exchange: " + exchange);
						break;
					case "Poloniex":
//						Log.d("etherticker", "Poloniex selected.");
						JSONObject BTCETH = jsono.getJSONObject("BTC_ETH");
						/* BTC ETH goes here */
//						Log.d("etherticker", "BTCETH.toString() -> " + BTCETH.toString());
//						JSONObject USDTETH = jsono.getJSONObject("USDT_ETH");
						from = null;
						to = BTCETH;
						convert = true;
						switch (currency) {
							case "1CR":
								JSONObject BTC1CR = jsono.getJSONObject("BTC_1CR");
								from = BTC1CR;
								break;
							case "ABY":
								JSONObject BTCABY = jsono.getJSONObject("BTC_ABY");
								from = BTCABY;
								break;
							case "ADN":
								JSONObject BTCADN = jsono.getJSONObject("BTC_ADN");
								from = BTCADN;
								break;
							case "ARCH":
								JSONObject BTCARCH = jsono.getJSONObject("BTC_ARCH");
								from = BTCARCH;
								break;
							case "BBR":
								JSONObject BTCBBR = jsono.getJSONObject("BTC_BBR");
								from = BTCBBR;
								break;
							case "BCN":
								JSONObject BTCBCN = jsono.getJSONObject("BTC_BCN");
								from = BTCBCN;
								break;
							case "BELA":
								JSONObject BTCBELA = jsono.getJSONObject("BTC_BELA");
								from = BTCBELA;
								break;
							case "BITS":
								JSONObject BTCBITS = jsono.getJSONObject("BTC_BITS");
								from = BTCBITS;
								break;
							case "BLK":
								JSONObject BTCBLK = jsono.getJSONObject("BTC_BLK");
								from = BTCBLK;
								break;
							case "BLOCK":
								JSONObject BTCBLOCK = jsono.getJSONObject("BTC_BLOCK");
								from = BTCBLOCK;
								break;
							case "BTC":
								convert = false;
								Log.d("etherticker", "BTCETH.toString() -> " + BTCETH.toString());
								low = BTCETH.getString("low24hr");
								high = BTCETH.getString("high24hr");
								price = BTCETH.getString("last");
//								low = String.valueOf(1/Float.valueOf(BTCETH.getString("low24hr")));
//								high = String.valueOf(1 / Float.valueOf(BTCETH.getString("high24hr")));
//								price = String.valueOf(1 / Float.valueOf(BTCETH.getString("last")));
								break;
							case "BTCD":
								JSONObject BTCBTCD = jsono.getJSONObject("BTC_BTCD");
								from = BTCBTCD;
								break;
							case "BTM":
								JSONObject BTCBTM = jsono.getJSONObject("BTC_BTM");
								from = BTCBTM;
								break;
							case "BTS":
								JSONObject BTCBTS = jsono.getJSONObject("BTC_BTS");
								from = BTCBTS;
								break;
							case "BURST":
								JSONObject BTCBURST = jsono.getJSONObject("BTC_BURST");
								from = BTCBURST;
								break;
							case "C2":
								JSONObject BTCC2 = jsono.getJSONObject("BTC_C2");
								from = BTCC2;
								break;
							case "CGA":
								JSONObject BTCCGA = jsono.getJSONObject("BTC_CGA");
								from = BTCCGA;
								break;
							case "CHA":
								JSONObject BTCCHA = jsono.getJSONObject("BTC_CHA");
								from = BTCCHA;
								break;
							case "CLAM":
								JSONObject BTCCLAM = jsono.getJSONObject("BTC_CLAM");
								from = BTCCLAM;
								break;
							case "CNMT":
								JSONObject BTCCNMT = jsono.getJSONObject("BTC_CNMT");
								from = BTCCNMT;
								break;
							case "CURE":
								JSONObject BTCCURE = jsono.getJSONObject("BTC_CURE");
								from = BTCCURE;
								break;
							case "CYC":
								JSONObject BTCCYC = jsono.getJSONObject("BTC_CYC");
								from = BTCCYC;
								break;
							case "DASH":
								JSONObject BTCDASH = jsono.getJSONObject("BTC_DASH");
								from = BTCDASH;
								break;
							case "DGB":
								JSONObject BTCDGB = jsono.getJSONObject("BTC_DGB");
								from = BTCDGB;
								break;
							case "DIEM":
								JSONObject BTCDIEM = jsono.getJSONObject("BTC_DIEM");
								from = BTCDIEM;
								break;
							case "DOGE":
								JSONObject BTCDOGE = jsono.getJSONObject("BTC_DOGE");
								from = BTCDOGE;
								break;
							case "EMC2":
								JSONObject BTCEMC2 = jsono.getJSONObject("BTC_EMC2");
								from = BTCEMC2;
								break;
							case "EXE":
								JSONObject BTCEXE = jsono.getJSONObject("BTC_EXE");
								from = BTCEXE;
								break;
							case "FIBRE":
								JSONObject BTCFIBRE = jsono.getJSONObject("BTC_FIBRE");
								from = BTCFIBRE;
								break;
							case "FLDC":
								JSONObject BTCFLDC = jsono.getJSONObject("BTC_FLDC");
								from = BTCFLDC;
								break;
							case "FLO":
								JSONObject BTCFLO = jsono.getJSONObject("BTC_FLO");
								from = BTCFLO;
								break;
							case "FLT":
								JSONObject BTCFLT = jsono.getJSONObject("BTC_FLT");
								from = BTCFLT;
								break;
							case "GAP":
								JSONObject BTCGAP = jsono.getJSONObject("BTC_GAP");
								from = BTCGAP;
								break;
							case "GEMZ":
								JSONObject BTCGEMZ = jsono.getJSONObject("BTC_GEMZ");
								from = BTCGEMZ;
								break;
							case "GEO":
								JSONObject BTCGEO = jsono.getJSONObject("BTC_GEO");
								from = BTCGEO;
								break;
							case "GMC":
								JSONObject BTCGMC = jsono.getJSONObject("BTC_GMC");
								from = BTCGMC;
								break;
							case "GRC":
								JSONObject BTCGRC = jsono.getJSONObject("BTC_GRC");
								from = BTCGRC;
								break;
							case "GRS":
								JSONObject BTCGRS = jsono.getJSONObject("BTC_GRS");
								from = BTCGRS;
								break;
							case "HIRO":
								JSONObject BTCHIRO = jsono.getJSONObject("BTC_HIRO");
								from = BTCHIRO;
								break;
							case "HUC":
								JSONObject BTCHUC = jsono.getJSONObject("BTC_HUC");
								from = BTCHUC;
								break;
							case "HUGE":
								JSONObject BTCHUGE = jsono.getJSONObject("BTC_HUGE");
								from = BTCHUGE;
								break;
							case "HYP":
								JSONObject BTCHYP = jsono.getJSONObject("BTC_HYP");
								from = BTCHYP;
								break;
							case "HZ":
								JSONObject BTCHZ = jsono.getJSONObject("BTC_HZ");
								from = BTCHZ;
								break;
							case "JLH":
								JSONObject BTCJLH = jsono.getJSONObject("BTC_JLH");
								from = BTCJLH;
								break;
							case "LQD":
								JSONObject BTCLQD = jsono.getJSONObject("BTC_LQD");
								from = BTCLQD;
								break;
							case "LTBC":
								JSONObject BTCLTBC = jsono.getJSONObject("BTC_LTBC");
								from = BTCLTBC;
								break;
							case "LTC":
								JSONObject BTCLTC = jsono.getJSONObject("BTC_LTC");
								from = BTCLTC;
								break;
							case "MAID":
								JSONObject BTCMAID = jsono.getJSONObject("BTC_MAID");
								from = BTCMAID;
								break;
							case "MCN":
								JSONObject BTCMCN = jsono.getJSONObject("BTC_MCN");
								from = BTCMCN;
								break;
							case "MIL":
								JSONObject BTCMIL = jsono.getJSONObject("BTC_MIL");
								from = BTCMIL;
								break;
							case "MINT":
								JSONObject BTCMINT = jsono.getJSONObject("BTC_MINT");
								from = BTCMINT;
								break;
							case "MMC":
								JSONObject BTCMMC = jsono.getJSONObject("BTC_MMC");
								from = BTCMMC;
								break;
							case "MMNXT":
								JSONObject BTCMMNXT = jsono.getJSONObject("BTC_MMNXT");
								from = BTCMMNXT;
								break;
							case "MRS":
								JSONObject BTCMRS = jsono.getJSONObject("BTC_MRS");
								from = BTCMRS;
								break;
							case "MSC":
								JSONObject BTCMSC = jsono.getJSONObject("BTC_MSC");
								from = BTCMSC;
								break;
							case "MYR":
								JSONObject BTCMYR = jsono.getJSONObject("BTC_MYR");
								from = BTCMYR;
								break;
							case "NAUT":
								JSONObject BTCNAUT = jsono.getJSONObject("BTC_NAUT");
								from = BTCNAUT;
								break;
							case "NAV":
								JSONObject BTCNAV = jsono.getJSONObject("BTC_NAV");
								from = BTCNAV;
								break;
							case "NBT":
								JSONObject BTCNBT = jsono.getJSONObject("BTC_NBT");
								from = BTCNBT;
								break;
							case "NEOS":
								JSONObject BTCNEOS = jsono.getJSONObject("BTC_NEOS");
								from = BTCNEOS;
								break;
							case "NMC":
								JSONObject BTCNMC = jsono.getJSONObject("BTC_NMC");
								from = BTCNMC;
								break;
							case "NOBL":
								JSONObject BTCNOBL = jsono.getJSONObject("BTC_NOBL");
								from = BTCNOBL;
								break;
							case "NOTE":
								JSONObject BTCNOTE = jsono.getJSONObject("BTC_NOTE");
								from = BTCNOTE;
								break;
							case "NOXT":
								JSONObject BTCNOXT = jsono.getJSONObject("BTC_NOXT");
								from = BTCNOXT;
								break;
							case "NSR":
								JSONObject BTCNSR = jsono.getJSONObject("BTC_NSR");
								from = BTCNSR;
								break;
							case "NXT":
								JSONObject BTCNXT = jsono.getJSONObject("BTC_NXT");
								from = BTCNXT;
								break;
							case "NXTI":
								JSONObject BTCNXTI = jsono.getJSONObject("BTC_NXTI");
								from = BTCNXTI;
								break;
							case "OPAL":
								JSONObject BTCOPAL = jsono.getJSONObject("BTC_OPAL");
								from = BTCOPAL;
								break;
							case "PIGGY":
								JSONObject BTCPIGGY = jsono.getJSONObject("BTC_PIGGY");
								from = BTCPIGGY;
								break;
							case "PINK":
								JSONObject BTCPINK = jsono.getJSONObject("BTC_PINK");
								from = BTCPINK;
								break;
							case "POT":
								JSONObject BTCPOT = jsono.getJSONObject("BTC_POT");
								from = BTCPOT;
								break;
							case "PPC":
								JSONObject BTCPPC = jsono.getJSONObject("BTC_PPC");
								from = BTCPPC;
								break;
							case "PRC":
								JSONObject BTCPRC = jsono.getJSONObject("BTC_PRC");
								from = BTCPRC;
								break;
							case "PTS":
								JSONObject BTCPTS = jsono.getJSONObject("BTC_PTS");
								from = BTCPTS;
								break;
							case "QBK":
								JSONObject BTCQBK = jsono.getJSONObject("BTC_QBK");
								from = BTCQBK;
								break;
							case "QORA":
								JSONObject BTQORA = jsono.getJSONObject("BTC_QORA");
								from = BTQORA;
								break;
							case "QTL":
								JSONObject BTCQTL = jsono.getJSONObject("BTC_QTL");
								from = BTCQTL;
								break;
							case "RBY":
								JSONObject BTCRBY = jsono.getJSONObject("BTC_RBY");
								from = BTCRBY;
								break;
							case "RDD":
								JSONObject BTCRDD = jsono.getJSONObject("BTC_RDD");
								from = BTCRDD;
								break;
							case "RIC":
								JSONObject BTCRIC = jsono.getJSONObject("BTC_RIC");
								from = BTCRIC;
								break;
							case "SDC":
								JSONObject BTCSDC = jsono.getJSONObject("BTC_SDC");
								from = BTCSDC;
								break;
							case "SILK":
								JSONObject BTCSILK = jsono.getJSONObject("BTC_SILK");
								from = BTCSILK;
								break;
							case "SJCX":
								JSONObject BTCSJCX = jsono.getJSONObject("BTC_SJCX");
								from = BTCSJCX;
								break;
							case "STR":
								JSONObject BTCSTR = jsono.getJSONObject("BTC_STR");
								from = BTCSTR;
								break;
							case "SWARM":
								JSONObject BTCSWARM = jsono.getJSONObject("BTC_SWARM");
								from = BTCSWARM;
								break;
							case "SYNC":
								JSONObject BTCSYNC = jsono.getJSONObject("BTC_SYNC");
								from = BTCSYNC;
								break;
							case "SYS":
								JSONObject BTCSYS = jsono.getJSONObject("BTC_SYS");
								from = BTCSYS;
								break;
							case "UIS":
								JSONObject BTCUIS = jsono.getJSONObject("BTC_UIS");
								from = BTCUIS;
								break;
							case "UNITY":
								JSONObject BTCUNITY = jsono.getJSONObject("BTC_UNITY");
								from = BTCUNITY;
								break;
							case "URO":
								JSONObject BTCURO = jsono.getJSONObject("BTC_URO");
								from = BTCURO;
								break;
							case "USDT":
								JSONObject BTCUSDT = jsono.getJSONObject("BTC_USDT");
								from = BTCUSDT;
								break;
							case "VIA":
								JSONObject BTCVIA = jsono.getJSONObject("BTC_VIA");
								from = BTCVIA;
								break;
							case "VNL":
								JSONObject BTCVNL = jsono.getJSONObject("BTC_VNL");
								from = BTCVNL;
								break;
							case "VRC":
								JSONObject BTCVRC = jsono.getJSONObject("BTC_VRC");
								from = BTCVRC;
								break;
							case "VTC":
								JSONObject BTCVTC = jsono.getJSONObject("BTC_VTC");
								from = BTCVTC;
								break;
							case "WDC":
								JSONObject BTCWDC = jsono.getJSONObject("BTC_WDC");
								from = BTCWDC;
								break;
							case "XAI":
								JSONObject BTCXAI = jsono.getJSONObject("BTC_XAI");
								from = BTCXAI;
								break;
							case "XBC":
								JSONObject BTCXBC = jsono.getJSONObject("BTC_XBC");
								from = BTCXBC;
								break;
							case "XC":
								JSONObject BTCXC = jsono.getJSONObject("BTC_XC");
								from = BTCXC;
								break;
							case "XCH":
								JSONObject BTCXCH = jsono.getJSONObject("BTC_XCH");
								from = BTCXCH;
								break;
							case "XCN":
								JSONObject BTCXCN = jsono.getJSONObject("BTC_XCN");
								from = BTCXCN;
								break;
							case "XCP":
								JSONObject BTCXCP = jsono.getJSONObject("BTC_XCP");
								from = BTCXCP;
								break;
							case "XCR":
								JSONObject BTCXCR = jsono.getJSONObject("BTC_XCR");
								from = BTCXCR;
								break;
							case "XDN":
								JSONObject BTCXDN = jsono.getJSONObject("BTC_XDN");
								from = BTCXDN;
								break;
							case "XDP":
								JSONObject BTCXDP = jsono.getJSONObject("BTC_XDP");
								from = BTCXDP;
								break;
							case "XEM":
								JSONObject BTCXEM = jsono.getJSONObject("BTC_XEM");
								from = BTCXEM;
								break;
							case "XMG":
								JSONObject BTCXMG = jsono.getJSONObject("BTC_XMG");
								from = BTCXMG;
								break;
							case "XMR":
								JSONObject BTCXMR = jsono.getJSONObject("BTC_XMR");
								from = BTCXMR;
								break;
							case "XPB":
								JSONObject BTCXPB = jsono.getJSONObject("BTC_XPB");
								from = BTCXPB;
								break;
							case "XPM":
								JSONObject BTCXPM = jsono.getJSONObject("BTC_XPM");
								from = BTCXPM;
								break;
							case "XRP":
								JSONObject BTCXRP = jsono.getJSONObject("BTC_XRP");
								from = BTCXRP;
								break;
							case "XST":
								JSONObject BTCXST = jsono.getJSONObject("BTC_XST");
								from = BTCXST;
								break;
							case "XUSD":
								JSONObject BTCXUSD = jsono.getJSONObject("BTC_XUSD");
								from = BTCXUSD;
								break;
							case "YACC":
								JSONObject BTCYACC = jsono.getJSONObject("BTC_YACC");
								from = BTCYACC;
								break;
							case "IOC":
								JSONObject BTCIOC = jsono.getJSONObject("BTC_IOC");
								from = BTCIOC;
								break;
							case "INDEX":
								JSONObject BTCINDEX = jsono.getJSONObject("BTC_INDEX");
								from = BTCINDEX;
								break;
							default:
								Log.d("etherticker", "switch(currency) is null");
								break;
						}
						if (convert) {
							low = convert(OPERATION.DIV, from.getString("low24hr"), to.getString("low24hr"));
							high = convert(OPERATION.DIV, from.getString("high24hr"), to.getString("high24hr"));
							price = convert(OPERATION.DIV, from.getString("last"), to.getString("last"));
						}
						break;
					default:
						Log.i("etherticker", "switch (exchange) is null");
						break;
				}
				Log.d("etherticker", low + " " + high + " " + price + " " + currency + " " + exchange);
				urlAndviews[0].views.setTextViewText(R.id.low, low);
				urlAndviews[0].views.setTextViewText(R.id.high, high);
				urlAndviews[0].views.setTextViewText(R.id.price, price);
				urlAndviews[0].views.setTextViewText(R.id.currency, currency);
				urlAndviews[0].views.setTextViewText(R.id.exchange, exchange);
				urlAndviews[0].manager.updateAppWidget(urlAndviews[0].widgetId, urlAndviews[0].views);

				return jsono;
			}


		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	protected void onPostExecute(boolean result) {
            /* eh */
	}

	private String convert(OPERATION op, String from, String to) {
		if (op == OPERATION.MULT)
			return String.valueOf(Float.valueOf(from) * Float.valueOf(to));
		else return String.valueOf(Float.valueOf(from) / Float.valueOf(to));
	}

	private enum OPERATION {MULT, DIV}
}
