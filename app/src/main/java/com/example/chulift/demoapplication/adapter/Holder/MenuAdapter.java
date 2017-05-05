package com.example.chulift.demoapplication.adapter.Holder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.chulift.demoapplication.AboutUsActivity;
import com.example.chulift.demoapplication.CameraActivity;
import com.example.chulift.demoapplication.MenusActivity;
import com.example.chulift.demoapplication.R;
import com.example.chulift.demoapplication.classes.Menu;
import com.example.chulift.demoapplication.examStorage.ManageExamStorageActivity;
import com.example.chulift.demoapplication.login.LoginActivity;
import com.example.chulift.demoapplication.template.ShowTemplateListActivity;

import java.util.ArrayList;


public class MenuAdapter extends RecyclerView.Adapter<MenuHolder2> {
    private Context context;
    private ArrayList<Menu> menus;

    public MenuAdapter(Context context, ArrayList<Menu> menus) {
        this.context = context;
        this.menus = menus;
    }

    @Override
    public MenuHolder2 onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_layout, parent, false);
        return new MenuHolder2(view);
    }

    @Override
    public void onBindViewHolder(MenuHolder2 holder, int position) {
        Menu menu = menus.get(position);
        final int thisPosition = position;
        holder.getNameOfMenu().setText(menu.getMenuName());
        holder.getCardView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = null;
                switch (thisPosition) {
                    case 0:
                        intent = new Intent(context, ShowTemplateListActivity.class);

                        break;
                    case 1:
                        intent = new Intent(context, ManageExamStorageActivity.class);

                        break;
                    case 2:
                        //Toast.makeText(context,"หน้านี้ยังไม่มี",Toast.LENGTH_SHORT).show();
                        intent = new Intent(context, CameraActivity.class);
                        break;
                    case 3:
                        intent = new Intent(context, AboutUsActivity.class);

                        break;
                    case 4:
                        intent = new Intent(context, LoginActivity.class);

                        break;
                    default:
                        Toast.makeText(context, "ยังไม่มีหน้านี้", Toast.LENGTH_SHORT).show();
                        break;
                }
                if(intent != null) {
                    context.startActivity(intent);
                    ((Activity)context).finish();
                }
                else Log.i("Intent","null intent");
            }
        });
    }


    @Override
    public int getItemCount() {
        return menus.size();
    }
}
