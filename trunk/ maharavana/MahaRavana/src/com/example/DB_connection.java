/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hp
 */
public class DB_connection {

    private  Connection conn;
    private Statement stat;
    private Messenger my_console;

    public DB_connection(Messenger m){
        my_console = m;
        try {
            Class.forName("org.sqlite.JDBC");
            //conn = DriverManager.getConnection("jdbc:sqlite:C://Users//hp//Desktop//MahaRavana//db//MahaRavana");
            conn = DriverManager.getConnection("jdbc:sqlite:D://MahaRawana//MahaRavana//db//new_test.db");
            stat = conn.createStatement();
        }catch (ClassNotFoundException ex) {
            //Logger.getLogger(Tagger.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            //Logger.getLogger(Tagger.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    public ArrayList<String> get_tags_for(String type){

        ArrayList<String> last_word_tags_arr = new ArrayList<String>();

        String q_string = "SELECT tag FROM TagForChunking WHERE chunk_for=\""+ type + "\";";

        try{
            ResultSet rs = stat.executeQuery(q_string);

            while (rs.next()) {
                last_word_tags_arr.add(rs.getString("tag"));
            }

            rs.close();
        } catch (SQLException ex) {
                    //Logger.getLogger(Tagger.class.getName()).log(Level.SEVERE, null, ex);
        }

        return last_word_tags_arr;
    }


    public ArrayList<String> get_tags(String word) {

        ArrayList<String> tag_set = new ArrayList<String>();

        String q_string = "SELECT tag FROM TagDict WHERE Word=\"" + word + "\";";

        try{
            ResultSet rs = stat.executeQuery(q_string);
            //ResultSet rs = stat.executeQuery("SELECT * FROM TagDict;");

            while (rs.next()) {
                tag_set.add(rs.getString("tag"));
            }

            rs.close();
        } catch (SQLException ex) {
                    //Logger.getLogger(Tagger.class.getName()).log(Level.SEVERE, null, ex);
        }

        return tag_set;
    }

    public void colse_conn() throws Exception{
        conn.close();
    }

    //return word object array of words whose stem is = to that of the input word
    public ArrayList<Word> get_related_words(String word){
        String stem_no = "";

        try {
            String q_string = "SELECT stem FROM TagDict WHERE  word=\"" + word + "\" LIMIT 1;";
            ResultSet rs;

            rs = stat.executeQuery(q_string);
            stem_no =  rs.getString("stem");

            //new Messenger(stem_no);

            //for default value of stem_no -1
            if(stem_no.equals("-1")){
                return new ArrayList<Word>();
            }

            rs.close();

            //new Messenger(stem_no);
         } catch (SQLException ex) {
            //Logger.getLogger(Tagger.class.getName()).log(Level.SEVERE, null, ex);
           my_console.write("expception 1");
        }

        return get_words_for_stem_no(stem_no);
    }


    private ArrayList<Word> get_words_for_stem_no(String stem_no){
        ArrayList<Word> related_words = new ArrayList<Word>();

        //String r_w_s = "";

         try {
            //has an index "stem_no", on stem_no
            String q_string_2 = "SELECT word FROM TagDict WHERE stem=\"" + stem_no + "\";";

            ResultSet rs = stat.executeQuery(q_string_2);

            while (rs.next()) {
                String related_word =  rs.getString("word");

                //new Messenger(related_word);

                //this does not work as get_tag will close this result set also
                //related_words.add(new Word(related_word, get_tags(related_word)));

                related_words.add(new Word(related_word));
                //r_w_s += related_word +", " ;
            }

            rs.close();
        } catch (SQLException ex) {
            //Logger.getLogger(Tagger.class.getName()).log(Level.SEVERE, null, ex);
           my_console.write("expception 2");
        }

        for (int i = 0; i < related_words.size(); i++) {
            related_words.get(i).set_tags(get_tags(related_words.get(i).get_word()));
        }

        //my_consol.write(r_w_s);

        return related_words;
    }


    public ArrayList<String> get_possible_tags_for_tag_after(String pre_tag, String center_tag){
        ArrayList<String> possible_tags_after = new ArrayList<String>();

        try {
            //has an index last_token_check : index on tagBefor and tagMiddle
            String q_string = "SELECT TagAfter FROM Patterns WHERE  TagBefore=\"" + pre_tag + "\" AND TagMiddle=\"" + center_tag + "\";";
            ResultSet rs = stat.executeQuery(q_string);
            //ResultSet rs = stat.executeQuery("SELECT * FROM TagDict;");
            while (rs.next()) {
                //System.out.println("word = " + rs.getString("word"));
                //System.out.println("job = " + rs.getString("occupation"));
                //tag_set += "<" + rs.getString("tag") + ">";
                possible_tags_after.add(rs.getString("TagAfter"));
            }
            rs.close();
        } catch (SQLException ex) {
            //Logger.getLogger(Tagger.class.getName()).log(Level.SEVERE, null, ex);
        }

        return possible_tags_after;
    }

    public boolean check_pattern(String[] tags)
    {
        boolean isMatched= true;

        String q_string = "Select * from Patterns where TagBefore=\""+tags[0]+"\" AND TagMiddle=\""+tags[1]+"\" AND TagAfter=\""+tags[2] + "\" LIMIT 1;";
        //String q_string = "Select * from TagPatterns where TagSequence=\""+tags[0]+"," + tags[1]+"," + tags[2] + "\"";

        ResultSet rs;
        try {
            rs = stat.executeQuery(q_string);
            if(!rs.next())
             {
                 isMatched=false;
             }
            rs.close();
        } catch (SQLException ex) {
            //Logger.getLogger(Tagger.class.getName()).log(Level.SEVERE, null, ex);
        }

        return isMatched;
    }

    public boolean check_tags_in_sim_set(String tag){
            String q_string = "SELECT count(*) as no FROM tags_in_sim_set WHERE tag=\""+ tag +"\" LIMIT 1;";

            boolean return_val = false;
            
            try {
                ResultSet rs = stat.executeQuery(q_string);

                if(rs.next()) {
                    if(rs.getString("no").equals("1")){
                        return_val = true;
                    }
                }
                rs.close();
            } catch (SQLException ex) {
                Logger.getLogger(DB_connection.class.getName()).log(Level.SEVERE, null, ex);
            }
            return return_val;
    }

}
