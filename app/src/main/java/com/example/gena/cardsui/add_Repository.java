package com.example.gena.cardsui;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gc.materialdesign.widgets.ProgressDialog;
import com.getbase.floatingactionbutton.FloatingActionButton;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.RepositoryService;

import java.io.IOException;
import java.net.URL;


public class add_Repository extends Activity implements View.OnClickListener
{
    GitHub task;
    EditText  edt_txt_name;
    Button button_back;
    EditText edt_txt_desc;
    FloatingActionButton btn_add;
    Animation anim; // анимация для появление  float button action
    String login;
    String pass;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        //отключение заголовка в активити
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add__repository);
        edt_txt_name = (EditText) findViewById(R.id.Edt_txt_name);
        edt_txt_desc= (EditText) findViewById(R.id.Edt_txt_desc);
        button_back = (Button) findViewById(R.id.BackButton);

        //анимация появления меню
        anim = AnimationUtils.loadAnimation(this, R.anim.scale_anim_add);
        btn_add = (FloatingActionButton) findViewById(R.id.button_add);
        btn_add.startAnimation(anim);

        btn_add.setOnClickListener(this);
        button_back.setOnClickListener(this);
        login = getIntent().getExtras().getString("login");
        pass = getIntent().getExtras().getString("pass");

    }
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.button_add:
                String name = edt_txt_name.getText().toString();
                String description = edt_txt_desc.getText().toString();
                try
                {
                    task = new GitHub(this,name,description,login,pass);
                    task.execute();
                } catch (Exception e)
                {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                }

                edt_txt_name.setText("");
                edt_txt_desc.setText("");
                Toast.makeText(getApplicationContext(),"Ваш репозиторий добавлен", Toast.LENGTH_LONG).show();
                break;
            case R.id.BackButton:
                button_back.setBackgroundResource(R.drawable.strelka_click);
                finish();
                // Проверяем с помощью какой анимации мы перешли, по той же анимации и возвращаемся
                if (MainActivity.transitionType == MainActivity.TransitionType.SlideLeft)
                {
                    // Для SecondActivity устанавливаем анимацию перехода
                    overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                }
                break;
        }
    }

    //Обработка кнопки нажатия добавить репозиторий
    public void Add(View view)
    {
        String name = edt_txt_name.getText().toString();
        String description = edt_txt_desc.getText().toString();
        try
        {
            task = new GitHub(this,name,description,login,pass);
            task.execute();
        } catch (Exception e)
        {
           e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }

        edt_txt_name.setText("");
        edt_txt_desc.setText("");
        Toast.makeText(getApplicationContext(),"Ваш репозиторий добавлен", Toast.LENGTH_LONG).show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add__repository, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class GitHub extends AsyncTask<Void, Void, Void>
    {
        private ProgressDialog spinner;
        public Context mcontext;
        public String mname;
        public String mdescription;
        public String mlogin;
        public String mpass;
        public GitHub(Context context,String name,String description,String login,String pass) throws Exception
        {
        mlogin=login;
        mpass=pass;
        mname=name;
        mdescription=description;
        mcontext=context;
        }
    @Override
    protected void onPreExecute()
    {
        // Вначале мы покажем пользователю ProgressDialog
        // чтобы он понимал что началась загрузка
        // этот метод выполняется в UI потоке
        spinner = new ProgressDialog(mcontext,"Добавление репозитория...");
        spinner.show();
    }
    @Override
    protected Void doInBackground(Void... params)
    {
        // тут мы делаем основную работу по загрузке данных
        // этот метод выполяется в другом потоке
        URL myfileurl =null;

        try
        {
            GitHubClient client = new GitHubClient();
            client.setCredentials(mlogin, mpass);
           RepositoryService service=new RepositoryService(client);
           Repository repository=new Repository();
           repository.setOwner(new User().setLogin(client.getUser()));

           repository.setName(mname);
          repository.setDescription(mdescription);
           repository.setPrivate(false);
           Repository created=service.createRepository(repository);


        }
       catch (IOException e)
        {
            e.printStackTrace();
       }
        return null;
    }
    @Override
    protected void onPostExecute(Void result)
    {
        // Загрузка закончена. Закроем ProgressDialog.
        // этот метод выполняется в UI потоке
        spinner.dismiss();
        //Toast.makeText(getApplicationContext(),edt_txt_name.getText(), Toast.LENGTH_LONG).show();
       // Toast.makeText(getApplicationContext(),"Репозиторий добавлен", Toast.LENGTH_LONG).show();

    }

}



}
