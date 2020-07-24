package com.example.bookclub;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class FetchBook extends AsyncTask<String,Void,String> {

    // Variables for the search input field, and results TextViews
    private EditText mBookInput;
   // private TextView mTitleText;
    //private TextView mAuthorText;
    private RecyclerView recyclerView;
    private ArrayList<Books> arrayBooksList;
    private CardAdapter_search_and_add adapter;
    private CardAdapter_search_frag adapter_2;

    private int usecode;

    // Class name for Log tag
    private static final String LOG_TAG = FetchBook.class.getSimpleName();

    // Constructor providing a reference to the views in MainActivity
    public FetchBook(EditText bookInput, ArrayList<Books> arrayBooksList,CardAdapter_search_frag adapter_2,int usecode) {
        //this.mTitleText = titleText;
        this.usecode =usecode;
        //this.mAuthorText = authorText;
        this.mBookInput = bookInput;
        this.arrayBooksList = arrayBooksList;
        this.adapter_2=adapter_2;

    }
    public FetchBook(EditText bookInput, ArrayList<Books> arrayBooksList, CardAdapter_search_and_add adapter,int usecode)
    {
        this.usecode =usecode;
        //this.mAuthorText = authorText;
        this.mBookInput = bookInput;
        this.arrayBooksList = arrayBooksList;
        this.adapter=adapter;

    }





    /**
     * Makes the Books API call off of the UI thread.
     *
     * @param params String array containing the search data.
     * @return Returns the JSON string from the Books API or
     *         null if the connection failed.
     */
    @SuppressLint("WrongThread")
    @Override
    protected String doInBackground(String... params) {

        // Get the search string
        String queryString = params[0];


        // Set up variables for the try block that need to be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String bookJSONString = null;

        // Attempt to query the Books API.
        try {
            // Base URI for the Books API.
            final String BOOK_BASE_URL =  "https://www.googleapis.com/books/v1/volumes?";

            final String QUERY_PARAM = "q"; // Parameter for the search string.
            final String MAX_RESULTS = "maxResults"; // Parameter that limits search results.
            final String PRINT_TYPE = "printType"; // Parameter to filter by print type.

            // Build up your query URI, limiting results to 10 items and printed books.
            Uri builtURI = Uri.parse(BOOK_BASE_URL).buildUpon()
                    .appendQueryParameter(QUERY_PARAM, queryString)
                    .appendQueryParameter(MAX_RESULTS, "10")
                    .appendQueryParameter(PRINT_TYPE, "books")
                    .build();

            URL requestURL = new URL(builtURI.toString());

            // Open the network connection.
            urlConnection = (HttpURLConnection) requestURL.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Get the InputStream.
            InputStream inputStream = urlConnection.getInputStream();

            // Read the response string into a StringBuilder.
            StringBuilder builder = new StringBuilder();

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // but it does make debugging a *lot* easier if you print out the completed buffer for debugging.
                builder.append(line + "\n");
            }

            if (builder.length() == 0) {
                // Stream was empty.  No point in parsing.
                // return null;
                return null;
            }
            bookJSONString = builder.toString();

            // Catch errors.
        } catch (IOException e) {
            e.printStackTrace();

            // Close the connections.
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

        // Return the raw response.
        return bookJSONString;
    }

    /**
     * Handles the results on the UI thread. Gets the information from
     * the JSON and updates the Views.
     *
     * @param s Result from the doInBackground method containing the raw JSON response,
     *          or null if it failed.
     */
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {
            // Convert the response into a JSON object.
            JSONObject jsonObject = new JSONObject(s);
            // Get the JSONArray of book items.
            JSONArray itemsArray = jsonObject.getJSONArray("items");

            // Initialize iterator and results fields.
            int i = 0;
            String title = null;
            String authors = null;

            // Look for results in the items array, exiting when both the title and author
            // are found or when all items have been checked.
            while (i < itemsArray.length()) {
                // Get the current item information.
                JSONObject book = itemsArray.getJSONObject(i);
                JSONObject volumeInfo = book.getJSONObject("volumeInfo");



                // Try to get the author and title from the current item,
                // catch if either field is empty and move on.
                try {
                    final Books b = new Books();
                    b.setName(volumeInfo.getString("title"));
                    b.setAuthor(volumeInfo.getString("authors"));
                    b.setId(book.getString("id"));
                    arrayBooksList.add(b);
                    if(usecode==1)adapter.notifyDataSetChanged();
                    if(usecode==2)adapter_2.notifyDataSetChanged();

                } catch (Exception e){
                    e.printStackTrace();
                }

                // Move to the next item.
                i++;
            }

            // If both are found, display the result.
            if (title != null && authors != null){
               // mTitleText.setText(title);
               // mAuthorText.setText(authors);
                //mBookInput.setText("");
            } else {
                // If none are found, update the UI to show failed results.
               // mTitleText.setText(R.string.no_results);
              //  mAuthorText.setText("");
            }

        } catch (Exception e){
            // If onPostExecute does not receive a proper JSON string,
            // update the UI to show failed results.
            //mTitleText.setText(R.string.no_results);
           // mAuthorText.setText("");
            e.printStackTrace();
        }
    }
}