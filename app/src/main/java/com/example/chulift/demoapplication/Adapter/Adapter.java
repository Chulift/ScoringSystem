package com.example.chulift.demoapplication.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.chulift.demoapplication.Adapter.Holder.AnswerSheetListHolder;
import com.example.chulift.demoapplication.Adapter.Holder.ExamSetHolder;
import com.example.chulift.demoapplication.Adapter.Holder.MenuHolder;
import com.example.chulift.demoapplication.Class.AnswerSheet;
import com.example.chulift.demoapplication.Class.ExamStorage;
import com.example.chulift.demoapplication.Class.Menu;
import com.example.chulift.demoapplication.Config.Config;
import com.example.chulift.demoapplication.ExamSet.CUExamSetActivity;
import com.example.chulift.demoapplication.AnswerSheet.AnswerSheetListActivity;
import com.example.chulift.demoapplication.Answer.ChooseAnswerMethodActivity;
import com.example.chulift.demoapplication.Answer.SelectAnswerActivity;

import com.example.chulift.demoapplication.ExamSet.ManageExamSetActivity;
import com.example.chulift.demoapplication.R;
import com.example.chulift.demoapplication.httpConnect.ConnectServer;
import com.google.gson.Gson;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by Chulift on 11/15/2016.
 */

public class Adapter extends BaseAdapter {
    Context context;
    ArrayList arrayList;
    String page;
    LayoutInflater inflater;

    public Adapter(Context context, ArrayList arrayList, String page) {
        this.page = page;
        this.context = context;
        this.arrayList = arrayList;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (page == "MenusActivity") {
            return initMenu(position, convertView);
        } else if (page == "ManageExamSetActivity") {
            return initExamSet(position, convertView);
        } else if (page == "AnswerSheetListActivity") {
            return initAnswerSheetList(position, convertView);
        }
        return null;
    }

    private View initAnswerSheetList(int position, View convertView) {
        AnswerSheetListHolder holder;
        final AnswerSheet answerSheet = (AnswerSheet) arrayList.get(position);
        if (convertView != null) {
            holder = (AnswerSheetListHolder) convertView.getTag();
        } else {
            convertView = inflater.inflate(R.layout.answersheets, null);
            holder = new AnswerSheetListHolder(convertView);
            convertView.setTag(holder);
        }
        holder.getIdOfExam().setText(position + "");
        holder.getScoreOfExam().setText(answerSheet.getScore() + "");
        holder.getStatusOfAnswerSheet().setText(answerSheet.getStatus() + "");
        Log.i("AnswerSheer", "" + position);
        convertView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.i("Click", "Clicked!");
                PopupMenu popupMenu = new PopupMenu(context, v);
                popupMenu.getMenuInflater().inflate(R.menu.answersheet_menu, popupMenu.getMenu());
                //register click on popup
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Intent intent = null;
                        switch (item.getItemId()) {
                            case R.id.popup_menu_delete_answer_sheet:
                                //Toast.makeText(context, "delete clicked.", Toast.LENGTH_SHORT).show();
                                deleteAnswerSheet(answerSheet);
                                break;
                            default:
                                return false;
                        }
                        if (intent != null) {
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
                    // Possible exceptions are NoSuchMethodError and
                    // NoSuchFieldError
                    //
                    // In either case, an exception indicates something is wrong
                    // with the reflection code, or the
                    // structure of the PopupMenu class or its dependencies has
                    // changed.
                    //
                    // These exceptions should never happen since we're shipping the
                    // AppCompat library in our own apk,
                    // but in the case that they do, we simply can't force icons to
                    // display, so log the error and
                    // show the menu normally.

                    Log.w("as", "error forcing menu icons to show", e);
                    popupMenu.show();
                    return;
                }


                popupMenu.show();
                return;
            }
        });
        return convertView;
    }

    private void deleteAnswerSheet(final AnswerSheet answerSheet) {
        final String delUrl = Config.serverURL + "deleteAnswerSheet.php";
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
                    public void onClick(DialogInterface dialogInterface, int i) {
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
                                context.startActivity(new Intent(context, AnswerSheetListActivity.class));
                                ((Activity) context).finish();
                            }
                        }.execute();
                    }
                }).show();
    }

    private View initMenu(int position, View convertView) {
        MenuHolder holder;
        Menu temp = (Menu) arrayList.get(position);
        if (convertView != null) {
            holder = (MenuHolder) convertView.getTag();
        } else {
            convertView = inflater.inflate(R.layout.menus, null);
            holder = new MenuHolder(convertView);
            convertView.setTag(holder);
        }
        convertView.setBackgroundResource(temp.getColor());
        holder.getImageOfMenu().getLayoutParams().height = 150;
        holder.getImageOfMenu().getLayoutParams().width = 150;
        holder.getImageOfMenu().setImageResource(temp.getImagePath());
        holder.getNameOfMenu().setText(temp.getMenuName());

        return convertView;
    }

    private void deleteExamStorage(final ExamStorage examStorage) {
        final String delUrl = Config.serverURL + "deleteExamStorage.php";
        new AlertDialog.Builder(context)
                .setTitle("Confirm delete.")
                .setMessage("ต้องการลบ " + examStorage.getExam_storage_name() + " ใช่หรือไม่?")
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
                        final String postBody = "{\"" + "id_answer_sheet\"" + ":\"" + examStorage.getId_examStorage() + "\"}";
                        new AsyncTask<Void, Void, String>() {
                            @Override
                            protected String doInBackground(Void... voids) {
                                ConnectServer.getJSONObject(delUrl, postBody);
                                return examStorage.getId_examStorage();
                            }

                            @Override
                            protected void onPostExecute(String s) {
                                Toast.makeText(context, "ลบชุดข้อสอบเรียบร้อยแล้ว", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                context.startActivity(new Intent(context, ManageExamSetActivity.class));
                                ((Activity) context).finish();
                            }
                        }.execute();
                    }
                }).show();
    }

    //แก้ไขอันนี้
    private View initExamSet(final int position, View convertView) {
        final ExamSetHolder holder;
        //assign set of exam
        //ID
        final ExamStorage examStorage = (ExamStorage) arrayList.get(position);
        if (convertView != null) {
            holder = (ExamSetHolder) convertView.getTag();
        } else {
            convertView = inflater.inflate(R.layout.set_exam_table_layout, null);
            holder = new ExamSetHolder(convertView);
            convertView.setTag(holder);
        }
        holder.getSequenceExamSet().setText((position + 1) + "");
        holder.getNameOfSetExam().setText("" + examStorage.getExam_storage_name());
        holder.getTemplateOfSetExam().setText(examStorage.getUser_input_template_name() + "");
        holder.getNumScoreOfexamSet().setText(examStorage.getNumScore());
        final View finalConvertView = convertView;
        convertView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                finalConvertView.setBackgroundResource(R.color.iron);
                Log.i("Click", "Clicked!");
                final PopupMenu popupMenu = new PopupMenu(context, v);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
                //register click on popup
                //
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Intent intent = null;
                        switch (item.getItemId()) {
                            case R.id.popup_menu_add_answer_to_exam:
                                if (examStorage.getId_answer() == "null") {
                                    intent = new Intent(context, ChooseAnswerMethodActivity.class);
                                } else {
                                    intent = new Intent(context, SelectAnswerActivity.class);
                                }
                                intent.putExtra("previousPage", "ManageExamSetActivity");
                                break;
                            case R.id.popup_menu_check_exam:
                                intent = new Intent(context, AnswerSheetListActivity.class);
                                break;
                            case R.id.popup_menu_edit_set_exam:
                                intent = new Intent(context, CUExamSetActivity.class);
                                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                break;
                            case R.id.popup_menu_delete_set_exam:
                                deleteExamStorage(examStorage);
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
                    // Possible exceptions are NoSuchMethodError and
                    // NoSuchFieldError
                    //
                    // In either case, an exception indicates something is wrong
                    // with the reflection code, or the
                    // structure of the PopupMenu class or its dependencies has
                    // changed.
                    //
                    // These exceptions should never happen since we're shipping the
                    // AppCompat library in our own apk,
                    // but in the case that they do, we simply can't force icons to
                    // display, so log the error and
                    // show the menu normally.

                    Log.w("as", "error forcing menu icons to show", e);
                    popupMenu.show();
                    return;
                }

                popupMenu.show();
                popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
                    @Override
                    public void onDismiss(PopupMenu popupMenu) {
                        finalConvertView.setBackgroundResource(R.color.white);
                    }
                });
                return;
            }
        });
        return convertView;
    }
}