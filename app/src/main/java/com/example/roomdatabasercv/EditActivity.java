package com.example.roomdatabasercv;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.roomdatabasercv.database.StudentDatabase;
import com.example.roomdatabasercv.model.Student;

public class EditActivity extends AppCompatActivity {

    private ImageView reg_back;
    private EditText edt_studentName;
    private EditText edt_studentID;
    private Button btn_editStudent;

    private Student mStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        initView();

        //ham return ve activity_main
        reg_back();

        //ham nhan du lieu tu main_activity
        getDataActivity();
    }

    private void getDataActivity() {
        mStudent = (Student) getIntent().getExtras().get("object_student");
        if (mStudent != null) {
            edt_studentName.setText(mStudent.getStudentName());
            edt_studentID.setText(mStudent.getStudentID());
        }

        btn_editStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ham chinh sua Student
                editStudent();
            }
        });
    }

    private void editStudent() {
        String strStudentName = edt_studentName.getText().toString().trim();
        String strStudentID = edt_studentID.getText().toString().trim();

        //neu 1 trong 2 khong duoc nhap thi khong ap vào trong database
        if (TextUtils.isEmpty(strStudentName) || TextUtils.isEmpty(strStudentID)) {
            Toast.makeText(this, "Pls enter full StudentName & StudentID", Toast.LENGTH_LONG).show();
            return;
        }

        //edit user
        mStudent.setStudentName(strStudentName);
        mStudent.setStudentID(strStudentID);

        //update len database
        StudentDatabase.getInstance(this).studentDAO().editStudent(mStudent);
        Toast.makeText(this, "Edit successfully", Toast.LENGTH_LONG).show();

        //quay tro lai activity_main de load lai du lieu
        Intent intent_result = new Intent();
        setResult(Activity.RESULT_OK, intent_result);
        finish();

    }

    private void initView() {
        reg_back = findViewById(R.id.reg_back);
        edt_studentName = findViewById(R.id.edt_studentName);
        edt_studentID = findViewById(R.id.edt_studentID);
        btn_editStudent = findViewById(R.id.btn_editStudent);
    }

    private void reg_back() {
        reg_back = (ImageView) findViewById(R.id.reg_back);
        reg_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_reg_back = new Intent(getApplicationContext(), MainActivity.class);
                intent_reg_back.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent_reg_back);
            }
        });
    }
}