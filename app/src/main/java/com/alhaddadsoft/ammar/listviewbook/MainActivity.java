package com.alhaddadsoft.ammar.listviewbook;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.alhaddadsoft.ammar.AppRater;
import com.alhaddadsoft.searchadapter.SearchAdapter;



import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import io.fabric.sdk.android.Fabric;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


import com.alhaddadsoft.ammar.listviewbook.R;
import com.alhaddadsoft.ammar.listviewbook.ImageLoader;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.UserTimeline;

public class MainActivity extends Activity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "lVmKw8cE7fv7F7OJ4la7y01pc";
    private static final String TWITTER_SECRET = "HCh9jbCivLjWWfDGJbShR3hCtoguVOgSQ4qWIfMmYjWXKNtzfN";


    private ImageView imgView;
    private ImageLoader imgLoader;
    private String strURL = "http://www.alhaddadapps.net/gbrankhalil_gbran/mainactivity.png";

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.


    public List<Movie> movies = new ArrayList<>();

    @InjectView(R.id.grid_view) GridView gridView;
    @InjectView(R.id.search_edit_text) EditText editText;
    Button shareApp;
    Button contactus;


    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig), new Crashlytics(), new Answers());

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/Roboto-Light.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        AppRater.app_launched(this);

        showdialog();

        imgView = (ImageView) findViewById(R.id.imageView);
        imgLoader = new ImageLoader(this);


            imgLoader.DisplayImage(strURL, imgView);




       try {
            ListView   twittera   = (ListView) findViewById(R.id.listView);

            final UserTimeline userTimeline = new UserTimeline.Builder()
                    .screenName("@Gbraniat")
                    .includeRetweets(false)
                    .build();
            final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter.Builder(this)
                    .setTimeline(userTimeline)
                    .build();
            twittera.setAdapter(adapter);

        }catch (Throwable throwable){
            Toast.makeText(MainActivity.this, "مرحبا", Toast.LENGTH_LONG).show();
        }

        fillList(movies);
        final SearchAdapter adapter = new MyAdapter(movies, this).registerFilter(Movie.class, "enTitle")
                .setIgnoreCase(true);
        gridView.setAdapter(adapter);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        contactus = (Button)findViewById(R.id.contatus);
        contactus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String DisplayName = "alhaddadsoft";
                String MobileNumber = "+966593710400";

                ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

                ops.add(ContentProviderOperation.newInsert(
                        ContactsContract.RawContacts.CONTENT_URI)
                        .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                        .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                        .build());

                //------------------------------------------------------ Names
                if (DisplayName != null) {
                    ops.add(ContentProviderOperation.newInsert(
                            ContactsContract.Data.CONTENT_URI)
                            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                            .withValue(ContactsContract.Data.MIMETYPE,
                                    ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                            .withValue(
                                    ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                                    DisplayName).build());
                }

                //------------------------------------------------------ Mobile Number
                if (MobileNumber != null) {
                    ops.add(ContentProviderOperation.
                            newInsert(ContactsContract.Data.CONTENT_URI)
                            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                            .withValue(ContactsContract.Data.MIMETYPE,
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                            .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, MobileNumber)
                            .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                                    ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                            .build());
                }


                // Asking the Contact provider to create a new contact
                try {
                    getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                showmakecontactdialog();
            }
        });
        shareApp = (Button)findViewById(R.id.shareapp);
        shareApp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "أعجبني برنامج " + "مكتبة جبران خليل جبران" + " " + "على الأندرويد تستطيع تحميله انت ايضا" +
                        " الرابط التالي\n" +
                        "https://play.google.com/store/apps/details?id=com.alhaddadsoft.ammar.listviewbook";
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "جبران خليل جبران");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
                // Intent d= new Intent(MainActivity.this, Main2Activity.class);
                //startActivity(d);
            }
        });

    }

    private void fillList(List<Movie> movies) {

        movies.add(new Movie("book1", "النبي", R.drawable.book1));
        movies.add(new Movie("book2", "الأرواح المتمردة", R.drawable.book2));
        movies.add(new Movie("book3", "الشعلة الزرقاء", R.drawable.book3));
        movies.add(new Movie("book4", "عيسى ابن الإنسان", R.drawable.book4));
        movies.add(new Movie("book5", "مناجاة أرواح", R.drawable.book5));
        movies.add(new Movie("book6", "أرباب الأرض", R.drawable.book6));
        movies.add(new Movie("book7", "العواصف", R.drawable.book7));
        movies.add(new Movie("book8", "دمعه وابتسامة", R.drawable.book8));
        movies.add(new Movie("book9", "عرائس المروج", R.drawable.book9));
        movies.add(new Movie("book10", "الموسيقى", R.drawable.book10));
        movies.add(new Movie("book11", "البدائع والطرائف", R.drawable.book11));
        movies.add(new Movie("book12", "رمل وزبد", R.drawable.book12));
        movies.add(new Movie("book13", "المواكب", R.drawable.book13));
        movies.add(new Movie("book14", "الأجنحة المتكسرة", R.drawable.book14));
        movies.add(new Movie("book15", "نصوص خارج المجموعة", R.drawable.book15));
        movies.add(new Movie("book16", "الأعمال المعربة", R.drawable.book16));



    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


       public void showdialog(){

            Toast.makeText(MainActivity.this, "جاري تحميل التغريدات ...()", Toast.LENGTH_LONG).show();

            final Dialog dialog = new Dialog(this);

    dialog.setContentView(R.layout.twitteraccount);
    dialog.setTitle("تغريدات جبران خليل جبران");
            ListView   twitter   = (ListView) dialog.findViewById(R.id.listView);

            final UserTimeline userTimeline = new UserTimeline.Builder()
                    .screenName("@Gbraniat")
                    .includeRetweets(false)
                    .build();
            final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter.Builder(this)
                    .setTimeline(userTimeline)
                    .build();
            twitter.setAdapter(adapter);

            // final TextView partonetext = (TextView) dialog.findViewById(R.id.Textpartone);

    dialog.show();
}
   public void showmakecontactdialog(){

       final Dialog dialog = new Dialog(this);

       dialog.setContentView(R.layout.makecontact);
       dialog.setTitle("تواصل مع :  مطور البرنامج");
      // ImageButton adabiatIns   = (ImageButton) dialog.findViewById(R.id.imageButton);
      // ImageButton adabiatSnap   = (ImageButton) dialog.findViewById(R.id.imageButton2);
      // ImageButton hamfarTwitt   = (ImageButton) dialog.findViewById(R.id.imageButton3);
       ImageButton hamfarWhat   = (ImageButton) dialog.findViewById(R.id.imageButton4);

     /* adabiatIns.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Uri uri = Uri.parse("http://instagram.com/_u/adabiat");
               Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

               likeIng.setPackage("com.instagram.android");

               try {
                   startActivity(likeIng);
               } catch (ActivityNotFoundException e) {
                   startActivity(new Intent(Intent.ACTION_VIEW,
                           Uri.parse("http://instagram.com/adabiat")));

               }
           }

       } );

       adabiatSnap.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Toast.makeText(MainActivity.this, "عزيزي، قم بإظافة المعرف Adabiat بشكل يدوي في سناب شات", Toast.LENGTH_LONG).show();

           }
       });

       hamfarTwitt.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               try {
                   startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + "hamfarouk")));
               }catch (Exception e) {
                   startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/#!/" + "hamfarouk")));
               }

          /*     try {
                   // Check if the Twitter app is installed on the phone.
                   getPackageManager().getPackageInfo("com.twitter.android", 0);

                   Intent intent = new Intent(Intent.ACTION_VIEW);
                   intent.setClassName("com.twitter.android", "com.twitter.android.ProfileActivity");
                   // Don't forget to put the "L" at the end of the id.
                   intent.putExtra("user_id", 269883973);
                   startActivity(intent);
               } catch (PackageManager.NameNotFoundException e) {
                   // If Twitter app is not installed, start browser.
                   startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/hamfarouk")));
               }

           }
       }); */
       hamfarWhat.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {


               String Eyad = "+966537340281";
               openConversationWithWhatsapp(Eyad);
               Toast.makeText(MainActivity.this, "إذا واجهتك رسالة خطأ تأكد من اتصالك بالإنترنت أو أظف رقم الجوال بشكل يدوي  ", Toast.LENGTH_LONG).show();

           }
       });
           // final TextView partonetext = (TextView) dialog.findViewById(R.id.Textpartone);

           dialog.show();
       }
    private void openConversationWithWhatsapp(String Eyad){
        String whatsappId = Eyad+"@s.whatsapp.net";
        Uri uri = Uri.parse("smsto:" + whatsappId);
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.setPackage("com.whatsapp");

        //intent.putExtra(Intent.EXTRA_TEXT, "text");
       // intent.putExtra(Intent.EXTRA_SUBJECT, "subject");
       // intent.putExtra(Intent.EXTRA_TITLE, "title");
       // intent.putExtra(Intent.EXTRA_EMAIL, "email");
       // intent.putExtra("sms_body", "The text goes here");
       // intent.putExtra("text","asd");
       // intent.putExtra("body","body");
       // intent.putExtra("subject","subjhect");

        startActivity(intent);
    }


    // TODO: Move this method and use your own event name to track your key metrics
    public void onKeyMetric() {
        // TODO: Use your own string attributes to track common values over time
        // TODO: Use your own number attributes to track median value over time
        Answers.getInstance().logCustom(new CustomEvent("Share Button Clicked")
                .putCustomAttribute("Category", "Comedy")
                .putCustomAttribute("Length", 350));
    }

}

