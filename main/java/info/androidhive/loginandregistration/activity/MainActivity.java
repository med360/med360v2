package info.androidhive.loginandregistration.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.HashMap;

import info.androidhive.loginandregistration.R;
import info.androidhive.loginandregistration.app.AppController;
import info.androidhive.loginandregistration.helper.SQLiteHandler;
import info.androidhive.loginandregistration.helper.SessionManager;

public class MainActivity extends AppCompatActivity{

	private TextView txtName;
	private TextView txtEmail;
	private TextView txtdob;
	private TextView txtnational;
	private TextView txtblood;
	private Button btnLogout;
	private Button btnchange;
	private ImageButton imgbtn1;
	private ImageButton imgbtn4;
	private NetworkImageView thumbNail;


	private SQLiteHandler db;
	private SessionManager session;

	public BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
			= new BottomNavigationView.OnNavigationItemSelectedListener() {

		@Override
		public boolean onNavigationItemSelected(@NonNull MenuItem item) {
			switch (item.getItemId()) {
				case R.id.navigation_home:
					Log.e("medlogin", "before redirecting to new activity on success login");
					Intent intent = new Intent(MainActivity.this,
							Viewdoctor.class);
					startActivity(intent);
					finish();
					return true;
				case R.id.messg:
					Log.e("medlogin", "before redirecting to new activity on success login");
					Intent intent2 = new Intent(MainActivity.this,
							ViewBooking.class);
					startActivity(intent2);
					finish();
					return true;
				case R.id.profilenav:
					Log.e("medlogin", "before redirecting to new activity on success login");
					Intent intent3 = new Intent(MainActivity.this,
							MainActivity.class);
					startActivity(intent3);
					finish();
					return true;
				case R.id.logoutnav:
					Log.e("medlogin", "before redirecting to new activity on success login");
					session = new SessionManager(getApplicationContext());
					session.setLogin(false);
					session.logoutUser();
					return true;

			}
			return false;
		}

	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Toolbar toolbarBottom = (Toolbar) findViewById(R.id.toolbar_bottom);
		toolbarBottom.inflateMenu(R.menu.navigation);
		toolbarBottom.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem menuItem) {
				//your code


				return false;
			}
		});
		txtName = (TextView) findViewById(R.id.name);
		txtEmail = (TextView) findViewById(R.id.email);
		btnLogout = (Button) findViewById(R.id.btnLogout);
		txtdob = (TextView) findViewById(R.id.dob);
		txtnational = (TextView) findViewById(R.id.nationality);
		txtblood = (TextView) findViewById(R.id.bloodgroup);
		thumbNail = (NetworkImageView) findViewById(R.id.userimage);
		imgbtn1 = (ImageButton) findViewById(R.id.imgbtn1);
		imgbtn4 = (ImageButton) findViewById(R.id.imgbtn4);
		btnchange = (Button) findViewById(R.id.btnchange);

		BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);


		navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);



		// SqLite database handler


		// session manager
		session = new SessionManager(getApplicationContext());

		if (!session.isLoggedIn()) {
			logoutUser();
		}

		// Fetching user details from SQLite
		HashMap<String, String> user = session.getUserDetails();
		Log.e("medlogin", "hashmap got returned and started to fetch details to strings");
		String name = user.get("name");

		// email
		String email = user.get("email");
		String dob = user.get("dob");
		String nationality = user.get("nationality");
		String blood = user.get("blood");
		String image = user.get("image");
		String utype = user.get("utype");



		// force doctor to chats page

		if(utype.equals("2")){
		Intent intent = new Intent(MainActivity.this, ViewChats.class);
		startActivity(intent);
		finish();
		}
		Log.e("medlogin", "all details fetched from hashmap and now displaying on text fields begin");
		// Displaying the user details on the screen
		txtName.setText(name);
		txtEmail.setText(email);
		txtdob.setText(dob);
		txtnational.setText(nationality);
		txtblood.setText(blood);

		ImageLoader imgLoader;
		imgLoader = AppController.getInstance().getImageLoader();
		thumbNail.setImageUrl(image, imgLoader);


		Log.e("medlogin", "all details are displayed succesfully");
		// Logout button click event
		btnLogout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				logoutUser();
			}
		});


		btnchange.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent in = new Intent(getApplicationContext(),
						ImageUploadActivity.class);
				// sending did to next activity

				// starting new activity and expecting some response back
				startActivityForResult(in, 100);
			}
		});


		imgbtn1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent in = new Intent(getApplicationContext(),
						Viewdoctor.class);
				// sending did to next activity

				// starting new activity and expecting some response back
				startActivityForResult(in, 100);
			}
		});

		imgbtn4.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				session.logoutUser();
			}
		});
	}

	/**
	 * Logging out the user. Will set isLoggedIn flag to false in shared
	 * preferences Clears the user data from sqlite users table
	 * */
	private void logoutUser() {
		HashMap<String, String> user2 = session.getUserDetails();
		String name2 = user2.get("name");
		Log.e("medlogin", "name before logout is"+name2);
		session.setLogin(false);
session.logoutUser();
		HashMap<String, String> user = session.getUserDetails();
		String name = user.get("name");
		Log.e("medlogin", "name after logout is"+name);
		// Launching the login activity
		//Intent intent = new Intent(MainActivity.this, LoginActivity.class);
		//startActivity(intent);
		//finish();
	}
}
