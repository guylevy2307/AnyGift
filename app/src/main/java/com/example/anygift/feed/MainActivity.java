package com.example.anygift.feed;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavHost;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anygift.R;
import com.example.anygift.login.LoginActivity;
import com.example.anygift.model.Model;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    NavController navCtr;
    FeedViewModel viewModel;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;
    TextView nameTv,emailTv;
    ImageView imageIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("111111111111");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println("22222222222222");
        NavHost navHost = (NavHost) getSupportFragmentManager().findFragmentById(R.id.main_navhost);
        System.out.println("44444444444444444");
        navCtr = navHost.getNavController();
        System.out.println("555555555555555555");
        viewModel = new ViewModelProvider(this).get(FeedViewModel.class);


        nameTv = findViewById(R.id.menu_header_name_tv);
        emailTv = findViewById(R.id.menu_header_email_tv);
        imageIv = findViewById(R.id.menu_header_image_iv);

        drawerLayout = findViewById(R.id.my_drawer_layout);
        navigationView = findViewById(R.id.Navigation_view);

        //--drawer navigation menu--//
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        // to make the Navigation drawer icon always appear on the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView.setItemIconTintList(null);
        setMenuHeader();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_feed: {
                        drawerLayout.closeDrawer(GravityCompat.START);
                        navCtr.navigate(R.id.action_global_feedFragment);
                        break;
                    }
                    case R.id.menu_profile: {
                        drawerLayout.closeDrawer(GravityCompat.START);
                        navCtr.navigate(R.id.action_global_userProfileFragment);
                        break;
                    }
                    case R.id.menu_cards: {
                        drawerLayout.closeDrawer(GravityCompat.START);
                        navCtr.navigate(R.id.action_global_myCardsFragment);
                        break;
                    }
                    case R.id.add_card: {
                        drawerLayout.closeDrawer(GravityCompat.START);
                        navCtr.navigate(R.id.action_global_addCardFragment);
                        break;
                    }
                    case R.id.cards_map: {
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Intent map = new Intent(MainActivity.this, MapsActivity.class);
                        startActivity(map);
                        break;
                    }
                    case R.id.shop: {
                        drawerLayout.closeDrawer(GravityCompat.START);
                        navCtr.navigate(R.id.action_global_shopFragment);
                        break;
                    }
                    case R.id.logout: {
                        Toast.makeText(getBaseContext(),"Logging Out",Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    }
                }
                return false;
            }
        });
    }

    private void setMenuHeader() {
        System.out.println(Model.instance.getSignedUser());
        View header= (View)navigationView.getHeaderView(0);
        nameTv = header.findViewById(R.id.menu_header_name_tv);
        emailTv = header.findViewById(R.id.menu_header_email_tv);
        imageIv = header.findViewById(R.id.menu_header_image_iv);
        nameTv.setText(  Model.instance.getSignedUser().getFirstName() + " " + Model.instance.getSignedUser().getLastName());
        emailTv.setText( Model.instance.getSignedUser().getEmail());
        if( Model.instance.getSignedUser().getProfilePicture().compareTo("")!=0){
            Picasso.get().load(Model.instance.getSignedUser().getProfilePicture()).into(imageIv);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);

    }
}