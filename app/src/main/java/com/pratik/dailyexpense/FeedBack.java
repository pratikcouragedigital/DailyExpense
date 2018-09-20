package com.pratik.dailyexpense;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


public class FeedBack extends AppCompatActivity implements View.OnClickListener {

    Button btnSubmit;
    EditText txtFeedback,txtEmail;
    String feedback,email;
    //String yourMail = "pratik.sonawane3@gmail.com";
    String toMail = "mobitechs17@gmail.com";
//    String password = "Pratik@10";
    String subject = "Daily Expense FeedBack";

    private AdView adView,adView1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed_back);

        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        txtFeedback = (EditText) findViewById(R.id.txtFeedback);
//        txtEmail = (EditText) findViewById(R.id.txtEmail);
        btnSubmit.setOnClickListener(this);

        adView = (AdView)findViewById(R.id.adView);
        adView1 = (AdView)findViewById(R.id.adView1);

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        adView1.loadAd(adRequest);

        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the user is about to return
                // to the app after tapping on an ad.
            }
        });
        adView1.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the user is about to return
                // to the app after tapping on an ad.
            }
        });

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnSubmit){
            feedback = txtFeedback.getText().toString();
//            email = txtFeedback.getText().toString();
            if(feedback.equals("") || feedback == null ){
                Toast.makeText(this, "Please Enter Your Feedback.", Toast.LENGTH_SHORT).show();
            }
//            else if(email.equals("") || email == null ){
//                Toast.makeText(this, "Please Enter Your Email.", Toast.LENGTH_SHORT).show();
//            }
            else{
               // new SendMail().execute("");


                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{ toMail});
                //email.putExtra(Intent.EXTRA_CC, new String[]{ to});
                //email.putExtra(Intent.EXTRA_BCC, new String[]{to});
                email.putExtra(Intent.EXTRA_SUBJECT, subject);
                email.putExtra(Intent.EXTRA_TEXT, feedback);

                //need this to prompts email client only
                email.setType("message/rfc822");

                startActivity(Intent.createChooser(email, "Choose an Email client :"));
                this.finish();
            }
        }
    }

//    private class SendMail extends AsyncTask<String, Integer, Void> {
//
//        private ProgressDialog progressDialog;
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            progressDialog = ProgressDialog.show(FeedBack.this, "Please wait", "Sending mail", true, false);
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            super.onPostExecute(aVoid);
//            progressDialog.dismiss();
//        }
//
//        protected Void doInBackground(String... params) {
//            Mail m = new Mail(yourMail, password);
//
//            String[] toArr = {toMail, yourMail};
//            m.setTo(toArr);
//            m.setFrom(yourMail);
//            m.setSubject("This is an email sent using my Mail JavaMail wrapper from an Android device.");
//            m.setBody(feedback);
//
//            try {
//                if(m.send()) {
//                    Toast.makeText(FeedBack.this, "Email was sent successfully.", Toast.LENGTH_LONG).show();
//                } else {
//                    Toast.makeText(FeedBack.this, "Email was not sent.", Toast.LENGTH_LONG).show();
//                }
//            } catch(Exception e) {
//                Toast.makeText(FeedBack.this, "Exp: "+ e.toString(), Toast.LENGTH_SHORT).show();
//                Log.e("MailApp", "Could not send email", e);
//            }
//            return null;
//        }
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
// Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Daily Expense  is an amazing app to deal with the spending in this swamped life. \n\n https://play.google.com/store/apps/details?id=com.pratik.dailyexpense");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                return true;
            case R.id.feedback:

                Intent gotofeedback = new Intent(this,FeedBack.class);
                startActivity(gotofeedback);
                return true;

            case R.id.checkUpdate:
                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}