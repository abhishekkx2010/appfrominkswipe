package util;

import android.util.Base64;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;

import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import io.jsonwebtoken.impl.Base64Codec;


public class ServiceFacade {
	 public static String response2=null;
	 static String	response3=null;

	public ServiceFacade() {
	}



	public static JSONObject getServiceResponseparams(String serviceUrl,Hashtable<String, String> params) {
		HttpClient httpClient = null;
		InputStream is = null;
		JSONObject jObj = null;
		String jsonResp = "";
		HttpResponse httpResponse = null;
		HttpGet getUrl ;
		// Making HTTP request

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

		Enumeration<String> paramsEnum = params.keys();

		while (paramsEnum.hasMoreElements()) {
			String key = paramsEnum.nextElement();
			String value = params.get(key);
			Log.i("Key", "Key=Value" + key + "=" + value);
			nameValuePairs.add(new BasicNameValuePair(key, value));
		}

		String paramsString = URLEncodedUtils.format(nameValuePairs, "UTF-8");
		try {
			httpClient = new DefaultHttpClient();
			getUrl = new HttpGet(serviceUrl + "?" + paramsString);

			httpResponse = httpClient.execute(getUrl);
			HttpEntity httpEntity = httpResponse.getEntity();
			is = httpEntity.getContent();
			Log.d("hii","getiss"+is+"==="+is.toString());
		} catch (Exception exc) {
			exc.printStackTrace();
		}

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			jsonResp = sb.toString();
			Log.i("", "Syncronization Response : " + jsonResp);
			//	jObj = new JSONObject(jsonResp);
		} catch (Exception exc) {
			exc.printStackTrace();
		}

		return jObj;
	}

	public static JSONObject getServiceResponse(String serviceUrl,
												Hashtable<String, String> params) {
		HttpClient httpClient = null;
		InputStream is = null;
		JSONObject jObj = null;
		String jsonResp = "";
		HttpResponse httpResponse = null;

		HttpPost postMethod = new HttpPost(serviceUrl);
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

		Enumeration<String> paramsEnum = params.keys();

		while (paramsEnum.hasMoreElements()) {
			String key = paramsEnum.nextElement();
			String value = params.get(key);
			Log.i("Key", "Key=Value" + key + "=" + value);
			nameValuePairs.add(new BasicNameValuePair(key, value));
		}
		// Making HTTP request
		try {
			postMethod.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			httpClient = new DefaultHttpClient();
			httpResponse = httpClient.execute(postMethod);
			HttpEntity httpEntity = httpResponse.getEntity();
			is = httpEntity.getContent();
		} catch (Exception exc) {
			exc.printStackTrace();
		}

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			jsonResp = sb.toString();
			Log.i("", params.get("table") + " Response : " + jsonResp);

			jObj = new JSONObject(jsonResp);
		} catch (Exception exc) {
			exc.printStackTrace();
			return null;
		}

		return jObj;
	}

	public static  String JwtHeadernormal(String userid) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {



		try{


			JSONObject jwtheader = new JSONObject();

			jwtheader.put("alg", "HS256");
			jwtheader.put("typ", "JWT");


			LoggerGeneral.info("header----" + jwtheader);


			JSONObject jwtPayload = new JSONObject();

			jwtPayload.put("userid",userid);
			jwtPayload.put("username","guest");

			LoggerGeneral.info("payload----" + jwtPayload);

			byte[] ddata1 = jwtheader.toString().getBytes("UTF-8");
			String bbasealg = Base64Codec.BASE64URL.encode(ddata1);

			String newheader =  Base64.encodeToString(ddata1, Base64.NO_WRAP);

			byte[] dddata2 = jwtPayload.toString().getBytes("UTF-8");
			String bbasejwt = Base64Codec.BASE64URL.encode(dddata2);


		/*	int padding = jwtPayload.toString().length()/4;
			int padding2 = bbasejwt.length()/4;


			if(padding2==2){


				bbasejwt.concat("==");
			}
			if(padding2==3){
				bbasejwt.concat("=");

			}*/

			String newpayload =  Base64.encodeToString(dddata2, Base64.NO_WRAP);

			Log.d("hiiii", "show22-headerencoded-" + bbasealg + "--------payloadencoded------" + bbasejwt);


		//	LoggerGeneral.info("show33--headerencoded--" + newheader + "-----" + newpayload);

			String keyToEncode = bbasealg+"."+bbasejwt;

			String newkeytoencode = newheader+"."+newpayload;


			//	String secret = "FSD1F6SDF11G61H6G16I1YU1IUOL161F6AS1F96RHG41J16GF1B6FD16D4G69ER4";

		//	String secret = "DSFH27354732YDX98HEDF76WETFR8723Y8";

			String secret = "FSD1F6SDF11G61H6G16I1YU1IUOL161F6AS1F96RHG41J16GF1B6FD16D4G69ER4";
			sha1(keyToEncode, secret);

	     	sha1(newkeytoencode, secret);



			String bserhash = sha1(keyToEncode, secret);

			String bserhashnew = sha1(newkeytoencode, secret);

			response2 = keyToEncode+"."+bserhash;

	    	response3 = newkeytoencode+"."+bserhashnew;

			LoggerGeneral.info("Jwtfinal-----" + response2);
			LoggerGeneral.info("Jwtfinalnew-----" + response3);

		}
		catch (Exception e){
			System.out.println("Error");
		}
		return response3;
	}

	public static  String JwtHeaderuser(String userid) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {



		try{


			JSONObject jwtheader = new JSONObject();

			jwtheader.put("alg","HS256");
			jwtheader.put("typ","JWT");


			LoggerGeneral.info("header----" + jwtheader);


			JSONObject jwtPayload = new JSONObject();

			jwtPayload.put("userid",userid);
			jwtPayload.put("username","guest");

			LoggerGeneral.info("payload----" + jwtPayload);

			byte[] ddata1 = jwtheader.toString().getBytes("UTF-8");
			String bbasealg = Base64Codec.BASE64URL.encode(ddata1);

		//	String newheader =  Base64.encodeToString(ddata1, Base64.DEFAULT);

			byte[] dddata2 = jwtPayload.toString().getBytes("UTF-8");
			String bbasejwt = Base64Codec.BASE64URL.encode(dddata2);

		//	String newpayload =  Base64.encodeToString(dddata2, Base64.NO_PADDING);

			Log.d("hiiii", "show22-headerencoded-" + bbasealg + "--------payloadencoded------" + bbasejwt);


		//	LoggerGeneral.info("show33--headerencoded--" + newheader + "-----" + newpayload);

			String keyToEncode = bbasealg+"."+bbasejwt;

		//	String newkeytoencode = newheader+"."+newpayload;

			//	String secret = "FSD1F6SDF11G61H6G16I1YU1IUOL161F6AS1F96RHG41J16GF1B6FD16D4G69ER4";

			String secret = "FSD1F6SDF11G61H6G16I1YU1IUOL161F6AS1F96RHG41J16GF1B6FD16D4G69ER4";
			sha1(keyToEncode, secret);

		//	sha1(newkeytoencode, secret);



			String bserhash = sha1(keyToEncode, secret);



			response2 = keyToEncode+"."+bserhash;



			LoggerGeneral.info("Jwtfinal-----" + response2);


		}
		catch (Exception e){
			System.out.println("Error");
		}
		return response2;
	}

	public static  String JwtHeader(String userid) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {



		try{

			JSONObject jwtheader = new JSONObject();

			jwtheader.put("alg","HS256");
			jwtheader.put("typ","JWT");


			LoggerGeneral.info("header----" + jwtheader);


			JSONObject jwtPayload = new JSONObject();

			jwtPayload.put("userid","0");
			jwtPayload.put("username","guest");

			LoggerGeneral.info("payload----" + jwtPayload);

			byte[] ddata1 = jwtheader.toString().getBytes("UTF-8");
			String bbasealg = Base64Codec.BASE64URL.encode(ddata1);

			String newheader =  Base64.encodeToString(ddata1, Base64.NO_PADDING);

			byte[] dddata2 = jwtPayload.toString().getBytes("UTF-8");
			String bbasejwt = Base64Codec.BASE64URL.encode(dddata2);

			String newpayload =  Base64.encodeToString(dddata2, Base64.NO_PADDING);

			Log.d("hiiii", "show22-headerencoded-" + bbasealg + "--------payloadencoded------" + bbasejwt);


			LoggerGeneral.info("show33--headerencoded--" + newheader + "-----" + newpayload);

			String keyToEncode = bbasealg+"."+bbasejwt;

			String newkeytoencode = newheader+"."+newpayload;

		//	String secret = "FSD1F6SDF11G61H6G16I1YU1IUOL161F6AS1F96RHG41J16GF1B6FD16D4G69ER4";

			String secret = "DSFH27354732YDX98HEDF76WETFR8723Y8";
			sha1(keyToEncode, secret);

			sha1(newkeytoencode, secret);



			String bserhash = sha1(keyToEncode, secret);



			response2 = keyToEncode+"."+bserhash;



			LoggerGeneral.info("Jwtfinal-----" + response2);


		}
		catch (Exception e){
			System.out.println("Error");
		}
		return response2;
	}



	public static String sha1(String s, String keyString) throws
			UnsupportedEncodingException, NoSuchAlgorithmException,
			InvalidKeyException {

		SecretKeySpec key = new SecretKeySpec((keyString).getBytes("UTF-8"), "HmacSHA256");
		Mac mac = Mac.getInstance("HmacSHA256");
		mac.init(key);



		byte[] bytes = mac.doFinal(s.getBytes("UTF-8"));


		return new String(Base64.encodeToString(bytes,Base64.NO_WRAP));
	}
//	eyAiYWxnIjogIkhTMjU2IiwgInR5cCI6ICJKV1QiIH0=.eyAidXNlcmlkIjogIjEiLCAidXNlcm5hbWUiOiAiQWppbmt5YSIgfQ==.K3spRppyKlc///+D1GXTNXo5/wE+mXYslGyJhY5nQL0=




	/*public  static String NSha1(String s,String keyString) throws
			UnsupportedEncodingException, NoSuchAlgorithmException,
			InvalidKeyException, ShortBufferException {


		String secret =keyString;
		String message = s;

		Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
		SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
		sha256_HMAC.init(secret_key);

		String hash = Base64.encodeToString(sha256_HMAC.doFinal(message.getBytes(),Base64.DEFAULT));
		System.out.println(hash);


		return hash;

	}*/
	public static String encode(String key, String data) throws Exception {
		Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
		SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
		sha256_HMAC.init(secret_key);
		byte[] bytes = sha256_HMAC.doFinal(data.getBytes("UTF-8"));
//		return Hex.encodeHexString(sha256_HMAC.doFinal(data.getBytes("UTF-8")));
		return Base64.encodeToString(bytes, Base64.DEFAULT);
	}

	public static JSONObject getResponsJsonParams(String serviceUrl,JSONObject json){

		JSONObject jObj = null;
		String jsonResp = "";
	/*	HttpParams httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters, 5000);  // allow 5 seconds to create the server connection
		HttpConnectionParams.setSoTimeout(httpParameters, 5000);*/
		HttpClient hc = new DefaultHttpClient();
		String message;
		InputStream is = null;
		HttpPost p = new HttpPost(serviceUrl);
		try {
			message = json.toString();
			p.setEntity(new StringEntity(message, "UTF8"));
			p.setHeader("Content-type", "application/json");
			p.addHeader("Jwt-Auth","eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyaWQiOiIxIiwidXNlcm5hbWUiOiJLZWRhciBHYW5kcmUifQ==.E2kIA/8PIG0gSwLMP08eg0tLbzMUyeSmnzzTffjCHaE=");
			HttpResponse resp = hc.execute(p);
			HttpEntity httpEntity = resp.getEntity();
			is = httpEntity.getContent();
			if (resp != null) {
				if (resp.getStatusLine().getStatusCode() == 204) {
				}
			}
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(
						is, "iso-8859-1"), 8);
				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
				is.close();
				jsonResp = sb.toString();
				jObj = new JSONObject(jsonResp);
			} catch (Exception exc) {
				exc.printStackTrace();

			}
			Log.d("Status line", "" + resp.getStatusLine().getStatusCode()+"====="+resp.toString()+"====="+jsonResp);
			return jObj;

		} catch (Exception e) {
			e.printStackTrace();

		}
		return jObj;
	}
	public static JSONArray getServiceResponsearr(String serviceUrl) {
		HttpClient httpClient = null;
		InputStream is = null;
		JSONArray jObj = null;
		String jsonResp = "";
		HttpResponse httpResponse = null;
		HttpGet getUrl = new HttpGet(serviceUrl);
		// Making HTTP request
		getUrl.setHeader("Content-type", "application/json");
		getUrl.addHeader("Jwt-Auth","eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyaWQiOiIxIiwidXNlcm5hbWUiOiJLZWRhciBHYW5kcmUifQ==.E2kIA/8PIG0gSwLMP08eg0tLbzMUyeSmnzzTffjCHaE=");

		try {
			httpClient = new DefaultHttpClient();
			httpResponse = httpClient.execute(getUrl);
			HttpEntity httpEntity = httpResponse.getEntity();
			is = httpEntity.getContent();
		} catch (Exception exc) {
			exc.printStackTrace();
		}

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			jsonResp = sb.toString();
			Log.i("", "Syncronization Response : " + jsonResp);
			jObj = new JSONArray(jsonResp);
		} catch (Exception exc) {
			exc.printStackTrace();
		}

		return jObj;
	}


	public static String getResponsJsonParamsJwt(String serviceUrl,JSONObject json,String str){

		String jObj = null;
		String jsonResp = "";
		HttpClient hc = new DefaultHttpClient();
		String message;
		InputStream is = null;
		HttpPost p = new HttpPost(serviceUrl);
		try {
			message = json.toString();
			p.setEntity(new StringEntity(message, "UTF8"));
			Log.d("ksjshdjksadh", "alhsdlkashd" + str);
			//p.setHeader("Jwt-Auth", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyaWQiOiIxIiwidXNlcm5hbWUiOiJLZWRhciBHYW5kcmUifQ==.1U48F3qWjt7mgvwQe0GrjEtZQO6mHeYmJwhzIyBqGV4=");
			p.setHeader("Jwt-Auth",str);

			HttpResponse resp = hc.execute(p);
			HttpEntity httpEntity = resp.getEntity();
			is = httpEntity.getContent();
			if (resp != null) {
				if (resp.getStatusLine().getStatusCode() == 204) {
				}
			}
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(
						is, "iso-8859-1"), 8);
				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
				is.close();
				jsonResp = sb.toString();
				jObj = new String(jsonResp);
			} catch (Exception exc) {
				exc.printStackTrace();

			}
			Log.d("Status line", "" + resp.getStatusLine().getStatusCode()+"====="+resp.toString()+"====="+jsonResp);
			return jObj;

		} catch (Exception e) {
			e.printStackTrace();

		}
		return jObj;
	}

	public static JSONObject getResponsJsonParamsJwt2(String serviceUrl,JSONObject json,String str){

		JSONObject jObj = null;
		String jsonResp = "";
		HttpClient hc = new DefaultHttpClient();
		String message;
		InputStream is = null;
		HttpPost p = new HttpPost(serviceUrl);
		try {
			message = json.toString();
			p.setEntity(new StringEntity(message, "UTF8"));
			Log.d("ksjshdjksadh", "alhsdlkashd" + str);
			//p.setHeader("Jwt-Auth", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyaWQiOiIxIiwidXNlcm5hbWUiOiJLZWRhciBHYW5kcmUifQ==.1U48F3qWjt7mgvwQe0GrjEtZQO6mHeYmJwhzIyBqGV4=");
		//	p.setHeader("Jwt-Auth",str);

			p.setHeader("Content-type", "application/json");
		//	p.addHeader("Jwt-Auth","eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyaWQiOiIxIiwidXNlcm5hbWUiOiJLZWRhciBHYW5kcmUifQ==.E2kIA/8PIG0gSwLMP08eg0tLbzMUyeSmnzzTffjCHaE=");
			p.addHeader("Jwt-Auth",str);

			HttpResponse resp = hc.execute(p);
			HttpEntity httpEntity = resp.getEntity();
			is = httpEntity.getContent();
			if (resp != null) {
				if (resp.getStatusLine().getStatusCode() == 204) {
				}
			}
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(
						is, "iso-8859-1"), 8);
				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
				is.close();
				jsonResp = sb.toString();
				jObj = new JSONObject(jsonResp);
			} catch (Exception exc) {
				exc.printStackTrace();

			}
			Log.d("Status line", "" + resp.getStatusLine().getStatusCode()+"====="+resp.toString()+"====="+jsonResp);
			return jObj;

		} catch (Exception e) {
			e.printStackTrace();

		}
		return jObj;
	}
	public static JSONObject getServiceResponsejson(String serviceUrl,
													Hashtable<String, String> params) {
		HttpClient httpClient = null;
		InputStream is = null;
		JSONObject jObj = null;
		String jsonResp = "";
		HttpResponse httpResponse = null;

		HttpPost postMethod = new HttpPost(serviceUrl);
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

		Enumeration<String> paramsEnum = params.keys();

		while (paramsEnum.hasMoreElements()) {
			String key = paramsEnum.nextElement();
			String value = params.get(key);
			Log.i("Key", "Key=Value" + key + "=" + value);
			nameValuePairs.add(new BasicNameValuePair(key, value));
		}
		// Making HTTP request
		try {
			postMethod.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			httpClient = new DefaultHttpClient();
			httpResponse = httpClient.execute(postMethod);
			HttpEntity httpEntity = httpResponse.getEntity();
			is = httpEntity.getContent();
		} catch (Exception exc) {
			exc.printStackTrace();
		}

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			jsonResp = sb.toString();
			Log.i("", params.get("table") + " Response : " + jsonResp);

			jObj = new JSONObject(jsonResp);
		} catch (Exception exc) {
			exc.printStackTrace();
			return null;
		}

		return jObj;
	}

	public static JSONObject getServiceResponse(String serviceUrl) {
		HttpClient httpClient = null;
		InputStream is = null;
		JSONObject jObj = null;
		String jsonResp = "";
		HttpResponse httpResponse = null;
		HttpGet getUrl = new HttpGet(serviceUrl);
		// Making HTTP request
		try {
			httpClient = new DefaultHttpClient();
			httpResponse = httpClient.execute(getUrl);
			HttpEntity httpEntity = httpResponse.getEntity();
			is = httpEntity.getContent();
		} catch (Exception exc) {
			exc.printStackTrace();
		}

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			jsonResp = sb.toString();
			Log.i("", "Syncronization Response : " + jsonResp);
			jObj = new JSONObject(jsonResp);
		} catch (Exception exc) {
			exc.printStackTrace();
		}

		return jObj;
	}



}














		/*	JSONObject jwtheader = new JSONObject();

			jwtheader.put("alg","HS256");
			jwtheader.put("typ","JWT");


			String alg = jwtheader.getString("alg");
			String jwt = jwtheader.getString("typ");

			String new1 =alg.concat(jwt);



			String one  = Base64.encodeToString(new1.getBytes(), Base64.DEFAULT);


			String al  =  Base64.encodeToString(alg.getBytes(), Base64.DEFAULT);


			String jw  =  Base64.encodeToString(jwt.getBytes(), 0);


			byte[] decodeValue1 = Base64.decode(Base64.encodeToString(new1.getBytes(), Base64.DEFAULT), Base64.DEFAULT);


			JSONObject jwtPayload = new JSONObject();

			jwtPayload.put("userid","1");
			jwtPayload.put("username","Ajinkya");


			String userid0 = jwtPayload.getString("userid");

			String usern =  jwtPayload.getString("username");


			String sample= "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpYXQiOjE0MTY5MjkxMDksImp0aSI6ImFhN2Y4ZDBhOTVjIiwic2NvcGVzIjpbInJlcG8iLCJwdWJsaWNfcmVwbyJdfQ.XCEwpBGvOLma4TCoh36FU7XhUbcskygS81HE1uHLf0E";

			Key key = MacProvider.generateKey();

			String s1 = Jwts.builder().setSubject(new1).signWith(SignatureAlgorithm.HS256, key).compact();




			String new2 = userid0.concat(usern);



			String two = Base64.encodeToString(new2.getBytes(), Base64.DEFAULT);

			byte[] decodeValue2 = Base64.decode(Base64.encodeToString(new2.getBytes(), Base64.DEFAULT), Base64.DEFAULT);

			byte[] decodeValue3 = Base64.decode(Base64.encodeToString(sample.getBytes(), Base64.DEFAULT), Base64.DEFAULT);



			LoggerGeneral.info("show keys---"+one+"----"+al+"----"+jw+"---"+new String(decodeValue1)+"---"+new String(decodeValue2)+"---"+decodeValue3+"----"+s1);

			String three = one.concat(".").concat(two);


			String text1 = "HS256";
			byte[] data1 = text1.getBytes("UTF-8");
			String base641 = Base64.encodeToString(data1, Base64.DEFAULT);

			String text2 = "JWT";
			byte[] data2 = text2.getBytes("UTF-8");
			String base642 = Base64.encodeToString(data2, Base64.DEFAULT);


			String text3 = "1";

			byte[] data3 = text3.getBytes("UTF-8");


			String base643 = Base64.encodeToString(data3, Base64.DEFAULT);


			String text4 = "Ajinkya";
			byte[] data4 = text4.getBytes("UTF-8");
			String base644 = Base64.encodeToString(data4, Base64.DEFAULT);

			String jwtHeaderEncoded= base641+base642;
			String jwtPayloadEncoded= base643+base644;


			String keyToEncode = jwtHeaderEncoded +"."+jwtPayloadEncoded;

			String secret = "FSD1F6SDF11G61H6G16I1YU1IUOL161F6AS1F96RHG41J16GF1B6FD16D4G69ER4";

			sha1(keyToEncode,secret);

			String serverHash = sha1(keyToEncode,secret);

			String serverHashEncoded  = Base64.encodeToString(serverHash.getBytes(), 0);

			String response = keyToEncode+"."+serverHash;

			Log.d("hiii","keygen..."+response);*/

			/*Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
			SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
			sha256_HMAC.init(secret_key);

			String hash = Base64.encodeToString(sha256_HMAC.doFinal(keyToEncode.getBytes()), 0);


			Key key = MacProvider.generateKey();

			String s = Jwts.builder().setSubject(hash).signWith(SignatureAlgorithm.HS256, key).compact();
			System.out.println(hash);


			String serverHashEncoded = Base64.encodeToString(keyToEncode.getBytes(),0);

			String response = keyToEncode.concat(".").concat(serverHashEncoded);


			LoggerGeneral.info("show hash"+hash+"===="+userid+"==="+ new String(decodeValue)+"==="+s+"==="+response);



			===================================================

			String alg = jwtheader.getString("alg");
			String jwt = jwtheader.getString("typ");


			byte[] data1 = alg.getBytes("UTF-8");
			String basealg = Base64Codec.BASE64URL.encode(data1);

			byte[] data2 = jwt.getBytes("UTF-8");
			String basejwt = Base64Codec.BASE64URL.encode(data2);


			String header1 = alg+jwt;

			byte[] data3 = header1.getBytes("UTF-8");
			String baseheader1 = Base64Codec.BASE64URL.encode(data3);

			String header2 = alg.concat(jwt);

			byte[] data4 = header2.getBytes("UTF-8");
			String baseheader2 = Base64Codec.BASE64URL.encode(data4);


			Log.d("hii", "ajinkya===" + basealg + "..====.." + basejwt + "..===.." + baseheader1 + "..===.." + baseheader2 + "===" + bbasealg + "===" + bbasejwt);



			String userid0 = jwtPayload.getString("userid");

			String usern =  jwtPayload.getString("username");


			*/