package com.example.roomdatabasercv.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roomdatabasercv.R;
import com.example.roomdatabasercv.model.Student;

import java.util.List;

//item_student
//recycleView

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {

    private List<Student> mListStudent;

    //su dung interface de callback su kie n ra ben ngo ai
    //vi xu ly logic thuoc ham main
    IClickItemStudent iClickItemStudent;
    public interface IClickItemStudent {
        void editStudent(Student student);
        void deleteStudent(Student student);
    }

    public StudentAdapter(IClickItemStudent iClickItemStudent) {
        this.iClickItemStudent = iClickItemStudent;
    }

    public void setData(List<Student> list) {
        this.mListStudent = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student, parent, false);
        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        final Student student = mListStudent.get(position);
        if (student == null) return;
        holder.txt_studentName.setText(student.getStudentName());
        holder.txt_studentID.setText(student.getStudentID());

        holder.btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iClickItemStudent.editStudent(student);
            }
        });

        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iClickItemStudent.deleteStudent(student);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (mListStudent != null) {
            return mListStudent.size();
        }
        return 0;
    }

    public class StudentViewHolder extends RecyclerView.ViewHolder {

        private TextView txt_studentName;
        private TextView txt_studentID;
        private Button btn_edit;
        private Button btn_delete;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_studentName = itemView.findViewById(R.id.txt_studentName);
            txt_studentID = itemView.findViewById(R.id.txt_studentID);
            btn_edit = itemView.findViewById(R.id.btn_edit);
            btn_delete = itemView.findViewById(R.id.btn_delete);
        }
    }
}
