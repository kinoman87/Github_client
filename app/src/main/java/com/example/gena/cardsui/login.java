package com.example.gena.cardsui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;


//пример взят от сюда: http://www.technotalkative.com/lazy-productive-android-developer-4/
public class login extends Activity
{
    EditText edt_txt_login;
    EditText  edt_txt_pass;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //отключение заголовка в активити
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login);
        edt_txt_login = (EditText) findViewById(R.id.Edt_txt_login);
        edt_txt_pass = (EditText) findViewById(R.id.Edt_txt_pass);
    }
    public void Log_in_click(View view)
    {
        String login = edt_txt_login.getText().toString();
        String password = edt_txt_pass.getText().toString();
        Intent intent = new Intent(login.this, MainActivity.class);
        //Передаем параметры на MainActivity активити
        intent.putExtra("login", login);
        intent.putExtra("pass",password);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if (id == R.id.action_settings)
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



}
