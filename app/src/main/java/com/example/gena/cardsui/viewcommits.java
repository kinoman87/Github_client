package com.example.gena.cardsui;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

import com.gc.materialdesign.widgets.ProgressDialog;

import java.net.URL;

/**
 *  Просмотр всех коммитов репозитория
 */
public class viewcommits  extends Activity
{
    String login;
    String pass;
    String repos;
    GitHub task;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        //отключение заголовка в активити
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.view_all_commits);
        login = getIntent().getExtras().getString("login");
        pass = getIntent().getExtras().getString("pass");
        repos= getIntent().getExtras().getString("repos");
        try
        {
            task = new GitHub(this,login,pass);
            task.execute();
        } catch (Exception e)
        {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
       // Toast.makeText(this,login+pass+repos, Toast.LENGTH_SHORT).show();
    }
    private class GitHub extends AsyncTask<Void, Void, Void>
    {
        private ProgressDialog spinner;
        public Context mcontext;
        public String login;
        public String pass;
        String textResult="";

        public GitHub(Context context,String mlogin,String mpass) throws Exception
        {
            login=mlogin;
            mcontext=context;
            pass=mpass;
        }
        @Override
        protected void onPreExecute()
        {
            // Вначале мы покажем пользователю ProgressDialog
            // чтобы он понимал что началась загрузка
            // этот метод выполняется в UI потоке
            spinner = new ProgressDialog(mcontext,"Загрузка коммитов...");
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

                //здесь надо будет сделать загрузку коммитов
            }
            catch (Exception e)
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
            Toast.makeText(mcontext,textResult, Toast.LENGTH_SHORT).show();
        }

    }
}
