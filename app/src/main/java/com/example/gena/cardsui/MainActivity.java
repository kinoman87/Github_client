package com.example.gena.cardsui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.widgets.ProgressDialog;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.RepositoryService;
import org.eclipse.egit.github.core.service.UserService;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.CardThumbnail;
import it.gmariotti.cardslib.library.view.CardListView;


//пример взят от сюда: http://www.technotalkative.com/lazy-productive-android-developer-4/
public class MainActivity extends Activity implements View.OnClickListener

{
    // Перечисление TransitionType создано только для демонстрации нескольких анимаций
    public static enum TransitionType
    {
        Zoom, SlideLeft, Diagonal
    }
    public static TransitionType transitionType;
    GitHub task;
    FloatingActionButton btn_add_rep;
    FloatingActionButton btn_about_user;
    FloatingActionButton btn_exit;
    FloatingActionButton btn_logoff;
    FloatingActionsMenu menu;


    Animation anim; // анимация для появление  float button action

    String login;
    String pass;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //анимация появления меню
        anim = AnimationUtils.loadAnimation(this, R.anim.scale_anim);
        menu = (FloatingActionsMenu) findViewById(R.id.multiple_actions_down);
        menu.startAnimation(anim);

        setBitmap();
        btn_add_rep = (FloatingActionButton)findViewById(R.id.button_add_rep);
        btn_about_user = (FloatingActionButton)findViewById(R.id.button_about_user);
        btn_exit = (FloatingActionButton)findViewById(R.id.button_exit);
        btn_logoff=(FloatingActionButton)findViewById(R.id.button_logoff);
       // ListRepos=(CardListView)findViewById(R.id.myList);
        // устанавливаем один обработчик для всех кнопок
        btn_add_rep.setOnClickListener(this);
        btn_about_user.setOnClickListener(this);
        btn_exit.setOnClickListener(this);
        btn_logoff.setOnClickListener(this);

        login = getIntent().getExtras().getString("login");
        pass = getIntent().getExtras().getString("pass");

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
    public void setBitmap()
    {
        try
        {
            task = new GitHub(this,login,pass);
            task.execute();
        } catch (Exception e)
        {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.button_about_user: task.View_about_user();
             break;
            case R.id.button_logoff:
                finish();
                Intent intent = new Intent(MainActivity.this, login.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                break;
            case R.id.button_add_rep:
                Intent intent2 = new Intent(MainActivity.this, add_Repository.class);
                intent2.putExtra("login", login);
                intent2.putExtra("pass",pass);
                startActivity(intent2);
                transitionType = TransitionType.SlideLeft;
                overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
             break;




            case R.id.button_exit: finish();
                break;
         }
    }

    private class GitHub extends AsyncTask<Void, Void, Void>
    {
        private ProgressDialog spinner;
        public Context mcontext;
        ArrayList<String> list = new ArrayList<String>();
        public    String st;
        public    String st2;
        String textResult;
        public String mlogin;
        public String mpass;

        Bitmap bmImg = null;

        public GitHub(Context context, String login, String pass) throws Exception
        {
            mlogin=login;
            mpass=pass;
            mcontext=context;
        }
        @Override
        protected void onPreExecute()
        {
            // Вначале мы покажем пользователю ProgressDialog
            // чтобы он понимал что началась загрузка
            // этот метод выполняется в UI потоке

            spinner = new ProgressDialog(mcontext,"Идет загрузка...");
            spinner.show();
        }
        public void View_about_user()
        {
            final Dialog dialog = new Dialog(mcontext);

            //setting custom layout to dialog
            dialog.setContentView(R.layout.custom_view_dialog);
            dialog.setTitle("About user");

            ImageView image = (ImageView)dialog.findViewById(R.id.image);
            //image.setImageResource(R.drawable.github);
            image.setImageBitmap(bmImg);
            TextView text = (TextView)dialog.findViewById(R.id.text);
            text.setText(st2);
          //image.setImageDrawable(BitmapFactory.decodeResource(mcontext.getResources(), R.drawable.github));

            //adding button click event
            Button dismissButton = (Button) dialog.findViewById(R.id.declineButton);
            dismissButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
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
                client.setCredentials(mlogin,mpass);
                RepositoryService service = new RepositoryService();


                UserService userService = new UserService(client);
                User ghUser = userService.getUser(mlogin);

                st2= "Name: "+ghUser.getName()+ "\n";
                st2= st2+"Email: "+ghUser.getEmail()+ "\n";
                st2= st2+"Location: "+ghUser.getLocation()+ "\n";
                st2=st2+"Followers:"+ghUser.getFollowers();
                String gravatarUrl = ghUser.getAvatarUrl();

                myfileurl= new URL(gravatarUrl);
                HttpURLConnection conn= (HttpURLConnection)myfileurl.openConnection();
                conn.setDoInput(true);
                conn.connect();
                int length = conn.getContentLength();
                if(length>0)
                {
                    int[] bitmapData =new int[length];
                    byte[] bitmapData2 =new byte[length];
                    InputStream is = conn.getInputStream();
                    bmImg = BitmapFactory.decodeStream(is);
                }

             for (Repository repo : service.getRepositories(login))
                {
                    textResult="";
                    list.add(repo.getName());
                    st="Watchers: "+Integer.toString(repo.getWatchers())+ "\n";
                    st=st+"Forks: "+Integer.toString(repo.getForks())+ "\n";
                    repo.setDescription("a repo");
                    st=st+"Description: "+repo.getDescription().toString()+ "\n";
                }
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
            ArrayList<Card> cards = new ArrayList<Card>();

           TextView mSecondaryTitle;

            for(int i = 0; i < list.size(); i++)
            {
                //Создаем карточку
                Card card2 = new Card(mcontext, R.layout.card_text);
                //Создаем заголовок карточки
                CardHeader header2 = new CardHeader(mcontext, R.layout.row_card);
                //Добавляем текст заголовока
                textResult =textResult+list.get(i);// repo.getName();
                header2.setTitle(list.get(i));
                //Добавляем текст заголовока внутри карточки
                card2.setTitle(st);
                CardThumbnail thumb2 = new CardThumbnail(mcontext);
                thumb2.setDrawableResource(R.drawable.github);
                card2.addCardThumbnail(thumb2);
                //Добавляем заголовок карточки к карточке
                card2.addCardHeader(header2);

                //Set onClick listener
                card2.setOnClickListener(new Card.OnCardClickListener()
                {
                    @Override
                    public void onClick(Card card, View view)
                    {
                        //Toast.makeText(mcontext, "Clickable card : " + card.getCardHeader().getTitle(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(mcontext, viewcommits.class);
                        //Передаем параметры на MainActivity активити
                        //intent.putExtra("login", login);
                        intent.putExtra("login",mlogin);
                        intent.putExtra("pass",mpass);
                        intent.putExtra("repos",card.getCardHeader().getTitle());

                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                    }
                });
                cards.add(card2);
            }

            CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(mcontext, cards);
            CardListView listView = (CardListView) findViewById(R.id.myList);
             if (listView != null)
             {
               listView.setAdapter(mCardArrayAdapter);
             }
         }

    }
}
