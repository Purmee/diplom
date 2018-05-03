package com.example.punny.test.fragment;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import java.net.URI;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * A simple {@link Fragment} subclass.
 */
public class Register extends Fragment {

    private EditText et_reg, et_lname, et_fname, et_age, et_gender, et_school, et_class,
            et_phone, et_email, et_password, et_pass_again;

    private Button button;

    public Register() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        // ((MainActivity)getActivity()).getSupportActionBar().setTitle("Бүртгэх");
        et_reg = (EditText)view.findViewById(R.id.et_rd);
        et_lname = (EditText)view.findViewById(R.id.et_lname);
        et_fname = (EditText)view.findViewById(R.id.et_fname);
        et_age = (EditText)view.findViewById(R.id.et_age);
        et_gender = (EditText)view.findViewById(R.id.et_gender);
        et_school = (EditText)view.findViewById(R.id.et_school);
        et_class = (EditText)view.findViewById(R.id.et_class);
        et_phone = (EditText)view.findViewById(R.id.et_phone);
        et_email = (EditText)view.findViewById(R.id.et_email);
        et_password = (EditText)view.findViewById(R.id.et_password);
        et_pass_again = (EditText)view.findViewById(R.id.et_pass_again);

        button = (Button)view.findViewById(R.id.reg_btn);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Register();
            }
        });
        return view;
    }
    // pass hash function
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

    private void Register() {

        if(!et_reg.getText().toString().equals("")
                && !et_lname.getText().toString().equals("")
                && !et_fname.getText().toString().equals("")
                && !et_age.getText().toString().equals("")
                && !et_gender.getText().toString().equals("")
                && !et_school.getText().toString().equals("")
                && !et_class.getText().toString().equals("")
                && !et_phone.getText().toString().equals("")
                && !et_email.getText().toString().equals("")
                && !et_password.getText().toString().equals("")
                && !et_pass_again.getText().toString().equals("")) {

            if (et_password.getText().toString().equals(et_password.getText().toString())) {

                AsyncTask<String,String,String> task = new AsyncTask<String, String, String>() {
                    @Override
                    protected String doInBackground(String... params) {

                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("register",et_reg.getText().toString());
                            jsonObject.put("l_name",et_lname.getText().toString());
                            jsonObject.put("f_name",et_fname.getText().toString());
                            jsonObject.put("age",Integer.parseInt(et_age.getText().toString()));
                            jsonObject.put("gender",et_gender.getText().toString());
                            jsonObject.put("school",et_school.getText().toString());
                            jsonObject.put("class",et_class.getText().toString());
                            jsonObject.put("mobile",et_phone.getText().toString());
                            jsonObject.put("email",et_email.getText().toString());
                            jsonObject.put("password",getSHA1HashData(et_password.getText().toString()));

                            URL url = new URL("http://192.168.1.2/app/query.php");
                            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                            httpURLConnection.setRequestMethod("POST");
                            httpURLConnection.setDoInput(true);
                            httpURLConnection.setDoOutput(true);

                            String send_data = "register="+jsonObject.toString();
                            OutputStream outputStream = httpURLConnection.getOutputStream();

                            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                            writer.write(send_data);

                            outputStream.flush();
                            writer.close();

                            InputStream inputStream = httpURLConnection.getInputStream();

                            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));

                            String response = "";
                            String row;
                            while((row = reader.readLine()) != null) {
                                response += row;
                            }

                            reader.close();
                            inputStream.close();
                            httpURLConnection.disconnect();
                            return response;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        super.onPostExecute(s);
                        if (s != null) {
                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                boolean ch = jsonObject.getBoolean("status");
                                if (ch) {
                                    Toast.makeText(getContext(),"Амжилттай бүртгэгдлээ",Toast.LENGTH_SHORT).show();
                                    et_reg.setText("");
                                    et_lname.setText("");
                                    et_fname.setText("");
                                    et_age.setText("");
                                    et_gender.setText("");
                                    et_school.setText("");
                                    et_class.setText("");
                                    et_email.setText("");
                                    et_phone.setText("");
                                    et_pass_again.setText("");
                                    et_password.setText("");
                                } else {
                                    Toast.makeText(getContext(),"Бүртгэл амжилтгүй боллоо дахин хандана уу !!!",Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(getContext(),"Сервертэй холбогдсонгүй." + s,Toast.LENGTH_SHORT).show();
                            Log.d("sql","sql: " + s);
                        }
                    }
                };
                task.execute();
            } else {
                Toast.makeText(getContext(),"Нууц үг тохирохгүй байна",Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(),"Бүртгэлийн мэдээллээ оруулна уу !!!",Toast.LENGTH_SHORT).show();
        }
    }
}
