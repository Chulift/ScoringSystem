package com.example.chulift.demoapplication.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.example.chulift.demoapplication.adapter.Holder.ExamStorageHolder;
import com.example.chulift.demoapplication.answer.SelectAnswerActivity;
import com.example.chulift.demoapplication.answerSheet.AnswerSheetListActivity;
import com.example.chulift.demoapplication.classes.ExamStorage;
import com.example.chulift.demoapplication.config.Config;
import com.example.chulift.demoapplication.examStorage.CUExamStorageActivity;
import com.example.chulift.demoapplication.examStorage.ManageExamStorageActivity;
import com.example.chulift.demoapplication.httpConnect.ConnectServer;
import com.google.gson.Gson;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class ExamStorageAdapter extends RecyclerView.Adapter<ExamStorageHolder> {

    private Context context;
    private ArrayList arrayList;
    public ExamStorageAdapter(ArrayList arrayList) {
        this.arrayList = arrayList;
    }
    @Override
    public ExamStorageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.set_exam_table_layout, parent, false);
        return new ExamStorageHolder(view);
    }

    @Override
    public void onBindViewHolder(final ExamStorageHolder holder, int position) {
        final ExamStorage examStorage = (ExamStorage) arrayList.get(position);
        holder.getSequenceExamStorage().setText((position + 1) + "");
        holder.getNameOfExamStorage().setText("" + examStorage.getExamStorageName());
        holder.getTemplateOfExamStorage().setText(examStorage.getTemplateName() + "");
        holder.getNumScoreOfExamStorage().setText(examStorage.getMaxScore());
        holder.getLayout().setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View v) {

                v.setBackgroundResource(R.color.iron);
                Log.i("Click", "Clicked!");
                final PopupMenu popupMenu = new PopupMenu(context, v);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
                //register click on popup
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Intent intent = null;
                        switch (item.getItemId()) {
                            case R.id.popup_menu_add_answer_to_exam:
                                intent = new Intent(context, SelectAnswerActivity.class);
                                break;
                            case R.id.popup_menu_check_exam:
                                intent = new Intent(context, AnswerSheetListActivity.class);
                                break;
                            case R.id.popup_menu_edit_set_exam:
                                intent = new Intent(context, CUExamStorageActivity.class);
                                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                break;
                            case R.id.popup_menu_delete_set_exam:
                                deleteExamStorage(holder.getAdapterPosition());
                                break;
                            default:
                                return false;
                        }
                        if (intent != null) {
                            intent.putExtra("examStorage", new Gson().toJson(examStorage));
                            context.startActivity(intent);
                            ((Activity) context).finish();
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
                    Log.w("as", "error forcing menu icons to show", e);
                    popupMenu.show();
                    return;
                }

                popupMenu.show();

                popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
                    @Override
                    public void onDismiss(PopupMenu popupMenu) {
                        v.setBackgroundResource(R.color.white);
                    }
                });

            }
        });
    }

    private void deleteExamStorage(final int position) {
        final String delUrl = Config.serverUrl + Config.projectName + "deleteExamStorage.php";
        final ExamStorage examStorage = (ExamStorage) arrayList.get(position);
        new AlertDialog.Builder(context)
                .setTitle("Confirm delete.")
                .setMessage("ต้องการลบ " + examStorage.getExamStorageName() + " ใช่หรือไม่?")
                .setNegativeButton("ไม่ใช่", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("ใช่", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final ProgressDialog progressDialog = new ProgressDialog(context,
                                R.style.AppTheme_Dark_Dialog);
                        progressDialog.setIndeterminate(true);
                        progressDialog.setMessage("กำลังลบชุดข้อสอบที่เลือก...");
                        progressDialog.show();
                        final String postBody = "{\"" + "id_answer_sheet\"" + ":\"" + examStorage.getExamStorageID() + "\"}";
                        new AsyncTask<Void, Void, String>() {
                            @Override
                            protected String doInBackground(Void... voids) {
                                ConnectServer.getJSONObject(delUrl, postBody);
                                return examStorage.getExamStorageID();
                            }

                            @Override
                            protected void onPostExecute(String s) {
                                Toast.makeText(context, "ลบชุดข้อสอบเรียบร้อยแล้ว", Toast.LENGTH_SHORT).show();
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
