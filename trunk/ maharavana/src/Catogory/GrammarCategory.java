/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Catogory;

import com.example.Word;
import java.util.ArrayList;

/**
 *
 * @author Angel
 */
public class GrammarCategory {

       public boolean isOrPattern(ArrayList<Word> words) {
        ArrayList<Word> wordsWithoutAdjAdv=new ArrayList<Word>();
        int lastOrIndex=0;
        int orCount=0;
       // throw new UnsupportedOperationException("Not yet implemented");
      wordsWithoutAdjAdv= removeAdjAdv(words);
        for (int i=(wordsWithoutAdjAdv.size()-1);i>=0;i--){

            for(int j=0;j<wordsWithoutAdjAdv.get(i).num_tags();j++){

                if(wordsWithoutAdjAdv.get(i).get_tag(j).equals("OR")){
                   if(orCount==0)
                       lastOrIndex=i;
                    orCount++;

            }

            }

        }


           // if(orCount*2==(lastOrIndex-1))
             if(orCount>0)
                 return true;
             else
                return false;



    }

    public ArrayList<Word> removeAdjAdv(ArrayList<Word> words) {
      //  throw new UnsupportedOperationException("Not yet implemented");
         for (int i=0;i<words.size();i++){
            for(int j=0;j<words.get(i).num_tags();j++){

                if((words.get(i).get_tag(j).equals("JJ")) ||(words.get(i).get_tag(j).equals("RB")) )
                    words.remove(i);


            }

        }
         return words;
    }

    public ArrayList<Word> checkOrPattern(ArrayList<Word> words) {
        ArrayList<Word> wordsWithoutAdjAdv=new ArrayList<Word>();
        ArrayList<Word> filteredWords=new ArrayList<Word>();
        int lastOrIndex=0;
        int orCount=0;
       // throw new UnsupportedOperationException("Not yet implemented");
      wordsWithoutAdjAdv= removeAdjAdv(words);
        for (int i=(wordsWithoutAdjAdv.size()-1);i>=0;i--){
            for(int j=0;j<wordsWithoutAdjAdv.get(i).num_tags();j++){

                if(wordsWithoutAdjAdv.get(i).get_tag(j).equals("OR")){
                   if(orCount==0)
                       lastOrIndex=i;
                    orCount++;

            }

            }

        }
            wordsWithoutAdjAdv.remove(lastOrIndex);


        for (int k=(lastOrIndex-1);k<wordsWithoutAdjAdv.size();k++){
            //filteredWords.add(k, wordsWithoutAdjAdv.get(k));
             filteredWords.add( wordsWithoutAdjAdv.get(k));
        }

    filteredWords.add(0,wordsWithoutAdjAdv.get(0));
return filteredWords;


}

}
