package com.tesis.agendatome.adapter;

import com.tesis.agendatome.ListaEventos;
import com.tesis.agendatome.MenuCategorias;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class CustomTabsPagerAdapter extends FragmentPagerAdapter {
 
    public CustomTabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }
 
    @Override
    public Fragment getItem(int index) {
 
        switch (index) {
        case 0:
            return new MenuCategorias();
        case 1:
            return new ListaEventos();
        }
        return null;
    }
 
    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 2;
    }
}