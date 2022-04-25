package com.example.roomdatabasercv;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.roomdatabasercv.adapter.StudentAdapter;
import com.example.roomdatabasercv.database.StudentDatabase;
import com.example.roomdatabasercv.model.Student;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int MY_REQUEST_CODE = 10;
    private TextView txt_delete_all;
    private EditText edt_studentName;
    private EditText edt_studentID;
    private EditText edt_search_student;
    private Button btn_addStudent;
    private RecyclerView rcv_student;

    private StudentAdapter studentAdapter;
    private List<Student> mListStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        studentAdapter = new StudentAdapter(new StudentAdapter.IClickItemStudent() {
            @Override
            public void editStudent(Student student) {
                clickEditStudent(student);
            }

            @Override
            public void deleteStudent(Student student) {
                clickDeleteStudent(student);
            }
        });
        mListStudent = new ArrayList<>();
        studentAdapter.setData(mListStudent);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcv_student.setLayoutManager(linearLayoutManager);

        rcv_student.setAdapter(studentAdapter);

        loadDataToRcv();

        //tha o tac voi database
        btn_addStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addStudent();
            }
        });

        txt_delete_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickDeleteAllStudent();
            }
        });

        //ham xu ly su kien khi nhan vao nut tim kiem o ban phim
        edt_search_student.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    //LOGIC SEARCH
                    handleSearchStudent();
                }
                return false;
            }
        });

        loadDataToRcv();

    }

    private void handleSearchStudent() {
        String strKeyWord = edt_search_student.getText().toString().trim();
        mListStudent = new ArrayList<>();
        mListStudent = StudentDatabase.getInstance(this).studentDAO().searchStudent(strKeyWord);
        studentAdapter.setData(mListStudent);
        hideSoftKeyBoard();
    }

    private void clickDeleteAllStudent() {
        new AlertDialog.Builder(this)
                .setTitle("Confirm delete student")
                .setMessage("Are you sure")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //delete student
                        StudentDatabase.getInstance(MainActivity.this).studentDAO().deleteAllStudent();
                        Toast.makeText(MainActivity.this, "Delete all student successfully", Toast.LENGTH_LONG).show();
                        loadDataToRcv();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void addStudent() {
        String strStudentName = edt_studentName.getText().toString().trim();
        String strStudentID = edt_studentID.getText().toString().trim();

        //neu 1 trong 2 khong duoc nhap thi khong ap vào trong database
        if (TextUtils.isEmpty(strStudentName) || TextUtils.isEmpty(strStudentID)) {
            Toast.makeText(this, "Pls enter full StudentName & StudentID", Toast.LENGTH_LONG).show();
            return;
        }

        Student student = new Student(strStudentName, strStudentID);

        //check xem student da ton tai hay chua
        if (isStudentExist(student)) {
            Toast.makeText(this, "Student exist", Toast.LENGTH_LONG).show();
            return;
        }

        //insert vao database
        StudentDatabase.getInstance(this).studentDAO().insertStudent(student);
        Toast.makeText(this, "Add student successfully", Toast.LENGTH_LONG).show();

        //dat lai 2 cai edt
        edt_studentName.setText("");
        edt_studentID.setText("");

        //an ban phi m
        hideSoftKeyBoard();

        //hie n thi list du lieu len recyclerview
        loadDataToRcv();
    }

    public void hideSoftKeyBoard() {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }

    private void initView() {
        edt_studentName = findViewById(R.id.edt_studentName);
        edt_studentID = findViewById(R.id.edt_studentID);
        btn_addStudent = findViewById(R.id.btn_addStudent);
        rcv_student = findViewById(R.id.rcv_student);
        txt_delete_all = findViewById(R.id.txt_delete_all);
        edt_search_student = findViewById(R.id.edt_search_student);
    }

    private void loadDataToRcv() {
        mListStudent = StudentDatabase.getInstance(this).studentDAO().getListStudent();
        studentAdapter.setData(mListStudent);
    }

    private boolean isStudentExist(Student student) {
        List<Student> list = StudentDatabase.getInstance(this).studentDAO().checkStudent(student.getStudentName());
        return list != null && !list.isEmpty();
    }

    private void clickEditStudent(Student student) {
        Intent intent = new Intent(getApplicationContext(), EditActivity.class);

        // ham truyen du lieu qua edit_activity
        // giao tiep du lieu giua 2 activity
        Bundle bundle = new Bundle();
        bundle.putSerializable("object_student", student);
        intent.putExtras(bundle);
        startActivityForResult(intent, MY_REQUEST_CODE);
    }

    private void clickDeleteStudent(Student student) {
        new AlertDialog.Builder(this)
                .setTitle("Confirm delete student")
                .setMessage("Are you sure")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //delete student
                        StudentDatabase.getInstance(MainActivity.this).studentDAO().deleteStudent(student);
                        Toast.makeText(MainActivity.this, "Delete student successfully", Toast.LENGTH_LONG).show();
                        loadDataToRcv();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }


    //lay ket qua tu edit_activity ve
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            loadDataToRcv();
        }
    }
}
