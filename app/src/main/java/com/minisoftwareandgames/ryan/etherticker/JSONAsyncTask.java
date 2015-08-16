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
                    Log.d("etherticker", "response: " + jsono.toString());
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
						Log.d("etherticker", "async -> currency: " + currency);
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
						JSONObject BTC1CR = jsono.getJSONObject("BTC_1CR");
						JSONObject BTCABY = jsono.getJSONObject("BTC_ABY");
						JSONObject BTCADN = jsono.getJSONObject("BTC_ADN");
						JSONObject BTCARCH = jsono.getJSONObject("BTC_ARCH");
						JSONObject BTCBBR = jsono.getJSONObject("BTC_BBR");
						JSONObject BTCBCN = jsono.getJSONObject("BTC_BCN");
						JSONObject BTCBELA = jsono.getJSONObject("BTC_BELA");
						JSONObject BTCBITS = jsono.getJSONObject("BTC_BITS");
						JSONObject BTCBLK = jsono.getJSONObject("BTC_BLK");
						JSONObject BTCBLOCK = jsono.getJSONObject("BTC_BLOCK");
						JSONObject BTCBTCD = jsono.getJSONObject("BTC_BTCD");
						JSONObject BTCBTM = jsono.getJSONObject("BTC_BTM");
						JSONObject BTCBTS = jsono.getJSONObject("BTC_BTS");
						JSONObject BTCBURST = jsono.getJSONObject("BTC_BURST");
						JSONObject BTCC2 = jsono.getJSONObject("BTC_C2");
						JSONObject BTCCGA = jsono.getJSONObject("BTC_CGA");
						JSONObject BTCCHA = jsono.getJSONObject("BTC_CHA");
						JSONObject BTCCLAM = jsono.getJSONObject("BTC_CLAM");
						JSONObject BTCCNMT = jsono.getJSONObject("BTC_CNMT");
						JSONObject BTCCURE = jsono.getJSONObject("BTC_CURE");
						JSONObject BTCCYC = jsono.getJSONObject("BTC_CYC");
						JSONObject BTCDASH = jsono.getJSONObject("BTC_DASH");
						JSONObject BTCDGB = jsono.getJSONObject("BTC_DGB");
						JSONObject BTCDIEM = jsono.getJSONObject("BTC_DIEM");
						JSONObject BTCDOGE = jsono.getJSONObject("BTC_DOGE");
						JSONObject BTCEMC2 = jsono.getJSONObject("BTC_EMC2");
						JSONObject BTCEXE = jsono.getJSONObject("BTC_EXE");
						JSONObject BTCFIBRE = jsono.getJSONObject("BTC_FIBRE");
						JSONObject BTCFLDC = jsono.getJSONObject("BTC_FLDC");
						JSONObject BTCFLO = jsono.getJSONObject("BTC_FLO");
						JSONObject BTCFLT = jsono.getJSONObject("BTC_FLT");
						JSONObject BTCGAP = jsono.getJSONObject("BTC_GAP");
						JSONObject BTCGEMZ = jsono.getJSONObject("BTC_GEMZ");
						JSONObject BTCGEO = jsono.getJSONObject("BTC_GEO");
						JSONObject BTCGMC = jsono.getJSONObject("BTC_GMC");
						JSONObject BTCGRC = jsono.getJSONObject("BTC_GRC");
						JSONObject BTCGRS = jsono.getJSONObject("BTC_GRS");
						JSONObject BTCHIRO = jsono.getJSONObject("BTC_HIRO");
						JSONObject BTCHUC = jsono.getJSONObject("BTC_HUC");
						JSONObject BTCHUGE = jsono.getJSONObject("BTC_HUGE");
						JSONObject BTCHYP = jsono.getJSONObject("BTC_HYP");
						JSONObject BTCHZ = jsono.getJSONObject("BTC_HZ");
						JSONObject BTCJLH = jsono.getJSONObject("BTC_JLH");
						JSONObject BTCLQD = jsono.getJSONObject("BTC_LQD");
						JSONObject BTCLTBC = jsono.getJSONObject("BTC_LTBC");
						JSONObject BTCLTC = jsono.getJSONObject("BTC_LTC");
						JSONObject BTCMAID = jsono.getJSONObject("BTC_MAID");
						JSONObject BTCMCN = jsono.getJSONObject("BTC_MCN");
						JSONObject BTCMIL = jsono.getJSONObject("BTC_MIL");
						JSONObject BTCMINT = jsono.getJSONObject("BTC_MINT");
						JSONObject BTCMMC = jsono.getJSONObject("BTC_MMC");
						JSONObject BTCMMNXT = jsono.getJSONObject("BTC_MMNXT");
						JSONObject BTCMRS = jsono.getJSONObject("BTC_MRS");
						JSONObject BTCMSC = jsono.getJSONObject("BTC_MSC");
						JSONObject BTCMYR = jsono.getJSONObject("BTC_MYR");
						JSONObject BTCNAUT = jsono.getJSONObject("BTC_NAUT");
						JSONObject BTCNAV = jsono.getJSONObject("BTC_NAV");
						JSONObject BTCNBT = jsono.getJSONObject("BTC_NBT");
						JSONObject BTCNEOS = jsono.getJSONObject("BTC_NEOS");
						JSONObject BTCNMC = jsono.getJSONObject("BTC_NMC");
						JSONObject BTCNOBL = jsono.getJSONObject("BTC_NOBL");
						JSONObject BTCNOTE = jsono.getJSONObject("BTC_NOTE");
						JSONObject BTCNOXT = jsono.getJSONObject("BTC_NOXT");
						JSONObject BTCNSR = jsono.getJSONObject("BTC_NSR");
						JSONObject BTCNXT = jsono.getJSONObject("BTC_NXT");
						JSONObject BTCNXTI = jsono.getJSONObject("BTC_NXTI");
						JSONObject BTCOPAL = jsono.getJSONObject("BTC_OPAL");
						JSONObject BTCPIGGY = jsono.getJSONObject("BTC_PIGGY");
						JSONObject BTCPINK = jsono.getJSONObject("BTC_PINK");
						JSONObject BTCPOT = jsono.getJSONObject("BTC_POT");
						JSONObject BTCPPC = jsono.getJSONObject("BTC_PPC");
						JSONObject BTCPRC = jsono.getJSONObject("BTC_PRC");
						JSONObject BTCPTS = jsono.getJSONObject("BTC_PTS");
						JSONObject BTCQBK = jsono.getJSONObject("BTC_QBK");
						JSONObject BTQORA = jsono.getJSONObject("BTC_QORA");
						JSONObject BTCQTL = jsono.getJSONObject("BTC_QTL");
						JSONObject BTCRBY = jsono.getJSONObject("BTC_RBY");
						JSONObject BTCRDD = jsono.getJSONObject("BTC_RDD");
						JSONObject BTCTIC = jsono.getJSONObject("BTC_RIC");
						JSONObject BTCSDC = jsono.getJSONObject("BTC_SDC");
						JSONObject BTCSILK = jsono.getJSONObject("BTC_SILK");
						JSONObject BTCSJCX = jsono.getJSONObject("BTC_SJCX");
						JSONObject BTCSTR = jsono.getJSONObject("BTC_STR");
						JSONObject BTCSWARM = jsono.getJSONObject("BTC_SWARM");
						JSONObject BTCSYNC = jsono.getJSONObject("BTC_SYNC");
						JSONObject BTCSYS = jsono.getJSONObject("BTC_SYS");
						JSONObject BTCUIS = jsono.getJSONObject("BTC_UIS");
						JSONObject BTCUNITY = jsono.getJSONObject("BTC_UNITY");
						BTCUSD = jsono.getJSONObject("BTC_USD");
						JSONObject BTCURO = jsono.getJSONObject("BTC_URO");
						JSONObject BTCVIA = jsono.getJSONObject("BTC_VIA");
						JSONObject BTCVNL = jsono.getJSONObject("BTC_VNL");
						JSONObject BTCVRC = jsono.getJSONObject("BTC_VRC");
						JSONObject BTCVTC = jsono.getJSONObject("BTC_VTC");
						JSONObject BTCWDC = jsono.getJSONObject("BTC_WDC");
						JSONObject BTCXAI = jsono.getJSONObject("BTC_XAI");
						JSONObject BTCXBC = jsono.getJSONObject("BTC_XBC");
						JSONObject BTCXC = jsono.getJSONObject("BTC_XC");
						JSONObject BTCXCH = jsono.getJSONObject("BTC_XCH");
						JSONObject BTCXCN = jsono.getJSONObject("BTC_XCN");
						JSONObject BTCXCP = jsono.getJSONObject("BTC_XCP");
						JSONObject BTCXCR = jsono.getJSONObject("BTC_XCR");
						JSONObject BTCXDN = jsono.getJSONObject("BTC_XDN");
						JSONObject BTCXDP = jsono.getJSONObject("BTC_XDP");
						JSONObject BTCXEM = jsono.getJSONObject("BTC_XEM");
						JSONObject BTCXMG = jsono.getJSONObject("BTC_XMG");
						JSONObject BTCXMR = jsono.getJSONObject("BTC_XMR");
						JSONObject BTCXPB = jsono.getJSONObject("BTC_XPB");
						JSONObject BTCXPM = jsono.getJSONObject("BTC_XPM");
						JSONObject BTCXRP = jsono.getJSONObject("BTC_XRP");
						JSONObject BTCXST = jsono.getJSONObject("BTC_XST");
						JSONObject BTCXUSD = jsono.getJSONObject("BTC_XUSD");
						JSONObject BTCYACC = jsono.getJSONObject("BTC_YACC");
						JSONObject BTCIOC = jsono.getJSONObject("BTC_IOC");
						JSONObject BTCINDEX = jsono.getJSONObject("BTC_INDEX");
						JSONObject BTCETH = jsono.getJSONObject("BTC_ETH");
						Log.d("etherticker", "BTCETH.toString() -> " + BTCETH.toString());
						JSONObject USDTETH = jsono.getJSONObject("USDT_ETH");
						from = BTCETH;
						to = BTCETH;
						convert = true;
						switch (currency) {
							case "1CR":
								from = BTC1CR;
								break;
							case "ABY":
								from = BTCABY;
								break;
							case "ADN":
								from = BTCADN;
								break;
							case "ARCH":
								from = BTCARCH;
								break;
							case "BBR":
								from = BTCBBR;
								break;
							case "BCN":
								from = BTCBCN;
								break;
							case "BELA":
								from = BTCBELA;
								break;
							case "BITS":
								from = BTCBITS;
								break;
							case "BLK":
								from = BTCBLK;
								break;
							case "BLOCK":
								from = BTCBLOCK;
								break;
							case "BTC":
								convert = false;
								low = BTCETH.getString("low24hr");
								high = BTCETH.getString("high24hr");
								price = BTCETH.getString("last");
//								low = String.valueOf(1/Float.valueOf(BTCETH.getString("low24hr")));
//								high = String.valueOf(1 / Float.valueOf(BTCETH.getString("high24hr")));
//								price = String.valueOf(1 / Float.valueOf(BTCETH.getString("last")));
								break;
							case "BTCD":
								from = BTCBTCD;
								break;
							case "BTM":
								from = BTCBTM;
								break;
							case "BTS":
								from = BTCBTS;
								break;
							case "BURST":
								from = BTCBURST;
								break;
							case "C2":
								from = BTCC2;
								break;
							case "CGA":
								from = BTCCGA;
								break;
							case "CHA":
								from = BTCCHA;
								break;
							case "CLAM":
								break;
							case "CNMT":
								break;
							case "CURE":
								break;
							case "CYC":
								break;
							case "DASH":
								break;
							case "DGB":
								break;
							case "DIEM":
								break;
							case "DOGE":
								break;
							case "EMC2":
								break;
							case "EXE":
								break;
							case "FIBRE":
								break;
							case "FLDC":
								break;
							case "FLO":
								break;
							case "FLT":
								break;
							case "GAP":
								break;
							case "GEMZ":
								break;
							case "GEO":
								break;
							case "GMC":
								break;
							case "GRC":
								break;
							case "GRS":
								break;
							case "HIRO":
								break;
							case "HUC":
								break;
							case "HUGE":
								break;
							case "HYP":
								break;
							case "HZ":
								break;
							case "JLH":
								break;
							case "LQD":
								break;
							case "LTBC":
								break;
							case "LTC":
								break;
							case "MAID":
								break;
							case "MCN":
								break;
							case "MIL":
								break;
							case "MINT":
								break;
							case "MMC":
								break;
							case "MMNXT":
								break;
							case "MRS":
								break;
							case "MSC":
								break;
							case "MYR":
								break;
							case "NAUT":
								break;
							case "NAV":
								break;
							case "NBT":
								break;
							case "NEOS":
								break;
							case "NMC":
								break;
							case "NOBL":
								break;
							case "NOTE":
								break;
							case "NOXT":
								break;
							case "NSR":
								break;
							case "NXT":
								break;
							case "NXTI":
								break;
							case "OPAL":
								break;
							case "PIGGY":
								break;
							case "PINK":
								break;
							case "POT":
								break;
							case "PPC":
								break;
							case "PRC":
								break;
							case "PTS":
								break;
							case "QBK":
								break;
							case "QORA":
								break;
							case "QTL":
								break;
							case "RBY":
								break;
							case "RDD":
								break;
							case "RIC":
								break;
							case "SDC":
								break;
							case "SILK":
								break;
							case "SJCX":
								break;
							case "STR":
								break;
							case "SWARM":
								break;
							case "SYNC":
								break;
							case "SYS":
								break;
							case "UIS":
								break;
							case "UNITY":
								break;
							case "URO":
								break;
							case "VIA":
								break;
							case "VNL":
								break;
							case "VRC":
								break;
							case "VTC":
								break;
							case "WDC":
								break;
							case "XAI":
								break;
							case "XBC":
								break;
							case "XC":
								break;
							case "XCH":
								break;
							case "XCN":
								break;
							case "XCP":
								break;
							case "XCR":
								break;
							case "XDN":
								break;
							case "XDP":
								break;
							case "XEM":
								break;
							case "XMG":
								break;
							case "XMR":
								break;
							case "XPB":
								break;
							case "XPM":
								break;
							case "XRP":
								break;
							case "XST":
								break;
							case "XUSD":
								break;
							case "YACC":
								break;
							case "IOC":
								break;
							case "INDEX":
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
//                            Log.i("switch (exchange)", "null");
						break;
				}
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
