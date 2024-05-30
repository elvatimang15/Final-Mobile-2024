package com.example.pizzaanddesserts;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.pizzaanddesserts.fragment.CartFragment;
import com.example.pizzaanddesserts.fragment.DessertFragment;
import com.example.pizzaanddesserts.fragment.PizzaFragment;
import com.example.pizzaanddesserts.fragment.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getSupportFragmentManager();
        PizzaFragment pizzaFragment = new PizzaFragment();
        Fragment fragment = fragmentManager.findFragmentByTag(PizzaFragment.class.getSimpleName());
        if (!(fragment instanceof PizzaFragment)){
            fragmentManager
                    .beginTransaction()
                    .add(R.id.frame_container, pizzaFragment, PizzaFragment.class.getSimpleName())
                    .commit();
        }

        BottomNavigationView bottomNav = findViewById(R.id.navmenu);
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.pizzabtn) {
                selectedFragment = new PizzaFragment();
            } else if (item.getItemId() == R.id.dessertbtn) {
                selectedFragment = new DessertFragment();
            } else if (item.getItemId() == R.id.cartbtn) {
                 selectedFragment = new CartFragment();
            } else if (item.getItemId() == R.id.profilebtn) {
                selectedFragment = new ProfileFragment();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame_container, selectedFragment)
                        .commit();

                return true;
            } else {
                return false;
            }
        });
    }
}