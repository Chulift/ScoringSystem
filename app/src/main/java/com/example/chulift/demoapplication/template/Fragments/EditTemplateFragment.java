package com.example.chulift.demoapplication.template.Fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.example.chulift.demoapplication.config.Config;
import com.example.chulift.demoapplication.config.NumberPickerConfig;
import com.example.chulift.demoapplication.template.ShowTemplateListActivity;
import com.example.chulift.demoapplication.classes.Template;
import com.example.chulift.demoapplication.R;
import com.example.chulift.demoapplication.httpConnect.ConnectServer;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditTemplateFragment extends Fragment {

    private final String url = Config.projectUrl + "updateTemplate.php";
    private final String deleteURL = Config.projectUrl + "DeleteTemplate.php";
    private static String ARG_SECTION_NUMBER = "section_number";
    private static String ARG_TEMPLATE = "template";
    private int param;
    private Template template;

    public EditTemplateFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            param = getArguments().getInt(ARG_SECTION_NUMBER);
            String jsonObject = getArguments().getString(ARG_TEMPLATE);
            template = new Gson().fromJson(jsonObject, Template.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_template, container, false);
    }

    public static EditTemplateFragment newInstance(int sectionNumber, Template template) {
        EditTemplateFragment fragment = new EditTemplateFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        args.putString(ARG_TEMPLATE, new Gson().toJson(template));
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.section_picker)
    NumberPicker sectionPicker;
    @BindView(R.id.choice_picker)
    NumberPicker choicePicker;
    @BindView(R.id.column_picker)
    NumberPicker columnPicker;
    @BindView(R.id.input_name)
    EditText inputTemplateName;
    @BindView(R.id.num_student_code_picker)
    NumberPicker studentCodePicker;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        init();
    }

    private void init() {
        choicePicker.setMaxValue(NumberPickerConfig.MAX_CHOICE);
        choicePicker.setMinValue(NumberPickerConfig.MIN_CHOICE);
        sectionPicker.setMaxValue(NumberPickerConfig.MAX_SECTION);
        sectionPicker.setMinValue(NumberPickerConfig.MIN_SECTION);
        columnPicker.setMinValue(NumberPickerConfig.MIN_COLUMN);
        columnPicker.setMaxValue(NumberPickerConfig.MAX_COLUMN);
        studentCodePicker.setMinValue(NumberPickerConfig.MIN_STUDENT_CODE);
        studentCodePicker.setMaxValue(NumberPickerConfig.MAX_STUDENT_CODE);

        inputTemplateName.setText(template.getUser_input_template_name());
        columnPicker.setValue(Integer.parseInt(template.getNumberOfCol()));
        choicePicker.setValue(Integer.parseInt(template.getNumberOfChoice()));
        sectionPicker.setValue(Integer.parseInt(template.getAnswerPerCol()));
        studentCodePicker.setValue(Integer.parseInt(template.getNumberOfStudentCode()));
    }

    @OnClick(R.id.delete_button)
    void delete() {
        new AlertDialog.Builder(getContext())
                .setTitle("Confirm delete.")
                .setMessage("ต้องการลบเทมเพลทที่ " + template.getId_template() + " ใช่หรือไม่?")
                .setNegativeButton("ไม่ใช่", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("ใช่", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final ProgressDialog progressDialog = new ProgressDialog(getContext(),
                                R.style.AppTheme_Dark_Dialog);
                        progressDialog.setIndeterminate(true);
                        progressDialog.setMessage("กำลังลบเทมเพลทที่เลือก...");
                        progressDialog.show();
                        final String postBody = "{\"" + "template_name\"" + ":\"" + template.getTemplate_name() + "\"}";
                        new AsyncTask<Void, Void, String>() {
                            @Override
                            protected String doInBackground(Void... voids) {
                                ConnectServer.getJSONObject(deleteURL, postBody);
                                return template.getId_template();
                            }

                            @Override
                            protected void onPostExecute(String s) {
                                Toast.makeText(getContext(), "ลบเทมเพลท " + template.getUser_input_template_name() + " เรียบร้อยแล้ว", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                getContext().startActivity(new Intent(getContext(), ShowTemplateListActivity.class));
                                ((Activity) getContext()).finish();
                            }
                        }.execute();
                    }
                }).show();
    }

    @OnClick(R.id.update_button)
    void update() {
        if (validate()) {

            final ProgressDialog progressDialog = new ProgressDialog(getActivity(),
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("กำลังอัพเดทข้อมูลเทมเพลท...");
            progressDialog.show();

            final RequestBody req = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("id_template", template.getId_template())
                    .addFormDataPart("input_template_name", inputTemplateName.getText().toString().trim())
                    .addFormDataPart("num_column", columnPicker.getValue() + "")
                    .addFormDataPart("num_section", sectionPicker.getValue() + "")
                    .addFormDataPart("num_choice", choicePicker.getValue() + "")
                    .addFormDataPart("num_student_code",String.valueOf(studentCodePicker.getValue()))
                    .build();

            int resp = ConnectServer.connectHttp(url, req);


            if (resp == 200) {
                Toast.makeText(getActivity(), "แก้ไขข้อมูลเทมเพลทเรียบร้อยแล้ว", Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(getActivity(), "ไม่สามารถแก้ไขข้อมูลได้ กรุณาตรวจสอบการเชื่อมต่ออินเทอร์เน็ต", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            //Update values of current template
            template.setAnswerPerCol(sectionPicker.getValue() + "");
            template.setNumberOfChoice(choicePicker.getValue() + "");
            template.setNumberOfCol(columnPicker.getValue() + "");
            template.setNumberOfStudentCode(String.valueOf(studentCodePicker.getValue()));
        }
    }

    private boolean validate() {
        if (inputTemplateName.getText().toString().trim().equals("")) {
            inputTemplateName.setError("กรุณาใส่ชื่อเทมเพลท");
            return false;
        }
        return true;
    }

    @OnClick(R.id.cancel_button)
    void clear() {
        columnPicker.setValue(Integer.parseInt(template.getNumberOfCol()));
        choicePicker.setValue(Integer.parseInt(template.getNumberOfChoice()));
        sectionPicker.setValue(Integer.parseInt(template.getAnswerPerCol()));
        studentCodePicker.setValue(Integer.parseInt(template.getNumberOfStudentCode()));
    }
}