package com.example.chulift.demoapplication.adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.chulift.demoapplication.R;
import com.example.chulift.demoapplication.adapter.Holder.AnswerSheetHolder;
import com.example.chulift.demoapplication.classes.AnswerSheet;
import com.example.chulift.demoapplication.config.Config;
import com.example.chulift.demoapplication.httpConnect.ConnectServer;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class AnswerSheetAdapter extends RecyclerView.Adapter<AnswerSheetHolder> {
    private Context context;
    private ArrayList arrayList;

    public ArrayList getArrayList() {
        return arrayList;
    }

    public AnswerSheetAdapter(ArrayList arrayList) {
        this.arrayList = arrayList;
    }

    @Override
    public AnswerSheetHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.answersheets, parent, false);
        return new AnswerSheetHolder(view);
    }

    @Override
    public void onBindViewHolder(final AnswerSheetHolder holder, int position) {
        final AnswerSheet answerSheet = (AnswerSheet) arrayList.get(holder.getAdapterPosition());
        holder.getIdOfExam().setText(String.valueOf(holder.getAdapterPosition() + 1));
        holder.getScoreOfExam().setText(answerSheet.getScore());
        holder.getStatusOfAnswerSheet().setText(answerSheet.getStatus());
        holder.getStudentCode().setText(answerSheet.getStudentCode());
        //Log.i("AnswerSheer", "" + holder.getAdapterPosition());
        holder.getLayout().setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.i("Click", "Clicked!");
                PopupMenu popupMenu = new PopupMenu(context, v);
                popupMenu.getMenuInflater().inflate(R.menu.answersheet_menu, popupMenu.getMenu());
                //register click on popup
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.popup_menu_delete_answer_sheet:
                                deleteAnswerSheet(holder.getAdapterPosition());
                                break;
                            default:
                                return false;
                        }
                        return true;
                    }


                });

                // Force icons to show
                Object menuHelper;
                Class[] argTypes;

                try {
                    Field fMenuHelper = PopupMenu.class.getDeclaredField("mPopup");
                    fMenuHelper.setAccessible(true);
                    menuHelper = fMenuHelper.get(popupMenu);
                    argTypes = new Class[]{boolean.class};
                    menuHelper.getClass()
                            .getDeclaredMethod("setForceShowIcon", argTypes)
                            .invoke(menuHelper, true);
                } catch (Exception e) {
                    Log.e("as", "error forcing menu icons to show", e);
                    popupMenu.show();
                    return;
                }
                popupMenu.show();
            }
        });
    }

    private void deleteAnswerSheet(final int position) {
        final AnswerSheet answerSheet = (AnswerSheet) arrayList.get(position);
        final String delUrl = Config.serverUrl + Config.projectName + "deleteAnswerSheet.php";
        new AlertDialog.Builder(context)
                .setTitle("Confirm delete.")
                .setMessage("ต้องการลบใช่หรือไม่?")
                .setNegativeButton("ไม่ใช่", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("ใช่", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, final int i) {
                        final ProgressDialog progressDialog = new ProgressDialog(context,
                                R.style.AppTheme_Dark_Dialog);
                        progressDialog.setIndeterminate(true);
                        progressDialog.setMessage("กำลังลบช้อสอบที่เลือก...");
                        progressDialog.show();
                        final String postBody = "{\"" + "id_answer_sheet\"" + ":\"" + answerSheet.getAnswerSheetID() + "\"}";
                        new AsyncTask<Void, Void, String>() {
                            @Override
                            protected String doInBackground(Void... voids) {
                                ConnectServer.getJSONObject(delUrl, postBody);
                                return answerSheet.getAnswerSheetID();
                            }

                            @Override
                            protected void onPostExecute(String s) {
                                Toast.makeText(context, "ลบข้อสอบเรียบร้อยแล้ว", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                arrayList.remove(position);
                                notifyDataSetChanged();
                            }
                        }.execute();
                    }
                }).show();
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
