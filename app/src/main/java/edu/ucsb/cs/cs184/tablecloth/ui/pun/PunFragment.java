package edu.ucsb.cs.cs184.tablecloth.ui.pun;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Random;

import edu.ucsb.cs.cs184.tablecloth.R;

public class PunFragment extends Fragment implements View.OnClickListener {

    private PunViewModel punViewModel;
    private View root;
    private String html;
    ArrayList<String> puns;
    int rand1;
    Random rand;
    Document doc;
    String url;
    Elements answers;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        punViewModel =
                ViewModelProviders.of(this).get(PunViewModel.class);
        root = inflater.inflate(R.layout.fragment_pun, container, false);

        Button btn = (Button)root.findViewById(R.id.buttonPun);
        puns = new ArrayList<String>();

        downloadThread.start();
        btn.setOnClickListener(this);

        return root;
    }

    @Override
    public void onActivityCreated(Bundle state){
        super.onActivityCreated(state);
    }

    @Override
    public void onClick(View v){
        setPun();
    }

    public void setPun(){
        TextView txt = (TextView)root.findViewById(R.id.textViewPun);
        rand = new Random();
        rand1 = rand.nextInt(puns.size());
//        System.out.println(puns.get(rand1));
//        System.out.println(puns);
//        System.out.println(rand1);
        if(rand1 == 26 || rand1== 27){
            rand1 = rand.nextInt(puns.size());
        }
        txt.setText(puns.get(rand1));
        return;
    }

    Thread downloadThread = new Thread(){
        public void run() {
            try {
                String url = "https://thoughtcatalog.com/maria-monrovia/2018/06/food-puns/";
                Document doc = Jsoup.connect(url).get();
                Elements answers = doc.select("p");

                ListIterator<Element> p = answers.listIterator();

                while (p.hasNext()) {
//                    p.next();
//                    if(p.next().text().matches(".*\\d.*") && p.next().text().contains(".")){
                     String  pu = p.next().text();
                    if(!pu.contains("You may unsubscribe at any time.") && !pu.contains("By subscribing, you agree to the terms") &&
                        !pu.contains("Follow Thought") && !pu.contains("Dedicated to your stories") && !pu.contains("Submit your writing") &&
                        !pu.contains("Learn more about working with") && !pu.contains("Trust me, they’ll fill") && !pu.contains("Add your own"))
                        puns.add(p.next().text());
//                    }
                }
            } catch (IOException e) {
                System.out.println("Failed to get webpage");
                System.out.println(e.getMessage());
            }
        }
    };

}
