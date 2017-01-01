package aleksandrov.aleksandr.shopping;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

import aleksandrov.aleksandr.shopping.list_of_goods.Good;
import aleksandrov.aleksandr.shopping.list_of_reviews.Review;

/**
 * Created by aleksandr on 12/22/16.
 */

public class Communication {

    public boolean sendReview(long product_id, String token, int rate, String text) {
        boolean response = false;
        try {
            URL url = new URL(Res.PROTOCOL_SCHEME
                    + Res.URL
                    + Res.REVIEWS + product_id);

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            String postString = Res.RATE + "=" + rate + "&"
                    + Res.TEXT + "=" + URLEncoder.encode(text, "UTF-8");

            con.setRequestProperty("Authorization", "Token " + token);

            con.setDoOutput(true);
            OutputStream os = con.getOutputStream();
            os.write(postString.getBytes());
            os.flush();
            os.close();

            int responseCode = con.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer stringBuffer = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    stringBuffer.append(inputLine);
                }
                JSONObject jsonObjectUser = new JSONObject(stringBuffer.toString());
                response = jsonObjectUser.getBoolean(Res.SUCCESS);
                in.close();
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return response;
    }

    public User authorizeUser(String username, String password) {
        User user = null;
        try {
            URL url = new URL(Res.PROTOCOL_SCHEME
                    + Res.URL
                    + Res.LOGIN);

            URLConnection urlConnection = url.openConnection();
            String postString = URLEncoder.encode(Res.USER_NAME, "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8") + "&"
                    + URLEncoder.encode(Res.PASSWORD, "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
            urlConnection.setDoOutput(true);
//            urlConnection.setChunkedStreamingMode(0);

            OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());

            out.write(postString);
            out.close();

            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;
            while((line = reader.readLine()) != null) {
                JSONObject jsonObjectUser = new JSONObject(line.toString());
                if (jsonObjectUser.getBoolean(Res.SUCCESS)) {
                    user = new User(username, jsonObjectUser.getString(Res.TOKEN).toString());
                } else {
                    return null;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return user;
    }

    public User registerUser(String username, String password) {
        User user = null;
        try {
            URL url = new URL(Res.PROTOCOL_SCHEME
                    + Res.URL
                    + Res.REGISTER_USER);

            URLConnection urlConnection = url.openConnection();
            String postString = URLEncoder.encode(Res.USER_NAME, "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8") + "&"
                    + URLEncoder.encode(Res.PASSWORD, "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
            urlConnection.setDoOutput(true);
//            urlConnection.setChunkedStreamingMode(0);

            OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());

            out.write(postString);
            out.close();

            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;
            while((line = reader.readLine()) != null) {
                JSONObject jsonObjectUser = new JSONObject(line.toString());
                if (jsonObjectUser.getBoolean(Res.SUCCESS)) {
                    user = new User(username, jsonObjectUser.getString(Res.TOKEN).toString());
                } else {
                    return null;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return user;
    }

    public ArrayList<Good> getProducts() {
        ArrayList<Good> goodArrayList = new ArrayList<>();
        try {
            URL url = new URL(Res.PROTOCOL_SCHEME
                    + Res.URL
                    + Res.GET_PRODUCTS);

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                String line;
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
                StringBuffer result = new StringBuffer();
                while ((line = bufferedReader.readLine()) != null) {
                    result.append(line);
                }
                JSONArray jsonArray = new JSONArray(result.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Good good = new Good(jsonObject.getInt(Res.ID), jsonObject.getString(Res.TITLE), jsonObject.getString(Res.IMAGE), jsonObject.getString(Res.TEXT));
                    goodArrayList.add(good);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return goodArrayList;
    }

    public ArrayList<Review> getReviews(int product_id) {
        ArrayList<Review> reviewArrayList = new ArrayList<>();
        try {
            URL url = new URL(Res.PROTOCOL_SCHEME
                    + Res.URL
                    + Res.REVIEWS
                    + product_id);

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                String line;
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
                StringBuffer result = new StringBuffer();
                while ((line = bufferedReader.readLine()) != null) {
                    result.append(line);
                }
                JSONArray jsonArray = new JSONArray(result.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    JSONObject created_byObject = jsonObject.getJSONObject(Res.CREATED_BY);
                    Review review = new Review(jsonObject.getInt(Res.ID),
                            jsonObject.getInt(Res.PRODUCT),
                            jsonObject.getInt(Res.RATE),
                            jsonObject.getString(Res.TEXT),
                            created_byObject.getInt(Res.ID),
                            created_byObject.getString(Res.USER_NAME),
                            created_byObject.getString(Res.EMAIL),
                            jsonObject.getString(Res.CREATED_AT));
                    reviewArrayList.add(review);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return reviewArrayList;
    }
}
