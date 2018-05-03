package com.example.punny.test.fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.punny.test.MainActivity;
import com.example.punny.test.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class Login extends Fragment {

    private EditText et_reg, et_pass;

    private Handler handler;

    public Login() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        handler = new Handler();
        final Button reg = (Button)view.findViewById(R.id.regBtn);
        Button log = (Button)view.findViewById(R.id.logBtn);
        et_reg = (EditText)view.findViewById(R.id.et_reg);
        et_pass = (EditText)view.findViewById(R.id.et_password);

        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!et_reg.getText().toString().equals("") && !et_pass.getText().toString().equals("")) {
                    String ureg = et_reg.getText().toString();
                    String upass = getSHA1HashData(et_pass.getText().toString());
                    _login(ureg,upass);
                } else {
                    Toast.makeText(getContext(),"Нэвтрэх өгөгдлийг оруулна уу.",Toast.LENGTH_SHORT).show();
                }
            }
        });

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                Register register = new Register();
                fragmentManager.beginTransaction().replace(R.id.content_frame,register).commit();
            }
        });

        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Нэвтрэх");
        return view;
    }

    private void _login(String username, String password) {

        final Timer[] timer = new Timer[1];
        final TimerTask[] timerTask = new TimerTask[1];
        final ProgressDialog dialog = new ProgressDialog(getContext());
        AsyncTask<String, String, String> task = new AsyncTask<String, String, String>() {

            @Override
            protected void onPreExecute() {
                dialog.setMessage("Таны мэдээллийг шалгаж байна. Түр хүлээнэ үү.");
                dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                dialog.show();
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(String... params) {
                try {
                    URL url = new URL("http://192.168.1.2/app/login.php");
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setDoOutput(true);
                    urlConnection.setDoInput(true);

                    OutputStream outputStream = urlConnection.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                    String post = "register=" + params[0] + "&" + "password=" + params[1];
                    writer.write(post);

                    outputStream.flush();
                    writer.close();

                    InputStream inputStream = urlConnection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));

                    String result = "";
                    String resp;
                    while ((resp = reader.readLine()) != null) {
                        result += resp;
                    }

                    inputStream.close();
                    reader.close();
                    return result;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(final String s) {

                timer[0] = new Timer();
                timerTask[0] = new TimerTask() {
                    @Override
                    public void run() {
                       handler.post(new Runnable() {
                           @Override
                           public void run() {
                               if (s != null) {
                                   try {
                                       JSONObject jsonObject = new JSONObject(s);
                                       Log.d("Status",""+jsonObject.toString());
                                       if (jsonObject.getBoolean("status")) {
                                           // Toast.makeText(getContext(),"Амжилттай нэвтэрлээ.",Toast.LENGTH_SHORT).show();
                                           Lessons fragment = new Lessons();
                                           FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                           fragmentManager.beginTransaction().replace(R.id.content_frame,fragment).commit();
                                           dialog.dismiss();
                                           timer[0].cancel();
                                       } else {
                                           dialog.dismiss();
                                           timer[0].cancel();
                                           Toast.makeText(getContext(),"Таны регистер дугаар эсвэл нууц үг буруу байна Дахин оролдоно уу.",Toast.LENGTH_SHORT).show();
                                       }
                                   } catch (JSONException e) {
                                       e.printStackTrace();
                                   }
                               } else {
                                   dialog.dismiss();
                                   timer[0].cancel();
                                   Toast.makeText(getContext(),"Сервертэй холбогдсонгүй дахин оролдоно уу.",Toast.LENGTH_SHORT).show();
                               }
                           }
                       });
                    }
                };
                timer[0].schedule(timerTask[0],3000,300);
                super.onPostExecute(s);
            }
        };
        task.execute(username, password);
    }

    private String getSHA1HashData(String data){

        MessageDigest digest = null;
        byte[] input = null;

        try {
            digest = MessageDigest.getInstance("SHA-1");
            digest.reset();
            input = digest.digest(data.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < input.length; i++) {
            int halfbyte = (input[i] >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                if ((0 <= halfbyte) && (halfbyte <= 9))
                    buf.append((char) ('0' + halfbyte));
                else
                    buf.append((char) ('a' + (halfbyte - 10)));
                halfbyte = input[i] & 0x0F;
            } while(two_halfs++ < 1);
        }
        return buf.toString();
    }
}
