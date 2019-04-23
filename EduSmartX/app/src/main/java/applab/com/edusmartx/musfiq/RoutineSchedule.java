package applab.com.edusmartx.musfiq;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import java.util.ArrayList;

import applab.com.edusmartx.R;

/**
 * Created by musfiq on 4/9/19.
 */



public class RoutineSchedule extends AppCompatActivity {


    public EditText i1c1,i1c2,i2c1,i2c2,i3c1,i3c2, classes, hours;

    public ClassAgent classAgent[];

    public ClassroomInstance assingment[]= new ClassroomInstance[100];




    public boolean isOK(int agentno,ClassroomInstance classroomInstance){

        for(int i=0;i<agentno;i++){

            if(assingment[i].equals(classroomInstance))
                return false;
        }

        return true;

    }

    public boolean BackTracK(int agentno){

        int classno=Integer.parseInt(classes.getText().toString());
        if(agentno==classno){
            return true;
        }


        for(int i=0;i<classAgent[agentno].domain.size(); i++){

            if(isOK(agentno,classAgent[agentno].domain.get(i))==true) {
                assingment[agentno] = classAgent[agentno].domain.get(i);

                Boolean ret=BackTracK(agentno+1);

                if(ret==false)
                    assingment[agentno]=null;
                else return true;

            }

        }

        return false;

    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.routine_schedule);

        i1c1=(EditText) findViewById(R.id.i1c1);
        i1c2=(EditText) findViewById(R.id.i1c2);
        i2c1=(EditText) findViewById(R.id.i2c1);
        i2c2=(EditText) findViewById(R.id.i2c2);
        i3c1=(EditText) findViewById(R.id.i3c1);
        i3c2=(EditText) findViewById(R.id.i3c2);
        classes=(EditText) findViewById(R.id.classes);
        hours=(EditText) findViewById(R.id.hours);

        int i1_c1=Integer.parseInt(i1c1.getText().toString());
        int i1_c2=Integer.parseInt(i1c2.getText().toString());
        int i2_c1=Integer.parseInt(i2c1.getText().toString());
        int i2_c2=Integer.parseInt(i2c2.getText().toString());
        int i3_c1=Integer.parseInt(i3c1.getText().toString());
        int i3_c2=Integer.parseInt(i3c2.getText().toString());

        int classNo= Integer.parseInt(classes.getText().toString());
        int hoursNo= Integer.parseInt(hours.getText().toString());

        classAgent= new ClassAgent[classNo];

        for( int i=0;i<classNo;i++){


            ArrayList<ClassroomInstance>arrayList=new ArrayList<ClassroomInstance>();

            for(int j=0;j<hoursNo;j++){
                arrayList.add(new ClassroomInstance(1,i1_c1,j));
                arrayList.add(new ClassroomInstance(1,i1_c2,j));

                System.out.println("1,"+i1_c1+","+i1_c2+","+j);
            }

            classAgent[i]= new ClassAgent(i, arrayList);
        }


        BackTracK(0);


        System.out.println("result:->>>");
        for(int i=0;i<classNo;i++){

            System.out.println("In classroom "+i);
            System.out.println("instructor="+assingment[i].instructor+",course="+assingment[i].selectedCourse+",hour="+assingment[i].instructor);



        }


    }
}
